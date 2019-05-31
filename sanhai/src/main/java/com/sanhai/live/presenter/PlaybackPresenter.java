package com.sanhai.live.presenter;

import com.sanhai.live.event.PlaybackListener;

import java.util.List;

public interface PlaybackPresenter {
    void playbackResumeVideo();

    void playbackPauseVideo();

    void playbackStop();

    void playbackSeekTo(long msec);

    void playbackImmediatelySeekTo(long var1);

    void setPlaybackListener(PlaybackListener var1);

    void setFilterQuestionFlag(boolean var1);

    void replayVideo();

    List<String> getPlaybackNetworkLines();

    void setPlaybackNetworkLine(String var1);

    void setPlaybackPlaySpeed(float var1);

    float getPlaybackPlaySpeed();

    //ModuleConfigHelper getModuleConfigHelper();
}
