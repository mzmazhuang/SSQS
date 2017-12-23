package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/25 11:04
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeMessageBean implements Serializable {
    private static final long serialVersionUID = -3643948787709589025L;

    /**
     * status : true
     * data : [{"typeName":"中奖信息:","content":"恭喜\"段JJ\"在猜球中赢得1,080,000金币！","type":1},{"typeName":"中奖信息:","content":"恭喜\"为丽娟\"在猜球中赢得3,419,999金币！","type":1},{"typeName":"中奖信息:","content":"恭喜\"球迷迷\"在猜球中赢得1,136,800金币！","type":1}]
     * code : 0
     * msg :
     */

    /**
     * typeName : 中奖信息:
     * content : 恭喜"段JJ"在猜球中赢得1,080,000金币！
     * type : 1
     */
    public String typeName;
    public String content;
    public int type;
}
