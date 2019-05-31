package com.sanhai.live.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommonJson implements Serializable {

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;
    @SerializedName("timestamp")
    private String timestamp;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CommonJson{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
