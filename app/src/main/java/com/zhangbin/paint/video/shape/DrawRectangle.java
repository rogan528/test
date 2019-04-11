package com.zhangbin.paint.video.shape;

import android.graphics.Canvas;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画矩形
 */
public final class DrawRectangle
        extends BaseShape {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public DrawRectangle() {
        setDrawType(2);
    }

    public final void draw(Canvas canvas) {
        canvas.drawRect(this.left * this.scaleRatio, this.top * this.scaleRatio, this.right * this.scaleRatio, this.bottom * this.scaleRatio, this.paint);
    }

    public final void add(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        if (top > bottom) {
            this.top = bottom;
            this.bottom = top;
        }
        if (left > right) {
            this.left = right;
            this.right = left;
        }
    }

    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        this.left = orderBean.getX1();
        this.top = orderBean.getY1();
        this.right = orderBean.getX2();
        this.bottom = orderBean.getY2();
        this.paint.setColor(OperationUtils.getInstance().mCurrentPenColor);
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentPenSize);

    }
}

