package com.zhangbin.paint.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.sanhai.live.module.CommonJson;

import java.util.List;

public class PlayBackBean extends CommonJson {
    /**
     * msg : ok
     * code : 0
     * data : [{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562655908","liveTime":"0","page":"1","liveId":"10012019033011111111","seqId":"1","content":"{\"ca\":1,\"cp\":1,\"s\":1562655904770,\"si\":1,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562655945","liveTime":"41965","page":"2","liveId":"10012019033011111111","seqId":"32","content":"{\"ca\":1,\"cp\":2,\"s\":1562655941748,\"si\":32,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562655981","liveTime":"77814","page":"3","liveId":"10012019033011111111","seqId":"53","content":"{\"ca\":1,\"cp\":3,\"s\":1562655977596,\"si\":53,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562655989","liveTime":"82759","page":"4","liveId":"10012019033011111111","seqId":"54","content":"{\"ca\":1,\"cp\":4,\"s\":1562655982554,\"si\":54,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656171","liveTime":"267620","page":"5","liveId":"10012019033011111111","seqId":"122","content":"{\"ca\":1,\"cp\":5,\"s\":1562656167408,\"si\":122,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656173","liveTime":"269866","page":"6","liveId":"10012019033011111111","seqId":"123","content":"{\"ca\":1,\"cp\":6,\"s\":1562656169660,\"si\":123,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656211","liveTime":"307853","page":"7","liveId":"10012019033011111111","seqId":"152","content":"{\"ca\":1,\"cp\":7,\"s\":1562656207642,\"si\":152,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656213","liveTime":"309272","page":"8","liveId":"10012019033011111111","seqId":"153","content":"{\"ca\":1,\"cp\":8,\"s\":1562656209058,\"si\":153,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656257","liveTime":"353452","page":"9","liveId":"10012019033011111111","seqId":"200","content":"{\"ca\":1,\"cp\":9,\"s\":1562656253240,\"si\":200,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656259","liveTime":"356104","page":"10","liveId":"10012019033011111111","seqId":"201","content":"{\"ca\":1,\"cp\":10,\"s\":1562656255896,\"si\":201,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656271","liveTime":"367398","page":"11","liveId":"10012019033011111111","seqId":"206","content":"{\"ca\":1,\"cp\":11,\"s\":1562656267192,\"si\":206,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656272","liveTime":"369052","page":"12","liveId":"10012019033011111111","seqId":"207","content":"{\"ca\":1,\"cp\":12,\"s\":1562656268841,\"si\":207,\"t\":505}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656277","liveTime":"374153","page":"10","liveId":"10012019033011111111","seqId":"208","content":"{\"ca\":5,\"cp\":10,\"s\":1562656273946,\"si\":208,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656282","liveTime":"378396","page":"8","liveId":"10012019033011111111","seqId":"209","content":"{\"ca\":6,\"cp\":8,\"s\":1562656278177,\"si\":209,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656283","liveTime":"379832","page":"6","liveId":"10012019033011111111","seqId":"210","content":"{\"ca\":5,\"cp\":6,\"s\":1562656279620,\"si\":210,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656290","liveTime":"386181","page":"1","liveId":"10012019033011111111","seqId":"211","content":"{\"ca\":1,\"cp\":1,\"s\":1562656285968,\"si\":211,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656299","liveTime":"395759","page":"3","liveId":"10012019033011111111","seqId":"216","content":"{\"ca\":1,\"cp\":3,\"s\":1562656295546,\"si\":216,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656308","liveTime":"404371","page":"2","liveId":"10012019033011111111","seqId":"219","content":"{\"ca\":5,\"cp\":2,\"s\":1562656304152,\"si\":219,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656312","liveTime":"408489","page":"4","liveId":"10012019033011111111","seqId":"222","content":"{\"ca\":5,\"cp\":4,\"s\":1562656308283,\"si\":222,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656318","liveTime":"414573","page":"6","liveId":"10012019033011111111","seqId":"226","content":"{\"ca\":5,\"cp\":6,\"s\":1562656314364,\"si\":226,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656322","liveTime":"418567","page":"7","liveId":"10012019033011111111","seqId":"229","content":"{\"ca\":1,\"cp\":7,\"s\":1562656318358,\"si\":229,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656325","liveTime":"421375","page":"8","liveId":"10012019033011111111","seqId":"231","content":"{\"ca\":6,\"cp\":8,\"s\":1562656321168,\"si\":231,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656327","liveTime":"423855","page":"9","liveId":"10012019033011111111","seqId":"233","content":"{\"ca\":1,\"cp\":9,\"s\":1562656323639,\"si\":233,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656329","liveTime":"425930","page":"11","liveId":"10012019033011111111","seqId":"235","content":"{\"ca\":1,\"cp\":11,\"s\":1562656325713,\"si\":235,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656331","liveTime":"427911","page":"12","liveId":"10012019033011111111","seqId":"238","content":"{\"ca\":1,\"cp\":12,\"s\":1562656327704,\"si\":238,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656334","liveTime":"430969","page":"10","liveId":"10012019033011111111","seqId":"241","content":"{\"ca\":5,\"cp\":10,\"s\":1562656330760,\"si\":241,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656338","liveTime":"434962","page":"3","liveId":"10012019033011111111","seqId":"244","content":"{\"ca\":1,\"cp\":3,\"s\":1562656334752,\"si\":244,\"t\":503}"},{"pptId":"3927F1600D2582C2995BEEC5F8EEF7E0","utime":"1562656341","liveTime":"437880","page":"4","liveId":"10012019033011111111","seqId":"245","content":"{\"ca\":5,\"cp\":4,\"s\":1562656337672,\"si\":245,\"t\":503}"}]
     * timestamp : 1562657711
     */
    @SerializedName("data")
    private List<DataBean> data;

    public static PlayBackBean objectFromData(String str) {

        return new Gson().fromJson(str, PlayBackBean.class);
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pptId : 3927F1600D2582C2995BEEC5F8EEF7E0
         * utime : 1562655908
         * liveTime : 0
         * page : 1
         * liveId : 10012019033011111111
         * seqId : 1
         */

        @SerializedName("pptId")
        private String pptId;
        @SerializedName("utime")
        private String utime;
        @SerializedName("liveTime")
        private String liveTime;
        @SerializedName("page")
        private String page;
        @SerializedName("liveId")
        private String liveId;
        @SerializedName("seqId")
        private String seqId;

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

        public String getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(String liveTime) {
            this.liveTime = liveTime;
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
    }
}
