package com.zhangbin.paint.video.shape;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画圆
 */
public final class DrawCircle
        extends BaseShape {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public DrawCircle() {
        setDrawType(3);
    }

    public final void draw(Canvas canvas) {
        RectF rectF = new RectF(this.left * this.scaleRatio,
                this.top * this.scaleRatio,
                this.right * this.scaleRatio,
                this.bottom * this.scaleRatio);
        canvas.drawOval(rectF, this.paint);
    }

    @Override
    public void moveTo(float x, float y) {

        super.moveOffset(x - this.left, y - this.top);
        this.left = this.left + this.offsetX;
        this.top = this.top + this.offsetY;
        this.right = this.right + this.offsetX;
        this.bottom = this.bottom + this.offsetY;
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

    public void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        this.left = orderBean.getX1();
        this.top = orderBean.getY1();
        this.right = orderBean.getX2();
        this.bottom = orderBean.getY2();
        this.strokeWidth = OperationUtils.getInstance().mCurrentPenSize;
        this.paint.setColor(OperationUtils.getInstance().mCurrentPenColor);
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentPenSize);
    }
}


