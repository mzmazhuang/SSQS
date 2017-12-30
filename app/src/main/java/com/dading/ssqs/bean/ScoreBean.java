package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/22 10:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScoreBean implements Serializable {

    private static final long serialVersionUID = -7022677653050541607L;

    /**
     * status : true
     * data : {"items":[{"home":"AC米兰","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818034411qnvgbxWRbu.png","aImageUrl":"http://192.168.0.134/images/team/20160818034442qlDcSuxVle.png","hRed":0,"openTime":"2016-08-22 00:00:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":116,"away":"都灵","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"希洪竞技","leagueName":"西甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160817084601yFcOEeptfd.png","aImageUrl":"http://192.168.0.134/images/team/20160817084639RMYHoIrpEi.png","hRed":0,"openTime":"2016-08-22 00:15:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":96,"away":"毕尔巴鄂竞技","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"皇家社会","leagueName":"西甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160817084734bSnaqYOoku.png","aImageUrl":"http://192.168.0.134/images/team/20160817084803trohARFkrP.png","hRed":0,"openTime":"2016-08-22 02:15:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":97,"away":"皇家马德里","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"博洛尼亚","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818034633aMmjysvQGH.png","aImageUrl":"http://192.168.0.134/images/team/20160818034724cDVVowNdhg.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":118,"away":"克罗托内","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"恩波利","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818034920wyfxmuuWzp.png","aImageUrl":"http://192.168.0.134/images/team/20160818034955oheyJYoilx.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":120,"away":"桑普多利亚","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"巴勒莫","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818035216FOPvIXMJIq.png","aImageUrl":"http://192.168.0.134/images/team/20160818035300JnlGxsjQus.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":122,"away":"萨索洛","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"亚特兰大","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818034521TMZFuOJIeH.png","aImageUrl":"http://192.168.0.134/images/team/20160818034555vBSHcLjWsA.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":117,"away":"拉齐奥","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"切沃","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818034812AQjwAWoWxE.png","aImageUrl":"http://192.168.0.134/images/team/20160818034841JJoFEmyYRn.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":119,"away":"国际米兰","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"热那亚","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818035023wtSrbgccut.png","aImageUrl":"http://192.168.0.134/images/team/20160818035124aVVStQBEAb.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":121,"away":"卡利亚里","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""},{"home":"佩斯卡拉","leagueName":"意甲","hYellow":0,"aHalfScore":"","isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160818035339FazZaPvAkJ.png","aImageUrl":"http://192.168.0.134/images/team/20160818035408ngHcxqBxkZ.png","hRed":0,"openTime":"2016-08-22 02:45:00","isOver":0,"type":1,"aScore":"","aOrder":0,"fouceDate":null,"id":123,"away":"那不勒斯","aYellow":0,"hHalfScore":"","joinCount":0,"aRed":0,"hOrder":0,"hScore":""}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    @Override
    public String toString() {
        return "ItemsEntity{" +
                "home='" + home + '\'' +
                ", leagueName='" + leagueName + '\'' +
                ", hYellow=" + hYellow +
                ", aHalfScore='" + aHalfScore + '\'' +
                ", isFouce=" + isFouce +
                ", hImageUrl='" + hImageUrl + '\'' +
                ", aImageUrl='" + aImageUrl + '\'' +
                ", hRed=" + hRed +
                ", openTime='" + openTime + '\'' +
                ", isOver=" + isOver +
                ", type=" + type +
                ", aScore='" + aScore + '\'' +
                ", aOrder=" + aOrder +
                ", fouceDate='" + fouceDate + '\'' +
                ", id=" + id +
                ", away='" + away + '\'' +
                ", aYellow=" + aYellow +
                ", hHalfScore='" + hHalfScore + '\'' +
                ", joinCount=" + joinCount +
                ", aRed=" + aRed +
                ", hOrder=" + hOrder +
                ", hScore='" + hScore + '\'' +
                '}';
    }

    public String homeScore;
    public String awayScore;
    public String home;
    public String leagueName;
    public int hYellow;
    public String aHalfScore;//上半场
    public String aSHalfScore;//下半场
    public int isFouce;
    public String hImageUrl;
    public String aImageUrl;
    public int hRed;
    public String openTime;
    public int isOver;
    public int type;
    public String aScore;
    public String aOrder;
    public String fouceDate;
    public int id;
    public String away;
    public int aYellow;
    public String hHalfScore;
    public String hSHalfScore;//下半场
    public int joinCount;
    public int aRed;
    public String hOrder;
    public String hScore;
    public boolean redIcon;
    public long playTime;
    public boolean isVisible;
    public boolean isVisibleTwilke;
    public String protime;


    //篮球第几节信息
    public String part1HScore;//主队第一节比分
    public String part1AScore;//客队第一节比分
    public String part2HScore;//主队第二节比分
    public String part2AScore;//客队第二节比分
    public String part3HScore;//主队第三节比分
    public String part3AScore;//客队第三节比分
    public String part4HScore;//主队第四节比分
    public String part4AScore;//客队第四节比分
    public String hOverTimeScore;//主队加时赛比分
    public String aOverTimeScore;//客队加时赛比分

    public String part1Time;//第一节时间
    public String isOverTime;
}
