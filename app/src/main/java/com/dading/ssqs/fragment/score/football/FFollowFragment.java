package com.dading.ssqs.fragment.score.football;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.adapter.newAdapter.ScoreFImmediateAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.swipetoloadlayout.OnLoadMoreListener;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/26.
 * 足球关注
 */

public class FFollowFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    public static final String TAG = "FFollowFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ScoreFImmediateAdapter adapter;
    private LoadingDialog loadingDialog;
    private ImageView defaultView;

    private int offset = 1;
    private int limit = 10;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    private boolean hasInit = false;
    private boolean beginInit = false;

    public void setBeginInit(boolean beginInit) {
        this.beginInit = beginInit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        return initView();
    }

    private View initView() {
        RelativeLayout container = new RelativeLayout(mContext);

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_rela_refresh_load, null);
        container.addView(view, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        defaultView = new ImageView(mContext);
        defaultView.setImageResource(R.mipmap.no_data);
        defaultView.setVisibility(View.GONE);
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);

        recyclerView = view.findViewById(R.id.swipe_target);

        adapter = new ScoreFImmediateAdapter(mContext);
        adapter.setMonth(true);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);

        init();

        return container;
    }

    private ScoreFImmediateAdapter.OnScoreFootBallListener listener = new ScoreFImmediateAdapter.OnScoreFootBallListener() {

        @Override
        public void onFavorite(int isFavor, String matchId) {
            if (checkIsLogin()) {
                favorOperation(isFavor, matchId);
            }
        }

        @Override
        public void onItemClick(int id) {
            if (checkIsLogin()) {
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);

                Intent intent = new Intent(mContext, MatchInfoActivity.class);
                intent.putExtra(Constent.MATCH_ID, id);
                intent.putExtra(Constent.INTENT_FROM, "JS");
                startActivity(intent);
            }
        }
    };

    private void favorOperation(final int isFavor, final String id) {
        loadingDialog.show();

        FouceMatchBallElement element = new FouceMatchBallElement();
        element.setMatchID(id);
        element.setStatus(isFavor == 1 ? "0" : "1");

        SSQSApplication.apiClient(0).fouceMatchBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();

                if (result.isOk()) {
                    List<ScoreBean> list = new ArrayList<>();
                    list.addAll(adapter.getData());

                    Iterator<ScoreBean> scoreIter = list.iterator();
                    while (scoreIter.hasNext()) {
                        ScoreBean bean = scoreIter.next();
                        if (id.equals(bean.id + "")) {
                            scoreIter.remove();
                            break;
                        }
                    }

                    adapter.setList(list);

                    if (list.size() == 0) {
                        defaultView.setVisibility(View.VISIBLE);
                    } else {
                        defaultView.setVisibility(View.GONE);
                    }

                } else {
                    ToastUtils.midToast(mContext, result.getMessage(), 0);
                }
            }
        });

    }

    private boolean checkIsLogin() {
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            return true;
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);

            return false;
        }
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadingDialog = new LoadingDialog(mContext);

        if (beginInit && hasInit) {
            swipeToLoadLayout.setRefreshing(true);
        } else if (beginInit) {
            hasInit();
        }
    }

    public void hasInit() {
        if (!hasInit) {
            hasInit = true;

            if (swipeToLoadLayout != null) {
                swipeToLoadLayout.setRefreshing(true);
            }
        }
    }

    private void getNetDataWork(int off, int lim, final boolean isRefre) {
        SSQSApplication.apiClient(0).getMatchBallOrTypeList(true, 5, "2016082200:00:00", "0", 0, "0", off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                loadingDialog.dismiss();

                if (result.isOk()) {
                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                    if (page != null) {
                        int totalCount = page.getTotalCount();

                        if (page.getItems() != null && page.getItems().size() >= 1) {
                            defaultView.setVisibility(View.GONE);

                            if (isRefre) {
                                adapter.setList(page.getItems());
                            } else {
                                adapter.addList(page.getItems());
                            }
                        } else {
                            adapter.clearData();

                            defaultView.setVisibility(View.VISIBLE);
                        }

                        swipeToLoadLayout.setLoadMoreEnabled(offset < totalCount);
                    }

                    isRefresh = false;
                    isLoadMore = false;
                } else {
                    ToastUtils.midToast(mContext, result.getMessage(), 0);
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

            offset++;

            getNetDataWork(offset, limit, false);
        }
    }
}
