package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gaoxixi.mapmutilnavigation.HttpService.RegisterService;
import com.example.gaoxixi.mapmutilnavigation.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //注册信息
    private EditText nickName;
    private EditText name;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText telphone;
    private EditText ofCity;
    private EditText password;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        //注册监听事件
        registerBtn.setOnClickListener(this);

    }

    private void initView() {
        nickName = (EditText) findViewById(R.id.nickName);
        name = (EditText) findViewById(R.id.name);
        telphone = (EditText) findViewById(R.id.telphone);
        ofCity = (EditText) findViewById(R.id.ofCity);
        password = (EditText) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.registerBtn);
    }

    @Override
    public void onClick(View view) {
        radioGroup = (RadioGroup) findViewById(R.id.rgSex);
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        final String NickName = nickName.getText().toString();
        final String Name = name.getText().toString();
        final String Sex = radioButton.getText().toString();
        final String Telphone = telphone.getText().toString();
        final String OfCity = ofCity.getText().toString();
        final String Password = password.getText().toString();

        final RegisterService registerService = new RegisterService();
        //创建子线程，分别进行get和post传输
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                boolean is = registerService.HttpPost(NickName,Name,Sex,Telphone,OfCity,Password);
                if(is){
                    msg.what = 1;
//                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);*/
                }else {
                    msg.what = 2;
//                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
