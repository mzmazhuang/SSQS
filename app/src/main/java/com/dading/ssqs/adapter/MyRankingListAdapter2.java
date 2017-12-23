package com.dading.ssqs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.RankingBean2;
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
public class MyRankingListAdapter2 extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyRankingListAdapter2";
    private final List<RankingBean2.OrdersEntity> data;
    private final Context content;
    private final int checkID;


    public MyRankingListAdapter2(Context content, List<RankingBean2.OrdersEntity> arrayList, int checkID) {
        this.content = content;
        this.data = arrayList;
        this.checkID = checkID;
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
            convertView = View.inflate(content, R.layout.gb_ranking_list_item2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RankingBean2.OrdersEntity entity = data.get(position);
        holder.mRankingItem2Nickname.setText(entity.userName);


        SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(content)).into(holder.mGbRankingitem2Photo);

        if (position % 2 != 0) {
            if (!TextUtils.isEmpty(entity.avatar))
                holder.mRankingitem2ListItem.setBackgroundColor(content.getResources().getColor(R.color.white));
        } else {
            holder.mRankingitem2ListItem.setBackgroundColor(content.getResources().getColor(R.color.gray_e));
        }


        holder.mGbRankingitem2Ranking.setText(entity.ranking);

        String s;
        if (checkID == 4)
            s = entity.value + "%";
        else
            s = entity.value;
        holder.mRankingItem2PrizeName.setText(s);


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
