package com.example.gaoxixi.mapmutilnavigation.HttpService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by GaoXixi on 2017/5/17.
 */

public class GetUserInfoService {
    private static String URL = "http://10.102.4.160:8080/MapMutilNaviagtion/GetUserInfoServlet";
    private HttpEntity httpEntity;
    String result;

    public Map<String,Object> HttpPost(final String username){
        Map<String, Object> map = new HashMap<String,Object>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        try{
            List<NameValuePair> parms = new ArrayList<>();
            parms.add(new BasicNameValuePair("username",username));
            //设置请求参数
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parms,"UTF-8");
            httpPost.setEntity(entity);
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

            //发送POST请求
            HttpResponse response = httpClient.execute(httpPost);
            int ret = response.getStatusLine().getStatusCode();
            if(ret == 200)
            {
                httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                //解析数据
                map = userInfoJsonTool(result);
            }else {
                httpPost.abort();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Object> userInfoJsonTool(String result){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nickName = jsonObject.getString("nickName");
                String name = jsonObject.getString("name");
                String sex = jsonObject.getString("sex");
                String ofCity = jsonObject.getString("ofCity");
                String telphone = jsonObject.getString("telphone");
                String status = jsonObject.getString("status");
                String grade = jsonObject.getString("grade");
                String ordersNum = jsonObject.getString("ordersNum");
                map.put("nickName",nickName);
                map.put("name",name);
                map.put("sex",sex);
                map.put("ofCity",ofCity);
                map.put("telphone",telphone);
                map.put("status",status);
                map.put("grade",grade);
                map.put("ordersNum",ordersNum);
                return map;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
