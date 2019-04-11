package com.zhangbin.paint.video.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;
import com.zhangbin.paint.video.WhiteDrawView;
import com.zhangbin.paint.video.shape.BaseDraw;
import com.zhangbin.paint.video.shape.DrawFactory;

/**
 * @ClassName fffff
 * @Description TODO
 * @Author yangjie
 * @Date 2019/4/1 下午1:46
 */
public class WhiteboardPresenter {
    private Context context;
    private ViewGroup c;
    protected WhiteDrawView whiteDrawView;
    private PageCommandCallback f;
    private ViewGroup viewGroup;
    private int indexPage;
    public WhiteboardPresenter(Context context, ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.context = context;
        this.init();

    }

    public void init() {
        this.whiteDrawView = new WhiteDrawView(this.context);
        this.whiteDrawView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setPPTContainer(viewGroup);
        this.whiteDrawView.init(this.indexPage, true);

    }

    public void setPPTContainer(final ViewGroup c) {
        if (this.viewGroup == c && this.c != null && this.whiteDrawView != null && this.c.indexOfChild((View) this.whiteDrawView) != -1) {
            return;
        }
        Log.d("video", "\u8bbe\u7f6e\u767d\u677f\u5bb9\u5668");
        this.removeFromContainer();
        this.c = c;
        if (this.whiteDrawView != null) {
            this.c.addView((View) this.whiteDrawView);
        }
    }

    public void setWhiteboardLoadFailDrawable(final Drawable pptLoadFailDrawable) {
        if (this.whiteDrawView != null) {
            this.whiteDrawView.setPPTLoadFailDrawable(pptLoadFailDrawable);
        }
    }

    public void removeFromContainer() {
        if (this.whiteDrawView == null) {
            return;
        }
        final ViewGroup viewGroup;
        if ((viewGroup = (ViewGroup) this.whiteDrawView.getParent()) != null) {
            viewGroup.removeView((View) this.whiteDrawView);
        }
    }

    public void setWhiteboardBackgroundColor(final int backgroundColor) {
        if (this.whiteDrawView == null) {
            return;
        }
        Log.i("video", "\u8bbe\u7f6e\u767d\u677f\u80cc\u666f\u989c\u8272\uff0ccolor:" + backgroundColor);
        this.whiteDrawView.setBackgroundColor(backgroundColor);
    }

    public void release() {
        if (this.c != null) {
            this.c.removeView((View) this.whiteDrawView);
        }
    }

    /**
     * 305指令  画笔大小
     * @param penSize
     */
    public void setPaintSize(float penSize){
        //this.whiteDrawView.setStrokeWidth(strokeWidth);
        OperationUtils.getInstance().mCurrentPenSize = penSize;
    }

    /**
     * 306指令 画笔颜色
     *
     * @param paintColor
     */
    public void setPaintColor(int paintColor){
        //this.whiteDrawView.setPaintColor(paintColor);
        OperationUtils.getInstance().mCurrentPenColor = paintColor;
    }

    /**
     * 307指令 橡皮大小
     *
     * @param eraserSize
     */
    public void setReaserSize(float eraserSize){
        OperationUtils.getInstance().mCurrentEraserSize = eraserSize;
    }

    /**
     * 308指令 设置文字字号
     * @param textSize
     */
    public void setTextSize(int textSize){
        OperationUtils.getInstance().mCurrentTextSize = textSize;
    }

    /**
     * 309指令 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor){
        OperationUtils.getInstance().mCurrentTextColor = textColor;
    }

    /**
     * 400-408指令
     *
     * @param s
     */
    public void addDrawData(final OrderBean s) {
        if (this.whiteDrawView == null) {
            return;
        }
        final BaseDraw pageDrawable = DrawFactory.createPageDrawable(s);
        if (pageDrawable == null) {
            return;
        }
        this.whiteDrawView.drawObj(this.indexPage, pageDrawable);
    }

    /**
     * 500指令,清除
     *
     */
    public void orderClear() {
        clearPageDraw(this.indexPage);
    }

    /**
     * 501指令,撤销
     *
     */
    public void undo() {
        whiteDrawView.undo();
    }

    /**
     * 502指令,恢复
     */
    public void redo(){
        whiteDrawView.redo();
    }

    /**
     * 503指令,跳转指定页面
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void jumpPage(int currentPage, int currentAnimation) {
        this.whiteDrawView.jumpPage(currentPage, currentAnimation);
        this.indexPage = currentPage;
    }

    /**
     * 504指令,上一页
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void lastSlideS(int currentPage, int currentAnimation) {
        this.whiteDrawView.lastSlideS(currentPage, currentAnimation);
        this.indexPage = currentPage;

    }

    /**
     * 505指令,下一页
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void nextSlideS(int currentPage, int currentAnimation) {
        this.whiteDrawView.nextSlideS(currentPage, currentAnimation);
        this.indexPage = currentPage;
    }

    public void preloadPage(final String s) {
        if (TextUtils.isEmpty((CharSequence) s) || this.whiteDrawView == null) {
            return;
        }
        Log.d("video", "\u9884\u52a0\u8f7d:" + s);
        this.whiteDrawView.init(s);
    }


    public void clearPageDraw(int pageIndex) {
        if (this.whiteDrawView == null) {
            return;
        }
//        pageIndex = this.indexPage;
        Log.d("video", String.format("清除指定页涂鸦", pageIndex));
        this.whiteDrawView.init(pageIndex);
    }


    public void clearAllDraw() {
        if (this.whiteDrawView == null) {
            return;
        }
        Log.d("video", "擦除全部涂鸦");
        this.whiteDrawView.init();
    }





    public void clearPage() {
        if (this.whiteDrawView == null) {
            return;
        }
        Log.d("video", "\u64e6\u9664");
        this.whiteDrawView.drawObj();
    }

    public void clearAll() {

        if (this.whiteDrawView != null) {
            this.whiteDrawView.clearAll();
        }
    }

    public void setPageCommandCallback(final PageCommandCallback f) {
        this.f = f;
    }

    public interface PageCommandCallback {
        void getPageCommand(final int p0);
    }
}