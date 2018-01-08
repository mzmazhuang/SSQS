package com.dading.ssqs.fragment.guesstheball.scrollball;

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
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallBasketBallDefaultAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.ScrollBallCell;
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
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 * 滚球-篮球-默认
 */

public class ScrollBallBasketBallDefaultFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "ScrollBallBasketBallDefaultFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ScrollBallBasketBallDefaultAdapter adapter;
    private LoadingDialog loadingDialog;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private SelectMatchDialog selectMatchDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private ImageView defaultView;

    private int offset = 1;
    private int limit = 20;
    private boolean isRefresh = false;

    private int sType = 0;

    private int totalPage;

    private List<ScoreBean> originalData;//原始数据

    private ScrollBallBasketBallBean currBasletBallDefaultBean = null;
    private List<ScrollBallBasketBallBean> newData = new ArrayList<>();
    private List<ScoreBean> networkData = new ArrayList<>();

    private int leagueMatchId = -1;//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private List<MergeBean> leagusList = new ArrayList<>();

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.basketBallFilter);
        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (filterCell != null) {
            filterCell.destoryRunnable(false);
        }

        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask);
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

        filterCell = new GuessFilterCell(mContext);
        filterCell.setSecondRefresh(30);
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
                if (DataController.Companion.getInstance().getBaskteBallData() == null) {
                    DataController.Companion.getInstance().syncBasketBall(TAG, 6);
                    loadingDialog.show();
                } else {
                    selectMatchDialog.show(DataController.Companion.getInstance().getBaskteBallData(), DataController.Companion.getInstance().getBasketBallHotData(), "联赛选择");
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

        adapter = new ScrollBallBasketBallDefaultAdapter(mContext);
        adapter.setReadyListener(readyListener);
        adapter.setItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("滚球-篮球");
                    commitMenuView.setBaskData(leagusList);
                    commitMenuView.show();

                    NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "open");
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
                NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "close");
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
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> list = leagusList.get(i).getBean();

            for (int j = 0; j < list.size(); j++) {
                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                bean.matchID = leagusList.get(i).getItems().getId();
                bean.type = 2;
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
                    Iterator<MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        MergeBean mergeBean = iterator.next();

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

    private BasketScrollBallItemAdapter.OnItemClickListener itemClickListener = new BasketScrollBallItemAdapter.OnItemClickListener() {

        @Override
        public boolean onItemClick(int id, ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem bean, ScrollBallBasketBallBean.ScrollBaksetBallItems items, boolean isAdd, boolean isHome, int position) {
            if (leagueMatchId == id || leagueMatchId == -1) {
                leagueMatchId = id;

                if (isAdd) {//是添加 还是删除
                    if (leagusList.size() == 0) {
                        MergeBean mergeBean = new MergeBean();
                        mergeBean.setItems(items);
                        mergeBean.setHome(isHome);

                        for (int i = 0; i < originalData.size(); i++) {
                            if (originalData.get(i).id == id) {
                                mergeBean.setTitle(originalData.get(i).leagueName);
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
                            MergeBean mergeBean = leagusList.get(i);

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
                            MergeBean mergeBean = new MergeBean();
                            mergeBean.setItems(items);
                            mergeBean.setHome(isHome);

                            for (int i = 0; i < originalData.size(); i++) {
                                if (originalData.get(i).id == id) {
                                    mergeBean.setTitle(originalData.get(i).leagueName);
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
                    Iterator<MergeBean> mergeBeanIterator = leagusList.iterator();

                    while (mergeBeanIterator.hasNext()) {
                        MergeBean mergeBean = mergeBeanIterator.next();

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

        @Override
        public void onInfoClick(int matchId, String title) {
            Intent intent = new Intent(mContext.getApplicationContext(), BasketBallDetailsActivity.class);

            intent.putExtra("data_id", matchId);
            intent.putExtra("data_title", title);

            startActivity(intent);
        }
    };

    private ScrollBallCell.OnReadyListener readyListener = new ScrollBallCell.OnReadyListener() {
        @Override
        public void onReady(final String title) {
            newData.clear();
            networkData.clear();

            loadingDialog.show();

            //当前联赛下面的item 取出来 根据id去获取对应的数据
            for (int i = 0; i < originalData.size(); i++) {
                if (originalData.get(i).leagueName.equals(title)) {
                    networkData.add(originalData.get(i));
                }
            }

            newData.addAll(adapter.getData());//当前的数据

            //找到当前点击的那一个item
            for (int i = 0; i < newData.size(); i++) {
                ScrollBallBasketBallBean temporaryBean = newData.get(i);
                if (temporaryBean.getTitle().getTitle().equals(title)) {
                    currBasletBallDefaultBean = temporaryBean;
                    break;
                }
            }

            //把子数据添加进去

            if (currBasletBallDefaultBean != null) {
                String params = "";
                for (int i = 0; i < networkData.size(); i++) {
                    params += networkData.get(i).id + ",";
                }

                if (params.length() >= 1) {
                    params = params.substring(0, params.length() - 1);
                }

                SSQSApplication.apiClient(0).getMatchBasketBallResult(params, "0", new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            final List<JCbean> items = (List<JCbean>) result.getData();

                            if (items != null && items.size() >= 1) {

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currBasletBallDefaultBean.setItems(handleItemData(items, networkData));

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
                            } else {
                                loadingDialog.dismiss();

                                ToastUtils.midToast(mContext, "篮球 暂无数据!!", 0);
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
        String mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss");

        SSQSApplication.apiClient(0).getScrollBallList(false, 6, mDate, sType, leagueIDs, off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                    filterCell.setCurrPage(off);

                    if (page != null && page.getItems() != null && page.getItems().size() >= 1) {
                        defaultView.setVisibility(View.GONE);

                        filterCell.beginRunnable(true);

                        originalData = page.getItems();

                        totalPage = page.getTotalCount();

                        filterCell.setTotalPage(totalPage);

                        adapter.setList(getData(getFilterData(page.getItems())));
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
    private List<CommonTitle> getFilterData(List<ScoreBean> data) {
        List<CommonTitle> list = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (list.size() > 0) {
                boolean isAdd = true;
                for (int j = 0; j < list.size(); j++) {
                    CommonTitle commonTitle = list.get(j);
                    if (commonTitle.getTitle().equals(data.get(i).leagueName)) {
                        isAdd = false;
                        break;
                    }
                }

                if (isAdd) {
                    CommonTitle title = new CommonTitle();
                    title.setId(data.get(i).id);
                    title.setTitle(data.get(i).leagueName);

                    list.add(title);
                }
            } else {

                CommonTitle title = new CommonTitle();
                title.setId(data.get(i).id);
                title.setTitle(data.get(i).leagueName);

                list.add(title);
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
    private List<ScrollBallBasketBallBean.ScrollBaksetBallItems> handleItemData(List<JCbean> items, List<ScoreBean> currData) {
        List<ScrollBallBasketBallBean.ScrollBaksetBallItems> beanItems = new ArrayList<>();

        for (int i = 0; i < currData.size(); i++) {
            ScrollBallBasketBallBean.ScrollBaksetBallItems item = new ScrollBallBasketBallBean.ScrollBaksetBallItems();

            ScoreBean currScoreBean = currData.get(i);

            item.setId(currScoreBean.id);
            item.setTitle(currScoreBean.home);
            item.setByTitle(currScoreBean.away);

            String time = DateUtils.changeFormater(currScoreBean.openTime, "yyyy-MM-dd HH:mm:ss", "HH:mm");

            item.setTime(time);

            ScrollBallBasketBallBean.ScrollBaksetBallItems.Score score = new ScrollBallBasketBallBean.ScrollBaksetBallItems.Score();
            score.setLeftScore(currScoreBean.hScore);
            score.setRightScore(currScoreBean.aScore);
            score.setCurrSchedule(currScoreBean.protime);

            item.setScore(score);

            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> scrollBeanItems = new ArrayList<>();

            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> oneRowData = new ArrayList<>();
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> twoRowData = new ArrayList<>();
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> threeRowData = new ArrayList<>();
            List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> fourRowData = new ArrayList<>();

            threeRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
            fourRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());

            if (items.size() != 0) {
                //独赢的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 1 && currScoreBean.id == jCbean.matchID) {
                        oneRowData.add(getBeanItems("", jCbean.realRate1, 1, jCbean.id));

                        twoRowData.add(getBeanItems("", jCbean.realRate3, 2, jCbean.id));

                        break;
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

                    if (jCbean.payTypeID == 2 && currScoreBean.id == jCbean.matchID) {
                        String topStr = getRate2Str(items.get(j).realRate2, false);
                        String bottomStr = getRate2Str(items.get(j).realRate2, true);

                        if (oneRowData.size() == 2 && twoRowData.size() == 2) {
                            threeRowData.add(getBeanItems(topStr, jCbean.realRate1, 1, jCbean.id));

                            fourRowData.add(getBeanItems(bottomStr, jCbean.realRate3, 2, jCbean.id));

                            break;
                        } else {
                            oneRowData.add(getBeanItems(topStr, jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems(bottomStr, jCbean.realRate3, 2, jCbean.id));
                        }
                    }
                }
                if (oneRowData.size() == 1) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 1) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (threeRowData.size() == 1) {
                    threeRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (fourRowData.size() == 1) {
                    fourRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                //大小的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 3 && currScoreBean.id == jCbean.matchID) {
                        if (oneRowData.size() == 3 && twoRowData.size() == 3) {
                            threeRowData.add(getBeanItems("大" + jCbean.realRate2, jCbean.realRate1, 1, jCbean.id));

                            fourRowData.add(getBeanItems("小" + jCbean.realRate2, jCbean.realRate3, 2, jCbean.id));

                            break;
                        } else {
                            oneRowData.add(getBeanItems("大" + jCbean.realRate2, jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems("小" + jCbean.realRate2, jCbean.realRate3, 2, jCbean.id));
                        }
                    }
                }
                if (oneRowData.size() == 2) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 2) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (threeRowData.size() == 2) {
                    threeRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (fourRowData.size() == 2) {
                    fourRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                //球队得分 大/小 的数据
                for (int j = 0; j < items.size(); j++) {
                    JCbean jCbean = items.get(j);

                    if (jCbean.payTypeID == 48) {//主
                        if (currScoreBean.id == jCbean.matchID) {
                            oneRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id));

                            twoRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id));
                        }
                    }
                    if (jCbean.payTypeID == 49) {//客队
                        if (currScoreBean.id == jCbean.matchID) {
                            threeRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id));

                            fourRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id));
                        }
                    }
                }

                if (oneRowData.size() == 3) {
                    oneRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (twoRowData.size() == 3) {
                    twoRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (threeRowData.size() == 3) {
                    threeRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }
                if (fourRowData.size() == 3) {
                    fourRowData.add(new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem());
                }

                scrollBeanItems.addAll(oneRowData);
                scrollBeanItems.addAll(twoRowData);
                scrollBeanItems.addAll(threeRowData);
                scrollBeanItems.addAll(fourRowData);

                item.setTestItems(scrollBeanItems);

                beanItems.add(item);
            }
        }
        return beanItems;
    }

    private ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem getBeanItems(String leftStr, String rightStr, int selected, int id) {
        ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem item = new ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem();
        item.setSelected(selected);
        item.setId(id);

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
        if (id == NotificationController.scroll_mask) {
            if (args != null && args.length >= 1) {
                if ("child_close".equals(args[0])) {
                    if (commitMenuView != null && commitMenuView.getVisibility() == View.VISIBLE) {
                        commitMenuView.hide(false);
                    }
                }
            }
        } else if (id == NotificationController.basketBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    selectMatchDialog.show(DataController.Companion.getInstance().getBaskteBallData(), DataController.Companion.getInstance().getBasketBallHotData(), "联赛选择");
                }
            }
        }
    }

    public static class MergeBean implements Serializable {

        private static final long serialVersionUID = -3943234106674174323L;

        private List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> bean;
        private ScrollBallBasketBallBean.ScrollBaksetBallItems items;
        private boolean isHome;
        private String title;

        public boolean isHome() {
            return isHome;
        }

        public void setHome(boolean home) {
            isHome = home;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> getBean() {
            return bean;
        }

        public void setBean(List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> bean) {
            this.bean = bean;
        }

        public ScrollBallBasketBallBean.ScrollBaksetBallItems getItems() {
            return items;
        }

        public void setItems(ScrollBallBasketBallBean.ScrollBaksetBallItems items) {
            this.items = items;
        }
    }
}
