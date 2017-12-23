package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/6/29.
 */
public class TryPlayBean implements Serializable{

    private static final long serialVersionUID = 5916566341994987863L;
    /**
     * account : zc2963371851
     * rules : 1.每个试玩账号初始金额5000元
     * 2.每个IP每天最多可以创建20个试玩账号
     * 3.每个账号从注册开始有效期为7天,超过有效期系统自动回收
     * 4.试玩账号不允许充值、提现操作
     */

    private String account;
    private String rules;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
