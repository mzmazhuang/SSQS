package com.dading.ssqs.bean;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/12 17:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SplashBean {

    /**
     * status : true
     * data : {"id":"20160704116012","sex":1,"username":"球技过人","level":1,"banlance":500,"address":null,"diamond":0,"authToken":"61411c677a9e41cca6bb3f94cbe4a1b2","avatar":"http://192.168.0.134/images/avatar/test-1.jpg","signature":null,"userType":1}
     * code : 0
     * msg :
     */
    public boolean status;
    public DataEntity data;
    public int        code;
    public String     msg;

    public static class DataEntity {
        /**
         * id : 20160704116012
         * sex : 1
         * username : 球技过人
         * level : 1
         * banlance : 500
         * address : null
         * diamond : 0
         * authToken : 61411c677a9e41cca6bb3f94cbe4a1b2
         * avatar : http://192.168.0.134/images/avatar/test-1.jpg
         * signature : null
         * userType : 1
         */
        public String id;
        public int    sex;
        public String username;
        public int    level;
        public int    banlance;
        public String address;
        public int    diamond;
        public String authToken;
        public String avatar;
        public String signature;
        public int    userType;
    }
}
