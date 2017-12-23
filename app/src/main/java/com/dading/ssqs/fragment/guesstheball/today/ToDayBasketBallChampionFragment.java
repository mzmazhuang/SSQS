package com.dading.ssqs.fragment.guesstheball.today;

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
import com.dading.ssqs.adapter.newAdapter.MyChampionAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Champion;
import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.cells.ChampionChildCell;
import com.dading.ssqs.cells.GuessFilterCell;
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
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by mazhuang on 2017/12/8.
 * 今日-篮球-冠军
 */

public class ToDayBasketBallChampionFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "ToDayFootBallChampionFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private MyChampionAdapter adapter;
    private LoadingDialog loadingDialog;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private GuessFilterCell filterCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;
    private ImageView defaultView;

    private int offset = 1;
    private int limit = 10;

    private int totalPage;

    private boolean isRefresh = false;

    private int leagueMatchId = -1;//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private List<ToDayFootBallChampionFragment.MergeBean> leagusList = new ArrayList<>();

    private String currTitle = "";

    private String filter_str = "按时间顺序";

    private int sType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.today_mask);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (filterCell != null) {
            filterCell.destoryRunnable(false);
        }

        NotificationController.getInstance().removeObserver(this, NotificationController.today_mask);
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
        filterCell.setSecondRefresh(180);
        filterCell.setRefreshListener(new GuessFilterCell.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
        filterCell.hideSelectMatch();
        filterCell.setFilterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterDialog == null) {
                    filterDialog = new FilterDialog(mContext);
                    filterDialog.setItemListener(new FilterDialogAdapter.OnClickListener() {
                        @Override
                        public void onClick(String title) {
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

        adapter = new MyChampionAdapter(mContext);
        adapter.setListener(clickListener);
        mRecyclerView.setAdapter(adapter);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle(currTitle);
                    commitMenuView.setChampionData(leagusList);
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
            List<ChampionBean.ChampionItems.ChampionItem> list = leagusList.get(i).getBean();

            for (int j = 0; j < list.size(); j++) {
                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                bean.type = 3;
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
                    Iterator<ToDayFootBallChampionFragment.MergeBean> iterator = leagusList.iterator();

                    while (iterator.hasNext()) {
                        ToDayFootBallChampionFragment.MergeBean mergeBean = iterator.next();

                        List<ChampionBean.ChampionItems.ChampionItem> items = mergeBean.getBean();

                        if (mergeBean.getItems().getLeagueId() == dataId) {
                            Iterator<ChampionBean.ChampionItems.ChampionItem> itemIterator = items.iterator();

                            while (itemIterator.hasNext()) {
                                ChampionBean.ChampionItems.ChampionItem beanItem = itemIterator.next();

                                if (beanItem.getId() == itemId && beanItem.getRightStr().equals(value)) {
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

    private ChampionChildCell.OnClickListener clickListener = new ChampionChildCell.OnClickListener() {
        @Override
        public boolean onClick(ChampionBean.ChampionItems.ChampionItem bean, ChampionBean.ChampionItems items, boolean isAdd, int position, String title) {
            if (leagueMatchId == -1 || leagueMatchId == bean.getLeagueId()) {
                leagueMatchId = bean.getLeagueId();
                currTitle = title;

                if (isAdd) {
                    if (leagusList.size() == 0) {
                        ToDayFootBallChampionFragment.MergeBean mergeBean = new ToDayFootBallChampionFragment.MergeBean();
                        mergeBean.setItems(items);
                        mergeBean.setTitle(items.getTitle());

                        List<ChampionBean.ChampionItems.ChampionItem> beanItems = new ArrayList<>();
                        bean.setPosition(position);
                        beanItems.add(bean);

                        mergeBean.setBean(beanItems);

                        leagusList.add(mergeBean);
                    } else {
                        boolean isNew = true;//是否添加新的数据
                        for (int i = 0; i < leagusList.size(); i++) {
                            ToDayFootBallChampionFragment.MergeBean mergeBean = leagusList.get(i);

                            if (mergeBean.getItems().getLeagueId() == items.getLeagueId()) {//一个比赛下 不同的item
                                isNew = false;

                                List<ChampionBean.ChampionItems.ChampionItem> beanItems = mergeBean.getBean();
                                bean.setPosition(position);
                                beanItems.add(bean);

                                mergeBean.setBean(beanItems);
                                break;
                            }
                        }

                        if (isNew) {
                            ToDayFootBallChampionFragment.MergeBean mergeBean = new ToDayFootBallChampionFragment.MergeBean();
                            mergeBean.setItems(items);
                            mergeBean.setTitle(items.getTitle());

                            List<ChampionBean.ChampionItems.ChampionItem> beanItems = new ArrayList<>();
                            bean.setPosition(position);
                            beanItems.add(bean);

                            mergeBean.setBean(beanItems);

                            leagusList.add(mergeBean);
                        }
                    }
                } else {
                    Iterator<ToDayFootBallChampionFragment.MergeBean> mergeBeanIterator = leagusList.iterator();

                    while (mergeBeanIterator.hasNext()) {
                        ToDayFootBallChampionFragment.MergeBean mergeBean = mergeBeanIterator.next();

                        if (mergeBean.getItems().getLeagueId() == items.getLeagueId()) {
                            List<ChampionBean.ChampionItems.ChampionItem> beanItems = mergeBean.getBean();

                            Iterator<ChampionBean.ChampionItems.ChampionItem> beanItemIterator = beanItems.iterator();
                            while (beanItemIterator.hasNext()) {
                                ChampionBean.ChampionItems.ChampionItem beanItem = beanItemIterator.next();
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
        SSQSApplication.apiClient(0).getChampionList(2, sType, off, lim, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                loadingDialog.dismiss();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {

                    CcApiResult.ResultChampionPage page = (CcApiResult.ResultChampionPage) result.getData();

                    if (page != null && page.getItems() != null && page.getItems().size() >= 1) {
                        defaultView.setVisibility(View.GONE);

                        filterCell.beginRunnable(true);

                        totalPage = page.getTotalCount();

                        filterCell.setTotalPage(totalPage);
                        filterCell.setCurrPage(off);

                        adapter.setList(getData(page.getItems()));
                    } else {
                        adapter.clear();

                        filterCell.destoryRunnable(true);

                        defaultView.setVisibility(View.VISIBLE);
                    }

                    isRefresh = false;
                } else {
                    adapter.clear();

                    filterCell.destoryRunnable(true);

                    defaultView.setVisibility(View.VISIBLE);

                    TmtUtils.midToast(mContext, result.getMessage(), 1000);
                }
            }
        });
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ChampionBean> getData(List<Champion> data) {
        List<CommonTitle> list = getFilterData(data);

        List<ChampionBean> items = new ArrayList<>();

        if (data != null && data.size() >= 1) {

            for (int i = 0; i < list.size(); i++) {
                ChampionBean bean = new ChampionBean();
                bean.setCommonTitle(list.get(i));

                List<ChampionBean.ChampionItems> championItems = new ArrayList<>();

                List<Champion.ChampionItem> championItems1 = data.get(0).getListSeason();//二级标题
                List<Champion.TeamRate> teamRates = data.get(0).getListTeamRate();//value

                if (championItems1 != null) {

                    for (int j = 0; j < championItems1.size(); j++) {
                        Champion.ChampionItem championItem = championItems1.get(j);

                        if (list.get(i).getTitle().equals(championItem.getLeagueName())) {
                            ChampionBean.ChampionItems championData = new ChampionBean.ChampionItems();
                            championData.setLeagueId(championItem.getLeagueId());
                            championData.setTitle(championItem.getMatchName());

                            List<ChampionBean.ChampionItems.ChampionItem> testItems = new ArrayList<>();

                            for (int k = 0; k < teamRates.size(); k++) {
                                Champion.TeamRate teamRate = teamRates.get(k);

                                if (list.get(i).getTitle().equals(teamRate.getLeagueName()) && championItem.getMatchName().equals(teamRate.getMatchName())) {
                                    ChampionBean.ChampionItems.ChampionItem item = new ChampionBean.ChampionItems.ChampionItem();
                                    if (testItems.size() < 16) {
                                        item.setId(teamRate.getId());
                                        item.setLeagueId(teamRate.getLeagueId());
                                        item.setLeftStr(teamRate.getTeamName());
                                        item.setRightStr(teamRate.getPayRate());
                                        testItems.add(item);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            championData.setItems(testItems);
                            championItems.add(championData);
                        }
                    }

                    bean.setItems(championItems);

                    items.add(bean);
                }
            }
        }

        return items;
    }

    //获取筛选完的数据
    private List<CommonTitle> getFilterData(List<Champion> items) {
        List<CommonTitle> list = new ArrayList<>();

        List<Champion.Leagus> leaguses = items.get(0).getListLeague();

        if (leaguses != null) {
            for (int i = 0; i < leaguses.size(); i++) {
                if (list.size() > 0) {
                    boolean isAdd = true;
                    for (int j = 0; j < list.size(); j++) {
                        CommonTitle commonTitle = list.get(j);
                        if (commonTitle.getTitle().equals(leaguses.get(i).getLeagueName())) {
                            isAdd = false;
                            break;
                        }
                    }

                    if (isAdd) {
                        CommonTitle title = new CommonTitle();
                        title.setTitle(leaguses.get(i).getLeagueName());

                        list.add(title);
                    }
                } else {
                    CommonTitle title = new CommonTitle();
                    title.setTitle(leaguses.get(i).getLeagueName());

                    list.add(title);
                }
            }
        }
        return list;
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            getNetDataWork(offset, limit);
        }
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.today_mask) {
            if (args != null && args.length >= 1) {
                if ("child_close".equals(args[0])) {
                    if (commitMenuView != null && commitMenuView.getVisibility() == View.VISIBLE) {
                    }
                    commitMenuView.hide(false);
                }
            }
        }
    }
}
