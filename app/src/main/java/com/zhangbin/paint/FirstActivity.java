package com.zhangbin.paint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangbin.paint.bugly.BaseActivity;
import com.sanhai.live.module.StartBean;
import com.sanhai.live.consts.Constatans;
import com.sanhai.live.util.HttpCallBack;
import com.sanhai.live.util.HttpUtil;
import com.sanhai.live.util.OperationUtils;
import com.sanhai.live.util.Util;

public class FirstActivity extends BaseActivity {
    private String TAG = "--FirstActivity--";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }


    /**
     * 初始化操作
     */
    private void initView() {
        final Toast mToast = Toast.makeText(FirstActivity.this, "", Toast.LENGTH_LONG);
        final EditText userId = findViewById(R.id.et_id);
        final EditText userName = findViewById(R.id.et_name);
        final EditText mLiveId = findViewById(R.id.et_liveId);
        userId.setText("001");
        userName.setText("测试1");
        findViewById(R.id.btn_vip_look_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Id = userId.getText().toString().trim();
                final String Name = userName.getText().toString().trim();
                final String liveId = mLiveId.getText().toString().trim();
                if(Id.length() == 0 || Name.length() == 0 || mLiveId.length() == 0) {
                    mToast.setText("请输入用户ID  用户名称 URL");
                    mToast.show();
                }else {
                    //请求网络
                    String url = Constatans.liveIdUrl+"?liveId="+liveId+"&userType=1";
                    HttpUtil.get(url, new HttpCallBack<StartBean>() {
                        @Override
                        public void onFailure(String message) {
                            Log.d(TAG, "onFailure: "+message);
                            mToast.setText("网络请求失败,请重试");
                            mToast.show();
                        }

                        @Override
                        public <T> void onSuccess(T json) {
                            StartBean startBean = (StartBean) json;
                            if (startBean != null && startBean.getMsg().equals("ok")){
                                updataData(startBean);
                                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                                intent.putExtra(MainActivity.IS_VIP, true);
                                intent.putExtra(MainActivity.USER_ID,Id);
                                intent.putExtra(MainActivity.USER_NAME,Name);
                                saveLoginInfo(Id,Name);
                                String allIpAddress = startBean.getLiveInfo().getPullUrl()+startBean.getLiveInfo().getLiveId();
                                intent.putExtra(MainActivity.ALL_IP_ADDRESS,allIpAddress);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
    private void saveLoginInfo(String userId, String userName) {
        SharedPreferences share = getSharedPreferences("userInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userId",userId);
        editor.putString("userName",userName);
        editor.commit();

    }
    /**
     * 更新数据
     * @param startBean
     */
    private void updataData(StartBean startBean) {
        OperationUtils.getInstance().mBoardHeight = Util.toInteger(startBean.getLiveInfo().getBoardHeight());
        OperationUtils.getInstance().mBoardWidth = Util.toInteger(startBean.getLiveInfo().getBoardWidth());
        int size = startBean.getPptInfo().size();
        OperationUtils.getInstance().mCurrentSlide = Integer.parseInt(startBean.getPptInfo().get(size - 1).getCurrentSlide());
        OperationUtils.getInstance().mCurrentPage = Integer.parseInt(startBean.getPptInfo().get(size - 1).getCurrentPage());
        OperationUtils.getInstance().mDeployPath = startBean.getPptInfo().get(size - 1).getDeployPath();
        OperationUtils.getInstance().mPptId = startBean.getPptInfo().get(size - 1).getPptId();
        OperationUtils.getInstance().mLiveId  = startBean.getPptInfo().get(size - 1).getLiveId();
    }
}
