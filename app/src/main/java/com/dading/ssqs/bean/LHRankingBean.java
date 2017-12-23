package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/8 10:30
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LHRankingBean implements Serializable{
    private static final long serialVersionUID = 7373909280042375559L;
    /**
     * id : 20160704116012
     * level : 1
     * order : 1
     * tag :
     * isFouce : 0
     * userName : 匿名
     * isSelf : 0
     * avatar : http://192.168.0.134/images/advert/home_vp_pic2.png
     */
    public String id;
    public int level;
    public int order;
    public String tag;
    public int isFouce;
    public String userName;
    public int isSelf;
    public String avatar;
}
