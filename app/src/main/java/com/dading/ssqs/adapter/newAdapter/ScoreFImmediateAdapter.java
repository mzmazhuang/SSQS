package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.cells.ScoreFootCell;
import com.dading.ssqs.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/26.
 */

public class ScoreFImmediateAdapter extends RecyclerView.Adapter<ScoreFImmediateAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScoreBean> list;
    private boolean isMonth = false;

    public void setMonth(boolean month) {
        isMonth = month;
    }

    public ScoreFImmediateAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
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

    @Override
    public ScoreFImmediateAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ScoreFootCell(mContext));
    }

    @Override
    public void onBindViewHolder(ScoreFImmediateAdapter.ItemViewHolder holder, int position) {
        ScoreBean bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ScoreFootCell cell;

        public ItemViewHolder(ScoreFootCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(final ScoreBean bean) {
            cell.setMatchName(bean.leagueName);
            cell.setTime(bean.openTime, isMonth);
            cell.setHomeNumber(bean.hOrder);
            cell.setAwayNumber(bean.aOrder);
            cell.setHomeYellow(bean.hYellow);
            cell.setHomeRed(bean.hRed);
            cell.setAwayYellow(bean.aYellow);
            cell.setAwayRed(bean.aRed);
            cell.setScoreTopText(bean.hHalfScore, bean.aHalfScore);
            cell.setScoreText(bean.hScore + "-" + bean.aScore);
            cell.setHomeName(bean.home);
            cell.setAwayName(bean.away);
            cell.setType(bean.isOver, bean.protime);
            cell.setFavorite(bean.isFouce == 1);
            cell.setFavoriteClickListener(new View.OnClickListener() {
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

    private OnScoreFootBallListener listener;

    public void setListener(OnScoreFootBallListener listener) {
        this.listener = listener;
    }

    public interface OnScoreFootBallListener {
        void onFavorite(int isFavor, String matchId);

        void onItemClick(int id);
    }
}
