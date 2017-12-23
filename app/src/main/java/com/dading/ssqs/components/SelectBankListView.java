package com.dading.ssqs.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.SelectBankListAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.BankBean;

import java.util.List;

/**
 * Created by mazhuang on 2017/11/25.
 * 选择银行列表
 */

public class SelectBankListView extends LinearLayout {

    private SelectBankListAdapter adapter;

    public SelectBankListView(Context context, SelectBankListAdapter.OnBankItemClickListener listener) {
        super(context);

        setBackgroundColor(Color.TRANSPARENT);

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new SelectBankListAdapter(context);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
    }

    public void setList(List<BankBean> list) {
        adapter.setData(list);
    }

}
