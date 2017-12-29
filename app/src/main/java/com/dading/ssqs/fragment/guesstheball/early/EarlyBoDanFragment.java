package com.dading.ssqs.fragment.guesstheball.early;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallBoDanAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.EarlyBean;
import com.dading.ssqs.bean.JCScorebean;
import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.cells.BoDanChildCell;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.cells.TimeCell;
import com.dading.ssqs.components.FilterDialog;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.PageDialog;
import com.dading.ssqs.components.RecyclerScrollview;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.components.ScrollBallCommitView;
import com.dading.ssqs.components.SelectMatchDialog;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.fragment.guesstheball.DataController;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by asus on 2017/12/7.
 * 早盘-足球-波胆
 */

public class EarlyBoDanFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "EarlyBoDanFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ScrollBallBoDanAdapter adapter;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private SelectMatchDialog selectMatchDialog;
    private LoadingDialog loadingDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private TimeCell timeCell;
    private EarlyBean originalData;//原始数据
    private ImageView defaultView;

    private boolean isRefresh = false;
    private int sType = 0;

    private String currSelectTime = "";

    private ScrollBallFootBallBoDanBean currFootBallBoDanBean = null;
    private List<ScrollBallFootBallBoDanBean> newData = new ArrayList<>();
    private List<EarlyBean.EarlyMatchs> networkData = new ArrayList<>();

    private int leagueMatchId = -1;//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private List<ScrollBallBoDanFragment.MergeBean> leagusList = new ArrayList<>();

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter);
        NotificationController.getInstance().addObserver(this, NotificationController.early_mask);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (filterCell != null) {
            filterCell.destoryRunnable(false);
        }

        NotificationController.getInstance().removeObserver(this, NotificationController.footBallFilter);
        NotificationController.getInstance().removeObserver(this, NotificationController.early_mask);
    }

    public void filterPause() {
        if (filterCell != null && filterCell.getStartStatus()) {
            filterCell.destoryRunnable(false);
        }
    }

    public void filterResume() {
        if (filterCell != null && filterCell.getStartStatus()) {
            filterCell.beginRunnable(false);
        }
    }

    private View initView() {
        RelativeLayout container = new RelativeLayout(mContext);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        timeCell = new TimeCell(mContext);
        timeCell.setListener(new TimeCell.OnChangTimeListener() {
            @Override
            public void onChange(String time) {
                currSelectTime = time;

                DataController.getInstance().clearEarlyFootBallData();

                swipeToLoadLayout.setRefreshing(true);
            }
        });
        contentLayout.addView(timeCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setBackgroundColor(0xFF00425D);
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView tvTitle = new TextView(mContext);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setTextSize(13);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText("早盘-足球:" + LocaleController.getString(R.string.scroll_title2));
        tvTitle.setGravity(Gravity.CENTER);
        titleLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 30, 12, 0, 0, 0));

        filterCell = new GuessFilterCell(mContext);
        filterCell.setSecondRefresh(180);
        filterCell.setRefreshListener(new GuessFilterCell.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
        filterCell.setSelectClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectMatchDialog == null) {
                    selectMatchDialog = new SelectMatchDialog(mContext);
                    selectMatchDialog.setListener(new SelectMatchDialog.OnSubmitListener() {
                        @Override
                        public void onSubmit(List<String> list, boolean isAll) {
                            isFilter = true;
                            if (isAll) {
                                filterCell.setSelectText(LocaleController.getString(R.string.select_all));
                            } else {
                                filterCell.setSelectText("选择联赛(" + list.size() + ")");
                            }
                            leagueIDs = "";

                            for (int i = 0; i < list.size(); i++) {
                                leagueIDs += list.get(i) + ",";
                            }

                            if (leagueIDs.length() >= 1) {
                                leagueIDs = leagueIDs.substring(0, leagueIDs.length() - 1);
                            }
                            swipeToLoadLayout.setRefreshing(true);
                        }
                    });
                }
                //判断是否有联赛的数据  没有的话网路请求
                if (DataController.getInstance().getEarlyFootBallData() == null) {
                    DataController.getInstance().syncEarlyFootBall(TAG, currSelectTime);
                    loadingDialog.show();
                } else {
                    selectMatchDialog.show(DataController.getInstance().getEarlyFootBallData(), DataController.getInstance().getEarlyFootBallHotData(), "联赛选择");
                }
            }
        });
        filterCell.setFilterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterDialog == null) {
                    filterDialog = new FilterDialog(mContext);
                    filterDialog.setItemListener(new FilterDialogAdapter.OnClickListener() {
                        @Override
                        public void onClick(String title) {
                            isFilter = true;

                            filter_str = title;

                            filterDialog.dismiss();

                            filterCell.setTimeText(title);

                            if ("按时间排序".equals(title)) {
                                sType = 0;
                            } else {
                                sType = 1;
                            }
                            swipeToLoadLayout.setRefreshing(true);
                        }
                    });
                }
                filterDialog.show("顺序选择", filter_str);
            }
        });
        filterCell.setPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageDialog == null) {
                    pageDialog = new PageDialog(mContext);
                    pageDialog.setItemListener(new PageDialogAdapter.OnClickListener() {
                        @Override
                        public void onClick(int page) {
                            pageDialog.dismiss();
                        }
                    });
                }

                List<Integer> pageList = new ArrayList<>();
                pageList.add(1);

                pageDialog.show(pageList, "页数选择", 1);
            }
        });
        contentLayout.addView(filterCell);

        View view = View.inflate(mContext, R.layout.fragment_scrollball, null);
        contentLayout.addView(view);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        RecyclerScrollview scrollview = (RecyclerScrollview) view.findViewById(R.id.swipe_target);

        mRecyclerView = new RecyclerView(mContext);
        scrollview.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new ScrollBallBoDanAdapter(mContext);
        adapter.setReadyListener(readyListener);
        adapter.setListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("今日-足球");
                    commitMenuView.setBoDanData(leagusList);
                    commitMenuView.show();

                    NotificationController.getInstance().postNotification(NotificationController.today_mask, "open");
                } else {
                    pay();
                }

            }
        });
        commitView.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitMenuView.hide(true);
                commitView.setVisibility(View.GONE);

                adapter.refreshData();

                leagusList.clear();
                leagueMatchId = -1;
            }
        });
        container.addView(commitView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM));

        commitMenuView = new ScrollBallCommitMenuView(mContext, LocaleController.getString(R.string.betting_slips), LocaleController.getString(R.string.latest_ten_transactions));
        commitMenuView.setMenuItemDeleteListener(menuListener);
        commitMenuView.setMenuListener(new ScrollBallCommitMenuView.OnCommitMenuListener() {
            @Override
            public void onClear() {
                commitView.setVisibility(View.GONE);
                leagusList.clear();
                leagueMatchId = -1;
            }

            @Override
            public void onHide() {
                NotificationController.getInstance().postNotification(NotificationController.today_mask, "close");
            }

            @Override
            public void onDone() {
                pay();
            }
        });
        commitMenuView.setVisibility(View.GONE);
        container.addView(commitMenuView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        defaultView = new ImageView(mContext);
        defaultView.setVisibility(View.GONE);
        defaultView.setImageResource(R.mipmap.no_data);
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        init();
        return container;
    }

    private void pay() {
        List<ScrollBallCommitMenuView.MergeBean> moneyLists = commitMenuView.getMoney();

        boolean isEmpty = false;

        for (int i = 0; i < moneyLists.size(); i++) {
            ScrollBallCommitMenuView.MergeBean bean = moneyLists.get(i);
            if (TextUtils.isEmpty(bean.getMoney()) || bean.getMoney().equals("0") || Integer.valueOf(bean.getMoney()) < 10) {
                isEmpty = true;
                break;
            }
        }

        if (isEmpty) {
            Toast.makeText(mContext, "请输入投注金额,并且不能小于10元", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.refreshData();

        commitMenuView.hide(true);
        commitView.setVisibility(View.GONE);

        loadingDialog.show();

        PayBallElement element = new PayBallElement();

        List<PayBallElement.BetBean> items = new ArrayList<>();

        for (int i = 0; i < leagusList.size(); i++) {
            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> list = leagusList.get(i).getBean();

            for (int j = 0; j < list.size(); j++) {
                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                bean.type = 1;
                bean.itemID = list.get(j).getId();
                bean.amount = moneyLists.get(j).getMoney();
                items.add(bean);
            }
        }

        element.setItems(items);

        leagusList.clear();
        leagueMatchId = -1;

        SSQSApplication.apiClient(0).payBallScore(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();

                if (result.isOk()) {
                    Toast.makeText(mContext, "下注成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (result.getErrno() == 403) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private ScrollBallCommitMenuAdapter.OnMenuClickListener menuListener = new ScrollBallCommitMenuAdapter.OnMenuClickListener() {
        @Override
        public void onClick(int position, final int dataId, final int itemId, final String value) {
            commitMenuView.changeData(position);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Iterator<ScrollBallBoDanFragment.MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        ScrollBallBoDanFragment.MergeBean mergeBean = iterator.next();

                        List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> items = mergeBean.getBean();

                        if (mergeBean.getItems().getId() == itemId) {
                            Iterator<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> itemIterator = items.iterator();

                            while (itemIterator.hasNext()) {
                                ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem beanItem = itemIterator.next();

                                if (beanItem.getId() == dataId && beanItem.getStr().equals(value)) {
                                    itemIterator.remove();

                                    if (items.size() == 0) {
                                        iterator.remove();
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    SSQSApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setFocus(leagusList);
                            adapter.refreshData();

                            if (leagusList.size() >= 1) {
                                commitView.setCount(leagusList.size());
                            }
                        }
                    });
                }
            });
            SSQSApplication.cachedThreadPool.execute(thread);
        }
    };

    private BoDanChildCell.OnItemClickListener itemClickListener = new BoDanChildCell.OnItemClickListener() {
        @Override
        public boolean onClick(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems items, ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem bean, int id, boolean isAdd, int position) {
            if (leagueMatchId == id || leagueMatchId == -1) {
                leagueMatchId = id;

                if (isAdd) {//是添加 还是删除
                    if (leagusList.size() == 0) {
                        ScrollBallBoDanFragment.MergeBean mergeBean = new ScrollBallBoDanFragment.MergeBean();
                        mergeBean.setItems(items);

                        for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                            if (originalData.getLeagueName().get(i).getId() == id) {
                                mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                                break;
                            }
                        }

                        List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = new ArrayList<>();
                        bean.setPosition(position);
                        beanItems.add(bean);

                        mergeBean.setBean(beanItems);

                        leagusList.add(mergeBean);
                    } else {
                        boolean isNew = true;//是否添加新的数据
                        for (int i = 0; i < leagusList.size(); i++) {
                            ScrollBallBoDanFragment.MergeBean mergeBean = leagusList.get(i);

                            if (mergeBean.getItems().getId() == items.getId()) {//一个比赛下 不同的item
                                isNew = false;

                                List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = mergeBean.getBean();
                                bean.setPosition(position);
                                beanItems.add(bean);

                                mergeBean.setBean(beanItems);
                                break;
                            }
                        }

                        if (isNew) {
                            ScrollBallBoDanFragment.MergeBean mergeBean = new ScrollBallBoDanFragment.MergeBean();
                            mergeBean.setItems(items);

                            for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                                if (originalData.getLeagueName().get(i).getId() == id) {
                                    mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                                    break;
                                }
                            }

                            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = new ArrayList<>();
                            bean.setPosition(position);
                            beanItems.add(bean);

                            mergeBean.setBean(beanItems);

                            leagusList.add(mergeBean);
                        }
                    }
                } else {
                    Iterator<ScrollBallBoDanFragment.MergeBean> mergeBeanIterator = leagusList.iterator();

                    while (mergeBeanIterator.hasNext()) {
                        ScrollBallBoDanFragment.MergeBean mergeBean = mergeBeanIterator.next();

                        if (mergeBean.getItems().getId() == items.getId()) {
                            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = mergeBean.getBean();

                            Iterator<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItemIterator = beanItems.iterator();
                            while (beanItemIterator.hasNext()) {
                                ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem beanItem = beanItemIterator.next();
                                if (beanItem.getId() == bean.getId()) {
                                    beanItemIterator.remove();

                                    if (beanItems.size() == 0) {
                                        mergeBeanIterator.remove();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                if (leagusList.size() == 0) {
                    leagueMatchId = -1;
                    commitView.setVisibility(View.GONE);
                } else {
                    commitView.setCount(leagusList.size());
                    commitView.setVisibility(View.VISIBLE);
                }

                return true;
            } else {
                return false;
            }
        }
    };

    private ScrollBallCell.OnReadyListener readyListener = new ScrollBallCell.OnReadyListener() {
        @Override
        public void onReady(final String title) {
            newData.clear();
            networkData.clear();

            loadingDialog.show();

            if (originalData != null && originalData.getLeagueName() != null) {

                for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                    EarlyBean.EarlyBeanItems items = originalData.getLeagueName().get(i);

                    List<EarlyBean.EarlyMatchs> matchs = items.getMatchs();

                    for (int k = 0; k < matchs.size(); k++) {
                        networkData.add(matchs.get(k));
                    }
                }
            }

            newData.addAll(adapter.getData());//当前的数据

            //找到当前点击的那一个item
            for (int i = 0; i < newData.size(); i++) {
                ScrollBallFootBallBoDanBean temporaryBean = newData.get(i);
                if (temporaryBean.getTitle().getTitle().equals(title)) {
                    currFootBallBoDanBean = temporaryBean;
                    break;
                }
            }

            //把子数据添加进去

            if (currFootBallBoDanBean != null) {
                String params = "";
                for (int i = 0; i < networkData.size(); i++) {
                    params += networkData.get(i).getId() + ",";
                }

                if (params.length() >= 1) {
                    params = params.substring(0, params.length() - 1);
                }

                SSQSApplication.apiClient(0).getScoreByIdList(params, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            final List<JCScorebean> items = (List<JCScorebean>) result.getData();

                            if (items != null) {

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currFootBallBoDanBean.setItems(handleOtherData(items, networkData));

                                        SSQSApplication.getHandler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.setOpenTitle(title);
                                                adapter.setFocus(leagusList);
                                                adapter.setList(newData);

                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }
                                });
                                SSQSApplication.cachedThreadPool.execute(thread);
                            }
                        } else {
                            ToastUtils.midToast(mContext, result.getMessage(), 0);
                        }
                    }
                });
            }
        }
    };

    public void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }

        filterCell.setTotalPage(1);
        filterCell.setCurrPage(1);

        loadingDialog.show();
        getNetDataWork();
    }

    private void getNetDataWork() {
        if (TextUtils.isEmpty(currSelectTime)) {
            currSelectTime = DateUtils.getCurTime("yyyyMMddHH:mm:ss");
        }

        SSQSApplication.apiClient(0).getEarlyFootBallList(currSelectTime, sType, leagueIDs, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    EarlyBean earlyBean = (EarlyBean) result.getData();

                    if (earlyBean != null && earlyBean.getLeagueName() != null && earlyBean.getLeagueName().size() >= 1) {
                        defaultView.setVisibility(View.GONE);

                        filterCell.beginRunnable(true);

                        originalData = earlyBean;

                        adapter.setList(getData(getFilterData(earlyBean)));
                    } else {
                        adapter.clearData();

                        filterCell.destoryRunnable(true);

                        defaultView.setVisibility(View.VISIBLE);
                    }

                    isRefresh = false;
                } else {
                    adapter.clearData();

                    filterCell.destoryRunnable(true);

                    defaultView.setVisibility(View.VISIBLE);

                    ToastUtils.midToast(mContext, result.getMessage(), 1000);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            if (isFilter) {
                isFilter = false;
            } else {
                filterCell.setSelectText(LocaleController.getString(R.string.select_all));
                leagueIDs = "0";
            }
            getNetDataWork();
        }
    }


    private List<ScrollBallFootBallBoDanBean> getData(List<CommonTitle> list) {
        List<ScrollBallFootBallBoDanBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ScrollBallFootBallBoDanBean bean = new ScrollBallFootBallBoDanBean();
            bean.setTitle(list.get(i));

            items.add(bean);
        }

        return items;
    }

    //获取筛选完的数据
    private List<CommonTitle> getFilterData(EarlyBean data) {
        List<CommonTitle> list = new ArrayList<>();

        List<EarlyBean.EarlyBeanItems> items = data.getLeagueName();

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (list.size() > 0) {
                    boolean isAdd = true;
                    for (int j = 0; j < list.size(); j++) {
                        CommonTitle commonTitle = list.get(j);
                        if (commonTitle.getTitle().equals(items.get(i).getTitle())) {
                            isAdd = false;
                            break;
                        }
                    }

                    if (isAdd) {
                        CommonTitle title = new CommonTitle();
                        title.setId(items.get(i).getId());
                        title.setTitle(items.get(i).getTitle());

                        list.add(title);
                    }
                } else {

                    CommonTitle title = new CommonTitle();
                    title.setId(items.get(i).getId());
                    title.setTitle(items.get(i).getTitle());

                    list.add(title);
                }
            }
        }
        return list;
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems> handleOtherData(List<JCScorebean> items, List<EarlyBean.EarlyMatchs> currData) {
        List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems> beanItems1 = new ArrayList<>();

        //波胆数据
        for (int i = 0; i < currData.size(); i++) {
            ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems item = new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems();
            item.setId(currData.get(i).getId());
            item.setTitle(currData.get(i).getHome());
            item.setByTitle(currData.get(i).getAway());

            String time = DateUtils.changeFormater(currData.get(i).getOpenTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm");

            item.setTime(time);

            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> testItems = new ArrayList<>();

            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> twoRowData = new ArrayList<>();
            twoRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(currData.get(i).getHome()));

            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> threeRowData = new ArrayList<>();
            threeRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(currData.get(i).getAway()));

            List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> fiveRowData = new ArrayList<>();
            fiveRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""));

            if (items.size() != 0) {
                for (int j = 0; j < items.size(); j++) {
                    List<JCScorebean.ListEntity> entityList = items.get(j).list;
                    for (int k = 0; k < entityList.size(); k++) {
                        List<JCScorebean.ListEntity.ItemsEntity> itemsEntities = entityList.get(k).items;
                        if (entityList.get(k).name.equals("主胜")) {
                            for (int l = 0; l < itemsEntities.size(); l++) {
                                if (currData.get(i).getId() == itemsEntities.get(l).matchID) {
                                    if (twoRowData.size() < 11) {
                                        twoRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities.get(l).id, itemsEntities.get(l).payRate, 1));
                                    }
                                }
                            }
                        } else if (entityList.get(k).name.equals("主负")) {
                            for (int l = 0; l < itemsEntities.size(); l++) {
                                if (currData.get(i).getId() == itemsEntities.get(l).matchID) {
                                    if (threeRowData.size() < 11) {
                                        threeRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities.get(l).id, itemsEntities.get(l).payRate, 2));
                                    }
                                }
                            }
                        } else if (entityList.get(k).name.equals("平")) {
                            for (int l = 0; l < itemsEntities.size(); l++) {
                                if (currData.get(i).getId() == itemsEntities.get(l).matchID) {
                                    if (fiveRowData.size() < 6) {
                                        fiveRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities.get(l).id, itemsEntities.get(l).payRate, 1));
                                    }
                                }
                            }
                        } else if (entityList.get(k).name.equals("其他")) {
                            for (int l = 0; l < itemsEntities.size(); l++) {
                                if (currData.get(i).getId() == itemsEntities.get(l).matchID) {
                                    if (fiveRowData.size() < 7) {
                                        fiveRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities.get(l).id, itemsEntities.get(l).payRate, 1));
                                    }
                                }
                            }
                        }
                    }
                }

                if (twoRowData.size() < 11) {
                    for (int j = twoRowData.size(); j < 11; j++) {
                        twoRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""));
                    }
                }

                if (threeRowData.size() < 11) {
                    for (int j = threeRowData.size(); j < 11; j++) {
                        threeRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""));
                    }
                }

                if (fiveRowData.size() < 7) {
                    for (int j = fiveRowData.size(); j < 7; j++) {
                        fiveRowData.add(new ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""));
                    }
                }

                testItems.addAll(twoRowData);
                testItems.addAll(threeRowData);
                testItems.addAll(fiveRowData);

                item.setItemList(testItems);
                beanItems1.add(item);
            }
        }
        return beanItems1;
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.early_mask) {
            if (args != null && args.length >= 1) {
                if ("child_close".equals(args[0])) {
                    if (commitMenuView != null && commitMenuView.getVisibility() == View.VISIBLE) {
                    }
                    commitMenuView.hide(false);
                }
            }
        } else if (id == NotificationController.footBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    selectMatchDialog.show(DataController.getInstance().getEarlyFootBallData(), DataController.getInstance().getEarlyFootBallHotData(), "联赛选择");
                }
            }
        }
    }
}
