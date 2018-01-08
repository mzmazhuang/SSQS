package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.cells.BankCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ItemViewHolder> {

    private List<WXDFBean.InfoBean> list;
    private Context mContext;

    public BankAdapter(Context context) {
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
    public BankAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BankCell(mContext));
    }

    @Override
    public void onBindViewHolder(BankAdapter.ItemViewHolder holder, int position) {
        WXDFBean.InfoBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private BankCell cell;

        public ItemViewHolder(BankCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(final WXDFBean.InfoBean bean) {
            this.cell.setIconResource(bean.getLogo());
            this.cell.setBankName(bean.getName());
            this.cell.setBankNumber(bean.getCardNumber());
            this.cell.setBankPeople(bean.getOwner());
            this.cell.setBankSubText(bean.getBankAddress());
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

    private OnRechargeClickListener listener;

    public void setListener(OnRechargeClickListener listener) {
        this.listener = listener;
    }

    public interface OnRechargeClickListener {
        void onClick(WXDFBean.InfoBean bean);
    }
}
