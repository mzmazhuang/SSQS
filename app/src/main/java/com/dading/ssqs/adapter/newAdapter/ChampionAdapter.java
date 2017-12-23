package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.cells.ChampionChildCell;
import com.dading.ssqs.cells.ChampionItemCell;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ChampionBean.ChampionItems> list;
    private ChampionChildCell.OnClickListener listener;
    private String title;
    private List<ToDayFootBallChampionFragment.MergeBean> focusList;

    public ChampionAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setFocus(List<ToDayFootBallChampionFragment.MergeBean> list) {
        focusList = list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setListener(ChampionChildCell.OnClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ChampionBean.ChampionItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public ChampionAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ChampionItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(ChampionAdapter.ItemViewHolder holder, int position) {
        ChampionBean.ChampionItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ChampionItemCell cell;

        public ItemViewHolder(ChampionItemCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ChampionBean.ChampionItems bean) {
            this.cell.setTitle(bean.getTitle());
            this.cell.setData(bean, listener, title,focusList);
        }
    }
}
