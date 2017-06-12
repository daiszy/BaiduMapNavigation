package com.example.gaoxixi.mapmutilnavigation.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.example.gaoxixi.mapmutilnavigation.Activity.AsignedOrderActivity;
import com.example.gaoxixi.mapmutilnavigation.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by GaoXixi on 2017/6/5.
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {

        Toast.makeText(context,customContentString,Toast.LENGTH_SHORT).show();
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            Map<String,Object> map = new HashMap<String,Object>();
          //  lists = StringToList(customContentString);
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String detail = customJson.getString("Detail");
                String latitude = customJson.getString("Latitude");
                String longitude = customJson.getString("Longitude");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //Intent intent = new Intent(context, AsignedOrderActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }


//    public static List<Map<String,Object>> StringToList(String listText){
//        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
//        listText = listText.substring(1);
//
//        listText = listText;
//
//        String[] text = listText.split("#");
//        for(String str : text){
//            if(str.charAt(0) == 'M'){
//                Map<String,Object> map = StringToMap(str);
//                lists.add(map);
//            }else if (str.charAt(0) == 'L'){
//                List<Map<String,Object>> list = StringToList(str);
//                lists.add(list);
//            }else {
//                lists.add(str);
//            }
//        }
//
//
//        return lists;
//    }
}
