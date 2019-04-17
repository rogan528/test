package com.zhangbin.paint.whiteboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.VideoView;

import java.lang.reflect.Field;

public class DragVideoView extends VideoView {
    private int width, height;

    private int screenWidth, screenHeight;
    private int left, right, top, bottom;
    private int[] location = new int[2];
    private int margin = 0;
    private int lastX, lastY;

    public DragVideoView(Context context) {
        super(context);
        getScreenInformation(context);
    }

    private void getScreenInformation(Context context) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        //screenHeight = dm.heightPixels - getStatusHeight(context) -dp2px(context, 102);
        screenHeight = dm.heightPixels;
        Log.e("DragVideoView","screenWidth:"+screenWidth+"screenHeight:"+screenHeight);
    }

    public DragVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenInformation(context);
    }

    public DragVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getScreenInformation(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*width = getMeasuredWidth();
        height = getMeasuredHeight();*/
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        //screenHeight = dm.heightPixels - getStatusHeight(context) -dp2px(context, 102);
        screenHeight = dm.heightPixels;
        width = screenWidth;
        height = screenHeight;
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