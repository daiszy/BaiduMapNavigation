package com.example.gaoxixi.mapmutilnavigation;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLauchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.gaoxixi.mapmutilnavigation.util.BikingRouteOverlay;
import com.example.gaoxixi.mapmutilnavigation.util.OverlayManager;
import com.example.gaoxixi.mapmutilnavigation.util.TsTSP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NavigationMainActivity extends AppCompatActivity{

    //地图相关
    MapView mapView = null;
    BaiduMap baiduMap = null;

    //骑车导航相关
    private BikeNavigateHelper bikeNavigateHelper;
    BikeNaviLauchParam parm;
    private static boolean isPermissionRequested = false;
    private BikeNavigateHelper mNaviHelper;

    BikingRouteResult nowResultBike;

    //搜索相关
    RoutePlanSearch mSearch;
    OverlayManager routeOverlay = null;

    //定义坐标点
    GeoPoint startPoint;
    GeoPoint endPoint;

    ArrayList<String> arrayList = new ArrayList<>();   //存放listView的地点名称
    List<GeoPoint> middlePoints = new ArrayList<>();   //存放listView的经纬度值

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

        requestPermission();
        initView();

        Intent intent = getIntent();
        String startAddress = intent.getStringExtra("startAddress");
        String endAddress = intent.getStringExtra("endAddress");
        arrayList = intent.getStringArrayListExtra("middleAddress");

        /**根据名称获取经纬度值*/
        startPoint=getGeoPointByStr(startAddress);
        endPoint=getGeoPointByStr(endAddress);
        for(int i = 0; i < arrayList.size(); i++)
        {
            middlePoints.add(getGeoPointByStr(arrayList.get(i)));
        }
        middlePoints.add(endPoint);

        /**计算每两个点之间的距离*/
        for(int i = 0;i < middlePoints.size(); i++)
        {
            Distance[0][i+1] = getDistance(startPoint.getLongitudeE6(),startPoint.getLatitudeE6(),
                    middlePoints.get(i).getLongitudeE6(),middlePoints.get(i).getLatitudeE6());
            Distance[i+1][0] = Distance[0][i+1];
        }
        for(int i = 0;i < middlePoints.size(); i++)
        {
            for(int j = i+1 ; j < middlePoints.size(); j++)
            {
                Distance[i+1][j+1] = getDistance(middlePoints.get(i).getLongitudeE6(),middlePoints.get(i).getLatitudeE6(),
                        middlePoints.get(j).getLongitudeE6(),middlePoints.get(j).getLatitudeE6());
                Distance[j+1][i+1] = Distance[i+1][j+1];
            }
        }

        /**规划最短路线，得到访问的目标点顺序*/
        try {
            TsTSP ts = new TsTSP(middlePoints.size(),Distance);
            ts.init();
            ts.solve();
        }catch (Exception e) {
            e.printStackTrace();
        }

        startBikeNavi();

        /**定义起始与目标坐标点*/
        LatLng startPt = new LatLng(startPoint.getLatitudeE6(),startPoint.getLongitudeE6());   //西安理工大学
        LatLng endPt = new LatLng(endPoint.getLatitudeE6(),endPoint.getLongitudeE6());   //火车站

        parm = new BikeNaviLauchParam().stPt(startPt).endPt(endPt);

    }


    /**根据经纬度计算两点之间的距离*/
    private Double getDistance(double longt1, double lat1, double longt2,double lat2) {
        double x, y, distance;
        x = (longt2 - longt1)*PI*earthR*Math.cos(((lat1+lat2)/2)*PI/180)/180;
        y = (lat2 - lat1)*PI*earthR/180;
        distance = Math.hypot(x, y);
        return distance;
    }

    /**根据名称获得经纬度*/
    private GeoPoint getGeoPointByStr(String str) {
        GeoPoint gpGeoPoint = null;
        if(str != null)
        {
            Geocoder geoCoder = new Geocoder(NavigationMainActivity.this, Locale.CHINA);
            List<Address> addressList = null;
            try {
                addressList = geoCoder.getFromLocationName(str,1);
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

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= 23 && !isPermissionRequested)
        {
            isPermissionRequested = true;

            ArrayList<String> permissions = new ArrayList<>();
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }

    private void initView() {
        /**获取地图控件引用*/
        mapView = (MapView) findViewById(R.id.navigationMapView);
        baiduMap = mapView.getMap();
    }

    private void startBikeNavi() {
        Log.d("View","开始骑车导航引擎初始化");
        Toast.makeText(NavigationMainActivity.this,"开始骑车导航引擎初始化",Toast.LENGTH_SHORT).show();
        bikeNavigateHelper = BikeNavigateHelper.getInstance() ;
        try
        {
            bikeNavigateHelper.initNaviEngine(this, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
//                Log.d("View","engineInitSuccess");
                    Toast.makeText(NavigationMainActivity.this,"导航引擎初始化成功",Toast.LENGTH_SHORT).show();
                    routePlanWithParm();
                }
                @Override
                public void engineInitFail() {
                    //Log.d("View","engineInitFail");
                    Toast.makeText(NavigationMainActivity.this,"导航引擎初始化失败",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void routePlanWithParm() {
        bikeNavigateHelper.routePlanWithParams(parm, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("View","onRoutePlanStart");
                Toast.makeText(NavigationMainActivity.this,"算路开始",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View","onRoutePlanSuccess");
                Toast.makeText(NavigationMainActivity.this,"算路成功",Toast.LENGTH_SHORT).show();

                mNaviHelper = BikeNavigateHelper.getInstance();

                View view = mNaviHelper.onCreate(NavigationMainActivity.this);
                if (view != null) {
                    setContentView(view);
                }
                mNaviHelper.startBikeNavi(NavigationMainActivity.this);
            }
            @Override
            public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
                Log.d("View","onRoutePlanFail");
                Toast.makeText(NavigationMainActivity.this,"算路失败",Toast.LENGTH_SHORT).show();
            }
        });
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
