package com.zhangbin.paint.video.shape;

import android.graphics.Canvas;

import com.zhangbin.paint.beans.OrderBean;

/**
 * 画带点线
 */
public final class DrawMove
        extends BaseDraw {
    public DrawMove() {
        setDrawType(10);
    }

    private float x;
    private float y;

    @Override
    public void moveTo(float x, float y) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    public void explainOrder(OrderBean orderBean) {
        if (orderBean == null) {
            throw new IllegalArgumentException();
        }
        //没有id 给个时间戳
        if (orderBean.getUuid() == null || orderBean.getUuid().equals("")) {
            orderBean.setUuid(System.currentTimeMillis() + "");
        }
        this.x = orderBean.getX();
        this.y = orderBean.getY();
        this.setId(orderBean.getUuid());


    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}


