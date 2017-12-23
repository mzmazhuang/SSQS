package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/17 16:29
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferCircleBean {

    /**
     * status : true
     * data : {"categorys":[{"id":1,"totalCount":p3,"imageUrl":" http://192.168.0.134/images/article/20160719022330pvPKnrZEQF.jpg","name":"中国足球","isFouce":0,"fanCount":0}],"articles":[{"sno":0,"categoryID":1,"commentCount":p3,"imageUrl3":"/article/20160719022442QCDDybzqFK.jpg","imageUrl2":"/article/20160719022442aAuugdebrN.jpg","imageUrl1":"/article/20160719022330pvPKnrZEQF.jpg","content":"<p>test2223<\/p><p><br><\/p>","id":8,"categoryName":"中国足球","userID":"20160708134661","title":"test23","userName":"实事球事","createDate":"2016-07-18 22:51:45","zanCount":1}]}
     * code : 0
     * msg :
     */
    public boolean status;
    public DataEntity data;
    public int        code;
    public String     msg;

    public static class DataEntity {
        /**
         * categorys : [{"id":1,"totalCount":p3,"imageUrl":" http://192.168.0.134/images/article/20160719022330pvPKnrZEQF.jpg","name":"中国足球","isFouce":0,"fanCount":0}]
         * articles : [{"sno":0,"categoryID":1,"commentCount":p3,"imageUrl3":"/article/20160719022442QCDDybzqFK.jpg","imageUrl2":"/article/20160719022442aAuugdebrN.jpg","imageUrl1":"/article/20160719022330pvPKnrZEQF.jpg","content":"<p>test2223<\/p><p><br><\/p>","id":8,"categoryName":"中国足球","userID":"20160708134661","title":"test23","userName":"实事球事","createDate":"2016-07-18 22:51:45","zanCount":1}]
         */
        public List<CategorysEntity> categorys;
        public List<ArticlesEntity> articles;

        public static class CategorysEntity {
            @Override
            public String toString() {
                return "{" + "\"id\":" + id +
                        ",\"totalCount\":" + totalCount +
                        ", \"imageUrl\":\"" + imageUrl + "\"" +
                        ", \"name\":\"" + name + "\"" +
                        ", \"isFouce\":" + isFouce +
                        ",\"fanCount\" :" + fanCount +
                        '}';
            }

            /**
             * id : 1
             * totalCount : p3
             * imageUrl :  http://192.168.0.134/images/article/20160719022330pvPKnrZEQF.jpg
             * name : 中国足球
             * isFouce : 0
             * fanCount : 0
             */
            public int id;
            public int    totalCount;
            public String imageUrl;
            public String name;
            public int    isFouce;
            public int    fanCount;
        }

        public static class ArticlesEntity {
            /**
             * id : 8
             * content : <p>test2223</p><p><br></p>
             * userID : 20160708134661
             * categoryName : 中国足球
             * title : test23
             * sno : 0
             * imageUrl : ["http://112.74.130.167/images/article/20161214114536RwbOYYqrAB-165x220.jpg","http://112.74.130.167/images/article/20161214114536TdlqJPdfvy-165x220.jpg","http://112.74.130.167/images/article/20161214114536anGBSFfVNv-165x220.jpg"]
             * categoryID : 1
             * userName : 实事球事
             * createDate : 2016-07-18 22:51:45
             * commentCount : 3
             * avatar :
             * zanCount : 1
             */
            public int id;
            public String       content;
            public String       userID;
            public String       categoryName;
            public String       title;
            public int          sno;
            public List<String> imageUrl;
            public int          categoryID;
            public String       userName;
            public String       createDate;
            public int          commentCount;
            public String       avatar;
            public int          zanCount;
        }
    }
}
