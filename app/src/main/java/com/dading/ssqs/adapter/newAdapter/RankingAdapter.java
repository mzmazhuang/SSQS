package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.cells.RankingCell;
import com.dading.ssqs.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ItemViewHolder> {

    private Context mContext;
    private List<HomeBean.OrdersBeanX.OrdersBean> list;

    public RankingAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<HomeBean.OrdersBeanX.OrdersBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public RankingAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new RankingCell(mContext));
    }

    @Override
    public void onBindViewHolder(RankingAdapter.ItemViewHolder holder, int position) {
        HomeBean.OrdersBeanX.OrdersBean bean = list.get(position % list.size());
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RankingCell cell;

        public ItemViewHolder(RankingCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(HomeBean.OrdersBeanX.OrdersBean bean) {
            cell.setTitleText(bean.getUsername());
            cell.setSubTitle(bean.getMatchName());
            cell.setRightText(bean.getAmount());
        }
    }
}
