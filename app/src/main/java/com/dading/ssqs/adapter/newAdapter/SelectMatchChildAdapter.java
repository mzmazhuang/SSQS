package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.cells.SelectMatchChildCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/13.
 */

public class SelectMatchChildAdapter extends RecyclerView.Adapter<SelectMatchChildAdapter.ItemViewHolder> {

    private Context mContext;
    private List<GusessChoiceBean.FilterEntity> list;

    public SelectMatchChildAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<GusessChoiceBean.FilterEntity> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public SelectMatchChildAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new SelectMatchChildCell(mContext));
    }

    @Override
    public void onBindViewHolder(SelectMatchChildAdapter.ItemViewHolder holder, int position) {
        GusessChoiceBean.FilterEntity bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private SelectMatchChildCell cell;

        public ItemViewHolder(SelectMatchChildCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final GusessChoiceBean.FilterEntity bean) {
            this.cell.setTitle(bean.title);
            this.cell.setCount(bean.nums);
            this.cell.setCheck(bean.checked);

            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.checked = !cell.getCheck();

                    cell.setCheck(!cell.getCheck());
                }
            });
        }
    }
}
