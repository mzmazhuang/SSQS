package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/12/20.
 */

public class FocusMatchElement extends BaseElement {

    private String payRateBallID;
    private int payTypeID;
    private int status;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPayRateBallID() {
        return payRateBallID;
    }

    public void setPayRateBallID(String payRateBallID) {
        this.payRateBallID = payRateBallID;
    }

    public int getPayTypeID() {
        return payTypeID;
    }

    public void setPayTypeID(int payTypeID) {
        this.payTypeID = payTypeID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
