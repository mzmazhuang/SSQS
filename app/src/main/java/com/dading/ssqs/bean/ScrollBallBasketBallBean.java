package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/6.
 */

public class ScrollBallBasketBallBean implements Serializable {

    private static final long serialVersionUID = 3894700851438917156L;

    private CommonTitle title;
    private List<ScrollBaksetBallItems> items;
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

    public List<ScrollBaksetBallItems> getItems() {
        return items;
    }

    public void setItems(List<ScrollBaksetBallItems> items) {
        this.items = items;
    }

    public static class ScrollBaksetBallItems implements Serializable {

        private static final long serialVersionUID = -800708922151595106L;

        private int id;
        private String time;
        private String title;
        private String byTitle;
        private Score score;
        private List<ScrollBeanItem> testItems;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Score getScore() {
            return score;
        }

        public void setScore(Score score) {
            this.score = score;
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

        public List<ScrollBeanItem> getTestItems() {
            return testItems;
        }

        public void setTestItems(List<ScrollBeanItem> testItems) {
            this.testItems = testItems;
        }

        public static class Score implements Serializable {

            private static final long serialVersionUID = 2917114233928642593L;

            private String currSchedule;//第几节
            private String leftScore;
            private String rightScore;

            public String getCurrSchedule() {
                return currSchedule;
            }

            public void setCurrSchedule(String currSchedule) {
                this.currSchedule = currSchedule;
            }

            public String getLeftScore() {
                return leftScore;
            }

            public void setLeftScore(String leftScore) {
                this.leftScore = leftScore;
            }

            public String getRightScore() {
                return rightScore;
            }

            public void setRightScore(String rightScore) {
                this.rightScore = rightScore;
            }
        }

        public static class ScrollBeanItem implements Serializable {

            private static final long serialVersionUID = -4378368475124193894L;

            private String leftStr;
            private String rightStr;
            private int id;
            private int position;
            private int selected;// 1:主胜2：客胜3：和局

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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }
    }
}
