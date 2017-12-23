package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/6.
 */

public class ScrollBallFootBallBean implements Serializable {

    private static final long serialVersionUID = 3894700851438917156L;

    private CommonTitle title;
    private List<ScrollBeanItems> items;

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

    public List<ScrollBeanItems> getItems() {
        return items;
    }

    public void setItems(List<ScrollBeanItems> items) {
        this.items = items;
    }

    public static class ScrollBeanItems implements Serializable {

        private static final long serialVersionUID = -800708922151595106L;

        private int id;
        private String time;
        private String title;
        private String byTitle;
        private List<ScrollBeanItem> testItems;

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

        public List<ScrollBeanItem> getTestItems() {
            return testItems;
        }

        public void setTestItems(List<ScrollBeanItem> testItems) {
            this.testItems = testItems;
        }

        public static class ScrollBeanItem implements Serializable {

            private static final long serialVersionUID = -4378368475124193894L;

            private int id;
            private int background;
            private String leftStr;
            private String rightStr;
            private int position;
            private int selected;// 1:主胜2：客胜3：和局

            public ScrollBeanItem() {

            }

            public ScrollBeanItem(int color) {
                setBackground(color);
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getBackground() {
                return background;
            }

            public void setBackground(int background) {
                this.background = background;
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

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }
    }
}
