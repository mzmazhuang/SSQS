package com.dading.ssqs.bean;


import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/22 11:59
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SNSCollectBean implements Serializable {
    private static final long serialVersionUID = -1886527565966614316L;

    /**
     * status : true
     * data : {"items":[{"writeType":1,"writeTypeName":"头条","isDeep":0,"commentCount":11,"type":1,"avatar":"http://192.168.0.115:8080/images/avatar/20170315110130MIbjAYoVOE-100x100.jpg","id":18,"content":"周二晚上！","userID":"20160929048804","title":"是温格毁了阿森纳吗？","userName":"精彩高手","isAdv":0,"smallImage":"http://www.ddzlink.com/php-uploads/2017/03/13/2017031316533240631.jpg","isTop":1,"createDate":"2017-03-13 16:53:35"}],"totalCount":1,"totalPage":1}
     * code : 0
     * msg :
     */

    /**
     * writeType : 1
     * writeTypeName : 头条
     * isDeep : 0
     * commentCount : 11
     * type : 1
     * avatar : http://192.168.0.115:8080/images/avatar/20170315110130MIbjAYoVOE-100x100.jpg
     * id : 18
     * content : 周二晚上！
     * userID : 20160929048804
     * title : 是温格毁了阿森纳吗？
     * userName : 精彩高手
     * isAdv : 0
     * smallImage : http://www.ddzlink.com/php-uploads/2017/03/13/2017031316533240631.jpg
     * isTop : 1
     * createDate : 2017-03-13 16:53:35
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
