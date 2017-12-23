package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/25 14:03
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyTzGTBean implements Serializable {

    /**
     * status : true
     * data : {"items":[{"title":"奥运会上的\u201c失意\u201d与\u201c得意\u201d","articleID":10,"comments":[{"content":"shisdfsadf sa fsaf asf sf ","userID":"20160704116012","userName":"球技过人","avatar":"http://192.168.0.134/images/avatar/test-1.jpg","createDate":"2016-08-22 14:05:11","articleID":10}]}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * title : 奥运会上的“失意”与“得意”
     * articleID : 10
     * comments : [{"content":"shisdfsadf sa fsaf asf sf ","userID":"20160704116012","userName":"球技过人","avatar":"http://192.168.0.134/images/avatar/test-1.jpg","createDate":"2016-08-22 14:05:11","articleID":10}]
     */
    public String title;
    public int articleID;
    public List<CommentsEntity> comments;

    public static class CommentsEntity implements Serializable {
        /**
         * content : shisdfsadf sa fsaf asf sf
         * userID : 20160704116012
         * userName : 球技过人
         * avatar : http://192.168.0.134/images/avatar/test-1.jpg
         * createDate : 2016-08-22 14:05:11
         * articleID : 10
         */
        public String content;
        public String userID;
        public String userName;
        public String avatar;
        public String createDate;
        public int articleID;
    }
}
