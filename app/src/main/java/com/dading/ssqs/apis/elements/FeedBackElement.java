package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/22.
 */

public class FeedBackElement extends BaseElement {
    private String suggest;
    private String imageUrl;

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
