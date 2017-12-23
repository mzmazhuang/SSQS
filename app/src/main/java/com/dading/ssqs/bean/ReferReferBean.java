package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/17 15:10
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferReferBean implements Serializable {
    private static final long serialVersionUID = -3158989961295044184L;

    /**
     * status : true
     * data : {"items":[{"home":"葡萄牙","leagueName":"欧洲杯","isBuy":1,"reason":null,"suppCount":0,"openTime":"2016-08-15 11:46:05","aScore":"1","avatar":"/advert/home_vp_pic2.png","payRateName":"欧赔","tip":3,"id":1,"amount":0,"lh":3,"tipContent":" 23场中1场","away":"波兰","userID":"20160704116012","level":1,"hateCount":1,"userName":"匿名","hScore":"1","createDate":"2016-08-04 10:16:58"}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * home : 葡萄牙
     * leagueName : 欧洲杯
     * isBuy : 1
     * reason : null
     * suppCount : 0
     * openTime : 2016-08-15 11:46:05
     * aScore : 1
     * avatar : /advert/home_vp_pic2.png
     * payRateName : 欧赔
     * tip : 3
     * id : 1
     * amount : 0
     * lh : 3
     * tipContent :  23场中1场
     * away : 波兰
     * userID : 20160704116012
     * level : 1
     * hateCount : 1
     * userName : 匿名
     * hScore : 1
     * createDate : 2016-08-04 10:16:58
     */
    public String home;
    public String leagueName;
    public int isBuy;
    public String reason;
    public int suppCount;
    public String openTime;
    public String aScore;
    public String avatar;
    public String payRateName;
    public int tip;
    public int id;
    public int amount;
    public int lh;
    public String tipContent;
    public String away;
    public String userID;
    public int level;
    public int hateCount;
    public String userName;
    public String hScore;
    public String createDate;
}
