package com.dading.ssqs.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.dading.ssqs.adapter.newAdapter.SelectRechargeMoneyAdapter;
import com.dading.ssqs.base.LayoutHelper;

import java.util.List;

/**
 * Created by mazhuang on 2017/11/24.
 * 支付
 */

public class SelectRechargeMoneyView extends LinearLayout {

    private SelectRechargeMoneyAdapter adapter;

    public SelectRechargeMoneyView(Context context) {
        super(context);

        RecyclerView recyclerView = new RecyclerView(context);
        addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SelectRechargeMoneyAdapter(context);
        recyclerView.setAdapter(adapter);
    }

    public void setList(List<Integer> list) {
        adapter.setList(list);
    }

    public void setListener(SelectRechargeMoneyAdapter.OnTextClickListener listener) {
        adapter.setListener(listener);
    }
}
