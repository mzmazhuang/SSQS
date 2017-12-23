package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.ProxyCmmsionsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/8/14.
 */
public class ProxyCommissionLvAdapter extends BaseAdapter implements ListAdapter {
    private final Context                                    context;
    private final List<ProxyCmmsionsBean.UsersBean> data;

    public ProxyCommissionLvAdapter (Context context, List<ProxyCmmsionsBean.UsersBean> users) {
        this.context = context;
        this.data = users;
    }

    @Override
    public int getCount ( ) {
        if (data != null) {
            return data.size( );
        }
        return 0;
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_proxy_commission, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag( );
        }
        ProxyCmmsionsBean.UsersBean bean = data.get(position);

        holder.mItemProxyCommisionsUsername.setText(bean.getUserName());
        holder.mItemProxyCommisionsFee.setText(bean.getFee());
        holder.mItemProxyCommisionsAccount.setText(bean.getAmount());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_proxy_commisions_username)
        TextView mItemProxyCommisionsUsername;
        @Bind(R.id.item_proxy_commisions_fee)
        TextView mItemProxyCommisionsFee;
        @Bind(R.id.item_proxy_commisions_account)
        TextView mItemProxyCommisionsAccount;
        ViewHolder (View view) {
            ButterKnife.bind(this, view);
        }
    }
}
