package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/21 9:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ALLCircleThings implements Serializable{

    private static final long serialVersionUID = 4634325069709386385L;
    /**
     * sno : 0
     * imageUrl : ["/article/20160719022330pvPKnrZEQF.jpg"]
     * categoryID : 1
     * avatar : null
     * commentCount : p3
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
    public String avatar;
    public int commentCount;
    public String content;
    public int id;
    public String userID;
    public String categoryName;
    public String title;
    public String userName;
    public String createDate;
    public int zanCount;
}
