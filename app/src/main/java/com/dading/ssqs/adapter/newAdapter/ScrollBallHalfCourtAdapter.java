package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallHalfCourtBean;
import com.dading.ssqs.cells.HalfCourtCell;
import com.dading.ssqs.cells.HalfCourtItemCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallHalfCourtAdapter extends RecyclerView.Adapter<ScrollBallHalfCourtAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallHalfCourtBean> list;
    private ScrollBallCell.OnReadyListener readyListener;
    private String openTitle;
    private List<ScrollBallHalfCourtFragment.MergeBean> foucusList;
    private HalfCourtItemCell.OnItemClickListener listener;

    public void setList(List<ScrollBallFootBallHalfCourtBean> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setListener(HalfCourtItemCell.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setFocus(List<ScrollBallHalfCourtFragment.MergeBean> list) {
        foucusList = list;
    }

    public void setReadyListener(ScrollBallCell.OnReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public void setOpenTitle(String openTitle) {
        this.openTitle = openTitle;
    }

    public ScrollBallHalfCourtAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void refreshData() {
        List<ScrollBallFootBallHalfCourtBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    @Override
    public ScrollBallHalfCourtAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new HalfCourtCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallHalfCourtAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallHalfCourtBean bean = list.get(position);
        holder.setData(bean);
    }

    public List<ScrollBallFootBallHalfCourtBean> getData() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private HalfCourtCell cell;

        public ItemViewHolder(HalfCourtCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallHalfCourtBean bean) {
            this.cell.setTitle(bean.getTitle().getTitle());
            this.cell.setData(bean);
            this.cell.setTopClickListener(readyListener);
            this.cell.setFocus(foucusList);
            this.cell.setListener(listener);

            if (bean.getTitle().getTitle().equals(openTitle)) {
                this.cell.openItemLayout();
                openTitle = "";
            } else {
                this.cell.checkShow();
            }
        }
    }
}
