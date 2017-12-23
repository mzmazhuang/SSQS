package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.InfoPLBean;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 10:54
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyLvAdapter extends BaseAdapter implements ListAdapter {
    private final List<InfoPLBean> data;
    private       Context                                 context;

    public MyLvAdapter(Context context, List<InfoPLBean> list) {
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
        ViewHoder hoder;
        if (convertView == null) {
            hoder = new ViewHoder();
            convertView = View.inflate(context, R.layout.pl_lv_item, null);
            hoder.oddsName = ButterKnife.findById(convertView, R.id.pl_odds_name);
            hoder.oddsCp1 = ButterKnife.findById(convertView, R.id.pl_cp_1);
            hoder.oddsCp2 = ButterKnife.findById(convertView, R.id.pl_cp_2);
            hoder.oddsCp3 = ButterKnife.findById(convertView, R.id.pl_cp_3);
            hoder.oddsJs1 = ButterKnife.findById(convertView, R.id.pl_js_1);
            hoder.oddsJs2 = ButterKnife.findById(convertView, R.id.pl_js_2);
            hoder.oddsJs3 = ButterKnife.findById(convertView, R.id.pl_js_3);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }

        InfoPLBean entity = data.get(position);
        hoder.oddsName.setText(entity.companyName);
        hoder.oddsCp1.setText(String.valueOf(entity.beginRate1));
        hoder.oddsCp2.setText(String.valueOf(entity.beginRate2));
        hoder.oddsCp3.setText(String.valueOf(entity.beginRate3));
        hoder.oddsJs1.setText(String.valueOf(entity.realRate1));
        hoder.oddsJs2.setText(String.valueOf(entity.realRate2));
        hoder.oddsJs3.setText(String.valueOf(entity.realRate3));
        return convertView;
    }

    class ViewHoder {
        public TextView oddsName;
        public TextView oddsCp1;
        public TextView oddsCp2;
        public TextView oddsCp3;
        public TextView oddsJs1;
        public TextView oddsJs2;
        public TextView oddsJs3;
    }
}
