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
import com.dading.ssqs.bean.MyTzGTBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.components.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/1/10 9:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyGTAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyGTAdapter";
    private final Context                                 context;
    private final List<MyTzGTBean> data;
    private       View                                    mView;
    private       ImageView                               mPhoto;
    private       TextView                                mName;
    private       TextView                                mTime;
    private       TextView                                mText;
    private       int                                     mId;

    public MyGTAdapter(Context context, List<MyTzGTBean> bean) {
        this.context = context;
        this.data = bean;
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
            convertView = View.inflate(context, R.layout.gt_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        hoder.mGtItemFram.removeAllViews();
        final MyTzGTBean itemsEntity = data.get(position);
        String s = "原帖:" + itemsEntity.title;
        hoder.mNoteGtTitle.setText(s);

        for (MyTzGTBean.CommentsEntity comm : itemsEntity.comments) {
            mView = View.inflate(context, R.layout.my_follow_note_item, null);
            mPhoto = ButterKnife.findById(mView, R.id.my_note_item_photo);
            mName = ButterKnife.findById(mView, R.id.my_note_item_name);
            mTime = ButterKnife.findById(mView, R.id.my_note_item_publish_time);
            mText = ButterKnife.findById(mView, R.id.my_note_item_publish_text);

            SSQSApplication.glide.load(comm.avatar ).error(R.mipmap.fail).centerCrop( ).transform(new GlideCircleTransform(context)).into(mPhoto );

            mName.setText(comm.userName);
            mText.setText(comm.content);

            String date = comm.createDate;
            String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
            if (TextUtils.isEmpty(diffCurTime))
                diffCurTime = "0";
            String text = diffCurTime + "前发布";
            mTime.setText(text);
                hoder.mGtItemFram.addView(mView);
        }

        hoder.mNoteGtLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeViewPagerActivity.class);
                //添加数据传递过去到详情页面
                mId = itemsEntity.articleID;
                intent.putExtra("infoId", mId);
                Logger.d(TAG, "赛事id---------------------" + mId);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.note_gt_ly)
        LinearLayout mNoteGtLy;
        @Bind(R.id.note_gt_title)
        TextView     mNoteGtTitle;
        @Bind(R.id.gt_item_fram)
        LinearLayout mGtItemFram;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
