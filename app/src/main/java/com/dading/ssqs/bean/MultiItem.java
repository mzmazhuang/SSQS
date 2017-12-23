package com.dading.ssqs.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by lenovo on 2017/8/3.
 */
public class MultiItem extends MultiItemEntity {
    public static int PROXY_HEAD= 1;
    public static int PROXY_RECYCLE_VIEW= 2;
    ProxyCenterBean data;

    public MultiItem (int type, ProxyCenterBean data) {
        this.data = data;
    }

    public MultiItem ( ) {
    }

    public ProxyCenterBean getData ( ) {
        return data;
    }

    public void setData (ProxyCenterBean data) {
        this.data = data;
    }
}
