package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.StoreBean2;
import com.dading.ssqs.utils.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 15:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class MyGvAdpter extends BaseAdapter {
    private static final String TAG = "MyGvAdpter";
    private final Context context;
    private final List<StoreBean2.AwardsEntity> data;

    public MyGvAdpter(Context context, List<StoreBean2.AwardsEntity> awards) {
        this.context = context;
        this.data = awards;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size() + 1;
        }
        return 0;
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
        ViewHolder hoder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }
        if (position < 4) {
            StoreBean2.AwardsEntity entity = data.get(position);

            Glide.with(context.getApplicationContext()).load(entity.itemImageUrl).error(R.mipmap.fail).centerCrop().into(hoder.mImageTurnTable);

            hoder.mTextTurnTable.setText(entity.name);
            if (position == 0) {
                hoder.mTextTurnTable.setText(entity.name);
            }
            LogUtil.util(TAG, "商品名4千------------------------------:" + entity.name + position);
        } else if (position == 4) {
            hoder.mLyTurnTable.setBackgroundResource(R.mipmap.sm_lottery);
        } else {
            StoreBean2.AwardsEntity entity = data.get(position - 1);

            Glide.with(context.getApplicationContext()).load(entity.itemImageUrl).error(R.mipmap.fail).centerCrop().into(hoder.mImageTurnTable);

            LogUtil.util(TAG, "商品名4后------------------------------:" + entity.name + position);
            hoder.mTextTurnTable.setText(entity.name);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.image_turn_table)
        ImageView mImageTurnTable;
        @Bind(R.id.text_turn_table)
        TextView mTextTurnTable;
        @Bind(R.id.ly_turn_table)
        LinearLayout mLyTurnTable;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
