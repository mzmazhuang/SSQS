package com.dading.ssqs.fragment.guesstheball.today;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.FilterDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ResultSelectTimeDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ScrollBallResultAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.CommonTitle;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.bean.ScrollBallFootBallResultBean;
import com.dading.ssqs.cells.GuessFilterCell;
import com.dading.ssqs.cells.ResultTimeLayout;
import com.dading.ssqs.components.FilterDialog;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.PageDialog;
import com.dading.ssqs.components.RecyclerScrollview;
import com.dading.ssqs.components.ResultSelectTimeDialog;
import com.dading.ssqs.components.SelectMatchDialog;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.fragment.guesstheball.DataController;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 * 今日-足球-赛果
 */

public class ToDayResultFragment extends Fragment implements OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private static final String TAG = "ToDayResultFragment";

    private Context mContext;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ScrollBallResultAdapter adapter;
    private LoadingDialog loadingDialog;
    private GuessFilterCell filterCell;
    private PageDialog pageDialog;
    private FilterDialog filterDialog;
    private ResultSelectTimeDialog selectTimeDialog;
    private SelectMatchDialog selectMatchDialog;
    private ImageView defaultView;
    private ResultTimeLayout resultTimeLayout;

    private int offset = 1;
    private int limit = 20;

    private int totalPage;
    private boolean isRefresh = false;

    private int sType = 0;

    private String filter_str = "按时间顺序";

    private String leagueIDs = "0";
    private boolean isFilter = false;

    private String currTime;

    private List<ResultSelectTimeDialog.ResultSelectTimeBean> timesArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

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
                if (DataController.Companion.getInstance().getTodayFootBallData() == null) {
                    DataController.Companion.getInstance().syncTodayFootBall(TAG, currTime);
                    loadingDialog.show();
                } else {
                    selectMatchDialog.show(DataController.Companion.getInstance().getTodayFootBallData(), DataController.Companion.getInstance().getTodayFootBallHotData(), "联赛选择");
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

        LinearLayout scrollContainer = new LinearLayout(mContext);
        scrollContainer.setOrientation(LinearLayout.VERTICAL);
        scrollview.addView(scrollContainer, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        resultTimeLayout = new ResultTimeLayout(mContext);
        resultTimeLayout.setTime(DateUtils.getCurTimeAddND(-1, "yyyy-MM-dd"));
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

                            setSelectTime(day);
                        }
                    });
                }
                selectTimeDialog.show(getTime(), "选择时间", resultTimeLayout.getDay());
            }
        });
        scrollContainer.addView(resultTimeLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

        mRecyclerView = new RecyclerView(mContext);
        scrollContainer.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new ScrollBallResultAdapter(mContext);
        mRecyclerView.setAdapter(adapter);

        defaultView = new ImageView(mContext);
        defaultView.setVisibility(View.GONE);
        defaultView.setImageResource(R.mipmap.no_data);
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        init();
        return container;
    }

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

    private void setSelectTime(int day) {
        currTime = DateUtils.getCurTimeAddND(-day, "yyyyMMddHH:mm:ss");
        resultTimeLayout.setTime(DateUtils.getCurTimeAddND(-day, "yyyy-MM-dd"));

        DataController.Companion.getInstance().clearToDayFootBallData();

        swipeToLoadLayout.setRefreshing(true);
    }

    private List<ResultSelectTimeDialog.ResultSelectTimeBean> getTime() {
        if (timesArray.size() > 0) {
            return timesArray;
        } else {
            Calendar calendar = Calendar.getInstance();

            for (int i = 1; i <= 7; i++) {
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -i);

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

                timesArray.add(new ResultSelectTimeDialog.ResultSelectTimeBean("(" + week + ")　" + sdf.format(resultDate), i));
            }
            return timesArray;
        }
    }

    private void getNetDataWork(final int off, int lim) {
        if (TextUtils.isEmpty(currTime)) {
            currTime = DateUtils.getCurTimeAddND(-1, "yyyyMMddHH:mm:ss");
        }

        SSQSApplication.apiClient(0).getScrollBallList(true, 3, currTime, sType, leagueIDs, off, lim, new CcApiClient.OnCcListener() {
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

                        totalPage = page.getTotalCount();

                        filterCell.setTotalPage(totalPage);

                        adapter.setList(getData(page.getItems()));
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


    private List<ScrollBallFootBallResultBean> getData(List<ScoreBean> data) {

        List<CommonTitle> list = getFilterData(data);

        List<ScrollBallFootBallResultBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ScrollBallFootBallResultBean bean = new ScrollBallFootBallResultBean();
            bean.setTitle(list.get(i));
            bean.setItems(handleOtherData(data, list.get(i)));

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

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private List<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems> handleOtherData(List<ScoreBean> currData, CommonTitle title) {
        List<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems> beanItems2 = new ArrayList<>();

        //半场/全场 数据
        for (int i = 0; i < currData.size(); i++) {
            if (currData.get(i).id == title.getId()) {
                ScrollBallFootBallResultBean.ScrollBallFootBallResultItems item = new ScrollBallFootBallResultBean.ScrollBallFootBallResultItems();
                item.setId(currData.get(i).id);
                item.setTitle(currData.get(i).home);
                item.setByTitle(currData.get(i).away);
                item.setIntegral1(currData.get(i).hHalfScore);
                item.setIntegral2(currData.get(i).hScore);
                item.setIntegral3(currData.get(i).aHalfScore);
                item.setIntegral4(currData.get(i).aScore);

                boolean result = true;

                try {
                    int hscore = Integer.valueOf(currData.get(i).hScore);
                    int ascore = Integer.valueOf(currData.get(i).aScore);

                    result = hscore >= ascore;
                } catch (Exception ex) {
                    Log.e("result", "failure");
                }

                item.setHome(result);

                beanItems2.add(item);
            }
        }

        return beanItems2;
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.footBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    selectMatchDialog.show(DataController.Companion.getInstance().getTodayFootBallData(), DataController.Companion.getInstance().getTodayFootBallHotData(), "联赛选择");
                }
            }
        }
    }
}
