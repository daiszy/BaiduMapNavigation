package com.example.gaoxixi.mapmutilnavigation.HttpService;

import com.example.gaoxixi.mapmutilnavigation.Activity.LoginActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoXixi on 2017/5/15.
 */

public class LoginService {

//    private static String URL = "http://10.102.4.160:8080/MapMutilNaviagtion/LoginServlet";
    private static String URL = "http://10.102.4.160:8080/MapMutilNaviagtion/LoginServlet";
    private HttpEntity httpEntity;

    public boolean HttpPost(final String username, final String password){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        try{
            List<NameValuePair> parms = new ArrayList<>();
            parms.add(new BasicNameValuePair("username",username));
            parms.add(new BasicNameValuePair("password",password));
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
                InputStream is  = httpEntity.getContent();
                byte[] data = LoginService.read(is);
                String acceptData = new String(data,"UTF-8");
                if("登录成功".equals(acceptData.toString().trim()))
                {
                    return true;
                }else {
                    return false;
                }
            }else {
                httpPost.abort();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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


/*String path = URL1 + "?username="+username+"&password="+password;
                    HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charset","UTF-8");
                    int staus = conn.getResponseCode();
                    if(staus == 200)
                    {
                       InputStream is = conn.getInputStream();
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }*/

/*try {
                    HttpURLConnection conn = null;
                    String path = URL + "?username=" + username + "&password=" + password;
                    conn = (HttpURLConnection) new URL(path).openConnection();
                    conn.setConnectTimeout(10000); // 设置超时时间
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST"); // 设置获取信息方式
                    conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

                    if (conn.getResponseCode() == 200) {
                        result = true;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }*/