package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/8 16:19
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BankBean implements Serializable {
    private static final long serialVersionUID = 5471498318388377425L;

    /**
     * status : true
     * data : [{"id":1,"name":"中国工商银行"},{"id":1,"name":"东亚银行"}]
     * code : 0
     * msg :
     */
    /**
     * id : 1
     * name : 中国工商银行
     */
    public int id;
    public String name;
    public String logoUrl;
}
