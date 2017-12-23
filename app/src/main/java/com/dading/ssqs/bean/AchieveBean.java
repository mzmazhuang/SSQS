package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/7 13:48
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AchieveBean implements Serializable {
    private static final long serialVersionUID = 3610401282616130196L;

    /**
     * status : true
     * data : [{"id":96,"status":1,"remark":"累计中奖5000,000金币","imageUrl":"http://192.168.0.115:8080/images/award/20170406020424JsaqrLZalX-92x86.png","name":"10元流量"},{"id":97,"status":0,"remark":"累计中奖1,000,000金币","imageUrl":"http://192.168.0.115:8080/images/award/20170406020515SLvbAtWShE-98x94.png","name":"20元话费"}]
     * code : 0
     * msg :
     */

    /**
     * id : 96
     * status : 1
     * remark : 累计中奖5000,000金币
     * imageUrl : http://192.168.0.115:8080/images/award/20170406020424JsaqrLZalX-92x86.png
     * name : 10元流量
     */
    public int id;
    public int status;
    public String remark;
    public String imageUrl;
    public String name;
}
