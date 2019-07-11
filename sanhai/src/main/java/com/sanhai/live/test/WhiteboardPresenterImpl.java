package com.sanhai.live.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class WhiteboardPresenterImpl implements WhiteboardDispatcher{
    private ViewGroup viewGroup;
    private Context context;
    View view;
    
    public void setPPTContainer(ViewGroup var1) {
        this.view = new View(context);
        if (this.viewGroup != var1 || this.viewGroup == null || this.viewGroup.indexOfChild(this.view) == -1) {
            this.removeFromContainer();
            this.viewGroup = var1;
            if (this.view != null) {
                this.viewGroup.addView(this.view);
            }

        }
    }
    public void removeFromContainer() {
        if (this.view != null) {
            ViewGroup var1;
            if ((var1 = (ViewGroup)this.view.getParent()) != null) {
                var1.removeView(this.view);
            }

        }
    }
    @Override
    public void gotoPage(String var1, String var2) {

    }

    @Override
    public void preloadPage(String var1) {

    }

    @Override
    public void clearPageDraw(int var1) {

    }

    @Override
    public void clearAllDraw() {

    }

    @Override
    public void addDrawData(int var1, int var2, String var3) {

    }

    @Override
    public void addImageData(int var1, String var2) {

    }

    @Override
    public void clearPage() {

    }
}
