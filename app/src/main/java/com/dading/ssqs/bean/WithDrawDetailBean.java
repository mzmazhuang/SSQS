package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/13.
 */
public class WithDrawDetailBean implements Serializable{
    private static final long serialVersionUID = 1271168605396112935L;
    /**
     * money : 2
     * orderID :
     * createDate : 2017-04-25 17:28:54
     * updateDate : 2017-05-03 14:48:47
     * bankName : 建设银行
     * bankCard : 2641 **** **** 7856
     * status : 2
     * branch : xxxx
     */

    private String money;
    private String orderID;
    private String createDate;
    private String updateDate;
    private String bankName;
    private String bankCard;
    private int status;
    private String branch;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
