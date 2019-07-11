package com.sanhai.live.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;

import com.sanhai.live.module.OrderBean;


/**
 * 笔刷
 */
public class DrawBrush
        extends BaseShape {
    private Path path = new Path();

    public DrawBrush() {
        setDrawType(0);
    }

    @Override
    public void moveTo(float x, float y) {

    }


    public  void draw(Canvas canvas) {
        if ((this.dataList != null) && (this.dataList.size() > 0)) {
            this.path.reset();
            OrderBean.DataBean start = this.dataList.get(0);
            this.path.moveTo(start.getX() * this.scaleRatio, start.getY() * this.scaleRatio);
            for (int i = 0; i <= this.dataList.size() - 1; i++) {
                OrderBean.DataBean end = this.dataList.get(i);
                this.path.lineTo(end.getX() * this.scaleRatio, end.getY() * this.scaleRatio);
            }
        }
        canvas.drawPath(this.path, this.paint);
    }

    public final void linTo(float x, float y) {
        this.path.lineTo(x, y);
    }

    public final void move(float x, float y) {
        this.path.moveTo(x, y);
    }

    public final void linTo(float x1, float y1, float x2, float y2) {
        this.path.quadTo(x1, y1, x2, y2);
    }


    public void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        if (orderBean != null && orderBean.getPenColor() != null){
            this.paint.setColor(Color.parseColor(orderBean.getPenColor()));
        }
        this.strokeWidth = orderBean.getStrokeWidth();
    }
}


