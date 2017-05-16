package com.example.tian.tourguideproject.com.example.HttpService;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by 高茜茜09 on 2016/11/30.
 */
public class RecordMyLocationService {
    private static String URL = "http://10.50.63.62:8080/SpringMe/UpdateLocOfVisitor.do";

    private boolean result = false;


    public boolean HttpPost(final List<NameValuePair> pairList){

        Thread thread=new Thread(new Runnable() {

            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URL);

                try{
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
                    httpPost.setEntity(entity);

                    HttpResponse response = httpClient.execute(httpPost);
                    int ret = response.getStatusLine().getStatusCode();

                    if(ret == 200){

                        result = true;
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        Log.e("RecoerLoc", "thread start....");
        Log.e("resulr", String.valueOf(result));

        return result;
    }
}
