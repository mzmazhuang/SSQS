package com.dading.ssqs.controllar.sns;

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
import com.dading.ssqs.activity.MyJsActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SnsBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 10:32
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HeadLineHeadAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HeadLineHeadAdapter";
    private Context context;
    private List<SnsBean.WritesEntity> data;

    public HeadLineHeadAdapter(Context context) {
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
            convertView = View.inflate(context, R.layout.head_line_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SnsBean.WritesEntity entity = data.get(position);
        LogUtil.util(TAG, "头条返回数据是------------------------------:" + entity.id);

        holder.snslvtitle.setText(entity.title);
        holder.snslvcomment.setText(entity.content);
        String talk = entity.commentCount + "评论";
        holder.snslvcommentnum.setText(talk);

        Glide.with(UIUtils.getContext())
                .load(entity.smallImage)
                .error(R.mipmap.fail)
                .centerCrop()
                .into(holder.snslviv);

        if (entity.isAdv == 1) {
            holder.snslvAdv.setVisibility(View.VISIBLE);
        } else {
            holder.snslvAdv.setVisibility(View.GONE);
        }
        if (entity.isDeep == 1) {
            holder.snslvDeep.setVisibility(View.VISIBLE);
        } else {
            holder.snslvDeep.setVisibility(View.GONE);
        }
        if (entity.isTop == 1) {
            holder.snslvTop.setVisibility(View.VISIBLE);
        } else {
            holder.snslvTop.setVisibility(View.GONE);
        }
        holder.mSnslvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyJsActivity.class);
                intent.putExtra(Constent.NEWS_ID, String.valueOf(entity.id));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public final ImageView snslviv;
        public final TextView snslvtitle;
        public final TextView snslvcomment;
        public final TextView snslvcommentnum;
        public final View root;
        private final ImageView snslvAdv;
        private final ImageView snslvDeep;
        private final ImageView snslvTop;
        private final LinearLayout mSnslvItem;

        public ViewHolder(View root) {
            mSnslvItem = (LinearLayout) root.findViewById(R.id.sns_lv_items);
            snslviv = (ImageView) root.findViewById(R.id.sns_lv_iv);
            snslvtitle = (TextView) root.findViewById(R.id.sns_lv_title);
            snslvcomment = (TextView) root.findViewById(R.id.sns_lv_comment);
            snslvcommentnum = (TextView) root.findViewById(R.id.sns_lv_comment_num);
            snslvAdv = (ImageView) root.findViewById(R.id.iv_adv);
            snslvDeep = (ImageView) root.findViewById(R.id.iv_deep);
            snslvTop = (ImageView) root.findViewById(R.id.iv_top);
            this.root = root;
        }
    }
}
