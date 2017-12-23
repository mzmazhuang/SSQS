package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/18 15:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AllCircleRBean implements Serializable {

    /**
     * status : true
     * data : [{"id":1,"sno":1,"totalCount":11,"imageUrl":"","isFouce":0,"fanCount":1,"name":"中国足球"},{"id":2,"sno":1,"totalCount":0,"imageUrl":"","isFouce":0,"fanCount":0,"name":"五大联赛"}]
     * code : 0
     * msg :
     */

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
     * sno : 1
     * totalCount : 11
     * imageUrl :
     * isFouce : 0
     * fanCount : 1
     * name : 中国足球
     */

    public int id;
    public int sno;
    public int totalCount;
    public String imageUrl;
    public int isFouce;
    public int fanCount;
    public String name;
}
