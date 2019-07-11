package com.sanhai.live.test;

import android.view.ViewGroup;

public class VideoViewPresenterImpl implements IShareDesktop{
    public void initVideoView() {
            /*this.c = new MtVideoView(this.k);
            this.c.setOnVideoStateChangeListener(this.o);
            this.c.setOnPreparedListener(this.q);
            this.c.setOnSeekListener(this.r);
            this.c.setVisibility(4);*/
    }
    public void setVideoContainer(ViewGroup var1) {
        this.removeFromContainer();
/*        this.a = var1;
        if (this.c != null) {
            this.c.setContainer(this.a);
        }*/

    }
    public void removeFromContainer() {
/*        if (this.c != null && this.a != null) {
            this.a.removeView(this.c);
        }*/

    }
    public void setDesktopVideoContainer(ViewGroup var1) {
        //this.b = var1;
    }
    @Override
    public void startShareDesktop(String var1) {

    }

    @Override
    public void stopShareDesktop() {

    }
}
