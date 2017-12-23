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
 * 创建者     ZCL
 * 创建时间   2016/10/7 14:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GuessballPopAdapter extends BaseAdapter implements ListAdapter {
    private final Context            context;
    private final ArrayList<String>  data;
    private final ArrayList<Integer> dataIcon;

    public GuessballPopAdapter(Context content, ArrayList<String> list, ArrayList<Integer> listIcon) {
        this.context = content;
        this.data = list;
        this.dataIcon = listIcon;
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
            convertView = View.inflate(context, R.layout.guessball_pop_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mGuessballPopIv.setImageResource(dataIcon.get(position));
        holder.mGuessballPopText.setText(data.get(position));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.guessball_pop_iv)
        ImageView mGuessballPopIv;
        @Bind(R.id.guessball_pop_text)
        TextView  mGuessballPopText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
