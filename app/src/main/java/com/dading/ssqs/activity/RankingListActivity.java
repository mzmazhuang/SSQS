package com.dading.ssqs.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.RankingListAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.dading.ssqs.components.swipetoloadlayout.OnLoadMoreListener;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "RankingListActivity";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private RankingListAdapter adapter;
    private LoadingDialog loadingDialog;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    private int offset = 1;
    private int limit = 30;

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        mContext = this;

        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE);

        TitleCell titleCell = new TitleCell(this, getResources().getString(R.string.new_ranking_people));
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_rela_refresh_load, null);
        container.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);

        adapter = new RankingListAdapter(mContext);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);

        mRecyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);

        headerAndFooterRecyclerViewAdapter.addHeaderView(getHeadView());

        init();
        return container;
    }

    private View getHeadView() {
        LinearLayout headLayout = new LinearLayout(mContext);
        headLayout.setBackgroundColor(0xFFFAFAFA);
        headLayout.setOrientation(LinearLayout.HORIZONTAL);
        headLayout.setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        TextView titleTextView = new TextView(mContext);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(0xFF323232);
        titleTextView.setText(LocaleController.getString(R.string.username));
        headLayout.addView(titleTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f, 15, 0, 0, 0));

        TextView moneyTextView = new TextView(mContext);
        moneyTextView.setGravity(Gravity.CENTER);
        moneyTextView.setTextSize(14);
        moneyTextView.setTextColor(0xFF323232);
        moneyTextView.setText(LocaleController.getString(R.string.winning_money));
        headLayout.addView(moneyTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        TextView typeTextView = new TextView(mContext);
        typeTextView.setGravity(Gravity.CENTER);
        typeTextView.setTextSize(14);
        typeTextView.setTextColor(0xFF323232);
        typeTextView.setText(LocaleController.getString(R.string.ranking_match_type));
        headLayout.addView(typeTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f, 0, 0, 15, 0));

        return headLayout;
    }

    private void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadingDialog = new LoadingDialog(mContext);

        loadingDialog.show();
        getNetDataWork(offset, limit, true);
    }

    private void getNetDataWork(final int off, int lim, final boolean isRefre) {
        SSQSApplication.apiClient(classGuid).getRankList(off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setLoadingMore(false);
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    CcApiResult.ResultRankingListPage page = (CcApiResult.ResultRankingListPage) result.getData();

                    if (page != null && page.getItems() != null && page.getItems().size() >= 1) {
                        if (isRefre) {
                            adapter.setList(page.getItems());
                        } else {
                            adapter.addList(page.getItems());
                        }
                        boolean isLoad = off < page.getTotalCount();
                        swipeToLoadLayout.setLoadMoreEnabled(isLoad);
                    }

                    isRefresh = false;
                    isLoadMore = false;
                } else {
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;

            offset = 1;

            getNetDataWork(offset, limit, true);
        }
    }

    @Override
    public void onLoadMore() {
        if (!isLoadMore) {
            isLoadMore = true;

            getNetDataWork(offset, limit, false);
        }
    }
}
