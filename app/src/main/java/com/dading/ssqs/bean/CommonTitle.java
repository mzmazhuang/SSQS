package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by mazhuang on 2017/12/6.
 */

public class CommonTitle implements Serializable {
    private static final long serialVersionUID = 5271323784836847518L;

    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
