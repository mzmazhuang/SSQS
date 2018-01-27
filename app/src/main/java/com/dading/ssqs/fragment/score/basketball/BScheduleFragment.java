package com.dading.ssqs.fragment.score.basketball;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.dading.ssqs.adapter.newAdapter.ResultSelectTimeDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScoreFImmediateAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.cells.ResultTimeLayout;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.ResultSelectTimeDialog;
import com.dading.ssqs.components.swipetoloadlayout.OnLoadMoreListener;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.fragment.guesstheball.ScoreDataController;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/26.
 * 篮球赛程
 */

public class BScheduleFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener, NotificationController.NotificationControllerDelegate {

    public static final String TAG = "BScheduleFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ScoreFImmediateAdapter adapter;
    private LoadingDialog loadingDialog;
    private ResultTimeLayout resultTimeLayout;
    private ResultSelectTimeDialog selectTimeDialog;
    private ImageView defaultView;

    private int offset = 1;
    private int limit = 10;

    private String leagusId = "0";

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    private boolean hasInit = false;
    private String currTime;

    private boolean beginInit = false;

    private List<ResultSelectTimeDialog.ResultSelectTimeBean> time = new ArrayList<>();

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

    private List<ResultSelectTimeDialog.ResultSelectTimeBean> getTime() {
        if (time.size() > 0) {
            return time;
        } else {
            Calendar calendar = Calendar.getInstance();

            for (int i = 1; i <= 7; i++) {
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, i);

                Date resultDate = calendar.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

                String week = "";

                switch (weekDay) {
                    case Calendar.MONDAY:
                        week = "星期一";
                        break;
                    case Calendar.TUESDAY:
                        week = "星期二";
                        break;
                    case Calendar.WEDNESDAY:
                        week = "星期三";
                        break;
                    case Calendar.THURSDAY:
                        week = "星期四";
                        break;
                    case Calendar.FRIDAY:
                        week = "星期五";
                        break;
                    case Calendar.SATURDAY:
                        week = "星期六";
                        break;
                    case Calendar.SUNDAY:
                        week = "星期日";
                        break;
                }

                time.add(new ResultSelectTimeDialog.ResultSelectTimeBean("(" + week + ")　" + sdf.format(resultDate), i));
            }
            return time;
        }
    }

    public View initView() {
        RelativeLayout container = new RelativeLayout(mContext);

        LinearLayout parentLayout = new LinearLayout(mContext);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(parentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        defaultView = new ImageView(mContext);
        defaultView.setImageResource(R.mipmap.no_data);
        defaultView.setVisibility(View.GONE);
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        resultTimeLayout = new ResultTimeLayout(mContext);
        resultTimeLayout.setDay(7);
        resultTimeLayout.changeImageStyle(7);
        resultTimeLayout.setTime(DateUtils.getCurTimeAddND(1, "yyyy-MM-dd"));
        resultTimeLayout.setListener(new ResultTimeLayout.TimeListener() {
            @Override
            public void onChange(int day) {
                setSelectTime(day);
            }

            @Override
            public void onSelectTime() {
                if (selectTimeDialog == null) {
                    selectTimeDialog = new ResultSelectTimeDialog(mContext);
                    selectTimeDialog.setItemListener(new ResultSelectTimeDialogAdapter.OnClickListener() {
                        @Override
                        public void onClick(int day) {
                            resultTimeLayout.setDay(day);

                            selectTimeDialog.dismiss();

                            resultTimeLayout.changeImageStyle(day);

                            setSelectTime(day);
                        }
                    });
                }
                selectTimeDialog.show(getTime(), "选择时间", resultTimeLayout.getDay());
            }
        });
        parentLayout.addView(resultTimeLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28, 0, 3, 0, 5));

        View lineView = new View(mContext);
        lineView.setBackgroundColor(0xFFEDEDED);
        parentLayout.addView(lineView, new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1));

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_rela_refresh_load, null);
        parentLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);

        recyclerView = view.findViewById(R.id.swipe_target);

        adapter = new ScoreFImmediateAdapter(mContext);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);

        init();


        return container;
    }

    private void setSelectTime(int day) {
        currTime = DateUtils.getCurTimeAddND((8 - day), "yyyyMMddHH:mm:ss");

        resultTimeLayout.setTime(DateUtils.getCurTimeAddND((8 - day), "yyyy-MM-dd"));

        swipeToLoadLayout.setRefreshing(true);

        NotificationController.getInstance().postNotification(NotificationController.scoreBasketChildPage, "3", currTime);

        ScoreDataController.Companion.getInstance().clearBasketBallSchedule();
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

    //检查是否登录 应该封装到父类中
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
        if (TextUtils.isEmpty(currTime)) {
            currTime = DateUtils.getCurTimeAddND(1, "yyyyMMddHH:mm:ss");
        }

        SSQSApplication.apiClient(0).getMatchBallOrTypeList(false, 4, currTime, "0", 0, leagusId, off, lim, new CcApiClient.OnCcListener() {
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
                leagusId = args[1];

                loadingDialog.show();

                offset = 1;

                getNetDataWork(leagusId, offset, limit, true);
            }
        }
    }
}
