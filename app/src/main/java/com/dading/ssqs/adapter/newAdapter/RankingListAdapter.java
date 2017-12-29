package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.cells.RankingListCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.ItemViewHolder> {

    private Context mContext;
    private List<HomeBean.OrdersBeanX.OrdersBean> list;

    public RankingListAdapter(Context context) {
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

    public void addList(List<HomeBean.OrdersBeanX.OrdersBean> data) {
        if (data != null) {
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public RankingListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new RankingListCell(mContext));
    }

    @Override
    public void onBindViewHolder(RankingListAdapter.ItemViewHolder holder, int position) {
        HomeBean.OrdersBeanX.OrdersBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RankingListCell cell;

        public ItemViewHolder(RankingListCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(HomeBean.OrdersBeanX.OrdersBean bean) {
            this.cell.setTitle(bean.getUsername());
            this.cell.setMoney(bean.getAmount());
            this.cell.setType(bean.getMatchName());
        }
    }
}
