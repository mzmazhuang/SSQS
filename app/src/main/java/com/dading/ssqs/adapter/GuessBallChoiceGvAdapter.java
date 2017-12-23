package com.dading.ssqs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.GusessChoiceBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/28 14:28
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GuessBallChoiceGvAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private List<GusessChoiceBean.FilterEntity> data;

    public GuessBallChoiceGvAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setList(List<GusessChoiceBean.FilterEntity> filter) {
        if (filter != null) {
            this.data.clear();
            this.data.addAll(filter);
            notifyDataSetChanged();
        }
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
            convertView = View.inflate(context, R.layout.guessball_choice_gv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GusessChoiceBean.FilterEntity filterEntity = data.get(position);
        if (filterEntity.checked) {
            holder.mGessballChoiceItemCb.setBackgroundResource(R.mipmap.p_background_box_sel);
            holder.mGessballChoiceItemCb.setTextColor(Color.RED);
        } else {
            holder.mGessballChoiceItemCb.setTextColor(context.getResources().getColor(R.color.gray8));
            holder.mGessballChoiceItemCb.setBackgroundResource(R.mipmap.p_background_box);
        }
        String title = "";
        if (filterEntity.title.length() <= 9) {
            title = filterEntity.title;
        } else {
            title = filterEntity.title.substring(0, 8);
        }
        String text = title + "   (" + filterEntity.nums + ")";
        holder.mGessballChoiceItemCb.setText(text);
        return convertView;
    }

    public List<GusessChoiceBean.FilterEntity> getList() {
        return data;
    }

    static class ViewHolder {
        @Bind(R.id.gessball_choice_item_cb)
        TextView mGessballChoiceItemCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
