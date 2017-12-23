package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.cells.DialogCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/12.
 */

public class PageDialogAdapter extends RecyclerView.Adapter<PageDialogAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Integer> list;
    private int select;

    public PageDialogAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public void setList(List<Integer> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public PageDialogAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new DialogCell(mContext));
    }

    @Override
    public void onBindViewHolder(PageDialogAdapter.ItemViewHolder holder, int position) {
        Integer bean = list.get(position);
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

        public void setData(final Integer title) {
            if (title.equals(select)) {
                this.cell.setCheck(true);
            }else{
                this.cell.setCheck(false);
            }
            this.cell.setTitle("第" + title + "页");
            this.cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(title);
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
        void onClick(int page);
    }
}
