package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class ReCommUploadElement extends BaseElement {
    private String matchID;
    private String reason;
    private String payRateID;
    private String selected;
    private String amount;

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayRateID() {
        return payRateID;
    }

    public void setPayRateID(String payRateID) {
        this.payRateID = payRateID;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
