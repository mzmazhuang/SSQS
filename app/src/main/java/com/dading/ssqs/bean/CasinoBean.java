package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 13:43
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CasinoBean implements Serializable {
    private static final long serialVersionUID = 7389115273793980113L;

    /**
     * status : true
     * data : [{"id":1,"status":0,"imageUrl":"http://192.168.0.115:8080/images/avatar/20170323050759ErgTlcBUER-100x100.jpg","name":"","url":""},{"id":2,"status":0,"imageUrl":"http://192.168.0.115:8080/images/avatar/20170323050759ErgTlcBUER-100x100.jpg","name":"","url":""}]
     * code : 0
     * msg :
     */

    /**
     * id : 1
     * status : 0
     * imageUrl : http://192.168.0.115:8080/images/avatar/20170323050759ErgTlcBUER-100x100.jpg
     * name :
     * url :
     */
    public int id;
    public int status;
    public String imageUrl;
    public String name;
    public String url;
}
