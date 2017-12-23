package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.cells.BasketBallDetailsItemCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 */

public class BasketBallDetailsItemAdapter extends RecyclerView.Adapter<BasketBallDetailsItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<BasketBallDetailsActivity.BasketData> list;
    private BasketBallDetailsItemCell.OnItemClickListener itemClickListener;
    private List<BasketBallDetailsActivity.BasketData.BasketItemData> focusList;

    public BasketBallDetailsItemAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setFocus(List<BasketBallDetailsActivity.BasketData.BasketItemData> list) {
        focusList = list;
    }

    public void setItemClickListener(BasketBallDetailsItemCell.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<BasketBallDetailsActivity.BasketData> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void refreshData() {
        List<BasketBallDetailsActivity.BasketData> list = new ArrayList<>();
        list.addAll(this.list);
        setData(list);
    }

    public List<BasketBallDetailsActivity.BasketData> getData() {
        return list;
    }

    @Override
    public BasketBallDetailsItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BasketBallDetailsItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(BasketBallDetailsItemAdapter.ItemViewHolder holder, int position) {
        BasketBallDetailsActivity.BasketData bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private BasketBallDetailsItemCell cell;

        public ItemViewHolder(BasketBallDetailsItemCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final BasketBallDetailsActivity.BasketData bean) {
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
        void onClick(BasketBallDetailsActivity.BasketData bean);
    }
}
