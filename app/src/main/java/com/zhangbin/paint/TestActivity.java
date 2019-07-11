package com.zhangbin.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sanhai.live.ShSdk;
import com.sanhai.live.consts.PlayerLoadState;
import com.sanhai.live.event.OnPlayerLoadStateChangeListener;
import com.sanhai.live.util.ActivityUtil;
import com.sanhai.live.util.DimensionUtils;
import com.sanhai.live.util.ScreenSwitchUtils;
import com.zhangbin.paint.utils.SeekBarHelper;
import com.zhangbin.paint.view.InputBarView;

import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;

public class TestActivity extends Activity implements View.OnClickListener{
    private String mIjkUrl;
    protected String mToken;
    private ScheduledExecutorService lance;
    protected boolean isTitleBarShow = false;
    public PowerManager.WakeLock wakeLock;
    private String tryDuration = "0";//试看时长
    private String TAG = "--TestActivity--";
    /**
     * 视频是否跟ppt对调
     */
    protected boolean isExchangeViewContainer = false;
    /**
     * 状态栏高度
     */
    public int statusBarHeight;
    RelativeLayout pptLayout; // 白板区域层（包含白板容器、桌面分享视频容器、控制按钮层）
    FrameLayout pptContainer;   // 添加白板的布局容器
    FrameLayout desktopVideoContainer;//添加桌面分享视频播放器的布局容器
    FrameLayout videoViewContainer;  // 添加摄像头视频播放器的布局容器
    InputBarView vgInputLayout;
    LinearLayout linearContainer;
    LinearLayout tab_container;
    protected boolean isLongShowTitleBar = false;
    public InputMethodManager mInputMethodManager;
    //显示退出对话框
    private AlertDialog exitDialog;
    private AlertDialog.Builder normalDialog;
    private boolean mIsPlaying = true;
    ImageView controlIv;
    /**
     * 记录摄像头显隐
     */
    protected boolean userVideoShow = true;
    protected int width = 0;
    protected int screenHeight = 0;
    private long mEnterTime;
    private Context mContext;
    private ShSdk mShSdk;
    LinearLayout llBottomMenu;
    private SeekBarHelper seekBarUtil;
    SeekBar seekBar;
    TextView currentDuration;
    TextView totalDuration;
    TextView tvTryWatch;
    LinearLayout llTryWatch;
    RelativeLayout rlVipOpen;
    //FullScreenInputBarView fullScreenInputBarView;
    private final int PLAY_MSG = 0;
    private final int PAUSE_MSG = 1;
    private long playTime = 0;
    private Timer timer;
    private long preClickTime = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_test);
        mContext = this;
        init();
        initView();
        initClick();
        initEvent();
        tryAndSee();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PLAY_MSG://播放
                    playTime++;
                    break;
                case PAUSE_MSG://暂停
                    break;
            }
        }
    };
    /**
     * 监听初始化
     */
    private void initEvent() {
        /***
         * 获取缓冲状态
         */
        mShSdk.setOnPlayerLoadStateChangeListener(new OnPlayerLoadStateChangeListener() {
            @Override
            public void onPlayerLoadStateChange(int loadState) {
                if (loadState == PlayerLoadState.MEDIA_INFO_BUFFERING_START) {
                    Log.d(TAG, "缓冲开始");
                } else if (loadState == PlayerLoadState.MEDIA_INFO_BUFFERING_END) {
                    Log.d(TAG, "缓冲结束");
                }
            }
        });
    }
    private void tryAndSee() {
        tryDuration = getIntent().getStringExtra("tryDuration");
        //tryDuration = "90";
        if (!TextUtils.isEmpty(tryDuration)) {
            if ("90".equals(tryDuration)) {
                //显示
                String tryDurStr = "90秒";
                String des = "<font color=\"#ffffff\">可试看" + tryDurStr + " 开通</font><font color=\"#0099ff\"><u>班海VIP会员</u></font><font color=\"#ffffff\">免费看</font>";
                tvTryWatch.setText(Html.fromHtml(des));
                llTryWatch.setVisibility(View.VISIBLE);
            }
        } else {
            llTryWatch.setVisibility(View.GONE);
            rlVipOpen.setVisibility(View.GONE);
        }
    }


    protected void init() {
        statusBarHeight = DimensionUtils.getStatusBarHeightone(this);
        PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getName());
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScreenSwitchUtils.getInstance(this).setIsPortrait(true);

        //mToken = getIntent().getStringExtra("token");
        mToken = "YDM0IDZ4MGMhZzNiVTMiZ2NwQjZ4Y2NldTZkFTMiFTM8xHf9JyMzIjNzcj" +
                "I6ISZtFmbyJCLwojIhJCLiAjI6ICZpdmIskDN1YjN1IjN1EjOiUWbpR3ZlJnIs" +
                "ICM2gDOzATM3IiOiQWa4JCLyUjMxEjOiQWawJCLdtlOiIHd0FmIsIiI6IichRXY2FmIsA" +
                "jOiIXZk5WZnJCL5QTMwcTNyYTNxojIlJXawhXZiwyMzIjNzcjOiQWat92byJCLikiMyE" +
                "jM1UHXxEmY4UHX1QmY4UHXiRDZ2UHXoIjMxITN1xVMhJGO1xVNkJGO1xlY0QmN1xlI6ISZt" +
                "FmbrNWauJCLiIXZzVnI6ISZs9mciwiI4czMxMzNzYzMwEzNxMTMiojIklWdiwiM1ITMxo" +
                "jIkl2XyVmb0JXYwJCLiEXUpF2ZndUSrVVeJJzZEBlI6IyclR2bjJye";
        mEnterTime = getIntent().getLongExtra("enterTime", 0);
    }

    protected void initView() {
        pptLayout = (RelativeLayout) findViewById(R.id.ppt_Layout);
        pptContainer = (FrameLayout) findViewById(R.id.ppt_container);
        desktopVideoContainer = (FrameLayout) findViewById(R.id.desktop_video_container);
        videoViewContainer = (FrameLayout) findViewById(R.id.video_container);
        vgInputLayout = (InputBarView) findViewById(R.id.inputEdt_layout);
        linearContainer = (LinearLayout) findViewById(R.id.play_container);
        llBottomMenu = findViewById(R.id.ll_bottom_menu);
        //tab_container = (LinearLayout) findViewById(R.id.tab_container);
        currentDuration = findViewById(R.id.current_duration);
        totalDuration = findViewById(R.id.total_duration);
        controlIv = findViewById(R.id.controller_iv);
        seekBar = findViewById(R.id.seek_bar);
        seekBarUtil = new SeekBarHelper(this, seekBar);
        tvTryWatch = findViewById(R.id.tv_trywatch);
        llTryWatch = findViewById(R.id.ll_trywatch);
        rlVipOpen = findViewById(R.id.rl_vip_open);
        mShSdk = ShSdk.getInstance();
       // mShSdk.init(pptContainer, videoViewContainer, mToken, true);
        seekBarUtil.addTouchSlidSeekEvent(pptLayout);
        seekBar.setClickable(true);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        //long duration = PlaybackInfo.getInstance().getDurationLong();
        //seekBar.setMax((int) duration);
    }
    private void initClick() {
        pptLayout.setOnClickListener(this);
        pptContainer.setOnClickListener(this);
        desktopVideoContainer.setOnClickListener(this);
        videoViewContainer.setOnClickListener(this);
        vgInputLayout.setOnClickListener(this);
        linearContainer.setOnClickListener(this);
        controlIv.setOnClickListener(this);
        findViewById(R.id.exchange).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.fullScreen_iv).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        //Log.e("---------","view:"+view);
        switch (id){
            case R.id.exchange:
                /**切换ppt容器与摄像头视频容器*/
               // mShSdk.exchangeVideoAndWhiteboard(mContext);
                break;
            case R.id.iv_back:
                gobackAction();
                break;
            case R.id.controller_iv:  //开始或暂停
                if (mIsPlaying) {
                    pause();
                    mIsPlaying = false;
                } else {
                    play();
                    mIsPlaying = true;
                }
                break;
            case R.id.pptLayout:
                if (System.currentTimeMillis() - preClickTime < 300) {  //双击全屏
                    onFullScreenChange();
                    return;
                }
                preClickTime = System.currentTimeMillis();
                break;
            case R.id.fullScreen_iv:  //全屏
                onFullScreenChange();
                break;
        }
    }
    private void pause() {
        setPauseStatus();
       // mShSdk.playbackPause();
        seekBarUtil.stopUpdateSeekBar();
    }
    private void play() {
        setPlayingStatus();
       // mShSdk.playbackResume();

        seekBarUtil.updateSeekBar();
    }
    private void setPlayingStatus() {
        controlIv.setImageResource(R.mipmap.pause);
        mIsPlaying = true;
    }
    private void setPauseStatus() {
        controlIv.setImageResource(R.mipmap.play);
        mIsPlaying = false;
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
     * 更新布局
     */
    public void updateLayout() {
        int width = DimensionUtils.getScreenWidth(this);
        int height = DimensionUtils.getScreenHeight(this);
        Boolean isPortrait = height > width;
        if (!ActivityUtil.isFullScreen(this) && isPortrait) {

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
                llBottomMenu.setVisibility(View.GONE);
            } else {  //横屏
                if (DimensionUtils.isPad(this)) {
                    pptLayoutWidth = (int) (width * 0.72);
                    height = pptLayoutWidth * 3 / 4;
                    pptLayout.setBackgroundColor(Color.BLACK);
                } else {
                    pptLayoutWidth = width;
                }
                llBottomMenu.setVisibility(View.GONE);
            }
            pptParams.width = pptLayoutWidth;
            pptParams.height = height;
            pptLayout.setLayoutParams(pptParams);

        }
    }
    /**
     * 返回
     *
     * @return
     */
    public void gobackAction() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScreenSwitchUtils.getInstance(TestActivity.this).toggleScreen(false);
        } else {
            showExitDialog("提示", "是否确认要退出?");
        }
    }
    /**
     * 退出的dialog
     */
    private void showExitDialog(String title, String msg) {
        normalDialog = new AlertDialog.Builder(TestActivity.this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);

        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        normalDialog = null;
                        finish();
                        ScreenSwitchUtils.getInstance(TestActivity.this).destroy();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        if(normalDialog != null  && !((Activity) mContext).isFinishing()){
            normalDialog.show();
        }
    }
    //TODO--------------------------------------------视频播放状态-----------------------------------------

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //currentDuration.setText(TimeUtil.displayHHMMSS(progress));
            currentDuration.setText(progress+"");
            if (!TextUtils.isEmpty(tryDuration)) {
                if (Long.parseLong(tryDuration) <= progress) {
                    //停止播放
                    //cancelTask();
                  //  mShSdk.playbackStop();
                    rlVipOpen.setVisibility(View.VISIBLE);
                } else {
                    rlVipOpen.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            seekTo(progress);
        }
    };
    /**
     * 跳转到指定时间点
     */
    public void seekTo(long progress) {
        seekBarUtil.seekTo(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShSdk = null;
        seekBarUtil.stopUpdateSeekBar();
        seekBarUtil = null;
        ScreenSwitchUtils.getInstance(TestActivity.this).destroy();
    }
}
