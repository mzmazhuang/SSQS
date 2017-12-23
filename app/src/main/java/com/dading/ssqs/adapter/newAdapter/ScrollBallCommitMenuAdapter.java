package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.cells.ScrollBallCommitMenuCell;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/2.
 */

public class ScrollBallCommitMenuAdapter extends RecyclerView.Adapter<ScrollBallCommitMenuAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallCommitMenuView.MergeBean> list;
    private int currPosition = -1;
    private int type;

    public ScrollBallCommitMenuAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCurrPosition(int currPosition) {
        this.currPosition = currPosition;
    }

    public void setData(List<ScrollBallCommitMenuView.MergeBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<ScrollBallCommitMenuView.MergeBean> getData() {
        return list;
    }

    public void refreshData(int position) {
        List<ScrollBallCommitMenuView.MergeBean> list = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            if (i != position) {
                list.add(this.list.get(i));
            }
        }
        setData(list);
    }


    @Override
    public ScrollBallCommitMenuAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ScrollBallCommitMenuCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallCommitMenuAdapter.ItemViewHolder holder, int position) {
        ScrollBallCommitMenuView.MergeBean bean = list.get(position);
        holder.setData(position, bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ScrollBallCommitMenuCell cell;

        public ItemViewHolder(ScrollBallCommitMenuCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final int position, final ScrollBallCommitMenuView.MergeBean bean) {
            this.cell.setPosition(position);
            this.cell.setType(type);
            this.cell.setMoney(bean.getMoney(), currPosition == position, bean.getBeanStr());
            this.cell.setRans1Title(bean.getItemsTitle());
            this.cell.setRans2Title(bean.getItemsByTitle());
            this.cell.setRans3Title(bean.getOddsName());
            this.cell.setOdds(bean.getBeanStr());
            this.cell.setListener(numberListener);
            this.cell.setDeleteOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(position, bean.getBeanId(), bean.getItemsId(), bean.getBeanStr());
                    }
                }
            });
        }
    }

    private OnMenuClickListener listener;

    public void setListener(OnMenuClickListener listener) {
        this.listener = listener;
    }

    public interface OnMenuClickListener {
        void onClick(int position, int dataId, int itemId, String value);
    }

    private OnNumberListener numberListener;

    public void setNumberListener(OnNumberListener numberListener) {
        this.numberListener = numberListener;
    }

    public interface OnNumberListener {
        void onReady(int position);
    }
}
