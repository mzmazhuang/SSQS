package com.dading.ssqs.bean;


import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/30 10:32
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class JCbean implements Serializable {

    private static final long serialVersionUID = -8061689071292744710L;
    /**
     * realRate1 : 5
     * id : 6
     * home : 球队1
     * away : 球队2
     * realRate3 : 6
     * realRate2 : 6
     * payTypeName : 半场让球
     * selected 選中的賠率
     * cbTag  是否選中
     */
    public int payTypeID;
    public int id;
    public int matchID;
    public int selected;
    public String home;
    public String away;
    public String realRate1;
    public String realRate3;
    public String realRate2;
    public String payTypeName;
    public String cbValue;
    public String cbTag;
    public boolean cbTag1;
    public boolean cbTag2;
    public boolean cbTag3;
    public boolean delete;
    public int mAllResult;
    public int mHalfResult;
    public int mAllSBig;
    public int mHalfSBig;
    public int mNowLost;
    public int mHalfLost;
    public String teamName;


    @Override
    public String toString() {
        return "DataEntity{" +
                "realRate1='" + realRate1 + '\'' +
                ", id=" + id +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                ", realRate3='" + realRate3 + '\'' +
                ", realRate2='" + realRate2 + '\'' +
                ", payTypeName='" + payTypeName + '\'' +
                ", cbValue='" + cbValue + '\'' +
                ", cbTag='" + cbTag + '\'' +
                '}';
    }
}

