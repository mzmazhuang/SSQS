package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/6.
 * 滚球-足球-波胆
 */

public class ScrollBallFootBallTotalBean implements Serializable {
    private static final long serialVersionUID = 1285026966240152611L;

    private CommonTitle title;
    private List<ScrollBallFootBallTotalItems> items;
    private int isOpen;

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public CommonTitle getTitle() {
        return title;
    }

    public void setTitle(CommonTitle title) {
        this.title = title;
    }

    public List<ScrollBallFootBallTotalItems> getItems() {
        return items;
    }

    public void setItems(List<ScrollBallFootBallTotalItems> items) {
        this.items = items;
    }

    public static class ScrollBallFootBallTotalItems implements Serializable {

        private static final long serialVersionUID = -7469788836779849492L;
        private int id;
        private String time;
        private String title;
        private String byTitle;
        private String aScore;
        private String hScore;
        private String protTime;
        private List<ScrollBallFootBallTotalItem> itemList;

        public String getaScore() {
            return aScore;
        }

        public void setaScore(String aScore) {
            this.aScore = aScore;
        }

        public String gethScore() {
            return hScore;
        }

        public void sethScore(String hScore) {
            this.hScore = hScore;
        }

        public String getProtTime() {
            return protTime;
        }

        public void setProtTime(String protTime) {
            this.protTime = protTime;
        }

        public List<ScrollBallFootBallTotalItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<ScrollBallFootBallTotalItem> itemList) {
            this.itemList = itemList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getByTitle() {
            return byTitle;
        }

        public void setByTitle(String byTitle) {
            this.byTitle = byTitle;
        }
    }

    public static class ScrollBallFootBallTotalItem implements Serializable {

        private static final long serialVersionUID = -1766731583126033584L;

        public ScrollBallFootBallTotalItem(int id, String str) {
            setId(id);
            setStr(str);
        }

        public ScrollBallFootBallTotalItem(String str) {
            setStr(str);
        }

        private int id;
        private String str;
        private int position;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
