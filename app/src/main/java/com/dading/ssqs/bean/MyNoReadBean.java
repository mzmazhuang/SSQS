package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/28 16:54
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyNoReadBean implements Serializable {

    private static final long serialVersionUID = 3630915140620267314L;
    /**
     * content : 段JJ是坑货哦，是的没错。。就是i这样
     * groupType : 1
     * groupName : 今日话题
     * msgNum : 2
     * createDate : 1480385592000
     */
    public String content;
    public int groupType;
    public String groupName;
    public int msgNum;
    public String createDate;
}
