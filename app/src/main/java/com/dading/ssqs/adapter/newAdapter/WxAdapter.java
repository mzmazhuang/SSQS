package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.cells.WxCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class WxAdapter extends RecyclerView.Adapter<WxAdapter.ItemViewHolder> {

    private Context mContext;
    private List<WXDFBean.InfoBean> list;

    public WxAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(List<WXDFBean.InfoBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public WxAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new WxCell(mContext));
    }

    @Override
    public void onBindViewHolder(WxAdapter.ItemViewHolder holder, int position) {
        WXDFBean.InfoBean bean = list.get(position);
        holder.setData(bean, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        WxCell cell;

        public ItemViewHolder(WxCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final WXDFBean.InfoBean bean, int position) {
            this.cell.setImageResource(bean.getLogo());
            this.cell.setTitle(bean.getName());
            this.cell.setSubTitle(bean.getMfrom() + "-" + bean.getMto());
            if (position == list.size() - 1) {
                this.cell.isShowLine(false);
            } else {
                this.cell.isShowLine(true);
            }
            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(bean);
                    }
                }
            });
        }
    }

    public BankAdapter.OnRechargeClickListener listener;

    public void setListener(BankAdapter.OnRechargeClickListener listener) {
        this.listener = listener;
    }
}
