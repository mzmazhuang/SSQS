package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.TodayTopicBean;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/6 10:26
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TodayTopicAdapter extends BaseAdapter implements ListAdapter {
    private final Context                                     context;
    private final List<TodayTopicBean> data;

    public TodayTopicAdapter(Context context, List<TodayTopicBean> list) {
        this.context = context;
        this.data = list;
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
            convertView = View.inflate(context, R.layout.topic_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TodayTopicBean entity = data.get(position);
        holder.mTopicItemTitle.setText(entity.title);
        holder.mTopicItemIntroduce.setText(entity.content);
        String date = entity.createDate;
        String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
        if (TextUtils.isEmpty(diffCurTime))
            diffCurTime = "0";
        String text2 = diffCurTime + "前发布";
        holder.mTopicItemTitleTime.setText(text2);

        holder.mTopicItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeViewPagerActivity.class);
                int value = entity.forwardID;
                intent.putExtra(Constent.INFO_ID, value);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.topic_item)
        LinearLayout mTopicItem;
        @Bind(R.id.topic_item_title)
        TextView     mTopicItemTitle;
        @Bind(R.id.topic_item_title_time)
        TextView     mTopicItemTitleTime;
        @Bind(R.id.topic_item_introduce)
        TextView     mTopicItemIntroduce;
       /* @Bind(R.id.topic_item_delete)
        TextView     mTopicItemDel;*/

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
