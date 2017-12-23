package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallHalfCourtBean;
import com.dading.ssqs.cells.HalfCourtChildCell;
import com.dading.ssqs.cells.HalfCourtItemCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class HalfCourtAdapter extends RecyclerView.Adapter<HalfCourtAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems> list;
    private List<ScrollBallHalfCourtFragment.MergeBean> focusList;
    private HalfCourtItemCell.OnItemClickListener listener;

    public void setFocus(List<ScrollBallHalfCourtFragment.MergeBean> list) {
        focusList = list;
    }

    public void setListener(HalfCourtItemCell.OnItemClickListener listener) {
        this.listener = listener;
    }

    public HalfCourtAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public HalfCourtAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new HalfCourtChildCell(mContext));
    }

    @Override
    public void onBindViewHolder(HalfCourtAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private HalfCourtChildCell cell;

        public ItemViewHolder(HalfCourtChildCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems bean) {
            cell.setTime(bean.getTime());
            cell.setTitle(bean.getTitle() + "　VS　" + bean.getByTitle());
            cell.setData(bean, focusList, listener);
        }
    }
}
