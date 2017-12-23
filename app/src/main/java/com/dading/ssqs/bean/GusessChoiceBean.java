package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/28 14:06
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GusessChoiceBean implements Serializable {
    private static final long serialVersionUID = 1189183494209311314L;

    /**
     * status : true
     * data : [{"pinyin":"a","filter":[{"title":"澳足杯","nums":2}]},{"pinyin":"y","filter":[{"title":"英超","nums":19}]}]
     * code : 0
     * msg :
     */

    /**
     * pinyin : a
     * filter : [{"title":"澳足杯","nums":2}]
     */
    public String pinyin;
    public List<FilterEntity> filter;

    public static class FilterEntity implements Serializable {
        /**
         * title : 澳足杯
         * nums : 2
         */
        public String title;
        public int nums;
        public int id;
        public boolean checked;

    }
}
