package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/25 14:37
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FileUpResultBean {

    /**
     * status : true
     * data : {"imageUrl":["http://112.74.130.167/images/identity/20170208110334pBafXVBBAe-20x20.jpg"]}
     * code : 0
     * msg :
     */
    public boolean status;
    public DataEntity data;
    public int        code;
    public String     msg;

    public static class DataEntity {
        /**
         * imageUrl : ["http://112.74.130.167/images/identity/20170208110334pBafXVBBAe-20x20.jpg"]
         */
        public List<String> imageUrl;
    }
}
