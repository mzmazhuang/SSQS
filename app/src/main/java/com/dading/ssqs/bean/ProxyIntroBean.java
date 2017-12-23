package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/9.
 */
public class ProxyIntroBean implements Serializable {

    private static final long serialVersionUID = -3107711195263486946L;
    /**
     * imageUrl : http://192.168.0.115:8080/images/agentMsg/20170815022800XMHyFQYCCV-199x300.jpg
     * content : <p>代理说明226666666666666666</p>
     */

    private String imageUrl;
    private String content;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
