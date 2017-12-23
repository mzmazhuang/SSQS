package com.dading.ssqs.apis.elements;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by mazhuang on 2017/11/21.
 */

public class BaseElement implements Serializable {

    public String buildParams() {
        String jsonStr = new Gson().toJson(this);
        if (TextUtils.isEmpty(jsonStr)) {
            jsonStr = "";
        }
        return jsonStr;
    }
}
