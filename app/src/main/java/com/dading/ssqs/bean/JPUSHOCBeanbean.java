package com.dading.ssqs.bean;

/**
 * Created by lenovo on 2017/9/21.
 */
public class JPUSHOCBeanbean {

    /**
     * status : true
     * code : 0
     * msg :
     * data : null
     */

    private boolean status;
    private int    code;
    private String msg;
    private Object data;

    public boolean isStatus ( ) {
        return status;
    }

    public void setStatus (boolean status) {
        this.status = status;
    }

    public int getCode ( ) {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getMsg ( ) {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public Object getData ( ) {
        return data;
    }

    public void setData (Object data) {
        this.data = data;
    }
}
