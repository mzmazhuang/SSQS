package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.cells.SelectMatchCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/12.
 */

public class SelectMatchAdapter extends RecyclerView.Adapter<SelectMatchAdapter.ItemViewHolder> {

    private Context mContext;
    private List<GusessChoiceBean> list;

    public SelectMatchAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<GusessChoiceBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<GusessChoiceBean> getData() {
        return list;
    }

    @Override
    public SelectMatchAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new SelectMatchCell(mContext));
    }

    @Override
    public void onBindViewHolder(SelectMatchAdapter.ItemViewHolder holder, int position) {
        GusessChoiceBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private SelectMatchCell cell;

        public ItemViewHolder(SelectMatchCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(GusessChoiceBean bean) {
            this.cell.setIndex(bean.pinyin);
            this.cell.setData(bean.filter);
        }
    }
}
