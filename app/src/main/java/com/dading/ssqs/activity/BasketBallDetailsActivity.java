package com.dading.ssqs.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.BasketBallDetailsItemAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusMatchElement;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.BasketBallLastBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.cells.BasketBallDetailsHeadCell;
import com.dading.ssqs.cells.BasketBallDetailsItemCell;
import com.dading.ssqs.cells.BasketBallItemCell;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.RecyclerScrollview;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.components.ScrollBallCommitView;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 */

public class BasketBallDetailsActivity extends BaseActivity implements OnRefreshListener {

    private Context mContext;

    private SwipeToLoadLayout swipeToLoadLayout;
    private BasketBallDetailsHeadCell headCell;

    private BasketBallItemCell myItemCell;
    private BasketBallItemCell mainItemCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;

    private LoadingDialog loadingDialog;

    private ScoreBean bean;
    private int matchID;
    private String matchTitle;

    private boolean mainRefresh = false;
    private boolean myRefresh = false;
    private boolean basketRefresh = false;

    private boolean isRefresh = false;

    private List<BasketData.BasketItemData> mainDatas = new ArrayList<>();
    private List<BasketData.BasketItemData> myDatas = new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headCell != null && headCell.getStartStatus()) {
            headCell.destoryRunnable(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (headCell != null && headCell.getStartStatus()) {
            headCell.beginRunnable();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (headCell != null && headCell.getStartStatus()) {
            headCell.destoryRunnable(false);
        }
    }

    @Override
    protected View getContentView() {
        mContext = this;

        RelativeLayout container = new RelativeLayout(mContext);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setBackgroundColor(Color.WHITE);
        container.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        TitleCell titleCell = new TitleCell(this, getResources().getString(R.string.match_section));
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        contentLayout.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        View view = View.inflate(mContext, R.layout.fragment_scrollball, null);
        contentLayout.addView(view);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        RecyclerScrollview scrollview = (RecyclerScrollview) view.findViewById(R.id.swipe_target);

        LinearLayout infoLayout = new LinearLayout(mContext);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        scrollview.addView(infoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        headCell = new BasketBallDetailsHeadCell(mContext);
        headCell.setRefreshCount(30);
        headCell.setListener(new GuessFilterCell.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
        infoLayout.addView(headCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 8));

        myItemCell = new BasketBallItemCell(mContext, 1);
        myItemCell.setOnItemClickListener(myListener);
        myItemCell.setListener(myClickListener);
        infoLayout.addView(myItemCell);

        mainItemCell = new BasketBallItemCell(mContext, 2);
        mainItemCell.setOnItemClickListener(mainListener);
        mainItemCell.setListener(mainClickListener);
        infoLayout.addView(mainItemCell);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("滚球-篮球-" + bean == null ? "" : bean.leagueName);
                    if (myDatas.size() > 0) {
                        commitMenuView.setBasketDetailsData(myDatas);
                    } else {
                        commitMenuView.setBasketDetailsData(mainDatas);
                    }
                    commitMenuView.show();
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

                if (myDatas.size() > 0) {
                    myDatas.clear();
                    myItemCell.refreshData();
                } else if (mainDatas.size() > 0) {
                    mainDatas.clear();
                    mainItemCell.refreshData();
                }

            }
        });
        container.addView(commitView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM));

        commitMenuView = new ScrollBallCommitMenuView(mContext, LocaleController.getString(R.string.betting_slips), LocaleController.getString(R.string.latest_ten_transactions));
        commitMenuView.setMenuItemDeleteListener(menuClickListener);
        commitMenuView.setMenuListener(new ScrollBallCommitMenuView.OnCommitMenuListener() {
            @Override
            public void onClear() {
                commitView.setVisibility(View.GONE);

                if (myDatas.size() > 0) {
                    myDatas.clear();
                    myItemCell.refreshData();
                } else if (mainDatas.size() > 0) {
                    mainDatas.clear();
                    mainItemCell.refreshData();
                }
            }

            @Override
            public void onHide() {

            }

            @Override
            public void onDone() {
                pay();
            }
        });
        commitMenuView.setVisibility(View.GONE);
        container.addView(commitMenuView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        init();
        return container;
    }

    private ScrollBallCommitMenuAdapter.OnMenuClickListener menuClickListener = new ScrollBallCommitMenuAdapter.OnMenuClickListener() {
        @Override
        public void onClick(int position, int dataId, final int itemId, final String value) {
            commitMenuView.changeData(position);

            if (myDatas.size() > 0) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<BasketData.BasketItemData> iterator = myDatas.iterator();

                        while (iterator.hasNext()) {
                            BasketData.BasketItemData bean = iterator.next();
                            if (bean.getId() == itemId && bean.getNumber().equals(value)) {
                                iterator.remove();
                                break;
                            }
                        }

                        SSQSApplication.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                myItemCell.setFocus(myDatas);
                                myItemCell.refreshData();
                            }
                        });
                    }
                });
                SSQSApplication.cachedThreadPool.execute(thread);
            } else if (mainDatas.size() > 0) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<BasketData.BasketItemData> iterator = mainDatas.iterator();

                        while (iterator.hasNext()) {
                            BasketData.BasketItemData bean = iterator.next();
                            if (bean.getId() == itemId) {
                                iterator.remove();
                                break;
                            }
                        }

                        SSQSApplication.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mainItemCell.setFocus(mainDatas);
                                mainItemCell.refreshData();
                            }
                        });
                    }
                });
                SSQSApplication.cachedThreadPool.execute(thread);
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

        commitMenuView.hide(true);
        commitView.setVisibility(View.GONE);

        loadingDialog.show();

        PayBallElement element = new PayBallElement();

        List<PayBallElement.BetBean> items = new ArrayList<>();

        if (mainDatas.size() > 0) {
            mainItemCell.refreshData();

            for (int i = 0; i < mainDatas.size(); i++) {
                BasketData.BasketItemData list = mainDatas.get(i);

                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                if (mainDatas.get(i).getTitle().contains("最后一位")) {
                    bean.type = 3;
                    items.add(bean);
                } else {
                    bean.type = 2;
                    bean.selected = list.getSelected();
                    items.add(bean);
                }
                bean.matchID = list.getMatchID();
                bean.payRateID = list.getId();
                bean.amount = moneyLists.get(i).getMoney();
            }
            mainDatas.clear();
        } else if (myDatas.size() > 0) {
            myItemCell.refreshData();

            for (int i = 0; i < myDatas.size(); i++) {
                BasketData.BasketItemData list = myDatas.get(i);

                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                if (myDatas.get(i).getTitle().contains("最后一位")) {
                    bean.type = 3;
                    items.add(bean);
                } else {
                    bean.type = 2;
                    bean.selected = list.getSelected();
                    items.add(bean);
                }
                bean.matchID = list.getMatchID();
                bean.payRateID = list.getId();
                bean.amount = moneyLists.get(i).getMoney();
            }
            myDatas.clear();
        }

        element.setItems(items);

        if (items.size() > 0) {
            basketBallPay(element);
        }
    }

    private void basketBallPay(PayBallElement element) {
        SSQSApplication.apiClient(classGuid).payBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    loadingDialog.dismiss();
                    Toast.makeText(mContext, "下注成功", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.dismiss();
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

    private BasketBallDetailsItemCell.OnItemClickListener myClickListener = new BasketBallDetailsItemCell.OnItemClickListener() {
        @Override
        public boolean onClick(BasketData.BasketItemData data, BasketData basketData, boolean isAdd) {
            if (mainDatas.size() == 0) {

                if (isAdd) {
                    data.setTitle(basketData.getTitle());
                    data.setHomeName(bean == null ? "" : bean.home);
                    data.setAwayName(bean == null ? "" : bean.away);

                    myDatas.add(data);
                } else {
                    myDatas.remove(data);
                }

                if (myDatas.size() > 0) {
                    commitView.setCount(1);
                    commitView.setVisibility(View.VISIBLE);
                } else {
                    commitView.setVisibility(View.GONE);
                }

                return true;
            } else {
                return false;
            }
        }
    };

    private BasketBallDetailsItemCell.OnItemClickListener mainClickListener = new BasketBallDetailsItemCell.OnItemClickListener() {
        @Override
        public boolean onClick(BasketData.BasketItemData data, BasketData basketData, boolean isAdd) {
            if (myDatas.size() == 0) {

                if (isAdd) {
                    data.setTitle(basketData.getTitle());
                    data.setHomeName(bean == null ? "" : bean.home);
                    data.setAwayName(bean == null ? "" : bean.away);

                    mainDatas.add(data);
                } else {
                    mainDatas.remove(data);
                }

                if (mainDatas.size() > 0) {
                    commitView.setCount(1);
                    commitView.setVisibility(View.VISIBLE);
                } else {
                    commitView.setVisibility(View.GONE);
                }

                return true;
            } else {
                return false;
            }
        }
    };

    private BasketBallDetailsItemAdapter.OnItemClickListener myListener = new BasketBallDetailsItemAdapter.OnItemClickListener() {
        @Override
        public void onClick(final BasketData bean) {
            loadingDialog.show();

            FocusMatchElement element = new FocusMatchElement();
            element.setStatus(0);

            int payTypeID = 0;
            String str = "";

            List<Integer> ids = new ArrayList<>();

            for (int i = 0; i < bean.getItems().size(); i++) {
                if (ids.size() == 0) {
                    ids.add(bean.getItems().get(i).getId());
                } else {
                    boolean isAdd = true;
                    for (int j = 0; j < ids.size(); j++) {
                        if (ids.get(j) == bean.getItems().get(i).getId()) {
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd) {
                        ids.add(bean.getItems().get(i).getId());
                    }
                }
                payTypeID = bean.getItems().get(i).getPayTypeID();
            }

            for (int i = 0; i < ids.size(); i++) {
                str += ids.get(i) + ",";
            }

            if (str.length() >= 1) {
                str = str.substring(0, str.length() - 1);
            }

            element.setPayRateBallID(str);
            element.setPayTypeID(payTypeID);

            SSQSApplication.apiClient(classGuid).focusMatch(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    loadingDialog.dismiss();

                    if (result.isOk()) {
                        bean.setLike(false);
                        List<BasketData> newData = new ArrayList<>();
                        newData.addAll(myItemCell.getItem());

                        Iterator<BasketData> iterator = newData.iterator();

                        while (iterator.hasNext()) {
                            BasketData data = iterator.next();
                            if (data.getId() == bean.getId()) {
                                iterator.remove();
                                break;
                            }
                        }

                        if (newData.size() == 0) {
                            myItemCell.resetArrow();
                        }

                        myItemCell.setData(newData);
                        myItemCell.setNumber(newData.size());

                        List<BasketData> mainData = new ArrayList<>();
                        mainData.addAll(mainItemCell.getItem());

                        for (int i = 0; i < mainData.size(); i++) {
                            if (mainData.get(i).getId() == bean.getId()) {
                                mainData.get(i).setLike(false);
                                break;
                            }
                        }

                        mainItemCell.setData(mainData);
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
    };

    private BasketBallDetailsItemAdapter.OnItemClickListener mainListener = new BasketBallDetailsItemAdapter.OnItemClickListener() {
        @Override
        public void onClick(final BasketData bean) {
            loadingDialog.show();

            FocusMatchElement element = new FocusMatchElement();
            element.setStatus(bean.isLike == true ? 0 : 1);

            int payTypeID = 0;
            String str = "";

            List<Integer> ids = new ArrayList<>();

            for (int i = 0; i < bean.getItems().size(); i++) {
                if (ids.size() == 0) {
                    ids.add(bean.getItems().get(i).getId());
                } else {
                    boolean isAdd = true;
                    for (int j = 0; j < ids.size(); j++) {
                        if (ids.get(j) == bean.getItems().get(i).getId()) {
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd) {
                        ids.add(bean.getItems().get(i).getId());
                    }
                }
                payTypeID = bean.getItems().get(i).getPayTypeID();
            }

            for (int i = 0; i < ids.size(); i++) {
                str += ids.get(i) + ",";
            }

            if (str.length() >= 1) {
                str = str.substring(0, str.length() - 1);
            }

            element.setPayRateBallID(str);
            element.setPayTypeID(payTypeID);

            SSQSApplication.apiClient(classGuid).focusMatch(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    loadingDialog.dismiss();

                    if (result.isOk()) {
                        if (!bean.isLike()) {
                            bean.setLike(true);
                            List<BasketData> newData = new ArrayList<>();
                            if (myItemCell.getItem().size() != 0) {
                                newData.addAll(myItemCell.getItem());
                            }
                            newData.add(bean);
                            myItemCell.setData(newData);
                            myItemCell.setNumber(newData.size());
                        } else {
                            bean.setLike(false);
                            List<BasketData> newData = new ArrayList<>();
                            newData.addAll(myItemCell.getItem());

                            Iterator<BasketData> iterator = newData.iterator();

                            while (iterator.hasNext()) {
                                BasketData data = iterator.next();
                                if (data.getId() == bean.getId()) {
                                    iterator.remove();
                                    break;
                                }
                            }

                            if (newData.size() == 0) {
                                myItemCell.resetArrow();
                            }

                            myItemCell.setData(newData);
                            myItemCell.setNumber(newData.size());
                        }
                        mainItemCell.refreshData();
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
    };

    private void init() {
        Intent intent = getIntent();
        matchID = intent.getIntExtra("data_id", 0);
        matchTitle = intent.getStringExtra("data_title");

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }
        loadingDialog.show();

        getBasketBallHeadInfo(matchID);
    }

    private void getBasketBallHeadInfo(int matchId) {
        SSQSApplication.apiClient(classGuid).getBasketBallHeadInfo(matchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    bean = (ScoreBean) result.getData();
                    if (bean != null) {
                        headCell.setTitle(matchTitle);

                        String time = bean.part1Time;
                        headCell.setTime(time);

                        String str = "";

                        if ("0".equals(bean.isOverTime)) {
                            str = "第一节";
                        } else if ("4".equals(bean.isOverTime)) {
                            str = "第二节";
                        } else if ("5".equals(bean.isOverTime)) {
                            str = "第三节";
                        } else if ("6".equals(bean.isOverTime)) {
                            str = "第四节";
                        } else if ("7".equals(bean.isOverTime)) {
                            str = "加时赛";
                        }

                        headCell.setSection(str);

                        headCell.setHomeTeamInfo(bean.home, bean.part1HScore, bean.part2HScore, bean.part3HScore, bean.part4HScore, bean.hOverTimeScore, bean.hHalfScore, bean.hSHalfScore, bean.homeScore);
                        headCell.setvisitingTeamInfo(bean.away, bean.part1AScore, bean.part2AScore, bean.part3AScore, bean.part4AScore, bean.aOverTimeScore, bean.aHalfScore, bean.aSHalfScore, bean.awayScore);

                        basketRefresh = true;

                        getBasketBallInfo(bean.id);
                        getMyBasketBallInfo(bean.id);
                    }
                } else

                {
                    basketRefresh = true;

                    checkRefresh();
                }
            }
        });
    }

    //我的盘口信息
    private void getMyBasketBallInfo(final int matchId) {
        SSQSApplication.apiClient(classGuid).getMatchMyBasketBallResult(matchId, "1", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> items = (List<JCbean>) result.getData();

                    getMyLastData(items, matchId);
                } else {
                    myRefresh = true;

                    checkRefresh();
                }
            }
        });
    }

    //我的盘口 最后一位
    private void getMyLastData(final List<JCbean> items, int matchID) {
        SSQSApplication.apiClient(classGuid).getMatchMyBasketLastResult(matchID, "1", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<BasketBallLastBean> bean = (List<BasketBallLastBean>) result.getData();

                    setData(items, bean, false);
                } else {
                    myRefresh = true;

                    checkRefresh();
                }
            }
        });
    }

    //主盘口的数据
    private void getBasketBallInfo(final int matchId) {
        SSQSApplication.apiClient(classGuid).getMatchBasketBallResult(matchId + "", "0", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> items = (List<JCbean>) result.getData();

                    getLastData(items, matchId);
                } else {
                    isRefresh = false;
                    loadingDialog.dismiss();
                    swipeToLoadLayout.setRefreshing(false);
                    swipeToLoadLayout.setRefreshEnabled(true);

                    ToastUtils.midToast(mContext, result.getMessage(), 1000);
                }
            }
        });
    }

    //主盘口的最后一位
    private void getLastData(final List<JCbean> items, int matchID) {
        SSQSApplication.apiClient(classGuid).getMatchBasketLastResult(matchID, "0", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<BasketBallLastBean> bean = (List<BasketBallLastBean>) result.getData();

                    setData(items, bean, true);
                } else {
                    isRefresh = false;
                    loadingDialog.dismiss();
                    swipeToLoadLayout.setRefreshing(false);
                    swipeToLoadLayout.setRefreshEnabled(true);

                    ToastUtils.midToast(mContext, result.getMessage(), 1000);
                }
            }
        });
    }

    private void setData(final List<JCbean> items, final List<BasketBallLastBean> lastBean, final boolean isMain) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<BasketData> list = getFilterData(items, lastBean);

                SSQSApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (isMain) {
                            mainRefresh = true;
                            mainItemCell.setData(list);
                            mainItemCell.setNumber(list.size());
                        } else {
                            myRefresh = true;
                            myItemCell.setData(list);
                            myItemCell.setNumber(list.size());
                        }
                        checkRefresh();
                    }
                });
            }
        });
        SSQSApplication.cachedThreadPool.execute(thread);
    }

    private void checkRefresh() {
        if (mainRefresh && myRefresh && basketRefresh) {
            loadingDialog.dismiss();
            swipeToLoadLayout.setRefreshing(false);
            swipeToLoadLayout.setRefreshEnabled(true);

            headCell.beginRunnable();

            if (myItemCell.getItem() != null && myItemCell.getItem().size() >= 1) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<BasketData> mainDatas = mainItemCell.getItem();
                        List<BasketData> myDatas = myItemCell.getItem();

                        for (int i = 0; i < myDatas.size(); i++) {
                            for (int j = 0; j < mainDatas.size(); j++) {
                                if (myDatas.get(i).getId() == mainDatas.get(j).getId()) {
                                    mainDatas.get(j).setLike(true);
                                    break;
                                }
                            }
                        }
                        SSQSApplication.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mainItemCell.refreshData();
                            }
                        });
                    }
                });
                SSQSApplication.cachedThreadPool.execute(thread);
            }

            isRefresh = false;
            mainRefresh = false;
            myRefresh = false;
            basketRefresh = false;
        }
    }

    public static class BasketData {
        private int id;
        private String title;
        private boolean isLike;
        private List<BasketItemData> items;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<BasketItemData> getItems() {
            return items;
        }

        public void setItems(List<BasketItemData> items) {
            this.items = items;
        }

        public static class BasketItemData {
            private int id;
            private String leftStr;
            private String number;
            private String rightStr;
            private int payTypeID;
            private String title;
            private String homeName;
            private String awayName;
            private int matchID;
            private int selected;
            private String tagName;

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }

            public String getHomeName() {
                return homeName;
            }

            public void setHomeName(String homeName) {
                this.homeName = homeName;
            }

            public String getAwayName() {
                return awayName;
            }

            public void setAwayName(String awayName) {
                this.awayName = awayName;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLeftStr() {
                return leftStr;
            }

            public void setLeftStr(String leftStr) {
                this.leftStr = leftStr;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getRightStr() {
                return rightStr;
            }

            public void setRightStr(String rightStr) {
                this.rightStr = rightStr;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPayTypeID() {
                return payTypeID;
            }

            public void setPayTypeID(int payTypeID) {
                this.payTypeID = payTypeID;
            }

            public int getMatchID() {
                return matchID;
            }

            public void setMatchID(int matchID) {
                this.matchID = matchID;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            getBasketBallHeadInfo(matchID);
        }
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<BasketData> getFilterData(List<JCbean> items, List<BasketBallLastBean> basketBallLastBeans) {
        List<BasketData> data = new ArrayList<>();

        BasketData bean1 = new BasketData();
        bean1.setId(1);
        bean1.setTitle("让球");

        BasketData bean2 = new BasketData();
        bean2.setId(2);
        bean2.setTitle("总分:　大/小");

        BasketData bean3 = new BasketData();
        bean3.setId(3);
        bean3.setTitle("总分:　单/双");

        BasketData bean4 = new BasketData();
        bean4.setId(4);
        bean4.setTitle("上半场　大/小");

        BasketData bean5 = new BasketData();
        bean5.setId(5);
        bean5.setTitle("上半场　单/双");

        BasketData bean6 = new BasketData();
        bean6.setId(6);
        bean6.setTitle("下半场　大/小");

        BasketData bean7 = new BasketData();
        bean7.setId(7);
        bean7.setTitle("下半场　单/双");

        BasketData bean8 = new BasketData();
        bean8.setId(8);
        bean8.setTitle("球队得分：" + bean.home + "-大/小");

        BasketData bean9 = new BasketData();
        bean9.setId(9);
        bean9.setTitle("球队得分：" + bean.away + "-大/小");

        BasketData bean10 = new BasketData();
        bean10.setId(10);
        bean10.setTitle("球队得分：" + bean.home + "-最后一位");

        BasketData bean11 = new BasketData();
        bean11.setId(11);
        bean11.setTitle("球队得分：" + bean.away + "-最后一位");

        BasketData bean12 = new BasketData();
        bean12.setId(12);
        bean12.setTitle("<font color=\"#FFFFFF\">总分：大/小-</font><font color=\"#FFF000\">第1节</font>");

        BasketData bean13 = new BasketData();
        bean13.setId(13);
        bean13.setTitle("<font color=\"#FFFFFF\">总分：单/双-</font><font color=\"#FFF000\">第1节</font>");

        BasketData bean14 = new BasketData();
        bean14.setId(14);
        bean14.setTitle("<font color=\"#FFFFFF\">总分：大小-</font><font color=\"#FFF000\">第2节</font>");

        BasketData bean15 = new BasketData();
        bean15.setId(15);
        bean15.setTitle("<font color=\"#FFFFFF\">总分：单/双-</font><font color=\"#FFF000\">第2节</font>");

        BasketData bean16 = new BasketData();
        bean16.setId(16);
        bean16.setTitle("<font color=\"#FFFFFF\">总分：大小-</font><font color=\"#FFF000\">第3节</font>");

        BasketData bean17 = new BasketData();
        bean17.setId(17);
        bean17.setTitle("<font color=\"#FFFFFF\">总分：单/双-</font><font color=\"#FFF000\">第3节</font>");

        BasketData bean18 = new BasketData();
        bean18.setId(18);
        bean18.setTitle("<font color=\"#FFFFFF\">总分：大小-</font><font color=\"#FFF000\">第4节</font>");

        BasketData bean19 = new BasketData();
        bean19.setId(19);
        bean19.setTitle("<font color=\"#FFFFFF\">总分：单/双-</font><font color=\"#FFF000\">第4节</font>");

        List<BasketData.BasketItemData> itemData = new ArrayList<>();//全场让球
        List<BasketData.BasketItemData> itemData1 = new ArrayList<>();//总分 大/小
        List<BasketData.BasketItemData> itemData10 = new ArrayList<>();//最后一位主队
        List<BasketData.BasketItemData> itemData11 = new ArrayList<>();//最后一位客队
        List<BasketData.BasketItemData> itemData3 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData4 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData5 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData6 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData7 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData8 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData9 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData12 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData13 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData14 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData15 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData16 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData17 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData18 = new ArrayList<>();
        List<BasketData.BasketItemData> itemData19 = new ArrayList<>();

        BasketData.BasketItemData basketItemData9 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData10 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData13 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData14 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData17 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData18 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData19 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData20 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData21 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData22 = new BasketData.BasketItemData();
        //第几节
        BasketData.BasketItemData basketItemData35 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData36 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData39 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData40 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData43 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData44 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData47 = new BasketData.BasketItemData();
        BasketData.BasketItemData basketItemData48 = new BasketData.BasketItemData();

        //最后一位的数据
        if (basketBallLastBeans != null && basketBallLastBeans.size() >= 1) {
            BasketBallLastBean lastBean = basketBallLastBeans.get(0);

            if (lastBean != null && lastBean.getList() != null && lastBean.getList().size() >= 1 && lastBean.getList().get(0) != null && lastBean.getList().get(0).getItems() != null) {
                List<BasketBallLastBean.BasketItem> list = lastBean.getList().get(0).getItems();
                for (int i = 0; i < list.size(); i++) {
                    if (bean.home.equals(list.get(i).getTeamName())) {//主队
                        BasketData.BasketItemData basketItemData = new BasketData.BasketItemData();
                        basketItemData.setMatchID(list.get(i).getMatchID());
                        basketItemData.setId(list.get(i).getId());
                        basketItemData.setPayTypeID(list.get(i).getPayTypeID());
                        basketItemData.setLeftStr(list.get(i).getName());
                        basketItemData.setNumber(list.get(i).getPayRate());
                        basketItemData.setTagName("最后一位");

                        itemData10.add(basketItemData);
                    } else if (bean.away.equals(list.get(i).getTeamName())) {//客队
                        BasketData.BasketItemData basketItemData = new BasketData.BasketItemData();
                        basketItemData.setMatchID(list.get(i).getMatchID());
                        basketItemData.setId(list.get(i).getId());
                        basketItemData.setPayTypeID(list.get(i).getPayTypeID());
                        basketItemData.setLeftStr(list.get(i).getName());
                        basketItemData.setNumber(list.get(i).getPayRate());
                        basketItemData.setTagName("最后一位");

                        itemData11.add(basketItemData);
                    }
                }
            }
        }

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if ("全场让球".equals(items.get(i).payTypeName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr(bean.home);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setSelected(1);
                    basketItemData1.setTagName(items.get(i).payTypeName);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr(bean.away);
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);
                    basketItemData2.setTagName(items.get(i).payTypeName);

                    itemData.add(basketItemData1);
                    itemData.add(basketItemData2);

                } else if (items.get(i).payTypeID == 3) {//总
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);
                    basketItemData1.setTagName(items.get(i).payTypeName);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);
                    basketItemData2.setTagName(items.get(i).payTypeName);

                    itemData1.add(basketItemData1);
                    itemData1.add(basketItemData2);

                } else if ("全场单双".equals(items.get(i).payTypeName)) {
                    basketItemData9.setId(items.get(i).id);
                    basketItemData9.setPayTypeID(items.get(i).payTypeID);
                    basketItemData9.setMatchID(items.get(i).matchID);
                    basketItemData9.setLeftStr("单");
                    basketItemData9.setNumber(items.get(i).realRate1);
                    basketItemData9.setSelected(1);

                    basketItemData10.setId(items.get(i).id);
                    basketItemData10.setPayTypeID(items.get(i).payTypeID);
                    basketItemData10.setMatchID(items.get(i).matchID);
                    basketItemData10.setLeftStr("双");
                    basketItemData10.setNumber(items.get(i).realRate3);
                    basketItemData10.setSelected(2);
                } else if ("上半场大小".equals(items.get(i).payTypeName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData4.add(basketItemData1);
                    itemData4.add(basketItemData2);
                } else if ("上半场单双".equals(items.get(i).payTypeName)) {
                    basketItemData13.setId(items.get(i).id);
                    basketItemData13.setPayTypeID(items.get(i).payTypeID);
                    basketItemData13.setMatchID(items.get(i).matchID);
                    basketItemData13.setLeftStr("单");
                    basketItemData13.setNumber(items.get(i).realRate1);
                    basketItemData13.setSelected(1);

                    basketItemData14.setId(items.get(i).id);
                    basketItemData14.setPayTypeID(items.get(i).payTypeID);
                    basketItemData14.setMatchID(items.get(i).matchID);
                    basketItemData14.setLeftStr("双");
                    basketItemData14.setNumber(items.get(i).realRate3);
                    basketItemData14.setSelected(2);
                } else if ("下半场大小".equals(items.get(i).payTypeName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData6.add(basketItemData1);
                    itemData6.add(basketItemData2);
                } else if ("下半场单双".equals(items.get(i).payTypeName)) {
                    basketItemData17.setId(items.get(i).id);
                    basketItemData17.setPayTypeID(items.get(i).payTypeID);
                    basketItemData17.setMatchID(items.get(i).matchID);
                    basketItemData17.setLeftStr("单");
                    basketItemData17.setNumber(items.get(i).realRate1);
                    basketItemData17.setSelected(1);

                    basketItemData18.setId(items.get(i).id);
                    basketItemData18.setPayTypeID(items.get(i).payTypeID);
                    basketItemData18.setMatchID(items.get(i).matchID);
                    basketItemData18.setLeftStr("双");
                    basketItemData18.setNumber(items.get(i).realRate3);
                    basketItemData18.setSelected(2);
                } else if (items.get(i).payTypeID == 48) {//主场 得分 大小
                    basketItemData19.setId(items.get(i).id);
                    basketItemData19.setPayTypeID(items.get(i).payTypeID);
                    basketItemData19.setMatchID(items.get(i).matchID);
                    basketItemData19.setLeftStr("大");
                    basketItemData19.setRightStr(items.get(i).realRate2);
                    basketItemData19.setNumber(items.get(i).realRate1);
                    basketItemData19.setSelected(1);

                    basketItemData20.setId(items.get(i).id);
                    basketItemData20.setPayTypeID(items.get(i).payTypeID);
                    basketItemData20.setMatchID(items.get(i).matchID);
                    basketItemData20.setLeftStr("小");
                    basketItemData20.setRightStr(items.get(i).realRate2);
                    basketItemData20.setNumber(items.get(i).realRate3);
                    basketItemData20.setSelected(2);

                    itemData8.add(basketItemData19);
                    itemData8.add(basketItemData20);
                } else if (items.get(i).payTypeID == 49) {//客场 得分 大小
                    basketItemData21.setId(items.get(i).id);
                    basketItemData21.setPayTypeID(items.get(i).payTypeID);
                    basketItemData21.setMatchID(items.get(i).matchID);
                    basketItemData21.setLeftStr("大");
                    basketItemData21.setRightStr(items.get(i).realRate2);
                    basketItemData21.setNumber(items.get(i).realRate1);
                    basketItemData21.setSelected(1);

                    basketItemData22.setId(items.get(i).id);
                    basketItemData22.setPayTypeID(items.get(i).payTypeID);
                    basketItemData22.setMatchID(items.get(i).matchID);
                    basketItemData22.setLeftStr("小");
                    basketItemData22.setRightStr(items.get(i).realRate2);
                    basketItemData22.setNumber(items.get(i).realRate3);
                    basketItemData22.setSelected(2);

                    itemData9.add(basketItemData21);
                    itemData9.add(basketItemData22);
                } else if ("第一节大小".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData12.add(basketItemData1);
                    itemData12.add(basketItemData2);
                } else if ("第一节单双".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    basketItemData35.setId(items.get(i).id);
                    basketItemData35.setPayTypeID(items.get(i).payTypeID);
                    basketItemData35.setMatchID(items.get(i).matchID);
                    basketItemData35.setLeftStr("单");
                    basketItemData35.setNumber(items.get(i).realRate1);
                    basketItemData35.setSelected(1);

                    basketItemData36.setId(items.get(i).id);
                    basketItemData36.setPayTypeID(items.get(i).payTypeID);
                    basketItemData36.setMatchID(items.get(i).matchID);
                    basketItemData36.setLeftStr("双");
                    basketItemData36.setNumber(items.get(i).realRate3);
                    basketItemData36.setSelected(2);
                } else if ("第二节大小".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData14.add(basketItemData1);
                    itemData14.add(basketItemData2);
                } else if ("第二节单双".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    basketItemData39.setId(items.get(i).id);
                    basketItemData39.setPayTypeID(items.get(i).payTypeID);
                    basketItemData39.setMatchID(items.get(i).matchID);
                    basketItemData39.setLeftStr("单");
                    basketItemData39.setRightStr(items.get(i).realRate2);
                    basketItemData39.setNumber(items.get(i).realRate1);
                    basketItemData39.setSelected(1);

                    basketItemData40.setId(items.get(i).id);
                    basketItemData40.setPayTypeID(items.get(i).payTypeID);
                    basketItemData40.setMatchID(items.get(i).matchID);
                    basketItemData40.setLeftStr("双");
                    basketItemData40.setRightStr(items.get(i).realRate2);
                    basketItemData40.setNumber(items.get(i).realRate3);
                    basketItemData40.setSelected(2);
                } else if ("第三节大小".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData16.add(basketItemData1);
                    itemData16.add(basketItemData2);
                } else if ("第三节单双".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    basketItemData43.setId(items.get(i).id);
                    basketItemData43.setPayTypeID(items.get(i).payTypeID);
                    basketItemData43.setMatchID(items.get(i).matchID);
                    basketItemData43.setLeftStr("单");
                    basketItemData43.setRightStr(items.get(i).realRate2);
                    basketItemData43.setNumber(items.get(i).realRate1);
                    basketItemData43.setSelected(1);

                    basketItemData44.setId(items.get(i).id);
                    basketItemData44.setPayTypeID(items.get(i).payTypeID);
                    basketItemData44.setMatchID(items.get(i).matchID);
                    basketItemData44.setLeftStr("双");
                    basketItemData44.setRightStr(items.get(i).realRate2);
                    basketItemData44.setNumber(items.get(i).realRate3);
                    basketItemData44.setSelected(2);
                } else if ("第四节大小".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    BasketData.BasketItemData basketItemData1 = new BasketData.BasketItemData();
                    basketItemData1.setId(items.get(i).id);
                    basketItemData1.setPayTypeID(items.get(i).payTypeID);
                    basketItemData1.setMatchID(items.get(i).matchID);
                    basketItemData1.setLeftStr("大");
                    basketItemData1.setRightStr(items.get(i).realRate2);
                    basketItemData1.setNumber(items.get(i).realRate1);
                    basketItemData1.setSelected(1);

                    BasketData.BasketItemData basketItemData2 = new BasketData.BasketItemData();
                    basketItemData2.setId(items.get(i).id);
                    basketItemData2.setPayTypeID(items.get(i).payTypeID);
                    basketItemData2.setMatchID(items.get(i).matchID);
                    basketItemData2.setLeftStr("小");
                    basketItemData2.setRightStr(items.get(i).realRate2);
                    basketItemData2.setNumber(items.get(i).realRate3);
                    basketItemData2.setSelected(2);

                    itemData18.add(basketItemData1);
                    itemData18.add(basketItemData2);
                } else if ("第四节单双".equals(items.get(i).payTypeName) && TextUtils.isEmpty(items.get(i).teamName)) {
                    basketItemData47.setId(items.get(i).id);
                    basketItemData47.setPayTypeID(items.get(i).payTypeID);
                    basketItemData47.setMatchID(items.get(i).matchID);
                    basketItemData47.setLeftStr("单");
                    basketItemData47.setRightStr(items.get(i).realRate2);
                    basketItemData47.setNumber(items.get(i).realRate1);
                    basketItemData47.setSelected(1);

                    basketItemData48.setId(items.get(i).id);
                    basketItemData48.setPayTypeID(items.get(i).payTypeID);
                    basketItemData48.setMatchID(items.get(i).matchID);
                    basketItemData48.setLeftStr("双");
                    basketItemData48.setRightStr(items.get(i).realRate2);
                    basketItemData48.setNumber(items.get(i).realRate3);
                    basketItemData48.setSelected(2);
                }
            }
        }

        itemData3.add(basketItemData9);
        itemData3.add(basketItemData10);
        itemData5.add(basketItemData13);
        itemData5.add(basketItemData14);
        itemData7.add(basketItemData17);
        itemData7.add(basketItemData18);
        itemData13.add(basketItemData35);
        itemData13.add(basketItemData36);
        itemData15.add(basketItemData39);
        itemData15.add(basketItemData40);
        itemData17.add(basketItemData43);
        itemData17.add(basketItemData44);
        itemData19.add(basketItemData47);
        itemData19.add(basketItemData48);

        bean1.setItems(itemData);
        bean2.setItems(itemData1);
        bean3.setItems(itemData3);
        bean4.setItems(itemData4);
        bean5.setItems(itemData5);
        bean6.setItems(itemData6);
        bean7.setItems(itemData7);
        bean8.setItems(itemData8);
        bean9.setItems(itemData9);
        bean10.setItems(itemData10);
        bean11.setItems(itemData11);
        bean12.setItems(itemData12);
        bean13.setItems(itemData13);
        bean14.setItems(itemData14);
        bean15.setItems(itemData15);
        bean16.setItems(itemData16);
        bean17.setItems(itemData17);
        bean18.setItems(itemData18);
        bean19.setItems(itemData19);

        if (bean1.getItems().size() != 0 && bean1.getItems().get(0).getId() != 0) {
            data.add(bean1);
        }
        if (bean2.getItems().size() != 0 && bean2.getItems().get(0).getId() != 0) {
            data.add(bean2);
        }
        if (bean3.getItems().size() != 0 && bean3.getItems().get(0).getId() != 0) {
            data.add(bean3);
        }
        if (bean4.getItems().size() != 0 && bean4.getItems().get(0).getId() != 0) {
            data.add(bean4);
        }
        if (bean5.getItems().size() != 0 && bean5.getItems().get(0).getId() != 0) {
            data.add(bean5);
        }
        if (bean6.getItems().size() != 0 && bean6.getItems().get(0).getId() != 0) {
            data.add(bean6);
        }
        if (bean7.getItems().size() != 0 && bean7.getItems().get(0).getId() != 0) {
            data.add(bean7);
        }
        if (bean8.getItems().size() != 0 && bean8.getItems().get(0).getId() != 0) {
            data.add(bean8);
        }
        if (bean9.getItems().size() != 0 && bean9.getItems().get(0).getId() != 0) {
            data.add(bean9);
        }
        if (bean10.getItems().size() != 0 && bean10.getItems().get(0).getId() != 0) {
            data.add(bean10);
        }
        if (bean11.getItems().size() != 0 && bean11.getItems().get(0).getId() != 0) {
            data.add(bean11);
        }
        if (bean12.getItems().size() != 0 && bean12.getItems().get(0).getId() != 0) {
            data.add(bean12);
        }
        if (bean13.getItems().size() != 0 && bean13.getItems().get(0).getId() != 0) {
            data.add(bean13);
        }
        if (bean14.getItems().size() != 0 && bean14.getItems().get(0).getId() != 0) {
            data.add(bean14);
        }
        if (bean15.getItems().size() != 0 && bean15.getItems().get(0).getId() != 0) {
            data.add(bean15);
        }
        if (bean16.getItems().size() != 0 && bean16.getItems().get(0).getId() != 0) {
            data.add(bean16);
        }
        if (bean17.getItems().size() != 0 && bean17.getItems().get(0).getId() != 0) {
            data.add(bean17);
        }
        if (bean18.getItems().size() != 0 && bean18.getItems().get(0).getId() != 0) {
            data.add(bean18);
        }
        if (bean19.getItems().size() != 0 && bean19.getItems().get(0).getId() != 0) {
            data.add(bean19);
        }
        return data;
    }
}
