package com.example.gaoxixi.mapmutilnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.gaoxixi.mapmutilnavigation.Activity.MutilNavigation;

public class MainActivity extends AppCompatActivity implements OnGetGeoCoderResultListener {

    MapView mapView = null;
    BaiduMap baiduMap;

    //定位相关
    LocationClient locationClient;
    private LocationMode mCurrentMode;

    /**底部导航栏按钮*/
    TextView BaseMap;    //首页
    TextView MutilMap;   //多点导航
    TextView PersonInfo;  //我的

    /**搜索地点*/
    private SearchView svPosition;
    GeoCoder mSearch = null;

    /**覆盖物*/
    OverlayOptions options;
    BitmapDescriptor bitmap;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        /**初始化控件*/
        initView();

        /**为搜索框设置信息*/
        setSearchInfo();

        mSearch.setOnGetGeoCodeResultListener(this);

        /**开启定位*/
        setMyLocation();

        /**为导航栏按钮设置监听事件*/
        BaseMap.setOnClickListener(new MyClickListenerBaseMap());
        MutilMap.setOnClickListener(new MyClickListenerMutilMap());
        PersonInfo.setOnClickListener(new MyClickListenerPersonInfo());
    }

    private void setSearchInfo() {
        svPosition.setIconified(false);
        svPosition.setSubmitButtonEnabled(true);
        svPosition.setQueryHint("请输入要搜索的地点");
        svPosition.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String svText) {
                mSearch.geocode(new GeoCodeOption()
                        .city("西安")
                        .address(svText));
                return true;
            }

            //用户输入字符串时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void initView()
    {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        //获取底部导航栏按钮
        BaseMap = (TextView) findViewById(R.id.app_bar_index);
        MutilMap = (TextView) findViewById(R.id.app_bar_mutilNavigation);
        PersonInfo = (TextView) findViewById(R.id.app_bar_personInfo);
        svPosition = (SearchView) findViewById(R.id.search_position);
        mSearch = GeoCoder.newInstance();
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.position_marker);
    }

    //定位函数
    public void setMyLocation()
    {
        //开启定位
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode,true,null);
        baiduMap.setMyLocationConfigeration(config);
        MyLocationListenner myListener = new MyLocationListenner();

        LocationClient mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);    //每隔一秒刷新一次
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        LatLng latLng = new LatLng(result.getLocation().latitude,result.getLocation().longitude);
        options = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .zIndex(9);
        marker = (Marker) baiduMap.addOverlay(options);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(16.0f);

        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    //定位SDK监听函数
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
             builder.target(ll).zoom(16.0f);

            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    //定义单击首页的监听器
    class MyClickListenerBaseMap implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

        }
    }
    //定义单击路线规划的监听器
    class MyClickListenerMutilMap implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

            //创建启动多点导航Activity对应的Intent
            Intent intent = new Intent(MainActivity.this,MutilNavigation.class);
            startActivity(intent);
        }
    }
    //定义单击个人信息的监听器
    class MyClickListenerPersonInfo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

        }
    }

    //实现地图生命周期管理
    public void onDestroy()
    {
        // 退出时销毁定位
        locationClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
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
