package com.dading.ssqs.controllar.savantinfo;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.SavantInfoBean;

import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/6 14:39
 * 描述	      专家详情圆形progress
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantInfoStatetrendControllarSingle {
    private  SavantInfoBean.RatesEntity data;
    private       Context                               context;
    public        View                                  mRootView;
    private       TextView                              mTrend1;
    private       TextView                              mTrend2;
    private       ProgressBar                           mPb1;
    private       ProgressBar                           mPb2;
    private       ProgressBar                           mPb3;

    private       TextView                              mText1;
    private       TextView                              mText2;
    private       TextView                              mText3;

    public SavantInfoStatetrendControllarSingle(Context context, SavantInfoBean.RatesEntity listStr) {
        this.context = context;
        mRootView = initView(context);
        this.data = listStr;
        initData();
        initListener();
    }

    private View initView(Context context) {
        View view = View.inflate(context, R.layout.savant_info_state_trend_single, null);
        mTrend1 = ButterKnife.findById(view, R.id.savant_info_vp_trend1);
        mTrend2 = ButterKnife.findById(view, R.id.savant_info_vp_trend2);

        mPb1 = ButterKnife.findById(view, R.id.savant_info_vp_item_pb1);
        mPb2 = ButterKnife.findById(view, R.id.savant_info_vp_item_pb2);
        mPb3 = ButterKnife.findById(view, R.id.savant_info_vp_item_pb3);
        mText1 = ButterKnife.findById(view, R.id.savant_info_order_text1);
        mText2 = ButterKnife.findById(view, R.id.savant_info_order_text2);
        mText3 = ButterKnife.findById(view, R.id.savant_info_order_text3);

        return view;
    }

    private void initData() {
        //全场赛果", "当前让球", "全场大小","半场赛果","半场让球","半场大小"
        switch (data.payTypeID) {
            case 1:
                mTrend1.setText("全场赛果");
                break;
            case 2:
                mTrend1.setText("当前让球");
                break;
            case 3:
                mTrend1.setText("全场大小");
                break;
            case 4:
                mTrend1.setText("半场赛果");
                break;
            case 5:
                mTrend1.setText("半场让球");
                break;
            case 6:
                mTrend1.setText("半场大小");
                break;
            default:
                break;
        }
        mTrend2.setText(data.tag);

        if (data.rate != null && data.rate.size() > 0) {
            mPb1.setProgress(Integer.parseInt(data.rate.get(0)));
            mPb2.setProgress(Integer.parseInt(data.rate.get(1)));
            mPb3.setProgress(Integer.parseInt(data.rate.get(2)));
        }

        if (data.title != null && data.title.size() > 0) {
            mText1.setText(data.title.get(0));
            mText2.setText(data.title.get(1));
            mText3.setText(data.title.get(2));
        }

    }

    private void initListener() {
    }
}
