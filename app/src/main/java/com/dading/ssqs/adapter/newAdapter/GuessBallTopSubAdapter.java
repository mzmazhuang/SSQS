package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.GuessTopTitle;
import com.dading.ssqs.cells.GuessBallTitleCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class GuessBallTopSubAdapter extends RecyclerView.Adapter<GuessBallTopSubAdapter.ItemViewHolder> {

    private Context mContext;
    private List<GuessTopTitle> list;
    private int select = 0;

    public GuessBallTopSubAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setSelect(int sel) {
        this.select = sel;
    }

    public void setData(List<GuessTopTitle> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    private void refreshData() {
        List<GuessTopTitle> data = new ArrayList<>();
        data.addAll(list);
        setData(data);
    }

    @Override
    public GuessBallTopSubAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new GuessBallTitleCell(mContext, true));
    }

    @Override
    public void onBindViewHolder(GuessBallTopSubAdapter.ItemViewHolder holder, int position) {
        GuessTopTitle bean = list.get(position);
        holder.setData(bean, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private GuessBallTitleCell cell;

        public ItemViewHolder(GuessBallTitleCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final GuessTopTitle bean, final int position) {
            this.cell.setName(bean.getName());

            if (select == position) {
                this.cell.setCheck(true);
            } else {
                this.cell.setCheck(false);
            }

            if (list.size() - 1 == position) {
                this.cell.isShowLine(false);
            } else {
                this.cell.isShowLine(true);
            }

            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select = position;

                    if (!cell.isCheck()) {
                        cell.setCheck(true);

                        if (listener != null) {
                            listener.onClick(bean.getId(), position);
                        }

                        refreshData();
                    }
                }
            });
        }
    }

    private OnGuessSubTitleClickListener listener;

    public void setListener(OnGuessSubTitleClickListener listener) {
        this.listener = listener;
    }

    public interface OnGuessSubTitleClickListener {
        void onClick(int id, int position);
    }
}
