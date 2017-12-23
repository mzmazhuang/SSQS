package com.dading.ssqs.controllar.guessball;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/12.
 */
public class MatchBeforeLAdapter extends BaseAdapter implements ListAdapter {
    private       boolean                                             b;
    private       Context                                             context;
    private final List<MatchBeforBeanAll.LeagueNameEntity> data;

    public MatchBeforeLAdapter(Context content, List<MatchBeforBeanAll.LeagueNameEntity> Date) {
        this.context = content;
        this.data = Date;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gb_matchbefore_left_n_ly, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MatchBeforBeanAll.LeagueNameEntity entity = data.get(position);
        if (entity.isColor) {
            holder.mMatchbeforeLeftItemType.setTextColor(ContextCompat.getColor(context, R.color.orange));
            holder.mMatchbeforeLeftItemNum.setTextColor(ContextCompat.getColor(context, R.color.orange));
            holder.mMatchbeforeLeftItemLy.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        } else {
            holder.mMatchbeforeLeftItemLy.setBackgroundColor(ContextCompat.getColor(context,R.color.gray_e));
            holder.mMatchbeforeLeftItemType.setTextColor(ContextCompat.getColor(context, R.color.gray6));
            holder.mMatchbeforeLeftItemNum.setTextColor(ContextCompat.getColor(context, R.color.gray6));
        }
        holder.mMatchbeforeLeftItemType.setText(entity.title);
        String s = "(" + entity.nums + ")";
        holder.mMatchbeforeLeftItemNum.setText(s);
        LogUtil.util("leftrightl","返回数据是-----------:"+entity.title+"---"+entity.nums +"----"+entity.matchs.size());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.matchbefore_left_item_ly)
        LinearLayout mMatchbeforeLeftItemLy;
        @Bind(R.id.matchbefore_left_item_icon)
        ImageView    mMatchbeforeLeftItemIcon;
        @Bind(R.id.matchbefore_left_item_type)
        TextView     mMatchbeforeLeftItemType;
        @Bind(R.id.matchbefore_left_item_num)
        TextView     mMatchbeforeLeftItemNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
