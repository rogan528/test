package com.zhangbin.paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        findViewById(R.id.btn_vip_look_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.IS_VIP,true);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_look_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.IS_VIP,false);
                startActivity(intent);
            }
        });
    }
}
