package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallFootBallResultBean implements Serializable {

    private static final long serialVersionUID = 5607157752798853563L;
    private CommonTitle title;
    private List<ScrollBallFootBallResultItems> items;

    public CommonTitle getTitle() {
        return title;
    }

    public void setTitle(CommonTitle title) {
        this.title = title;
    }

    public List<ScrollBallFootBallResultItems> getItems() {
        return items;
    }

    public void setItems(List<ScrollBallFootBallResultItems> items) {
        this.items = items;
    }

    public static class ScrollBallFootBallResultItems implements Serializable {

        private static final long serialVersionUID = -7469788836779849492L;
        private int id;
        private String title;
        private String byTitle;
        private String integral1;
        private String integral2;
        private String integral3;
        private String integral4;
        private boolean isHome;//是否主队赢

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

        public String getByTitle() {
            return byTitle;
        }

        public void setByTitle(String byTitle) {
            this.byTitle = byTitle;
        }

        public String getIntegral1() {
            return integral1;
        }

        public void setIntegral1(String integral1) {
            this.integral1 = integral1;
        }

        public String getIntegral2() {
            return integral2;
        }

        public void setIntegral2(String integral2) {
            this.integral2 = integral2;
        }

        public String getIntegral3() {
            return integral3;
        }

        public void setIntegral3(String integral3) {
            this.integral3 = integral3;
        }

        public String getIntegral4() {
            return integral4;
        }

        public void setIntegral4(String integral4) {
            this.integral4 = integral4;
        }

        public boolean isHome() {
            return isHome;
        }

        public void setHome(boolean home) {
            isHome = home;
        }
    }
}
