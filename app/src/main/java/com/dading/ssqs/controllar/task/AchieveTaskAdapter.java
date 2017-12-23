package com.dading.ssqs.controllar.task;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.AchieveBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 16:28
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AchieveTaskAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private List<AchieveBean> data;

    public AchieveTaskAdapter(Context context, List<AchieveBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.free_glod_achieve, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AchieveBean entity = data.get(position);
        if (entity.status == 0) {
            holder.mFreeGlodAchieveItemState.setImageResource(R.mipmap.notfinish);
        } else {
            holder.mFreeGlodAchieveItemState.setImageResource(R.mipmap.finish);
        }

        SSQSApplication.glide.load(entity.imageUrl).error(R.mipmap.fail).centerCrop().into(holder.mFreeGlodAchieveItemIv);

        if (!TextUtils.isEmpty(entity.name))
            holder.mFreeGlodAchieveItemTitle.setText(entity.name);
        if (!TextUtils.isEmpty(entity.remark))
            holder.mFreeGlodAchieveItemTv.setText(entity.remark);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.free_glod_achieve_item_iv)
        ImageView mFreeGlodAchieveItemIv;
        @Bind(R.id.free_glod_achieve_item_title)
        TextView mFreeGlodAchieveItemTitle;
        @Bind(R.id.free_glod_achieve_item_tv)
        TextView mFreeGlodAchieveItemTv;
        @Bind(R.id.free_glod_achieve_item_state)
        ImageView mFreeGlodAchieveItemState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
