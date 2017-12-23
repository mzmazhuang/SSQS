package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/7 14:42
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPopleARBean implements Serializable {
    private static final long serialVersionUID = -87662966753192218L;

    /**
     * status : true
     * data : {"items":[{"id":"20160909099470","profit":90,"rate":38,"level":1,"order":10,"tag":"1","isFouce":0,"userName":"Houadbasbc","isSelf":0,"counts":26,"win":10,"avatar":"http://192.168.0.115:8080/images/avatar/20161024051030CtffxcpOyX.jpg"}],"totalCount":2,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * id : 20160909099470
     * profit : 90
     * rate : 38
     * level : 1
     * order : 10
     * tag : 1
     * isFouce : 0
     * userName : Houadbasbc
     * isSelf : 0
     * counts : 26
     * win : 10
     * avatar : http://192.168.0.115:8080/images/avatar/20161024051030CtffxcpOyX.jpg
     */
    public String id;
    public int profit;
    public int rate;
    public int level;
    public int order;
    public String tag;
    public int isFouce;
    public String userName;
    public int isSelf;
    public int counts;
    public int win;
    public String avatar;
}
