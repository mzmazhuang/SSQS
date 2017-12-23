package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/10 16:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GeneralUserBean implements Serializable {

    private static final long serialVersionUID = 7401971577612201065L;
    /**
     * id : 20160704116012
     * record : [{"draw":0,"lost":0,"banlance":0,"repayRate":0,"winRate":0,"win":0,"type":1},{"draw":0,"lost":0,"banlance":0,"repayRate":0,"winRate":0,"win":0,"type":2}]
     * isFriend : 0
     * rank : 2
     * banlance : 57098
     * userName : 球技过人
     * isSelf : 0
     * avatar : http://112.74.130.167/images/avatar/test-1.jpg
     * signature : 实事球是，最足球，跟我一起玩吧！
     */
    public String id;
    public List<RecordEntity> record;
    public int isFriend;
    public String rank;
    public int banlance;
    public String userName;
    public int isSelf;
    public String avatar;
    public String signature;

    public static class RecordEntity implements Serializable {
        private static final long serialVersionUID = -9206416695321191668L;
        /**
         * draw : 0
         * lost : 0
         * banlance : 0
         * repayRate : 0
         * winRate : 0
         * win : 0
         * type : 1
         */
        public int draw;
        public int lost;
        public int banlance;
        public int repayRate;
        public int winRate;
        public int win;
        public int type;
    }
}
