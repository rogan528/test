package com.sanhai.live;


import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sanhai.live.event.ListenerManager;
import com.sanhai.live.event.OnPlayerLoadStateChangeListener;
import com.sanhai.live.event.PlaybackListener;
import com.sanhai.live.module.RoomInfo;
import com.sanhai.live.presenter.LivePresenter;
import com.sanhai.live.presenter.PlaybackPresenter;
import com.sanhai.live.test.VideoViewPresenterImpl;
import com.sanhai.live.test.WhiteboardPresenterImpl;

import java.util.List;

/**
 * SDK初始化操作
 */
public class ShSdk implements LivePresenter , PlaybackPresenter {
    private static ShSdk shSdk = null;
    //private IM_SDK im_sdk;
    private LivePresenter livePresenter;
    private PlaybackPresenter playbackPresenter;
    private Context mContext;
    protected WhiteboardPresenterImpl whiteboardPresenter;
    protected VideoViewPresenterImpl videoViewPresenter;
    public static ShSdk getInstance() {
        if (shSdk == null) {
            synchronized(ShSdk.class) {
                if (shSdk == null) {
                    shSdk = new ShSdk();
                }
            }
        }

        return shSdk;
    }

    /**
     * 初始化操作
     */
    public void init() {
        //im_sdk = new IM_SDK();
        //im_sdk.InitSDK(initType, userId, userName, groupID, "",Constatans.serverImIP, "", ipPort);
        //im_sdk.ConnectMsgServer();
    }
    public void init(ViewGroup pptContainer, ViewGroup videoViewContainer, String mToken, boolean isFlag) {
        this.mContext = pptContainer.getContext();
        if (isFlag) {
            /*n var5 = new n(var1, var2, var3);
            this.b = var5;
            this.d = var5;*/
            Context var6 = pptContainer.getContext();
            if (pptContainer == null) {
                throw new NullPointerException("ppt view container can not null");
            }else if (videoViewContainer == null) {
                throw new NullPointerException("Video view container can not null");
            }
            init(pptContainer,videoViewContainer);
        } else {
            this.init(pptContainer, videoViewContainer, mToken);
        }
    }
    public void init(ViewGroup var1, ViewGroup var2, String var3) {
/*        c var4 = new c(var1, var2, var3);
        this.b = var4;
        this.c = var4;*/
    }
    protected final void init(ViewGroup pptContainer, ViewGroup videoViewContainer) {
        if (this.whiteboardPresenter != null && this.videoViewPresenter != null) {
            this.whiteboardPresenter.setPPTContainer(pptContainer);
            this.videoViewPresenter.initVideoView();
            this.videoViewPresenter.setVideoContainer(videoViewContainer);
            this.videoViewPresenter.setDesktopVideoContainer(pptContainer);
        }
    }
    public long getVideoCurrentTime() {
        //return this.b == null ? 0L : this.b.g();
        return 0;
    }
    /**
     * 获取房间信息
     * @return 获取房间信息
     */
    public RoomInfo getRoomInfo(){
        return this.livePresenter != null ? this.livePresenter.getRoomInfo() : null;
    }
    /**
     *
     * @return
     */
    @Override
    public boolean isLiving() {
        return this.livePresenter != null ? this.livePresenter.isLiving() : false;
    }

    /**
     * 白板和视频交换
     */
    public void exchangeVideoAndWhiteboard(Context context) {
        Toast mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mToast.setText("交换正在开发中");
        mToast.show();
    }
    /**
     * 释放操作
     */
    public void release() {
        //im_sdk.DisConnServer();
        //IjkMediaPlayer.native_profileEnd();
        //mDragIjkVideoView.stopBackgroundPlay();
    }

    @Override
    public void playbackResumeVideo() {
        if (this.playbackPresenter != null) {
            this.playbackPresenter.playbackResumeVideo();
        }

    }
    public void playbackResume() {
        this.playbackResumeVideo();
    }
    @Override
    public void playbackPauseVideo() {
        if (this.playbackPresenter != null) {
            this.playbackPresenter.playbackPauseVideo();
        }

    }
    public void playbackPause() {
        this.playbackPauseVideo();
    }
    @Override
    public void playbackStop() {
        if (this.playbackPresenter != null) {
            this.playbackPresenter.playbackStop();
        }

        //PlaybackDataManage.getInstance().pauseAutoScroll();
    }

    @Override
    public void playbackSeekTo(long msec) {
        if (this.playbackPresenter != null) {
            this.playbackPresenter.playbackSeekTo((long)((int)msec));
        }
    }

    @Override
    public void playbackImmediatelySeekTo(long var1) {

    }

    @Override
    public void setPlaybackListener(PlaybackListener var1) {

    }

    @Override
    public void setFilterQuestionFlag(boolean var1) {

    }

    @Override
    public void replayVideo() {
        if (this.playbackPresenter != null) {
            this.playbackPresenter.replayVideo();
        }
    }

    @Override
    public List<String> getPlaybackNetworkLines() {
        return null;
    }

    @Override
    public void setPlaybackNetworkLine(String var1) {

    }

    @Override
    public void setPlaybackPlaySpeed(float var1) {

    }

    @Override
    public float getPlaybackPlaySpeed() {
        return 0;
    }
    public void setOnPlayerLoadStateChangeListener(OnPlayerLoadStateChangeListener var1) {
        Log.d("设置视频缓冲状态监听","设置视频缓冲状态监听");
        ListenerManager.getInstance().setListener("playerLoadStateChangeListener", var1);
    }
}
