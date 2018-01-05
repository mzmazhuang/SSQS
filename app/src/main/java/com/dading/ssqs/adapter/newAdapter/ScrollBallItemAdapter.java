package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.cells.ScrollBallItemCell;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class ScrollBallItemAdapter extends RecyclerView.Adapter<ScrollBallItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallBean.ScrollBeanItems> list;
    private int dataId;
    private List<ScrollBallDefaultFragment.MergeBean> focusList;
    private int pageType = -1;

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public void setFocus(List<ScrollBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public void setBeanId(int id) {
        dataId = id;
    }

    public ScrollBallItemAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallBean.ScrollBeanItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addList(List<ScrollBallFootBallBean.ScrollBeanItems> data) {
        if (data != null) {
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void refresh() {
        List<ScrollBallFootBallBean.ScrollBeanItems> list = new ArrayList<>();
        list.addAll(this.list);
        setList(list);
    }


    @Override
    public ScrollBallItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ScrollBallItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScrollBallItemAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallBean.ScrollBeanItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ScrollBallItemCell cell;

        public ItemViewHolder(ScrollBallItemCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(ScrollBallFootBallBean.ScrollBeanItems bean) {
            this.cell.setListener(listener);
            this.cell.setFocus(focusList);
            this.cell.setBeanId(dataId);
            this.cell.setData(bean, pageType);
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        boolean onItemClick(int id, ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem bean, ScrollBallFootBallBean.ScrollBeanItems items, boolean isAdd, boolean isHome, int position);
    }
}
