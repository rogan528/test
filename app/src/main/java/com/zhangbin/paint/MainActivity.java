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
import android.text.SpannableString;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lijian.sf_im_sdk.IM_SDK;
import com.example.lijian.sf_im_sdk.MsgContent;
import com.example.lijian.sf_im_sdk.OnGetInterface;
import com.example.lijian.sf_im_sdk.OnUpdateUiInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanhai.live.entity.ExpressionEntity;
import com.sanhai.live.module.ExpressionData;
import com.sanhai.live.util.ExpressionUtil;
import com.zhangbin.paint.adapter.MsgItemAdapter;
import com.zhangbin.paint.bugly.BaseActivity;
import com.sanhai.live.module.OrderBean;
import com.sanhai.live.util.ActivityUtil;
import com.sanhai.live.util.DimensionUtils;
import com.sanhai.live.util.ScreenSwitchUtils;
import com.sanhai.live.util.Util;
import com.sanhai.live.ijk.player.DragFrameLayout;
import com.sanhai.live.whiteboard.OrderDrawManger;
import com.sanhai.live.whiteboard.presenter.WhiteboardPresenter;
import com.zhangbin.paint.view.ExpressionView;
import com.zhangbin.paint.view.InputBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import tv.danmaku.ijk.media.example.callback.OnGetMediaPlayInterface;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MainActivity extends BaseActivity implements View.OnClickListener, OnUpdateUiInterface {
    private FrameLayout pptLayout;
    private DragFrameLayout dragFrameLayout;
    private WhiteboardPresenter whiteboardPresenter;
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private IjkDragVideoView mDragIjkVideoView;
    private String ijkVideoUrl = "rtmp://192.168.1.207/live/100120190330EO9Fr0V6";
    //private String ijkVideoUrl = "rtmp://192.168.1.207/live/100120190330P6jUPHIs";
    //private String ijkVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private AndroidMediaController mMediaController;
    private Context mContext;
    private ArrayList<OrderBean> listOrderBean;
    private OrderDrawManger orderDrawManger;
    //private Handler handler = new Handler();
    protected boolean isTitleBarShow = false;
    protected boolean isLongShowTitleBar = false;
    //是否是VIP
    public static String IS_VIP = "isVip";
    private boolean isVip;
    //用户id和用户名
    public static String USER_ID = "userId";
    public static String USER_NAME = "userName";
    public static String ALL_IP_ADDRESS = "allIpAddress";
    public static String START_BEAN = "startBean";
    private String userId, userName, mIjkUrl;
    //是否可见按钮
    private Button isVisiable;
    private ScheduledExecutorService lance;
    private LinearLayout titlebarContainer;
    private IM_SDK im_sdk;
    private ListView lvMsg;
    private TextView loginStateView;
    List<MsgContent> list = new ArrayList<>();
    MsgItemAdapter itemAdapter;
    private Toast mToast;
    private long preClickTime = 0L;
    private long endClickTime = 0L;
    private int initType = 1, ipPort = 8084;
    private String groupID = "2";
    private InputBarView inputEdt_layout;
    //连接互联服务状态码
    private int state = -6;
    //发消息错误回调码
    private int resCode = -8;
    private InputMethodManager imm;
    private ImageView mImageVideoView;
    private int sendType = 0, forbidType = 0;
    private String forbidUserId, forbidUserName, forbidTime;
    int mDefaultRes;
    private Button mPlay;
    private TextView mShow;
    private AlertDialog.Builder normalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        isVip = getIntent().getBooleanExtra(IS_VIP, false);
        userId = getIntent().getStringExtra(USER_ID);
        userName = getIntent().getStringExtra(USER_NAME);
        mIjkUrl = getIntent().getStringExtra(ALL_IP_ADDRESS);
        ijkVideoUrl = mIjkUrl.length() == 0 ? ijkVideoUrl : mIjkUrl;
        mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
        im_sdk = MyApplication.getApplication().getmIm_sdk();
        EventBus.getDefault().register(this);
        initIM();
        initView();
        playiJKVideo();
        initOrderData();
        showTitleBar();
        initVideoStatus();
        ExpressionUtil.tvImgWidth = DimensionUtils.dip2px(this, 55);
        ExpressionUtil.tvImgHeight = DimensionUtils.dip2px(this, 45);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initAssetsData();
        //mDragIjkVideoView.resume();
        //mDragIjkVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mDragIjkVideoView.pause();
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
                mImageVideoView.setVisibility(View.VISIBLE);
                dragFrameLayout.setIsDrag(false);
                showExitDialog("提示", "网络中断或者老师已经下课,是否退出?");
            }
        });
    }

    /**
     * 连接IM服务初始化操作
     */
    private void initIM() {

        im_sdk.ConnectMsgServer();
        MyApplication.getApplication().setCallback(new OnGetInterface() {
            @Override
            public void AddItem(MsgContent item) {
                UpdateUIInterface(item);
            }

            public void GetLoginStaete(int state) {
                UpdateLoginInterface(state);
            }

            @Override
            public void GetSendMsgData(int resCode, String userID) {
                UpdateSendMsgDataStateInterface(resCode, userID);
            }

            @Override
            public void GetDisableSend(int sendType, int resCode, int forbidType, String userId, String username, String time) {
                UpdateDisableSendStateInterface(sendType, resCode, forbidType, userId, username, time);
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
        inputEdt_layout = findViewById(R.id.inputEdt_layout);
        mImageVideoView = findViewById(R.id.iv_videoView);
        mPlay = findViewById(R.id.btn_play);
        mShow = findViewById(R.id.tv_show);
        findViewById(R.id.jx_next).setOnClickListener(this);
        findViewById(R.id.btn_exchange).setOnClickListener(this);
        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.redo).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.is_visiable).setOnClickListener(this);
        findViewById(R.id.iv_go_back).setOnClickListener(this);
        findViewById(R.id.is_fullscreen).setOnClickListener(this);
        findViewById(R.id.pptLayout).setOnClickListener(this);
        mPlay.setOnClickListener(this);
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
                    mHandler.sendEmptyMessageDelayed(DRAG_SHOW, 1500);

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
        ScreenSwitchUtils.getInstance(MainActivity.this).start(MainActivity.this);
        realHeight = screenHeight;
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        updateLayout();
        itemAdapter = new MsgItemAdapter(MainActivity.this, R.layout.item, list);
        lvMsg.setAdapter(itemAdapter);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            Glide.with(MainActivity.this)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1000000)
                                    .centerCrop()
                                    .error(mDefaultRes)
                                    .placeholder(mDefaultRes))
                    .load(ijkVideoUrl)
                    .into(mImageVideoView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用本地json数据解析
     */
    private void initAssetsData() {
        String input = Util.readFileFromAssets(this, "LiveClientNew.json");
        Gson gson = new Gson();
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        orderDrawManger.setListorderBean(listOrderBean);
    }

    /**
     * 回调白板指令
     */
    private void initOrderData() {
        String text = mDragIjkVideoView.getJsonMsg();
        if (!TextUtils.isEmpty(text) && text.length() > 0) {
            executeOrder(text);
        }
        mDragIjkVideoView.setCalReCallBackListenner(new OnGetMediaPlayInterface() {
            @Override
            public void GetMediaPlayerText(String text) {
                executeOrder(text);
            }
        });
    }

    /**
     * 处理指令的方法
     *
     * @param text 传递过来的内容
     */
    private void executeOrder(String text) {
        if (text != null && !"".equals(text)) {
            Gson gson = new Gson();
            try {
                OrderBean orderBean = gson.fromJson(text, OrderBean.class);
                //Log.e("SanHaiEdu","executeOrder text:"+text);
                mShow.setText("第"+orderBean.getSi()+"步");
                orderDrawManger.SetOrder(orderBean).ExecuteOrder();
            } catch (Exception e) {

            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        // 更新界面
        if (state == 0 || state == 2) {
            //[嘻嘻]霸道1234[嘻嘻][哈哈]
            SpannableString expressionString = ExpressionUtil.getExpressionString(this, event, "mipmap");
            Log.e("--------","main String:"+expressionString);
            mShow.setText(expressionString);
            sendMsg(event);
        } else {
            mToast.setText("连接互动服务失败");
            mToast.show();
        }
    }
    private static final int MSG_UPDATE_BOARD = 1;
    private static final int DRAG_SHOW = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DRAG_SHOW: {
                    mImageVideoView.setVisibility(View.GONE);
                    dragFrameLayout.setIsDrag(true);
                    mHandler.removeMessages(DRAG_SHOW);
                }
            }
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
            case R.id.btn_exchange:
                mToast.setText("交换正在开发中");
                mToast.show();
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
        }
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
     * 发送消息
     */
    private void sendMsg(String msgData) {
        if (!msgData.isEmpty()) {
            im_sdk.SendMsg(msgData, msgData.length());
            imm.hideSoftInputFromWindow(inputEdt_layout.getWindowToken(), 0);
            int serverTime = im_sdk.GetServerTime();
            String timeStr = Long.toString(serverTime);
            MsgContent item = new MsgContent(userName, timeStr, msgData);
            list.add(item);
            itemAdapter.notifyDataSetChanged();
        } else {
            mToast.setText("发送消息不允许为空!");
            mToast.show();
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
            showExitDialog("提示", "是否确认要退出?");
        }
    }

    /**
     * 退出的dialog
     */
    private void showExitDialog(String title, String msg) {
        normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);

        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        normalDialog = null;
                        finish();
                        ScreenSwitchUtils.getInstance(MainActivity.this).destroy();
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


    /**
     * 输入框的控制
     */
    private void showInputLayout() {
        if (mHandler == null) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!DimensionUtils.isPad(MainActivity.this) && ScreenSwitchUtils.getInstance(MainActivity.this).isFullScreen()) {
                    //输入框的隐藏
                    if (inputEdt_layout != null && lvMsg != null) {
                        inputEdt_layout.setVisibility(View.GONE);
                        lvMsg.setVisibility(View.GONE);
                    }
                } else {
                    //输入框的显示
                    if (inputEdt_layout != null && lvMsg != null) {
                        inputEdt_layout.setVisibility(View.VISIBLE);
                        lvMsg.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 100);
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
                lvMsg.setVisibility(View.VISIBLE);
                inputEdt_layout.setVisibility(View.VISIBLE);
            } else {  //横屏
                if (DimensionUtils.isPad(this)) {
                    pptLayoutWidth = (int) (width * 0.72);
                    height = pptLayoutWidth * 3 / 4;
                    pptLayout.setBackgroundColor(Color.BLACK);
                } else {
                    pptLayoutWidth = width;
                }
                lvMsg.setVisibility(View.GONE);
                inputEdt_layout.setVisibility(View.GONE);
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

    /**
     * IM服务的处理
     */
    private Handler mHandlerUI = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
            MsgContent m_Item = new MsgContent();
            if (dataBundle == null)
                return;
            int Type = dataBundle.getInt("type");

            switch (Type) {
                case 0:
                    state = dataBundle.getInt("state");
                    switch (state) {
                        case -1: {
                            inputEdt_layout.setEnabled(false);
                            loginStateView.setText("连接互动服务失败");
                            break;
                        }
                        case 0: {
                            inputEdt_layout.setEnabled(true);
                            loginStateView.setText("已连上互动服务");
                            break;
                        }
                        case 1: {
                            loginStateView.setText("检测到断开互动服务，正在重连");
                            break;
                        }
                        case 2: {
                            inputEdt_layout.setEnabled(true);
                            loginStateView.setText("已重连到互动服务");
                            break;
                        }
                        case 3: {
                            loginStateView.setText("网络异常，重连到互动服务失败");
                            break;
                        }
                    }
                    break;
                case 1:
                    resCode = dataBundle.getInt("resCode");
                    if (resCode == 1) {
                        mToast.setText("你发送信息太快了,请慢些");
                        mToast.show();
                    } else if (resCode == 2) {
                        mToast.setText("发送消息失败,请重发");
                        mToast.show();
                    }
                    break;
                case 3:
                    //发消息回调
                    if (resCode != 1 && resCode != 2) {
                        m_Item.setName(dataBundle.getString("name"));
                        m_Item.setMsgtime(dataBundle.getString("msgTime"));
                        m_Item.setMsgData(dataBundle.getString("msgData"));
                        list.add(m_Item);
                        itemAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    /**
     * 更新数据
     *
     * @param msgContent
     */
    @Override
    public void UpdateUIInterface(MsgContent msgContent) {
        Message msg = new Message();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("type", 3);
        dataBundle.putString("name", msgContent.getName());
        dataBundle.putString("msgTime", msgContent.getMsgtime());
        dataBundle.putString("msgData", msgContent.getMsgData());
        msg.setData(dataBundle);
        mHandlerUI.sendMessage(msg);
        return;
    }

    /**
     * 更新登陆状态
     *
     * @param state
     */
    @Override
    public void UpdateLoginInterface(int state) {
        Message msg = new Message();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("type", 0);
        dataBundle.putInt("state", state);
        msg.setData(dataBundle);
        mHandlerUI.sendMessage(msg);
        return;
    }

    /**
     * 更新发送消息的状态
     *
     * @param resCode
     * @param userID
     */
    @Override
    public void UpdateSendMsgDataStateInterface(int resCode, String userID) {
        Message msg = new Message();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("type", 1);
        dataBundle.putInt("resCode", resCode);
        dataBundle.putString("userID", userID);
        msg.setData(dataBundle);
        mHandlerUI.sendMessage(msg);
        return;
    }

    /**
     * 禁言回调接口
     *
     * @param sendType
     * @param resCode
     * @param forbidType
     * @param userId
     * @param username
     * @param time
     */
    @Override
    public void UpdateDisableSendStateInterface(int sendType, int resCode, int forbidType, String userId, String username, String time) {
        forbidUserId = userId;
        forbidUserName = userName;
        this.forbidType = forbidType;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        im_sdk.DisConnServer();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mHandlerUI.removeCallbacksAndMessages(null);
        mHandlerUI = null;
        whiteboardPresenter.release();
        //使用原生的so下面两行不影响，使用的编译的打卡会卡住
        //mDragIjkVideoView.stopPlayback();
        //mDragIjkVideoView.release(true);
        mDragIjkVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
        im_sdk.setCalReCallBackListenner(null);
        im_sdk = null;
        EventBus.getDefault().unregister(this);
    }
}
