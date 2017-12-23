package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class ForgetPasswordElement extends BaseElement {

    private String password;
    private String repassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}
