package com.zhangbin.paint.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.zhangbin.paint.OrderHistoryStack;
import com.zhangbin.paint.video.shape.BaseDraw;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 白板控制类
 * 产生画板绘制部
 */
public final class PageWhite {

    /**
     * 主要获取画板布局的宽高和进行重绘部分
     */
    private WhiteDrawView whitedrawView;

//    private ImageView imageView;

    /**
     * 两个
     */
    private DrawLayerView drawFabricView;
    private DrawLayerView imageFabricView;
    //private HistoryOrder historyOrder;
    private OrderHistoryStack historyOrder;
    private float scaleRatio = 1.0F;
    int imageWidth = 0;
    int imageHeight = 0;
    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private int mWidth;
    private int mHeight;
    //是否是草稿纸
    private boolean isScratch = false;
    //暂无用
//    private float o = 0.0F;
    private int page = -1;
    private AtomicBoolean q = new AtomicBoolean(false);
    private boolean r = false;
    private Drawable drawable = null;
//    private boolean t = false;

    public PageWhite(boolean b, WhiteDrawView whitedrawView) {
        this.whitedrawView = whitedrawView;
//        this.imageView = whitedrawView.getImageView();
        this.drawFabricView = whitedrawView.getDrawFabricView();
        this.imageFabricView = whitedrawView.getImageFabricView();
        this.r = b;
        //this.historyOrder = new HistoryOrder();
        this.historyOrder = new OrderHistoryStack();
    }

    public final void drawObjA(final float x) {

//        this.o = height;
//        final int y2 = (int) (this.o * this.scaleRatio);
        final int y2 = (int) (this.scaleRatio);
        if (this.whitedrawView.getScaleY() == y2) {
            return;
        }

        this.whitedrawView.post(new RunnableScroll(this, x, y2));

//        this.whitedrawView.post(new Runnable() {
//            @Override
//            public void run() {
//                whitedrawView.scrollTo((int) x, y2);
//            }
//        });
    }

    public final void drawObjA(int toPage, BaseDraw baseDraw) {
        if ((this.historyOrder != null) && ((this.r) || (this.q.get()))) {
            this.historyOrder.clear(toPage, baseDraw);
        }
        if ((this.page == toPage) && (!this.q.get())) {
            this.imageFabricView.DrawLayerView(baseDraw);
        }
    }

    /**
     * 如果开始画
     *
     * @param toPage
     * @param draw
     */
    public final void drawObjB(int toPage, BaseDraw draw) {
        //如果初始为-1，并要进行绘画就跳到第一页
        if (this.page == -1) {
            ToPage(1);
        }
        if ((this.historyOrder != null) && ((this.r) || (this.q.get()))) {
            this.historyOrder.getDrawA(toPage, draw);
        }
        if ((this.page == toPage) && (!this.q.get())) {
            this.drawFabricView.DrawLayerView(draw);
        }
    }

    public final void drawObjA(int pageIndex) {
        if (this.historyOrder != null) {
            this.historyOrder.clear(pageIndex);
        }
        if ((this.page == pageIndex) && (!this.q.get())) {
            this.drawFabricView.DrawLayerView();
        }
    }

    public final boolean drawObjB(int pageIndex) {
        if (this.historyOrder != null) {
            return this.historyOrder.exist(pageIndex);
        }
        return false;
    }

    public final void drawObjA(Drawable paramDrawable) {
        this.drawable = paramDrawable;
    }


    /**
     * 跑到到指定页面
     *
     * @param pageIndex
     */
    public final void ToPage(int pageIndex) {


        boolean b = (this.page != pageIndex) || (!this.r);
        this.page = pageIndex;
        if (!this.r) {
            this.historyOrder.clear(pageIndex);
        }

        this.q.set(false);
        drawObjA(this.imageWidth, this.imageHeight);
        if (b) {
            drawObjB();
        }
        if (this.historyOrder != null) {
            drawObjA(this.historyOrder.getDrawA(pageIndex));
            drawObjB(this.historyOrder.getDrawB(pageIndex));
        }
    }

    /**
     * 设置绘画区域大小
     *
     * @param width
     * @param height
     */
    public void setWidthHeight(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }


    //是否可以调图片
    public boolean isImageload(Context context) {
        if (context == null) {
            return false;
        } else {
            Activity activity;
            return !(context instanceof Activity) || !(activity = (Activity) context).isDestroyed() && !activity.isFinishing();
        }
    }

    public final void drawObjA(CopyOnWriteArrayList<BaseDraw> drawLayerViews) {
        this.drawFabricView.setFabricViewDataList(drawLayerViews);
    }

    public final void drawObjB(CopyOnWriteArrayList<BaseDraw> drawLayerViews) {
        this.imageFabricView.setFabricViewDataList(drawLayerViews);
    }

    public final void drawObjA() {


        this.whitedrawView.postDelayed(new ToRun(this), 100L);
    }

    public final void drawObjA(int width, int height) {
        if ((width == 0) || (height == 0)) {
            return;
        }

        /**暂无使用 **/
//        FrameLayout.LayoutParams imageViewlayout;
//        imageViewlayout = (FrameLayout.LayoutParams) this.imageView.getLayoutParams();
//        imageViewlayout.gravity = Gravity.CENTER;


        FrameLayout.LayoutParams drawFabricViewLayout;
        drawFabricViewLayout = (FrameLayout.LayoutParams) this.drawFabricView.getLayoutParams();
        drawFabricViewLayout.gravity = Gravity.CENTER;


        float tWidth = this.mWidth;
        float tHeight = this.mHeight;
//        this.t = false;

        //是草稿纸
        if (this.isScratch) {
            this.scaleRatio = (tWidth / 800.0F);
//            imageViewlayout.gravity = 49;
            drawFabricViewLayout.gravity = 49;
        } else if (width < height) {
            Log.i("宽度高度宽度小于高度", "宽度：" + width + ",高度：" + height);

            tWidth = 0.025F * this.layoutWidth;
            tWidth = this.mWidth - tWidth;
            this.scaleRatio = (tWidth / width);
            tHeight = this.scaleRatio * height;
//            imageViewlayout.gravity = 49;
            drawFabricViewLayout.gravity = 49;


        } else {

            if (this.mWidth < this.layoutWidth) {
                Log.i("宽度高度新高度", "宽度：" + width + ",高度：" + height);
                tWidth = (float) this.mWidth / (float) width * (float) height;

                float localRatio = (float) this.layoutWidth / (float) this.mWidth;
                if (localRatio * tWidth > this.layoutHeight) {
                    localRatio = this.layoutHeight / tWidth;
                    tHeight = this.mHeight * localRatio;
                    tWidth = this.mWidth * localRatio;
                } else {
                    tHeight *= localRatio;
                    tWidth = this.layoutWidth;
                }


            }

            this.scaleRatio = (tWidth / width);
//            if (this.o != 0.0F) {
//                this.t = true;
//            }
        }
        int outWidth = Math.round(tWidth);
        height = Math.round(tHeight);
//        imageViewlayout.width = outWidth;
//        imageViewlayout.height = height;
//        this.imageView.setLayoutParams(layoutParams);


        drawFabricViewLayout.width = outWidth;
        drawFabricViewLayout.height = height;
        Log.i("宽度高度1", "输出宽度：" + outWidth + ",高度：" + height);

        this.drawFabricView.setLayoutParams(drawFabricViewLayout);
        this.drawFabricView.setScaleRatio(this.scaleRatio);
        this.imageFabricView.setScaleRatio(this.scaleRatio);
        Log.i("宽度高度比例", "输出比例：" + this.scaleRatio + "输出宽度：outWidth:" + outWidth);
//        this.drawFabricView.setScaleRatio( outWidth / 934f);
//        this.imageFabricView.setScaleRatio(outWidth / 934f);
//        this.drawFabricView.setScaleRatio(this.scaleRatio * outWidth / 934f);
//        this.imageFabricView.setScaleRatio(this.scaleRatio * outWidth / 934f);
//        if (this.t) {
//            this.drawFabricView.setScrollY((int) (this.o * this.scaleRatio));
//            this.imageFabricView.setScrollY((int) (this.o * this.scaleRatio));
//            this.whitedrawView.setScrollY(0);
//            Log.i("宽度高度2", "比率：" + this.scaleRatio + "比率2：" + this.o);
//            return;
//        }
        if (this.drawFabricView.getScrollY() != 0) {
            this.drawFabricView.setScrollY(0);
        }
        if (this.imageFabricView.getScrollY() != 0) {
            this.imageFabricView.setScrollY(0);
        }
        drawObjA(0.0F);

        Log.i("宽度高度3", "比率：" + this.scaleRatio);
    }


    public final void drawObjB() {
        this.imageFabricView.DrawLayerView();
        this.drawFabricView.DrawLayerView();
    }

    public final void clear() {
        if (this.historyOrder != null) {
            this.historyOrder.clear();
        }
        drawObjB();
    }

    public final void clearAll() {
        drawObjB();
        this.historyOrder.getDrawA();
        this.imageHeight = 0;
        this.imageWidth = 0;
//        ViewGroup.LayoutParams localLayoutParams;
//        (localLayoutParams = this.imageView.getLayoutParams()).width = -1;
//        localLayoutParams.height = -1;
    }

    final class RunnableScroll implements Runnable {
        private float a;
        private int b;
        private PageWhite c;

        RunnableScroll(final PageWhite pageWhite, final float x, final int y) {
            this.c = pageWhite;
            this.a = x;
            this.b = y;
        }

        @Override
        public final void run() {
            this.c.whitedrawView.scrollTo((int) this.a, this.b);
        }
    }

    final class ToRun
            implements Runnable {
        private PageWhite p;

        ToRun(PageWhite pageWhite) {
            if (this.p != null && this.p.equals(pageWhite)) {
                Log.i("相同", "");
                return;
            }
            this.p = pageWhite;
        }


        public final void run() {
            p.layoutWidth = whitedrawView.getWidth();
            p.layoutHeight = whitedrawView.getHeight();
            Log.i("宽度高度重新调整", "宽度：" + p.layoutWidth + ",高度：" + p.layoutHeight);
            if (p.layoutWidth <= 0 || p.layoutHeight <= 0) {
                return;
            }
            p.mHeight = p.layoutHeight;
            //转4：3比例
            p.mWidth = (p.layoutHeight << 2) / 3;


            Log.i("宽度高度重新调整", "宽度m：" + p.mWidth + ",高度m：" + p.mHeight);
            p.drawObjA(p.imageWidth, p.imageHeight);
            p.whitedrawView.invalidate();
        }
    }
}



