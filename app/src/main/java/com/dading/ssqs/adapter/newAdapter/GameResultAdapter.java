package com.dading.ssqs.adapter.newAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.bean.ScrollBallFootBallResultBean;
import com.dading.ssqs.cells.GameResultItemCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class GameResultAdapter extends RecyclerView.Adapter<GameResultAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems> list;

    public GameResultAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
    }

    public void setList(List<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems> data) {
        if (data != null) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public GameResultAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new GameResultItemCell(mContext));
    }

    @Override
    public void onBindViewHolder(GameResultAdapter.ItemViewHolder holder, int position) {
        ScrollBallFootBallResultBean.ScrollBallFootBallResultItems bean = list.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private GameResultItemCell cell;

        public ItemViewHolder(GameResultItemCell cell) {
            super(cell);

            this.cell = cell;
        }

        public void setData(ScrollBallFootBallResultBean.ScrollBallFootBallResultItems bean) {
            this.cell.setRansTitle(bean.getTitle(), bean.getByTitle(), bean.isHome());
            this.cell.setIntegral(bean.getIntegral1(), bean.getIntegral2(), bean.getIntegral3(), bean.getIntegral4(), bean.isHome());
        }
    }
}
