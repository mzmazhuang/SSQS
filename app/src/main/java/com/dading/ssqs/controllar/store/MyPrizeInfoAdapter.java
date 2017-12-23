package com.dading.ssqs.controllar.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.TurnTablePrizeBean;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/15 10:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyPrizeInfoAdapter extends BaseAdapter implements ListAdapter {
    private final Context                             context;
    private final List<TurnTablePrizeBean> data;

    public MyPrizeInfoAdapter(Context context, List<TurnTablePrizeBean> list) {
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
        ViewHoder hoder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.prize_record_info_item, null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        TurnTablePrizeBean entity = data.get(position);

        Glide.with(UIUtils.getContext( ))
                .load(entity.itemImageUrl)
                .error(R.mipmap.fail)
                .centerCrop( )
                .into(hoder.prizeItemImg);

        hoder.prizeItemName.setText(entity.name);
        if (entity.status == 0) {
            hoder.prizeItemStatus.setText("未发放");
        } else {
            hoder.prizeItemStatus.setText("已发放");
        }
        String glod = entity.cost + "金币";
        hoder.prizeItemCost.setText(glod);

        hoder.prizeItemData.setText(entity.createDate.substring(2));
        return convertView;
    }

    class ViewHoder {
        @Bind(R.id.store_prize_info_record_item_icon)
        ImageView prizeItemImg;
        @Bind(R.id.store_prize_info_record_item_prize_text)
        TextView  prizeItemName;
        @Bind(R.id.store_prize_info_record_item_prize_status)
        TextView  prizeItemStatus;
        @Bind(R.id.store_prize_info_record_item_cost)
        TextView  prizeItemCost;
        @Bind(R.id.store_prize_info_record_item_data)
        TextView  prizeItemData;

        public ViewHoder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
