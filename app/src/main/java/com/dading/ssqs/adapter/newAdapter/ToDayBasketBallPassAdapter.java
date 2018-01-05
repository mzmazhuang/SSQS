package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.cells.BasketBallPassCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/11.
 */

public class ToDayBasketBallPassAdapter extends RecyclerView.Adapter<ToDayBasketBallPassAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallBean> list;
    private ScrollBallCell.OnReadyListener readyListener;
    private String openTitle;
    private ScrollBallItemAdapter.OnItemClickListener itemClickListener;
    private List<ScrollBallDefaultFragment.MergeBean> foucusList;

    public ToDayBasketBallPassAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallBean> data) {
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
        List<ScrollBallFootBallBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    public void setFocus(List<ScrollBallDefaultFragment.MergeBean> list) {
        foucusList = list;
    }

    public void setItemClickListener(ScrollBallItemAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<ScrollBallFootBallBean> getData() {
        return list;
    }

    public void setReadyListener(ScrollBallCell.OnReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public void setOpenTitle(String openTitle) {
        this.openTitle = openTitle;
    }

    @Override
    public ToDayBasketBallPassAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ScrollBallCell(mContext));
    }

    @Override
    public void onBindViewHolder(ToDayBasketBallPassAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ScrollBallCell cell;

        public ItemViewHolder(ScrollBallCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallBean bean) {
            this.cell.setFocus(foucusList);
            this.cell.setBean(bean, 2);
            this.cell.setTopClickListener(readyListener);
            this.cell.setItemClickListener(itemClickListener);

            if (bean.getTitle().getTitle().equals(openTitle)) {
                this.cell.openItemLayout();
                openTitle = "";
            } else {
                this.cell.checkShow();
            }
        }
    }
}
