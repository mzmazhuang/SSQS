package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by lenovo on 2017/9/22.
 */
public class HotMatchInfoLAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HotMatchInfoLAdapter";
    private final List<JCbean>             data;
    private final Context                             context;
    private final HashMap<Integer, JCbean> mMap;
    private final boolean isFootBall;

    public HotMatchInfoLAdapter (List<JCbean> data, Context context, boolean isFootBall) {
        this.context = context;
        this.data = data;
        this.isFootBall = isFootBall;
        mMap = new HashMap<>( );
    }

    @Override
    public int getCount ( ) {
        if (data != null)
            return data.size( );

        return 0;
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.hot_match_info_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag( );
        }
        final JCbean d = data.get(position);

        holder.mMatchInfoItemTitle.setText(d.payTypeName);
        switch (d.payTypeName) {
            case "全场赛果":
                if (d.mAllResult == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);

                if (isFootBall)
                    holder.mItemSonMid.setVisibility(View.VISIBLE);
                else
                    holder.mItemSonMid.setVisibility(View.GONE);

                LogUtil.util(TAG,"是不是足球------------:"+isFootBall);

                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.guess_ninty_result));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.vs);
                break;
            case "当前让球":
                if (d.mNowLost == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                holder.mItemSonLeftMid.setVisibility(View.VISIBLE);
                holder.mItemSonRightMid.setVisibility(View.VISIBLE);
                holder.mItemSonMid.setVisibility(View.GONE);
                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.ninty_lostball));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.rangqiu);
                setLost(holder, d);
                break;
            case "半场让球":
                if (d.mHalfLost == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                holder.mItemSonLeftMid.setVisibility(View.VISIBLE);
                holder.mItemSonRightMid.setVisibility(View.VISIBLE);
                holder.mItemSonMid.setVisibility(View.GONE);
                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.guess_half_match_lost));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.rang_banchang);
                setLost(holder, d);
                break;
            case "全场大小":
                if (d.mAllSBig == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                setSB(holder, d);
                holder.mItemSonMid.setVisibility(View.GONE);
                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.ninty_all_balls));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.daxiao);
                break;
            case "半场赛果":
                if (d.mHalfResult == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);

                if (isFootBall)
                    holder.mItemSonMid.setVisibility(View.VISIBLE);
                else
                    holder.mItemSonMid.setVisibility(View.GONE);

                LogUtil.util(TAG,"是不是足球------------:"+isFootBall);

                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.guess_half_result));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.vs_banchang);
                break;
            case "半场大小":
                if (d.mHalfSBig == 1)
                    holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                else
                    holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                setSB(holder, d);
                holder.mItemSonMid.setVisibility(View.GONE);
                holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.half_samll_big_score_num));
                holder.mMatchInfoItemIcon.setImageResource(R.mipmap.da_bangchang);
                break;
        }

        //主队
        holder.mItemSonLeftName.setText(d.home);
        holder.mItemSonLeftNum.setText(d.realRate1);
        //平局
        holder.mItemSonMidName.setText(context.getString(R.string.draw_result));
        holder.mItemSonMidNum.setText(d.realRate2);
        //客队
        holder.mItemSonRightName.setText(d.away);
        holder.mItemSonRightNum.setText(d.realRate3);
        if (!d.cbTag1) {
            holder.mItemSonLeft.setBackgroundColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonLeftName.setTextColor(context.getResources( ).getColor(R.color.black));
            holder.mItemSonLeftNum.setTextColor(context.getResources( ).getColor(R.color.orange));
        } else {
            holder.mItemSonLeft.setBackgroundColor(context.getResources( ).getColor(R.color.orange));
            holder.mItemSonLeftName.setTextColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonLeftNum.setTextColor(context.getResources( ).getColor(R.color.yellow_light));
        }
        if (!d.cbTag2) {
            holder.mItemSonMid.setBackgroundColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonMidName.setTextColor(context.getResources( ).getColor(R.color.black));
            holder.mItemSonMidNum.setTextColor(context.getResources( ).getColor(R.color.orange));
        } else {
            holder.mItemSonMid.setBackgroundColor(context.getResources( ).getColor(R.color.orange));
            holder.mItemSonMidName.setTextColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonMidNum.setTextColor(context.getResources( ).getColor(R.color.yellow_light));
        }
        if (!d.cbTag3) {
            holder.mItemSonRight.setBackgroundColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonRightName.setTextColor(context.getResources( ).getColor(R.color.black));
            holder.mItemSonRightNum.setTextColor(context.getResources( ).getColor(R.color.orange));
        } else {
            holder.mItemSonRight.setBackgroundColor(context.getResources( ).getColor(R.color.orange));
            holder.mItemSonRightName.setTextColor(context.getResources( ).getColor(R.color.white));
            holder.mItemSonRightNum.setTextColor(context.getResources( ).getColor(R.color.yellow_light));
        }
        holder.mItemSonLeft.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                d.cbTag1 = !d.cbTag1;
                if (d.cbTag1) {
                    mMap.put(d.id, d);
                } else {
                    if (!d.cbTag1 && !d.cbTag2 && !d.cbTag3)
                        mMap.remove(d);
                }
                notifyDataSetChanged( );
                UIUtils.SendReRecevice(Constent.HOT_DATA);
            }
        });
        holder.mItemSonMid.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                d.cbTag2 = !d.cbTag2;
                if (d.cbTag2) {
                    mMap.put(d.id, d);
                } else {
                    if (!d.cbTag1 && !d.cbTag2 && !d.cbTag3)
                        mMap.remove(d);
                }
                UIUtils.SendReRecevice(Constent.HOT_DATA);
                notifyDataSetChanged( );
            }
        });
        holder.mItemSonRight.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                d.cbTag3 = !d.cbTag3;
                if (d.cbTag3) {
                    mMap.put(d.id, d);
                } else {
                    if (!d.cbTag1 && !d.cbTag2 && !d.cbTag3)
                        mMap.remove(d);
                }
                UIUtils.SendReRecevice(Constent.HOT_DATA);
                notifyDataSetChanged( );
            }
        });
        return convertView;
    }

    private void setSB (ViewHolder baseViewHolder, JCbean d) {
        String highBs = "高于" + d.realRate2;
        String lowBs = "低于" + d.realRate2;
        baseViewHolder.mItemSonLeftName.setText(highBs);
        baseViewHolder.mItemSonRightName.setText(lowBs);
        baseViewHolder.mItemSonLeftNum.setText(d.realRate1);
        baseViewHolder.mItemSonRightNum.setText(d.realRate3);
    }

    private void setLost (ViewHolder baseViewHolder, JCbean d) {
        if (d.realRate2.contains("/")) {
            String[] split = d.realRate2.split("/");
            if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                if (Double.valueOf(split[0]) > 0) {
                    String s = "+" + split[0];
                    baseViewHolder.mItemSonLeftMid.setText(s);
                } else {
                    baseViewHolder.mItemSonLeftMid.setText(split[0]);
                }
                if (Double.valueOf(split[1]) > 0) {
                    String s = "+" + split[1];
                    baseViewHolder.mItemSonRightMid.setText(s);
                } else {
                    baseViewHolder.mItemSonRightMid.setText(split[1]);
                }
            } else {
                baseViewHolder.mItemSonLeftMid.setText(d.realRate2);
                baseViewHolder.mItemSonRightMid.setText(d.realRate2);
            }
        } else {
            if (Double.valueOf(d.realRate2) > 0) {
                String s = "+" + d.realRate2;
                baseViewHolder.mItemSonLeftMid.setText(s);
                String s1 = "-" + d.realRate2;
                baseViewHolder.mItemSonRightMid.setText(s1);
            } else {
                if (Double.valueOf(d.realRate2) == 0) {
                    String s = "" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.mItemSonLeftMid.setText(s);
                    String s1 = "" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.mItemSonRightMid.setText(s1);
                } else {
                    baseViewHolder.mItemSonLeftMid.setText(d.realRate2);
                    String text = "+" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.mItemSonRightMid.setText(text);
                }
            }
        }
    }

    public HashMap<Integer, JCbean> getChecked ( ) {
        return mMap;
    }

    class ViewHolder {
        @Bind(R.id.match_info_item_icon)
        ImageView    mMatchInfoItemIcon;
        @Bind(R.id.match_info_item_title)
        TextView     mMatchInfoItemTitle;
        @Bind(R.id.match_info_item_subscribe)
        TextView     mMatchInfoItemSubscribe;
        @Bind(R.id.match_info_item_title_ly)
        LinearLayout mMatchInfoItemTitleLy;
        @Bind(R.id.item_son_left_name)
        TextView     mItemSonLeftName;
        @Bind(R.id.item_son_left_mid)
        TextView     mItemSonLeftMid;
        @Bind(R.id.item_son_left_num)
        TextView     mItemSonLeftNum;
        @Bind(R.id.item_son_left)
        LinearLayout mItemSonLeft;
        @Bind(R.id.item_son_mid_name)
        TextView     mItemSonMidName;
        @Bind(R.id.item_son_mid_num)
        TextView     mItemSonMidNum;
        @Bind(R.id.item_son_mid)
        LinearLayout mItemSonMid;
        @Bind(R.id.item_son_right_name)
        TextView     mItemSonRightName;
        @Bind(R.id.item_son_right_mid)
        TextView     mItemSonRightMid;
        @Bind(R.id.item_son_right_num)
        TextView     mItemSonRightNum;
        @Bind(R.id.item_son_right)
        LinearLayout mItemSonRight;

        ViewHolder (View view) {
            ButterKnife.bind(this, view);
        }
    }
}
