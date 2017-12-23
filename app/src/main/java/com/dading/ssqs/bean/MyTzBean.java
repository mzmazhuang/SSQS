package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/25 13:46
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyTzBean implements Serializable {
    private static final long serialVersionUID = 4580891557308041033L;

    /**
     * status : true
     * data : {"totalCount":1,"items":[{"id":18,"content":"　　在今天","userID":"20160704116012","categoryName":"中国足球","title":"央视:本届奥运会阵痛不小 可惜镜头不能只拍女排","sno":0,"imageUrl":["http://192.168.0.134/images/avatar/test-1.jpg"],"categoryID":1,"userName":"球技过人","createDate":"2016-08-23 10:15:48","commentCount":1,"avatar":"http://192.168.0.134/images/avatar/test-1.jpg","zanCount":1}],"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * id : 18
     * content : 　　在今天
     * userID : 20160704116012
     * categoryName : 中国足球
     * title : 央视:本届奥运会阵痛不小 可惜镜头不能只拍女排
     * sno : 0
     * imageUrl : ["http://192.168.0.134/images/avatar/test-1.jpg"]
     * categoryID : 1
     * userName : 球技过人
     * createDate : 2016-08-23 10:15:48
     * commentCount : 1
     * avatar : http://192.168.0.134/images/avatar/test-1.jpg
     * zanCount : 1
     */
    public int id;
    public String content;
    public String userID;
    public String categoryName;
    public String title;
    public int sno;
    public List<String> imageUrl;
    public int categoryID;
    public String userName;
    public String createDate;
    public int commentCount;
    public String avatar;
    public int zanCount;
}
