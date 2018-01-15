package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.PerferentialBean;
import com.dading.ssqs.cells.PreferentialActivitiesCell;
import com.dading.ssqs.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/15.
 */

public class PreferentialActivitiesAdapter extends RecyclerView.Adapter<PreferentialActivitiesAdapter.ItemViewHolder> {

    private Context mContext;
    private List<PerferentialBean> list;

    public PreferentialActivitiesAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(List<PerferentialBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public PreferentialActivitiesAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new PreferentialActivitiesCell(mContext));
    }

    @Override
    public void onBindViewHolder(PreferentialActivitiesAdapter.ItemViewHolder holder, int position) {
        PerferentialBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private PreferentialActivitiesCell cell;

        public ItemViewHolder(PreferentialActivitiesCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(final PerferentialBean bean) {
            this.cell.setIconResource(bean.getImageUrl());
            this.cell.setTitle(bean.getTitle());
            this.cell.setTime("活动时间:　" + DateUtils.changeFormater(bean.getStartDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd") + "至" + DateUtils.changeFormater(bean.getEndDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnClick(bean);
                    }
                }
            });
        }
    }

    private OnPreferClickListener listener;

    public void setListener(OnPreferClickListener listener) {
        this.listener = listener;
    }

    public interface OnPreferClickListener {
        void OnClick(PerferentialBean bean);
    }
}
