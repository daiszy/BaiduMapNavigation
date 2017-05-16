//package com.example.tian.tourguideproject.com.example.HttpService;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//import com.example.tian.tourguideproject.com.example.bean.DetailGuideInfo;
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
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by tian on 2016/12/1.
// */
//
//public class GetDetailGuideInfoService {
//
//    public static final String BASE_URL = "http://10.50.63.62:8080/SpringMe/getGuideNumID.do";
//
//    private DetailGuideInfo guideInfo;
//    private String result;
//
//    public DetailGuideInfo getDetailGuideInfo(final List<NameValuePair> params){
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(BASE_URL);
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
//                guideInfo = guideInfoJsonTool(result);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return guideInfo;
//    }
//
//    /**
//     * 解析服务端返回的数据
//     * @param str
//     */
//    public DetailGuideInfo guideInfoJsonTool(String str){
//
//        ImageTools imageTools = new ImageTools();
//        byte[] byteImage = null;
//
//        try {
//
//            JSONObject jsonObject = new JSONObject(str);
//
//            String guideImagePath = jsonObject.getString("guideImage");
//            String guideName = jsonObject.getString("guideName");
//            String guideWorkAge = jsonObject.getString("guideWorkAge");
//            String guideIntro = jsonObject.getString("guideIntro");
//            String guidePrice = jsonObject.getString("guidePrice");
//            String guideNumID = jsonObject.getString("guideNumID");
//            String guideLevel = jsonObject.getString("guideLevel");
//            String guideSex = jsonObject.getString("guideSex");
//            String guideAge = jsonObject.getString("guideAge");
//            String guideLanguage = jsonObject.getString("guideLanguage");
//            String guideCertificateID = jsonObject.getString("guideCertificateID");
//
//            try {
//                byteImage = imageTools.getImage(guideImagePath); //将网络图片转为二进制格式
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0,
//                    byteImage.length);//生成位图
//            bitmap = imageTools.settingImage(bitmap);
//            Drawable guideImage = new BitmapDrawable(bitmap);
//
//            guideInfo = new DetailGuideInfo(guideAge, guideSex, guideLevel,
//                    guideCertificateID, guideImage, guideName, guideWorkAge, guideIntro,
//                    guidePrice, guideNumID, guideLanguage);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return guideInfo;
//    }
//}
