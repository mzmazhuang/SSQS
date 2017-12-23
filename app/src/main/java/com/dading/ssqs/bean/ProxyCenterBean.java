package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/9.
 */

public class ProxyCenterBean implements Serializable {

    private static final long serialVersionUID = 5483075581953545714L;
    /**
     * userName : 球技过人GG
     * banlance : 28899021
     * fee : 0.0
     * avatar : http://www.ddzlink.com/images/avatar.jpg
     */

    private String userName;
    private String banlance;
    private String fee;
    private String avatar;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBanlance() {
        return banlance;
    }

    public void setBanlance(String banlance) {
        this.banlance = banlance;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
