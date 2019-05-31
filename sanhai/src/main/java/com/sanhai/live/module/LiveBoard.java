package com.sanhai.live.module;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.sanhai.live.util.JsonUtil;

import java.util.List;

public class LiveBoard extends CommonJson {


    @SerializedName("data")
    private List<DataBean> data;

    public static LiveBoard objectFromData(String str) {

        return new Gson().fromJson(str, LiveBoard.class);
    }


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pptId : 83461B08A0401FC68D9C2A7E036C4710
         * utime : 2019-05-14 07:29:38.0
         * page : 1
         * liveId : 100120190330P6jUPHIs
         * seqId : 1
         * content : 1111111111
         */

        @SerializedName("pptId")
        private String pptId;
        @SerializedName("utime")
        private String utime;
        @SerializedName("page")
        private String page;
        @SerializedName("liveId")
        private String liveId;
        @SerializedName("seqId")
        private String seqId;
        @SerializedName("content")
        private String content;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getPptId() {
            return pptId;
        }

        public void setPptId(String pptId) {
            this.pptId = pptId;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public String getSeqId() {
            return seqId;
        }

        public void setSeqId(String seqId) {
            this.seqId = seqId;
        }

        public String getContent() {
            return content;
        }

        public OrderBean getOrderBean() {
            OrderBean orderBean = null;
            try {
                orderBean = JsonUtil.fromJson(content, OrderBean.class);
            } catch (JsonSyntaxException e) {
                Log.e("json", e.toString());
            }
            return orderBean;
        }


        public void setContent(String content) {
            this.content = content;
        }
    }
}
