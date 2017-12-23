package com.dading.ssqs.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.PriceBean;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/1.
 */
public class WXDFRecyclePrieAdapter extends BaseAdapter implements ListAdapter {
    private final Context                     context;
    private final HashMap<Integer, PriceBean> data;
    private       String                      mSelectPosition;

    public WXDFRecyclePrieAdapter(Context context, HashMap<Integer, PriceBean> moneys) {
        this.context = context;
        this.data = moneys;
        mSelectPosition = "no";
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.wx_daifu_price_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PriceBean bean = data.get(position);
        holder.mWxDaifuPriceItemCb.setText(String.valueOf(bean.getMoney()));
        if (data.get(position).isSelect()){
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.rect_orange2);
            holder.mWxDaifuPriceItemCb.setBackground(drawable);
            holder.mWxDaifuPriceItemCb.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else{
            holder.mWxDaifuPriceItemCb.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.mWxDaifuPriceItemCb.setTextColor(ContextCompat.getColor(context,R.color.gray6));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.wx_daifu_price_item_cb)
        TextView mWxDaifuPriceItemCb;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
