package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/29 15:56
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFollowBean implements Serializable {
    private static final long serialVersionUID = 2249261441959458859L;

    /**
     * status : true
     * data : {"items":[{"id":"20160825119787","isFouce":1,"userName":"匿名骨","isSelf":1,"avatar":"http://192.168.0.115:8080/images/avatar/20161108034015ZZiHCRCLQJ.jpg","signature":"刚刚给v下","userType":1}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * id : 20160825119787
     * isFouce : 1
     * userName : 匿名骨
     * isSelf : 1
     * avatar : http://192.168.0.115:8080/images/avatar/20161108034015ZZiHCRCLQJ.jpg
     * signature : 刚刚给v下
     * userType : 1
     */
    public String id;
    public int isFouce;
    public String userName;
    public int isSelf;
    public String avatar;
    public String signature;
    public int userType;
}
