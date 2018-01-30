package com.dading.ssqs.bean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/17 10:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class JCScorebean {

    /**
     * status : true
     * data : [{"list":[{"items":[{"id":861,"payRate":"1.00","name":"0~1球"},{"id":862,"payRate":"2.00","name":"2~3球"},{"id":863,"payRate":"3.00","name":"4~6球"},{"id":864,"payRate":"4.00","name":"7球以上"}],"name":""}],"type":1},{"list":[{"items":[{"id":865,"payRate":"3.00","name":"1:0"},{"id":866,"payRate":"3.00","name":"2:0"},{"id":867,"payRate":"3.00","name":"2:1"},{"id":868,"payRate":"3.00","name":"3:0"},{"id":869,"payRate":"3.00","name":"3:1"},{"id":870,"payRate":"3.00","name":"3:2"},{"id":871,"payRate":"3.00","name":"4:0"},{"id":872,"payRate":"3.00","name":"4:1"},{"id":873,"payRate":"3.00","name":"4:2"},{"id":874,"payRate":"3.00","name":"4:3"}],"name":"主胜"},{"items":[{"id":875,"payRate":"3.00","name":"0:0"},{"id":876,"payRate":"3.00","name":"1:1"},{"id":877,"payRate":"3.00","name":"2:2"},{"id":878,"payRate":"3.00","name":"3:3"},{"id":879,"payRate":"3.00","name":"4:4"}],"name":"平"},{"items":[{"id":880,"payRate":"3.00","name":"0:1"},{"id":881,"payRate":"3.00","name":"0:2"},{"id":882,"payRate":"3.00","name":"1:2"},{"id":883,"payRate":"3.00","name":"0:3"},{"id":884,"payRate":"3.00","name":"1:3"},{"id":885,"payRate":"3.00","name":"2:3"},{"id":886,"payRate":"3.00","name":"0:4"},{"id":887,"payRate":"3.00","name":"1:4"},{"id":888,"payRate":"3.00","name":"2:4"},{"id":889,"payRate":"3.00","name":"3:4"}],"name":"主负"},{"items":[{"id":890,"payRate":"3.00","name":"其他比分"}],"name":"其他"}],"type":2},{"list":[{"items":[{"id":891,"payRate":"3.00","name":"胜-胜"},{"id":892,"payRate":"3.00","name":"胜-平"},{"id":893,"payRate":"3.00","name":"胜-负"},{"id":894,"payRate":"3.00","name":"平-胜"},{"id":895,"payRate":"3.00","name":"平-平"},{"id":896,"payRate":"3.00","name":"平-负"},{"id":897,"payRate":"3.00","name":"负-胜"},{"id":898,"payRate":"3.00","name":"负-平"},{"id":899,"payRate":"3.00","name":"负-负"}],"name":""}],"type":3},{"list":[{"items":[{"id":900,"payRate":"3.00","name":"单数"},{"id":901,"payRate":"3.00","name":"双数"}],"name":""}],"type":5},{"list":[{"items":[{"id":902,"payRate":"3.00","name":"单数"},{"id":903,"payRate":"3.00","name":"双数"}],"name":""}],"type":5}]
     * code : 0
     * msg :
     */
    /**
     * list : [{"items":[{"id":861,"payRate":"1.00","name":"0~1球"},{"id":862,"payRate":"2.00","name":"2~3球"},{"id":863,"payRate":"3.00","name":"4~6球"},{"id":864,"payRate":"4.00","name":"7球以上"}],"name":""}]
     * type : 1
     */
    public List<ListEntity> list;
    public int type;

    public static class ListEntity {
        /**
         * items : [{"id":861,"payRate":"1.00","name":"0~1球"},{"id":862,"payRate":"2.00","name":"2~3球"},{"id":863,"payRate":"3.00","name":"4~6球"},{"id":864,"payRate":"4.00","name":"7球以上"}]
         * name :
         */
        public List<ItemsEntity> items;
        public String name;

        public static class ItemsEntity {
            /**
             * id : 861
             * payRate : 1.00
             * name : 0~1球
             */
            public int id;
            public String payRate;
            public String name;
            public boolean checked;
            public boolean cbTag;
            public String amount;
            public int matchID;
            public String returnNum;
            public int wdr;
            public int type;
            public int payTypeID;
        }
    }
}
