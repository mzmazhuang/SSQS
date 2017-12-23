package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallResultBean;
import com.dading.ssqs.cells.GameResultCell;
import com.dading.ssqs.cells.ScrollBallCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallResultAdapter extends RecyclerView.Adapter<ScrollBallResultAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallResultBean> list;

    public ScrollBallResultAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallResultBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public List<ScrollBallFootBallResultBean> getData() {
        return list;
    }

    @Override
    public ScrollBallResultAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new GameResultCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallResultAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallResultBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private GameResultCell cell;

        public ItemViewHolder(GameResultCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallResultBean bean) {
            this.cell.setTitle(bean.getTitle().getTitle());
            this.cell.setData(bean);
        }
    }
}
