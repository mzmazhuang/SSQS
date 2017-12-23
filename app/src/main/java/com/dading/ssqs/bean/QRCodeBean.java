package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * 创建者     zcl
 * 创建时间   2017/11/14$ 16:31$
 * 描述	      $${TODO}$$
 * <p>
 * 更新者     $$Author$$
 * 更新时间   $$Date$$
 * 更新描述   $${TODO}$$
 */
public class QRCodeBean implements Serializable {

    private static final long serialVersionUID = -6392864370438716532L;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
