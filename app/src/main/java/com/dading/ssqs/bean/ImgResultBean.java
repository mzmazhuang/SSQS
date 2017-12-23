package com.dading.ssqs.bean;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/13 14:47
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ImgResultBean {

    /**
     * code : 0
     * data : http://www.ddzlink.com/images/avatar/20171026021906SBKpLGZipj-100x100.jpg
     * msg :
     * status : true
     */

    private int code;
    private String  data;
    private String  msg;
    private boolean status;

    public int getCode ( ) {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getData ( ) {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public String getMsg ( ) {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public boolean isStatus ( ) {
        return status;
    }

    public void setStatus (boolean status) {
        this.status = status;
    }
}
