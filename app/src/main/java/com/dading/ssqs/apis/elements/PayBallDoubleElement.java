package com.dading.ssqs.apis.elements;

import android.text.TextUtils;

import com.dading.ssqs.bean.BetBean;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by mazhuang on 2017/11/23.
 */

public class PayBallDoubleElement extends BaseElement {
    private ArrayList<BetBean> mFrilstList = new ArrayList<>();
    private ArrayList<BetBean> mSecondList = new ArrayList<>();

    public ArrayList<BetBean> getmFrilstList() {
        return mFrilstList;
    }

    public void setmFrilstList(ArrayList<BetBean> mFrilstList) {
        this.mFrilstList = mFrilstList;
    }

    public ArrayList<BetBean> getmSecondList() {
        return mSecondList;
    }

    public void setmSecondList(ArrayList<BetBean> mSecondList) {
        this.mSecondList = mSecondList;
    }

    public String buildParams() {
        String jsonStr;
        if (getmFrilstList() != null && getmFrilstList().size() >= 1) {
            jsonStr = new Gson().toJson(getmFrilstList());
        } else if (getmSecondList() != null && getmSecondList().size() >= 1) {
            jsonStr = new Gson().toJson(getmSecondList());
        } else {
            jsonStr = "";
        }
        if (TextUtils.isEmpty(jsonStr)) {
            jsonStr = "";
        }
        return jsonStr;
    }
}
