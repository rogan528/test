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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijian.sf_im_sdk.MsgContent;
import com.google.gson.Gson;
import com.sanhai.live.ShSdk;
import com.sanhai.live.consts.Constatans;
import com.sanhai.live.ijk.player.DragFrameLayout;
import com.sanhai.live.module.OrderBean;
import com.sanhai.live.module.PlaybackInfo;
import com.sanhai.live.module.StartBean;
import com.sanhai.live.util.ActivityUtil;
import com.sanhai.live.util.DimensionUtils;
import com.sanhai.live.util.ExpressionUtil;
import com.sanhai.live.util.HttpCallBack;
import com.sanhai.live.util.HttpUtil;
import com.sanhai.live.util.OperationUtils;
import com.sanhai.live.util.ScreenSwitchUtils;
import com.sanhai.live.util.TimeUtil;
import com.sanhai.live.whiteboard.OrderDrawManger;
import com.sanhai.live.whiteboard.presenter.WhiteboardPresenter;
import com.zhangbin.paint.adapter.MsgItemAdapter;
import com.zhangbin.paint.bean.PlayBackBean;
import com.zhangbin.paint.bean.PlayBackDetailBean;
import com.zhangbin.paint.bugly.BaseActivity;
import com.zhangbin.paint.utils.SeekBarHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tv.danmaku.ijk.media.example.callback.OnGetMediaPlayInterface;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class PlaybackNativeActivity extends BaseActivity implements View.OnClickListener{
    private FrameLayout pptLayout;
    private DragFrameLayout dragFrameLayout;
    private WhiteboardPresenter whiteboardPresenter;
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private IjkDragVideoView mDragIjkVideoView;
    private String ijkVideoUrl = "http://192.168.1.207/10012019033011111111.mp4";
    //private String ijkVideoUrl = "rtmp://192.168.1.207/live/100120190330P6jUPHIs";
    //private String ijkVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private AndroidMediaController mMediaController;
    private Context mContext;
    private OrderDrawManger orderDrawManger;
    //private Handler handler = new Handler();
    protected boolean isTitleBarShow = false;
    protected boolean isLongShowTitleBar = false;
    private String userLoginId, userLoginName, mIjkUrl,userLiveId;
    //是否可见按钮
    private Button isVisiable;
    private ScheduledExecutorService lance;
    private LinearLayout titlebarContainer;
    private ListView lvMsg;
    private TextView loginStateView;
    List<MsgContent> list = new ArrayList<>();
    MsgItemAdapter itemAdapter;
    private Toast mToast;
    private long preClickTime = 0L;
    private InputMethodManager imm;
    private ImageView mImageVideoView;
    private Button mPlay;
    private AlertDialog.Builder normalDialog;
    ImageView controlIv;
    private boolean mIsPlaying = true;
    private SeekBarHelper seekBarUtil;
    SeekBar seekBar;
    TextView currentDuration;
    TextView totalDuration;
    LinearLayout seekbarLayout;
    LinearLayout operationContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_playback);
        userLoginId = getIntent().getStringExtra("userId");
        userLoginName = getIntent().getStringExtra("userName");
        mIjkUrl = getIntent().getStringExtra("allIpAddress");
        userLiveId = getIntent().getStringExtra("liveId");
        ijkVideoUrl = mIjkUrl.length() == 0 ? ijkVideoUrl : mIjkUrl;
        mToast = Toast.makeText(PlaybackNativeActivity.this, "", Toast.LENGTH_SHORT);
        initView();
        playiJKVideo();
        showTitleBar();
        initVideoStatus();
        setSeekBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        mDragIjkVideoView.resume();
        mDragIjkVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDragIjkVideoView.pause();
    }

    /**
     * 获取视频状态
     */
    private void initVideoStatus() {
        mDragIjkVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                mImageVideoView.setVisibility(View.VISIBLE);
                dragFrameLayout.setIsDrag(false);
                showExitDialog("发生错误", "视频传输,发生错误("+framework_err+","+impl_err+"),是否退出");
                return true;
            }
        });
        mDragIjkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mDragIjkVideoView.start();
                seekBarUtil.resetSeekBarProgress();
                seekBarUtil.updateSeekBar();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        pptLayout = findViewById(R.id.pptLayout);
        dragFrameLayout = findViewById(R.id.dragframeLayout);
        isVisiable = findViewById(R.id.is_visiable);
        titlebarContainer = findViewById(R.id.title_bar);
        loginStateView = findViewById(R.id.sample_text);
        lvMsg = findViewById(R.id.listview);
        mImageVideoView = findViewById(R.id.iv_videoView);
        mPlay = findViewById(R.id.btn_play);
        findViewById(R.id.is_visiable).setOnClickListener(this);
        findViewById(R.id.iv_go_back).setOnClickListener(this);
        findViewById(R.id.is_fullscreen).setOnClickListener(this);
        findViewById(R.id.pptLayout).setOnClickListener(this);
        mPlay.setOnClickListener(this);
        controlIv = findViewById(R.id.controller_iv);
        mImageVideoView.setVisibility(View.GONE);
        dragFrameLayout.setIsDrag(true);
        controlIv.setOnClickListener(this);
        seekBar = findViewById(R.id.seek_bar);
        seekBarUtil = new SeekBarHelper(this, seekBar);
        //seekBarUtil.addTouchSlidSeekEvent(pptLayout);
        currentDuration = findViewById(R.id.current_duration);
        totalDuration = findViewById(R.id.total_duration);
        seekbarLayout = findViewById(R.id.seek_bar_layout);
        operationContainer = findViewById(R.id.operation_btn_container);
    }
    private static final int UPDATA_VIDEO = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_VIDEO: {
                    int currentPosition = mDragIjkVideoView.getCurrentPosition();
                    seekToVideo(currentPosition);
                    mHandler.removeMessages(UPDATA_VIDEO);
                    mHandler.sendEmptyMessageDelayed(UPDATA_VIDEO, 500);
                }
            }
        }
    };

    private void seekToVideo(int currentPosition) {
        currentDuration.setText(TimeUtil.displayHHMMSS(currentPosition/1000));
        seekBarUtil.seekTo(currentPosition);
        requestData(currentPosition);
    }

    private void setSeekBar() {
        seekBar.setClickable(true);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        long duration = PlaybackInfo.getInstance().getDurationLong();
        duration = 667L;
        seekBar.setMax((int) duration);
        totalDuration.setText(TimeUtil.displayHHMMSS((int) duration));
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(UPDATA_VIDEO, 500);
        }

    }
    //TODO--------------------------------------------视频播放状态-----------------------------------------

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentDuration.setText(TimeUtil.displayHHMMSS(progress));
            //currentDuration.setText(progress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            seekTo(progress);
            int duration = mDragIjkVideoView.getDuration();
            int currentPosition = mDragIjkVideoView.getCurrentPosition();
            mDragIjkVideoView.seekTo(progress*1000);
            requestData(currentPosition);
        }
    };

    /**
     * 请求网络
     */
    private void requestData(final int currentPosition) {
        String url = "http://192.168.1.205:8080/live/live_board_page_seq?liveId="+userLiveId;
        HttpUtil.get(url, new HttpCallBack<PlayBackDetailBean>() {
            @Override
            public void onFailure(String message) {
                mToast.setText("网络请求失败,请重试");
                mToast.show();
            }

            @Override
            public void onSuccess(Object json) {
                PlayBackDetailBean playBackBean = (PlayBackDetailBean) json;
                if (playBackBean != null && playBackBean.getMsg().equals("ok")){
                    int size = playBackBean.getData().size();
                    for (int i= 0;i<size;i++){
                        String liveTime = playBackBean.getData().get(i).getLiveTime();
                        if (currentPosition < Integer.parseInt(liveTime)){
                            int page = Integer.parseInt(playBackBean.getData().get(i - 1).getPage());
                            requestDetailData(page);
                            break;
                        }
                    }

                }

            }
        });
    }

    private void requestDetailData(int page) {
        String playBackUrl = Constatans.playBackIdUrl+"?liveId="+userLiveId+"&pptId=3927F1600D2582C2995BEEC5F8EEF7E0&page="+page;
        HttpUtil.get(playBackUrl, new HttpCallBack<PlayBackDetailBean>() {
            @Override
            public void onFailure(String message) {
                mToast.setText("网络请求失败,请重试");
                mToast.show();
            }

            @Override
            public void onSuccess(Object json) {
                PlayBackDetailBean playBackBean = (PlayBackDetailBean) json;
                if (playBackBean != null && playBackBean.getMsg().equals("ok")) {
                    List<PlayBackDetailBean.DataBean> data = playBackBean.getData();
                    int size = data.size();
                    for (int i=0;i<size;i++){
                        String text = data.get(i).getContent();
                        if (text != null && !"".equals(text)) {
                            Gson gson = new Gson();
                            try {
                                OrderBean orderBean = gson.fromJson(text, OrderBean.class);
                                orderDrawManger.SetOrder(orderBean).ExecuteOrder();
                            } catch (Exception e) {

                            }
                        }
                    }


                }
            }
        });
    }

    /**
     * 跳转到指定时间点
     */
    public void seekTo(long progress) {
        seekBarUtil.seekTo(progress);
    }
    /**
     * 播放IJK视频
     */
    private void playiJKVideo() {
        mDragIjkVideoView = findViewById(R.id.ijk_videoView);
        Uri uri = Uri.parse(ijkVideoUrl);
        mMediaController = new AndroidMediaController(this, false);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mMediaController.setVisibility(View.VISIBLE);
        mDragIjkVideoView.setMediaController(mMediaController);
        if (TextUtils.isEmpty(ijkVideoUrl)) {
            mToast.setText("没有发现视频，请退出！");
            mToast.show();
        } else {
            mDragIjkVideoView.setVideoURI(uri);
            dragFrameLayout.setVisibility(View.VISIBLE);
            mDragIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                    float ratio = Float.parseFloat(df.format((float) mp.getVideoWidth() / mp.getVideoHeight()));
                    ViewGroup.LayoutParams lp;
                    lp = mDragIjkVideoView.getLayoutParams();
                    lp.width = mDragIjkVideoView.getWidth();
                    lp.height = Math.round(mDragIjkVideoView.getWidth() / ratio);
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

    /**
     * 初始化数据
     */
    private void initData() {
        whiteboardPresenter = new WhiteboardPresenter(mContext, pptLayout);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        ScreenSwitchUtils.getInstance(PlaybackNativeActivity.this).start(PlaybackNativeActivity.this);
        realHeight = screenHeight;
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        updateLayout();
        itemAdapter = new MsgItemAdapter(PlaybackNativeActivity.this, R.layout.item, list);
        lvMsg.setAdapter(itemAdapter);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go_back:
                gobackAction();
                break;
            case R.id.is_visiable:
                if (dragFrameLayout.getVisibility() == View.VISIBLE) {
                    dragFrameLayout.setVisibility(View.GONE);
                    isVisiable.setText("可见");
                } else {
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
            case R.id.btn_play:
                playAndPause();
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
        mDragIjkVideoView.start();
    }
    private void setPauseStatus() {
        controlIv.setImageResource(R.mipmap.play);
        mIsPlaying = false;
        mDragIjkVideoView.pause();
    }
    /**
     * 播放或者暂停
     */
    private void playAndPause() {
        if (mPlay.getText().toString().trim().equals("暂停")) {
            mDragIjkVideoView.pause();
            mPlay.setText("播放");
        } else if (mPlay.getText().toString().trim().equals("播放")) {
            mDragIjkVideoView.start();
            mPlay.setText("暂停");
        }
    }



    /**
     * 显示标题栏和操作按钮
     */
    protected final void showTitleBar() {
        if (lance != null && !lance.isShutdown())
            lance.shutdown();
        titlebarContainer.setVisibility(View.VISIBLE);
        isTitleBarShow = true;
        autoDismissTitleBar();
    }

    /**
     * 标题栏计时器. 3秒后自动隐藏
     */
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
        if (titlebarContainer == null)
            return;
        titlebarContainer.setVisibility(View.GONE);
        isTitleBarShow = false;
        if (seekbarLayout == null)
            return;
        /*seekbarLayout.setVisibility(View.INVISIBLE);
        operationContainer.setVisibility(View.GONE);*/
    }

    /**
     * 全屏。非全屏切换
     */
    public void switchFullScreen() {
        onFullScreenChange();
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
            ScreenSwitchUtils.getInstance(PlaybackNativeActivity.this).toggleScreen(false);
        } else {
            showExitDialog("提示", "是否确认要退出?");
        }
    }

    /**
     * 退出的dialog
     */
    private void showExitDialog(String title, String msg) {
        normalDialog = new AlertDialog.Builder(PlaybackNativeActivity.this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);

        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        normalDialog = null;
                        finish();
                        ScreenSwitchUtils.getInstance(PlaybackNativeActivity.this).destroy();
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
            pptLayout.setLayoutParams(pptParams);

        }
    }

    @Override
    public void onBackPressed() {
        gobackAction();
    }




    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        whiteboardPresenter.release();
        //使用原生的so下面两行不影响，使用的编译的打卡会卡住
        mDragIjkVideoView.stopPlayback();
        mDragIjkVideoView.release(true);
        mDragIjkVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }
}
