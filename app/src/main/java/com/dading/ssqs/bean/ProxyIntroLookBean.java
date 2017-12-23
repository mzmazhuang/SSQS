package com.dading.ssqs.bean;


/**
 * Created by lenovo on 2017/8/10.
 */
public class ProxyIntroLookBean {

    /**
     * id : 1
     * code : 5f21461f
     * status : 1
     * type : 1
     * fee : 0.0
     */

    private int id;
    private String code;
    private int status;
    private int type;
    private String fee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
