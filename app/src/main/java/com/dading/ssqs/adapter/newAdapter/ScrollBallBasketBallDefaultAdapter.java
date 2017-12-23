package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.cells.BasketScrollBallCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 */

public class ScrollBallBasketBallDefaultAdapter extends RecyclerView.Adapter<ScrollBallBasketBallDefaultAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallBasketBallBean> list;
    private ScrollBallCell.OnReadyListener readyListener;
    private String openTitle;
    private BasketScrollBallItemAdapter.OnItemClickListener itemClickListener;
    private List<ScrollBallBasketBallDefaultFragment.MergeBean> foucusList;

    public ScrollBallBasketBallDefaultAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> foucusList) {
        this.foucusList = foucusList;
    }

    public void setItemClickListener(BasketScrollBallItemAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setReadyListener(ScrollBallCell.OnReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public void setOpenTitle(String openTitle) {
        this.openTitle = openTitle;
    }

    public void setList(List<ScrollBallBasketBallBean> data) {
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

    public void refreshData() {
        List<ScrollBallBasketBallBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    public List<ScrollBallBasketBallBean> getData() {
        return list;
    }

    @Override
    public ScrollBallBasketBallDefaultAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BasketScrollBallCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallBasketBallDefaultAdapter.ItemViewHolder holder, int position) {
        ScrollBallBasketBallBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private BasketScrollBallCell cell;

        public ItemViewHolder(BasketScrollBallCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallBasketBallBean bean) {
            this.cell.setFocus(foucusList);
            this.cell.setData(bean);
            this.cell.setTopClickListener(readyListener);
            this.cell.setOnItemListener(itemClickListener);

            if (bean.getTitle().getTitle().equals(openTitle)) {
                this.cell.openItemLayout();
                openTitle = "";
            } else {
                this.cell.checkShow();
            }
        }
    }
}
