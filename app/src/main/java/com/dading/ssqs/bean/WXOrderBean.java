package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/28 16:37
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WXOrderBean implements Serializable {

    /**
     * status : true
     * data : {"sign":"35D708388B681034C66D8B782A6F61F7","timestamp":"1482887972","noncestr":"4db3b13512dc8e62a9a93c445b20260f","partnerid":"1427966702","packages":"Sign=WXPay","prepayid":"wx20161228091934dd590de6c60134449302","appid":"wxf03354b453566347"}
     * code : 0
     * msg :
     */

    /**
     * sign : 35D708388B681034C66D8B782A6F61F7
     * timestamp : 1482887972
     * noncestr : 4db3b13512dc8e62a9a93c445b20260f
     * partnerid : 1427966702
     * packages : Sign=WXPay
     * prepayid : wx20161228091934dd590de6c60134449302
     * appid : wxf03354b453566347
     */
    public String sign;
    public String timestamp;
    public String noncestr;
    public String partnerid;
    public String packages;
    public String prepayid;
    public String appid;
    public String orderID;
}
