package com.dading.ssqs.bean;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/2 16:15
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AlipaySucBean {

    /**
     * sign : JzRB7kYOg01RaRDWAdhzLApGbkI0je03BNVCY81o41Nr63OiqCbgJFb/E35qhp43bjLZAZe86uTpW/z6j4ElToF2GNpHchoM7uuhgglYtlh/zJ3Ns3choxTRIvRNMLseTnyhZOvIO+AiCCaeyBUJTlvLWMCOLeI+BLWF5NyYFK8=
     * sign_type : RSA
     * alipay_trade_app_pay_response : {"total_amount":"1.00","timestamp":"2016-12-02 16:12:49","trade_no":"2016120221001004540200121423","charset":"utf-8","auth_app_id":"2016091301896183","seller_id":"2088421875553442","app_id":"2016091301896183","code":"10000","out_trade_no":"20161202082022","msg":"Success"}
     */
    public String sign;
    public String                          sign_type;
    public AlipayTradeAppPayResponseEntity alipay_trade_app_pay_response;

    public static class AlipayTradeAppPayResponseEntity {
        /**
         * total_amount : 1.00
         * timestamp : 2016-12-02 16:12:49
         * trade_no : 2016120221001004540200121423
         * charset : utf-8
         * auth_app_id : 2016091301896183
         * seller_id : 2088421875553442
         * app_id : 2016091301896183
         * code : 10000
         * out_trade_no : 20161202082022
         * msg : Success
         */
        public String total_amount;
        public String timestamp;
        public String trade_no;
        public String charset;
        public String auth_app_id;
        public String seller_id;
        public String app_id;
        public String code;
        public String out_trade_no;
        public String msg;
    }
}
