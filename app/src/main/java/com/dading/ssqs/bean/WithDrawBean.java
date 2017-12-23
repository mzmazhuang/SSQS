package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/2 14:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WithDrawBean implements Serializable {
    private static final long serialVersionUID = 5654147382958793740L;
    /**
     * bankID : 7
     * bankCard : 1236*********2545
     * province : 广东
     * city :
     * extractMoney : 120
     * dontUsed : 285,178
     * bankName : 广发银行
     * currentMoney : 285,298
     * id : 1
     * userName : 22**
     */
    public int bankID;
    public String bankCard;
    public String province;
    public String city;
    public String extractMoney;
    public String dontUsed;
    public String bankName;
    public String currentMoney;
    public int id;
    public String userName;

    @Override
    public String toString() {
        return "DataEntity{" +
                "bankID=" + bankID +
                ", bankCard='" + bankCard + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", extractMoney='" + extractMoney + '\'' +
                ", dontUsed='" + dontUsed + '\'' +
                ", bankName='" + bankName + '\'' +
                ", currentMoney='" + currentMoney + '\'' +
                ", id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
