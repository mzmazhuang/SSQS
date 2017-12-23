package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/17 19:02
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantLeveBean implements Serializable{

    private static final long serialVersionUID = 4300973625067705358L;
    /**
     * id : 20160704116012
     * level : 1
     * tag : 3
     * userName : 球技过人
     * avatar : http://192.168.0.115:8080/images/avatar/test_1.jpg
     * intro : 我就是我，不一样的烟火！
     */
    public String id;
    public int level;
    public String tag;
    public String userName;
    public String avatar;
    public String intro;
}
