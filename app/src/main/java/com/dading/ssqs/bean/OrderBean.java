package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/1 12:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 2591312803965786557L;
    /**
     * status : true
     * data : app_id=2016091301896183&biz_content=%7B%22subject%22%3A%221%252C200%22%2C%22out_trade_no%22%3A%2220161201119915%22%2C%22total_amount%22%3A%2212.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.ddzlink.com%2Frest%2Fv1.0%2Falipay%2Frtn&sign=jgiL%2BHZVQZLAsJLjhrbLaH0rpLAge8AbBojpt%2FCmxJsmBXMgD0naX7qyPkIucbSEYZ5gkTgSHbzhfTQW4zh1isg1C%2FbUbsyWIMiQrzdG11drQL%2BTT%2BGBVEQbIObBzoWZ5gILPVll4hIHX3BHRjmSapKQItSUrMvFZPTF424TK1s%3D&sign_type=RSA&timestamp=2016-12-01+10%3A01%3A03&version=1.0
     * code : 0
     * msg :
     */
    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
