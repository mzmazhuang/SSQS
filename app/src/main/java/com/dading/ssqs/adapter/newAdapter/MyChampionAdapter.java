package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.cells.ChampionCell;
import com.dading.ssqs.cells.ChampionChildCell;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 */

public class MyChampionAdapter extends RecyclerView.Adapter<MyChampionAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ChampionBean> list;
    private ChampionChildCell.OnClickListener listener;
    private List<ToDayFootBallChampionFragment.MergeBean> focusList;

    public MyChampionAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setFocus(List<ToDayFootBallChampionFragment.MergeBean> list) {
        focusList = list;
    }

    public void setListener(ChampionChildCell.OnClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ChampionBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void refreshData() {
        List<ChampionBean> beans = new ArrayList<>();
        beans.addAll(list);
        setList(beans);
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyChampionAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ChampionCell(mContext));
    }

    @Override
    public void onBindViewHolder(MyChampionAdapter.ItemViewHolder holder, int position) {
        ChampionBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ChampionCell cell;

        public ItemViewHolder(ChampionCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ChampionBean bean) {
            this.cell.setTitle(bean.getCommonTitle().getTitle());
            this.cell.setData(bean);
            this.cell.setListener(listener);
            this.cell.setFocus(focusList);
        }
    }
}
