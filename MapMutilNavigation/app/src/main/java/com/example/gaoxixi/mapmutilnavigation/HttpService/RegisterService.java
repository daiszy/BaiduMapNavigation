package com.example.gaoxixi.mapmutilnavigation.HttpService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoXixi on 2017/5/22.
 */

public class RegisterService {
    private static String URL = "http://10.102.4.160:8080/MapMutilNaviagtion/RegisterServlet";
    private HttpEntity httpEntity;
    public  boolean HttpPost(final String nickName, final String name, final String sex, final String telphone, final String ofCity, final String pasword) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        try {
            List<NameValuePair> parms = new ArrayList<>();
            parms.add(new BasicNameValuePair("nickName", nickName));
            parms.add(new BasicNameValuePair("name", name));
            parms.add(new BasicNameValuePair("sex", sex));
            parms.add(new BasicNameValuePair("telphone", telphone));
            parms.add(new BasicNameValuePair("ofCity", ofCity));
            parms.add(new BasicNameValuePair("password", pasword));
            //设置请求参数
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parms, "UTF-8");
            httpPost.setEntity(entity);

           // HttpGet httpGet = new HttpGet(URL+"?"+parms);

            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

            //发送POST请求
            HttpResponse response = httpClient.execute(httpPost);
            int ret = response.getStatusLine().getStatusCode();
            if (ret == 200) {
                httpEntity = response.getEntity();
                String value = EntityUtils.toString(httpEntity,"utf-8");
                if("true".equals(value)){
                    return true;
                }
            } else {
                httpPost.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 将输入流转化为byte型
    public static byte[] read1(InputStream inStream) throws Exception {
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
