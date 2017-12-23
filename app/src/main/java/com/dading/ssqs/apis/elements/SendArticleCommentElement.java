package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/22.
 */

public class SendArticleCommentElement extends BaseElement {
    private String articleID;
    private String content;

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
