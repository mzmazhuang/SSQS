package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 */

public class ChampionBean implements Serializable {
    private static final long serialVersionUID = -7319049440067505976L;

    private CommonTitle commonTitle;
    private List<ChampionItems> items;

    public CommonTitle getCommonTitle() {
        return commonTitle;
    }

    public void setCommonTitle(CommonTitle commonTitle) {
        this.commonTitle = commonTitle;
    }

    public List<ChampionItems> getItems() {
        return items;
    }

    public void setItems(List<ChampionItems> items) {
        this.items = items;
    }

    public static class ChampionItems implements Serializable {

        private static final long serialVersionUID = -4746436074730104149L;

        private int leagueId;
        private String title;
        private List<ChampionItem> items;

        public int getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(int leagueId) {
            this.leagueId = leagueId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ChampionItem> getItems() {
            return items;
        }

        public void setItems(List<ChampionItem> items) {
            this.items = items;
        }

        public static class ChampionItem implements Serializable {

            private static final long serialVersionUID = 3449388858089112552L;

            private int id;
            private int leagueId;
            private String leftStr;
            private String rightStr;
            private int position;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLeagueId() {
                return leagueId;
            }

            public void setLeagueId(int leagueId) {
                this.leagueId = leagueId;
            }

            public String getLeftStr() {
                return leftStr;
            }

            public void setLeftStr(String leftStr) {
                this.leftStr = leftStr;
            }

            public String getRightStr() {
                return rightStr;
            }

            public void setRightStr(String rightStr) {
                this.rightStr = rightStr;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }
        }
    }
}
