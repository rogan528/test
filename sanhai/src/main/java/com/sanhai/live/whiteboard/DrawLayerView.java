package com.sanhai.live.whiteboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sanhai.live.whiteboard.shape.BaseDraw;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName whiteDrawView
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/28 下午4:01
 */
public class DrawLayerView
        extends View {
    private float strokeWidth = 5;
    private int color = Color.RED;
    private int textSize = 15;

    /**
     * 是否在所有页进行绘画指定信息，注：所有页
     */
    private boolean isDrawAll = false;
    private float scaleRatio = 1.0F;
    private Paint paint;
    private Paint.Style style = Paint.Style.STROKE;
    private int drawType = 0;
    private List<BaseDraw> fabricViewDataList = new CopyOnWriteArrayList();
    private List<BaseDraw> undoDrawableList = new CopyOnWriteArrayList();
    private List<BaseDraw> redoDrawableList = new CopyOnWriteArrayList();
    private CallBack callBack;
    private int layerId = 0;

    /**
     * @param context
     */
    public DrawLayerView(Context context) {
        this(context, null);

    }

    private DrawLayerView(Context context, AttributeSet attributeSet) {
        this(context, null, 0);
    }

    private DrawLayerView(Context context, AttributeSet attributeSet, int i) {

        super(context, attributeSet, 0);

        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置硬件加速与橡皮擦有关
        setLayerType(LAYER_TYPE_HARDWARE, null);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.color);
        this.paint.setStyle(this.style);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(this.strokeWidth);

    }

    protected final void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.translate(getWidth() >> 1, getHeight() >> 1);
        for (BaseDraw next : this.fabricViewDataList) {
            next.setScaleRatio(this.scaleRatio);
            next.draw(canvas);
        }

        if (this.callBack != null) {

            if (this.isDrawAll) {
                this.callBack.onDrawBack(this.fabricViewDataList, this.undoDrawableList, this.redoDrawableList, null);
                this.isDrawAll = false;
            }
        }
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public final void setPaint(Paint paint) {
        if (paint != null) {
            this.paint = paint;
        }
    }

    public final Paint.Style getStyle() {
        return this.style;
    }

    public final void setStyle(Paint.Style style) {
        this.style = style;
        this.paint.setStyle(style);
    }

    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    public final void setStrokeWidth(float width) {
        this.strokeWidth = width;
        this.paint.setStrokeWidth(width);
    }

    public final void setTextSize(int size) {
        this.textSize = size;
    }

    public final int getTextSize() {
        return this.textSize;
    }

    public final void setScaleRatio(float scaleRatio) {
        if (this.scaleRatio == scaleRatio) {
            return;
        }
        this.scaleRatio = scaleRatio;
    }

    public final void setCmdScaleRatio(float cmdScaleRatio) {
    }

    public final void setCmdPPTRatio(float cmdPPTRatio) {
    }


    public final int getDrawType() {
        return this.drawType;
    }

    public final void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public final List<BaseDraw> getAllPointList() {
        if (this.fabricViewDataList.size() > 0) {
            return this.fabricViewDataList;
        }
        return null;
    }

    public final List<BaseDraw> getUndoDrawableList() {
        return this.undoDrawableList;
    }

    public final List<BaseDraw> getRedoDrawableList() {
        return this.redoDrawableList;
    }

    public final void setFabricViewDataList(CopyOnWriteArrayList<BaseDraw> paramCopyOnWriteArrayList) {
        if ((paramCopyOnWriteArrayList != null) && (this.fabricViewDataList != null)) {
            this.fabricViewDataList.clear();
            this.fabricViewDataList.addAll(paramCopyOnWriteArrayList);
            invalidate();
        }
    }

    /**
     * 进行绘画
     *
     * @param draw
     */
    public final void DrawLayerView(BaseDraw draw) {
        if (this.fabricViewDataList != null) {
            for (BaseDraw localDraw : this.fabricViewDataList) {
                if (localDraw == null) {
                    continue;
                }
                //清除重复id 的绘画
                if (localDraw.getId() != null && !"".equals(localDraw.getId()) && draw.getId() != null && !"".equals(draw.getId())) {
                    if (localDraw.getId().equals(draw.getId())) {
                        this.fabricViewDataList.remove(localDraw);
                        break;
                    }
                }
            }

            if (draw.getIsShow()) {
                this.fabricViewDataList.add(draw);
                this.undoDrawableList.add(draw);
            }

            invalidate();
        }
    }

    public final void DrawLayerViewC(BaseDraw draw) {
        if (this.fabricViewDataList != null) {
            for (BaseDraw localDraw : this.fabricViewDataList) {
                if (localDraw == null) {
                    continue;
                }
                //清除重复id 的绘画
                if (localDraw.getId() != null && !"".equals(localDraw.getId()) && draw.getId() != null && !"".equals(draw.getId())) {
                    if (localDraw.getId().equals(draw.getId())) {
                        this.fabricViewDataList.remove(localDraw);

                        break;
                    }
                }
            }
            if (draw.getIsShow()) {
                this.fabricViewDataList.add(draw);
            }
        }
    }

    /**
     * 501指令,撤销
     */
    public void undo() {
        int size = undoDrawableList.size();
        if (size == 0) {
            return;
        }

        this.fabricViewDataList.clear();
        redoDrawableList.add(undoDrawableList.get(size - 1));
        undoDrawableList.remove(size - 1);
        isDrawAll = true;
        redrawOnBitmap();

    }

    /**
     * 502指令,恢复
     */
    public void redo() {
        int size = redoDrawableList.size();
        if (size == 0) {
            return;
        }
        undoDrawableList.add(redoDrawableList.get(size - 1));
        redoDrawableList.remove(size - 1);
        isDrawAll = true;
        redrawOnBitmap();

    }

    //将剩下的path重绘
    private void redrawOnBitmap() {
        for (BaseDraw localDraw : undoDrawableList) {
            if (localDraw == null) {
                continue;
            }
            this.DrawLayerViewC(localDraw);
        }
        // 刷新
        invalidate();
    }

    /**
     * 清除刷新
     */
    public final void clear() {
        this.undoDrawableList.clear();
        this.fabricViewDataList.clear();
        this.redoDrawableList.clear();
        invalidate();
    }

    public final void setOnDrawListener(CallBack callBack) {
        this.callBack = callBack;
    }


    public int getLayerId() {
        return layerId;
    }

    //设置层id
    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }
}