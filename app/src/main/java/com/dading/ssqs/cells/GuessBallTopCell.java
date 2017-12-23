package com.dading.ssqs.cells;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.dading.ssqs.adapter.newAdapter.GuessBallTopAdapter;
import com.dading.ssqs.adapter.newAdapter.GuessBallTopSubAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GuessTopTitle;

import java.util.List;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class GuessBallTopCell extends LinearLayout {

    private Context mContext;

    private RecyclerView topTitleRecyclerView;
    private RecyclerView topSubTitleRecyclerView;

    private GuessBallTopAdapter topAdapter;
    private GuessBallTopSubAdapter topSubAdapter;

    public GuessBallTopCell(Context context) {
        super(context);
        mContext = context;

        setOrientation(LinearLayout.VERTICAL);

        LinearLayout topTitleLayout = new LinearLayout(mContext);
        topTitleLayout.setBackgroundColor(0xFF007DB0);
        addView(topTitleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        topTitleRecyclerView = new RecyclerView(mContext);
        topTitleLayout.addView(topTitleRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        topAdapter = new GuessBallTopAdapter(mContext);
        topTitleRecyclerView.setAdapter(topAdapter);

        LinearLayout topSubTitleLayout = new LinearLayout(mContext);
        topSubTitleLayout.setBackgroundColor(0xFF00638C);
        addView(topSubTitleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        topSubTitleRecyclerView = new RecyclerView(mContext);
        topSubTitleLayout.addView(topSubTitleRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        topSubAdapter = new GuessBallTopSubAdapter(mContext);
        topSubTitleRecyclerView.setAdapter(topSubAdapter);

        init();
    }

    //一级标题点击事件
    public void setTopListener(GuessBallTopAdapter.OnGuessTopClickListener listener) {
        topAdapter.setListener(listener);
    }

    //二级标题点击事件
    public void setSubTopListener(GuessBallTopSubAdapter.OnGuessSubTitleClickListener listener) {
        topSubAdapter.setListener(listener);
    }

    //一级标题数据
    public void setTopTitleData(List<GuessTopTitle> data) {
        topAdapter.setData(data);
    }

    //二级标题数据
    public void setTopSubTitleData(List<GuessTopTitle> data) {
        topSubAdapter.setData(data);
    }

    //一级标题选中
    public void setTopTitleSelect(int sel) {
        topAdapter.setSelect(sel);
    }

    //二级标题选中
    public void setTopSubTitleSelect(int sel) {
        topSubAdapter.setSelect(sel);
    }

    private void init() {
        topTitleRecyclerView.setHasFixedSize(true);
        LinearLayoutManager topTitleLinearLayoutManager = new LinearLayoutManager(mContext);
        topTitleLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topTitleRecyclerView.setLayoutManager(topTitleLinearLayoutManager);
        topTitleRecyclerView.setItemAnimator(new DefaultItemAnimator());

        topSubTitleRecyclerView.setHasFixedSize(true);
        LinearLayoutManager topSubTitlelinearLayoutManager = new LinearLayoutManager(mContext);
        topSubTitlelinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topSubTitleRecyclerView.setLayoutManager(topSubTitlelinearLayoutManager);
        topSubTitleRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
