package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.AccountDetailBean;
import com.dading.ssqs.utils.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/7/7.
 */
public class AccountDetailAdapter2 extends RecyclerView.Adapter<AccountDetailAdapter2.MyViewHolder> {
    private static final String TAG = "AccountDetailAdapter2";
    private final Context                                    content;
    private final List<AccountDetailBean> data;

    public AccountDetailAdapter2 (Context content, List<AccountDetailBean> items) {
        this.content = content;
        this.data = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(content).inflate(R.layout.account_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder (MyViewHolder holder, int position) {
        LogUtil.util(TAG, "返回数据是------------------------------:postion:" + position);
        AccountDetailBean bean = data.get(position);
        holder.mAccountDetailItemItem.setText(bean.getItem( ));
        holder.mAccountDetailItemAmount.setText(bean.getAmount( ) + "");
        holder.mAccountDetailItemBalance.setText(bean.getBalance( ) + "");
        holder.mAccountDetailItemTime.setText(bean.getCreateDate( ));
        holder.mAccountDetailItemRechargeNum.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount ( ) {
        LogUtil.util(TAG, "返回数据是------------------------------:" + data.size( ));
        return data.size( );
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.account_detail_item_item)
        TextView mAccountDetailItemItem;
        @Bind(R.id.account_detail_item_amount)
        TextView mAccountDetailItemAmount;
        @Bind(R.id.account_detail_item_recharge_num)
        TextView mAccountDetailItemRechargeNum;
        @Bind(R.id.account_detail_item_balance)
        TextView mAccountDetailItemBalance;
        @Bind(R.id.account_detail_item_time)
        TextView mAccountDetailItemTime;

        public MyViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
