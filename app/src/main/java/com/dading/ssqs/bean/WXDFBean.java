package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/23 14:52
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WXDFBean implements Serializable {

    /**
     * status : true
     * code : 0
     * msg :
     * data : [{"id":1,"name":"微信支付","remark":"1，点\u201c立即充值\u201d将自动为您截屏并保存到相册，同时打开微信。\r\n2，请在微信中打开\u201c扫一扫\u201d。\r\n3，在扫一扫中点击右上角，选择\u201c从相册选取二维码\u201d选取截屏的图片。\r\n4，输入您欲充值的金额并进行转账。如充值未及时到账，请联系在线客服。","moneys":[10,100,300,800],"state":0,"info":[{"id":5,"name":"微信支付平台","logo":"http://192.168.0.115:8080/images/charge/20170606020636MTnzipuUYJ-650x613.jpg","owner":"郑贤","cardNumber":"13926978352","bankAddress":"http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png","title":"","hint":"","mfrom":1,"mto":100,"addrType":2}]},{"id":2,"name":"支付宝支付","remark":"1，点\u201c立即充值\u201d将自动为您截屏并保存到相册，同时打开微信。\r\n2，请在微信中打开\u201c扫一扫\u201d。\r\n3，在扫一扫中点击右上角，选择\u201c从相册选取二维码\u201d选取截屏的图片。\r\n4，输入您欲充值的金额并进行转账。如充值未及时到账，请联系在线客服。","moneys":[10,100,300,800],"state":0,"info":[{"id":6,"name":"支付宝支付平台","logo":"http://192.168.0.115:8080/images/charge/20170601051027HQnkePjJLR-909x908.jpg","owner":"郑贤","cardNumber":"1581536@163.com","bankAddress":"http://192.168.0.115:8080/images/charge/20170606033140iyZHzyFEod-223x220.png","title":"","hint":"","mfrom":1,"mto":100,"addrType":2}]},{"id":3,"name":"银行转账","remark":"需要客户填写存入时间，存入金额，存款人姓名，还有选择转账的渠道，包括网银转账，ATM自动柜员机，ATM现金入款，银行柜台转账，手机银行转账，其他的方式","moneys":[10,100,300,800],"state":0,"info":[{"id":7,"name":"中国银行","logo":"http://192.168.0.115:8080/images/charge/20170601051032aHVfFPiFCZ-732x557.jpg","owner":"郑贤","cardNumber":"541254284856253","bankAddress":"中国银行深圳南山分行","title":"","hint":"","mfrom":1,"mto":100,"addrType":2}]}]
     */

    /**
     * id : 1
     * name : 微信支付
     * remark : 1，点“立即充值”将自动为您截屏并保存到相册，同时打开微信。
     * 2，请在微信中打开“扫一扫”。
     * 3，在扫一扫中点击右上角，选择“从相册选取二维码”选取截屏的图片。
     * 4，输入您欲充值的金额并进行转账。如充值未及时到账，请联系在线客服。
     * moneys : [10,100,300,800]
     * state : 0
     * info : [{"id":5,"name":"微信支付平台","logo":"http://192.168.0.115:8080/images/charge/20170606020636MTnzipuUYJ-650x613.jpg","owner":"郑贤","cardNumber":"13926978352","bankAddress":"http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png","title":"","hint":"","mfrom":1,"mto":100,"addrType":2}]
     */

    private int id;
    private String name;
    private String remark;
    private int state;
    private List<Integer> moneys;
    private List<InfoBean> info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Integer> getMoneys() {
        return moneys;
    }

    public void setMoneys(List<Integer> moneys) {
        this.moneys = moneys;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable {
        @Override
        public String toString() {
            return "InfoBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", logo='" + logo + '\'' +
                    ", owner='" + owner + '\'' +
                    ", cardNumber='" + cardNumber + '\'' +
                    ", bankAddress='" + bankAddress + '\'' +
                    ", title='" + title + '\'' +
                    ", hint='" + hint + '\'' +
                    ", mfrom=" + mfrom +
                    ", mto=" + mto +
                    ", addrType=" + addrType +
                    ", remark='" + remark + '\'' +
                    ", money=" + money +
                    ", isChecked=" + isChecked +
                    '}';
        }

        /**
         * id : 5
         * name : 微信支付平台
         * logo : http://192.168.0.115:8080/images/charge/20170606020636MTnzipuUYJ-650x613.jpg
         * owner : 郑贤
         * cardNumber : 13926978352
         * bankAddress : http://www.ddzlink.com/images/charge/20170606020622LQNBpxGDSh-286x280.png
         * title :
         * hint :
         * mfrom : 1
         * mto : 100
         * addrType : 2
         * payType
         */

        private int id;
        private String name;
        private String logo;
        private String owner;
        private String cardNumber;
        private String bankAddress;
        private String title;
        private String hint;
        private int mfrom;
        private int mto;
        private int addrType;
        private String remark;
        private int isThird;
        private int money;
        private int payType;
        private boolean isChecked;


        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getIsThird() {
            return isThird;
        }

        public void setIsThird(int isThird) {
            this.isThird = isThird;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getBankAddress() {
            return bankAddress;
        }

        public void setBankAddress(String bankAddress) {
            this.bankAddress = bankAddress;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public int getMfrom() {
            return mfrom;
        }

        public void setMfrom(int mfrom) {
            this.mfrom = mfrom;
        }

        public int getMto() {
            return mto;
        }

        public void setMto(int mto) {
            this.mto = mto;
        }

        public int getAddrType() {
            return addrType;
        }

        public void setAddrType(int addrType) {
            this.addrType = addrType;
        }
    }
}
