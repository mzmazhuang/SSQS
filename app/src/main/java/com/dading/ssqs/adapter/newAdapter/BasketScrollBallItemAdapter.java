package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.cells.BasketScrollBallItemCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class BasketScrollBallItemAdapter extends RecyclerView.Adapter<BasketScrollBallItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallBasketBallBean.ScrollBaksetBallItems> list;
    private int dataId;
    private List<ScrollBallBasketBallDefaultFragment.MergeBean> focusList;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBeanId(int id) {
        dataId = id;
    }

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public BasketScrollBallItemAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallBasketBallBean.ScrollBaksetBallItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }


    @Override
    public BasketScrollBallItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BasketScrollBallItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(BasketScrollBallItemAdapter.ItemViewHolder holder, int position) {
        ScrollBallBasketBallBean.ScrollBaksetBallItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private BasketScrollBallItemCell cell;

        public ItemViewHolder(BasketScrollBallItemCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(final ScrollBallBasketBallBean.ScrollBaksetBallItems bean) {
            this.cell.setListener(listener);
            this.cell.setBeanId(dataId);
            this.cell.setData(bean);
            this.cell.setFocus(focusList);

            this.cell.setInfoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onInfoClick(bean.getId(), title);
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
        boolean onItemClick(int id, ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem bean, ScrollBallBasketBallBean.ScrollBaksetBallItems items, boolean isAdd, boolean isHome, int position);

        void onInfoClick(int matchId, String title);
    }
}
