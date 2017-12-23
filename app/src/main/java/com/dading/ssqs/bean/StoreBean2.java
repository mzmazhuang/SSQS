package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/21 10:04
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StoreBean2 implements Serializable {
    private static final long serialVersionUID = -1482960004402544116L;

    /**
     * status : true
     * data : {"startTime":"2016-11-21 17:04:31","isEnd":0,"remark":"","isStart":1,"awards":[{"id":8,"count":"","remark":"","name":"300金币","itemImageUrl":"http://192.168.0.115:8080/images/award/20161013060210gBLmoOPTSK-62x49.png","cost":"500"}],"endTime":"2017-11-19 17:08:25"}
     * code : 0
     * msg :
     */

    /**
     * startTime : 2016-11-21 17:04:31
     * isEnd : 0
     * remark :
     * isStart : 1
     * awards : [{"id":8,"count":"","remark":"","name":"300金币","itemImageUrl":"http://192.168.0.115:8080/images/award/20161013060210gBLmoOPTSK-62x49.png","cost":"500"}]
     * endTime : 2017-11-19 17:08:25
     */
    public String startTime;
    public int isEnd;
    public String remark;
    public int isStart;


    public List<AwardsEntity> awards;
    public String endTime;

    public static class AwardsEntity implements Serializable {
        /**
         * id : 8
         * count :
         * remark :
         * name : 300金币
         * itemImageUrl : http://192.168.0.115:8080/images/award/20161013060210gBLmoOPTSK-62x49.png
         * cost : 500
         */
        public int id;
        public String count;
        public String remark;
        public String name;
        public String itemImageUrl;
        public String cost;
    }
}
