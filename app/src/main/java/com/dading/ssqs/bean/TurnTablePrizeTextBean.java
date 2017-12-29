package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/28 11:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TurnTablePrizeTextBean implements Serializable{

    private static final long serialVersionUID = 8501127344559291911L;
    /**
     * status : true
     * data : ["恭喜球技过人获取得了300金额","恭喜匿名获取得了300金额"]
     * code : 0
     * msg :
     */
    public List<String> data;
}
