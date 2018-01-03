package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.cells.BoDanCell;
import com.dading.ssqs.cells.BoDanChildCell;
import com.dading.ssqs.cells.ScrollBallCell;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/7.
 */

public class ScrollBallBoDanAdapter extends RecyclerView.Adapter<ScrollBallBoDanAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallBoDanBean> list;
    private ScrollBallCell.OnReadyListener readyListener;
    private String openTitle;
    private BoDanChildCell.OnItemClickListener listener;
    private List<ScrollBallBoDanFragment.MergeBean> foucusList;
    private boolean isScroll = false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public void setReadyListener(ScrollBallCell.OnReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    public void setFocus(List<ScrollBallBoDanFragment.MergeBean> list) {
        foucusList = list;
    }

    public void setListener(BoDanChildCell.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOpenTitle(String openTitle) {
        this.openTitle = openTitle;
    }

    public ScrollBallBoDanAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void refreshData() {
        List<ScrollBallFootBallBoDanBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    public void setList(List<ScrollBallFootBallBoDanBean> data) {
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

    public List<ScrollBallFootBallBoDanBean> getData() {
        return list;
    }


    @Override
    public ScrollBallBoDanAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new BoDanCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallBoDanAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallBoDanBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //足球-波胆
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private BoDanCell cell;

        public ItemViewHolder(BoDanCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallBoDanBean bean) {
            this.cell.setFocus(foucusList);
            this.cell.setTitle(bean.getTitle().getTitle());
            this.cell.setData(bean, isScroll);
            this.cell.setTopClickListener(readyListener);
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
