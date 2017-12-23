package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/8 14:50
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BetResult {

    /**
     * status : true
     * data : [{}]
     * code : 0
     * msg :
     */
    public boolean          status;
    public List<DataEntity> data;
    public int              code;
    public String           msg;

    @Override
    public String toString() {
        return "BetResult{" +
                "status=" + status +
                ", data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static class DataEntity {
    }
}
