package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.gaoxixi.mapmutilnavigation.HttpService.GetUserInfoService;
import com.example.gaoxixi.mapmutilnavigation.R;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private TextView nickNameText;
    private TextView nameText;
    private TextView sexText;
    private TextView telphoneText;
    private TextView ofCityText;
    private TextView statusText;
    private TextView gradeText;
    private TextView ordersNumText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info2);

        CharSequence title = "个人信息";
        setTitle(title);

        initView();

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        //加载个人信息
        updateUserInfo(username);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //加载个人信息
//                updateUserInfo(username);
//            }});
//        thread.start();

    }

    private void initView() {
        nickNameText = (TextView) findViewById(R.id.info_nickName);
        nameText = (TextView) findViewById(R.id.info_name);
        telphoneText = (TextView) findViewById(R.id.info_telphone);
        ofCityText = (TextView) findViewById(R.id.info_ofCity);
        sexText = (TextView) findViewById(R.id.info_sex);
        statusText = (TextView) findViewById(R.id.info_status);
        gradeText = (TextView) findViewById(R.id.info_grade);
        ordersNumText = (TextView) findViewById(R.id.info_ordersNum);
    }

    public void updateUserInfo(final String loginName){

        Thread thread = new Thread(new Runnable() {
                        @Override
            public void run() {
                            Map<String, Object> map = new HashMap<>();
                            //根据手机号获取用户详细信息
                            GetUserInfoService getUserInfoService = new GetUserInfoService();
                            map = getUserInfoService.HttpPost(loginName);
                            try{
                                String nickName = map.get("nickName").toString().trim();
                                String name = map.get("name").toString().trim();
                                String sex = map.get("sex").toString().trim();
                                String telphone = map.get("telphone").toString().trim();
                                String ofCity = map.get("ofCity").toString().trim();
                                String status = map.get("status").toString().trim();
                                String grade = map.get("grade").toString().trim();
                                String ordersNum = map.get("ordersNum").toString().trim();
                                nickNameText.setText(nickName);
                                nameText.setText(name);
                                sexText.setText(sex);
                                telphoneText.setText(telphone);
                                ofCityText.setText(ofCity);
                                //statusText.setText(status);
                                gradeText.setText(grade);
                                ordersNumText.setText(ordersNum);
                                if(status.equals("0"))
                                {
                                    statusText.setText("空闲");
                                }
                                if(status.equals("1"))
                                {
                                    statusText.setText("忙碌");
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
            }});
        thread.start();

    }
}
