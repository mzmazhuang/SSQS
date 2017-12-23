package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/15 15:19
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StoreBean implements Serializable {
    private static final long serialVersionUID = 271977548504552277L;

    /**
     * status : true
     * data : [{"id":4,"count":"","remark":"1元=100球币","name":"1,200","itemImageUrl":"http://192.168.0.115:8080/images/award/20161014044705EqsrEtLSUs-57x93.png","cost":"12"}]
     * code : 0
     * msg :
     */

    /**
     * id : 4
     * count :
     * remark : 1元=100球币
     * name : 1,200
     * itemImageUrl : http://192.168.0.115:8080/images/award/20161014044705EqsrEtLSUs-57x93.png
     * cost : 12
     */
    public int id;
    public String count;
    public String remark;
    public String name;
    public String itemImageUrl;
    public String cost;
}
