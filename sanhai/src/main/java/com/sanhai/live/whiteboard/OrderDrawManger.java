package com.sanhai.live.whiteboard;

import com.google.gson.Gson;
import com.sanhai.live.module.OrderBean;
import com.sanhai.live.whiteboard.presenter.WhiteboardPresenter;

import java.util.ArrayList;

/**
 * @ClassName DrawManger
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/20 下午1:13
 */
public class OrderDrawManger {

    private WhiteboardPresenter whiteboardPresenter;
    private Gson gson = new Gson();
    private int cur = -1;

    private OrderBean orderBean;
    private ArrayList listorderBean = new ArrayList<OrderBean>();


    public OrderDrawManger(WhiteboardPresenter whiteboardPresenter) {
        this.whiteboardPresenter = whiteboardPresenter;
    }

    public OrderDrawManger SetOrder(OrderBean orderBean) {

        this.orderBean = orderBean;
        return this;
    }

    public OrderDrawManger NextOrder() {


        if (cur >= listorderBean.size() - 1) {
            this.orderBean = null;
            return this;
        }
        cur = cur + 1;
        this.orderBean = (OrderBean) listorderBean.get(cur);

        return this;

    }

    public OrderDrawManger PreOrder() {
        if (cur <= 0) {
            this.orderBean = null;
            return this;
        }
        cur = cur - 1;
        this.orderBean = (OrderBean) listorderBean.get(cur);
        return this;


    }


    public void ExecuteOrder() {
        if (orderBean == null) {
            return;
        }

        this.whiteboardPresenter.ExecuteOrder(this.orderBean);
    }


    public void setListorderBean(ArrayList<OrderBean> listorderBean) {
        this.listorderBean = listorderBean;
    }
}
