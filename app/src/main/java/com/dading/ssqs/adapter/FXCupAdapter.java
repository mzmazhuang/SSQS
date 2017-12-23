package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.FXBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/26 16:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FXCupAdapter extends BaseAdapter implements ListAdapter {
    private final Context                              context;
    private final List<FXBean.HOrderEntity> data;
    private final String[]                             topS;

    public FXCupAdapter(Context context, List<FXBean.HOrderEntity> hOrder, String[] topS) {
        this.context = context;
        this.data = hOrder;
        this.topS = topS;
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
            convertView = View.inflate(context, R.layout.fx_cup_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (position) {
            case 0:
                holder.mFxCupItem.setBackgroundResource(R.mipmap.background_selectrect);
                break;
            case 1:
                holder.mFxCupItem.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            case 2:
                holder.mFxCupItem.setBackgroundColor(context.getResources().getColor(R.color.yellow_light2));
                break;
            case 3:
                holder.mFxCupItem.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            case 4:
                holder.mFxCupItem.setBackgroundColor(context.getResources().getColor(R.color.blue_Light2));
                break;
            default:
                break;
        }
        if (position==0){
            holder.mFxCupItemTv1.setText(topS[0]);
            holder.mFxCupItemTv2.setText(topS[1]);
            holder.mFxCupItemTv3.setText(topS[2]);
            holder.mFxCupItemTv4.setText(topS[3]);
            holder.mFxCupItemTv5.setText(topS[4]);
            holder.mFxCupItemTv6.setText(topS[5]);
            holder.mFxCupItemTv7.setText(topS[6]);
            holder.mFxCupItemTv8.setText(topS[7]);
            holder.mFxCupItemTv9.setText(topS[8]);
            holder.mFxCupItemTv1.setTextSize(12);
            holder.mFxCupItemTv2.setTextSize(12);
            holder.mFxCupItemTv3.setTextSize(12);
            holder.mFxCupItemTv4.setTextSize(12);
            holder.mFxCupItemTv5.setTextSize(12);
            holder.mFxCupItemTv6.setTextSize(12);
            holder.mFxCupItemTv7.setTextSize(12);
            holder.mFxCupItemTv8.setTextSize(12);
            holder.mFxCupItemTv9.setTextSize(12);
        }else{
            List<String> nums = data.get(position-1).nums;
            holder.mFxCupItemTv1.setText(nums.get(0));
            holder.mFxCupItemTv2.setText(nums.get(1));
            holder.mFxCupItemTv3.setText(nums.get(2));
            holder.mFxCupItemTv4.setText(nums.get(3));
            holder.mFxCupItemTv5.setText(nums.get(4));
            holder.mFxCupItemTv6.setText(nums.get(5));
            holder.mFxCupItemTv7.setText(nums.get(6));
            holder.mFxCupItemTv8.setText(nums.get(7));
            holder.mFxCupItemTv9.setText(nums.get(8));
            holder.mFxCupItemTv1.setTextSize(11);
            holder.mFxCupItemTv2.setTextSize(10);
            holder.mFxCupItemTv3.setTextSize(10);
            holder.mFxCupItemTv4.setTextSize(10);
            holder.mFxCupItemTv5.setTextSize(10);
            holder.mFxCupItemTv6.setTextSize(10);
            holder.mFxCupItemTv7.setTextSize(10);
            holder.mFxCupItemTv8.setTextSize(10);
            holder.mFxCupItemTv9.setTextSize(10);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.fx_cup_item_tv1)
        TextView     mFxCupItemTv1;
        @Bind(R.id.fx_cup_item_tv2)
        TextView     mFxCupItemTv2;
        @Bind(R.id.fx_cup_item_tv3)
        TextView     mFxCupItemTv3;
        @Bind(R.id.fx_cup_item_tv4)
        TextView     mFxCupItemTv4;
        @Bind(R.id.fx_cup_item_tv5)
        TextView     mFxCupItemTv5;
        @Bind(R.id.fx_cup_item_tv6)
        TextView     mFxCupItemTv6;
        @Bind(R.id.fx_cup_item_tv7)
        TextView     mFxCupItemTv7;
        @Bind(R.id.fx_cup_item_tv8)
        TextView     mFxCupItemTv8;
        @Bind(R.id.fx_cup_item_tv9)
        TextView     mFxCupItemTv9;
        @Bind(R.id.fx_cup_item)
        LinearLayout mFxCupItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
