package com.dading.ssqs.controllar.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.TurnTablePrizeBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/13 17:55
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyTurnTablePrizeAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final List<TurnTablePrizeBean> data;

    public MyTurnTablePrizeAdapter(Context context, List<TurnTablePrizeBean> list) {
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
        ViewHolder hoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.prize_record_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }
        TurnTablePrizeBean entity = data.get(position);

        SSQSApplication.glide.load(entity.itemImageUrl).error(R.mipmap.fail).centerCrop().into(hoder.mStorePrizeRecordIcon);

        hoder.mStorePrizeRecordName.setText(entity.name);
        if (entity.status == 0) {
            hoder.mStorePrizeRecordState.setText("未兑换");
        } else {
            hoder.mStorePrizeRecordState.setText("已兑换");
        }
        String cost = entity.cost + "元";
        hoder.mStorePrizeRecordCost.setText(cost);
        String time = entity.createDate.substring(2);
        hoder.mStorePrizeRecordTime.setText(time);
        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.store_prize_record_icon)
        ImageView mStorePrizeRecordIcon;
        @Bind(R.id.store_prize_record_name)
        TextView mStorePrizeRecordName;
        @Bind(R.id.store_prize_record_state)
        TextView mStorePrizeRecordState;
        @Bind(R.id.store_prize_record_time)
        TextView mStorePrizeRecordTime;
        @Bind(R.id.store_prize_record_cost)
        TextView mStorePrizeRecordCost;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
