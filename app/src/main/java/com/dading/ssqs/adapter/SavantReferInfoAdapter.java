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
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantLvItemActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ReferInfoARBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/6 16:38
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantReferInfoAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "SavantInfoAdapter";
    private final Context context;
    private final List<ReferInfoARBean> data;


    public SavantReferInfoAdapter(Context context, List<ReferInfoARBean> data) {
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.savant_info_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReferInfoARBean entity = data.get(position);

        holder.matchType.setText(entity.leagueName);
        holder.matchMain.setText(entity.home);
        holder.matchSecond.setText(entity.away);

        String createDate = entity.createDate;

        Logger.INSTANCE.d(TAG, "创建时间返回数据是------------------------------:" + createDate);
        if (createDate != null) {
            String date = createDate.substring(5, 16);
            holder.publishTime.setText(date);//2016-07-29 11:46:05
        }

        String s = entity.openTime.substring(5, 10);
        holder.matchData.setText(s);

        holder.matchDataHour.setText(entity.openTime.substring(11, 16));
        if (entity.amount == -1) {
            holder.matchPrice.setVisibility(View.GONE);
        } else if (entity.amount == 0) {
            holder.matchPrice.setVisibility(View.VISIBLE);
            holder.matchPrice.setText("免费");
        }
        if (entity.isBuy == 2) {
            holder.matchPrice.setText("我的推荐");
        } else if (entity.isBuy == 0) {
            String s2 = entity.amount + "钻购买";
            holder.matchPrice.setText(s2);
        } else {
            holder.matchPrice.setText("已购买");
        }

        holder.matchType.setText(entity.payRateName);
        String text = entity.suppCount + "";
        holder.goodNum.setText(text);

        String text1 = entity.hateCount + "";
        holder.badNum.setText(text1);

        //1---未完结 2---赢 3--输 4--平  5--输一半6--赢一半
        switch (entity.status) {
            case 1:
                holder.result.setVisibility(View.GONE);
                break;
            case 2:
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setImageResource(R.mipmap.ying);
                break;
            case 3:
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setImageResource(R.mipmap.shu);
                break;
            case 4:
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setImageResource(R.mipmap.pingju);
                break;
            case 5:
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setImageResource(R.mipmap.shuban);
                break;
            case 6:
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setImageResource(R.mipmap.yingban);
                break;

            default:
                break;
        }

        holder.savantInfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRefer = new Intent(context, SavantLvItemActivity.class);
                String value = entity.id + "";
                intentRefer.putExtra(Constent.MATCH_ID, value);
                //跳转进推荐内容也
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (entity.isBuy == 0) {
                        String dimons = UIUtils.getSputils().getString(Constent.DIAMONDS, "0");
                        int i = Integer.parseInt(dimons.replaceAll(",", ""));
                        if (entity.amount <= i) {
                            i = i - entity.amount;
                            UIUtils.getSputils().putString(Constent.DIAMONDS, i + "");
                            entity.isBuy = 1;
                            holder.matchPrice.setText("已购买");

                            UIUtils.SendReRecevice(Constent.SERIES);
                            context.startActivity(intentRefer);
                        } else {
                            //TmtUtils.midToast(context, "对不起您的余额不足请充值!", 0);

                        }
                    } else {
                        context.startActivity(intentRefer);
                    }
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.savant_info_lv_item)
        LinearLayout savantInfoItem;
        @Bind(R.id.savant_info_lv_item_type)
        TextView matchType;
        @Bind(R.id.savant_info_lv_item_main)
        TextView matchMain;
        @Bind(R.id.savant_info_lv_item_second)
        TextView matchSecond;
        @Bind(R.id.savant_info_lv_item_time)
        TextView publishTime;

        @Bind(R.id.savant_info_lv_item_data)
        TextView matchData;
        @Bind(R.id.savant_info_lv_item_hour)
        TextView matchDataHour;
        @Bind(R.id.savant_info_lv_item_price)
        TextView matchPrice;

        /*@Bind(R.id.savant_info_lv_item_iv_winfail)
        ImageView    winOrFail;*/
        @Bind(R.id.savant_info_item_bad_num_ly)
        LinearLayout badLy;
        @Bind(R.id.savant_info_item_badnum)
        TextView badNum;
        @Bind(R.id.savant_info_good_item_num_ly)
        LinearLayout goodLy;
        @Bind(R.id.savant_info_item_goodnum)
        TextView goodNum;
        @Bind(R.id.savant_info_lv_item_refer_result)
        ImageView result;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
