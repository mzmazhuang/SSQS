package com.dading.ssqs.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.AccountDetailBean;
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
public class AccountDetailAdapter extends BaseQuickAdapter<AccountDetailBean> {
    String TAG = "AccountDetailAdapter";
    private int mI;

    public AccountDetailAdapter(int layoutResId, List<AccountDetailBean> data) {
        super(layoutResId, data);
        mI = 0;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AccountDetailBean itemsBean) {
        mI++;
        Logger.INSTANCE.d(TAG, "item 数------------------------------:" + mI);
        /**
         * 1:rmb
         2:钻石，
         3：球币
         */
        String value = String.valueOf(itemsBean.getBalance());
        switch (itemsBean.getPriceType()) {
            case 1:
                value = "RMB:" + value;
                break;
            case 2:
                value = "钻石:" + value;
                break;
            case 3:
                value = "球币:" + value;
                break;
        }
        int amount = itemsBean.getAmount();
        baseViewHolder.setText(R.id.account_detail_item_item, itemsBean.getItem())
                .setVisible(R.id.account_detail_item_recharge_num, false)
                .setText(R.id.account_detail_item_amount, (amount > 0 ? "+" : "") + String.valueOf(itemsBean.getAmount()))
                .setText(R.id.account_detail_item_balance, value)
                .setText(R.id.account_detail_item_time, itemsBean.getCreateDate());
        TextView tv = baseViewHolder.getView(R.id.account_detail_item_amount);
        tv.setTextColor(UIUtils.getColor((amount > 0 ? R.color.orange_tv : R.color.blue_light3)));
    }


}
