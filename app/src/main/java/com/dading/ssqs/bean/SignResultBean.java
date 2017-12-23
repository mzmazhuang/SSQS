package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/22 16:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SignResultBean implements Serializable{
    /**
     * status : true
     * data : {"banlanceCount":10,"tasks":null,"dayCount":1}
     * code : 0
     * msg :
     */

    /**
     * banlanceCount : 10
     * tasks : null
     * dayCount : 1
     */
    public int banlanceCount;
    public String tasks;
    public int dayCount;
}
