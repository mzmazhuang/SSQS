package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/9 13:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferInfoARBean implements Serializable {
    private static final long serialVersionUID = -6856737703118204710L;

    /**
     * status : true
     * data : {"items":[{"home":"葡萄牙","leagueName":"欧洲杯","isBuy":1,"status":0,"suppCount":0,"openTime":"2016-07-29 11:46:05","aScore":"0","payRateName":"大小球","id":2,"amount":0,"away":"波兰","hateCount":0,"createDate":"2016-07-28 09:17:56","hScore":"0"}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * home : 葡萄牙
     * leagueName : 欧洲杯
     * isBuy : 1
     * status : 0
     * suppCount : 0
     * openTime : 2016-07-29 11:46:05
     * aScore : 0
     * payRateName : 大小球
     * id : 2
     * amount : 0
     * away : 波兰
     * hateCount : 0
     * createDate : 2016-07-28 09:17:56
     * hScore : 0
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
