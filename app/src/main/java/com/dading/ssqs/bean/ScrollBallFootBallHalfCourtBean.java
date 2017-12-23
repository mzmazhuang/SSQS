package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallFootBallHalfCourtBean implements Serializable {

    private static final long serialVersionUID = -1905008199803244296L;

    private CommonTitle title;
    private List<ScrollBallFootBallHalfCourtItems> items;
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

    public List<ScrollBallFootBallHalfCourtItems> getItems() {
        return items;
    }

    public void setItems(List<ScrollBallFootBallHalfCourtItems> items) {
        this.items = items;
    }

    public static class ScrollBallFootBallHalfCourtItems implements Serializable {

        private static final long serialVersionUID = -7469788836779849492L;
        private int id;
        private String time;
        private String title;
        private String byTitle;
        private List<ScrollBallFootBallHalfCourtItem> itemList;

        public List<ScrollBallFootBallHalfCourtItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<ScrollBallFootBallHalfCourtItem> itemList) {
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

    public static class ScrollBallFootBallHalfCourtItem implements Serializable {

        private static final long serialVersionUID = -1766731583126033584L;

        public ScrollBallFootBallHalfCourtItem(int id, String str) {
            setId(id);
            setStr(str);
        }

        public ScrollBallFootBallHalfCourtItem(String str) {
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
