package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.JCScorebean;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/17 14:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class JCScoreIntervalAdapter extends BaseAdapter implements ListAdapter {
    private       int                                                             type;
    private       Context                                                         context;
    private       List<JCScorebean.ListEntity.ItemsEntity>             data;
    private final HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecked;


    public JCScoreIntervalAdapter(Context context, List<JCScorebean.ListEntity.ItemsEntity> list, int type) {
        this.context = context;
        this.data = list;
        this.type = type;
        mMapChecked = new HashMap<>();
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.jc_score_interval_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JCScorebean.ListEntity.ItemsEntity listEntity = data.get(position);
        if (listEntity.checked){
            holder.mJcScoreItem.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            holder.mJcScoreLeftBlackTv.setTextColor(Color.WHITE);
            holder.mJcScoreRightOrangeTv.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }else {
            holder.mJcScoreItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.mJcScoreLeftBlackTv.setTextColor(Color.BLACK);
            holder.mJcScoreRightOrangeTv.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }
        holder.mJcScoreLeftBlackTv.setText(listEntity.name);
        holder.mJcScoreRightOrangeTv.setText(listEntity.payRate);
        return convertView;
    }

    public HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> getChecked() {
        return mMapChecked;
    }
    public int getType() {
        return type;
    }

    static class ViewHolder {
        @Bind(R.id.jc_score_left_black_tv)
        TextView       mJcScoreLeftBlackTv;
        @Bind(R.id.jc_score_right_orange_tv)
        TextView       mJcScoreRightOrangeTv;
        @Bind(R.id.jc_score_item)
        RelativeLayout mJcScoreItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
