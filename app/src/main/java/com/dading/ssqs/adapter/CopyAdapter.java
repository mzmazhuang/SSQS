package com.dading.ssqs.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;

import java.util.List;

/**
 * Created by lenovo on 2017/8/22.
 */
public class CopyAdapter extends BaseQuickAdapter<String> {
    public CopyAdapter (int layoutResId, List data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert (BaseViewHolder baseViewHolder, String o) {
        baseViewHolder.setText(R.id.recycleview_copy,o);
    }
}
