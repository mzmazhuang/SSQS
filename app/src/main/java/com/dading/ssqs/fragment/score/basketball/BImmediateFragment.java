package com.dading.ssqs.fragment.score.basketball;

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
import android.widget.RelativeLayout;

import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.adapter.newAdapter.ScoreBImmediateAdapter;
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
import java.util.List;

/**
 * Created by mazhuang on 2018/1/26.
 * 篮球即时
 */

public class BImmediateFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener, NotificationController.NotificationControllerDelegate {

    public static final String TAG = "BImmediateFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ImageView defaultView;
    private ScoreBImmediateAdapter adapter;
    private LoadingDialog loadingDialog;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    private String leagusId;

    private int offset = 1;
    private int limit = 10;

    private boolean hasInit = false;
    private boolean beginInit = false;

    public void setBeginInit(boolean beginInit) {
        this.beginInit = beginInit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.scoreChildRefresh);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreChildRefresh);
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

        adapter = new ScoreBImmediateAdapter(mContext);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);

        init();

        return container;
    }

    private ScoreBImmediateAdapter.OnScoreBasketBallListener listener = new ScoreBImmediateAdapter.OnScoreBasketBallListener() {
        @Override
        public void onFavorite(int isFavor, String matchId) {
            if (checkIsLogin()) {
                favorOperation(isFavor, matchId);
            }
        }

        @Override
        public void onItemClick(int id) {
            if (checkIsLogin()) {
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);

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

        SSQSApplication.apiClient(0).fouceMatchBasketBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();

                if (result.isOk()) {
                    List<ScoreBean> list = new ArrayList<>();
                    list.addAll(adapter.getData());

                    for (int i = 0; i < list.size(); i++) {
                        if (id.equals(list.get(i).id + "")) {
                            list.get(i).isFouce = isFavor == 1 ? 0 : 1;
                            break;
                        }
                    }

                    adapter.setList(list);

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

    private void getNetDataWork(String leagusId, int off, int lim, final boolean isRefre) {
        String date = DateUtils.getCurTime("yyyyMMddHH:mm:ss");

        SSQSApplication.apiClient(0).getMatchBallOrTypeList(false, 2, date, "0", 0, leagusId, off, lim, new CcApiClient.OnCcListener() {
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
            leagusId = "0";

            getNetDataWork(leagusId, offset, limit, true);
        }
    }

    @Override
    public void onLoadMore() {
        if (!isLoadMore) {
            isLoadMore = true;

            offset++;

            getNetDataWork(leagusId, offset, limit, false);
        }
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.scoreChildRefresh) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    leagusId = args[1];

                    loadingDialog.show();

                    offset = 1;

                    getNetDataWork(leagusId, offset, limit, true);
                }
            }
        }
    }
}
