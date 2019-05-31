package com.sanhai.live.event;

public interface LiveInListener {
    void onLaunch();

    void onInitFail(String var1);

    void onLiveStart();

    void onLiveStop();

    void memberForceout();

    void memberKick();
}
