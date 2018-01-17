package com.dading.ssqs.components;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.RankingAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingView extends LinearLayout {

    private AutoRecyclerView autoRecyclerView;
    private RankingAdapter adapter;

    private boolean isStart = false;

    public RankingView(Context context, AutoRecyclerView.OnAutoRecyclerClickListener listener) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        TextView titleView = new TextView(context);
        titleView.setPadding(AndroidUtilities.INSTANCE.dp(15), 0, 0, 0);
        titleView.setText(LocaleController.getString(R.string.winning_list));
        titleView.setTextSize(14);
        titleView.setTextColor(0xFF323232);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ranking, 0, 0, 0);
        titleView.setCompoundDrawablePadding(AndroidUtilities.INSTANCE.dp(5));
        addView(titleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFEFEFF4);
        addView(lineView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));

        autoRecyclerView = new AutoRecyclerView(context);
        autoRecyclerView.setHasFixedSize(true);
        autoRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        autoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoRecyclerView.setListener(listener);
        addView(autoRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        adapter = new RankingAdapter(context);
        autoRecyclerView.setAdapter(adapter);
    }

    public void setData(List<HomeBean.OrdersBeanX.OrdersBean> list) {
        adapter.setList(list);

        autoRecyclerView.start();

        isStart = true;
    }

    public void resume() {
        if (isStart && autoRecyclerView != null) {
            autoRecyclerView.start();
        }
    }

    public void pause() {
        if (isStart && autoRecyclerView != null) {
            autoRecyclerView.stop();
        }
    }
}
