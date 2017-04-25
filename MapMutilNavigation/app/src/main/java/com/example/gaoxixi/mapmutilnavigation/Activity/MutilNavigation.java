package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.gaoxixi.mapmutilnavigation.Adapter.mutilNavigation;
import com.example.gaoxixi.mapmutilnavigation.MainActivity;
import com.example.gaoxixi.mapmutilnavigation.NavigationMainActivity;
import com.example.gaoxixi.mapmutilnavigation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutilNavigation extends Activity implements View.OnClickListener,mutilNavigation.Callback{

    private ListView listView;
    private ImageView imageViewAddItem;
    private Button mutilNavigationButton;
    private mutilNavigation mutilNavigationAdapter;

    GeoCoder geoCoder;

    //创建一个list集合，集合元素是Map
    final List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

    //定义item中用到的信息
    private int positionIcon  = R.drawable.position_icon;
    private String[] positionName = {"省图书馆","省体育场"};
    final String positionName1 = " ";
    private int positionDelete = R.drawable.delete1;

    EditText startAddressET,endAddressET,middleAddressET;
    ArrayList<String> listArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutil_navigation);

        /**初始化控件*/
        initView();

        /**为listView布局填充信息*/
        addInfo();

        /**通过自定义Adapter显示Adapter*/
        mutilNavigationAdapter = new mutilNavigation(MutilNavigation.this,listItems,this);
        listView.setAdapter(mutilNavigationAdapter);

        /**定义增加一条Item的事件监听器*/
        imageViewAddItem.setOnClickListener(new MyClickListenerAddItem());

        /**为导航按钮设置监听事件*/
        mutilNavigationButton.setOnClickListener(new MyClickListenerNavition());

        System.out.println(mutilNavigationAdapter.getCount());
    }

    /**初始化控件*/
    private void initView() {
        listView = (ListView) findViewById(R.id.mutil_middle_list);
        imageViewAddItem = (ImageView) findViewById(R.id.mutil_end_add);
        mutilNavigationButton = (Button) findViewById(R.id.mutil_navigation_button);
        geoCoder = GeoCoder.newInstance();
        startAddressET = (EditText) findViewById(R.id.mutil_start_text);
        endAddressET = (EditText) findViewById(R.id.mutil_end_text);
    }

    /**为ListView填充信息*/
    private void addInfo() {
        for(int i = 0 ; i < positionName.length ; i++)
        {
           /**为listItem填充信息*/
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("positionIcon",positionIcon);
            listItem.put("positionName",positionName[i]);
            listItem.put("positionDelete",positionDelete);
            listItems.add(listItem);
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 接口方法，响应ListView内部的按钮点击事件
     */
    @Override
    public void clickInListview(View view) {
        if(view.getId() == R.id.mutil_middle_delete)
        {
            /**
             *删除该item*/
            deleteItem(view);
        }
    }

    /**
     * 删除一条Item记录*/
    private void deleteItem(View view) {
        int position;
        position =  Integer.parseInt(view.getTag().toString());
        listItems.remove(position);
        mutilNavigationAdapter.notifyDataSetChanged();
    }

    /**增加一条Item的事件监听器*/
    class MyClickListenerAddItem implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /**增加一条Item*/
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("positionIcon",positionIcon);
            listItem.put("positionName",positionName1);
            listItem.put("positionDelete",positionDelete);
            listItems.add(listItem);
            mutilNavigationAdapter.notifyDataSetChanged();
        }
    }

    /**导航按钮监听器*/
    class MyClickListenerNavition implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

            mutilNavigationAdapter.notifyDataSetChanged();
            int size = mutilNavigationAdapter.getCount();
            int sizeList = listView.getChildCount();
            /**循环获取输入的中间点*/
            for(int i = 0;i < sizeList; i++){
                LinearLayout layout = (LinearLayout) listView.getChildAt(i);
                EditText et = (EditText) layout.getChildAt(1);
                listArray.add(et.getText().toString().trim());
            }
            /**获取输入的起点与目标点*/
            String startAddress = startAddressET.getText().toString().trim();
            String endAddress = endAddressET.getText().toString().trim();

            Intent intent = new Intent(MutilNavigation.this, NavigationMainActivity.class);
            intent.putExtra("startAddress",startAddress);
            intent.putExtra("endAddress",endAddress);
            intent.putStringArrayListExtra("middleAddress",listArray);
            startActivity(intent);
        }
    }
}
