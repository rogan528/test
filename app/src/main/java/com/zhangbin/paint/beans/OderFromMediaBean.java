package com.zhangbin.paint.beans;

import com.google.gson.Gson;

import java.util.List;

public class OderFromMediaBean {

    /**
     * si : 21
     * t : 400
     * s : 223434343
     * e : 223434349
     * th : 30
     * pc : ffffff
     * d : [{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":361,"y":110}]
     */

    private int si;
    private int t;
    private int s;
    private int e;
    private int th;
    private String pc;
    private List<DataBean> d;
    /**
     * w : 640
     * h : 480
     */

    private double w;
    private double h;
    /**
     * v : 6 或者 "#00ff00"
     */

    private String v;
    /**
     * cp : 3
     * ca : 5
     */

    private int cp;
    private int ca;
    /**
     * x : 5.1
     * y : 5.2
     * w : 10.1
     * h : 10.1
     * u : 2012
     * tx : 文本输入
     */

    private double x;
    private double y;
    private String u;
    private String tx;
    /**
     * x1 : 100.0
     * y1 : 100.0
     * x2 : 100.0
     * y2 : 100.0
     */

    private double x1;
    private double y1;
    private double x2;
    private double y2;


    public static OderFromMediaBean objectFromData(String str) {

        return new Gson().fromJson(str, OderFromMediaBean.class);
    }


    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getTh() {
        return th;
    }

    public void setTh(int th) {
        this.th = th;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public List<DataBean> getD() {
        return d;
    }

    public void setD(List<DataBean> d) {
        this.d = d;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public static class DataBean {
        /**
         * x : 364
         * y : 110
         */

        private int x;
        private int y;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
