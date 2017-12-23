package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/9 15:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantFansBean implements Serializable {

    private static final long serialVersionUID = -4046628978791277785L;
    /**
     * id : 20160825119787
     * isFouce : 1
     * userName : 匿名骨
     * avatar : http://192.168.0.115:8080/images/avatar/20161108034015ZZiHCRCLQJ.jpg
     * signature : 刚刚给v下
     * userType : 1
     */
    public String id;
    public int isFouce;
    public String userName;
    public String avatar;
    public String signature;
    public int userType;
    public int isSelf;
}
