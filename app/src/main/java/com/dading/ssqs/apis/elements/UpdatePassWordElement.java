package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class UpdatePassWordElement extends BaseElement {
    private String password;
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
