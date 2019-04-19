package com.zhangbin.paint.video.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class VideoPresenter {
    private Context context;
    private ViewGroup c;
    protected VideoDragView videoDragView;
    private ViewGroup viewGroup;

    public VideoPresenter(Context context, ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.context = context;
        this.init();

    }

    public void init() {
        this.videoDragView = new VideoDragView(this.context);
        this.videoDragView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setPPTContainer(viewGroup);
        //this.videoDragView.init(true);

    }

    public void setPPTContainer(final ViewGroup c) {
        if (this.viewGroup == c && this.c != null && this.videoDragView != null && this.c.indexOfChild((View) this.videoDragView) != -1) {
            return;
        }
        Log.d("video", "设置ppt");
        this.removeFromContainer();
        this.c = c;
        if (this.videoDragView != null) {
            this.c.addView((View) this.videoDragView);
        }
    }

    public void removeFromContainer() {
        if (this.videoDragView == null) {
            return;
        }
        final ViewGroup viewGroup;
        if ((viewGroup = (ViewGroup) this.videoDragView.getParent()) != null) {
            viewGroup.removeView((View) this.videoDragView);
        }
    }
}