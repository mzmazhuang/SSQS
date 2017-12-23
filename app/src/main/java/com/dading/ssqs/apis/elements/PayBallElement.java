package com.dading.ssqs.apis.elements;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/23.
 */

public class PayBallElement extends BaseElement {
    private List<BetBean> items = new ArrayList<>();

    public List<BetBean> getItems() {
        return items;
    }

    public void setItems(List<BetBean> items) {
        this.items = items;
    }

    @Override
    public String buildParams() {
        String jsonStr = new Gson().toJson(getItems());
        if (TextUtils.isEmpty(jsonStr)) {
            jsonStr = "";
        }
        return jsonStr;
    }

    public static class BetBean implements Serializable {

        /**
         * amount : 1
         * matchID : 16
         * selected : 1
         * payRateID : 1
         */
        public int line;
        public String amount;
        public int matchID;
        public int payRateID;
        public int id;
        public String home;
        public String away;
        public String realRate;
        public String payTypeName;
        public Boolean cbTag;
        public String returnNum;
        public int type;
        public int selectID;
        public int selected;
        public int itemID;
    }
}
