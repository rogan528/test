package com.sanhai.live.test;

public interface WhiteboardDispatcher {
    void gotoPage(String var1, String var2);

    void preloadPage(String var1);

    void clearPageDraw(int var1);

    void clearAllDraw();

    void addDrawData(int var1, int var2, String var3);

    void addImageData(int var1, String var2);

    void clearPage();
}
