package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gaoxixi.mapmutilnavigation.HttpService.GetUserInfoService;
import com.example.gaoxixi.mapmutilnavigation.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsignedOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asigned_order);

        CharSequence title = "获得分配的订单";
        setTitle(title);

        //接收订单信息
        ArrayList<Map<String,Object>> list = (ArrayList<Map<String,Object>>)getIntent().getSerializableExtra("infoList");

        Map<String,Object> map= new HashMap<String,Object>();

        try{
            JSONArray jsonArray = new JSONArray(list);
            for(int i =0 ;i < jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String latitude = jsonObject.getString("Latitude");
                String longitude = jsonObject.getString("Longitude");
            }
        }catch (Exception e){

        }

    }
}
