package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/18.
 */
public class ProxyCodeBean implements Serializable {

    private static final long serialVersionUID = 2385662059328397810L;
    /**
     * status : true
     * code : 0
     * msg :
     * data : http://www.ddzlink.com/images/agent/agent.jpg
     */
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
