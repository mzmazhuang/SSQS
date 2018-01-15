package com.dading.ssqs.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.PerferentialInfoActivity;
import com.dading.ssqs.adapter.newAdapter.PreferentialActivitiesAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.PerferentialBean;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;

import java.util.List;

/**
 * Created by mazhuang on 2018/1/15.
 * 优惠活动
 */

public class PreferentialActivitiesFragment extends Fragment implements OnRefreshListener {

    private static final String TAG = "PreferentialActivitiesFragment";

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private PreferentialActivitiesAdapter adapter;
    private boolean hasInit = false;
    private boolean isRefresh = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    private View initView() {
        LinearLayout container = new LinearLayout(getContext());
        container.setBackgroundColor(Color.WHITE);
        container.setOrientation(LinearLayout.VERTICAL);

        TitleCell titleCell = new TitleCell(getContext(), LocaleController.getString(R.string.perferential_activity));
        titleCell.hideBack();
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_rela_refresh_load, null);
        container.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);

        recyclerView = view.findViewById(R.id.swipe_target);

        adapter = new PreferentialActivitiesAdapter(getContext());
        adapter.setListener(new PreferentialActivitiesAdapter.OnPreferClickListener() {
            @Override
            public void OnClick(PerferentialBean bean) {
                Intent intent = new Intent(getContext(), PerferentialInfoActivity.class);
                if (bean != null) {
                    intent.putExtra(Constent.PERFERENTIAL_WEB, bean.getWebUrl());
                    intent.putExtra(Constent.PERFERENTIAL_TITLE, bean.getTitle());
                    intent.putExtra(Constent.PERFERENTIAL_CONTENT, bean.getContent());
                }
                getContext().startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        init();
        return container;
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true;
                swipeToLoadLayout.setRefreshing(true);
            }
        }
    }

    private void getNetDataWork() {
        SSQSApplication.apiClient(0).getActivityList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    List<PerferentialBean> items = (List<PerferentialBean>) result.getData();

                    if (items != null) {
                        adapter.setData(items);
                    }

                    isRefresh = false;
                } else {
                    Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            getNetDataWork();
        }
    }
}
