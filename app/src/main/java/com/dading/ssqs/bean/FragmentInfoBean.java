package com.dading.ssqs.bean;

import android.support.v4.app.Fragment;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/16 17:37
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FragmentInfoBean {
    private String          title;
    private Class fragment;

    /*注意不要加泛型*/
    public FragmentInfoBean(String title, Class fragment) {
        this.fragment = fragment;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<Fragment> fragment) {
        this.fragment = fragment;
    }
}
