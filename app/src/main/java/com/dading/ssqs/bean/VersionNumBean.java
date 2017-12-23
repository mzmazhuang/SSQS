package com.dading.ssqs.bean;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/11 9:38
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class VersionNumBean {

    /**
     * status : true
     * data : {"isDownload":1,"downloadUrl":"http://www.baidu.com","versionNO":"1.0","type":0}
     * code : 0
     * msg :
     */
    public boolean status;
    public DataEntity data;
    public int        code;
    public String     msg;

    public static class DataEntity {
        /**
         * isDownload : 1
         * downloadUrl : http://www.baidu.com
         * versionNO : 1.0
         * type : 0
         */
        public int isDownload;
        public String downloadUrl;
        public String versionNO;
        public int    type;
    }
}
