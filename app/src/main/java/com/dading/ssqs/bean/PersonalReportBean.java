package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/12.
 */
public class PersonalReportBean implements Serializable {

    private static final long serialVersionUID = -5658610096110456464L;
    /**
     * wins : 0
     * amount : 0
     * rewards : 0
     * fees : 0
     * recharges : 0
     * extracts : 2
     */

    private String wins;
    private String amount;
    private String rewards;
    private String fees;
    private String recharges;
    private String extracts;

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getRecharges() {
        return recharges;
    }

    public void setRecharges(String recharges) {
        this.recharges = recharges;
    }

    public String getExtracts() {
        return extracts;
    }

    public void setExtracts(String extracts) {
        this.extracts = extracts;
    }
}
