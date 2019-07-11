package com.sanhai.live.module;

import java.io.Serializable;
import java.util.List;

public class PlaybackInfo implements Serializable {
    //private static final long serialVersionUID = -3566019675986703878L;
    private String liveId;
    private int views;
    private String duration = "0";
    private long durationLong = 0L;
    private String title;
    private boolean isAlbum = false;
    private int currentAlbumIndex = 0;
    private List<VideoInfo> videoInfoList;
    private List<CameraOperateInfo> cameraOperateInfoBeen;
    private static PlaybackInfo playbackInfo;

    public static PlaybackInfo getInstance() {
        if (playbackInfo == null) {
            Class var0 = PlaybackInfo.class;
            synchronized(PlaybackInfo.class) {
                if (playbackInfo == null) {
                    playbackInfo = new PlaybackInfo();
                }
            }
        }

        return playbackInfo;
    }

    public String getLiveId() {
        return this.liveId;
    }

    public void setLiveId(String var1) {
        this.liveId = var1;
    }

    public int getViews() {
        return this.views;
    }

    public void setViews(int var1) {
        this.views = var1;
    }

    @Deprecated
    public String getDuration() {
        return this.duration;
    }

    @Deprecated
    public void setDuration(String var1) {
        this.duration = var1;
    }

    public long getDurationLong() {
        return this.durationLong;
    }

    public void setDurationLong(Long var1) {
        this.durationLong = var1;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String var1) {
        this.title = var1;
    }

    public boolean isAlbum() {
        return this.isAlbum;
    }

    public void setIsAlbum(boolean var1) {
        this.isAlbum = var1;
    }

    public int getCurrentAlbumIndex() {
        return this.currentAlbumIndex;
    }

    public void setCurrentAlbumIndex(int var1) {
        this.currentAlbumIndex = var1;
    }

    public List<VideoInfo> getVideoInfoList() {
        return this.videoInfoList;
    }

    public void setVideoInfoList(List<VideoInfo> var1) {
        this.videoInfoList = var1;
    }

    public List<CameraOperateInfo> getCameraOperateInfoList() {
        return this.cameraOperateInfoBeen;
    }

    public void setCameraOperateInfoList(List<CameraOperateInfo> var1) {
        this.cameraOperateInfoBeen = var1;
    }

    public void destroy() {
        this.liveId = null;
        this.views = 0;
        this.duration = "0";
        this.title = null;
        this.isAlbum = false;
        this.currentAlbumIndex = 0;
        playbackInfo = null;
    }

    private PlaybackInfo() {
    }
}
