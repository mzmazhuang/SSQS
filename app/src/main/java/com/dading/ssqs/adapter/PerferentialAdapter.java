package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.PerferentialBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PerferentialAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final List<PerferentialBean> data;

    public PerferentialAdapter(Context context, List<PerferentialBean> bean) {
        this.context = context;
        this.data = bean;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.perferential_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PerferentialBean bean = data.get(position);
        SSQSApplication.glide.load(bean.getImageUrl()).error(R.mipmap.fail).centerCrop().into(viewHolder.mPerferentialItemIv);
        viewHolder.mPerferentialItemTitle.setText(bean.getTitle());

        String start = getSubStr(bean.getStartDate());
        String end = getSubStr(bean.getEndDate());
        viewHolder.mPerferentialItemSubtitle.setText("活动时间:" + start + "至" + end);
        String remark = bean.getRemark();
        if (!TextUtils.isEmpty(remark)) {
            viewHolder.mPerferentialItemRemark.setText(remark);
            viewHolder.mPerferentialItemRemark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mPerferentialItemRemark.setVisibility(View.GONE);
        }
        return convertView;
    }

    @NonNull
    private String getSubStr(String string) {
        String s = null;
        if (string.length() > 9)
            s = string.substring(0, 9);
        return s;
    }

    class ViewHolder {
        @Bind(R.id.perferential_item_title)
        TextView mPerferentialItemTitle;
        @Bind(R.id.perferential_item_subtitle)
        TextView mPerferentialItemSubtitle;
        @Bind(R.id.perferential_item_iv)
        ImageView mPerferentialItemIv;
        @Bind(R.id.perferential_item_remark)
        TextView mPerferentialItemRemark;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

