package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.bean.DiamondsGlodRecordBean;
import com.dading.ssqs.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/26 11:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyRecordAdapter extends BaseAdapter implements ListAdapter {
    private final Context                                             context;
    private final List<DiamondsGlodRecordBean> data;

    public MyRecordAdapter(Context context, List<DiamondsGlodRecordBean> items) {
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
        ViewHoder hoder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.record_lv_item, null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        DiamondsGlodRecordBean itemsEntity = data.get(position);
        hoder.mDiamondsRecordChangeNum.setText(itemsEntity.amount+"");
        hoder.mDiamondsRecordContext.setText(itemsEntity.item);
        hoder.mDiamondsRecordTime.setText(itemsEntity.createDate);
        return convertView;
    }

    class ViewHoder {
        @Bind(R.id.diamonds_record_time)
        TextView mDiamondsRecordTime;
        @Bind(R.id.diamonds_record_context)
        TextView mDiamondsRecordContext;
        @Bind(R.id.diamonds_record_change_num)
        TextView mDiamondsRecordChangeNum;

        public ViewHoder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}
