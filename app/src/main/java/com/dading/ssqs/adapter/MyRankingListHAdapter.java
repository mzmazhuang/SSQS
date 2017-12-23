package com.dading.ssqs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/12.
 */
public class MyRankingListHAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final List<HomeBean.OrdersBeanX.OrdersBean> data;

    public MyRankingListHAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setList(List<HomeBean.OrdersBeanX.OrdersBean> orders) {
        if (orders != null) {
            this.data.clear();
            this.data.addAll(orders);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gb_ranking_list_item2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HomeBean.OrdersBeanX.OrdersBean entity = data.get(position);
        holder.mRankingItem2Nickname.setText(entity.getUserName());

        SSQSApplication.glide.load(entity.getAvatar()).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(holder.mGbRankingitem2Photo);

        if (position % 2 != 0) {
            if (!TextUtils.isEmpty(entity.getAvatar()))
                holder.mRankingitem2ListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.mRankingitem2ListItem.setBackgroundColor(context.getResources().getColor(R.color.gray_e));
        }

        holder.mGbRankingitem2Ranking.setText(entity.getRanking());

        holder.mRankingItem2PrizeName.setText(entity.getValue());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.gb_rankingitem2_ranking)
        TextView mGbRankingitem2Ranking;
        @Bind(R.id.gb_rankingitem2_photo)
        ImageView mGbRankingitem2Photo;
        @Bind(R.id.ranking_item2_nickname)
        TextView mRankingItem2Nickname;

        @Bind(R.id.ranking_item2_prize_name)
        TextView mRankingItem2PrizeName;
        @Bind(R.id.rankingitem2_list_item)
        LinearLayout mRankingitem2ListItem;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
