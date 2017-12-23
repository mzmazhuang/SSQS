package com.dading.ssqs.cells;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.SelectMatchChildAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GusessChoiceBean;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/13.
 */

public class SelectMatchCell extends LinearLayout {

    private TextView indexTextView;
    private SelectMatchChildAdapter adapter;

    public SelectMatchCell(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setBackgroundColor(0xFFC2EDFF);
        topLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        indexTextView = new TextView(context);
        indexTextView.setTextColor(0xFF37C4FF);
        indexTextView.setTextSize(14);
        indexTextView.setGravity(Gravity.CENTER);
        indexTextView.setBackgroundResource(R.drawable.bg_select_match_circle);
        topLayout.addView(indexTextView, LayoutHelper.createLinear(20, 20, 12, 0, 0, 0));

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 10, 0));

        adapter = new SelectMatchChildAdapter(context);
        recyclerView.setAdapter(adapter);
    }

    public void setIndex(String index) {
        indexTextView.setText(index);
    }

    public void setData(List<GusessChoiceBean.FilterEntity> list) {
        adapter.setList(list);
    }
}
