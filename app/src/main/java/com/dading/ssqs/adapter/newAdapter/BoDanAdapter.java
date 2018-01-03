package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.cells.BoDanChildCell;
import com.dading.ssqs.cells.CommonBoDanCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class BoDanAdapter extends RecyclerView.Adapter<BoDanAdapter.ItemViewHolder> {
    private Context mContext;
    private List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems> list;
    private BoDanChildCell.OnItemClickListener listener;
    private List<ScrollBallBoDanFragment.MergeBean> focusList;
    private boolean isScroll = false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public void setFocus(List<ScrollBallBoDanFragment.MergeBean> list) {
        focusList = list;
    }

    public BoDanAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setListener(BoDanChildCell.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }


    @Override
    public BoDanAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new CommonBoDanCell(mContext));
    }

    @Override
    public void onBindViewHolder(BoDanAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private CommonBoDanCell cell;

        public ItemViewHolder(CommonBoDanCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems items) {
            this.cell.setTime(items.getTime());
            this.cell.setTitle(items, isScroll);
            this.cell.setTableRowAndColumn(items, listener, focusList);
        }
    }
}
