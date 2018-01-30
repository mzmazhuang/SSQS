package com.dading.ssqs.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.FootBallDetailsItemAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusMatchElement;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCScorebean;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.cells.FootBallDetailsHeadCell;
import com.dading.ssqs.cells.FootBallDetailsItemCell;
import com.dading.ssqs.cells.FootBallItemCell;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.RecyclerScrollview;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.components.ScrollBallCommitView;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/27.
 * 滚球足球的所有玩法
 */

public class ScrollFootBallDetailsActivity extends BaseActivity implements OnRefreshListener {

    private Context mContext;

    private SwipeToLoadLayout swipeToLoadLayout;
    private FootBallDetailsHeadCell headCell;

    private FootBallItemCell myItemCell;
    private FootBallItemCell mainItemCell;
    private ScrollBallCommitView commitView;
    private ScrollBallCommitMenuView commitMenuView;

    private LoadingDialog loadingDialog;

    private String matchID;
    private String matchTitle;

    private boolean isRefresh;
    private boolean mainRefresh;
    private boolean myRefresh;

    private List<FootData.FootItemData> mainDatas = new ArrayList<>();
    private List<FootData.FootItemData> myDatas = new ArrayList<>();

    private ScoreBean bean;

    @Override
    protected int setLayoutId() {
        return 0;
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

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);

        RecyclerScrollview scrollview = view.findViewById(R.id.swipe_target);

        LinearLayout infoLayout = new LinearLayout(mContext);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        scrollview.addView(infoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        headCell = new FootBallDetailsHeadCell(mContext);
        headCell.setRefreshCount(30);
        headCell.setListener(new GuessFilterCell.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
        infoLayout.addView(headCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 8));

        myItemCell = new FootBallItemCell(mContext, 1);
        myItemCell.setOnItemClickListener(myItemClickListener);
        myItemCell.setListener(myItemListener);
        infoLayout.addView(myItemCell);

        mainItemCell = new FootBallItemCell(mContext, 2);
        mainItemCell.setOnItemClickListener(mainItemClickListener);
        mainItemCell.setListener(mainItemListener);
        infoLayout.addView(mainItemCell);

        commitView = new ScrollBallCommitView(mContext);
        commitView.setVisibility(View.GONE);
        commitView.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitMenuView.getVisibility() == View.GONE) {
                    commitMenuView.setTitle("滚球-足球-" + matchTitle);
                    if (myDatas.size() > 0) {
                        commitMenuView.setFootDetailsData(myDatas);
                    } else {
                        commitMenuView.setFootDetailsData(mainDatas);
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

    //删除Item
    private ScrollBallCommitMenuAdapter.OnMenuClickListener menuClickListener = new ScrollBallCommitMenuAdapter.OnMenuClickListener() {
        @Override
        public void onClick(int position, int dataId, final int itemId, final String value) {
            commitMenuView.changeData(position);

            if (myDatas.size() > 0) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<FootData.FootItemData> iterator = myDatas.iterator();

                        while (iterator.hasNext()) {
                            FootData.FootItemData bean = iterator.next();
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
                        Iterator<FootData.FootItemData> iterator = mainDatas.iterator();

                        while (iterator.hasNext()) {
                            FootData.FootItemData bean = iterator.next();
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

    //投注
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

        //可以抽取出来一个方法 后续优化
        if (mainDatas.size() > 0) {
            mainItemCell.refreshData();

            for (int i = 0; i < mainDatas.size(); i++) {
                FootData.FootItemData list = mainDatas.get(i);

                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                if ("波胆".equals(mainDatas.get(i).getTagName()) || "半场/全场".equals(mainDatas.get(i).getTagName()) || "总进球个数".equals(mainDatas.get(i).getTagName())) {
                    bean.type = 4;
                } else {
                    bean.type = 1;
                }
                bean.selected = list.getSelected();
                bean.matchID = list.getMatchID();
                bean.payRateID = list.getId();
                bean.amount = moneyLists.get(i).getMoney();
                items.add(bean);
            }
            mainDatas.clear();
        } else if (myDatas.size() > 0) {
            myItemCell.refreshData();

            for (int i = 0; i < myDatas.size(); i++) {
                FootData.FootItemData list = myDatas.get(i);

                PayBallElement.BetBean bean = new PayBallElement.BetBean();
                if ("波胆".equals(myDatas.get(i).getTagName()) || "半场/全场".equals(myDatas.get(i).getTagName()) || "总进球个数".equals(myDatas.get(i).getTagName())) {
                    bean.type = 4;
                } else {
                    bean.type = 1;
                }
                bean.selected = list.getSelected();
                bean.matchID = list.getMatchID();
                bean.payRateID = list.getId();
                bean.amount = moneyLists.get(i).getMoney();
                items.add(bean);
            }
            myDatas.clear();
        }

        element.setItems(items);

        if (items.size() > 0) {
            footBallPay(element);
        }
    }

    //下注
    private void footBallPay(PayBallElement element) {
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

    //取消收藏
    private FootBallDetailsItemAdapter.OnItemClickListener myItemClickListener = new FootBallDetailsItemAdapter.OnItemClickListener() {
        @Override
        public void onClick(final FootData bean) {
            loadingDialog.show();

            FocusMatchElement element = new FocusMatchElement();
            element.setStatus(bean.isLike == true ? 0 : 1);

            getIds(bean, element);
            element.setType(1);

            SSQSApplication.apiClient(classGuid).focusMatch(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    loadingDialog.dismiss();

                    if (result.isOk()) {
                        bean.setLike(false);
                        List<FootData> newData = new ArrayList<>();
                        newData.addAll(myItemCell.getItem());

                        Iterator<FootData> iterator = newData.iterator();

                        while (iterator.hasNext()) {
                            FootData data = iterator.next();
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

                        List<FootData> mainData = new ArrayList<>();
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

    //点击Item 进行投注
    private FootBallDetailsItemCell.OnItemClickListener myItemListener = new FootBallDetailsItemCell.OnItemClickListener() {
        @Override
        public boolean onClick(FootData.FootItemData data, FootData footData, boolean isAdd) {
            if (mainDatas.size() == 0) {

                if (isAdd) {
                    data.setTitle(data.getTitle());
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

    //点击Item 进行投注
    private FootBallDetailsItemCell.OnItemClickListener mainItemListener = new FootBallDetailsItemCell.OnItemClickListener() {
        @Override
        public boolean onClick(FootData.FootItemData data, FootData footData, boolean isAdd) {
            if (myDatas.size() == 0) {

                if (isAdd) {
                    data.setTitle(data.getTitle());
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

    //收藏 / 取消收藏
    private FootBallDetailsItemAdapter.OnItemClickListener mainItemClickListener = new FootBallDetailsItemAdapter.OnItemClickListener() {
        @Override
        public void onClick(final FootData bean) {
            loadingDialog.show();

            FocusMatchElement element = new FocusMatchElement();
            element.setStatus(bean.isLike == true ? 0 : 1);

            getIds(bean, element);
            element.setType(1);

            SSQSApplication.apiClient(classGuid).focusMatch(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    loadingDialog.dismiss();

                    if (result.isOk()) {
                        if (!bean.isLike()) {
                            bean.setLike(true);
                            List<FootData> newData = new ArrayList<>();
                            if (myItemCell.getItem().size() != 0) {
                                newData.addAll(myItemCell.getItem());
                            }
                            newData.add(bean);
                            myItemCell.setData(newData);
                            myItemCell.setNumber(newData.size());
                        } else {
                            bean.setLike(false);
                            List<FootData> newData = new ArrayList<>();
                            newData.addAll(myItemCell.getItem());

                            Iterator<FootData> iterator = newData.iterator();

                            while (iterator.hasNext()) {
                                FootData data = iterator.next();
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

    //获取ID和Ball Id
    private void getIds(FootData bean, FocusMatchElement element) {
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
    }

    private void init() {
        Intent intent = getIntent();
        matchID = intent.getStringExtra("data_id");
        matchTitle = intent.getStringExtra("data_title");

        loadingDialog = new LoadingDialog(mContext);

        loadingDialog.show();

        headCell.setTitle(matchTitle);

        getHeadInfo();
    }

    private void getHeadInfo() {
        SSQSApplication.apiClient(classGuid).getFootHeadInfo(matchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    bean = (ScoreBean) result.getData();

                    if (bean != null) {
                        headCell.setHomeTeamInfo(bean.home, bean.hOverTimeScore, bean.hHalfScore, bean.hSHalfScore, bean.homeScore);
                        headCell.setvisitingTeamInfo(bean.away, bean.aOverTimeScore, bean.aHalfScore, bean.aSHalfScore, bean.awayScore);
                        headCell.setSection(bean.protime);
                    }

                    getMyNetDataWork();
                    getMainNetDataWork();
                } else {
                    getMyNetDataWork();
                    getMainNetDataWork();
                }
            }
        });
    }

    //主盘口
    private void getMainNetDataWork() {
        SSQSApplication.apiClient(classGuid).getMatchResult(matchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> beans = (List<JCbean>) result.getData();

                    getMainNetDataWork(beans);
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

    //主盘口
    private void getMainNetDataWork(final List<JCbean> jcBeans) {
        SSQSApplication.apiClient(classGuid).getScoreByIdList(matchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCScorebean> jcsBeans = (List<JCScorebean>) result.getData();

                    setData(jcBeans, jcsBeans, true);
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

    //我的盘口
    private void getMyNetDataWork() {
        SSQSApplication.apiClient(classGuid).getFootInfo(matchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> beans = (List<JCbean>) result.getData();

                    getMyNetDataWork(beans);
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

    //我的盘口
    private void getMyNetDataWork(final List<JCbean> jcBeans) {
        SSQSApplication.apiClient(classGuid).getFootDetails(matchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCScorebean> jcsBeans = (List<JCScorebean>) result.getData();

                    setData(jcBeans, jcsBeans, false);
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

    //对数据进行筛选
    private void setData(final List<JCbean> items, final List<JCScorebean> jcsBeans, final boolean isMain) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<FootData> list = handerData(items, jcsBeans);

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

    //对数据进行筛选
    private void checkRefresh() {
        if (mainRefresh && myRefresh) {
            loadingDialog.dismiss();
            swipeToLoadLayout.setRefreshing(false);
            swipeToLoadLayout.setRefreshEnabled(true);

            headCell.beginRunnable();

            if (myItemCell.getItem() != null && myItemCell.getItem().size() >= 1) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<FootData> mainDatas = mainItemCell.getItem();
                        List<FootData> myDatas = myItemCell.getItem();

                        for (int i = 0; i < myDatas.size(); i++) {
                            for (int j = 0; j < mainDatas.size(); j++) {
                                if (myDatas.get(i).getId() == mainDatas.get(j).getId()) {
                                    mainDatas.get(j).setLike(true);//收藏
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
        }
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;

            getHeadInfo();
        }
    }

    private List<FootData> handerData(List<JCbean> jcBeans, List<JCScorebean> jcsBeans) {
        List<FootData> list = new ArrayList<>();

        FootData footData1 = new FootData();
        footData1.setId(1);
        footData1.setTitle("独赢");

        FootData footData2 = new FootData();
        footData2.setId(2);
        footData2.setTitle("让球");

        FootData footData3 = new FootData();
        footData3.setId(3);
        footData3.setTitle("总进球: 大/小");

        FootData footData4 = new FootData();
        footData4.setId(4);
        footData4.setTitle("总进球个数");

        FootData footData5 = new FootData();
        footData5.setId(5);
        footData5.setTitle("总进球: 单/双");

        FootData footData6 = new FootData();
        footData6.setId(6);
        footData6.setTitle("半场总进球: 大/小");

        FootData footData7 = new FootData();
        footData7.setId(7);
        footData7.setTitle("半场总进球: 单/双");

        FootData footData8 = new FootData();
        footData8.setId(8);
        footData8.setTitle("全场波胆");

        FootData footData9 = new FootData();
        footData9.setId(9);
        footData9.setTitle("半场/全场 (胜负)");

        List<FootData.FootItemData> items1 = new ArrayList<>();
        List<FootData.FootItemData> items2 = new ArrayList<>();
        List<FootData.FootItemData> items3 = new ArrayList<>();
        List<FootData.FootItemData> items4 = new ArrayList<>();
        List<FootData.FootItemData> items5 = new ArrayList<>();
        List<FootData.FootItemData> items6 = new ArrayList<>();
        List<FootData.FootItemData> items7 = new ArrayList<>();
        List<FootData.FootItemData> items8 = new ArrayList<>();
        List<FootData.FootItemData> items9 = new ArrayList<>();

        if (jcBeans != null && jcBeans.size() >= 1) {
            for (int i = 0; i < jcBeans.size(); i++) {
                if ("全场赛果".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 3; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setTagName("独赢");
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setLeftStr("主胜");
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                            footItemData.setSelected(1);
                        } else if (j == 1) {
                            footItemData.setLeftStr("平");
                            footItemData.setNumber(jcBeans.get(i).realRate2);
                            footItemData.setSelected(3);
                        } else {
                            footItemData.setLeftStr("客胜");
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                            footItemData.setSelected(2);
                        }
                        items1.add(footItemData);
                    }
                } else if ("全场让球".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 2; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setTagName("全场让球");
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setSelected(1);
                            footItemData.setLeftStr(jcBeans.get(i).home);
                            footItemData.setRightStr(getRate2Str(jcBeans.get(i).realRate2, false));
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                        } else {
                            footItemData.setSelected(2);
                            footItemData.setLeftStr(jcBeans.get(i).away);
                            footItemData.setRightStr(getRate2Str(jcBeans.get(i).realRate2, true));
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                        }
                        items2.add(footItemData);
                    }
                } else if ("全场大小".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 2; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setSelected(1);
                            footItemData.setLeftStr("大");
                            footItemData.setRightStr(jcBeans.get(i).realRate2);
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                        } else {
                            footItemData.setSelected(2);
                            footItemData.setLeftStr("小");
                            footItemData.setRightStr(jcBeans.get(i).realRate2);
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                        }
                        items3.add(footItemData);
                    }
                } else if ("全场单双".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 2; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setSelected(1);
                            footItemData.setLeftStr("单");
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                        } else {
                            footItemData.setSelected(2);
                            footItemData.setLeftStr("双");
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                        }
                        items5.add(footItemData);
                    }
                } else if ("半场大小".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 2; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setSelected(1);
                            footItemData.setLeftStr("大");
                            footItemData.setRightStr(jcBeans.get(i).realRate2);
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                        } else {
                            footItemData.setSelected(2);
                            footItemData.setLeftStr("小");
                            footItemData.setRightStr(jcBeans.get(i).realRate2);
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                        }
                        items6.add(footItemData);
                    }
                } else if ("半场单双".equals(jcBeans.get(i).payTypeName)) {
                    for (int j = 0; j < 2; j++) {
                        FootData.FootItemData footItemData = new FootData.FootItemData();
                        footItemData.setId(jcBeans.get(i).id);
                        footItemData.setMatchID(jcBeans.get(i).matchID);
                        if (j == 0) {
                            footItemData.setSelected(1);
                            footItemData.setLeftStr("单");
                            footItemData.setNumber(jcBeans.get(i).realRate1);
                        } else {
                            footItemData.setSelected(2);
                            footItemData.setLeftStr("双");
                            footItemData.setNumber(jcBeans.get(i).realRate3);
                        }
                        items7.add(footItemData);
                    }
                }
            }
        }

        if (jcsBeans != null && jcsBeans.size() >= 1) {
            for (int i = 0; i < jcsBeans.size(); i++) {
                if (jcsBeans.get(i).type == 1) {//总进球个数
                    List<JCScorebean.ListEntity> listEntities = jcsBeans.get(i).list;

                    for (int j = 0; j < listEntities.size(); j++) {
                        List<JCScorebean.ListEntity.ItemsEntity> itemsEntityList = listEntities.get(j).items;
                        for (int k = 0; k < itemsEntityList.size(); k++) {
                            FootData.FootItemData footItemData = new FootData.FootItemData();
                            footItemData.setId(itemsEntityList.get(k).id);
                            footItemData.setMatchID(itemsEntityList.get(k).matchID);
                            footItemData.setTagName("总进球个数");
                            footItemData.setNumber(itemsEntityList.get(k).payRate);
                            footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                            if (k == 0) {
                                footItemData.setLeftStr("0-1");
                            } else if (k == 1) {
                                footItemData.setLeftStr("2-3");
                            } else if (k == 2) {
                                footItemData.setLeftStr("4-6");
                            } else if (k == 3) {
                                footItemData.setLeftStr("6以上");
                            }
                            items4.add(footItemData);
                        }
                    }
                } else if (jcsBeans.get(i).type == 3) {//半全场
                    List<JCScorebean.ListEntity> listEntities = jcsBeans.get(i).list;

                    for (int j = 0; j < listEntities.size(); j++) {
                        List<JCScorebean.ListEntity.ItemsEntity> itemsEntityList = listEntities.get(j).items;
                        for (int k = 0; k < itemsEntityList.size(); k++) {
                            FootData.FootItemData footItemData = new FootData.FootItemData();
                            footItemData.setId(itemsEntityList.get(k).id);
                            footItemData.setMatchID(itemsEntityList.get(k).matchID);
                            footItemData.setTagName("半场/全场");
                            footItemData.setNumber(itemsEntityList.get(k).payRate);
                            footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                            if (k == 0) {
                                footItemData.setLeftStr("主/主");
                            } else if (k == 1) {
                                footItemData.setLeftStr("主/和");
                            } else if (k == 2) {
                                footItemData.setLeftStr("主/客");
                            } else if (k == 3) {
                                footItemData.setLeftStr("和/主");
                            } else if (k == 4) {
                                footItemData.setLeftStr("和/和");
                            } else if (k == 5) {
                                footItemData.setLeftStr("和/客");
                            } else if (k == 6) {
                                footItemData.setLeftStr("客/主");
                            } else if (k == 7) {
                                footItemData.setLeftStr("客/和");
                            } else if (k == 8) {
                                footItemData.setLeftStr("客/客");
                            }
                            items9.add(footItemData);
                        }
                    }
                } else if (jcsBeans.get(i).type == 2) {//波胆
                    List<JCScorebean.ListEntity> listEntities = jcsBeans.get(i).list;

                    List<FootData.FootItemData> tempItems1 = new ArrayList<>();
                    List<FootData.FootItemData> tempItems2 = new ArrayList<>();
                    List<FootData.FootItemData> tempItems3 = new ArrayList<>();
                    List<FootData.FootItemData> tempItems4 = new ArrayList<>();
                    List<FootData.FootItemData> tempItems5 = new ArrayList<>();
                    List<FootData.FootItemData> tempItems6 = new ArrayList<>();

                    for (int j = 0; j < listEntities.size(); j++) {
                        String typeName = listEntities.get(j).name;
                        List<JCScorebean.ListEntity.ItemsEntity> itemsEntityList = listEntities.get(j).items;

                        if ("主胜".equals(typeName)) {
                            for (int k = 0; k < itemsEntityList.size(); k++) {
                                if (tempItems1.size() < 5) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setSelected(1);
                                    if (tempItems1.size() == 0) {
                                        footItemData.setLeftStr("1:0");
                                    } else if (tempItems1.size() == 1) {
                                        footItemData.setLeftStr("2:0");
                                    } else if (tempItems1.size() == 2) {
                                        footItemData.setLeftStr("2:1");
                                    } else if (tempItems1.size() == 3) {
                                        footItemData.setLeftStr("3:0");
                                    } else {
                                        footItemData.setLeftStr("3:1");
                                    }
                                    tempItems1.add(footItemData);
                                } else if (tempItems2.size() < 5) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setSelected(1);
                                    if (tempItems2.size() == 0) {
                                        footItemData.setLeftStr("3:2");
                                    } else if (tempItems2.size() == 1) {
                                        footItemData.setLeftStr("4:0");
                                    } else if (tempItems2.size() == 2) {
                                        footItemData.setLeftStr("4:1");
                                    } else if (tempItems2.size() == 3) {
                                        footItemData.setLeftStr("4:2");
                                    } else {
                                        footItemData.setLeftStr("4:3");
                                    }
                                    tempItems2.add(footItemData);
                                }
                            }
                        } else if ("主负".equals(typeName)) {
                            for (int k = 0; k < itemsEntityList.size(); k++) {
                                if (tempItems3.size() < 5) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setSelected(2);
                                    if (tempItems3.size() == 0) {
                                        footItemData.setLeftStr("0:1");
                                    } else if (tempItems3.size() == 1) {
                                        footItemData.setLeftStr("0:2");
                                    } else if (tempItems3.size() == 2) {
                                        footItemData.setLeftStr("1:2");
                                    } else if (tempItems3.size() == 3) {
                                        footItemData.setLeftStr("0:3");
                                    } else {
                                        footItemData.setLeftStr("1:3");
                                    }
                                    tempItems3.add(footItemData);
                                } else if (tempItems4.size() < 5) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setSelected(2);
                                    if (tempItems4.size() == 0) {
                                        footItemData.setLeftStr("2:3");
                                    } else if (tempItems4.size() == 1) {
                                        footItemData.setLeftStr("0:4");
                                    } else if (tempItems4.size() == 2) {
                                        footItemData.setLeftStr("1:4");
                                    } else if (tempItems4.size() == 3) {
                                        footItemData.setLeftStr("2:4");
                                    } else {
                                        footItemData.setLeftStr("3:4");
                                    }
                                    tempItems4.add(footItemData);
                                }
                            }
                        } else if ("平".equals(typeName)) {
                            for (int k = 0; k < itemsEntityList.size(); k++) {
                                if (tempItems5.size() < 5) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setSelected(1);
                                    if (tempItems5.size() == 0) {
                                        footItemData.setLeftStr("0:0");
                                    } else if (tempItems5.size() == 1) {
                                        footItemData.setLeftStr("1:1");
                                    } else if (tempItems5.size() == 2) {
                                        footItemData.setLeftStr("2:2");
                                    } else if (tempItems5.size() == 3) {
                                        footItemData.setLeftStr("3:3");
                                    } else {
                                        footItemData.setLeftStr("4:4");
                                    }
                                    tempItems5.add(footItemData);
                                }
                            }
                        } else if ("其他".equals(typeName)) {
                            for (int k = 0; k < itemsEntityList.size(); k++) {
                                if (tempItems6.size() < 1) {
                                    FootData.FootItemData footItemData = new FootData.FootItemData();
                                    footItemData.setId(itemsEntityList.get(k).id);
                                    footItemData.setMatchID(itemsEntityList.get(k).matchID);
                                    footItemData.setTagName("波胆");
                                    footItemData.setPayTypeID(itemsEntityList.get(k).payTypeID);
                                    footItemData.setNumber(itemsEntityList.get(k).payRate);
                                    footItemData.setSelected(1);
                                    footItemData.setLeftStr("其他");
                                    tempItems6.add(footItemData);
                                }
                            }
                        }
                    }
                    items8.addAll(tempItems1);
                    items8.addAll(tempItems3);
                    items8.addAll(tempItems2);
                    items8.addAll(tempItems4);
                    items8.addAll(tempItems5);
                    items8.addAll(tempItems6);
                }
            }
        }

        if (items1.size() >= 1) {
            footData1.setItems(items1);
            list.add(footData1);
        }

        if (items2.size() >= 1) {
            footData2.setItems(items2);
            list.add(footData2);
        }

        if (items3.size() >= 1) {
            footData3.setItems(items3);
            list.add(footData3);
        }

        if (items4.size() >= 1) {
            footData4.setItems(items4);
            list.add(footData4);
        }

        if (items5.size() >= 1) {
            footData5.setItems(items5);
            list.add(footData5);
        }

        if (items6.size() >= 1) {
            footData6.setItems(items6);
            list.add(footData6);
        }

        if (items7.size() >= 1) {
            footData7.setItems(items7);
            list.add(footData7);
        }

        if (items8.size() >= 1) {
            footData8.setItems(items8);
            list.add(footData8);
        }

        if (items9.size() >= 1) {
            footData9.setItems(items9);
            list.add(footData9);
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

    public static class FootData {
        private int id;
        private String title;
        private boolean isLike;
        private List<FootItemData> items;

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

        public List<FootItemData> getItems() {
            return items;
        }

        public void setItems(List<FootItemData> items) {
            this.items = items;
        }

        public static class FootItemData {
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
}
