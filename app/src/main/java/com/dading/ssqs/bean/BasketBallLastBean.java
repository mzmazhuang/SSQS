package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/11.
 */

public class BasketBallLastBean implements Serializable {
    private static final long serialVersionUID = -7892335063493107915L;

    private List<BasketItems> list;
    private int type;

    public List<BasketItems> getList() {
        return list;
    }

    public void setList(List<BasketItems> list) {
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class BasketItems implements Serializable {

        private static final long serialVersionUID = -6736542857046495098L;

        private String name;
        private List<BasketItem> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<BasketItem> getItems() {
            return items;
        }

        public void setItems(List<BasketItem> items) {
            this.items = items;
        }
    }

    public static class BasketItem implements Serializable {

        private static final long serialVersionUID = 5948507719855655306L;

        private int id;
        private String name;
        private String payRate;
        private String teamName;
        private int payTypeID;

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

        public String getPayRate() {
            return payRate;
        }

        public void setPayRate(String payRate) {
            this.payRate = payRate;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public int getPayTypeID() {
            return payTypeID;
        }

        public void setPayTypeID(int payTypeID) {
            this.payTypeID = payTypeID;
        }
    }
}
