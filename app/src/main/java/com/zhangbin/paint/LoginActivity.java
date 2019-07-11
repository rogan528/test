package com.zhangbin.paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sanhai.live.util.HttpCallBack;
import com.sanhai.live.util.HttpUtil;

import java.util.HashMap;

public class LoginActivity extends Activity implements View.OnClickListener{
    private Toast mToast;
    private String TAG = "--LoginActivity--";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToast = Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_login:
                login();
                break;
        }
    }

    /**
     * 登陆页面
     */
    private void login() {
        String userName = ((EditText)findViewById(R.id.et_userName)).getText().toString().trim();
        String userPasswod = ((EditText)findViewById(R.id.et_psw)).getText().toString().trim();
        if(userName.length() == 0 || userPasswod.length() == 0) {
            mToast.setText("用户名或者密码不能为空");
            mToast.show();
        }else{
            //requestData(userName,userPasswod);
            enterFirst();
        }
    }

    private void enterFirst() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void requestData(String userName, String userPasswod) {
        //String url = Constatans.liveIdUrl+"?liveId="+liveId+"&userType=1";
        String url = "1111";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(userName,userName);
        hashMap.put(userPasswod,userPasswod);
        HttpUtil.post(url, hashMap, new HttpCallBack() {
            @Override
            public void onSuccess(Object json) {
                enterFirst();
            }
            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure: "+message);
                mToast.setText("网络请求失败,请重试");
                mToast.show();
            }

        });
    }
}
