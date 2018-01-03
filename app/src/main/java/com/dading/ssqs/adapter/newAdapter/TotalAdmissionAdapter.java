package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallTotalBean;
import com.dading.ssqs.cells.TotalChildCell;
import com.dading.ssqs.cells.TotalItemCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class TotalAdmissionAdapter extends RecyclerView.Adapter<TotalAdmissionAdapter.ItemViewHolder> {

    public Context mContext;
    private List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems> list;
    private List<ScrollBallTotalFragment.MergeBean> focusList;
    private TotalItemCell.OnItemClickListener listener;
    private boolean isScroll = false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public TotalAdmissionAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setFocus(List<ScrollBallTotalFragment.MergeBean> list) {
        focusList = list;
    }

    public void setListener(TotalItemCell.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public TotalAdmissionAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new TotalChildCell(mContext));
    }

    @Override
    public void onBindViewHolder(TotalAdmissionAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TotalChildCell cell;

        public ItemViewHolder(TotalChildCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItems bean) {
            cell.setTime(bean.getTime());
            cell.setTitle(bean,isScroll);
            cell.setData(bean, focusList, listener);
        }
    }
}
