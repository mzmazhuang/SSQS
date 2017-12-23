package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/20 15:02
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class InviteCodeBean implements Serializable {

    private static final long serialVersionUID = 2711848015114780837L;
    /**
     * banlance : 1000
     * count : 1
     * shareUrl	 : http://www.baiu.com
     * code : SSQS15468579
     * shareContent : 推荐内容：http://www.baiu.com
     */
    public int banlance;
    public int count;
    public String shareUrl;
    public String code;
    public String shareContent;
}
