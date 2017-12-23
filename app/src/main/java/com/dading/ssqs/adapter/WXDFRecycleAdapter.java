package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/23 15:09
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WXDFRecycleAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final List<WXDFBean> data;
    private final int type;
    private List<WXDFBean.InfoBean> mInfo;

    public WXDFRecycleAdapter(Context context, List<WXDFBean> data, int i) {
        this.context = context;
        this.data = data;
        this.type = i;
    }

    @Override
    public int getCount() {
        if (data != null) {
            int size = 0;
            switch (type) {
                case 0:
                    if (data.size() > 0) {
                        List<WXDFBean.InfoBean> info = data.get(0).getInfo();
                        if (info != null)
                            size = info.size();
                    }
                    break;
                case 1:
                    if (data.size() > 1) {
                        List<WXDFBean.InfoBean> info = data.get(1).getInfo();
                        if (info != null)
                            size = info.size();
                    }
                    break;
                case 2:
                    if (data.size() > 2) {
                        List<WXDFBean.InfoBean> info = data.get(2).getInfo();
                        if (info != null)
                            size = info.size();
                    }
                    break;
            }
            return size;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.wx_daifu_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mWxDfItemCb.setVisibility(View.GONE);

        switch (type) {
            case 0:
                mInfo = data.get(0).getInfo();
                break;
            case 1:
                mInfo = data.get(1).getInfo();
                break;
            case 2:
                mInfo = data.get(2).getInfo();
                break;

            default:
                break;
        }
        final WXDFBean.InfoBean bean = mInfo.get(position);

        if (bean != null) {
            holder.mWxDfItemTitle.setText(bean.getName());
            holder.mWxDfItemCb.setChecked(bean.isChecked());
            String logo = bean.getLogo();

            SSQSApplication.glide.load(logo).error(R.mipmap.fail).centerCrop().into(holder.mWxDfItemIcon);

            if (data.size() > 0) {
                WXDFBean dataBean = data.get(0);
                Logger.d("WXDaifuActivity", "返回数据是------:" + dataBean.getRemark());
                holder.mWxDfItemContent.setText(bean.getMfrom() + "--" + bean.getMto());
            }

        }

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.wx_df_item_ly)
        LinearLayout mWxDfItemLy;
        @Bind(R.id.wx_df_item_icon)
        ImageView mWxDfItemIcon;
        @Bind(R.id.wx_df_item_title)
        TextView mWxDfItemTitle;
        @Bind(R.id.wx_df_item_content)
        TextView mWxDfItemContent;
        @Bind(R.id.wx_df_item_cb)
        CheckBox mWxDfItemCb;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
