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
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallTotalAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCScorebean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.bean.ScrollBallFootBallTotalBean;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.cells.TotalItemCell;
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
 * Created by mazhuang on 2017/12/7.
 * 滚球-足球-总入球
 */

public class ScrollBallTotalFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "ScrollBallTotalFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ScrollBallTotalAdapter adapter;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private SelectMatchDialog selectMatchDialog;
    private LoadingDialog loadingDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private ImageView defaultView;

    private int offset = 1;
    private int limit = 10;
    private boolean isRefresh = false;

    private int totalPage;

    private int sType = 0;

    private List<ScoreBean> originalData;//原始数据

    private ScrollBallFootBallTotalBean currFootBallTotalBean = null;
    private List<ScrollBallFootBallTotalBean> newData = new ArrayList<>();
    private List<ScoreBean> networkData = new ArrayList<>();

    private int leagueMatchId = -1;//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private List<MergeBean> leagusList = new ArrayList<>();

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter);
        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (filterCell != null) {
            filterCell.destoryRunnable(false);
        }

        NotificationController.getInstance().removeObserver(this, NotificationController.footBallFilter);
        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask);
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
                if (DataController.getInstance().getFootBallData() == null) {
                    DataController.getInstance().syncFootBall(TAG, 6);
                    loadingDialog.show();
                } else {
                    selectMatchDialog.show(DataController.getInstance().getFootBallData(), DataController.getInstance().getFootBallHotData(), "联赛选择");
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

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swiptToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        RecyclerScrollview scrollview = (RecyclerScrollview) view.findViewById(R.id.swipe_target);

        mRecyclerView = new RecyclerView(mContext);
        scrollview.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new ScrollBallTotalAdapter(mContext);
        adapter.setReadyListener(readyListener);
        adapter.setListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("滚球-足球");
                    commitMenuView.setTotalData(leagusList);
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

    private TotalItemCell.OnItemClickListener itemClickListener = new TotalItemCell.OnItemClickListener() {
        @Override
        public boolean onClick(ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems items, ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem bean, int id, boolean isAdd, int position) {
            if (leagueMatchId == id || leagueMatchId == -1) {
                leagueMatchId = id;

                if (isAdd) {//是添加 还是删除
                    if (leagusList.size() == 0) {
                        MergeBean mergeBean = new MergeBean();
                        mergeBean.setItems(items);

                        for (int i = 0; i < originalData.size(); i++) {
                            if (originalData.get(i).id == id) {
                                mergeBean.setTitle(originalData.get(i).leagueName);
                                break;
                            }
                        }

                        List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItems = new ArrayList<>();
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

                                List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItems = mergeBean.getBean();
                                bean.setPosition(position);
                                beanItems.add(bean);

                                mergeBean.setBean(beanItems);
                                break;
                            }
                        }

                        if (isNew) {
                            MergeBean mergeBean = new MergeBean();
                            mergeBean.setItems(items);

                            for (int i = 0; i < originalData.size(); i++) {
                                if (originalData.get(i).id == id) {
                                    mergeBean.setTitle(originalData.get(i).leagueName);
                                    break;
                                }
                            }

                            List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItems = new ArrayList<>();
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
                            List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItems = mergeBean.getBean();

                            Iterator<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItemIterator = beanItems.iterator();
                            while (beanItemIterator.hasNext()) {
                                ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem beanItem = beanItemIterator.next();
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
            List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> list = leagusList.get(i).getBean();

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
                    Iterator<MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        MergeBean mergeBean = iterator.next();

                        List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> items = mergeBean.getBean();

                        if (mergeBean.getItems().getId() == itemId) {
                            Iterator<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> itemIterator = items.iterator();

                            while (itemIterator.hasNext()) {
                                ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem beanItem = itemIterator.next();

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
                ScrollBallFootBallTotalBean temporaryBean = newData.get(i);
                if (temporaryBean.getTitle().getTitle().equals(title)) {
                    currFootBallTotalBean = temporaryBean;
                    break;
                }
            }

            //把子数据添加进去

            if (currFootBallTotalBean != null) {
                String params = "";
                for (int i = 0; i < networkData.size(); i++) {
                    params += networkData.get(i).id + ",";
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
                                        currFootBallTotalBean.setItems(handleOtherData(items, networkData));

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
        String mDate = DateUtils.getCurTime("yyyyMMdd");

        SSQSApplication.apiClient(0).getScrollBallList(true, 6, mDate, sType, leagueIDs, off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                    if (page != null && page.getItems() != null && page.getItems().size() >= 1) {
                        defaultView.setVisibility(View.GONE);

                        filterCell.beginRunnable(true);

                        originalData = page.getItems();

                        totalPage = page.getTotalCount();

                        filterCell.setTotalPage(totalPage);
                        filterCell.setCurrPage(off);

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


    private List<ScrollBallFootBallTotalBean> getData(List<CommonTitle> list) {
        List<ScrollBallFootBallTotalBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ScrollBallFootBallTotalBean bean = new ScrollBallFootBallTotalBean();
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
            getNetDataWork(offset, limit);
        }
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems> handleOtherData(List<JCScorebean> items, List<ScoreBean> currData) {
        List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems> beanItems2 = new ArrayList<>();

        //总入球数据
        for (int i = 0; i < currData.size(); i++) {
            ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems item = new ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems();
            item.setId(currData.get(i).id);
            item.setTitle(currData.get(i).home);
            item.setByTitle(currData.get(i).away);

            //硬编码
            String time = currData.get(i).openTime;
            if (time.length() == 19) {
                time = time.substring(11, time.length() - 3);
            }

            item.setTime(time);

            List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> testItems = new ArrayList<>();

            List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> data = new ArrayList<>();
            data.add(new ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem(currData.get(i).home + currData.get(i).away));

            if (items.size() != 0) {
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).type == 1) {//总入球
                        List<JCScorebean.ListEntity> entityList = items.get(j).list;
                        for (int k = 0; k < entityList.size(); k++) {
                            List<JCScorebean.ListEntity.ItemsEntity> itemsEntities = entityList.get(k).items;
                            for (int l = 0; l < itemsEntities.size(); l++) {
                                if (currData.get(i).id == itemsEntities.get(l).matchID) {
                                    data.add(new ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem(itemsEntities.get(l).id, itemsEntities.get(l).payRate));
                                }
                            }
                        }
                    }
                }

                if (data.size() < 5) {
                    for (int j = data.size(); j < 5; j++) {
                        data.add(new ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem(""));
                    }
                }

                testItems.addAll(data);

                item.setItemList(testItems);
                beanItems2.add(item);
            }
        }

        return beanItems2;
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.scroll_mask) {
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
                    selectMatchDialog.show(DataController.getInstance().getFootBallData(), DataController.getInstance().getFootBallHotData(), "联赛选择");
                }
            }
        }
    }

    public static class MergeBean implements Serializable {

        private static final long serialVersionUID = -3943234106674174323L;

        private List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> bean;
        private ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems items;
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

        public List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> getBean() {
            return bean;
        }

        public void setBean(List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> bean) {
            this.bean = bean;
        }

        public ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems getItems() {
            return items;
        }

        public void setItems(ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems items) {
            this.items = items;
        }
    }
}
