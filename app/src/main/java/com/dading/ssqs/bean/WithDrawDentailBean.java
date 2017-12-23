package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/4 11:04
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WithDrawDentailBean {

    /**
     * msg :
     * code : 0
     * data : [{"updateDate":"2017-04-27 16:26:18","money":"60","bankCard":"1236*********2545","orderID":"","bankName":"广发银行","createDate":"2017-04-25 17:28:54","status":0},{"updateDate":"2017-04-27 16:23:34","money":"60","bankCard":"1236*********2545","orderID":"sdfasf154622545","bankName":"广发银行","createDate":"2017-04-21 10:24:44","status":2}]
     * status : true
     */
    public String msg;
    public int              code;
    public List<DataEntity> data;
    public boolean          status;

    public static class DataEntity {
        /**
         * updateDate : 2017-04-27 16:26:18
         * money : 60
         * bankCard : 1236*********2545
         * orderID :
         * bankName : 广发银行
         * createDate : 2017-04-25 17:28:54
         * status : 0
         */
        public String updateDate;
        public String money;
        public String bankCard;
        public String orderID;
        public String bankName;
        public String createDate;
        public int    status;
    }
}
