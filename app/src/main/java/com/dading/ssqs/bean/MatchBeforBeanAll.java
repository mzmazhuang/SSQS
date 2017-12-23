package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/5 16:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchBeforBeanAll implements Serializable{

    /**
     * leagueName : [{"title":"英超","nums":8,"matchs":[{"home":"曼联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png","aImageUrl":"http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png","hRed":0,"openTime":"2016-09-10 19:30:00","isOver":0,"type":null,"aScore":"","aOrder":6,"fouceDate":null,"id":101,"away":"曼城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":4,"hScore":""},{"home":"伯恩利","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065013XJVOKmFJBb.png","aImageUrl":"http://192.168.0.134/images/team/20160813064636YyIXIKWOjL.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":5,"fouceDate":null,"id":103,"away":"赫尔城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":17,"hScore":""},{"home":"斯托克城","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065917alHatbGdhm.png","aImageUrl":"http://192.168.0.134/images/team/20160816035338eqACXkWzXS.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":12,"fouceDate":null,"id":105,"away":"热刺","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":13,"hScore":""},{"home":"米德尔斯堡","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065426tVIbvjepkx.png","aImageUrl":"http://192.168.0.134/images/team/20160813065101YcFTOPirNU.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":15,"fouceDate":null,"id":104,"away":"水晶宫","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":10,"hScore":""},{"home":"西汉姆联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025118jDxQfJjuFu.png","aImageUrl":"http://192.168.0.134/images/team/20160813070006jgQOmDUZLR.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":11,"fouceDate":null,"id":106,"away":"南安普敦","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":18,"hScore":""},{"home":"伯恩茅斯","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813070435zyvLjuQScx.png","aImageUrl":"http://192.168.0.134/images/team/20160816035205ixrKLzParQ.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":p3,"fouceDate":null,"id":102,"away":"西布朗","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":14,"hScore":""},{"home":"利物浦","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025138mAIlOFsGbL.png","aImageUrl":"http://192.168.0.134/images/team/20160813064914imrbvmLjOT.png","hRed":0,"openTime":"2016-09-11 00:30:00","isOver":0,"type":null,"aScore":"","aOrder":16,"fouceDate":null,"id":107,"away":"莱斯特","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":1,"hScore":""},{"home":"斯旺西","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816034959XmnCPlfBNI.png","aImageUrl":"http://192.168.0.134/images/team/20160816025155cTNJqNmpyV.png","hRed":0,"openTime":"2016-09-11 23:00:00","isOver":0,"type":null,"aScore":"","aOrder":7,"fouceDate":null,"id":108,"away":"切尔西","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":2,"hScore":""}]}]
     * leagueDate : [{"title":"2016-09-10","nums":6,"matchs":[{"home":"曼联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png","aImageUrl":"http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png","hRed":0,"openTime":"2016-09-10 19:30:00","isOver":0,"type":null,"aScore":"","aOrder":6,"fouceDate":null,"id":101,"away":"曼城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":4,"hScore":""},{"home":"伯恩利","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065013XJVOKmFJBb.png","aImageUrl":"http://192.168.0.134/images/team/20160813064636YyIXIKWOjL.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":5,"fouceDate":null,"id":103,"away":"赫尔城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":17,"hScore":""},{"home":"伯恩茅斯","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813070435zyvLjuQScx.png","aImageUrl":"http://192.168.0.134/images/team/20160816035205ixrKLzParQ.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":p3,"fouceDate":null,"id":102,"away":"西布朗","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":14,"hScore":""},{"home":"斯托克城","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065917alHatbGdhm.png","aImageUrl":"http://192.168.0.134/images/team/20160816035338eqACXkWzXS.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":12,"fouceDate":null,"id":105,"away":"热刺","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":13,"hScore":""},{"home":"米德尔斯堡","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065426tVIbvjepkx.png","aImageUrl":"http://192.168.0.134/images/team/20160813065101YcFTOPirNU.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":15,"fouceDate":null,"id":104,"away":"水晶宫","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":10,"hScore":""},{"home":"西汉姆联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025118jDxQfJjuFu.png","aImageUrl":"http://192.168.0.134/images/team/20160813070006jgQOmDUZLR.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":11,"fouceDate":null,"id":106,"away":"南安普敦","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":18,"hScore":""}]},{"title":"2016-09-11","nums":2,"matchs":[{"home":"利物浦","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025138mAIlOFsGbL.png","aImageUrl":"http://192.168.0.134/images/team/20160813064914imrbvmLjOT.png","hRed":0,"openTime":"2016-09-11 00:30:00","isOver":0,"type":null,"aScore":"","aOrder":16,"fouceDate":null,"id":107,"away":"莱斯特","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":1,"hScore":""},{"home":"斯旺西","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816034959XmnCPlfBNI.png","aImageUrl":"http://192.168.0.134/images/team/20160816025155cTNJqNmpyV.png","hRed":0,"openTime":"2016-09-11 23:00:00","isOver":0,"type":null,"aScore":"","aOrder":7,"fouceDate":null,"id":108,"away":"切尔西","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":2,"hScore":""}]}]
     */
    public List<LeagueNameEntity> leagueName;
    public List<LeagueDateEntity> leagueDate;

    public static class LeagueNameEntity implements Serializable{
        /**
         * title : 英超
         * nums : 8
         * matchs : [{"home":"曼联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png","aImageUrl":"http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png","hRed":0,"openTime":"2016-09-10 19:30:00","isOver":0,"type":null,"aScore":"","aOrder":6,"fouceDate":null,"id":101,"away":"曼城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":4,"hScore":""},{"home":"伯恩利","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065013XJVOKmFJBb.png","aImageUrl":"http://192.168.0.134/images/team/20160813064636YyIXIKWOjL.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":5,"fouceDate":null,"id":103,"away":"赫尔城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":17,"hScore":""},{"home":"斯托克城","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065917alHatbGdhm.png","aImageUrl":"http://192.168.0.134/images/team/20160816035338eqACXkWzXS.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":12,"fouceDate":null,"id":105,"away":"热刺","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":13,"hScore":""},{"home":"米德尔斯堡","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065426tVIbvjepkx.png","aImageUrl":"http://192.168.0.134/images/team/20160813065101YcFTOPirNU.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":15,"fouceDate":null,"id":104,"away":"水晶宫","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":10,"hScore":""},{"home":"西汉姆联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025118jDxQfJjuFu.png","aImageUrl":"http://192.168.0.134/images/team/20160813070006jgQOmDUZLR.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":11,"fouceDate":null,"id":106,"away":"南安普敦","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":18,"hScore":""},{"home":"伯恩茅斯","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813070435zyvLjuQScx.png","aImageUrl":"http://192.168.0.134/images/team/20160816035205ixrKLzParQ.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":p3,"fouceDate":null,"id":102,"away":"西布朗","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":14,"hScore":""},{"home":"利物浦","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025138mAIlOFsGbL.png","aImageUrl":"http://192.168.0.134/images/team/20160813064914imrbvmLjOT.png","hRed":0,"openTime":"2016-09-11 00:30:00","isOver":0,"type":null,"aScore":"","aOrder":16,"fouceDate":null,"id":107,"away":"莱斯特","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":1,"hScore":""},{"home":"斯旺西","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816034959XmnCPlfBNI.png","aImageUrl":"http://192.168.0.134/images/team/20160816025155cTNJqNmpyV.png","hRed":0,"openTime":"2016-09-11 23:00:00","isOver":0,"type":null,"aScore":"","aOrder":7,"fouceDate":null,"id":108,"away":"切尔西","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":2,"hScore":""}]
         */
        public String title;
        public int nums;
        public boolean isColor;


        public List<MatchsEntity> matchs;

        public static class MatchsEntity {

            /**
             * home : 曼联
             * leagueName : 英超
             * hYellow : 0
             * aHalfScore : null
             * isFouce : 0
             * hImageUrl : http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png
             * aImageUrl : http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png
             * hRed : 0
             * openTime : 2016-09-10 19:30:00
             * isOver : 0
             * type : null
             * aScore :
             * aOrder : 6
             * fouceDate : null
             * id : 101
             * away : 曼城
             * aYellow : 0
             * hHalfScore : null
             * joinCount : 0
             * aRed : 0
             * hOrder : 4
             * hScore :
             */
            public String home;
            public String leagueName;
            public int hYellow;
            public String aHalfScore;
            public int isFouce;
            public String hImageUrl;
            public String aImageUrl;
            public int hRed;
            public String openTime;
            public int isOver;
            public String type;
            public String aScore;
            public String aOrder;
            public String fouceDate;
            public int id;
            public String away;
            public int aYellow;
            public String hHalfScore;
            public int joinCount;
            public int aRed;
            public String hOrder;
            public String hScore;
        }

    }

    public static class LeagueDateEntity implements Serializable{
        /**
         * title : 2016-09-10
         * nums : 6
         * matchs : [{"home":"曼联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png","aImageUrl":"http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png","hRed":0,"openTime":"2016-09-10 19:30:00","isOver":0,"type":null,"aScore":"","aOrder":6,"fouceDate":null,"id":101,"away":"曼城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":4,"hScore":""},{"home":"伯恩利","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065013XJVOKmFJBb.png","aImageUrl":"http://192.168.0.134/images/team/20160813064636YyIXIKWOjL.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":5,"fouceDate":null,"id":103,"away":"赫尔城","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":17,"hScore":""},{"home":"伯恩茅斯","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813070435zyvLjuQScx.png","aImageUrl":"http://192.168.0.134/images/team/20160816035205ixrKLzParQ.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":p3,"fouceDate":null,"id":102,"away":"西布朗","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":14,"hScore":""},{"home":"斯托克城","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065917alHatbGdhm.png","aImageUrl":"http://192.168.0.134/images/team/20160816035338eqACXkWzXS.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":12,"fouceDate":null,"id":105,"away":"热刺","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":13,"hScore":""},{"home":"米德尔斯堡","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160813065426tVIbvjepkx.png","aImageUrl":"http://192.168.0.134/images/team/20160813065101YcFTOPirNU.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":15,"fouceDate":null,"id":104,"away":"水晶宫","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":10,"hScore":""},{"home":"西汉姆联","leagueName":"英超","hYellow":0,"aHalfScore":null,"isFouce":0,"hImageUrl":"http://192.168.0.134/images/team/20160816025118jDxQfJjuFu.png","aImageUrl":"http://192.168.0.134/images/team/20160813070006jgQOmDUZLR.png","hRed":0,"openTime":"2016-09-10 22:00:00","isOver":0,"type":null,"aScore":"","aOrder":11,"fouceDate":null,"id":106,"away":"南安普敦","aYellow":0,"hHalfScore":null,"joinCount":0,"aRed":0,"hOrder":18,"hScore":""}]
         */
        public String title;
        public int nums;
        public boolean isColor;
        public List<MatchsEntity> matchs;

        @Override
        public String toString() {
            return "LeagueDateEntity{" +
                    "title='" + title + '\'' +
                    ", nums=" + nums +
                    ", matchs=" + matchs +
                    '}';
        }

        public static class MatchsEntity implements Serializable{
            @Override
            public String toString() {
                return "MatchsEntity{" +
                        "home='" + home + '\'' +
                        ", leagueName='" + leagueName + '\'' +
                        ", hYellow=" + hYellow +
                        ", aHalfScore='" + aHalfScore + '\'' +
                        ", isFouce=" + isFouce +
                        ", hImageUrl='" + hImageUrl + '\'' +
                        ", aImageUrl='" + aImageUrl + '\'' +
                        ", hRed=" + hRed +
                        ", openTime='" + openTime + '\'' +
                        ", isOver=" + isOver +
                        ", type='" + type + '\'' +
                        ", aScore='" + aScore + '\'' +
                        ", aOrder=" + aOrder +
                        ", fouceDate='" + fouceDate + '\'' +
                        ", id=" + id +
                        ", away='" + away + '\'' +
                        ", aYellow=" + aYellow +
                        ", hHalfScore='" + hHalfScore + '\'' +
                        ", joinCount=" + joinCount +
                        ", aRed=" + aRed +
                        ", hOrder=" + hOrder +
                        ", hScore='" + hScore + '\'' +
                        '}';
            }

            /**
             * home : 曼联
             * leagueName : 英超
             * hYellow : 0
             * aHalfScore : null
             * isFouce : 0
             * hImageUrl : http://192.168.0.134/images/team/20160816035536GnuipnJQSy.png
             * aImageUrl : http://192.168.0.134/images/team/20160813070239bqMGabQqfk.png
             * hRed : 0
             * openTime : 2016-09-10 19:30:00
             * isOver : 0
             * type : null
             * aScore :
             * aOrder : 6
             * fouceDate : null
             * id : 101
             * away : 曼城
             * aYellow : 0
             * hHalfScore : null
             * joinCount : 0
             * aRed : 0
             * hOrder : 4
             * hScore :
             */
            public String home;
            public String leagueName;
            public int hYellow;
            public String aHalfScore;
            public int isFouce;
            public String hImageUrl;
            public String aImageUrl;
            public int hRed;
            public String openTime;
            public int isOver;
            public String type;
            public String aScore;
            public String aOrder;
            public String fouceDate;
            public int id;
            public String away;
            public int aYellow;
            public String hHalfScore;
            public int joinCount;
            public int aRed;
            public String hOrder;
            public String hScore;
        }
    }
}
