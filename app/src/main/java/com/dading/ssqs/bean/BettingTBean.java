package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/26 14:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BettingTBean {

    /**
     * status : true
     * data : [{"amount":200,"profit":"0","count":2,"payDetails":[{"status":3,"cCount":3,"info":[{"profit":2004,"home":"沃特福德","payRate":3.2,"orderID":"2016122604250710836","homeScore":"1","status":3,"matchType":1,"openTime":"2016-12-26 20:30:00","amount":100,"away":"水晶宫","payBallType":2,"name":"水晶宫","createDate":"2016-12-26 16:25:07","awayScore":"1"},{"profit":0,"home":"阿德莱德联","payRate":3.28,"orderID":"2016122604250710836","homeScore":"0","status":2,"matchType":1,"openTime":"2016-12-26 16:50:00","amount":0,"away":"悉尼FC","payBallType":2,"name":"悉尼FC","createDate":"2016-12-26 16:25:07","awayScore":"4"},{"profit":0,"home":"布伦特福德","payRate":1.91,"orderID":"2016122604250710836","homeScore":"2","status":3,"matchType":2,"openTime":"2016-12-26 21:00:00","amount":0,"away":"加的夫城","payBallType":2,"name":"加的夫城 -0.5","createDate":"2016-12-26 16:25:07","awayScore":"2"}]},{"status":3,"cCount":1,"info":[{"profit":945,"home":"阿森纳","payRate":9.45,"orderID":"2016122605582783451","homeScore":"1","status":3,"matchType":1,"openTime":"2016-12-26 23:00:00","amount":100,"away":"西布朗","payBallType":1,"name":"平局","createDate":"2016-12-26 17:58:27","awayScore":"0"}]}],"date":"2016-12-26","wins":"-200"}]
     * code : 0
     * msg :
     */

    /**
     * amount : 200
     * profit : 0
     * count : 2
     * payDetails : [{"status":3,"cCount":3,"info":[{"profit":2004,"home":"沃特福德","payRate":3.2,"orderID":"2016122604250710836","homeScore":"1","status":3,"matchType":1,"openTime":"2016-12-26 20:30:00","amount":100,"away":"水晶宫","payBallType":2,"name":"水晶宫","createDate":"2016-12-26 16:25:07","awayScore":"1"},{"profit":0,"home":"阿德莱德联","payRate":3.28,"orderID":"2016122604250710836","homeScore":"0","status":2,"matchType":1,"openTime":"2016-12-26 16:50:00","amount":0,"away":"悉尼FC","payBallType":2,"name":"悉尼FC","createDate":"2016-12-26 16:25:07","awayScore":"4"},{"profit":0,"home":"布伦特福德","payRate":1.91,"orderID":"2016122604250710836","homeScore":"2","status":3,"matchType":2,"openTime":"2016-12-26 21:00:00","amount":0,"away":"加的夫城","payBallType":2,"name":"加的夫城 -0.5","createDate":"2016-12-26 16:25:07","awayScore":"2"}]},{"status":3,"cCount":1,"info":[{"profit":945,"home":"阿森纳","payRate":9.45,"orderID":"2016122605582783451","homeScore":"1","status":3,"matchType":1,"openTime":"2016-12-26 23:00:00","amount":100,"away":"西布朗","payBallType":1,"name":"平局","createDate":"2016-12-26 17:58:27","awayScore":"0"}]}]
     * date : 2016-12-26
     * wins : -200
     */
    public int amount;
    public String profit;
    public int count;
    public List<PayDetailsEntity> payDetails;
    public String date;
    public String wins;

    public static class PayDetailsEntity {
        /**
         * status : 3
         * cCount : 3
         * info : [{"profit":2004,"home":"沃特福德","payRate":3.2,"orderID":"2016122604250710836","homeScore":"1","status":3,"matchType":1,"openTime":"2016-12-26 20:30:00","amount":100,"away":"水晶宫","payBallType":2,"name":"水晶宫","createDate":"2016-12-26 16:25:07","awayScore":"1"},{"profit":0,"home":"阿德莱德联","payRate":3.28,"orderID":"2016122604250710836","homeScore":"0","status":2,"matchType":1,"openTime":"2016-12-26 16:50:00","amount":0,"away":"悉尼FC","payBallType":2,"name":"悉尼FC","createDate":"2016-12-26 16:25:07","awayScore":"4"},{"profit":0,"home":"布伦特福德","payRate":1.91,"orderID":"2016122604250710836","homeScore":"2","status":3,"matchType":2,"openTime":"2016-12-26 21:00:00","amount":0,"away":"加的夫城","payBallType":2,"name":"加的夫城 -0.5","createDate":"2016-12-26 16:25:07","awayScore":"2"}]
         */
        public int status;
        public int cCount;
        public List<InfoEntity> info;

        public static class InfoEntity {
            /**
             * profit : 2004
             * home : 沃特福德
             * payRate : 3.2
             * orderID : 2016122604250710836
             * homeScore : 1
             * status : 3
             * matchType : 1
             * openTime : 2016-12-26 20:30:00
             * amount : 100
             * away : 水晶宫
             * payBallType : 2
             * name : 水晶宫
             * createDate : 2016-12-26 16:25:07
             * awayScore : 1
             */
            public int profit;
            public String home;
            public double payRate;
            public String orderID;
            public String homeScore;
            public int status;
            public int matchType;
            public String openTime;
            public int amount;
            public String away;
            public int payBallType;
            public String name;
            public String createDate;
            public String awayScore;
            public boolean isAcount;
            public boolean isReturn;
        }
    }
}
