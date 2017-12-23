package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class GuessTopTitle implements Serializable {
    private static final long serialVersionUID = -7841455195259025368L;

    private int id;
    private String name;
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
