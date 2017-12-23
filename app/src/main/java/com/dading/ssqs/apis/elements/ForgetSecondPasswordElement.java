package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/22.
 */

public class ForgetSecondPasswordElement extends BaseElement{
    private String mobile;
    private String password;
    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
