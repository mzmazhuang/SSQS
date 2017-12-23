package com.dading.ssqs.apis.elements;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class CommentSaveElement extends BaseElement {
    private String matchID;
    private String content;

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
