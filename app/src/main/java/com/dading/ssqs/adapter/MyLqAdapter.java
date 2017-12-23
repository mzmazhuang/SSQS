package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.MatchInfoLqBean;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/27 18:03
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyLqAdapter extends BaseAdapter implements ListAdapter {
    private List<MatchInfoLqBean> data;
    private Context                                      context;

    public MyLqAdapter(Context context, List<MatchInfoLqBean> list) {
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
            convertView = View.inflate(context, R.layout.lq_lv_item, null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        MatchInfoLqBean entity = data.get(position);
            hoder.otherSayItem.setVisibility(View.VISIBLE);
            hoder.mySayItem.setVisibility(View.GONE);
            String text = entity.content;
            hoder.otherPublishText.setText(text);
            hoder.otherPublishTime.setText(entity.createDate);
            hoder.otherUserName.setText(entity.userName);

        Glide.with(context.getApplicationContext())
                .load( entity.avatar)
                .error(R.mipmap.fail)
                .centerCrop( )
                .transform(new GlideCircleTransform(context))
                .into( hoder.otherUserPhoto);

        return convertView;
    }

    class ViewHoder {
        @Bind(R.id.lq_my_say_item)
        LinearLayout mySayItem;
        @Bind(R.id.lq_other_say_item)
        LinearLayout otherSayItem;

        @Bind(R.id.lq_user_name_my)
        public TextView  myUserName;
        @Bind(R.id.lq_publish_time_my)
        public TextView  myPublishTime;
        @Bind(R.id.lq_publish_text_my)
        public TextView  myPublishText;
        @Bind(R.id.lq_user_name_photo_my)
        public ImageView myUserPhoto;

        @Bind(R.id.lq_user_name_other)
        public TextView  otherUserName;
        @Bind(R.id.lq_publish_time_other)
        public TextView  otherPublishTime;
        @Bind(R.id.lq_publish_text_other)
        public TextView  otherPublishText;
        @Bind(R.id.lq_user_name_photo_other)
        public ImageView otherUserPhoto;

        public ViewHoder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
