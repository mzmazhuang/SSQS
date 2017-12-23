package com.dading.ssqs.fragment.guesstheball;

import android.text.TextUtils;

import com.dading.ssqs.NotificationController;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/12.
 */

public class DataController {
    private static String TAG = "DataController";
    private static volatile DataController Instance = null;

    private List<GusessChoiceBean> footBallFilterData;//足球滚球筛选数据
    private List<GusessChoiceBean> footBallFilterHotData;//足球滚球筛选热门数据
    private List<GusessChoiceBean> basketBallFilterData;//篮球滚球筛选数据
    private List<GusessChoiceBean> basketBallFilterHotData;//篮球滚球筛选热门数据

    private List<GusessChoiceBean> todayFootBallFilterData;//足球今日筛选数据
    private List<GusessChoiceBean> todayFootBallFilterHotData;//足球今日筛选热门数据
    private List<GusessChoiceBean> todayBasketBallFilterData;//篮球今日筛选数据
    private List<GusessChoiceBean> todayBasketBallFilterHotData;//篮球今日筛选热门数据

    private List<GusessChoiceBean> earlyFootBallFilterData;//足球早盘筛选数据
    private List<GusessChoiceBean> earlyFootBallFilterHotData;//足球早盘筛选热门数据
    private List<GusessChoiceBean> earlyBasketBallFilterData;//篮球早盘筛选数据
    private List<GusessChoiceBean> earlyBasketBallFilterHotData;//篮球早盘筛选热门数据

    public static DataController getInstance() {
        DataController localInstance = Instance;
        if (localInstance == null) {
            synchronized (DataController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new DataController();
                }
            }
        }
        return localInstance;
    }

    public void clearEarlyFootBallData() {
        earlyFootBallFilterData = null;
        earlyFootBallFilterHotData = null;
    }

    public void clearEarlyBasketBallData() {
        earlyBasketBallFilterData = null;
        earlyBasketBallFilterHotData = null;
    }

    public void clearScrollFootBallData() {
        footBallFilterData = null;
        footBallFilterHotData = null;
    }

    public void clearToDayFootBallData() {
        todayFootBallFilterData = null;
        todayFootBallFilterHotData = null;
    }

    public List<GusessChoiceBean> getFootBallData() {
        return footBallFilterData;
    }

    public List<GusessChoiceBean> getFootBallHotData() {
        return footBallFilterHotData;
    }

    public List<GusessChoiceBean> getBaskteBallData() {
        return basketBallFilterData;
    }

    public List<GusessChoiceBean> getBasketBallHotData() {
        return basketBallFilterHotData;
    }

    public List<GusessChoiceBean> getTodayFootBallData() {
        return todayFootBallFilterData;
    }

    public List<GusessChoiceBean> getTodayFootBallHotData() {
        return todayFootBallFilterHotData;
    }

    public List<GusessChoiceBean> getTodayBaskteBallData() {
        return todayBasketBallFilterData;
    }

    public List<GusessChoiceBean> getTodayBasketBallHotData() {
        return todayBasketBallFilterHotData;
    }

    public List<GusessChoiceBean> getEarlyFootBallData() {
        return earlyFootBallFilterData;
    }

    public List<GusessChoiceBean> getEarlyFootBallHotData() {
        return earlyFootBallFilterHotData;
    }

    public List<GusessChoiceBean> getEarlyBaskteBallData() {
        return earlyBasketBallFilterData;
    }

    public List<GusessChoiceBean> getEarlyBasketBallHotData() {
        return earlyBasketBallFilterHotData;
    }

    public void syncFootBall(String tag, int type) {
        syncFootBallData(tag, type, "");
    }

    public void syncScrollFootBall(String tag, String date) {
        syncFootBallData(tag, 6, date);
    }

    public void syncTodayFootBall(String tag, String date) {
        syncFootBallData(tag, 2, date);
    }

    public void syncEarlyFootBall(String tag, String date) {
        syncFootBallData(tag, 7, date);
    }

    public void syncBasketBall(String tag, int type) {
        syncBasketBallData(tag, type, "");
    }

    public void syncEarlyBasketBall(String tag, String date) {
        syncBasketBallData(tag, 7, date);
    }

    private void syncBasketBallData(final String tag, final int type, final String date) {
        String mDate;

        if (!TextUtils.isEmpty(date)) {
            mDate = date;
        } else {
            mDate = DateUtils.getCurTime("yyyyMMdd");
        }

        SSQSApplication.apiClient(0).getMatchBasketBallFilterList(type, 0, mDate, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    if (type == 6) {
                        basketBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(basketBallFilterData);
                    } else if (type == 2) {
                        todayBasketBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(todayBasketBallFilterData);
                    } else if (type == 7) {
                        earlyBasketBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(earlyBasketBallFilterData);
                    }

                    syncBasketBallHotData(tag, type, date);
                }
            }
        });
    }

    private void syncBasketBallHotData(final String tag, final int type, String date) {
        String mDate;

        if (!TextUtils.isEmpty(date)) {
            mDate = date;
        } else {
            mDate = DateUtils.getCurTime("yyyyMMdd");
        }

        SSQSApplication.apiClient(0).getMatchBasketBallFilterList(type, 1, mDate, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    if (type == 6) {
                        basketBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(basketBallFilterHotData);
                    } else if (type == 2) {
                        todayBasketBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(todayBasketBallFilterHotData);
                    } else if (type == 7) {
                        earlyBasketBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(earlyBasketBallFilterHotData);
                    }

                    NotificationController.getInstance().postNotification(NotificationController.basketBallFilter, tag);
                }
            }
        });
    }

    private void syncFootBallHotData(final String tag, final int type, String date) {
        String mDate;

        if (!TextUtils.isEmpty(date)) {
            mDate = date;
        } else {
            mDate = DateUtils.getCurTime("yyyyMMdd");
        }

        SSQSApplication.apiClient(0).getMatchFilterList(type, 1, mDate, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    if (type == 6) {
                        footBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(footBallFilterHotData);
                    } else if (type == 2) {
                        todayFootBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(todayFootBallFilterHotData);
                    } else if (type == 7) {
                        earlyFootBallFilterHotData = (List<GusessChoiceBean>) result.getData();

                        selectAll(earlyFootBallFilterHotData);
                    }

                    NotificationController.getInstance().postNotification(NotificationController.footBallFilter, tag);
                }
            }
        });
    }

    private void syncFootBallData(final String tag, final int type, final String date) {
        String mDate;

        if (!TextUtils.isEmpty(date)) {
            mDate = date;
        } else {
            mDate = DateUtils.getCurTime("yyyyMMdd");
        }

        SSQSApplication.apiClient(0).getMatchFilterList(type, 0, mDate, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    if (type == 6) {
                        footBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(footBallFilterData);
                    } else if (type == 2) {
                        todayFootBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(todayFootBallFilterData);
                    } else if (type == 7) {
                        earlyFootBallFilterData = (List<GusessChoiceBean>) result.getData();

                        selectAll(earlyFootBallFilterData);
                    }

                    syncFootBallHotData(tag, type, date);
                }
            }
        });
    }

    //改成默认全部选中
    private void selectAll(List<GusessChoiceBean> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<GusessChoiceBean.FilterEntity> filters = list.get(i).filter;
                for (int j = 0; j < filters.size(); j++) {
                    filters.get(j).checked = true;
                }
            }
        }
    }
}
