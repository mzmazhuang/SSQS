package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/8/18.
 */
public class WXDFRecycleYHAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "WXDFRecycleYHAdapter";
    private final Context context;
    private final List<WXDFBean.InfoBean> data;
    private WXDFBean.InfoBean mCheckBean;

    public WXDFRecycleYHAdapter(Context context, List<WXDFBean.InfoBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.wx_daifu_bank_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WXDFBean.InfoBean bean = data.get(position);

        holder.mBankItemCb.setChecked(bean.isChecked());

        if (bean != null) {
            String bank = "" + bean.getName();
            holder.mBankNameTv.setText(bank);

            String bankAddr = "" + bean.getBankAddress();
            holder.mBankAddressTv.setText(bankAddr);

            String cardNum = "" + bean.getCardNumber();
            holder.mCardNumberTv.setText(cardNum);

            String cardOwner = "" + bean.getOwner();
            holder.mCardOwner.setText(cardOwner);
        }

        holder.mBankItemCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isChecked()) {
                    bean.setChecked(false);
                    mCheckBean = null;
                    Logger.INSTANCE.d(TAG, "返回数据是------------------------------:空的");
                } else {
                    for (WXDFBean.InfoBean beas : data) {
                        beas.setChecked(false);
                    }
                    bean.setChecked(true);
                    mCheckBean = bean;
                    Logger.INSTANCE.d(TAG, "返回数据是------------------------------:bean");
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    public WXDFBean.InfoBean getCheckBean() {
        return mCheckBean;
    }

    static class ViewHolder {
        @Bind(R.id.bank_name_tv)
        TextView mBankNameTv;
        @Bind(R.id.card_number_tv)
        TextView mCardNumberTv;
        @Bind(R.id.card_owner)
        TextView mCardOwner;
        @Bind(R.id.bank_address_tv)
        TextView mBankAddressTv;
        @Bind(R.id.bank_item_cb)
        CheckBox mBankItemCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

