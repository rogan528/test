package com.sanhai.live.module;

import java.io.Serializable;
import java.util.List;

public class VideoInfo implements Serializable {
    //private static final long serialVersionUID = -8492402609321039509L;
    private String type;
    private int st;
    private int et;
    private String url;
    private int duration;
    private List<String> urlList;
    private int index = 0;

    public VideoInfo() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String var1) {
        this.type = var1;
    }

    public int getStartTime() {
        return this.st;
    }

    public void setStartTime(int var1) {
        this.st = var1;
    }

    public int getEndTime() {
        return this.et;
    }

    public void setEndTime(int var1) {
        this.et = var1;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String var1) {
        this.url = var1;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int var1) {
        this.duration = var1;
    }

    public List<String> getUrlList() {
        return this.urlList;
    }

    public void setUrlList(List<String> var1) {
        this.urlList = var1;
    }

    public int getCurrentIndex() {
        return this.index;
    }

    public void setCurrentIndex(int var1) {
        this.index = var1;
    }
}
