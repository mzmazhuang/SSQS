package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/26 15:10
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantInfoBean implements Serializable{

    private static final long serialVersionUID = 3774987103665757220L;
    /**
     * shotCount : 2
     * isFouce : 0
     * contentCount : 0
     * rates : [{"title":["近30天1中0","近7天0中0","近30天0中0"],"rate":["0","0","0"],"tag":"最近1场连红","payTypeID":6}]
     * winCount : 0
     * avatar : http://192.168.0.115:8080/images/avatar/test-1.jpg
     * recomm : [{"home":"大阪飞脚","leagueName":"天皇杯","isBuy":0,"status":3,"suppCount":0,"openTime":"2016-11-09 13:00:00","aScore":"","payRateName":"半场赛果","id":3,"amount":100,"away":"清水心跳","hateCount":0,"createDate":"2016-09-19 11:35:05","hScore":""}]
     * intro : 0
     * id : 11
     * userID : 20160704116012
     * recomCount : 0
     * rate : 0
     * level : 1
     * fanCount : 2
     * userName : 球技过人
     * isSelf : 0
     */
    public int shotCount;
    public int isFouce;
    public int contentCount;
    public List<RatesEntity> rates;
    public int winCount;
    public String avatar;
    public List<RecommEntity> recomm;
    public String intro;
    public int id;
    public String userID;
    public int recomCount;
    public int rate;
    public int level;
    public int fanCount;
    public String userName;
    public int isSelf;

    public static class RatesEntity {
        /**
         * title : ["近30天1中0","近7天0中0","近30天0中0"]
         * rate : ["0","0","0"]
         * tag : 最近1场连红
         * payTypeID : 6
         */
        public List<String> title;
        public List<String> rate;
        public String tag;
        public int payTypeID;
    }

    public static class RecommEntity {
        /**
         * home : 大阪飞脚
         * leagueName : 天皇杯
         * isBuy : 0
         * status : 3
         * suppCount : 0
         * openTime : 2016-11-09 13:00:00
         * aScore :
         * payRateName : 半场赛果
         * id : 3
         * amount : 100
         * away : 清水心跳
         * hateCount : 0
         * createDate : 2016-09-19 11:35:05
         * hScore :
         */
        public String home;
        public String leagueName;
        public int isBuy;
        public int status;
        public int suppCount;
        public String openTime;
        public String aScore;
        public String payRateName;
        public int id;
        public int amount;
        public String away;
        public int hateCount;
        public String createDate;
        public String hScore;
    }
}
