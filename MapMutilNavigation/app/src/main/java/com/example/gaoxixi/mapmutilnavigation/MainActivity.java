package com.example.gaoxixi.mapmutilnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {

    MapView mapView = null;
    BaiduMap baiduMap;

    //定位相关
    LocationClient locationClient;
    private LocationMode mCurrentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();
        //开启定位
        setMyLocation();

        //获取底部导航栏按钮
        TextView BaseMap = (TextView) findViewById(R.id.app_bar_index);
        TextView MutilMap = (TextView) findViewById(R.id.app_bar_mutilNavigation);
        TextView PersonInfo = (TextView) findViewById(R.id.app_bar_personInfo);
        //为导航栏按钮设置监听事件
        BaseMap.setOnClickListener(new MyClickListenerBaseMap());
        MutilMap.setOnClickListener(new MyClickListenerMutilMap());
        PersonInfo.setOnClickListener(new MyClickListenerPersonInfo());
    }
    public void initView()
    {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();

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
        option.setScanSpan(1000);    //每隔一秒刷新一次
        mLocClient.setLocOption(option);
        mLocClient.start();
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
