package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/13.
 */
public class MatchBeforeRNAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MatchBeforeRNAdapter";
    private final Context context;
    private final List<MatchBeforBeanAll.LeagueNameEntity.MatchsEntity> data;

    public MatchBeforeRNAdapter(Context context, List<MatchBeforBeanAll.LeagueNameEntity.MatchsEntity> matchs) {
        this.context = context;
        this.data = matchs;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gb_matchbefore_r_n_ly, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String openTime = data.get(position).openTime;
        if (openTime.length() > 16) {
            String time = openTime.substring(5, 16);
            String todaystr = openTime.substring(0, 10);
            Logger.d(TAG,"日期是------------------------------:"+todaystr);
            String zh = DateUtils.getTodayZh(todaystr);
            if (TextUtils.isEmpty(zh)) {
                holder.mMatchTime.setText(time);
            } else {
                String text = zh + openTime.substring(10, 16);
                holder.mMatchTime.setText(text);
            }
            holder.mMatchTime.setTextSize(10);
        }
        String home = data.get(position).home;
        String away = data.get(position).away;
        int joinCount = data.get(position).joinCount;
        Logger.d("leftrightr","返回数据是-----------:"+home+"---"+away);
        holder.mMatchMain.setText(home);
        holder.mMatchSecond.setText(away);
        String text = joinCount + "人竞猜";
        holder.mGuessPeople.setText(text);

        holder.mMatchLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MatchInfoActivity.class);
                intent.putExtra(Constent.MATCH_ID, data.get(position).id);
                intent.putExtra("where", "match_before");
                context.startActivity(intent);//跳转进比赛内容页
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.match_time)
        TextView     mMatchTime;
        @Bind(R.id.match_main)
        TextView     mMatchMain;
        @Bind(R.id.match_second)
        TextView     mMatchSecond;
        @Bind(R.id.guess_people)
        TextView     mGuessPeople;
        @Bind(R.id.go_match_info)
        ImageView    mGoMatchInfo;
        @Bind(R.id.match_ly)
        LinearLayout mMatchLy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
