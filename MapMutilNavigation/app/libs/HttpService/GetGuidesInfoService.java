//package com.example.tian.tourguideproject.com.example.HttpService;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//import com.example.tian.tourguideproject.com.example.bean.SimpleGuideInfoListItem;
//import com.example.tian.tourguideproject.com.example.utils.ImageTools;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by tian on 2016/11/29.
// * 从服务端获取导游的信息
// */
//
//public class GetGuidesInfoService {
//
//    public static final String BASE_URL = "http://10.50.63.62:8080/SpringMe/getguideinfo.do";
//
//    public static final String BASE_URL_2 = "http://10.50.63.62:8080/SpringMe/getSelectedGuideInfo.do";
//
//    String result;
//
//    /**
//     * 返回所有的导游信息
//     * @return
//     */
//    public List<SimpleGuideInfoListItem> getAllGuides(){
//
//        List<SimpleGuideInfoListItem> guideList = new ArrayList<>();
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(BASE_URL);
//
//        try{
//
//            HttpResponse response = httpClient.execute(httpPost);
//            int ret = response.getStatusLine().getStatusCode();
//
//            if(ret == 200){
//
//                HttpEntity httpEntity = response.getEntity();
//                result = EntityUtils.toString(httpEntity);
//
//                guideList = guideInfoJsonTool(result);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return guideList;
//    }
//
//    /**
//     * 返回符合条件的导游信息
//     * @return
//     */
//    public List<SimpleGuideInfoListItem> getSelectedGuides(final List<NameValuePair> params){
//
//        List<SimpleGuideInfoListItem> guideList = new ArrayList<>();
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(BASE_URL_2);
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
//                guideList = guideInfoJsonTool(result);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return guideList;
//    }
//
//    /**
//     * 解析服务端返回的数据
//     * @param str
//     */
//    public List<SimpleGuideInfoListItem> guideInfoJsonTool(String str){
//
//        List<SimpleGuideInfoListItem> guideList = new ArrayList<>();
//
//        ImageTools imageTools = new ImageTools();
//        byte[] byteImage = null;
//
//        try {
//            JSONArray jsonArray = new JSONArray(str);
//
//            for (int i = 0; i < jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                String guideImagePath = jsonObject.getString("guideImage");
//                String guideName = jsonObject.getString("guideName");
//                String guideWorkAge = jsonObject.getString("guideWorkAge");
//                String guideIntro = jsonObject.getString("guideIntro");
//                String guidePrice = jsonObject.getString("guidePrice");
//                String guideNumID = jsonObject.getString("guideNumID");
//
//                try {
//                    byteImage = imageTools.getImage(guideImagePath); //将网络图片转为二进制格式
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0,
//                        byteImage.length);//生成位图
//                bitmap = imageTools.settingImage(bitmap);
//                Drawable guideImage = new BitmapDrawable(bitmap);
//
//                SimpleGuideInfoListItem guideInfo = new SimpleGuideInfoListItem(guideImage,
//                        guideName, guideWorkAge, guideIntro, guidePrice, guideNumID);
//
//                guideList.add(guideInfo);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return guideList;
//    }
//
//
//}
