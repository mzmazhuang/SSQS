package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.SKTJBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/26 18:08
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MySkAdapter extends BaseAdapter implements ListAdapter {

    private final Context                   context;
    private final List<SKTJBean> data;


    public MySkAdapter(Context context, List<SKTJBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
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
            convertView = View.inflate(context, R.layout.sk_lv_item, null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }

        SKTJBean entity = data.get(position);
        hoder.middleType.setText(entity.eventName);
        String main1 = entity.homeNum + "";
        hoder.mainNum.setText(main1);
        hoder.mainProgressBar.setProgress(entity.homeNum);
        String away1 = entity.awayNum + "";
        hoder.secondNum.setText(away1);
        hoder.secondProgressBar.setProgress(entity.awayNum);

        return convertView;
    }

    class ViewHoder {
        @Bind(R.id.sk_tj_left_num)
        TextView    mainNum;
        @Bind(R.id.sk_tj_left_progress)
        ProgressBar mainProgressBar;

        @Bind(R.id.sk_tj_middle_type)
        TextView middleType;

        @Bind(R.id.sk_tj_right_num)
        TextView    secondNum;
        @Bind(R.id.sk_tj_right_progress)
        ProgressBar secondProgressBar;


        public ViewHoder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
