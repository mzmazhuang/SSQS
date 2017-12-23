package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 */

public class BasketBallDetailsAdapter extends RecyclerView.Adapter<BasketBallDetailsAdapter.ItemViewHolder> {

    private Context mContext;
    private List<String> list;

    public BasketBallDetailsAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(List<String> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public BasketBallDetailsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BasketBallDetailsAdapter.ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
