package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.BankBean;
import com.dading.ssqs.cells.SelectBankListCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class SelectBankListAdapter extends RecyclerView.Adapter<SelectBankListAdapter.ItemViewHolder> {

    private Context mContext;
    private List<BankBean> list;

    public SelectBankListAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(List<BankBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public SelectBankListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new SelectBankListCell(mContext));
    }

    @Override
    public void onBindViewHolder(SelectBankListAdapter.ItemViewHolder holder, int position) {
        BankBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private SelectBankListCell cell;

        public ItemViewHolder(SelectBankListCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final BankBean bean) {
            this.cell.setIconResource(bean.logoUrl);
            this.cell.setTitle(bean.name);
            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(bean);
                    }
                }
            });
        }
    }

    private OnBankItemClickListener listener;

    public void setListener(OnBankItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnBankItemClickListener {
        void onItemClick(BankBean bean);
    }
}
