package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.cells.BasketBallPassItemCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 */

public class BasketBallPassAdapter extends RecyclerView.Adapter<BasketBallPassAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallBasketBallBean.ScrollBaksetBallItems> list;
    private int dataId;
    private List<ScrollBallBasketBallDefaultFragment.MergeBean> focusList;
    private BasketScrollBallItemAdapter.OnItemClickListener listener;

    public BasketBallPassAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setListener(BasketScrollBallItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setBeanId(int id) {
        dataId = id;
    }

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public void setData(List<ScrollBallBasketBallBean.ScrollBaksetBallItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public BasketBallPassAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BasketBallPassItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(BasketBallPassAdapter.ItemViewHolder holder, int position) {
        ScrollBallBasketBallBean.ScrollBaksetBallItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private BasketBallPassItemCell cell;

        public ItemViewHolder(BasketBallPassItemCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallBasketBallBean.ScrollBaksetBallItems bean) {
            this.cell.setListener(listener);
            this.cell.setBeanId(dataId);
            this.cell.setTime(bean.getTime());
            this.cell.setTitle(bean.getTitle() + "　VS　" + bean.getByTitle());
            this.cell.setData(bean);
            this.cell.setFocus(focusList);
        }
    }
}
