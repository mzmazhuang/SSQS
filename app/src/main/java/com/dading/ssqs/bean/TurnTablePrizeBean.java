package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/23 9:19
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TurnTablePrizeBean implements Serializable {

    private static final long serialVersionUID = 5218853569575377987L;
    /**
     * status : 1
     * name : 300金币
     * itemImageUrl : http://192.168.0.134/images/award/20160810124801AJQsuxXiPh.jpg,/award/20160810124801MNKJyHoQpV.jpg
     * createDate : 2016-08-16 09:09:17
     * type : 2
     * cost : 1
     */
    public int status;
    public String name;
    public String itemImageUrl;
    public String createDate;
    public int type;
    public String cost;
}
