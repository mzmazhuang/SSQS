package com.dading.ssqs.controllar.withdrawdetail;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.WithDrawDetailBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/9/13.
 */
public class WithDrawFragmentDetailAdapter extends BaseQuickAdapter<WithDrawDetailBean> {
    String TAG = "RechargeFragmentDetailAdapter";
    private int mI;

    public WithDrawFragmentDetailAdapter (int layoutResId, List<WithDrawDetailBean> items) {
        super(layoutResId, items);
        mI = 0;
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder, WithDrawDetailBean itemsBean) {
        mI++;
        LogUtil.util(TAG, "item 数------------------------------:" + mI);
        /**
         * 0：审核中，1：失败 2：提现成功
         */
        String createTime = "";
        switch (itemsBean.getStatus()) {
            case 0:
                createTime = "审核中 " + itemsBean.getCreateDate( );
                break;
            case 1:
                createTime = "失败 " + itemsBean.getCreateDate();
                break;
            case 2:
                createTime = "提现成功 " + itemsBean.getCreateDate();
                break;
        }
        String amount = itemsBean.getMoney( );
        baseViewHolder.setText(R.id.account_detail_item_item, itemsBean.getOrderID( ))
                .setText(R.id.account_detail_item_recharge_num,"提现金额")
                .setText(R.id.account_detail_item_amount, ((Integer.parseInt(amount.replaceAll(",","")) >0 ? "+" :"") + itemsBean.getMoney( )))
                .setText(R.id.account_detail_item_balance, itemsBean.getBankCard())
                .setText(R.id.account_detail_item_time, createTime);

        TextView tv = baseViewHolder.getView(R.id.account_detail_item_amount);
        tv.setTextColor(UIUtils.getColor((Integer.parseInt(amount.replaceAll(",",""))>0 ? R.color.orange_tv :R.color.blue_light3)));
    }
}
