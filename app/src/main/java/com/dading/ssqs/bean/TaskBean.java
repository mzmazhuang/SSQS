package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/21 15:41
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TaskBean implements Serializable {
    private static final long serialVersionUID = -883416833184104411L;

    /**
     * status : true
     * data : {"banlance":60,"taskCount":7,"tasks":[{"id":1,"banlance":1000,"status":0,"timesType":1,"name":"新手体验"},{"id":2,"banlance":398,"status":0,"timesType":0,"name":"7天签到"},{"id":p3,"banlance":20,"status":0,"timesType":0,"name":"发布帖子"},{"id":4,"banlance":500,"status":0,"timesType":1,"name":"验证手机"},{"id":5,"banlance":50,"status":1,"timesType":1,"name":"关注圈子"},{"id":6,"banlance":500,"status":0,"timesType":1,"name":"购买服务"},{"id":7,"banlance":1000,"status":0,"timesType":1,"name":"邀请码领球币"}],"finishCount":2}
     * code : 0
     * msg :
     */

    /**
     * banlance : 60
     * taskCount : 7
     * tasks : [{"id":1,"banlance":1000,"status":0,"timesType":1,"name":"新手体验"},{"id":2,"banlance":398,"status":0,"timesType":0,"name":"7天签到"},{"id":p3,"banlance":20,"status":0,"timesType":0,"name":"发布帖子"},{"id":4,"banlance":500,"status":0,"timesType":1,"name":"验证手机"},{"id":5,"banlance":50,"status":1,"timesType":1,"name":"关注圈子"},{"id":6,"banlance":500,"status":0,"timesType":1,"name":"购买服务"},{"id":7,"banlance":1000,"status":0,"timesType":1,"name":"邀请码领球币"}]
     * finishCount : 2
     */
    public int banlance;
    public int taskCount;
    public List<TasksEntity> tasks;
    public int finishCount;

    public static class TasksEntity implements Serializable {
        /**
         * id : 1
         * banlance : 1000
         * status : 0
         * timesType : 1
         * name : 新手体验
         */
        public int id;
        public int banlance;
        public int status;
        public int timesType;
        public String name;
    }
}
