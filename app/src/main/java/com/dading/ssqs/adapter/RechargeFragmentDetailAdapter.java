
package com.dading.ssqs.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.RechargeDetailBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

/**
 * 创建者     zcl
 * 创建时间   2017/7/7 15:45
 * 描述	      ${账户明细适配器}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class RechargeFragmentDetailAdapter extends BaseQuickAdapter<RechargeDetailBean> {
    String TAG = "RechargeFragmentDetailAdapter";
    private int mI;

    public RechargeFragmentDetailAdapter (int layoutResId, List<RechargeDetailBean> items) {
        super(layoutResId, items);
        mI = 0;
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder, RechargeDetailBean itemsBean) {
        mI++;
        Logger.INSTANCE.d(TAG, "item 数------------------------------:" + mI);
        /**
         * 0：审核中，1：失败 2：提现成功
         */
        String createTime = "";
        switch (itemsBean.getState( )) {
            case 0:
                createTime = "审核中  " + itemsBean.getCreateTime( );
                break;
            case 1:
                createTime = "失败  " + itemsBean.getCreateTime( );
                break;
            case 2:
                createTime = "提现成功  " + itemsBean.getCreateTime( );
                break;
        }
        int amount = itemsBean.getAmount( );
        baseViewHolder.setText(R.id.account_detail_item_item, itemsBean.getOrderID( ))
                .setText(R.id.account_detail_item_recharge_num,"充值金额")
                .setText(R.id.account_detail_item_amount, (amount > 0 ? "+" :"") + String.valueOf(itemsBean.getAmount( )))
                .setText(R.id.account_detail_item_balance, itemsBean.getTypeName())
                .setText(R.id.account_detail_item_time, createTime);

        TextView tv = baseViewHolder.getView(R.id.account_detail_item_amount);
        tv.setTextColor(UIUtils.getColor((amount > 0 ? R.color.orange_tv :R.color.blue_light3)));
    }

}
