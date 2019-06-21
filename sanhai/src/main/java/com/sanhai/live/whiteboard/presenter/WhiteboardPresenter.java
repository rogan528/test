package com.sanhai.live.whiteboard.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.sanhai.live.module.LiveBoard;
import com.sanhai.live.module.OrderBean;
import com.sanhai.live.consts.Constatans;
import com.sanhai.live.util.HttpCallBack;
import com.sanhai.live.util.HttpUtil;
import com.sanhai.live.util.JsonUtil;
import com.sanhai.live.util.OperationUtils;
import com.sanhai.live.util.Util;
import com.sanhai.live.whiteboard.WhiteDrawView;
import com.sanhai.live.whiteboard.shape.BaseDraw;
import com.sanhai.live.whiteboard.shape.DrawFactory;

import java.util.List;

import static android.view.View.INVISIBLE;

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
    private int indexPage = OperationUtils.getInstance().mCurrentPage;
    private float backpenSize = 2;
    private int backPenColor = Color.RED;
    private float backEraserSize = 5;
    private float backTextSize = 50;
    private int backTextColor = Color.RED;

    public WhiteboardPresenter(Context context, ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.context = context;
        /*float scale = context.getResources().getDisplayMetrics().density;
        float ratio = (scale / 160) * 72;
        OperationUtils.getInstance().mCurrentTextSizeRatio = ratio;*/
        this.init();

    }

    public void init() {
        this.whiteDrawView = new WhiteDrawView(this.context);
        this.whiteDrawView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setPPTContainer(viewGroup);
        this.whiteDrawView.init(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                testPage(OperationUtils.getInstance().mCurrentPage, new WhiteDrawView.ICallBack() {
                    @Override
                    public void postExec() {
                        jumpPage(OperationUtils.getInstance().mCurrentPage, 1);
                    }
                });

            }
        }, 1000);

    }


    private void requestNetPageOrder(final int currentPage) {
        if (indexPage <= 0) {
            return;
        }

        String liveBoardUrl = Constatans.liveBoardUrl + "?liveId=" + OperationUtils.getInstance().mLiveId +
                "&pptId=" + OperationUtils.getInstance().mPptId + "&page=" + currentPage;

        HttpUtil.get(liveBoardUrl, new HttpCallBack<LiveBoard>() {

            @Override
            public void onFailure(String message) {

            }

            @Override
            public <T> void onSuccess(T json) {
                LiveBoard j = (LiveBoard) json;
                if (j != null && j.getData() != null) {
                    Log.i("httpRequest", j.getData().size() + "");
                    List<LiveBoard.DataBean> data = j.getData();
                    OrderBean jumporderBean = null;
                    for (int i = 0; i < data.size(); i++) {
                        final OrderBean orderBean = data.get(i).getOrderBean();
                        if (orderBean == null) {
                            continue;
                        }
                        int type = Util.toInteger(orderBean.getType());
                        //排除跑转指令
                        if (type == 503) {
                            continue;
                        }


                        //排除上一步和下一步不是当前页指令
                        if (type > 503 && orderBean.getCurrentPage() != currentPage) {
                            continue;
                        }

                        if (type == 503 || type == 504 || type == 505) {
                            jumporderBean = orderBean;
                            continue;
                        }
                        Log.i("httpRequestJson", JsonUtil.toJson(orderBean) + "");


                        ExecuteOrder(orderBean);

                    }

                    if (jumporderBean != null) {
                        ExecuteOrder(jumporderBean);
                        Log.i("httpRequestJson", JsonUtil.toJson(jumporderBean) + "");
                    }

                }
            }
        });

    }

    public void setPPTContainer(final ViewGroup viewGroup) {
        if (this.viewGroup == viewGroup && this.c != null && this.whiteDrawView != null && this.c.indexOfChild((View) this.whiteDrawView) != -1) {
            return;
        }
        Log.d("video", "设置ppt");
        this.removeFromContainer();
        this.c = viewGroup;
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

    /**
     * 设置PPT的背景色
     *
     * @param backgroundColor
     */
    public void setWhiteboardBackgroundColor(final int backgroundColor) {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.setBackgroundColor(backgroundColor);
    }

    public void release() {
        if (this.c != null) {
            this.c.removeView((View) this.whiteDrawView);
        }
    }

    /**
     * 305指令  画笔大小
     *
     * @param penSize
     */
    public void setPaintSize(float penSize) {
        OperationUtils.getInstance().mCurrentPenSize = penSize;
    }

    /**
     * 306指令 画笔颜色
     *
     * @param paintColor
     */
    public void setPaintColor(int paintColor) {
        OperationUtils.getInstance().mCurrentPenColor = paintColor;
    }

    /**
     * 307指令 橡皮大小
     *
     * @param eraserSize
     */
    public void setReaserSize(float eraserSize) {
        OperationUtils.getInstance().mCurrentEraserSize = eraserSize;
    }

    /**
     * 308指令 设置文字字号
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        OperationUtils.getInstance().mCurrentTextSize = textSize;
    }

    /**
     * 309指令 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
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
     * 410 打开草稿纸
     */
    public void openDraftPaper(int addDraftPage) {
        OperationUtils.getInstance().mBackPage = this.indexPage;
        backpenSize = OperationUtils.getInstance().mCurrentPenSize;
        backPenColor = OperationUtils.getInstance().mCurrentPenColor;
        backEraserSize = OperationUtils.getInstance().mCurrentEraserSize;
        backTextSize = OperationUtils.getInstance().mCurrentTextSize;
        backTextColor = OperationUtils.getInstance().mCurrentTextColor;
        setWhiteboardBackgroundColor(Color.WHITE);
        this.whiteDrawView.openDraftPaper(addDraftPage);
        this.indexPage = addDraftPage;
    }

    /**
     * 411 关闭草稿纸
     */
    public void closeDraftPaper() {
        OperationUtils.getInstance().mCurrentPenSize = backpenSize;
        OperationUtils.getInstance().mCurrentPenColor = backPenColor;
        OperationUtils.getInstance().mCurrentEraserSize = backEraserSize;
        OperationUtils.getInstance().mCurrentTextSize = backTextSize;
        OperationUtils.getInstance().mCurrentTextColor = backTextColor;
        this.whiteDrawView.closeDraftPaper(OperationUtils.getInstance().mBackPage);
        this.indexPage = OperationUtils.getInstance().mBackPage;

    }

    /**
     * 412 草稿纸换页
     */
    public void changeDraftPaper(int changeDraftPage) {
        backpenSize = OperationUtils.getInstance().mCurrentPenSize;
        backPenColor = OperationUtils.getInstance().mCurrentPenColor;
        backEraserSize = OperationUtils.getInstance().mCurrentEraserSize;
        backTextSize = OperationUtils.getInstance().mCurrentTextSize;
        backTextColor = OperationUtils.getInstance().mCurrentTextColor;
        setWhiteboardBackgroundColor(OperationUtils.getInstance().mWhiteboardBackgroundColor);
        this.whiteDrawView.changeDraftPaper(changeDraftPage);
    }

    /**
     * 413 草稿纸背景切换
     */
    public void setBackgroundColor(String value) {
        if (value.equals("1")) {  //白色
            OperationUtils.getInstance().mWhiteboardBackgroundColor = Color.WHITE;
            setWhiteboardBackgroundColor(Color.WHITE);
        } else {//黑色
            OperationUtils.getInstance().mWhiteboardBackgroundColor = Color.BLACK;
            setWhiteboardBackgroundColor(Color.BLACK);
        }
    }

    /**
     * 414 增加草稿纸
     */
    public void addDraftPaper(int addDraftPage) {
        backpenSize = OperationUtils.getInstance().mCurrentPenSize;
        backPenColor = OperationUtils.getInstance().mCurrentPenColor;
        backEraserSize = OperationUtils.getInstance().mCurrentEraserSize;
        backTextSize = OperationUtils.getInstance().mCurrentTextSize;
        backTextColor = OperationUtils.getInstance().mCurrentTextColor;
        setWhiteboardBackgroundColor(OperationUtils.getInstance().mWhiteboardBackgroundColor);
        OperationUtils.getInstance().mEndDraftPage = addDraftPage + 1;
        this.indexPage = OperationUtils.getInstance().mEndDraftPage;
        this.whiteDrawView.addDraftPaper(this.indexPage);
    }

    /**
     * 500指令,清除
     */
    public void orderClear() {
        clearPageDraw(this.indexPage);
    }

    /**
     * 501指令,撤销
     */
    public void undo() {
        whiteDrawView.undo();
    }

    /**
     * 502指令,恢复
     */
    public void redo() {
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
        //Log.e("SanHaiEdu","jumpPage currentPage:"+currentPage);
        /*if (!this.whiteDrawView.exist(currentPage)) {
            this.requestNetPageOrder(currentPage);
        }*/
        this.requestNetPageOrder(currentPage);
        this.indexPage = currentPage;
    }

    public void testPage(int currentPage, WhiteDrawView.ICallBack callBack) {
        this.whiteDrawView.testPage(currentPage, callBack);
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


    /**
     * 清除指定页涂鸦
     *
     * @param pageIndex
     */
    public void clearPageDraw(int pageIndex) {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.clearPageIndex(pageIndex);
    }


    public void ExecuteOrder(OrderBean orderBean) {
        if (orderBean == null) {
            return;
        }
        if (!orderBean.getType().equals("")) {
            int type = Util.toInteger(orderBean.getType());
            List<OrderBean.DataBean> lst = orderBean.getData();
            switch (type) {
                case 300:
                    break;
                case 301:
                    break;
                case 302:
                    break;
                case 303:
                    break;
                case 304:
                    break;
                case 305:
                    //设置画笔大小
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setPaintSize(Float.parseFloat(orderBean.getValue()));
                    }
                    break;
                case 306:
                    //设置画笔颜色
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setPaintColor(Color.parseColor(orderBean.getValue()));
                    }
                    break;
                case 307:
                    //设置橡皮大小
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setReaserSize(Float.parseFloat(orderBean.getValue()));
                    }
                    break;
                case 308:
                    //文字大小
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setTextSize(Integer.parseInt(orderBean.getValue()));
                    }
                    break;
                case 309:
                    //文字颜色
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setTextColor(Color.parseColor(orderBean.getValue()));
                    }
                    break;
                //400画笔401橡皮402创建文字403编辑文字404移动文字
                // 405画线406画虚线407画矩形408画圆409图形移动
                case 400:
                case 401:
                case 402:
                case 403:
                case 404:
                case 405:
                case 406:
                case 407:
                case 408:
                case 409:
                    this.addDrawData(orderBean);
                    break;
                /*case 410:
                    //打开草稿纸
                    this.openDraftPaper(OperationUtils.getInstance().mEndDraftPage);
                    break;
                case 411:
                    //关闭草稿纸
                    this.closeDraftPaper();
                    break;
                case 412:
                    //翻页草稿纸
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.changeDraftPaper(Integer.parseInt(orderBean.getValue()) + OperationUtils.getInstance().mStartDraftPage);
                    }
                    break;
                case 413:
                    //设置背景色
                    if (orderBean != null && orderBean.getValue() != null) {
                        this.setBackgroundColor(orderBean.getValue());
                    }
                    break;
                case 414:
                    //新建草稿纸
                    this.addDraftPaper(OperationUtils.getInstance().mEndDraftPage);
                    break;*/
                case 500:
                    //清空
                    this.orderClear();
                    break;
                case 501:
                    //撤销
                    this.undo();
                    break;
                case 502:
                    //回退
                    this.redo();
                    break;
                case 503:
                    //跳转指定页
                    this.jumpPage(orderBean.getCurrentPage(), orderBean.getCurrentAnimation());
                    break;
                case 504:
                    //上一页
                    this.lastSlideS(orderBean.getCurrentPage(), orderBean.getCurrentAnimation());
                    break;
                case 505:
                    //下一页
                    this.nextSlideS(orderBean.getCurrentPage(), orderBean.getCurrentAnimation());
                    break;
            }
        }
    }

    /**
     * 清除所有
     */
    public void clearAllDraw() {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.init();
    }

    public void setPageCommandCallback(final PageCommandCallback f) {
        this.f = f;
    }

    public interface PageCommandCallback {
        void getPageCommand(final int p0);
    }
}