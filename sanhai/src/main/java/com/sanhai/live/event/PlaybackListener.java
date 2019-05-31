package com.sanhai.live.event;

public interface PlaybackListener {
    void initSuccess();

    void onInitFail(String onFailMsg);
}
