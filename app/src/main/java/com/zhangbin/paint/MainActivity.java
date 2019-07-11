package com.zhangbin.paint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangbin.paint.bean.PlayBackDetailBean;
import com.zhangbin.paint.bugly.BaseActivity;
import com.sanhai.live.module.StartBean;
import com.sanhai.live.consts.Constatans;
import com.sanhai.live.util.HttpCallBack;
import com.sanhai.live.util.HttpUtil;
import com.sanhai.live.util.OperationUtils;
import com.sanhai.live.util.Util;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private String TAG = "--MainActivity--";
    private Toast mToast;
    private EditText userId;
    private EditText userName;
    private EditText mLiveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
        userId = findViewById(R.id.et_id);
        userName = findViewById(R.id.et_name);
        mLiveId = findViewById(R.id.et_liveId);
        userId.setText("001");
        userName.setText("测试1");
        findViewById(R.id.btn_livedata).setOnClickListener(this);
        findViewById(R.id.btn_playback).setOnClickListener(this);
    }
    private void saveLoginInfo(String userId, String userName,String liveId) {
        SharedPreferences share = getSharedPreferences("userInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userId",userId);
        editor.putString("userName",userName);
        editor.putString("liveId",liveId);
        editor.commit();

    }
    /**
     * 更新直播数据
     * @param startBean
     */
    private void updataLiveData(StartBean startBean) {
        OperationUtils.getInstance().mBoardHeight = Util.toInteger(startBean.getLiveInfo().getBoardHeight());
        OperationUtils.getInstance().mBoardWidth = Util.toInteger(startBean.getLiveInfo().getBoardWidth());
        int size = startBean.getPptInfo().size();
        OperationUtils.getInstance().mCurrentSlide = Integer.parseInt(startBean.getPptInfo().get(size - 1).getCurrentSlide());
        OperationUtils.getInstance().mCurrentPage = Integer.parseInt(startBean.getPptInfo().get(size - 1).getCurrentPage());
        OperationUtils.getInstance().mFirstPage = Integer.parseInt(startBean.getPptInfo().get(size - 1).getCurrentPage());
        OperationUtils.getInstance().mDeployPath = startBean.getPptInfo().get(size - 1).getDeployPath();
        OperationUtils.getInstance().mPptId = startBean.getPptInfo().get(size - 1).getPptId();
        OperationUtils.getInstance().mLiveId  = startBean.getPptInfo().get(size - 1).getLiveId();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_livedata:
                lookLiveData();
                break;
            case R.id.btn_playback:
                lookPlayBack();
                break;
        }
    }

    /**
     * 看直播
     */
    private void lookLiveData() {
        final String Id = userId.getText().toString().trim();
        final String Name = userName.getText().toString().trim();
        final String liveId = mLiveId.getText().toString().trim();
        if(Id.length() == 0 || Name.length() == 0 || mLiveId.length() == 0) {
            mToast.setText("请输入用户ID  用户名称 URL");
            mToast.show();
        }else {
            requestLiveData(Id,Name,liveId,true);
        }
    }

    /**
     * 看点播
     */
    private void lookPlayBack() {
        String videoId = ((EditText) findViewById(R.id.et_videoId)).getText().toString().trim();
        if (TextUtils.isEmpty(videoId)){
            mToast.setText("点播视频Id不能为空");
            mToast.show();
        }else{
            requestPlayBackData(videoId);

        }
    }

    /**
     * 请求点播数据拿到token然后播放
     * @param videoId
     */
    private void requestPlayBackData(String videoId) {
        //http://192.168.1.205:8080/live/live_board_select?liveId=10012019033011111111&pptId=3927F1600D2582C2995BEEC5F8EEF7E0&page=1
        String playBackUrl = Constatans.playBackIdUrl+"?liveId="+videoId+"&pptId=3927F1600D2582C2995BEEC5F8EEF7E0&page=1";
        HttpUtil.get(playBackUrl, new HttpCallBack<PlayBackDetailBean>() {
            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure: "+message);
                mToast.setText("网络请求失败,请重试");
                mToast.show();
            }

            @Override
            public void onSuccess(Object json) {
                PlayBackDetailBean playBackBean = (PlayBackDetailBean) json;
                if (playBackBean != null && playBackBean.getMsg().equals("ok")) {
                    String liveId = playBackBean.getData().get(0).getLiveId();
                    requestLiveData("","",liveId,false);
                }
            }
        });

    }

    private void requestLiveData(final String Id, final String Name, final String liveId, final boolean isLive) {
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
                    if (isLive) {
                        updataLiveData(startBean);
                        Intent intent = new Intent(MainActivity.this, LiveNativeActivity.class);
                        intent.putExtra("isVip", true);
                        intent.putExtra("userId", Id);
                        intent.putExtra("userName", Name);
                        intent.putExtra("liveId", liveId);
                        String allIpAddress = startBean.getLiveInfo().getPullUrl() + startBean.getLiveInfo().getLiveId();
                        intent.putExtra("allIpAddress", allIpAddress);
                        saveLoginInfo(Id, Name, liveId);
                        startActivity(intent);
                    }else{
                        OperationUtils.getInstance().mBoardHeight = Util.toInteger(startBean.getLiveInfo().getBoardHeight());
                        OperationUtils.getInstance().mBoardWidth = Util.toInteger(startBean.getLiveInfo().getBoardWidth());
                        int size = startBean.getPptInfo().size();
                        OperationUtils.getInstance().mCurrentSlide = 1;
                        OperationUtils.getInstance().mCurrentPage = 1;
                        OperationUtils.getInstance().mFirstPage = 1;
                        OperationUtils.getInstance().mDeployPath = startBean.getPptInfo().get(size - 1).getDeployPath();
                        OperationUtils.getInstance().mPptId = startBean.getPptInfo().get(size - 1).getPptId();
                        OperationUtils.getInstance().mLiveId  = startBean.getPptInfo().get(size - 1).getLiveId();
                        String allIpAddress = "http://192.168.1.207/10012019033011111111.mp4";
                        Intent intent = new Intent(MainActivity.this, PlaybackNativeActivity.class);
                        intent.putExtra("tryDuration","90");
                        intent.putExtra("allIpAddress",allIpAddress);
                        intent.putExtra("liveId", liveId);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
