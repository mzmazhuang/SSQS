package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.cells.DialogCell;
import com.dading.ssqs.components.ResultSelectTimeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/12.
 */

public class ResultSelectTimeDialogAdapter extends RecyclerView.Adapter<ResultSelectTimeDialogAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ResultSelectTimeDialog.ResultSelectTimeBean> list;
    private int select;

    public ResultSelectTimeDialogAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public void setList(List<ResultSelectTimeDialog.ResultSelectTimeBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public ResultSelectTimeDialogAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new DialogCell(mContext));
    }

    @Override
    public void onBindViewHolder(ResultSelectTimeDialogAdapter.ItemViewHolder holder, int position) {
        ResultSelectTimeDialog.ResultSelectTimeBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private DialogCell cell;

        public ItemViewHolder(DialogCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final ResultSelectTimeDialog.ResultSelectTimeBean bean) {
            if (select == bean.getDay()) {
                this.cell.setCheck(true);
            } else {
                this.cell.setCheck(false);
            }
            this.cell.setTitle(bean.getTime());
            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(bean.getDay());
                    }
                }
            });
        }
    }

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(int day);
    }
}
