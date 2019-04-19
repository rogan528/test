package com.zhangbin.paint.video.presenter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.zhangbin.paint.whiteboard.PageWhite;
import com.zhangbin.paint.whiteboard.shape.BaseDraw;

import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;


public final class VideoDragView extends FrameLayout {


    private IjkDragVideoView ijkDragVideoView;
    private FrameLayout frameLayout;
    private PageWhite pageWhite;
    private int screenWidth, screenHeight;
    private int width, height;
    private int left, right, top, bottom;
    private int lastX, lastY;
    public VideoDragView(Context context) {
        this(context, null);
    }

    private VideoDragView(Context context, AttributeSet attributeSet) {
        this(context, null, 0);
    }

    private VideoDragView(Context context, AttributeSet attributeSet, int index) {
        super(context, attributeSet, 0);
        this.frameLayout = new FrameLayout(this.getContext());
        this.ijkDragVideoView = new IjkDragVideoView(this.getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.frameLayout.addView(this.ijkDragVideoView, layoutParams);
        this.addView(this.frameLayout, index);
        getScreenInformation(context);
    }


    /**
     * 初始化
     *
     * @param isClient
     */
    public final void init(boolean isClient) {
        /*this.pageWhite = new PageWhite(isClient, this);
        //要设置原始ppt 大小以便进行缩放控制
        this.pageWhite.setWidthHeight(934, 508);*/
//        }

    }

    private void getScreenInformation(Context context) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        width = screenWidth;
        height = screenHeight;
    }
    public final FrameLayout getFrameLayout() {
        return this.frameLayout;
    }
    public IjkDragVideoView getIjkDragVideoView() {
        return ijkDragVideoView;
    }

    public void setIjkDragVideoView(IjkDragVideoView ijkDragVideoView) {
        this.ijkDragVideoView = ijkDragVideoView;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                left = getLeft() + dx;
                top = getTop() + dy;
                right = getRight() + dx;
                bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }

                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }

                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (top > screenHeight) {
                    top = screenHeight;
                }

                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                    layout(left, top, right, bottom);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "ACTION_UP    ");
                break;
        }
        return super.onTouchEvent(event);
    }
}
