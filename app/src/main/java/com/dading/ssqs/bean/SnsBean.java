package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/13 14:51
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SnsBean implements Serializable {
    private static final long serialVersionUID = 7189501922839821083L;

    /**
     * status : true
     * data : {"topPic":[{"writeType":1,"writeTypeName":"头条","isDeep":1,"commentCount":0,"type":1,"avatar":"/avatar/test_1.jpg","id":1,"content":"sfsfasfasfas fas fsaf saf saf asf saf sadf s","userID":"20160704116012","title":"test1","userName":"球技过人GG","isAdv":1,"smallImage":"/country/ukraine_nationalflag.png","isTop":1,"createDate":"2017-03-13 11:14:08"}],"totalCount":1,"writes":[{"writeType":1,"writeTypeName":"头条","isDeep":1,"commentCount":0,"type":1,"avatar":"/avatar/test_1.jpg","id":1,"content":"sfsfasfasfas fas fsaf saf saf asf saf sadf s","userID":"20160704116012","title":"test1","userName":"球技过人GG","isAdv":1,"smallImage":"/country/ukraine_nationalflag.png","isTop":1,"createDate":"2017-03-13 11:14:08"}],"hasTop":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * topPic : [{"writeType":1,"writeTypeName":"头条","isDeep":1,"commentCount":0,"type":1,"avatar":"/avatar/test_1.jpg","id":1,"content":"sfsfasfasfas fas fsaf saf saf asf saf sadf s","userID":"20160704116012","title":"test1","userName":"球技过人GG","isAdv":1,"smallImage":"/country/ukraine_nationalflag.png","isTop":1,"createDate":"2017-03-13 11:14:08"}]
     * totalCount : 1
     * writes : [{"writeType":1,"writeTypeName":"头条","isDeep":1,"commentCount":0,"type":1,"avatar":"/avatar/test_1.jpg","id":1,"content":"sfsfasfasfas fas fsaf saf saf asf saf sadf s","userID":"20160704116012","title":"test1","userName":"球技过人GG","isAdv":1,"smallImage":"/country/ukraine_nationalflag.png","isTop":1,"createDate":"2017-03-13 11:14:08"}]
     * hasTop : 1
     * totalPage : 1
     */
    public List<TopPicEntity> topPic;
    public List<WritesEntity> writes;
    public int hasTop;

    public static class TopPicEntity implements Serializable {
        /**
         * writeType : 1
         * writeTypeName : 头条
         * isDeep : 1
         * commentCount : 0
         * type : 1
         * avatar : /avatar/test_1.jpg
         * id : 1
         * content : sfsfasfasfas fas fsaf saf saf asf saf sadf s
         * userID : 20160704116012
         * title : test1
         * userName : 球技过人GG
         * isAdv : 1
         * smallImage : /country/ukraine_nationalflag.png
         * isTop : 1
         * createDate : 2017-03-13 11:14:08
         */
        public int writeType;
        public String writeTypeName;
        public int isDeep;
        public int commentCount;
        public int type;
        public String avatar;
        public int id;
        public String content;
        public String userID;
        public String title;
        public String userName;
        public int isAdv;
        public String smallImage;
        public int isTop;
        public String createDate;
    }

    public static class WritesEntity implements Serializable {
        /**
         * writeType : 1
         * writeTypeName : 头条
         * isDeep : 1
         * commentCount : 0
         * type : 1
         * avatar : /avatar/test_1.jpg
         * id : 1
         * content : sfsfasfasfas fas fsaf saf saf asf saf sadf s
         * userID : 20160704116012
         * title : test1
         * userName : 球技过人GG
         * isAdv : 1
         * smallImage : /country/ukraine_nationalflag.png
         * isTop : 1
         * createDate : 2017-03-13 11:14:08
         */
        public int writeType;
        public String writeTypeName;
        public int isDeep;
        public int commentCount;
        public int type;
        public String avatar;
        public int id;
        public String content;
        public String userID;
        public String title;
        public String userName;
        public int isAdv;
        public String smallImage;
        public int isTop;
        public String createDate;
    }
}
