package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.cells.ScoreBasketCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/27.
 */

public class ScoreBImmediateAdapter extends RecyclerView.Adapter<ScoreBImmediateAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScoreBean> list;

    public ScoreBImmediateAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScoreBean> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<ScoreBean> getData() {
        return list;
    }

    public void addList(List<ScoreBean> data) {
        if (data != null) {
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public ScoreBImmediateAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ScoreBasketCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScoreBImmediateAdapter.ItemViewHolder holder, int position) {
        ScoreBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ScoreBasketCell cell;

        public ItemViewHolder(ScoreBasketCell cell) {
            super(cell);
            this.cell = cell;
        }

        public void setData(final ScoreBean bean) {
            this.cell.setMatchName(bean.leagueName);
            this.cell.setHomeName(bean.home);
            this.cell.setAwayName(bean.away);
            this.cell.setTime(bean.openTime);
            this.cell.setMatchScore(bean.part1HScore, bean.part1AScore, bean.part2HScore, bean.part2AScore, bean.part3HScore, bean.part3AScore, bean.part4HScore, bean.part4AScore, bean.hOverTimeScore, bean.aOverTimeScore, bean.homeScore, bean.awayScore);
            this.cell.setType(bean.isOver, bean.protime);
            this.cell.setFavorite(bean.isFouce == 1);
            this.cell.setFavoriteClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onFavorite(bean.isFouce, bean.id + "");
                    }
                }
            });
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(bean.id);
                    }
                }
            });
        }
    }

    private OnScoreBasketBallListener listener;

    public void setListener(OnScoreBasketBallListener listener) {
        this.listener = listener;
    }

    public interface OnScoreBasketBallListener {
        void onFavorite(int isFavor, String matchId);

        void onItemClick(int id);
    }
}
