package com.github.isuperred.ijkplayerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import tv.danmaku.ijk.media.example.application.Settings;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private IjkDragVideoView mVideoView;
    private AndroidMediaController mMediaController;
    private TextView mTextView;
    private String mVideoPath;
    private TableLayout mHudView;

    private Settings mSettings;
    //是否是VIP
    public static String IS_VIP = "isVip";
    private boolean isVip;
    //用户id和用户名
    public static String USER_ID = "userId";
    public static String USER_NAME = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startVideo();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }
    private void startVideo() {
        mSettings = new Settings(this);
        mVideoPath = "rtmp://192.168.1.207/live/100120190330EO9Fr0V6";

        mMediaController = new AndroidMediaController(this, false);

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");//上一句已加载，这里为什么还加载一次？

        mVideoView = (IjkDragVideoView) findViewById(R.id.videoView);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        if (TextUtils.isEmpty(mVideoPath)) {
            Toast.makeText(this,
                    "No Video Found! Press Back Button To Exit",
                    Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }
}
