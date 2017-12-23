package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/6 13:54
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBSeriesBean implements Serializable {

    private static final long serialVersionUID = 255173245024718986L;
    /**
     * id : 101
     * payRate : [{"realRate1":"2.01","matchID":101,"realRate3":"4.02","realRate2":"p3.23","payTypeName":"全场赛果"},{"realRate1":"1.89","matchID":101,"realRate3":"1.91","realRate2":"-1/0.5","payTypeName":"当前让球"},{"realRate1":"1.79","matchID":101,"realRate3":"2.02","realRate2":"-2/-2","payTypeName":"全场大小"}]
     * home : 曼联
     * away : 曼城
     * leagueName : 英超
     * openTime : 2016-09-10 19:30:00
     */
    public int id;
    public List<PayRateEntity> payRate;
    public String home;
    public String away;
    public String leagueName;
    public String openTime;

    public static class PayRateEntity implements Serializable {
        /**
         * realRate1 : 2.01
         * matchID : 101
         * realRate3 : 4.02
         * realRate2 : p3.23
         * payTypeName : 全场赛果
         */
        public int id;
        public String realRate1;
        public int matchID;
        public int payTypeID;
        public String realRate3;
        public String realRate2;
        public String payTypeName;
        public boolean allResult;
        public boolean nowLost;
        public boolean allBigSmall;
        public String home;
        public String away;
        public boolean cbTag1;
        public boolean cbTag2;
        public boolean cbTag3;

        @Override
        public String toString() {
            return "PayRateEntity{" +
                    "id=" + id +
                    ", realRate1='" + realRate1 + '\'' +
                    ", matchID=" + matchID +
                    ", payTypeID=" + payTypeID +
                    ", realRate3='" + realRate3 + '\'' +
                    ", realRate2='" + realRate2 + '\'' +
                    ", payTypeName='" + payTypeName + '\'' +
                    ", allResult=" + allResult +
                    ", nowLost=" + nowLost +
                    ", allBigSmall=" + allBigSmall +
                    ", home='" + home + '\'' +
                    ", away='" + away + '\'' +
                    ", cbTag1=" + cbTag1 +
                    ", cbTag2=" + cbTag2 +
                    ", cbTag3=" + cbTag3 +
                    '}';
        }
    }
}
