package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/16 11:51
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SsxCity {
    /**
     * citylist : [{"c":[{"n":"东城区"},{"n":"西城区"},{"n":"崇文区"},{"n":"宣武区"},{"n":"朝阳区"},{"n":"丰台区"},{"n":"石景山区"},{"n":"海淀区"},{"n":"门头沟区"},{"n":"房山区"},{"n":"通州区"},{"n":"顺义区"},{"n":"昌平区"},{"n":"大兴区"},{"n":"平谷区"},{"n":"怀柔区"},{"n":"密云县"},{"n":"延庆县"}],"p":"北京"}"s":"锡山区"},{"s":"惠山区"},{"s":"滨湖区"},{"s":"江阴市"},{"s":"宜兴市"}],"n":"无锡"},{"a":[{"s":"鼓楼区
     */
    public List<CitylistEntity> citylist;

    public static class CitylistEntity {
        /**
         * c : [{"n":"东城区"},
         * {"n":"西城区"},
         * {"n":"崇文区"},
         * {"n":"宣武区"},
         * {"n":"朝阳区"},
         * {"n":"丰台区"},
         * {"n":"石景山区"},
         * {"n":"海淀区"},
         *
         * {"n":"门头沟区"},
         * {"n":"房山区"},
         * {"n":"通州区"},
         * {"n":"顺义区"},
         * {"n":"昌平区"},
         * {"n":"大兴区"},
         * {"n":"平谷区"},
         * {"n":"怀柔区"},
         * {"n":"密云县"},
         * {"n":"延庆县"}]
         * p : 北京
         */
        public List<CEntity> c;
        public String        p;

        public static class CEntity {
            /**
             * n: "乌鲁木齐"
             * a:[
             * {"s": "天山区"},
             * {"s": "沙依巴克区"},
             * {"s": "新市区"},
             * {"s": "水磨沟区"},
             * {"s": "头屯河区"},
             * {"s": "达坂城区"},
             * {"s": "米东区"},
             * {"s": "乌鲁木齐县"}
             * ]
             */
            public String        n;
            public List<XEntity> a;
        }

        public static class XEntity {
            /**
             * n : 东城区
             */
            public String s;
        }
    }
}
