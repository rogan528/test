package com.zhangbin.paint.video.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画文字
 */
public final class DrawText
        extends BaseShape {
    private String text;
    private float size = 25;
    private float x = 0;
    private float y = 0;
    private Context n;

    public DrawText() {
        setDrawType(5);
    }

    private DrawText(String s, int y, int x) {
        this();
        this.text = s;
        this.y = y;
        this.x = x;
    }

    @Override
    public void moveTo(float x, float y) {
        super.moveOffset(x - this.x, y - this.y);
        this.x = this.x + this.offsetX;
        this.y = this.y + this.offsetY;

    }

    public DrawText(Context context, String s, int y, int x, Paint paint) {
        this(s, y, x);
        this.paint = new Paint(paint);
        this.n = context;
    }

    public final void size(float size) {
        this.size = size;
        this.paint.setTextSize(this.size);
    }

    public final void draw(Canvas canvas) {
        this.paint.setTextSize(this.size * this.scaleRatio);
        canvas.drawText(this.text, this.x + this.scaleRatio, this.y * this.scaleRatio + this.size * this.scaleRatio, this.paint);
    }

    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        text = orderBean.getText();
        x = (int) orderBean.getX();
        y = (int) orderBean.getY();
        this.size = OperationUtils.getInstance().mCurrentTextSize;
        this.paint.setTextSize(OperationUtils.getInstance().mCurrentTextSize);
        this.paint.setColor(OperationUtils.getInstance().mCurrentTextColor);

    }
}




