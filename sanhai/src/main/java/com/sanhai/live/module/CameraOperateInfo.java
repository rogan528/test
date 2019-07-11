package com.sanhai.live.module;

public class CameraOperateInfo {
    public static final String OPEN = "101";
    public static final String CLOSE = "102";
    private String type;
    private int time;

    public CameraOperateInfo() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String var1) {
        this.type = var1;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int var1) {
        this.time = var1;
    }
}
