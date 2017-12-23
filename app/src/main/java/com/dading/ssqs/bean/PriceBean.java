package com.dading.ssqs.bean;

/**
 * Created by lenovo on 2017/6/7.
 */
public class PriceBean {
    public boolean select;
    public int     money;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
