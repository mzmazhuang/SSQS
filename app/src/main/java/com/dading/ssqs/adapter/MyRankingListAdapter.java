package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.RankingBean;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/20 11:40
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyRankingListAdapter extends BaseAdapter implements ListAdapter {
    private final List<RankingBean> data;
    private final Context content;

    public MyRankingListAdapter(Context content, List<RankingBean> arrayList) {
        this.content = content;
        this.data = arrayList;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
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
            convertView = View.inflate(content, R.layout.gb_ranking_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RankingBean entity = data.get(position);

        Glide.with(content.getApplicationContext())
                .load(entity.avatar)
                .error(R.mipmap.nologinportrait)
                .centerCrop()
                .transform(new GlideCircleTransform(content))
                .into(holder.mGbRankingPhoto);

        if (position % 2 != 0) {
            holder.mRankingitem.setBackgroundColor(content.getResources().getColor(R.color.white));
        } else {
            holder.mRankingitem.setBackgroundColor(content.getResources().getColor(R.color.gray_e));
        }
        holder.mGbRankingNickname.setText(entity.userName);
        holder.mGbRankingRanking.setText(entity.ranking);
        holder.mGbRankingGlod.setText(entity.value);
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.gb_ranking_ranking)
        TextView mGbRankingRanking;
        @Bind(R.id.gb_ranking_photo)
        ImageView mGbRankingPhoto;
        @Bind(R.id.gb_ranking_nickname)
        TextView mGbRankingNickname;
        @Bind(R.id.gb_ranking_glod)
        TextView mGbRankingGlod;
        @Bind(R.id.ranking_list_item)
        LinearLayout mRankingitem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
