package com.dading.ssqs.bean;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/30 16:40
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class JPUSHBean {

    /**
     * android : {"title":"实是球事更新啦！","alert":"（1）修改了让球的数据，更专业！（2）增加了邀请奖励，只要您邀请，赠送1000金币!（3）被邀请人输入邀请码获得1000金币奖励 （4）增加了专家推荐奖励规则。","extras":{"forwardID":229}}
     * ios : {"alert":"（1）修改了让球的数据，更专业！（2）增加了邀请奖励，只要您邀请，赠送1000金币!（3）被邀请人输入邀请码获得1000金币奖励 （4）增加了专家推荐奖励规则。","sound":"default","badge":"+1","extras":{"forwardID":229}}
     * alert : （1）修改了让球的数据，更专业！（2）增加了邀请奖励，只要您邀请，赠送1000金币!（3）被邀请人输入邀请码获得1000金币奖励 （4）增加了专家推荐奖励规则。
     */
    public AndroidEntity android;
    public IosEntity ios;
    public String    alert;

    public static class AndroidEntity {
        /**
         * title : 实是球事更新啦！
         * alert : （1）修改了让球的数据，更专业！（2）增加了邀请奖励，只要您邀请，赠送1000金币!（3）被邀请人输入邀请码获得1000金币奖励 （4）增加了专家推荐奖励规则。
         * extras : {"forwardID":229}
         */
        public String title;
        public String       alert;
        public ExtrasEntity extras;

        public static class ExtrasEntity {
            /**
             * forwardID : 229
             */
            public int forwardID;
            public int forwardType;
        }
    }

    public static class IosEntity {
        /**
         * alert : （1）修改了让球的数据，更专业！（2）增加了邀请奖励，只要您邀请，赠送1000金币!（3）被邀请人输入邀请码获得1000金币奖励 （4）增加了专家推荐奖励规则。
         * sound : default
         * badge : +1
         * extras : {"forwardID":229}
         */
        public String alert;
        public String       sound;
        public String       badge;
        public ExtrasEntity extras;

        public static class ExtrasEntity {
            /**
             * forwardID : 229
             */
            public int forwardID;
        }
    }
}
