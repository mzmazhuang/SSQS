package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SavantLeveBean;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/17 19:06
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantLeveAdapter extends BaseAdapter implements ListAdapter {
    private  Context                                          context;
    private  List<SavantLeveBean> data;

    public SavantLeveAdapter(Context context, List<SavantLeveBean> items) {
        this.context = context;
        this.data = items;
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
            convertView = View.inflate(context, R.layout.savant_leve_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        final SavantLeveBean entity = data.get(position);

        Glide.with(context.getApplicationContext())
                .load(entity.avatar)
                .error(R.mipmap.fail)
                .centerCrop( )
                .transform(new GlideCircleTransform(context))
                .into( hoder.mSavantSeachItemSavantpoto);

        hoder.mSavantSeachItemNickname.setText(entity.userName);
        String s = entity.tag + "连红";
        hoder.mSavantSeachItemLianhong.setText(s);
        switch (entity.level) {
            case 1:
                hoder.mSavantSeachItemNickleve.setText("初级专家");
                break;
            case 2:
                hoder.mSavantSeachItemNickleve.setText("中级专家");
                break;
            case 3:
                hoder.mSavantSeachItemNickleve.setText("高级专家");
                break;
            case 4:
                hoder.mSavantSeachItemNickleve.setText("资深专家");
                break;
            default:
                break;
        }

        hoder.mSavantSeachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SavantInfoActivity.class);
                intent.putExtra(Constent.SAVANT_ID,entity.id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.savant_seach_item_savantpoto)
        ImageView    mSavantSeachItemSavantpoto;
        @Bind(R.id.savant_seach_item_nickname)
        TextView     mSavantSeachItemNickname;
        @Bind(R.id.savant_seach_item_nickleve)
        TextView     mSavantSeachItemNickleve;
        @Bind(R.id.savant_seach_item_lianhong)
        TextView     mSavantSeachItemLianhong;
        @Bind(R.id.savant_seach_item)
        LinearLayout mSavantSeachItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
