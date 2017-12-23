package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/8/10.
 */
public class ProxyCmmsionsBean implements Serializable {

    private static final long serialVersionUID = -8217779430624497286L;
    /**
     * fee : 3780041.50
     * amount : 37800415
     * users : [{"id":"20160704116012","userName":"球技过人GG","amount":"37800415","fee":"3780041.50"}]
     */

    private String fee;
    private String amount;
    private List<UsersBean> users;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean implements Serializable {

        private static final long serialVersionUID = -5361725652789249294L;
        /**
         * id : 20160704116012
         * userName : 球技过人GG
         * amount : 37800415
         * fee : 3780041.50
         */

        private String id;
        private String userName;
        private String amount;
        private String fee;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }
}
