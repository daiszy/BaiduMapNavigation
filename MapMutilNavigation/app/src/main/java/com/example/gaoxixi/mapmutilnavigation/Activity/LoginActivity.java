package com.example.gaoxixi.mapmutilnavigation.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gaoxixi.mapmutilnavigation.HttpService.LoginService;
import com.example.gaoxixi.mapmutilnavigation.MainActivity;
import com.example.gaoxixi.mapmutilnavigation.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private  EditText loginName;
    private EditText password;
    private Button loginBtn;
    private Button loginError;
    private Button registerBtn;

    //等待框
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CharSequence title = "登录";
        setTitle(title);

        initView();

        //设置按钮监听器
        loginBtn.setOnClickListener(this);
        loginError.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private void initView() {
        loginName = (EditText) findViewById(R.id.loginName);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginError = (Button) findViewById(R.id.login_error);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);
    }

    /**登录、注册、忘记密码点击事件*/
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.loginBtn:
                /**登录*/
                //提示框
                progressDialog.setTitle("提示");
                progressDialog.setMessage("正在登录，请稍候...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //获取用户名和密码
                final String UserName = loginName.getText().toString();
                final String Password = password.getText().toString();
                final LoginService loginService = new LoginService();
                //创建子线程，分别进行get和post传输
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean is = loginService.HttpPost(UserName,Password);
                        if(is){
                            progressDialog.setCancelable(true);
                            Intent intent = new Intent();
                            intent.putExtra("loginName",UserName);
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                thread.start();
                break;
            case R.id.login_error:
                /**忘记密码*/
                break;
            case R.id.registerBtn:
                /**注册*/
                break;
        }
    }

    private class MyThread implements Runnable {
        @Override
        public void run() {

        }
    }
}
