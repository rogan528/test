package com.sanhai.live.whiteboard;

import android.support.v4.util.SparseArrayCompat;

import com.sanhai.live.whiteboard.shape.BaseDraw;

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

    public final boolean getDrawB(int pageIndex, BaseDraw baseDraw) {
        CopyOnWriteArrayList lst = this.getDrawB(pageIndex);
        clear(baseDraw, lst);
        return true;
    }

    public final boolean clear(int pageIndex) {

        CopyOnWriteArrayList arrayList = this.a.get(pageIndex);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList.clear();
        }
        CopyOnWriteArrayList arrayListb = this.b.get(pageIndex);
        if (arrayListb != null && arrayListb.size() > 0) {
            arrayListb.clear();
        }

        return true;

    }

    private static void clear(BaseDraw shapeDraw, CopyOnWriteArrayList<BaseDraw> baseDraws) {
        if ((shapeDraw == null) || (baseDraws == null)) {
            return;
        }


        for (BaseDraw localBaseDraw : baseDraws) {
            if (localBaseDraw.getId() != null && !"".equals(localBaseDraw.getId())
                    && shapeDraw.getId() != null && !"".equals(shapeDraw.getId())) {
                if (localBaseDraw.getId().equals(shapeDraw.getId())) {
                    baseDraws.remove(localBaseDraw);
                    break;
                }
            }
        }

        if (shapeDraw.getIsShow()) {
            baseDraws.add(shapeDraw);
        }

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
        CopyOnWriteArrayList lista = this.a.get(pageIndex);
        CopyOnWriteArrayList listb = this.b.get(pageIndex);
        return ((lista != null) && (lista.size() > 0)) || ((listb != null) && (listb.size() > 0));

    }

    public final void clear() {
        this.b.clear();
        this.a.clear();
    }

    public final void clearAll() {
        clear();
    }
}

