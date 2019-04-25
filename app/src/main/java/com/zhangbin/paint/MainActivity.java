package com.zhangbin.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.lijian.sf_im_sdk.IM_SDK;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.ActivityUtil;
import com.zhangbin.paint.util.DimensionUtils;
import com.zhangbin.paint.util.ScreenSwitchUtils;
import com.zhangbin.paint.util.Util;
import com.zhangbin.paint.video.DragFrameLayout;
import com.zhangbin.paint.whiteboard.presenter.WhiteboardPresenter;
import com.zhangbin.paint.whiteboard.OrderDrawManger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MainActivity extends Activity implements View.OnClickListener{

    //private String url = "https://www.baidu.com/";
    private String url = "http://192.168.8.37:8081/83461B08A0401FC68D9C2A7E036C4710/h5/h5.html?aaaa";
    //  private String url = "file:///android_asset/javascript.html";
    private FrameLayout pptLayout;
    private DragFrameLayout dragFrameLayout;
    private Button isVisiable;
    private WhiteboardPresenter whiteboardPresenter;
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
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
    private String TAG = "--IjkDragVideoView--";
    // 是否是竖屏
    private boolean isPortrait = true;
    private boolean mIsSensorSwitch = false;
    boolean mIsOpenSwitchAuto = false;  //是否开启自动重力切换
    private boolean mIsFullScreen = false; //全屏
    private Handler handler = new Handler();
    private boolean isClick;
    private long preClickTime = 0L;
    protected boolean isTitleBarShow = false;
    protected boolean isLongShowTitleBar = false;
    public static String IS_VIP = "isVip";
    private boolean isVip;
    private LinearLayout tryWatch;
    private ScheduledExecutorService lance;
    private LinearLayout titlebarContainer;
    private IM_SDK im_sdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        isVip = getIntent().getBooleanExtra(IS_VIP, false);
        initView();
        playiJKVideo();
        initData();
        showTitleBar();
        initIM();
    }
    /**
     * 初始化数据
     */
    private void initIM() {
        im_sdk = new IM_SDK();
        int rescode = im_sdk.InitSDK(1, "11", "222", "2222", "",
                "192.168.1.206", "", 8084
        );
        im_sdk.OnGetLoginState(rescode);
        im_sdk.ConnectMsgServer();
        String msg = "我要发测试消息了";
        im_sdk.SendMsg(msg,msg.length());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        whiteboardPresenter = new WhiteboardPresenter(mContext,pptLayout);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        ScreenSwitchUtils.getInstance(MainActivity.this).start(MainActivity.this);
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
        if (!isVip){
            tryWatch.setVisibility(View.VISIBLE);
        }else {
            tryWatch.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        pptLayout =  findViewById(R.id.pptLayout);
        dragFrameLayout =  findViewById(R.id.dragframeLayout);
        isVisiable = findViewById(R.id.is_visiable);
        tryWatch = findViewById(R.id.ll_trywatch);
        titlebarContainer = findViewById(R.id.title_bar);
        findViewById(R.id.jx_next).setOnClickListener(this);
        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.redo).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.is_visiable).setOnClickListener(this);
        findViewById(R.id.iv_go_back).setOnClickListener(this);
        findViewById(R.id.fullScreen_iv).setOnClickListener(this);
        findViewById(R.id.is_fullscreen).setOnClickListener(this);
        findViewById(R.id.pptLayout).setOnClickListener(this);
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
            mDragIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                    float ratio = Float.parseFloat(df.format((float)mp.getVideoWidth()/mp.getVideoHeight()));
                    ViewGroup.LayoutParams lp;
                    lp = mDragIjkVideoView.getLayoutParams();
                    lp.width = mDragIjkVideoView.getWidth();
                    lp.height =  Math.round(mDragIjkVideoView.getWidth()/ratio);
                    mDragIjkVideoView.setLayoutParams(lp);
                    mDragIjkVideoView.setVisibility(View.VISIBLE);
                    isVisiable.setVisibility(View.VISIBLE);
                    mDragIjkVideoView.start();

                }
            });

        }
        mDragIjkVideoView.setOnLongClickListener(new View.OnLongClickListener() {
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
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go_back:
                gobackAction();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    showInputLayout();
                }
                break;
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
            case R.id.is_visiable:

                if (dragFrameLayout.getVisibility() == View.VISIBLE){
                    dragFrameLayout.setVisibility(View.GONE);
                    isVisiable.setText("可见");
                }else {
                    dragFrameLayout.setVisibility(View.VISIBLE);
                    isVisiable.setText("不可见");
                }
                break;
            case R.id.is_fullscreen: //全屏
                switchFullScreen();
                break;
            case R.id.pptLayout:
                if (System.currentTimeMillis() - preClickTime < 300) {  //双击全屏
                    switchFullScreen();
                    return;
                }
                preClickTime = System.currentTimeMillis();
                if (isTitleBarShow) {
                    isLongShowTitleBar = false;
                    hideTitleBar();
                } else {
                    //刚进来显示，三秒之后自动隐藏
                    isTitleBarShow = true;
                    showTitleBar();
                }
                break;
        }
    }
    /**
     * 显示标题栏和操作按钮
     */
    protected final void showTitleBar() {
        if (lance != null && !lance.isShutdown())
            lance.shutdown();
        showController();
        isTitleBarShow = true;
        autoDismissTitleBar();
    }
    private void showController() {
        titlebarContainer.setVisibility(View.VISIBLE);
        //operationContainer.setVisibility(View.VISIBLE);
    }

    //标题栏计时器. 3秒后自动隐藏
    protected void autoDismissTitleBar() {
        stopDismissTitleBar();
        Runnable sendBeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTitleBarShow) {
                    if (lance != null && !lance.isShutdown() && !isLongShowTitleBar) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTitleBarShow) {
                                    hideTitleBar();
                                } else {
                                    stopDismissTitleBar();
                                }
                            }
                        });
                    }
                }
            }
        };

        lance = Executors.newSingleThreadScheduledExecutor();
        lance.scheduleAtFixedRate(sendBeatRunnable, 5, 5, TimeUnit.SECONDS);
    }

    protected void stopDismissTitleBar() {
        if (lance != null) {
            if (!lance.isShutdown()) {
                lance.shutdown();
            }
            lance = null;
        }
    }

    /**
     * 隐藏标题栏和操作按钮
     */
    protected final void hideTitleBar() {
        if (isLongShowTitleBar)
            return;
        stopDismissTitleBar();
        hideController();
        isTitleBarShow = false;
    }
   private void hideController() {
        if (titlebarContainer == null)
            return;
        titlebarContainer.setVisibility(View.GONE);
        /*operationContainer.setVisibility(View.GONE);
        fullScreenInputBarView.hideSoftInput();
        if (mNetCheckHelper != null) {
            mNetCheckHelper.dismissPop();
        }*/
    }
    /**
     * 全屏。非全屏切换
     */
    public void switchFullScreen() {
        onFullScreenChange();
        showInputLayout();

    }
    /**
     * 全屏和非全屏切换
     */
    public void onFullScreenChange() {
        ScreenSwitchUtils.getInstance(this).setIsFullScreen(!ScreenSwitchUtils.getInstance(this).isFullScreen());
        if (ScreenSwitchUtils.getInstance(this).isSensorSwitchLandScreen()) {  //重力切换的横屏的话
            updateLayout();
        } else {
            ScreenSwitchUtils.getInstance(this).toggleScreen();
        }
    }
    /**
     * 返回
     *
     * @return
     */
    public void gobackAction() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScreenSwitchUtils.getInstance(MainActivity.this).toggleScreen(false);
        } else {
            showExitDialog();
        }
    }
    private void showExitDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否确认退出?");

        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         ScreenSwitchUtils.getInstance(MainActivity.this).stop();
                        finish();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.show();
    }
    private void showInputLayout() {
        if (handler == null) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!DimensionUtils.isPad(MainActivity.this) && ScreenSwitchUtils.getInstance(MainActivity.this).isFullScreen()) {
                    //输入框的隐藏
                    /*if (vgInputLayout != null) {
                        vgInputLayout.setVisibility(View.GONE);
                    }*/
                } else {
                   /* if (mLiveMessageView != null) {
                        if (mLiveMessageView.getCurrentItem() != 2) {
                            if (vgInputLayout != null) {
                                vgInputLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }*/

                }
            }
        }, 100);
    }
    @Override
    public void onBackPressed() {
        gobackAction();
    }
}
