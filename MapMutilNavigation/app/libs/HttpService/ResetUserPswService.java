package com.example.tian.tourguideproject.com.example.HttpService;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tian on 2016/11/23.
 */

public class ResetUserPswService {

    private static String BASE_URL = "http://10.50.63.62:8080/SpringMe/updatephonenumber.do";

    public  Boolean executeHttpGet(String phone, String password){

        HttpURLConnection conn = null;

        try {

            String path = BASE_URL + "?userPhone=" + phone + "&userPsw=" + password;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }
}
