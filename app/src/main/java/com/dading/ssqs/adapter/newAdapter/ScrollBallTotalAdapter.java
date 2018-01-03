package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallTotalBean;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.cells.TotalAdmissionCell;
import com.dading.ssqs.cells.TotalItemCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallTotalAdapter extends RecyclerView.Adapter<ScrollBallTotalAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallTotalBean> list;
    private ScrollBallCell.OnReadyListener readyListener;
    private String openTitle;
    private List<ScrollBallTotalFragment.MergeBean> foucusList;
    private TotalItemCell.OnItemClickListener listener;
    private boolean isScroll=false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public void setReadyListener(ScrollBallCell.OnReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public void setListener(TotalItemCell.OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setOpenTitle(String openTitle) {
        this.openTitle = openTitle;
    }

    public ScrollBallTotalAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallTotalBean> data) {
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

    public void setFocus(List<ScrollBallTotalFragment.MergeBean> list) {
        foucusList = list;
    }


    public void refreshData() {
        List<ScrollBallFootBallTotalBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    public List<ScrollBallFootBallTotalBean> getData() {
        return list;
    }

    @Override
    public ScrollBallTotalAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new TotalAdmissionCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallTotalAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallTotalBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //足球-总入球
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TotalAdmissionCell cell;

        public ItemViewHolder(TotalAdmissionCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(ScrollBallFootBallTotalBean bean) {
            this.cell.setTitle(bean.getTitle().getTitle());
            this.cell.setData(bean,isScroll);
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
