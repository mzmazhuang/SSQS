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
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.bean.EssayInfoBean;
import com.dading.ssqs.utils.LogUtil;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/30 12:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class EssayInfoAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HomeMatchInfoAdapter";
    private final Context context;
    private final List<EssayInfoBean> data;
    private String mText;
    private String mText1;
    private int mId;


    public EssayInfoAdapter(Context context, List<EssayInfoBean> data) {
        this.context = context;
        this.data = data;
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
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.home_lv_match_info, null);

            holder = new ViewHolder();

            holder.item = (LinearLayout) convertView.findViewById(R.id.home_lv_information_item);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.home_lv_information_item_title);
            holder.tvContent = (TextView) convertView.findViewById(R.id.home_lv_information_item_content);
            holder.ivIv1 = (ImageView) convertView.findViewById(R.id.home_lv_information_item_iv1);
            holder.ivIv2 = (ImageView) convertView.findViewById(R.id.home_lv_information_item_iv2);
            holder.ivIv3 = (ImageView) convertView.findViewById(R.id.home_lv_information_item_iv3);
            holder.tvExplainName = (TextView) convertView.findViewById(R.id.home_lv_information_item_explan_name);
            holder.tvExplainPlace = (TextView) convertView.findViewById(R.id.home_lv_information_item_explan_place);
            holder.tvHomeGoods = (TextView) convertView.findViewById(R.id.number_goods_home);
            holder.tvHomeComments = (TextView) convertView.findViewById(R.id.number_comments_home);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EssayInfoBean entity = data.get(position);
        holder.tvTitle.setText(entity.title);
        String content = entity.content;
        String[] ssqs_s = content.split("_ssqs_");

        holder.tvContent.setText(ssqs_s[0]);

        List<String> url = entity.imageUrl;
        if (url != null) {
            switch (url.size()) {
                case 1:
                    Glide.with(context.getApplicationContext()).load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.ivIv1);
                    holder.ivIv2.setImageResource(R.mipmap.query_background);
                    holder.ivIv3.setImageResource(R.mipmap.query_background);
                    break;
                case 2:
                    Glide.with(context.getApplicationContext()).load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.ivIv1);
                    Glide.with(context.getApplicationContext()).load(url.get(1)).error(R.mipmap.fail).centerCrop().into(holder.ivIv2);
                    holder.ivIv3.setImageResource(R.mipmap.query_background);
                    break;
                case 3:
                    Glide.with(context.getApplicationContext()).load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.ivIv1);
                    Glide.with(context.getApplicationContext()).load(url.get(1)).error(R.mipmap.fail).centerCrop().into(holder.ivIv2);
                    Glide.with(context.getApplicationContext()).load(url.get(2)).error(R.mipmap.fail).centerCrop().into(holder.ivIv3);
                    break;
                default:
                    break;
            }
        }

        holder.tvExplainName.setText(entity.userName);
        holder.tvExplainPlace.setText(entity.categoryName);
        mText = entity.zanCount + "";
        holder.tvHomeGoods.setText(mText);
        mText1 = entity.commentCount + "";
        holder.tvHomeComments.setText(mText1);

        holder.item.setOnClickListener(new MyHomeMatchLsitener(holder) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeViewPagerActivity.class);
                //添加数据传递过去到详情页面
                mId = entity.id;
                intent.putExtra("infoId", mId);
                LogUtil.util(TAG, "赛事id---------------------" + entity.id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivIv1;
        public ImageView ivIv2;
        public ImageView ivIv3;
        public TextView tvExplainName;
        public TextView tvExplainPlace;
        public TextView tvHomeGoods;
        public TextView tvHomeComments;
        public LinearLayout item;
    }

    private class MyHomeMatchLsitener implements View.OnClickListener {
        private final ViewHolder holder;

        public MyHomeMatchLsitener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
