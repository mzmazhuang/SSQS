package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.dading.ssqs.bean.MyTzBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/25 13:55
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFTAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyFTAdapter";
    private final Context context;
    private final List<MyTzBean> data;
    private int mId;

    public MyFTAdapter(Context context, List<MyTzBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_ft_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyTzBean entity = data.get(position);
        holder.mMyFtItemTitle.setText(entity.title.replaceAll("_ssqs_", ""));
        holder.mMyFtItemContent.setText(entity.content.replaceAll("_ssqs_", ""));
        holder.mMyFtItemExplanName.setText(entity.userName);
        String date = entity.createDate;
        String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
        if (TextUtils.isEmpty(diffCurTime))
            diffCurTime = "0";
        String text = diffCurTime + "前发布";
        holder.mMyFtItemExplanTime.setText(text);
        List<String> imageUrl = entity.imageUrl;
        switch (imageUrl.size()) {
            case 0:
                holder.mMyFtItemIv2.setImageResource(R.mipmap.query_background);
                holder.mMyFtItemIv3.setImageResource(R.mipmap.query_background);
                holder.mMyFtItemIv1.setImageResource(R.mipmap.query_background);
                holder.mMyFtItemLy.setVisibility(View.GONE);
                break;
            case 1:
                holder.mMyFtItemLy.setVisibility(View.VISIBLE);

                SSQSApplication.glide.load(imageUrl.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv1);

                holder.mMyFtItemIv2.setImageResource(R.mipmap.query_background);
                holder.mMyFtItemIv3.setImageResource(R.mipmap.query_background);
                break;
            case 2:
                holder.mMyFtItemLy.setVisibility(View.VISIBLE);

                SSQSApplication.glide.load(imageUrl.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv1);
                SSQSApplication.glide.load(imageUrl.get(1)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv2);

                holder.mMyFtItemIv3.setImageResource(R.mipmap.query_background);
                break;
            case 3:
                holder.mMyFtItemLy.setVisibility(View.VISIBLE);
                SSQSApplication.glide.load(imageUrl.get(0)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv1);
                SSQSApplication.glide.load(imageUrl.get(1)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv2);
                SSQSApplication.glide.load(imageUrl.get(2)).error(R.mipmap.fail).centerCrop().into(holder.mMyFtItemIv3);
            default:
                break;
        }
        String zan = entity.zanCount + "";
        holder.mMyFtNumberGoodsHome.setText(zan);
        String commentCount = entity.commentCount + "";
        holder.mMyFtNumberCommentsHome.setText(commentCount);

        holder.mMyFtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳进帖子详情
                Intent intent = new Intent(context, HomeViewPagerActivity.class);
                //添加数据传递过去到详情页面
                mId = entity.id;
                intent.putExtra("infoId", mId);
                Logger.d(TAG, "赛事id---------------------" + mId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.my_ft_item_ly)
        LinearLayout mMyFtItemLy;
        @Bind(R.id.my_ft_item_title)
        TextView mMyFtItemTitle;
        @Bind(R.id.my_ft_item_content)
        TextView mMyFtItemContent;
        @Bind(R.id.my_ft_item_iv1)
        ImageView mMyFtItemIv1;
        @Bind(R.id.my_ft_item_iv2)
        ImageView mMyFtItemIv2;
        @Bind(R.id.my_ft_item_iv3)
        ImageView mMyFtItemIv3;
        @Bind(R.id.my_ft_item_explan_name)
        TextView mMyFtItemExplanName;
        @Bind(R.id.my_ft_item_explan_time)
        TextView mMyFtItemExplanTime;
        @Bind(R.id.my_ft_number_goods_home)
        TextView mMyFtNumberGoodsHome;
        @Bind(R.id.my_ft_number_comments_home)
        TextView mMyFtNumberCommentsHome;
        @Bind(R.id.my_ft_item)
        LinearLayout mMyFtItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
