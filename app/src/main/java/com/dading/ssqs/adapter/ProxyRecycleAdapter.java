package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;

import java.util.List;

/**
 * Created by lenovo on 2017/8/3.
 */
public class ProxyRecycleAdapter extends BaseQuickAdapter<String> {

    private final Context context;

    public ProxyRecycleAdapter (Context context, int layoutResId, List data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder, String s) {
        ImageView icon = baseViewHolder.getView(R.id.my_item_icon);
        icon.setVisibility(View.GONE);
        baseViewHolder.setText(R.id.my_item_title, s);
    }
}
