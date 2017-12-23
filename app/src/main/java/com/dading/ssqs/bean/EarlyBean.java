package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/9.
 */

public class EarlyBean implements Serializable {
    private static final long serialVersionUID = 5417594684866279799L;

    private List<EarlyBeanItems> leagueName;

    public List<EarlyBeanItems> getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(List<EarlyBeanItems> leagueName) {
        this.leagueName = leagueName;
    }

    public static class EarlyBeanItems implements Serializable {

        private static final long serialVersionUID = -2576830598329000042L;

        private int id;
        private String title;
        private int nums;
        private List<EarlyMatchs> matchs;

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

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public List<EarlyMatchs> getMatchs() {
            return matchs;
        }

        public void setMatchs(List<EarlyMatchs> matchs) {
            this.matchs = matchs;
        }
    }

    public static class EarlyMatchs implements Serializable {

        private static final long serialVersionUID = 3795820575560654662L;

        private int id;
        private String home;
        private String away;
        private String hOrder;
        private String aOrder;
        private String openTime;
        private String leagueName;
        private String hOrderFrom;
        private String aOrderFrom;
        private String source;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getAway() {
            return away;
        }

        public void setAway(String away) {
            this.away = away;
        }

        public String gethOrder() {
            return hOrder;
        }

        public void sethOrder(String hOrder) {
            this.hOrder = hOrder;
        }

        public String getaOrder() {
            return aOrder;
        }

        public void setaOrder(String aOrder) {
            this.aOrder = aOrder;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String gethOrderFrom() {
            return hOrderFrom;
        }

        public void sethOrderFrom(String hOrderFrom) {
            this.hOrderFrom = hOrderFrom;
        }

        public String getaOrderFrom() {
            return aOrderFrom;
        }

        public void setaOrderFrom(String aOrderFrom) {
            this.aOrderFrom = aOrderFrom;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
