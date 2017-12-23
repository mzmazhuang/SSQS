package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyLvAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.InfoPLBean;
import com.dading.ssqs.utils.LogUtil;

import java.util.ArrayList;
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
public class MatchInfoPl {
    private static final String TAG = "MatchInfoPl";
    private final int matchId;
    private Context context;
    public final View mRootView;
    private ListView mLv;
    private ArrayList<String> mList;
    private RadioGroup mRg;
    /*private       ListView          mQxLv1;
    private       ListView          mQxLv2;
    private       LinearLayout      mLyQx;*/
    private RelativeLayout mPlNoDataView;
    private LinearLayout mPlDataView;
    private List<InfoPLBean> mBean;
    private TextView mQxLvBottom;

    public MatchInfoPl(Context context, int matchId) {
        this.context = context;
        this.matchId = matchId;
        mRootView = initView(context);
        initListner();
    }

    private View initView(final Context context) {
        View view = View.inflate(context, R.layout.lv_odds, null);

        mPlNoDataView = ButterKnife.findById(view, R.id.data_empty);
        mPlDataView = ButterKnife.findById(view, R.id.pl_data_view);

        mRg = ButterKnife.findById(view, R.id.pl_rg);
        mLv = ButterKnife.findById(view, R.id.pl_listview);
       /* mLyQx = ButterKnife.findById(view, R.id.pl_ly_qx);
        mQxLv1 = ButterKnife.findById(view, R.id.pl_qx_listview1);
        mQxLv2 = ButterKnife.findById(view, R.id.pl_qx_listview2);*/
        mQxLvBottom = ButterKnife.findById(view, R.id.pl_cb_group_bottom);

        /**
         * a)	请求地址：
         /v1.0/pay/type/{type}/matchID/{matchID}/page/{page}/count/{count}

         * c)	请求参数说明：
         Type: 赔率类型ID ={1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小}
         matchID: 比赛结果ID
         Page : 第几页，当前页码8
         Count: 页数
         */
        getData(matchId, 1);

        mRg.check(R.id.pl_btn_op);
        return view;
    }

    private void setPL(CcApiResult.ResultInfoPLPage page, Context context) {
        mBean = page.getItems();
        if (mBean.size() == 0) {
            mPlDataView.setVisibility(View.GONE);
            mPlNoDataView.setVisibility(View.VISIBLE);
            mQxLvBottom.setVisibility(View.GONE);
        } else {
            mPlDataView.setVisibility(View.VISIBLE);
            mPlNoDataView.setVisibility(View.GONE);
            mLv.setVisibility(View.VISIBLE);
            mLv.setAdapter(new MyLvAdapter(context, mBean));
            mQxLvBottom.setVisibility(View.VISIBLE);
            LogUtil.util(TAG, "倾向隐藏");
            // mLyQx.setVisibility(View.GONE);
        }
    }

    private void initListner() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.pl_btn_op:
                        getData(matchId, 1);
                        break;
                    case R.id.pl_btn_yp:
                        getData(matchId, 2);
                        break;
                    case R.id.pl_btn_dx:
                        getData(matchId, 3);
                        break;
                    case R.id.pl_btn_kl:
                        mPlDataView.setVisibility(View.GONE);
                        mPlNoDataView.setVisibility(View.VISIBLE);
                        mQxLvBottom.setVisibility(View.GONE);
                       /* mLyQx.setVisibility(View.GONE);
                        LogUtil.util(TAG, "倾向隐藏");
                        mLv.setVisibility(View.VISIBLE);*/
                        break;
                    case R.id.pl_btn_qx:
                        mPlDataView.setVisibility(View.GONE);
                        mPlNoDataView.setVisibility(View.VISIBLE);
                        mQxLvBottom.setVisibility(View.GONE);
                         /*   mQxLv1.setAdapter(new MyQxAdapter(context, list5));
                            mQxLv2.setAdapter(new MyQxAdapter2(context, list5));*/
                      /*  ListScrollUtil.setListViewHeightBasedOnChildren(mQxLv1);
                        ListScrollUtil.setListViewHeightBasedOnChildren(mQxLv2);
                        mLyQx.setVisibility(View.GONE);*/
                        LogUtil.util(TAG, "倾向显示");
                        mLv.setVisibility(View.GONE);
                        LogUtil.util(TAG, "倾向隐藏");

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getData(int matchId, int type) {
        SSQSApplication.apiClient(0).getPayTypeList(matchId, type, 1, 100, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultInfoPLPage page = (CcApiResult.ResultInfoPLPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        setPL(page, context);
                    }
                } else {
                    mPlDataView.setVisibility(View.GONE);
                    mPlNoDataView.setVisibility(View.VISIBLE);
                    LogUtil.util(TAG, result.getMessage() + "比赛详情赔率请求失败.失败信息");
                }
            }
        });
    }
}
