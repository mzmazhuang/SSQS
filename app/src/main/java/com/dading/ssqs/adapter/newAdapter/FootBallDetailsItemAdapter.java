package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.activity.ScrollFootBallDetailsActivity;
import com.dading.ssqs.cells.FootBallDetailsItemCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/29.
 */

public class FootBallDetailsItemAdapter extends RecyclerView.Adapter<FootBallDetailsItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollFootBallDetailsActivity.FootData> list;
    private FootBallDetailsItemCell.OnItemClickListener itemClickListener;
    private List<ScrollFootBallDetailsActivity.FootData.FootItemData> focusList;

    public FootBallDetailsItemAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setItemClickListener(FootBallDetailsItemCell.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setFocus(List<ScrollFootBallDetailsActivity.FootData.FootItemData> list) {
        focusList = list;
    }

    public void setData(List<ScrollFootBallDetailsActivity.FootData> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<ScrollFootBallDetailsActivity.FootData> getData() {
        return list;
    }

    public void refreshData() {
        List<ScrollFootBallDetailsActivity.FootData> list = new ArrayList<>();
        list.addAll(this.list);
        setData(list);
    }

    @Override
    public FootBallDetailsItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new FootBallDetailsItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(FootBallDetailsItemAdapter.ItemViewHolder holder, int position) {
        ScrollFootBallDetailsActivity.FootData bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private FootBallDetailsItemCell cell;

        public ItemViewHolder(FootBallDetailsItemCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final ScrollFootBallDetailsActivity.FootData bean) {
            this.cell.setData(bean, itemClickListener, focusList);
            this.cell.setCollectionClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(bean);
                    }
                }
            });
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(ScrollFootBallDetailsActivity.FootData bean);
    }
}
