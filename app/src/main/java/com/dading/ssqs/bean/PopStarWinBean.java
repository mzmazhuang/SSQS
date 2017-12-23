package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/8 9:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PopStarWinBean {

    /**
     * status : true
     * data : {"items":[{"id":"20160704116012","level":1,"order":1,"tag":"3连红","isFouce":0,"userName":"匿名","isSelf":0,"winCount":10,"avatar":"http://192.168.0.134/images/advert/home_vp_pic2.png"}],"totalCount":2,"totalPage":1}
     * code : 0
     * msg :
     */
    public boolean status;
    public DataEntity data;
    public int        code;
    public String     msg;

    public static class DataEntity {
        /**
         * items : [{"id":"20160704116012","level":1,"order":1,"tag":"3连红","isFouce":0,"userName":"匿名","isSelf":0,"winCount":10,"avatar":"http://192.168.0.134/images/advert/home_vp_pic2.png"}]
         * totalCount : 2
         * totalPage : 1
         */
        public List<ItemsEntity> items;
        public int totalCount;
        public int totalPage;

        public static class ItemsEntity {
            /**
             * id : 20160704116012
             * level : 1
             * order : 1
             * tag : 3连红
             * isFouce : 0
             * userName : 匿名
             * isSelf : 0
             * winCount : 10
             * avatar : http://192.168.0.134/images/advert/home_vp_pic2.png
             */
            public String id;
            public int    level;
            public int    order;
            public String tag;
            public int    isFouce;
            public String userName;
            public int    isSelf;
            public int    winCount;
            public String avatar;
        }
    }
}
