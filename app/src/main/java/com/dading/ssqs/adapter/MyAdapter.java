package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/14.
 */
public class MyAdapter extends BaseAdapter implements ListAdapter {
    private final Context            context;
    private final ArrayList<Integer> data;
    private final ArrayList<String>  dataTabs;

    public MyAdapter(Context context, ArrayList<Integer> listIcon, ArrayList<String> listTitle) {
        this.context = context;
        this.data = listIcon;
        this.dataTabs = listTitle;
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
            convertView = View.inflate(context, R.layout.my_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mMyItemIcon.setImageResource(data.get(position));
        holder.mMyItemTitle.setText(dataTabs.get(position));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.my_item_icon)
        ImageView mMyItemIcon;
        @Bind(R.id.my_item_title)
        TextView  mMyItemTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
