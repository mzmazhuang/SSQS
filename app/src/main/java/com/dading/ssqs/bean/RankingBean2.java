package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/21 10:48
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RankingBean2 implements Serializable {

    /**
     * status : true
     * data : {"startDate":"2016-07-27 00:00:00","isEnd":0,"orders":[{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035103AvHKyTokwL-66x87.png"],"value":"","userName":"--","awardName":"iphone 7 Plus","avatar":"","ranking":"1"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035213fYaDsDSYwP-131x77.png"],"value":"","userName":"--","awardName":"佳能微单","avatar":"","ranking":"2"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035523EitNAEQDZg-68x97.png"],"value":"","userName":"--","awardName":"电子阅读器","avatar":"","ranking":"p3"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035623yLTauwTkAH-96x99.png"],"value":"","userName":"--","awardName":"魅族头戴式耳机","avatar":"","ranking":"4"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040048jDaYsTPIzw-66x95.png"],"value":"","userName":"--","awardName":"美的豆浆机","avatar":"","ranking":"5"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040129FDRFNyXjTM-97x61.png"],"value":"","userName":"--","awardName":"京东卡300","avatar":"","ranking":"6"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040227QuRyunoqNl-108x61.png"],"value":"","userName":"--","awardName":"100元话费","avatar":"","ranking":"7"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"8"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"9"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"10"}],"endDate":"2022-08-08 00:00:00"}
     * code : 0
     * msg :
     */

    /**
     * startDate : 2016-07-27 00:00:00
     * isEnd : 0
     * orders : [{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035103AvHKyTokwL-66x87.png"],"value":"","userName":"--","awardName":"iphone 7 Plus","avatar":"","ranking":"1"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035213fYaDsDSYwP-131x77.png"],"value":"","userName":"--","awardName":"佳能微单","avatar":"","ranking":"2"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035523EitNAEQDZg-68x97.png"],"value":"","userName":"--","awardName":"电子阅读器","avatar":"","ranking":"p3"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014035623yLTauwTkAH-96x99.png"],"value":"","userName":"--","awardName":"魅族头戴式耳机","avatar":"","ranking":"4"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040048jDaYsTPIzw-66x95.png"],"value":"","userName":"--","awardName":"美的豆浆机","avatar":"","ranking":"5"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040129FDRFNyXjTM-97x61.png"],"value":"","userName":"--","awardName":"京东卡300","avatar":"","ranking":"6"},{"userID":"","status":0,"awardUrlArr":["http://112.74.130.167/images/award/20161014040227QuRyunoqNl-108x61.png"],"value":"","userName":"--","awardName":"100元话费","avatar":"","ranking":"7"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"8"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"9"},{"userID":"","status":0,"awardUrlArr":null,"value":"","userName":"--","awardName":"-","avatar":"","ranking":"10"}]
     * endDate : 2022-08-08 00:00:00
     */
    public String startDate;
    public int isEnd;
    public List<OrdersEntity> orders;
    public String endDate;

    public static class OrdersEntity implements Serializable {
        /**
         * userID :
         * status : 0
         * awardUrlArr : ["http://112.74.130.167/images/award/20161014035103AvHKyTokwL-66x87.png"]
         * value :
         * userName : --
         * awardName : iphone 7 Plus
         * avatar :
         * ranking : 1
         */
        public String userID;
        public int status;
        public List<String> awardUrlArr;
        public String value;
        public String userName;
        public String awardName;
        public String avatar;
        public String ranking;
    }
}
