package com.zhangbin.paint.video;

import android.support.v4.util.SparseArrayCompat;


import com.zhangbin.paint.video.shape.BaseDraw;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 白板历史管理类
 */
public final class HistoryOrder {
    private SparseArrayCompat<CopyOnWriteArrayList<BaseDraw>> a = new SparseArrayCompat();
    private SparseArrayCompat<CopyOnWriteArrayList<BaseDraw>> b = new SparseArrayCompat();

    public final boolean clear(int pageIndex, BaseDraw paramf) {
        CopyOnWriteArrayList lst = this.getDrawB(pageIndex);
        clear(paramf, lst);
        return true;
    }

    public final boolean getDrawA(int pageIndex, BaseDraw baseDraw) {
        CopyOnWriteArrayList lst = this.getDrawA(pageIndex);
        clear(baseDraw, lst);
        return true;
    }

    public final boolean clear(int pageIndex) {

        CopyOnWriteArrayList arrayList = this.a.get(pageIndex);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList.clear();
        }

        return true;

    }

    private static void clear(BaseDraw baseDraw, CopyOnWriteArrayList<BaseDraw> baseDraws) {
        if ((baseDraw == null) || (baseDraws == null)) {
            return;
        }
        Iterator iterator = baseDraws.iterator();
        while (iterator.hasNext()) {
            BaseDraw baseDraw1 = (BaseDraw) iterator.next();
            if (baseDraw1.getId() != null && !"".equals(baseDraw1.getId()) && baseDraw.getId() != null && !"".equals(baseDraw.getId())) {
                baseDraws.remove(baseDraw1);
                break;
            }
        }


//        if (baseDraw.getIsShow()) {
        baseDraws.add(baseDraw);
//        }
    }

    public final CopyOnWriteArrayList<BaseDraw> getDrawA(int pageIndex) {
        CopyOnWriteArrayList lst = this.a.get(pageIndex);
        if (lst == null) {
            lst = new CopyOnWriteArrayList();
            this.a.put(pageIndex, lst);
        }

        return lst;
    }

    public final CopyOnWriteArrayList<BaseDraw> getDrawB(int pageIndex) {
        CopyOnWriteArrayList lst = this.b.get(pageIndex);
        if (lst == null) {
            lst = new CopyOnWriteArrayList();
            this.b.put(pageIndex, lst);
        }
        return lst;
    }

    public final boolean exist(int pageIndex) {
        CopyOnWriteArrayList list = this.a.get(pageIndex);
        return (list != null) && (list.size() > 0);

    }

    public final void clear() {
        this.b.clear();
        this.a.clear();
    }

    public final void getDrawA() {
        clear();
    }
}

