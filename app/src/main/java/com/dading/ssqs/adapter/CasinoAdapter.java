package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LuckBallActivity;
import com.dading.ssqs.bean.CasinoBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 14:10
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CasinoAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "CasinoAdapter";
    private Context context;
    private List<CasinoBean> data;

    public CasinoAdapter(Context content) {
        this.context = content;
        this.data = new ArrayList<>();
    }

    public void setList(List<CasinoBean> list) {
        if (list != null) {
            this.data.clear();
            this.data.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.casino_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CasinoBean entity = data.get(position);

        SSQSApplication.glide.load(entity.imageUrl).error(R.mipmap.fail).centerCrop().into(holder.mCasinoGvItemIv);

        if (entity.status == 0) {
            holder.mCasinoGvItemTv.setText(context.getString(R.string.ComingSoon));
            holder.mCasinoGvItemTv.setBackgroundColor(ContextCompat.getColor(context, R.color.black_alpha_8));
            holder.mCasinoGvItemTv.setVisibility(View.VISIBLE);
        } else if (entity.status == 1) {
            holder.mCasinoGvItemTv.setText("");
            holder.mCasinoGvItemTv.setBackground(null);
            holder.mCasinoGvItemTv.setVisibility(View.GONE);
        }
        holder.mCasinoGvItemIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LuckBallActivity.class);
                intent.putExtra(Constent.CASINO_NAME, entity.name);
                intent.putExtra(Constent.CASINO_URL, entity.url);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.casino_gv_item_iv)
        ImageView mCasinoGvItemIv;
        @Bind(R.id.casino_gv_item_tv)
        TextView mCasinoGvItemTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
