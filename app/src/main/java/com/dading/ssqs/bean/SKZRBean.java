package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/27 9:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SKZRBean implements Serializable {
    private static final long serialVersionUID = -6531787306929830837L;

    /**
     * status : true
     * data : {"matchID":6671,"home":"切沃","away":"AC米兰","aSubPlayers":[{"id":667,"num":35,"name":"Alessandro Plizzari"}],"hPlayers":[{"id":16037,"num":70,"name":"索伦蒂诺"}],"hSubPlayers":[{"id":16070,"num":90,"name":"安德雷亚\u2022塞库林"}],"aPlayers":[{"id":657,"num":99,"name":"布冯"}]}
     * code : 0
     * msg :
     */

    /**
     * matchID : 6671
     * home : 切沃
     * away : AC米兰
     * aSubPlayers : [{"id":667,"num":35,"name":"Alessandro Plizzari"}]
     * hPlayers : [{"id":16037,"num":70,"name":"索伦蒂诺"}]
     * hSubPlayers : [{"id":16070,"num":90,"name":"安德雷亚\u2022塞库林"}]
     * aPlayers : [{"id":657,"num":99,"name":"布冯"}]
     */
    public int matchID;
    public String home;
    public String away;
    public List<ASubPlayersEntity> aSubPlayers;
    public List<HPlayersEntity> hPlayers;
    public List<HSubPlayersEntity> hSubPlayers;
    public List<APlayersEntity> aPlayers;

    public static class ASubPlayersEntity implements Serializable {
        /**
         * id : 667
         * num : 35
         * name : Alessandro Plizzari
         */
        public int id;
        public int num;
        public String name;
    }

    public static class HPlayersEntity implements Serializable {
        /**
         * id : 16037
         * num : 70
         * name : 索伦蒂诺
         */
        public int id;
        public int num;
        public String name;
    }

    public static class HSubPlayersEntity implements Serializable {
        /**
         * id : 16070
         * num : 90
         * name : 安德雷亚•塞库林
         */
        public int id;
        public int num;
        public String name;
    }

    public static class APlayersEntity implements Serializable {
        /**
         * id : 657
         * num : 99
         * name : 布冯
         */
        public int id;
        public int num;
        public String name;
    }
}
