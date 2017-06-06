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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoXixi on 2017/5/24.
 */

public class RecordLocationService {
    private static String URL = "http://10.102.4.160:8080/MapMutilNaviagtion/recordMyLocationServlet";
    private HttpEntity httpEntity;

    public boolean httpPost(String telphone,String latitude,String longitude){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        List<NameValuePair> parms = new ArrayList<>();
        parms.add(new BasicNameValuePair("telphone",telphone));
        parms.add(new BasicNameValuePair("latitude",latitude));
        parms.add(new BasicNameValuePair("longitude",longitude));

        //设置请求参数
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(parms, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity);

        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

        //发送POST请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int ret = response.getStatusLine().getStatusCode();
        if(ret == 200){
            httpEntity = response.getEntity();

            String value = null;
            try {
                value = EntityUtils.toString(httpEntity,"utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if("true".equals(value)){
                return true;
            }
        }else {
            httpPost.abort();
        }
        return false;
    }
}
