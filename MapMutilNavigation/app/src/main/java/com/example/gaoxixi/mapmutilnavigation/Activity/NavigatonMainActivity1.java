package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.gaoxixi.mapmutilnavigation.NavigationMainActivity;
import com.example.gaoxixi.mapmutilnavigation.R;
import com.example.gaoxixi.mapmutilnavigation.util.BikingRouteOverlay;
import com.example.gaoxixi.mapmutilnavigation.util.OverlayManager;
import com.example.gaoxixi.mapmutilnavigation.util.TsTSP;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NavigatonMainActivity1 extends AppCompatActivity implements OnGetRoutePlanResultListener {

    MapView mapView = null;
    BaiduMap baiduMap;
    OverlayManager routeOverlay = null;

    RoutePlanSearch mSearch;
    BikingRouteResult nowResultbike = null;
    RouteLine route;

    //定义坐标点
    GeoPoint startPoint;
    GeoPoint endPoint;

    ArrayList<String> arrayList = new ArrayList<>();   //存放listView的地点名称
    List<GeoPoint> middlePoints = new ArrayList<>();   //存放listView的经纬度值
    List<LatLng> middlePt = new ArrayList<>();         //存放listview的坐标点
    List<PlanNode> middleNode = new ArrayList<>();

    public List pathOrder = new ArrayList();

    private final static double PI = Math.PI; // 圆周率
    private final static double earthR = 6371.229; // 地球的半径
    Double[][] Distance = new Double[10][10];   //存放距离的数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_navigation_main);

        CharSequence title = "路线规划功能";
        setTitle(title);

       /**获取地图控件引用*/
        mapView = (MapView) findViewById(R.id.navigationMapView);
        baiduMap = mapView.getMap();

        mSearch = RoutePlanSearch.newInstance();

        Intent intent = getIntent();
        String startAddress = intent.getStringExtra("startAddress");
        String endAddress = intent.getStringExtra("endAddress");
        arrayList = intent.getStringArrayListExtra("middleAddress");

        /**根据名称获取经纬度值*/
        startPoint=getGeoPointByStr(startAddress);
        endPoint=getGeoPointByStr(endAddress);
        middlePoints.add(startPoint);
        for(int i = 0; i < arrayList.size(); i++)
        {
            middlePoints.add(getGeoPointByStr(arrayList.get(i)));
        }
        middlePoints.add(endPoint);
        /**计算每两个点之间的距离*/
        for(int i = 0;i < middlePoints.size()-1; i++)
        {
            Distance[0][i+1] = getDistance(startPoint.getLongitudeE6(),startPoint.getLatitudeE6(),
                    middlePoints.get(i+1).getLongitudeE6(),middlePoints.get(i+1).getLatitudeE6());
            Distance[i+1][0] = Distance[0][i+1];
        }
        for(int i = 0;i < middlePoints.size()-1; i++)
        {
            for(int j = i+1 ; j < middlePoints.size()-1; j++)
            {
                Distance[i+1][j+1] = getDistance(middlePoints.get(i+1).getLongitudeE6(),middlePoints.get(i+1).getLatitudeE6(),
                        middlePoints.get(j+1).getLongitudeE6(),middlePoints.get(j+1).getLatitudeE6());
                Distance[j+1][i+1] = Distance[i+1][j+1];
            }
        }

        /**规划最短路线，得到访问的目标点顺序*/

        try
        {
            TsTSP ts = new TsTSP(middlePoints.size(),Distance);
            ts.init();
            ts.solve();
            pathOrder = ts.pathOrder;
        }catch (Exception e) {
            e.printStackTrace();
        }

        /**定义起始与目标坐标点*/
        LatLng startPt = new LatLng(startPoint.getLatitudeE6(),startPoint.getLongitudeE6());   //西安理工大学
        LatLng endPt = new LatLng(endPoint.getLatitudeE6(),endPoint.getLongitudeE6());   //火车站
        PlanNode stNode = PlanNode.withLocation(startPt);
        PlanNode enNode = PlanNode.withLocation(endPt);
        for(int i = 0; i < middlePoints.size()-1; i++)
        {
            middlePt.add(new LatLng(middlePoints.get(Integer.parseInt(pathOrder.get(i+1).toString())).getLatitudeE6(),middlePoints.get(Integer.parseInt(pathOrder.get(i+1).toString())).getLongitudeE6()));
        }
        for(int i = 0; i < middlePoints.size()-1; i++)
        {
            middleNode.add(PlanNode.withLocation(middlePt.get(i)));
        }

//        mSearch.bikingSearch((new BikingRoutePlanOption())
//                .from(stNode).to(enNode));

        mSearch.setOnGetRoutePlanResultListener(this);
        /**循环添加多个点*/
//        PlanNode midd = middleNode.get(0);
//        mSearch.bikingSearch((new BikingRoutePlanOption())
//                    .from(stNode).to(midd));
        PlanNode midd = middleNode.get(0);
        mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(midd));
        for(int i = 0; i < middlePoints.size()-2; i++)
        {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(middleNode.get(i)).to(middleNode.get(i+1)));
        }

    }

    /**根据名称获得经纬度*/
    private GeoPoint getGeoPointByStr(String str) {
        GeoPoint gpGeoPoint = null;
        if(str != null)
        {
            Geocoder geoCoder = new Geocoder(NavigatonMainActivity1.this, Locale.CHINA);
            List<Address> addressList = null;
            try {
                addressList = geoCoder.getFromLocationName(str,2);
                if(!addressList.isEmpty())
                {
                    Address address_temp = addressList.get(0);
                    //计算经纬度
                    double latitude = address_temp.getLatitude();
                    double longtitude = address_temp.getLongitude();

                    gpGeoPoint = new GeoPoint(latitude,longtitude);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return gpGeoPoint;
    }

    /**根据经纬度计算两点之间的距离*/
    private Double getDistance(double longt1, double lat1, double longt2,double lat2) {
        double x, y, distance;
        x = (longt2 - longt1)*PI*earthR*Math.cos(((lat1+lat2)/2)*PI/180)/180;
        y = (lat2 - lat1)*PI*earthR/180;
        distance = Math.hypot(x, y);
        return distance;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigatonMainActivity1.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo();
            Toast.makeText(NavigatonMainActivity1.this, "歧义", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigatonMainActivity1.this, "成功", Toast.LENGTH_SHORT).show();
            route = result.getRouteLines().get(0);
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
        else
        {
            Toast.makeText(NavigatonMainActivity1.this, "其他", Toast.LENGTH_SHORT).show();
        }
    }
    private class MyBikingRouteOverlay extends BikingRouteOverlay
    {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.position_marker);
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.position_marker);
        }
    }

    //实现地图生命周期管理
    public void onDestroy()
    {
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
}
