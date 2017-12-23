package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/6 18:09
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BetBean implements Serializable {

    /**
     * amount : 1
     * matchID : 16
     * selected : 1
     * payRateID : 1
     */
    public int  line;
    public String  amount;
    public int     matchID;
    public int  payRateID;
    public int     id;
    public String  home;
    public String  away;
    public String  realRate;
    public String  payTypeName;
    public Boolean cbTag;
    public String  returnNum;
    public int  type;
    public int selectID;
    public int selected;

    @Override
    public String toString() {
        return "BetBean{" +
                "amount='" + amount + '\'' +
                ", matchID=" + matchID +
                ", selected=" + selectID +
                ", payRateID=" + payRateID +
                ", id=" + id +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                ", realRate='" + realRate + '\'' +
                ", payTypeName='" + payTypeName + '\'' +
                ", cbTag=" + cbTag +
                '}';
    }
}
