package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/29 14:08
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoBean implements Serializable {
    private static final long serialVersionUID = -8904099606942761490L;
    /**
     * home : 热刺
     * leagueName : 英超
     * hYellow : 0
     * aHalfScore : null
     * isFouce : 0
     * hImageUrl : http://192.168.0.134/images/team/20160816035338eqACXkWzXS.png
     * aImageUrl : http://192.168.0.134/images/team/20160816025138mAIlOFsGbL.png
     * hRed : 0
     * openTime : 2016-08-27 19:00:00
     * isOver : 0
     * type : 1
     * aScore :
     * aOrder : 2
     * fouceDate : null
     * id : 81
     * away : 利物浦
     * aYellow : 0
     * hHalfScore : null
     * joinCount : 0
     * aRed : 0
     * hOrder : 13
     * hScore :
     */
    public String home;
    public String leagueName;
    public int hYellow;
    public String aHalfScore;
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
    public int joinCount;
    public int aRed;
    public String hOrder;
    public String hScore;
    public String hOrderFrom;
    public String aOrderFrom;
    public String protime;
    public String liveCastUrl;
    public boolean isVisible;
}
