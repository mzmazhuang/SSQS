package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/12.
 */
public class RechargeDetailBean implements Serializable {
    private static final long serialVersionUID = -8004616412942928811L;

    /**
     * status : true
     * code : 0
     * msg :
     * data : {"totalPage":2,"totalCount":2,"items":[{"amount":800,"orderID":"2017060904233773906","typeName":"微信支付","cardNumber":"13926978352","bankAddress":"http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png","createTime":"2017-06-29 09:34:24","state":0},{"amount":800,"orderID":"2017060904310842786","typeName":"微信支付","cardNumber":"13926978352","bankAddress":"http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png","createTime":"2017-06-29 09:34:24","state":0}]}
     */


    /**
     * amount : 800
     * orderID : 2017060904233773906
     * typeName : 微信支付
     * cardNumber : 13926978352
     * bankAddress : http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png
     * createTime : 2017-06-29 09:34:24
     * state : 0
     */

    private int amount;
    private String orderID;
    private String typeName;
    private String cardNumber;
    private String bankAddress;
    private String createTime;
    private int state;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
