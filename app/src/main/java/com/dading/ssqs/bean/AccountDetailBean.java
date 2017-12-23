package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/7/7.
 */
public class AccountDetailBean implements Serializable {

    /**
     * code : 0
     * data : {"items":[{"amount":547000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3},{"amount":467000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3},{"amount":334000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3}],"totalCount":125,"totalPage":1}
     * msg :
     * status : true
     */

    /**
     * items : [{"amount":547000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3},{"amount":467000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3},{"amount":334000,"balance":0,"createDate":"2017-06-13 20:00:00","item":"投注返回","priceType":3}]
     * totalCount : 125
     * totalPage : 1
     */

    /**
     * amount : 547000
     * balance : 0
     * createDate : 2017-06-13 20:00:00
     * item : 投注返回
     * priceType : 3
     */

    private int amount;
    private int balance;
    private String createDate;
    private String item;
    private int priceType;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }
}
