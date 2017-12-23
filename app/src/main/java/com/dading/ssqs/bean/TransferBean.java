package com.dading.ssqs.bean;

/**
 * Created by lenovo on 2017/8/18.
 */
public class TransferBean {
    boolean isChecked;
    String  title;

    public TransferBean (String title, boolean isChecked) {
        this.title = title;
        this.isChecked = isChecked;
    }

    public boolean isChecked ( ) {
        return isChecked;
    }

    public void setChecked (boolean checked) {
        isChecked = checked;
    }

    public String getTitle ( ) {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }
}
