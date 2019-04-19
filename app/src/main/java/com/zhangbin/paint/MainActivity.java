package com.zhangbin.paint;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.ActivityUtil;
import com.zhangbin.paint.util.DimensionUtils;
import com.zhangbin.paint.util.ScreenSwitchUtils;
import com.zhangbin.paint.util.Util;
import com.zhangbin.paint.whiteboard.presenter.WhiteboardPresenter;
import com.zhangbin.paint.whiteboard.DragVideoView;
import com.zhangbin.paint.whiteboard.OrderDrawManger;

import java.util.ArrayList;

import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MainActivity extends Activity implements View.OnClickListener{

    //private String url = "https://www.baidu.com/";
    private String url = "http://192.168.8.37:8081/83461B08A0401FC68D9C2A7E036C4710/h5/h5.html?aaaa";
    //  private String url = "file:///android_asset/javascript.html";
    private FrameLayout pptLayout;
    private Button mJxNext;//解析
    private WhiteboardPresenter whiteboardPresenter;
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private DragVideoView mDragVideoView;
    private String dragVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private IjkDragVideoView mDragIjkVideoView;
    private String ijkVideoUrl = "rtmp://192.168.1.207/live/sanhaieduLive";
   // private String ijkVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    private Context mContext;
    private ArrayList<OrderBean> listOrderBean;
//    private DrawManger drawManger;
    private OrderDrawManger orderDrawManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
        playDragVideo();
        playiJKVideo();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        whiteboardPresenter = new WhiteboardPresenter(mContext,pptLayout);       Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        // realHeight = (int) (screenHeight - getResources().getDimension(R.dimen.DIMEN_100PX) - getResources().getDimension(R.dimen.DIMEN_100PX));
        realHeight = screenHeight;
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        String input = Util.readFileFromAssets(this, "LiveClient.json");
 //     String input = Util.readFileFromAssets(this, "LiveClient2.json");
        Gson gson = new Gson();
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        orderDrawManger.setListorderBean(listOrderBean);
        updateLayout();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        pptLayout =  findViewById(R.id.pptLayout);
        mJxNext = findViewById(R.id.jx_next);
        mJxNext.setOnClickListener(this);
        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.redo).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
    }
    /**
     * 播放IJK视频
     */
    private void playiJKVideo() {
        mDragIjkVideoView = findViewById(R.id.ijk_videoView);
        mHudView = findViewById(R.id.hud_view);
        Uri uri = Uri.parse(ijkVideoUrl);
        mMediaController = new AndroidMediaController(this, false);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mMediaController.setVisibility(View.VISIBLE);
        mDragIjkVideoView.setMediaController(mMediaController);
        mDragIjkVideoView.setHudView(mHudView);
        if (TextUtils.isEmpty(ijkVideoUrl)) {
            Toast.makeText(this,
                    "没有发现视频，请退出！",
                    Toast.LENGTH_LONG).show();
        } else {
            mDragIjkVideoView.setVideoURI(uri);
            mDragIjkVideoView.start();
        }
        mDragIjkVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }
    /**
     * 播放拖拽视频
     */
    private void playDragVideo() {
        mDragVideoView = findViewById(R.id.drag_videoView);
        Uri uri = Uri.parse(dragVideoUrl);
        MediaController mediaController = new MediaController(this);
        mDragVideoView.setMediaController(mediaController);
        mediaController.setVisibility(View.INVISIBLE);
        mDragVideoView.setVideoURI(uri);
        mDragVideoView.start();
        mDragVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mDragVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        boolean isPortrait = ScreenSwitchUtils.getInstance(this).isPortrait();
        boolean isFullScreen = ScreenSwitchUtils.getInstance(this).isFullScreen();
        if (!isPortrait && isFullScreen) {
            ActivityUtil.setFullScreen(this, true);
        } else if (isPortrait && !isFullScreen) {
            ActivityUtil.setFullScreen(this, true);
        }
        updateLayout();
        super.onConfigurationChanged(newConfig);
    }

    public void updateLayout() {
        int width = DimensionUtils.getScreenWidth(this);
        int height = DimensionUtils.getScreenHeight(this);
        Log.i("ppt宽高1", "宽：" + width + "  高：" + height);
        Boolean isPortrait = height > width;
        if (!ActivityUtil.isFullScreen(this) && isPortrait) {
//            height -= DimensionUtils.getStatusBarHeight(this);
        }

        screenHeight = height;
        //获取宽高
        int pptLayoutWidth = 0;

        if (pptLayout != null) {
            ViewGroup.LayoutParams pptParams = pptLayout.getLayoutParams();
            pptLayout.setBackgroundColor(Color.TRANSPARENT);
            if (isPortrait) {   //竖屏

                pptLayoutWidth = width;
                height = 3 * width / 4;


            } else {  //横屏

                if (DimensionUtils.isPad(this)) {
                    pptLayoutWidth = (int) (width * 0.72);
                    height = pptLayoutWidth * 3 / 4;
                    pptLayout.setBackgroundColor(Color.BLACK);

                } else {
                    pptLayoutWidth = width;
                }

            }

            pptParams.width = pptLayoutWidth;
            pptParams.height = height;
            Log.i("ppt宽高", "宽：" + pptParams.width + "  高：" + height);
            //FrameLayout.LayoutParams videoP = (FrameLayout.LayoutParams) videoLayout.getLayoutParams();
            //videoP.leftMargin = DimensionUtils.getScreenWidth(this) - 300;
            //videoP.topMargin = DimensionUtils.getScreenHeight(this) - 300;
            //videoLayout.setLayoutParams(videoP);


            pptLayout.setLayoutParams(pptParams);
           // whiteboardPresenter = new WhiteboardPresenter(mContext,pptLayout);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jx_next:
                orderDrawManger.NextOrder().ExecuteOrder();
                break;
            case R.id.undo:
                whiteboardPresenter.undo();
                break;
            case R.id.redo:
                whiteboardPresenter.redo();
                break;
            case R.id.clear:
                whiteboardPresenter.orderClear();
                break;
        }
    }


}
