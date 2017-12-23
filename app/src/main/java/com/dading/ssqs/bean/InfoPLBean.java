package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/11 9:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class InfoPLBean implements Serializable {
    private static final long serialVersionUID = -1230320668923786862L;

    /**
     * status : true
     * data : {"items":[{"realRate1":"2.p3","id":78435,"realRate3":"p3.p3","realRate2":"p3.1","beginRate3":"2.63","companyName":"Bet365","beginRate1":"2.5","payTypeName":"全场赛果","beginRate2":"p3.2"}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * items : [{"realRate1":"2.p3","id":78435,"realRate3":"p3.p3","realRate2":"p3.1","beginRate3":"2.63","companyName":"Bet365","beginRate1":"2.5","payTypeName":"全场赛果","beginRate2":"p3.2"}]
     * totalCount : 1
     * totalPage : 1
     */

    /**
     * realRate1 : 2.p3
     * id : 78435
     * realRate3 : p3.p3
     * realRate2 : p3.1
     * beginRate3 : 2.63
     * companyName : Bet365
     * beginRate1 : 2.5
     * payTypeName : 全场赛果
     * beginRate2 : p3.2
     */
    public String realRate1;
    public int id;
    public String realRate3;
    public String realRate2;
    public String beginRate3;
    public String companyName;
    public String beginRate1;
    public String payTypeName;
    public String beginRate2;
}
