package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/21.
 */
public class JPushCheckedBean implements Serializable {

    private static final long serialVersionUID = -8717205199141045323L;
    /**
     * status : true
     * code : 0
     * msg :
     * data : 1
     */

    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
