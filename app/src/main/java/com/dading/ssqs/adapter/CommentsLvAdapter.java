package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.NewInfoBean;
import com.dading.ssqs.view.GlideCircleTransform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/24 11:56
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CommentsLvAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final List<NewInfoBean.CommentsEntity> data;

    public CommentsLvAdapter(Context context, List<NewInfoBean.CommentsEntity> comments) {
        this.context = context;
        this.data = comments;
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
            convertView = View.inflate(context, R.layout.comments_items, null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        SSQSApplication.glide.load(data.get(position).avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(hoder.mCommentsLvPhoto);

        hoder.mCommentsLvName.setText(data.get(position).userName);
        NewInfoBean.CommentsEntity commentsEntity = data.get(position);
        if (commentsEntity.createDate != null) {
            String s = commentsEntity.createDate.substring(5, 16);
            hoder.mCommentsLvPublish.setText(s);
        }
        // String s = createDate.substring(5, 16);
        String content = data.get(position).content;
        try {
            String context = content.replace("_ssqs_", "");
            String decode = URLDecoder.decode(context, "UTF-8");
            hoder.mCommentsLvContext.setText(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHoder {
        @Bind(R.id.comments_lv_photo)
        ImageView mCommentsLvPhoto;
        @Bind(R.id.comments_lv_name)
        TextView mCommentsLvName;
        @Bind(R.id.comments_lv_publish)
        TextView mCommentsLvPublish;
        @Bind(R.id.comments_lv_context)
        TextView mCommentsLvContext;

        public ViewHoder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}