package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.SKSJBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/26 17:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MySkAdapterSJ extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MySkAdapterSJ";
    private final Context                   context;
    private final List<SKSJBean> data;

    public MySkAdapterSJ(Context context, List<SKSJBean> data) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.sk_lv_sj_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SKSJBean entity = data.get(position);

        holder.mSkSjMiddleType.setText(entity.time);
        String main1 = entity.player + "";
        Logger.INSTANCE.d(TAG, "人物返回数据是------------------------------:" + main1);
        switch (entity.type) {
            case 1:
                holder.mSkSjLeftNum.setText(main1);
                holder.mSkSjRightNum.setText("");
                holder.mSkSjRightIv.setImageResource(R.mipmap.background_image);
                switch (entity.nameType) {
                    case 1:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dajinqiu);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 2:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dadianqiu);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 3:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dawulong);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 4:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dawuxiao);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 5:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dahuanren);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 6:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dahongpai);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 7:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dahuangpai);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 8:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dalianghuang);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    case 9:
                        holder.mSkSjLeftIv.setImageResource(R.mipmap.dajiaoqiu);
                        Logger.INSTANCE.d(TAG,"主隊------------------------------:"+entity.nameType);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                holder.mSkSjLeftNum.setText("");
                holder.mSkSjLeftIv.setImageResource(R.mipmap.background_image);
                holder.mSkSjRightNum.setText(main1);
                switch (entity.nameType) {
                    case 1:
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dajinqiu);
                        break;
                    case 2:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dadianqiu);
                        Logger.INSTANCE.d(TAG,"客隊------------------------------:"+entity.nameType);
                        break;
                    case 3:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dawulong);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 4:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dawuxiao);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 5:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dahuanren);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 6:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dahongpai);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 7:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dahuangpai);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 8:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dalianghuang);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    case 9:
                        holder.mSkSjRightIv.setImageResource(R.mipmap.dajiaoqiu);
                        Logger.INSTANCE.d(TAG, "客隊------------------------------:" + entity.nameType);
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.sk_sj_left_iv)
        ImageView mSkSjLeftIv;
        @Bind(R.id.sk_sj_left_num)
        TextView  mSkSjLeftNum;
        @Bind(R.id.sk_sj_middle_type)
        TextView  mSkSjMiddleType;
        @Bind(R.id.sk_sj_right_num)
        TextView  mSkSjRightNum;
        @Bind(R.id.sk_sj_right_iv)
        ImageView mSkSjRightIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
