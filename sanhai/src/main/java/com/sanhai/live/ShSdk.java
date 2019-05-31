package com.sanhai.live;


import com.sanhai.live.event.PlaybackListener;
import com.sanhai.live.module.RoomInfo;
import com.sanhai.live.presenter.LivePresenter;
import com.sanhai.live.presenter.PlaybackPresenter;

import java.util.List;

/**
 * SDK初始化操作
 */
public class ShSdk implements LivePresenter , PlaybackPresenter {
    private static ShSdk shSdk = null;
    //private IM_SDK im_sdk;
    private LivePresenter livePresenter;
    private PlaybackPresenter playbackPresenter;

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
}
