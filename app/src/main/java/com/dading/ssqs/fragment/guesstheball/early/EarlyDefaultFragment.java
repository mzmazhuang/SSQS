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
import android.util.Log;
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
import com.dading.ssqs.adapter.newAdapter.EarlyDefaultAdapter;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallItemAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.EarlyBean;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
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
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 * 早盘-足球-默认
 */

public class EarlyDefaultFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "EarlyDefaultFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private EarlyDefaultAdapter adapter;
    private LoadingDialog loadingDialog;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private SelectMatchDialog selectMatchDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private TimeCell timeCell;
    private EarlyBean originalData;//原始数据
    private ImageView defaultView;

    private boolean isRefresh;

    private int sType;

    private int offset = 1;
    private int limit = 10;

    private String currSelectTime = "";

    private ScrollBallFootBallBean currFootBallDefaultBean = null;
    private List<ScrollBallFootBallBean> newData = new ArrayList<>();
    private List<EarlyBean.EarlyMatchs> networkData = new ArrayList<>();

    private int leagueMatchId = -1;//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private List<ScrollBallDefaultFragment.MergeBean> leagusList = new ArrayList<>();

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    private int totalPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.early_mask);
        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter);
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
        tvTitle.setText("早盘-足球:" + LocaleController.getString(R.string.scroll_title1));
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

        adapter = new EarlyDefaultAdapter(mContext);
        adapter.setReadyListener(readyListener);
        adapter.setItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("早盘-足球");
                    commitMenuView.setData(leagusList);
                    commitMenuView.show();

                    NotificationController.getInstance().postNotification(NotificationController.early_mask, "open");
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
                NotificationController.getInstance().postNotification(NotificationController.early_mask, "close");
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

        commitMenuView.hide(true);
        commitView.setVisibility(View.GONE);

        adapter.refreshData();

        loadingDialog.show();

        PayBallElement element = new PayBallElement();

        List<PayBallElement.BetBean> items = new ArrayList<>();

        for (int i = 0; i < leagusList.size(); i++) {
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> list = leagusList.get(i).getBean();

            for (int j = 0; j < list.size(); j++) {
                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                bean.matchID = leagusList.get(i).getItems().getId();
                bean.type = 1;
                bean.amount = moneyLists.get(j).getMoney();
                bean.payRateID = list.get(j).getId();
                bean.selected = list.get(j).getSelected();
                items.add(bean);
            }
        }

        element.setItems(items);

        leagusList.clear();
        leagueMatchId = -1;

        SSQSApplication.apiClient(0).payBall(element, new CcApiClient.OnCcListener() {
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
                    Iterator<ScrollBallDefaultFragment.MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        ScrollBallDefaultFragment.MergeBean mergeBean = iterator.next();

                        List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> items = mergeBean.getBean();

                        if (mergeBean.getItems().getId() == itemId) {
                            Iterator<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> itemIterator = items.iterator();

                            while (itemIterator.hasNext()) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = itemIterator.next();

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

    private ScrollBallItemAdapter.OnItemClickListener itemClickListener = new ScrollBallItemAdapter.OnItemClickListener() {
        @Override
        //联赛id                                      //所点击的item                          //所点击的联赛信息                                                //是否是主场       //点击的位置
        public boolean onItemClick(int id, ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem bean, ScrollBallFootBallBean.ScrollBeanItems items, boolean isAdd, boolean isHome, int position) {
            if (leagueMatchId == id || leagueMatchId == -1) {
                leagueMatchId = id;

                if (isAdd) {//是添加 还是删除
                    if (leagusList.size() == 0) {
                        ScrollBallDefaultFragment.MergeBean mergeBean = new ScrollBallDefaultFragment.MergeBean();
                        mergeBean.setItems(items);

                        for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                            if (originalData.getLeagueName().get(i).getId() == id) {
                                mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                                break;
                            }
                        }

                        List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = new ArrayList<>();
                        bean.setPosition(position);
                        beanItems.add(bean);

                        mergeBean.setBean(beanItems);

                        leagusList.add(mergeBean);
                    } else {
                        boolean isNew = true;//是否添加新的数据
                        for (int i = 0; i < leagusList.size(); i++) {
                            ScrollBallDefaultFragment.MergeBean mergeBean = leagusList.get(i);

                            if (mergeBean.getItems().getId() == items.getId()) {//一个比赛下 不同的item
                                isNew = false;

                                List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = mergeBean.getBean();
                                bean.setPosition(position);
                                beanItems.add(bean);

                                mergeBean.setBean(beanItems);
                                break;
                            }
                        }

                        if (isNew) {
                            ScrollBallDefaultFragment.MergeBean mergeBean = new ScrollBallDefaultFragment.MergeBean();
                            mergeBean.setItems(items);

                            for (int i = 0; i < originalData.getLeagueName().size(); i++) {
                                if (originalData.getLeagueName().get(i).getId() == id) {
                                    mergeBean.setTitle(originalData.getLeagueName().get(i).getTitle());
                                    break;
                                }
                            }

                            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = new ArrayList<>();
                            bean.setPosition(position);
                            beanItems.add(bean);

                            mergeBean.setBean(beanItems);

                            leagusList.add(mergeBean);
                        }
                    }
                } else {
                    Iterator<ScrollBallDefaultFragment.MergeBean> mergeBeanIterator = leagusList.iterator();

                    while (mergeBeanIterator.hasNext()) {
                        ScrollBallDefaultFragment.MergeBean mergeBean = mergeBeanIterator.next();

                        if (mergeBean.getItems().getId() == items.getId()) {
                            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = mergeBean.getBean();

                            Iterator<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItemIterator = beanItems.iterator();
                            while (beanItemIterator.hasNext()) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = beanItemIterator.next();
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
                ScrollBallFootBallBean temporaryBean = newData.get(i);
                if (temporaryBean.getTitle().getTitle().equals(title)) {
                    currFootBallDefaultBean = temporaryBean;
                    break;
                }
            }

            //把子数据添加进去
            try {
                if (currFootBallDefaultBean != null) {
                    String params = "";
                    for (int i = 0; i < networkData.size(); i++) {
                        params += networkData.get(i).getId() + ",";
                    }

                    if (params.length() >= 1) {
                        params = params.substring(0, params.length() - 1);
                    }

                    SSQSApplication.apiClient(0).getMatchResult(params, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                final List<JCbean> items = (List<JCbean>) result.getData();

                                if (items != null) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            currFootBallDefaultBean.setItems(handleItemData(items, networkData));

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
                                    }).start();
                                }
                            } else {
                                ToastUtils.midToast(mContext, result.getMessage(), 0);
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("Early default", "failure" + ex);
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

        SSQSApplication.apiClient(0).getEarlyFootBallList(currSelectTime, sType, leagueIDs, off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    EarlyBean earlyBean = (EarlyBean) result.getData();

                    filterCell.setCurrPage(off);

                    if (earlyBean != null && earlyBean.getLeagueName() != null && earlyBean.getLeagueName().size() >= 1) {

                        totalPage = earlyBean.getTotalCount();

                        filterCell.setTotalPage(earlyBean.getTotalCount());

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

                offset = 1;
            }

            getNetDataWork(offset, limit);
        }
    }

    private List<ScrollBallFootBallBean> getData(List<CommonTitle> list) {
        List<ScrollBallFootBallBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ScrollBallFootBallBean bean = new ScrollBallFootBallBean();
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

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ScrollBallFootBallBean.ScrollBeanItems> handleItemData(List<JCbean> items, List<EarlyBean.EarlyMatchs> currData) {
        List<ScrollBallFootBallBean.ScrollBeanItems> beanItems = new ArrayList<>();

        for (int i = 0; i < currData.size(); i++) {
            ScrollBallFootBallBean.ScrollBeanItems item = new ScrollBallFootBallBean.ScrollBeanItems();
            item.setId(currData.get(i).getId());
            item.setTitle(currData.get(i).getHome());
            item.setByTitle(currData.get(i).getAway());

            String time = DateUtils.changeFormater(currData.get(i).getOpenTime(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm");

            item.setTime(time);

            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> testItems = new ArrayList<>();

            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> oneRowData = new ArrayList<>();
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> twoRowData = new ArrayList<>();
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> threeRowData = new ArrayList<>();
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> fourRowData = new ArrayList<>();
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> fiveRowData = new ArrayList<>();
            List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> sixRowData = new ArrayList<>();

            if (items.size() != 0) {
                //独赢
                for (int j = 0; j < items.size(); j++) {
                    if (currData.get(i).getId() == items.get(j).matchID) {
                        if ("全场赛果".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFFFF);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    oneRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setRightStr(items.get(j).realRate2);
                                    twoRowData.add(beanItem);
                                } else {
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    threeRowData.add(beanItem);
                                }
                            }

                        } else if ("半场赛果".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFADC);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    fourRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setRightStr(items.get(j).realRate2);
                                    fiveRowData.add(beanItem);
                                } else {
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    sixRowData.add(beanItem);
                                }
                            }
                        }
                    }
                }

                if (oneRowData.size() == 0) {
                    oneRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (twoRowData.size() == 0) {
                    twoRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (threeRowData.size() == 0) {
                    threeRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (fourRowData.size() == 0) {
                    fourRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (fiveRowData.size() == 0) {
                    fiveRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (sixRowData.size() == 0) {
                    sixRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }

                //让球
                for (int j = 0; j < items.size(); j++) {
                    if (currData.get(i).getId() == items.get(j).matchID) {
                        if ("全场让球".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFFFF);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setLeftStr(getRate2Str(items.get(j).realRate2, false));
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    oneRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setLeftStr(getRate2Str(items.get(j).realRate2, true));
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    twoRowData.add(beanItem);
                                } else {
                                    threeRowData.add(beanItem);
                                }
                            }

                        } else if ("半场让球".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFADC);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setLeftStr(getRate2Str(items.get(j).realRate2, false));
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    fourRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setLeftStr(getRate2Str(items.get(j).realRate2, true));
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    fiveRowData.add(beanItem);
                                } else {
                                    sixRowData.add(beanItem);
                                }
                            }
                        }
                    }
                }

                if (oneRowData.size() == 1) {
                    oneRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (twoRowData.size() == 1) {
                    twoRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (threeRowData.size() == 1) {
                    threeRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (fourRowData.size() == 1) {
                    fourRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (fiveRowData.size() == 1) {
                    fiveRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (sixRowData.size() == 1) {
                    sixRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }

                //大小
                for (int j = 0; j < items.size(); j++) {
                    if (currData.get(i).getId() == items.get(j).matchID) {
                        if ("全场大小".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFFFF);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setLeftStr("大" + items.get(j).realRate2);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    oneRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setLeftStr("小" + items.get(j).realRate2);
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    twoRowData.add(beanItem);
                                } else {
                                    threeRowData.add(beanItem);
                                }
                            }

                        } else if ("半场大小".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFADC);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setLeftStr("大" + items.get(j).realRate2);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    fourRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setLeftStr("小" + items.get(j).realRate2);
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    fiveRowData.add(beanItem);
                                } else {
                                    sixRowData.add(beanItem);
                                }
                            }
                        }
                    }
                }

                if (oneRowData.size() == 2) {
                    oneRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (twoRowData.size() == 2) {
                    twoRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (threeRowData.size() == 2) {
                    threeRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (fourRowData.size() == 2) {
                    fourRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (fiveRowData.size() == 2) {
                    fiveRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (sixRowData.size() == 2) {
                    sixRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }

                //单双
                for (int j = 0; j < items.size(); j++) {
                    if (currData.get(i).getId() == items.get(j).matchID) {
                        if ("全场单双".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setBackground(0xFFFFFFFF);
                                beanItem.setId(items.get(j).id);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    oneRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    twoRowData.add(beanItem);
                                } else {
                                    threeRowData.add(beanItem);
                                }
                            }

                        } else if ("半场单双".equals(items.get(j).payTypeName)) {
                            for (int k = 0; k < 3; k++) {
                                ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem beanItem = new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem();
                                beanItem.setId(items.get(j).id);
                                beanItem.setBackground(0xFFFFFADC);
                                if (k == 0) {
                                    beanItem.setSelected(1);
                                    beanItem.setRightStr(items.get(j).realRate1);
                                    fourRowData.add(beanItem);
                                } else if (k == 1) {
                                    beanItem.setSelected(2);
                                    beanItem.setRightStr(items.get(j).realRate3);
                                    fiveRowData.add(beanItem);
                                } else {
                                    sixRowData.add(beanItem);
                                }
                            }
                        }
                    }
                }

                if (oneRowData.size() == 3) {
                    oneRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (twoRowData.size() == 3) {
                    twoRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (threeRowData.size() == 3) {
                    threeRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(0xFFFFFFFF));
                }
                if (fourRowData.size() == 3) {
                    fourRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (fiveRowData.size() == 3) {
                    fiveRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }
                if (sixRowData.size() == 3) {
                    sixRowData.add(new ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem());
                }

                testItems.addAll(oneRowData);
                testItems.addAll(twoRowData);
                testItems.addAll(threeRowData);
                testItems.addAll(fourRowData);
                testItems.addAll(fiveRowData);
                testItems.addAll(sixRowData);

                item.setTestItems(testItems);

                beanItems.add(item);
            }
        }
        return beanItems;
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
