package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/13.
 */
public class MatchBeforeLDAdapter extends BaseAdapter implements ListAdapter {
    private final Context                                             context;
    private final List<MatchBeforBeanAll.LeagueDateEntity> data;
    private       boolean                                             b;

    public MatchBeforeLDAdapter(Context context, List<MatchBeforBeanAll.LeagueDateEntity> leagueDate) {
        this.context = context;
        this.data = leagueDate;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gb_matchbefore_left_d_ly, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MatchBeforBeanAll.LeagueDateEntity entity = data.get(position);
        if (b){
            entity.isColor=true;
            b = false;
        }
        if (entity.isColor) {
            holder.mMatchbeforeGroupDWeek.setTextColor(ContextCompat.getColor(context, R.color.orange));
            holder.mMatchbeforeGroupDDate.setTextColor(ContextCompat.getColor(context, R.color.orange));
            holder.mMatchbeforeGroupDLy.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        } else {
            holder.mMatchbeforeGroupDWeek.setTextColor(ContextCompat.getColor(context, R.color.gray6));
            holder.mMatchbeforeGroupDDate.setTextColor(ContextCompat.getColor(context, R.color.gray6));
            holder.mMatchbeforeGroupDLy.setBackgroundColor(ContextCompat.getColor(context,R.color.gray_e));
        }
        String week = DateUtils.getweekdayBystr(entity.title);
        String s = week+"(" + entity.nums + ")";
        holder.mMatchbeforeGroupDWeek.setText(s);
        holder.mMatchbeforeGroupDDate.setText(entity.title);

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.matchbefore_group_d_week)
        TextView     mMatchbeforeGroupDWeek;
        @Bind(R.id.matchbefore_group_d_date)
        TextView     mMatchbeforeGroupDDate;
        @Bind(R.id.matchbefore_group_d_ly)
        LinearLayout mMatchbeforeGroupDLy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
