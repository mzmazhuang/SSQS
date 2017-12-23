package com.dading.ssqs.controllar.sns;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.MyJsActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SnsBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.components.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import com.dading.ssqs.components.richtextview.RichTextView;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 11:16
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FootBallBabyAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "FootBallBabyAdapter";
    private final Context context;
    private final List<SnsBean.WritesEntity> data;

    public FootBallBabyAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setList(List<SnsBean.WritesEntity> list) {
        if (list != null) {
            this.data.clear();
            this.data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addList(List<SnsBean.WritesEntity> list) {
        if (list != null) {
            this.data.addAll(list);
            notifyDataSetChanged();
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.football_baby_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SnsBean.WritesEntity entity = data.get(position);

        SSQSApplication.glide.load(entity.avatar).error(R.mipmap.nologinportrait).centerCrop().transform(new GlideCircleTransform(context)).into(holder.footballbabyphoto);

        holder.footballbabynickname.setText(entity.userName);

        String html = entity.content;

        holder.footballbabypcontent.setHtml(html, 800);
        holder.footballbabypcontent.setMovementMethod(LinkMovementMethod.getInstance());

        String date = entity.createDate;
        if (date != null && !TextUtils.isEmpty(date)) {
            String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
            if (diffCurTime.isEmpty()) {
                String t1 = "刚刚发布";
                holder.footballbabypublishtime.setText(t1);
            }
            String t2 = diffCurTime + "前发布";
            holder.footballbabypublishtime.setText(t2);
        }
        holder.mFootballbabyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyJsActivity.class);
                intent.putExtra(Constent.NEWS_ID, String.valueOf(entity.id));
                context.startActivity(intent);
                Logger.d(TAG, "足球宝贝跳转------------------------------:");
            }
        });
        holder.footballbabypcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyJsActivity.class);
                intent.putExtra(Constent.NEWS_ID, String.valueOf(entity.id));
                context.startActivity(intent);
                Logger.d(TAG, "足球宝贝跳转------------------------------:");
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public final ImageView footballbabyphoto;
        public final TextView footballbabynickname;
        public final TextView footballbabydescribe;
        public final TextView footballbabypublishtime;
        public final RichTextView footballbabypcontent;
        public final View root;
        private final LinearLayout mFootballbabyItem;

        public ViewHolder(View root) {
            mFootballbabyItem = (LinearLayout) root.findViewById(R.id.football_baby_item);
            footballbabyphoto = (ImageView) root.findViewById(R.id.football_baby_photo);
            footballbabynickname = (TextView) root.findViewById(R.id.football_baby_nickname);
            footballbabydescribe = (TextView) root.findViewById(R.id.football_baby_describe);
            footballbabypublishtime = (TextView) root.findViewById(R.id.football_baby_publish_time);
            footballbabypcontent = (RichTextView) root.findViewById(R.id.football_baby_pcontent);
            // footballbabyselftimer = (ImageView) root.findViewById(R.id.football_baby_self_timer);
            this.root = root;
        }
    }
}