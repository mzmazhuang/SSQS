package com.dading.ssqs.controllar.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.HomeFreeGlodAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SevenPopBean;
import com.dading.ssqs.bean.TaskBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 15:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FreeGlodTaskControllar extends Fragment {
    private static final String TAG = "FreeGlodTaskControllar";
    TextView mFreeGlodFinishTask;
    TextView mFreeGlodNoFinishTask;
    TextView mNewsTasteHaveGlod;
    TextView mNewsTasteHaveDiamons;
    TextView mNewsTasteNoHaveGlod;
    TextView mNewsTasteNoHaveDiamons;
    ListView mNewsTasteLv;
    private Context context;
    private View mView;
    public TaskRecevice mRecevice;
    private TaskBean mBean;
    public View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getActivity();
        mRootView = initView();
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    private void initListener() {
    }

    private View initView() {
        mView = View.inflate(context, R.layout.home_free_glod_controllar, null);
        mFreeGlodFinishTask = (TextView) mView.findViewById(R.id.free_glod_finish_task);
        mFreeGlodNoFinishTask = (TextView) mView.findViewById(R.id.free_glod_no_finish_task);
        mNewsTasteHaveGlod = (TextView) mView.findViewById(R.id.news_taste_have_glod);
        mNewsTasteHaveDiamons = (TextView) mView.findViewById(R.id.news_taste_have_diamons);
        mNewsTasteNoHaveGlod = (TextView) mView.findViewById(R.id.news_taste_no_have_glod);
        mNewsTasteNoHaveDiamons = (TextView) mView.findViewById(R.id.news_taste_no_have_diamons);
        mNewsTasteLv = (ListView) mView.findViewById(R.id.news_taste_lv);
        return mView;
    }

    private void initData() {
        mRecevice = new TaskRecevice();

        UIUtils.ReRecevice(mRecevice, Constent.TASK_TAG);
        UIUtils.ReRecevice(mRecevice, Constent.TASK_TAG2);

        SSQSApplication.apiClient(0).getTaskList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    TaskBean bean = (TaskBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((HomeFreeGlodActivity) context).finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });

        setNoIsloadin();
    }

    private void setNoIsloadin() {
        mFreeGlodFinishTask.setText("0");
        mFreeGlodNoFinishTask.setText("0");
        mNewsTasteHaveGlod.setText("-");
        mNewsTasteHaveDiamons.setText("-");
        mNewsTasteNoHaveGlod.setText("-");
        mNewsTasteNoHaveDiamons.setText("-");
    }

    private void processData(TaskBean bean) {
        if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            setNoIsloadin();
        } else {
            mBean = bean;

            SSQSApplication.apiClient(0).getTaskDay(new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        SevenPopBean bean = (SevenPopBean) result.getData();

                        if (bean != null) {
                            mNewsTasteLv.setAdapter(new HomeFreeGlodAdapter(context, mBean.tasks, mView, bean));

                            int getGlod = 0;
                            int getDiamons = 0;
                            int noGetGlod = 0;
                            int noGetDiamons = 0;

                            for (TaskBean.TasksEntity entity : mBean.tasks) {
                                if (entity.id == 2) {
                                    if (entity.status == 1) {
                                        if (bean.dayCount == 0) {
                                            getGlod += bean.tasks.get(bean.dayCount).banlance;
                                        } else {
                                            getGlod += bean.tasks.get((bean.dayCount - 1)).banlance;
                                        }
                                        getDiamons += 0;
                                    } else {
                                        if (bean.dayCount == 0) {
                                            noGetGlod += bean.tasks.get(bean.dayCount).banlance;
                                        } else {
                                            noGetGlod += bean.tasks.get((bean.dayCount - 1)).banlance;
                                        }
                                        noGetDiamons += 0;
                                    }
                                } else {
                                    if (entity.status == 1) {
                                        getGlod += entity.banlance;
                                        getDiamons += 0;
                                    } else {
                                        noGetGlod += entity.banlance;
                                        noGetDiamons += 0;
                                    }
                                }
                            }

                            //已获得金币钻石
                            String getGlodT = getGlod + "";
                            mNewsTasteHaveGlod.setText(getGlodT);
                            String getDiamonsT = getDiamons + "";
                            mNewsTasteHaveDiamons.setText(getDiamonsT);

                            //未获得
                            String noGetGlodT = noGetGlod + "";
                            mNewsTasteNoHaveGlod.setText(noGetGlodT);
                            String noGetDiamonsT = noGetDiamons + "";
                            mNewsTasteNoHaveDiamons.setText(noGetDiamonsT);
                        }
                    } else {
                        if (403 == result.getErrno()) {
                            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            ((HomeFreeGlodActivity) context).finish();
                        } else {
                            ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                        }
                    }
                }
            });

            String countT = mBean.finishCount + "";
            mFreeGlodFinishTask.setText(countT);

            String sizeT = "/" + mBean.taskCount;//总任务数
            mFreeGlodNoFinishTask.setText(sizeT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UIUtils.UnReRecevice(mRecevice);
    }

    private class TaskRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constent.TASK_TAG:
                    initData();
                    break;
                case Constent.TASK_TAG2:
                    UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }
    }
}
