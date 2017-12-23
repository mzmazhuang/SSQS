package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MySkAdapter;
import com.dading.ssqs.adapter.MySkAdapterSJ;
import com.dading.ssqs.adapter.SkZrAdapter;
import com.dading.ssqs.adapter.SkZrAdapterReplace;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.SKSJBean;
import com.dading.ssqs.bean.SKZRBean;
import com.dading.ssqs.bean.SKTJBean;
import com.dading.ssqs.utils.ListScrollUtil;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/14 17:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoSk {
    private static final String TAG = "MatchInfoSk";
    private final Context context;
    public final View mRootView;
    private final int matchId;
    private ListView mSkLv;
    private RelativeLayout mSkNoDataView;
    private RadioGroup mSkRg;
    private TextView mSkZRFristMain;
    private TextView mSkZRFristSeond;
    private ListView mSkZRListview1;
    private ListView mSkZRListview2;
    private RelativeLayout mSkDataView;
    private LinearLayout mSkZr;
    private LinearLayout mSkSjImage;
    private RadioButton mSkTjRb;
    private RadioButton mSkSjRb;
    private RadioButton mSkZrRb;
    private List<SKSJBean> SJData;

    public MatchInfoSk(Context context, int matchId) {
        this.context = context;
        this.matchId = matchId;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private void initListener() {
        mSkRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sk_btn_tj:
                        initData();
                        break;
                    case R.id.sk_btn_sj:
                        /**
                         p3.	事件
                         a)	请求地址：/v1.0/matchDetail/event/matchID/{matchID}
                         b)	请求方式:Get
                         c)	请求参数说明：matchID:比赛id
                         */
                        SSQSApplication.apiClient(0).getMatchDetailsEvent(matchId, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    SJData = (List<SKSJBean>) result.getData();

                                    if (SJData != null) {
                                        processDatasj(SJData);
                                    }
                                } else {
                                    mSkNoDataView.setVisibility(View.VISIBLE);
                                    mSkDataView.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;
                    case R.id.sk_btn_zr:
                        /**
                         * 2.	阵容
                         a)	请求地址：
                         /v1.0/matchDetail/play/matchID/{matchID}
                         b)	请求方式:
                         Get
                         c)	请求参数说明：
                         matchID:比赛id
                         */
                        SSQSApplication.apiClient(0).getMatchDetailsPlay(matchId, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    SKZRBean bean = (SKZRBean) result.getData();

                                    if (bean != null) {
                                        processDataZr(bean);
                                    }
                                } else {
                                    mSkNoDataView.setVisibility(View.VISIBLE);
                                    mSkDataView.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void processDataZr(SKZRBean data) {
        if (data == null) {
            mSkNoDataView.setVisibility(View.VISIBLE);
            mSkDataView.setVisibility(View.GONE);
        } else {
            mSkNoDataView.setVisibility(View.GONE);
            mSkDataView.setVisibility(View.VISIBLE);
            mSkSjImage.setVisibility(View.GONE);

            mSkZRListview1.setAdapter(new SkZrAdapter(context, data.hPlayers, data.aPlayers));
            mSkZRListview2.setAdapter(new SkZrAdapterReplace(context, data.hSubPlayers, data.aSubPlayers));
            mSkZRFristMain.setText(data.home);
            mSkZRFristSeond.setText(data.away);
            ListScrollUtil.setListViewHeightBasedOnChildren(mSkZRListview1);
            ListScrollUtil.setListViewHeightBasedOnChildren(mSkZRListview2);
            mSkLv.setVisibility(View.GONE);
            mSkZr.setVisibility(View.VISIBLE);
        }
    }

    private void processDatasj(List<SKSJBean> data) {
        mSkSjRb.setChecked(true);
        if (data == null) {
            mSkNoDataView.setVisibility(View.VISIBLE);
            mSkDataView.setVisibility(View.GONE);
        } else {
            mSkNoDataView.setVisibility(View.GONE);
            mSkDataView.setVisibility(View.VISIBLE);
            mSkLv.setVisibility(View.VISIBLE);
            mSkZr.setVisibility(View.GONE);
            mSkSjImage.setVisibility(View.VISIBLE);
            mSkLv.setAdapter(new MySkAdapterSJ(context, data));
            ListScrollUtil.setListViewHeightBasedOnChildren(mSkLv);
        }
    }

    private void initData() {
        /**
         * 1.	统计
         a)	请求地址：
         /v1.0/matchDetail/count/matchID/{matchID}
         b)	请求方式:
         get
         c)	请求参数说明：
         matchID:比赛id
         */

        SSQSApplication.apiClient(0).getMatchCountById(matchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<SKTJBean> items = (List<SKTJBean>) result.getData();

                    if (items != null) {
                        processData(items);
                    }
                } else {
                    mSkNoDataView.setVisibility(View.VISIBLE);
                    mSkDataView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void processData(List<SKTJBean> data) {
        mSkTjRb.setChecked(true);
        if (data == null) {
            mSkNoDataView.setVisibility(View.VISIBLE);
            mSkDataView.setVisibility(View.GONE);
        } else {
            mSkNoDataView.setVisibility(View.GONE);
            mSkDataView.setVisibility(View.VISIBLE);
            mSkLv.setVisibility(View.VISIBLE);
            mSkZr.setVisibility(View.GONE);
            mSkSjImage.setVisibility(View.GONE);
            mSkLv.setAdapter(new MySkAdapter(context, data));
            ListScrollUtil.setListViewHeightBasedOnChildren(mSkLv);
        }
    }

    private View initView(Context context) {
        View view = View.inflate(context, R.layout.lv_scene, null);

        mSkRg = ButterKnife.findById(view, R.id.sk_btn_rg);
        mSkTjRb = ButterKnife.findById(view, R.id.sk_btn_tj);
        mSkSjRb = ButterKnife.findById(view, R.id.sk_btn_sj);
        mSkZrRb = ButterKnife.findById(view, R.id.sk_btn_zr);
        mSkLv = ButterKnife.findById(view, R.id.sk_lv);
        mSkZr = ButterKnife.findById(view, R.id.sk_zr);

        mSkSjImage = ButterKnife.findById(view, R.id.sk_sj_img);
        mSkDataView = ButterKnife.findById(view, R.id.sk_data_view);
        mSkNoDataView = ButterKnife.findById(view, R.id.data_empty);

        mSkZRFristMain = ButterKnife.findById(view, R.id.sk_zr_frist_main);
        mSkZRFristSeond = ButterKnife.findById(view, R.id.sk_zr_frist_second);
        mSkZRListview1 = ButterKnife.findById(view, R.id.sk_zr_listview1);
        mSkZRListview2 = ButterKnife.findById(view, R.id.sk_zr_listview2);

        return view;
    }
}
