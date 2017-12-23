package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class RecommDetailElement extends BaseElement {
    private String recommMatchID;
    private String status;

    public String getRecommMatchID() {
        return recommMatchID;
    }

    public void setRecommMatchID(String recommMatchID) {
        this.recommMatchID = recommMatchID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
