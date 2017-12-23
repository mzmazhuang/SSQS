package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/22 13:52
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SevenPopBean implements Serializable {

    /**
     * status : true
     * data : {"banlanceCount":10,"tasks":[{"banlance":10,"status":1,"date":1},{"banlance":20,"status":0,"date":2},{"banlance":30,"status":0,"date":p3},{"banlance":40,"status":0,"date":4},{"banlance":50,"status":0,"date":5},{"banlance":60,"status":0,"date":6},{"banlance":118,"status":0,"date":7}],"dayCount":1}
     * code : 0
     * msg :
     */

    /**
     * banlanceCount : 10
     * tasks : [{"banlance":10,"status":1,"date":1},{"banlance":20,"status":0,"date":2},{"banlance":30,"status":0,"date":p3},{"banlance":40,"status":0,"date":4},{"banlance":50,"status":0,"date":5},{"banlance":60,"status":0,"date":6},{"banlance":118,"status":0,"date":7}]
     * dayCount : 1
     */
    public int banlanceCount;
    public List<TasksEntity> tasks;
    public int dayCount;

    public static class TasksEntity implements Serializable {
        /**
         * banlance : 10
         * status : 1
         * date : 1
         */
        public int banlance;
        public int status;
        public int date;
    }
}
