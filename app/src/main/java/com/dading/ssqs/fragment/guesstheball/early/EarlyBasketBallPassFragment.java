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
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.adapter.newAdapter.ToDayBasketBallDefaultAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.EarlyBean;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
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
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 * 早盘-篮球-综合过关
 */

public class EarlyBasketBallPassFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "EarlyBasketBallPassFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ToDayBasketBallDefaultAdapter adapter;
    private LoadingDialog loadingDialog;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private SelectMatchDialog selectMatchDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private TimeCell timeCell;
    private EarlyBean originalData;//原始数据
    private boolean isRefresh = false;
    private String currSelectTime = "";
    private ImageView defaultView;

    private int sType;

    private ScrollBallBasketBallBean currBasketBallDefaultBean = null;
    private List<ScrollBallBasketBallBean> newData = new ArrayList<>();
    private List<EarlyBean.EarlyMatchs> networkData = new ArrayList<>();

    private List<ScrollBallBasketBallDefaultFragment.MergeBean> leagusList = new ArrayList<>();

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    private int offset = 1;
    private int limit = 10;
    private int totalPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.early_mask);
        NotificationController.getInstance().addObserver(this, NotificationController.basketBallFilter);

        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (filterCell != null) {
            filterCell.destoryRunnable(false);
        }

        NotificationController.getInstance().removeObserver(this, NotificationController.early_mask);
        NotificationController.getInstance().removeObserver(this, NotificationController.basketBallFilter);
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

                DataController.getInstance().clearEarlyBasketBallData();

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
        tvTitle.setText("早盘-篮球:" + LocaleController.getString(R.string.scroll_title14));
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
                if (DataController.getInstance().getEarlyBaskteBallData() == null) {
                    DataController.getInstance().syncEarlyBasketBall(TAG, currSelectTime);
                    loadingDialog.show();
                } else {
                    selectMatchDialog.show(DataController.getInstance().getEarlyBaskteBallData(), DataController.getInstance().getEarlyBasketBallHotData(), "联赛选择");
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
                            isFilter = true;

                            pageDialog.dismiss();

                            offset = page;

                            swipeToLoadLayout.setRefreshing(true);
                        }
                    });
                }

                List<Integer> pageList = new ArrayList<>();

                for (int i = 1; i <= totalPage; i++) {
                    pageList.add(i);
                }

                pageDialog.show(pageList, "页数选择", offset);
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

        adapter = new ToDayBasketBallDefaultAdapter(mContext);
        adapter.setReadyListener(readyListener);
        adapter.setItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("今日-篮球");
                    commitMenuView.setBaskData(leagusList);
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
            }
        });
        container.addView(commitView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM));

        commitMenuView = new ScrollBallCommitMenuView(mContext, LocaleController.getString(R.string.betting_slips), LocaleController.getString(R.string.latest_ten_transactions));
        commitMenuView.setType(2);
        commitMenuView.setMenuItemDeleteListener(menuListener);
        commitMenuView.setMenuListener(new ScrollBallCommitMenuView.OnCommitMenuListener() {
            @Override
            public void onClear() {
                commitView.setVisibility(View.GONE);
                leagusList.clear();
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
        if (TextUtils.isEmpty(commitMenuView.getStandResult()) || commitMenuView.getStandResult().equals("0") || Integer.valueOf(commitMenuView.getStandResult()) < 10) {
            Toast.makeText(mContext, "请输入投注金额,并且不能小于10元", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.refreshData();

        commitMenuView.hide(true);
        commitView.setVisibility(View.GONE);

        loadingDialog.show();

        PayBallElement element = new PayBallElement();

        List<PayBallElement.BetBean> items = new ArrayList<>();

        String moneys = commitMenuView.getStandResult();

        for (int i = 0; i < leagusList.size(); i++) {
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> list = leagusList.get(i).getBean();

            for (int j = 0; j < list.size(); j++) {
                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                bean.matchID = leagusList.get(i).getItems().getId();
                bean.type = 2;
                bean.amount = moneys;
                bean.payRateID = list.get(j).getId();
                bean.selected = list.get(j).getSelected();
                items.add(bean);

                moneys = "0";
            }
        }

        element.setItems(items);

        leagusList.clear();

        SSQSApplication.apiClient(0).payBallDouble(element, new CcApiClient.OnCcListener() {
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

    private BasketScrollBallItemAdapter.OnItemClickListener itemClickListener = new BasketScrollBallItemAdapter.OnItemClickListener() {

        @Override
        public boolean onItemClick(int id, ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem bean, ScrollBallBasketBallBean.ScrollBaksetBallItems items, boolean isAdd, boolean isHome, int position) {
            for (int i = 0; i < leagusList.size(); i++) {
                ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = leagusList.get(i);

                if (mergeBean.getItems().getId() == items.getId()) {
                    if (isAdd) {
                        return false;
                    }
                }
            }
            if (isAdd) {//是添加 还是删除
                if (leagusList.size() == 0) {
                    ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = new ScrollBallBasketBallDefaultFragment.MergeBean();
                    mergeBean.setItems(items);
                    mergeBean.setHome(isHome);

                    for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                        if (originalData.getLeagueName().get(i).getId() == id) {
                            mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                            break;
                        }
                    }

                    List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = new ArrayList<>();
                    bean.setPosition(position);
                    beanItems.add(bean);

                    mergeBean.setBean(beanItems);

                    leagusList.add(mergeBean);
                } else {
                    boolean isNew = true;//是否添加新的数据
                    for (int i = 0; i < leagusList.size(); i++) {
                        ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = leagusList.get(i);

                        if (mergeBean.getItems().getId() == items.getId()) {//一个比赛下 不同的item
                            isNew = false;

                            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = mergeBean.getBean();
                            bean.setPosition(position);
                            beanItems.add(bean);

                            mergeBean.setBean(beanItems);
                            break;
                        }
                    }

                    if (isNew) {
                        ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = new ScrollBallBasketBallDefaultFragment.MergeBean();
                        mergeBean.setItems(items);
                        mergeBean.setHome(isHome);

                        for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                            if (originalData.getLeagueName().get(i).getId() == id) {
                                mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                                break;
                            }
                        }

                        List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = new ArrayList<>();
                        bean.setPosition(position);
                        beanItems.add(bean);

                        mergeBean.setBean(beanItems);

                        leagusList.add(mergeBean);
                    }
                }
            } else {
                Iterator<ScrollBallBasketBallDefaultFragment.MergeBean> mergeBeanIterator = leagusList.iterator();

                while (mergeBeanIterator.hasNext()) {
                    ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = mergeBeanIterator.next();

                    if (mergeBean.getItems().getId() == items.getId()) {
                        List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = mergeBean.getBean();

                        Iterator<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItemIterator = beanItems.iterator();
                        while (beanItemIterator.hasNext()) {
                            ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem beanItem = beanItemIterator.next();
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
                commitView.setVisibility(View.GONE);
            } else {
                commitView.setCount(leagusList.size());
                commitView.setVisibility(View.VISIBLE);
            }
            return true;
        }

        @Override
        public void onInfoClick(int matchId, String title) {

        }
    };

    private ScrollBallCommitMenuAdapter.OnMenuClickListener menuListener = new ScrollBallCommitMenuAdapter.OnMenuClickListener() {
        @Override
        public void onClick(int position, final int dataId, final int itemId, final String value) {
            commitMenuView.changeData(position);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Iterator<ScrollBallBasketBallDefaultFragment.MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        ScrollBallBasketBallDefaultFragment.MergeBean mergeBean = iterator.next();

                        List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> items = mergeBean.getBean();

                        if (mergeBean.getItems().getId() == itemId) {
                            Iterator<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> itemIterator = items.iterator();

                            while (itemIterator.hasNext()) {
                                ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem beanItem = itemIterator.next();

                                if (beanItem.getId() == dataId && beanItem.getRightStr().equals(value)) {
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

    private ScrollBallCell.OnReadyListener readyListener = new ScrollBallCell.OnReadyListener() {
        @Override
        public void onReady(final String title) {
            newData.clear();
            networkData.clear();

            loadingDialog.show();

            if (originalData != null && originalData.getLeagueName() != null) {

                for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                    EarlyBean.EarlyBeanItems items = originalData.getLeagueName().get(i);

                    if (title.equals(items.getTitle())) {
                        List<EarlyBean.EarlyMatchs> matchs = items.getMatchs();

                        for (int k = 0; k < matchs.size(); k++) {
                            networkData.add(matchs.get(k));
                        }

                        break;
                    }
                }
            }

            newData.addAll(adapter.getData());//当前的数据

            //找到当前点击的那一个item
            for (int i = 0; i < newData.size(); i++) {
                ScrollBallBasketBallBean temporaryBean = newData.get(i);
                if (temporaryBean.getTitle().getTitle().equals(title)) {
                    currBasketBallDefaultBean = temporaryBean;
                    break;
                }
            }

            //把子数据添加进去

            if (currBasketBallDefaultBean != null) {
                String params = "";
                for (int i = 0; i < networkData.size(); i++) {
                    params += networkData.get(i).getId() + ",";
                }

                if (params.length() >= 1) {
                    params = params.substring(0, params.length() - 1);
                }

                SSQSApplication.apiClient(0).getMatchBasketBallResult(params, "0", new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            final List<JCbean> items = (List<JCbean>) result.getData();

                            if (items != null) {

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currBasketBallDefaultBean.setItems(handleItemData(items, networkData));

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

        loadingDialog.show();
        getNetDataWork(offset, limit);
    }

    private void getNetDataWork(final int off, int lim) {
        if (TextUtils.isEmpty(currSelectTime)) {
            currSelectTime = DateUtils.getCurTime("yyyyMMddHH:mm:ss");
        }

        SSQSApplication.apiClient(0).getMatchBallGuessEarlyList(currSelectTime, sType, leagueIDs, off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    EarlyBean bean = (EarlyBean) result.getData();

                    filterCell.setCurrPage(off);

                    if (bean != null && bean.getLeagueName() != null && bean.getLeagueName().size() >= 1) {
                        defaultView.setVisibility(View.GONE);

                        totalPage = bean.getTotalCount();

                        filterCell.setTotalPage(totalPage);

                        filterCell.beginRunnable(true);

                        originalData = bean;

                        adapter.setList(getData(getFilterData(bean)));
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


    private List<ScrollBallBasketBallBean> getData(List<CommonTitle> list) {
        List<ScrollBallBasketBallBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ScrollBallBasketBallBean bean = new ScrollBallBasketBallBean();
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

    private String getRate2Str(String str, boolean isBig) {
        String value = "";
        if (!TextUtils.isEmpty(str)) {

            String[] array = null;

            if (str.contains("/")) {
                array = str.split("/");
            }

            String rate2;

            if (array != null && array.length == 2) {
                rate2 = array[1];
            } else {
                rate2 = str;
            }

            try {
                if (isBig) {
                    if (rate2.equals("0")) {
                        value = "0";
                    } else if (Double.valueOf(rate2) > 0.0) {
                        if (array != null) {
                            value = array[0] + "/" + Math.abs(Double.valueOf(rate2)) + "";
                        } else {
                            value = Math.abs(Double.valueOf(rate2)) + "";
                        }
                    } else {
                        value = "null";
                    }
                } else {
                    if (rate2.equals("-0")) {
                        value = "0";
                    } else if (Double.valueOf(rate2) < 0.0) {
                        if (array != null) {
                            value = array[0] + "/" + Math.abs(Double.valueOf(rate2)) + "";
                        } else {
                            value = Math.abs(Double.valueOf(rate2)) + "";
                        }
                    } else {
                        value = "null";
                    }
                }
            } catch (Exception ex) {//不是double类型

            }
        } else {
            value = "null";
        }

        return value;
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

                offset = 1;
            }
            getNetDataWork(offset, limit);
        }
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ScrollBallBasketBallBean.ScrollBaksetBallItems> handleItemData(List<JCbean> items, List<EarlyBean.EarlyMatchs> currData) {
        List<ScrollBallBasketBallBean.ScrollBaksetBallItems> beanItems = new ArrayList<>();

        for (int i = 0; i < currData.size(); i++) {
            ScrollBallBasketBallBean.ScrollBaksetBallItems item = new ScrollBallBasketBallBean.ScrollBaksetBallItems();

            EarlyBean.EarlyMatchs currScoreBean = currData.get(i);

            item.setId(currScoreBean.getId());
            item.setTitle(currScoreBean.getHome());
            item.setByTitle(currScoreBean.getAway());

            String time = DateUtils.changeFormater(currScoreBean.getOpenTime(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm");

            item.setTime(time);

            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> scrollBeanItems = new ArrayList<>();

            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> oneRowData = new ArrayList<>();
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> twoRowData = new ArrayList<>();

            if (items.size() != 0) {
                //独赢的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 1) {
                        if (currScoreBean.getId() == jCbean.matchID) {
                            oneRowData.add(getBeanItems("", jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems("", jCbean.realRate3, 2, jCbean.id));

                            break;
                        }
                    }
                }

                if (oneRowData.size() == 0) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 0) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                //让球的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 2) {
                        if (currScoreBean.getId() == jCbean.matchID) {

                            String topStr = getRate2Str(jCbean.realRate2, false);
                            String bottomStr = getRate2Str(jCbean.realRate2, true);

                            oneRowData.add(getBeanItems(topStr, jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems(bottomStr, jCbean.realRate3, 2, jCbean.id));

                            break;
                        }
                    }
                }

                if (oneRowData.size() == 1) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 1) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                //大小的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 3 && TextUtils.isEmpty(jCbean.teamName)) {
                        if (currScoreBean.getId() == jCbean.matchID) {
                            oneRowData.add(getBeanItems("大" + jCbean.realRate2, jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems("小" + jCbean.realRate2, jCbean.realRate3, 2, jCbean.id));

                            break;
                        }
                    }
                }

                if (oneRowData.size() == 2) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 2) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                //球队得分 大/小 的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 48) {//主队
                        if (currScoreBean.getId() == jCbean.matchID) {
                            if (currScoreBean.getHome().equals(jCbean.teamName)) {//主队的数据
                                oneRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id));

                                twoRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id));
                            }
                        }
                    } else if (jCbean.payTypeID == 49) {//客队
                        if (currScoreBean.getAway().equals(jCbean.teamName)) {//客队的数据
                            oneRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id));
                        }
                    }
                }

                if (oneRowData.size() == 3) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 3) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                if (oneRowData.size() == 4) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 4) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                scrollBeanItems.addAll(oneRowData);
                scrollBeanItems.addAll(twoRowData);

                item.setTestItems(scrollBeanItems);

                beanItems.add(item);
            }
        }
        return beanItems;
    }

    private ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem getBeanItems(String leftStr, String rightStr, int selected, int id) {
        ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem item = new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem();
        item.setId(id);
        item.setSelected(selected);

        if (!TextUtils.isEmpty(leftStr)) {
            item.setLeftStr(leftStr);
        }

        if (!TextUtils.isEmpty(rightStr)) {
            item.setRightStr(rightStr);
        }
        return item;
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
        } else if (id == NotificationController.basketBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    selectMatchDialog.show(DataController.getInstance().getEarlyBaskteBallData(), DataController.getInstance().getEarlyBasketBallHotData(), "联赛选择");
                }
            }
        }
    }
}
