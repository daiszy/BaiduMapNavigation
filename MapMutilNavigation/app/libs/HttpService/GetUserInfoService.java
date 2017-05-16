//package com.example.tian.tourguideproject.com.example.HttpService;
//
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//import com.example.tian.tourguideproject.com.example.bean.SimpleUserInfoListItem;
//import com.example.tian.tourguideproject.com.example.utils.ImageTools;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by tian on 2016/11/23.
// * 从服务端获取用户的信息
// * 并按一定的格式返回
// */
//
//public class GetUserInfoService {
//
//    public static final String BASE_URL = "http://10.50.63.62:8080/SpringMe/getphonenumber.do";
//
//    private String result = null;
//    List<SimpleUserInfoListItem> userinfoList = new ArrayList<>();
//
//
//    public  List<SimpleUserInfoListItem> executeHttpPost(final String userTel){
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(BASE_URL);
//
//        List <NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("userTel", userTel));
//
//        try{
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//            httpPost.setEntity(entity);
//
//            HttpResponse response = httpClient.execute(httpPost);
//            int ret = response.getStatusLine().getStatusCode();
//
//            if(ret == 200){
//
//                HttpEntity httpEntity = response.getEntity();
//                result = EntityUtils.toString(httpEntity);
//
//                userinfoList = userInfoJsonTool(result);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return userinfoList;
//    }
//
//    /**
//     * 解析服务端返回的json
//     */
//    public List<SimpleUserInfoListItem> userInfoJsonTool(String str){
//
//        byte[] data = null;
//        ImageTools imageTools = new ImageTools();
//
//        try {
//            JSONObject jsonObject = new JSONObject(str);
//            String image = jsonObject.getString("userImage");
//            String name = jsonObject.getString("userName");
//            String nickName = jsonObject.getString("userNickName");
//            String phone = jsonObject.getString("userTel");
//            String sex = jsonObject.getString("userSex");
//
//            try {
//                data = imageTools.getImage(image);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//生成位图
//            bitmap = imageTools.settingImage(bitmap);
//            Drawable drawable = new BitmapDrawable(bitmap);
//
//            SimpleUserInfoListItem userImage = new SimpleUserInfoListItem("头像", drawable);
//            userinfoList.add(userImage);
//            SimpleUserInfoListItem userName = new SimpleUserInfoListItem("姓名", name);
//            userinfoList.add(userName);
//            SimpleUserInfoListItem userNickname = new SimpleUserInfoListItem("昵称", nickName);
//            userinfoList.add(userNickname);
//            SimpleUserInfoListItem userTel = new SimpleUserInfoListItem("手机号", phone);
//            userinfoList.add(userTel);
//            SimpleUserInfoListItem userSex = new SimpleUserInfoListItem("性别", sex);
//            userinfoList.add(userSex);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return userinfoList;
//    }
//
//}
