package com.example.tian.tourguideproject.com.example.HttpService;

import com.example.tian.tourguideproject.com.example.bean.Location;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高茜茜09 on 2016/12/2.
 */

public class getLocationService {
    private static String URL = "http://10.50.63.62:8080/SpringMe/getLocationofvisitors.do";
    List<Location> locations = new ArrayList<Location>();

    public List<Location> HttpPost(final List<NameValuePair> pairList)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            int ret = response.getStatusLine().getStatusCode();

            if(ret == 200)
            {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                /**解析服务器端的经纬度值*/
                jsonMyLocation(result);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return locations;
    }
    /*解析服务器端获取到的经纬度值*/
    public void jsonMyLocation(String result)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                locations.add(new Location(latitude,longitude));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
