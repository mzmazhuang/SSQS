package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/22 15:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyReferBean implements Serializable {
    private static final long serialVersionUID = 8258476786109249127L;

    /**
     * status : true
     * data : {"items":[{"home":"桑卡塔普世邦","leagueName":"土耳其","isBuy":2,"status":2,"suppCount":2,"openTime":"2016-12-21 19:30:00","aScore":"1","payRateName":"全场赛果","id":112,"amount":-1,"away":"里泽体育  ","hateCount":1,"createDate":"2016-12-21 16:31:37","hScore":"0"},{"home":"凯瑟里体育","leagueName":"土耳其","isBuy":2,"status":3,"suppCount":0,"openTime":"2016-12-22 21:30:00","aScore":"1","payRateName":"全场赛果","id":116,"amount":-1,"away":"Darica Genclerbirligi","hateCount":1,"createDate":"2016-12-22 19:04:35","hScore":"3"},{"home":"曼联","leagueName":"英超","isBuy":2,"status":3,"suppCount":1,"openTime":"2016-12-31 23:00:00","aScore":"1","payRateName":"全场赛果","id":118,"amount":-1,"away":"米德尔斯堡","hateCount":1,"createDate":"2016-12-26 17:21:29","hScore":"2"},{"home":"利物浦","leagueName":"英超","isBuy":2,"status":2,"suppCount":0,"openTime":"2017-01-01 01:30:00","aScore":"0","payRateName":"全场赛果","id":119,"amount":-1,"away":"曼城","hateCount":0,"createDate":"2016-12-27 18:14:54","hScore":"1"},{"home":"阿森纳","leagueName":"英超","isBuy":2,"status":2,"suppCount":0,"openTime":"2017-01-02 00:00:00","aScore":"0","payRateName":"全场赛果","id":120,"amount":-1,"away":"水晶宫","hateCount":0,"createDate":"2016-12-29 15:22:40","hScore":"2"},{"home":"利物浦","leagueName":"足总杯","isBuy":2,"status":1,"suppCount":0,"openTime":"2017-01-08 21:30:00","aScore":"","payRateName":"全场赛果","id":162,"amount":0,"away":"普利茅斯","hateCount":0,"createDate":"2017-01-03 17:06:25","hScore":""}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * home : 桑卡塔普世邦
     * leagueName : 土耳其
     * isBuy : 2
     * status : 2
     * suppCount : 2
     * openTime : 2016-12-21 19:30:00
     * aScore : 1
     * payRateName : 全场赛果
     * id : 112
     * amount : -1
     * away : 里泽体育
     * hateCount : 1
     * createDate : 2016-12-21 16:31:37
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
