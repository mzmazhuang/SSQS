package com.dading.ssqs.bean;

/**
 * Created by lenovo on 2017/9/12.
 */
public class ReportInfoBean {
    private String title;
    private String data;

    public String getTitle ( ) {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getData ( ) {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public ReportInfoBean (String title, String winNum) {
        this.title = title;
        this.data = winNum;
    }
}
