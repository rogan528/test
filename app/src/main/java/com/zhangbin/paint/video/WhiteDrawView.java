package com.zhangbin.paint.video;

/**
 * @ClassName WhitedrawView
 * @Description 主画板视图类，控制发起由这发起
 * @Author yangjie
 * @Date 2019/3/28 下午6:27
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhangbin.paint.R;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.video.shape.BaseDraw;

import java.util.List;


public final class WhiteDrawView extends FrameLayout {
    private ImageView imageView;
    private DrawLayerView imageFabricView;
    private DrawLayerView drawFabricView;
    private EditText editText;
    private int paintColor = 16711680;
    private FrameLayout frameLayout;
    //    private com.talkfun.sdk.whiteboard.presenter.imageView.imageView g;
    private PageWhite pageWhite;
    private PptWebView webView;
    private int backColor = -1;
//    private b j;

    public WhiteDrawView(Context context) {
        this(context, null);
    }

    private WhiteDrawView(Context context, AttributeSet attributeSet) {
        this(context, null, 0);
    }

    private WhiteDrawView(Context context, AttributeSet attributeSet, int paramInt) {
        super(context, attributeSet, 0);

        super.setBackgroundColor(this.backColor);
        this.frameLayout = new FrameLayout(this.getContext());
        this.editText = new EditText(this.getContext());
        this.drawFabricView = new DrawLayerView(this.getContext());
        this.imageFabricView = new DrawLayerView(this.getContext());
        this.webView = new PptWebView(this.getContext());
        this.imageView = new ImageView(this.getContext());
        this.imageView.setAdjustViewBounds(true);
        this.editText.setPadding(0, 0, 0, 0);
        LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        initWebSetting();
        this.frameLayout.addView(this.webView, layoutParams);
        this.frameLayout.addView(this.imageView, layoutParams);
        this.frameLayout.addView(this.imageFabricView, layoutParams);
        this.frameLayout.addView(this.drawFabricView, layoutParams);
        this.addView(this.frameLayout, paramInt);


    }

    private void initWebSetting() {
        String url = "http://192.168.8.37:8081/83461B08A0401FC68D9C2A7E036C4710/h5/h5.html?aaaa";
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //mWebView.setWebViewClient(new WebChromeClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl(url);

    }

    public final void init(int paramInt, boolean paramBoolean) {
//        if (paramInt == imageFabricView.imageFabricView) {
//            this.pageWhite = null;
//            this.g = new com.talkfun.sdk.whiteboard.presenter.imageView.imageView(this);
//            return;
//        }
//        if (paramInt == imageFabricView.imageView) {
//            this.g = null;
        this.pageWhite = new PageWhite(paramBoolean, this);

        //要设置原始ppt 大小以便进行缩放控制
        this.pageWhite.setWidthHeight(934, 508);
//        }

    }

    public final ImageView getImageView() {
        return this.imageView;
    }

    public final DrawLayerView getImageFabricView() {
        return this.imageFabricView;
    }

    public final DrawLayerView getDrawFabricView() {
        return this.drawFabricView;
    }

    public final FrameLayout getFrameLayout() {
        return this.frameLayout;
    }

    public final EditText getEditText() {
        return this.editText;
    }

    public final WebView getWebView() {
        return this.webView;
    }

    public final void setImageResource(int paramInt) {
        if (this.imageView != null) {
            this.imageView.setImageResource(paramInt);
        }
    }

    public final void setImageDrawable(int color) {
        if (this.imageView != null) {
            this.imageView.setImageDrawable(new ColorDrawable(color));
        }
    }

    public final void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        this.drawFabricView.setColor(this.paintColor);
    }

    public final int getPaintColor() {
        return this.paintColor;
    }

    public final void setStrokeWidth(float strokeWidth) {
        this.drawFabricView.setStrokeWidth(strokeWidth);
    }

    public final float getStrokeWidth() {
        return this.drawFabricView.getStrokeWidth();
    }

    public final void setTextSize(int size) {
        this.drawFabricView.setTextSize(size);
    }

    public final int getTextSize() {
        return this.drawFabricView.getTextSize();
    }

    public final void setDrawType(int drawType) {
        this.drawFabricView.setDrawType(drawType);
    }

    public final int getDrawType() {
        return this.drawFabricView.getDrawType();
    }

    public final void setBackgroundColor(int color) {
        this.backColor = color;
        super.setBackgroundColor(this.backColor);
    }

    public final boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
//        if (this.g == null) {
        return true;
//        }
//        return this.g.imageView(paramMotionEvent);
    }

    public final boolean onTouchEvent(MotionEvent paramMotionEvent) {
        return false;
    }

    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA();
        }
//        if (this.g != null) {
//            this.g.imageView();
//        }
    }

    public final void init(float width, float height) {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA(0.0F);
        }
    }

//    public final void init(int pageIndex, BaseDraw baseDraw) {
//        if (this.pageWhite != null) {
//            this.pageWhite.drawObjA(pageIndex, baseDraw);
//        }
//    }

    public final void drawObj(int pageIndex, BaseDraw baseDraw) {

        if (this.pageWhite != null) {
            this.pageWhite.drawObjB(pageIndex, baseDraw);
        }
    }

    public final void init(String paramString) {
        if ((!TextUtils.isEmpty(paramString)) && (getContext() != null) && (this.pageWhite != null)) {
            getContext();
        }
    }

//    public final void init(int pageIndex, String tag, int color, int paramInt3, float paramFloat, paging parama) {
//        if (this.pageWhite != null) {
//            this.pageWhite.drawObjA(getContext(), pageIndex, tag, color, paramFloat, parama);
//        }
//    }

    public final void setPPTLoadFailDrawable(Drawable paramDrawable) {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA(paramDrawable);
        }
    }

    public final void init(int pageIndex) {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA(pageIndex);
        }
    }

    public final void init() {
        if (this.pageWhite != null) {
            this.pageWhite.clear();
        }
    }

//    public final void setOnLoadImageErrorListener$3489a6d9(paging.whiteDrawView parama) {
//    }

//    public final void setOnCmdSendListener(imageFabricView paramb) {
//        this.g.imageView(paramb);
//    }

//    public final int getCurrentPage() {
//        if (this.g == null) {
//            return -1;
//        }
//        return this.g.imageFabricView();
//    }

//    public final int getTotalPage() {
//        if (this.g == null) {
//            return -1;
//        }
//        return this.g.target();
//    }

    public final boolean drawObj(int pageIndex) {
        return this.pageWhite.drawObjB(pageIndex);
    }

    public final void drawObj() {
//        if (this.g != null) {
//            this.g.editText();
//        }
        if (this.pageWhite != null) {
            this.pageWhite.drawObjB();
        }
    }

    public final void clearAll() {
//        if (this.g != null) {
//            this.g.frameLayout();
//        }
        if (this.pageWhite != null) {
            this.pageWhite.clearAll();
        }
    }

//    public final void setOnWhiteBoardLogListener$6e575bbd(b paramb) {
//        this.j = paramb;
//    }

    /**
     * 410 打开草稿纸
     * @param currentPage
     */
    public void openDraftPaper(int currentPage) {
        webView.setVisibility(INVISIBLE);
        jumpPage(currentPage,1);
    }

    /**
     * 411 关闭草稿纸
     * @param currentPage
     */
    public void closeDraftPaper(int currentPage) {
        webView.setVisibility(VISIBLE);
        jumpPage(currentPage,1);
    }

    /**
     * 414 增加草稿纸
     * @param currentPage
     */
    public void addDraftPaper(int currentPage) {
        webView.setVisibility(INVISIBLE);
        this.pageWhite.ToPage(currentPage);
    }
    public void jumpPage(int currentPage, int currentAnimation) {

        webView.evaluateJavascript("javascript:JumpPage(" + currentPage + "," + currentAnimation + ",1)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        this.pageWhite.ToPage(currentPage);

    }

    public void lastSlideS(int currentPage, int currentAnimation) {
        webView.evaluateJavascript("javascript:lastSlideS(" + currentPage + "," + currentAnimation + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        this.pageWhite.ToPage(currentPage);
    }

    public void nextSlideS(int currentPage, int currentAnimation) {
        webView.evaluateJavascript("javascript:nextSlideS(" + currentPage + "," + currentAnimation + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        this.pageWhite.ToPage(currentPage);
    }


    /**
     * 501指令,撤销
     *
     */
    public void undo() {
        this.pageWhite.undo();

        //imageFabricView.undo();
    }

    /**
     * 502指令,恢复
     */
    public void redo(){
        this.pageWhite.redo();

        //imageFabricView.redo();
    }


//    public static enum b {}





}
