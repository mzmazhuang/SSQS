package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/22.
 */

public class OrderStatusElement extends BaseElement {
    private String orderID;
    private String status;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
