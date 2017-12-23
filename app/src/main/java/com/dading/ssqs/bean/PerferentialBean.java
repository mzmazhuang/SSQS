package com.dading.ssqs.bean;


import java.io.Serializable;

/**
 * Created by lenovo on 2017/6/30.
 */
public class PerferentialBean implements Serializable {
    private static final long serialVersionUID = -1660515718185214010L;

    /**
     * status : true
     * code : 0
     * msg :
     * data : [{"imageUrl":"http://www.ddzlink.com/images/activity/20170821045706yLpbpabAFr-650x212.jpg","webUrl":"","title":"注册优惠","content":"<p>阿斯顿发顺丰<\/p><p><br><\/p>","startDate":"2017-08-15 09:14:29","endDate":"2017-08-31 09:14:31","remark":""},{"imageUrl":"http://www.ddzlink.com/images/activity/20170821045643dYGRLPunkA-1920x650.jpg","webUrl":"","title":"3232eee","content":"<p><br><\/p><p>新用户注册送彩金100元<\/p><p>新用户注册送彩金100元<\/p><p>新用户注册送彩金100元<\/p><p>新用户注册送彩金100元<\/p><p>新用户注册送彩金100元<\/p><p><\/p><p><img src=\"http://img.baidu.com/hi/jx2/j_0019.gif\"><\/p><p><br><\/p>","startDate":"2017-08-11 10:34:43","endDate":"2017-08-25 10:34:44","remark":""}]
     */

    /**
     * imageUrl : http://www.ddzlink.com/images/activity/20170821045706yLpbpabAFr-650x212.jpg
     * webUrl :
     * title : 注册优惠
     * content : <p>阿斯顿发顺丰</p><p><br></p>
     * startDate : 2017-08-15 09:14:29
     * endDate : 2017-08-31 09:14:31
     * remark :
     */

    private String imageUrl;
    private String webUrl;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String remark;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
