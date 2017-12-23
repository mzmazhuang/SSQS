package com.dading.ssqs.apis.elements;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/23.
 */

public class PayBallScoreElement extends BaseElement {

    private List<PayBallBean> items = new ArrayList<>();

    public List<PayBallBean> getItems() {
        return items;
    }

    public void setItems(List<PayBallBean> items) {
        this.items = items;
    }

    public static class PayBallBean implements Serializable {
        private String itemID;
        private String amount;

        public String getItemID() {
            return itemID;
        }

        public void setItemID(String itemID) {
            this.itemID = itemID;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }

    public String buildParams() {
        Object json = JSON.toJSON(getItems());
        String jsonStr = new Gson().toJson(this);
        if (TextUtils.isEmpty(jsonStr)) {
            jsonStr = "";
        }
        return jsonStr;
    }
}
