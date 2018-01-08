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

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.bean.ALLCircleThings;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.components.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/29 16:18
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyPostAdapter extends BaseAdapter implements ListAdapter {

    private static String TAG = "MyPostAdapter";
    private Context context;
    private List<ALLCircleThings> data;
    private String mText;
    private String mText1;
    private int mId;

    public MyPostAdapter(Context context, List<ALLCircleThings> data) {
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
            convertView = View.inflate(context, R.layout.my_post_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ALLCircleThings topicsEntity = data.get(position);

        holder.mMyPostItemTitle.setText(topicsEntity.title);
        String text = topicsEntity.content;
        String[] ssqs_s = text.split("_ssqs_");
        holder.mMyPostItemContent.setText(ssqs_s[0]);
        holder.mMyPostTvNikname.setText(topicsEntity.userName);
        String createDate = topicsEntity.createDate;
        if (createDate != null)
            holder.mMyPostPublishTime.setText(createDate.substring(5, 16));
        String zan = topicsEntity.zanCount + "";
        holder.mMyPostZan.setText(zan);
        String PL = topicsEntity.commentCount + "";
        holder.mMyPostPl.setText(PL);

        SSQSApplication.glide.load(topicsEntity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(holder.mMyPostPhoto);

        List<String> url = topicsEntity.imageUrl;
        switch (url.size()) {
            case 1:
                SSQSApplication.glide.load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv1);

                holder.mMyPostItemIv1.setVisibility(View.VISIBLE);
                break;
            case 2:
                SSQSApplication.glide.load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv1);
                holder.mMyPostItemIv1.setVisibility(View.VISIBLE);

                SSQSApplication.glide.load(url.get(1)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv2);
                holder.mMyPostItemIv2.setVisibility(View.VISIBLE);
                break;
            case 3:
                SSQSApplication.glide.load(url.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv1);
                holder.mMyPostItemIv1.setVisibility(View.VISIBLE);

                SSQSApplication.glide.load(url.get(1)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv2);
                holder.mMyPostItemIv2.setVisibility(View.VISIBLE);

                SSQSApplication.glide.load(url.get(2)).error(R.mipmap.fail).centerCrop().into(holder.mMyPostItemIv3);
                holder.mMyPostItemIv3.setVisibility(View.VISIBLE);

                break;
            default:
                break;
        }

        holder.mMyPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeViewPagerActivity.class);
                //添加数据传递过去到详情页面
                mId = topicsEntity.id;
                intent.putExtra("infoId", mId);
                Logger.INSTANCE.d(TAG, "赛事id---------------------" + topicsEntity.id);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.my_post_photo)
        ImageView mMyPostPhoto;
        @Bind(R.id.my_post_tv_nikname)
        TextView mMyPostTvNikname;
        @Bind(R.id.my_post_item_title)
        TextView mMyPostItemTitle;
        @Bind(R.id.my_post_item_content)
        TextView mMyPostItemContent;
        @Bind(R.id.my_post_item_iv1)
        ImageView mMyPostItemIv1;
        @Bind(R.id.my_post_item_iv2)
        ImageView mMyPostItemIv2;
        @Bind(R.id.my_post_item_iv3)
        ImageView mMyPostItemIv3;
        @Bind(R.id.my_post_publish_time)
        TextView mMyPostPublishTime;
        @Bind(R.id.my_post_zan)
        TextView mMyPostZan;
        @Bind(R.id.my_post_PL)
        TextView mMyPostPl;
        @Bind(R.id.my_post_item)
        LinearLayout mMyPostItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
