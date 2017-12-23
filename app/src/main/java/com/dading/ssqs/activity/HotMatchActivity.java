package com.dading.ssqs.activity;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.dading.ssqs.adapter.newAdapter.SelectMatchAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GusessChoiceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/13.
 */

public class HotMatchActivity extends LinearLayout {

    private Context mContext;
    private RecyclerView recyclerView;

    private SelectMatchAdapter adapter;

    public HotMatchActivity(Context context) {
        super(context);
        mContext = context;

        recyclerView = new RecyclerView(mContext);
        addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new SelectMatchAdapter(mContext);
        recyclerView.setAdapter(adapter);
    }

    public void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setData(List<GusessChoiceBean> list) {
        adapter.setList(list);
    }

    public List<GusessChoiceBean> getData() {
        return adapter.getData();
    }

    public void isAll(boolean isAll) {
        List<GusessChoiceBean> list = new ArrayList<>();
        list.addAll(adapter.getData());
        for (int i = 0; i < list.size(); i++) {
            List<GusessChoiceBean.FilterEntity> entities = list.get(i).filter;
            for (int j = 0; j < entities.size(); j++) {
                if (isAll) {
                    entities.get(j).checked = true;
                } else {
                    entities.get(j).checked = false;
                }
            }
        }

        adapter.setList(list);
    }
}
