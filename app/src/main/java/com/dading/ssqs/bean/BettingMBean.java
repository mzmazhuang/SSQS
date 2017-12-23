package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/28 9:32
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BettingMBean {

    /**
     * status : true
     * data : [{"amount":null,"matchID":5056,"away":"贝西克塔斯","home":"那不勒斯","profit":"--","leagueName":"欧冠杯","homeScore":"","count":2,"openTime":"2016-10-20 02:45:00","payDetails":[{"status":1,"cCount":1,"info":[{"profit":382,"home":"那不勒斯","payRate":1.91,"orderID":"2016101905251231924","homeScore":"","status":1,"matchType":2,"openTime":"2016-10-20 02:45:00","amount":200,"away":"贝西克塔斯","payBallType":1,"name":"贝西克塔斯 -1.5","createDate":"2016-10-19 17:25:12","awayScore":""}]},{"status":1,"cCount":1,"info":[{"profit":758,"home":"那不勒斯","payRate":7.58,"orderID":"2016101905250486205","homeScore":"","status":1,"matchType":1,"openTime":"2016-10-20 02:45:00","amount":100,"away":"贝西克塔斯","payBallType":1,"name":"平局","createDate":"2016-10-19 17:25:04","awayScore":""}]}],"awayScore":"","wins":"--"},{"amount":null,"matchID":5053,"away":"曼城","home":"巴塞罗那","profit":"--","leagueName":"欧冠杯","homeScore":"","count":1,"openTime":"2016-10-20 02:45:00","payDetails":[{"status":1,"cCount":1,"info":[{"profit":382,"home":"巴塞罗那","payRate":1.91,"orderID":"2016101906145918118","homeScore":"","status":1,"matchType":2,"openTime":"2016-10-20 02:45:00","amount":200,"away":"曼城","payBallType":1,"name":"曼城 -1.0","createDate":"2016-10-19 18:14:59","awayScore":""}]}],"awayScore":"","wins":"--"}]
     * code : 0
     * msg :
     */

        /**
         * amount : null
         * matchID : 5056
         * away : 贝西克塔斯
         * home : 那不勒斯
         * profit : --
         * leagueName : 欧冠杯
         * homeScore :
         * count : 2
         * openTime : 2016-10-20 02:45:00
         * payDetails : [{"status":1,"cCount":1,"info":[{"profit":382,"home":"那不勒斯","payRate":1.91,"orderID":"2016101905251231924","homeScore":"","status":1,"matchType":2,"openTime":"2016-10-20 02:45:00","amount":200,"away":"贝西克塔斯","payBallType":1,"name":"贝西克塔斯 -1.5","createDate":"2016-10-19 17:25:12","awayScore":""}]},{"status":1,"cCount":1,"info":[{"profit":758,"home":"那不勒斯","payRate":7.58,"orderID":"2016101905250486205","homeScore":"","status":1,"matchType":1,"openTime":"2016-10-20 02:45:00","amount":100,"away":"贝西克塔斯","payBallType":1,"name":"平局","createDate":"2016-10-19 17:25:04","awayScore":""}]}]
         * awayScore :
         * wins : --
         */
        public String                 amount;
        public int                    matchID;
        public String                 away;
        public String                 home;
        public String                 profit;
        public String                 leagueName;
        public String                 homeScore;
        public int                    count;
        public String                 openTime;
        public List<PayDetailsEntity> payDetails;
        public String                 awayScore;
        public String                 wins;

        public static class PayDetailsEntity {
            /**
             * status : 1
             * cCount : 1
             * info : [{"profit":382,"home":"那不勒斯","payRate":1.91,"orderID":"2016101905251231924","homeScore":"","status":1,"matchType":2,"openTime":"2016-10-20 02:45:00","amount":200,"away":"贝西克塔斯","payBallType":1,"name":"贝西克塔斯 -1.5","createDate":"2016-10-19 17:25:12","awayScore":""}]
             */
            public int                                                       status;
            public int                                                       cCount;
            public List<BettingMBean.PayDetailsEntity.InfoEntity> info;

            public static class InfoEntity {
                /**
                 * profit : 382
                 * home : 那不勒斯
                 * payRate : 1.91
                 * orderID : 2016101905251231924
                 * homeScore :
                 * status : 1
                 * matchType : 2
                 * openTime : 2016-10-20 02:45:00
                 * amount : 200
                 * away : 贝西克塔斯
                 * payBallType : 1
                 * name : 贝西克塔斯 -1.5
                 * createDate : 2016-10-19 17:25:12
                 * awayScore :
                 */
                public int    profit;
                public String home;
                public double payRate;
                public String orderID;
                public String homeScore;
                public int    status;
                public int    matchType;
                public String openTime;
                public int    amount;
                public String away;
                public int    payBallType;
                public String name;
                public String createDate;
                public String awayScore;
                public boolean isLIine;
                public boolean isAcount;
                public boolean isReturn;
            }
    }
}
