package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/10 16:30
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FXBean implements Serializable {


    private static final long serialVersionUID = -4611184860514438542L;

    /**
     * status : true
     * data : {"awayZl":"5胜1平4负","aOverall":64,"aAvGetBallPer":50,"awayZlPer":57,"aZlPoint":"积16分","hAvGetBallPer":50,"hOverall":36,"hZlPoint":"积9分","aAvLostBall":"场均失球1.0个","aOrder":[{"nums":["总","4","10","5","1","4","19/14","16","50%"],"name":"总"},{"nums":["主","4","6","p3","1","2","12/8","10","50%"],"name":"主"},{"nums":["客","0","p3","2","0","1","5/p3","6","67%"],"name":"客"},{"nums":["近六场","--","4","2","0","2","6/7","6","50%"],"name":"近六场"}],"hZkPoint":"积6分","homeZkPer":46,"hAvLostBallPer":60,"aAvLostBallPer":40,"aAvGetBall":"场均进球2.0个","matchType":1,"homeZl":"2胜3平5负","homeZk":"1胜3平1负","awayZkPer":54,"homeZlPer":43,"aZkPoint":"积10分","history":{"awayCount":0,"drawCount":0,"homeCount":0},"record":{"awayZl":57,"hWinDesc":"1胜1平5负","aRate":"胜57%","hRate":"胜14%","homeZl":14,"aWinDesc":"4胜0平3负"},"hAvGetBall":"场均进球2.0个","awayZk":"3胜1平2负","hOrder":[{"nums":["总","15","10","2","p3","5","16/22","9","20%"],"name":"总"},{"nums":["主","11","5","1","p3","1","10/8","6","20%"],"name":"主"},{"nums":["客","0","p3","0","0","p3","1/8","0","0%"],"name":"客"},{"nums":["近六场","--","5","1","0","4","14/6","p3","20%"],"name":"近六场"}],"hAvLostBall":"场均失球2.0个"}
     * code : 0
     * msg :
     */

    /**
     * awayZl : 5胜1平4负
     * aOverall : 64
     * aAvGetBallPer : 50
     * awayZlPer : 57
     * aZlPoint : 积16分
     * hAvGetBallPer : 50
     * hOverall : 36
     * hZlPoint : 积9分
     * aAvLostBall : 场均失球1.0个
     * aOrder : [{"nums":["总","4","10","5","1","4","19/14","16","50%"],"name":"总"},{"nums":["主","4","6","p3","1","2","12/8","10","50%"],"name":"主"},{"nums":["客","0","p3","2","0","1","5/p3","6","67%"],"name":"客"},{"nums":["近六场","--","4","2","0","2","6/7","6","50%"],"name":"近六场"}]
     * hZkPoint : 积6分
     * homeZkPer : 46
     * hAvLostBallPer : 60
     * aAvLostBallPer : 40
     * aAvGetBall : 场均进球2.0个
     * matchType : 1
     * homeZl : 2胜3平5负
     * homeZk : 1胜3平1负
     * awayZkPer : 54
     * homeZlPer : 43
     * aZkPoint : 积10分
     * history : {"awayCount":0,"drawCount":0,"homeCount":0}
     * record : {"awayZl":57,"hWinDesc":"1胜1平5负","aRate":"胜57%","hRate":"胜14%","homeZl":14,"aWinDesc":"4胜0平3负"}
     * hAvGetBall : 场均进球2.0个
     * awayZk : 3胜1平2负
     * hOrder : [{"nums":["总","15","10","2","p3","5","16/22","9","20%"],"name":"总"},{"nums":["主","11","5","1","p3","1","10/8","6","20%"],"name":"主"},{"nums":["客","0","p3","0","0","p3","1/8","0","0%"],"name":"客"},{"nums":["近六场","--","5","1","0","4","14/6","p3","20%"],"name":"近六场"}]
     * hAvLostBall : 场均失球2.0个
     */
    public String awayZl;
    public int aOverall;
    public int aAvGetBallPer;
    public int awayZlPer;
    public String aZlPoint;
    public int hAvGetBallPer;
    public int hOverall;
    public String hZlPoint;
    public String aAvLostBall;
    public List<AOrderEntity> aOrder;
    public String hZkPoint;
    public int homeZkPer;
    public int hAvLostBallPer;
    public int aAvLostBallPer;
    public String aAvGetBall;
    public int matchType;
    public String homeZl;
    public String homeZk;
    public int awayZkPer;
    public int homeZlPer;
    public String aZkPoint;
    public HistoryEntity history;
    public RecordEntity record;
    public String hAvGetBall;
    public String awayZk;
    public List<HOrderEntity> hOrder;
    public String hAvLostBall;

    public static class AOrderEntity implements Serializable {
        /**
         * nums : ["总","4","10","5","1","4","19/14","16","50%"]
         * name : 总
         */
        public List<String> nums;
        public String name;
    }

    public static class HistoryEntity implements Serializable {
        /**
         * awayCount : 0
         * drawCount : 0
         * homeCount : 0
         */
        public int awayCount;
        public int drawCount;
        public int homeCount;
    }

    public static class RecordEntity implements Serializable {
        /**
         * awayZl : 57
         * hWinDesc : 1胜1平5负
         * aRate : 胜57%
         * hRate : 胜14%
         * homeZl : 14
         * aWinDesc : 4胜0平3负
         */
        public int awayZl;
        public String hWinDesc;
        public String aRate;
        public String hRate;
        public int homeZl;
        public String aWinDesc;
    }

    public static class HOrderEntity implements Serializable {
        /**
         * nums : ["总","15","10","2","p3","5","16/22","9","20%"]
         * name : 总
         */
        public List<String> nums;
        public String name;
    }
}
