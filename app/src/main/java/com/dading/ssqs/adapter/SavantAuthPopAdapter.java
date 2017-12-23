package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.BankBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/1 9:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantAuthPopAdapter extends BaseAdapter implements ListAdapter {
    private final Context                   context;
    private final List<BankBean> data;

    public SavantAuthPopAdapter(Context context, List<BankBean> list) {
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
            convertView = View.inflate(context, R.layout.savant_auth_pop_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        hoder.mSavantSuthPopBankText.setText(data.get(position).name);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.savant_suth_pop_bank_text)
        TextView mSavantSuthPopBankText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
