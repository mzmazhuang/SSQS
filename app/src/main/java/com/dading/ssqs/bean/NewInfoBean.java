package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/24 11:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NewInfoBean {

    /**
     * status : true
     * data : {"articles":{"hotCount":1,"imageUrl":["http://192.168.0.134/images/article/20160719022442aAuugdebrN.jpg"],"categoryID":1,"isFouce":0,"isZan":0,"commentCount":58,"avatar":"/advert/home_vp_pic2.png","isCollect":1,"content":"    ","id":8,"userID":"20160708134661","categoryName":"中国足球","title":"你若不离不弃，我便深情相依","categoryImageUrl":"/article/20160719022330pvPKnrZEQF.jpg","fanCount":0,"userName":"实事球事","createDate":"2016-07-18 22:51:45","zanCount":1},"comments":[{"content":"xxx","id":4,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-25 15:07:33","avatar":"/advert/home_vp_pic2.png"},{"content":"xxx","id":3,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-19 17:37:59","avatar":"/advert/home_vp_pic2.png"},{"content":"xxx","id":2,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-19 17:37:47","avatar":"/advert/home_vp_pic2.png"}]}
     * code : 0
     * msg :
     */
    /**
     * articles : {"hotCount":1,"imageUrl":["http://192.168.0.134/images/article/20160719022442aAuugdebrN.jpg"],"categoryID":1,"isFouce":0,"isZan":0,"commentCount":58,"avatar":"/advert/home_vp_pic2.png","isCollect":1,"content":"    ","id":8,"userID":"20160708134661","categoryName":"中国足球","title":"你若不离不弃，我便深情相依","categoryImageUrl":"/article/20160719022330pvPKnrZEQF.jpg","fanCount":0,"userName":"实事球事","createDate":"2016-07-18 22:51:45","zanCount":1}
     * comments : [{"content":"xxx","id":4,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-25 15:07:33","avatar":"/advert/home_vp_pic2.png"},{"content":"xxx","id":3,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-19 17:37:59","avatar":"/advert/home_vp_pic2.png"},{"content":"xxx","id":2,"userID":"20160704116012","userName":"匿名","createDate":"2016-07-19 17:37:47","avatar":"/advert/home_vp_pic2.png"}]
     */
    public ArticlesEntity articles;
    public List<CommentsEntity> comments;

    public static class ArticlesEntity {
        /**
         * hotCount : 1
         * imageUrl : ["http://192.168.0.134/images/article/20160719022442aAuugdebrN.jpg"]
         * categoryID : 1
         * isFouce : 0
         * isZan : 0
         * commentCount : 58
         * avatar : /advert/home_vp_pic2.png
         * isCollect : 1
         * content :
         * id : 8
         * userID : 20160708134661
         * categoryName : 中国足球
         * title : 你若不离不弃，我便深情相依
         * categoryImageUrl : /article/20160719022330pvPKnrZEQF.jpg
         * fanCount : 0
         * userName : 实事球事
         * createDate : 2016-07-18 22:51:45
         * zanCount : 1
         */
        public int hotCount;
        public List<String> imageUrl;
        public int categoryID;
        public int isFouce;
        public int isZan;
        public int commentCount;
        public String avatar;
        public int isCollect;
        public String content;
        public int id;
        public String userID;
        public String categoryName;
        public String title;
        public String categoryImageUrl;
        public int fanCount;
        public String userName;
        public String createDate;
        public int zanCount;
    }

    public static class CommentsEntity {
        /**
         * content : xxx
         * id : 4
         * userID : 20160704116012
         * userName : 匿名
         * createDate : 2016-07-25 15:07:33
         * avatar : /advert/home_vp_pic2.png
         */
        public String content;
        public int id;
        public String userID;
        public String userName;
        public String createDate;
        public String avatar;
    }
}
