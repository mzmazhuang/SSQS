package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.cells.SelectRechargeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/24.
 */

public class SelectRechargeMoneyAdapter extends RecyclerView.Adapter<SelectRechargeMoneyAdapter.ItemViewHolder> {

    private List<Integer> moneys;
    private Context mContext;

    private OnTextClickListener listener;
    private int select = 0;

    public void setListener(OnTextClickListener listener) {
        this.listener = listener;
    }

    public SelectRechargeMoneyAdapter(Context context) {
        mContext = context;
        moneys = new ArrayList<>();
    }

    public void setList(List<Integer> list) {
        if (list != null) {
            moneys.clear();
            moneys.addAll(list);
            notifyDataSetChanged();
        }
    }

    private void refreshData() {
        List<Integer> list = new ArrayList<>();
        list.addAll(moneys);
        setList(list);
    }

    @Override
    public SelectRechargeMoneyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new SelectRechargeItem(mContext));
    }

    @Override
    public void onBindViewHolder(SelectRechargeMoneyAdapter.ItemViewHolder holder, int position) {
        int text = moneys.get(position);
        holder.setData(text, position);
    }

    @Override
    public int getItemCount() {
        return moneys.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private SelectRechargeItem item;

        public ItemViewHolder(SelectRechargeItem item) {
            super(item);
            this.item = item;
        }

        public void setData(int text, final int position) {
            this.item.setText(text + "");
            if (position == select) {
                this.item.setCheck(true);
            } else {
                this.item.setCheck(false);
            }
            this.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setCheck(true);
                    select = position;

                    if (listener != null) {
                        listener.onClick(item.getText());
                    }

                    refreshData();
                }
            });
        }
    }

    public interface OnTextClickListener {
        void onClick(String text);
    }
}
