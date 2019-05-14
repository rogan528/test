package com.zhangbin.paint.whiteboard.shape;

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
    private float height = 0;
    //行高系数
    private float lineDistanceHeightRatio = 0.3f;
    private Context n;
    private float sizeRatio = OperationUtils.getInstance().mCurrentTextSizeRatio;

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
        height = 0;
        this.paint.setTextSize(this.size * this.scaleRatio * sizeRatio);
        this.paint.setSubpixelText(true);
        for (String line : this.text.split("\n")) {
            this.lineBreak(canvas, line);
            height = height + getLineHeight();
        }


    }

    private float getLineHeight() {
        return this.size * this.scaleRatio + this.size * this.scaleRatio * this.lineDistanceHeightRatio;
    }


    /**
     * @param canvas
     * @param str
     */
    private void lineBreak(Canvas canvas, String str) {
        //计算当前宽度(width)能显示多少个汉字
        //以主心为坐标系，取宽度除以2
        int subIndex;
        if(width==0){
            subIndex = this.paint.breakText(str, 0, str.length(), true, (canvas.getWidth() >> 1) - this.x * this.scaleRatio, null);
        }else {
            subIndex = this.paint.breakText(str, 0, str.length(), true, this.width* this.scaleRatio, null);
        }

        //截取可以显示的汉字
        String mytext = str.substring(0, subIndex);
        canvas.drawText(mytext, this.x * this.scaleRatio, this.y * this.scaleRatio + sizeRatio * this.size * this.scaleRatio + height, this.paint);

        //计算剩下的汉字
        String ss = str.substring(subIndex);
        if (ss.length() > 0) {
            height = height + getLineHeight();
            //行距 为高度0.2倍
            lineBreak(canvas, ss);
        }
    }



    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        text = orderBean.getText();
        x = orderBean.getX();
        y = orderBean.getY();
        width=orderBean.getW();

        this.size = OperationUtils.getInstance().mCurrentTextSize;
        this.paint.setColor(OperationUtils.getInstance().mCurrentTextColor);

    }
}




