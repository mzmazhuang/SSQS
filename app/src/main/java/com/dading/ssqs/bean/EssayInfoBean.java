package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/10 10:31
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class EssayInfoBean implements Serializable {

    private static final long serialVersionUID = -3692685462796326956L;
    /**
     * sno : 0
     * imageUrl : ["/article/20160719022442aAuugdebrN.jpg"]
     * categoryID : 1
     * commentCount : p3
     * avatar : null
     * isCollect : 1
     * content : <p>test2223</p><p><br></p>
     * id : 8
     * userID : 20160708134661
     * categoryName : 中国足球
     * title : test23
     * userName : 实事球事
     * createDate : 2016-07-18 22:51:45
     * zanCount : 1
     */
    public int sno;
    public List<String> imageUrl;
    public int categoryID;
    public int commentCount;
    public String avatar;
    public int isCollect;
    public String content;
    public int id;
    public String userID;
    public String categoryName;
    public String title;
    public String userName;
    public String createDate;
    public int zanCount;
}
