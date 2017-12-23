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
import com.dading.ssqs.bean.SavantInfoBean;

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
public class SavantInfoAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "SavantInfoAdapter";
    private final Context                                      context;
    private final List<SavantInfoBean.RecommEntity> data;

    public SavantInfoAdapter(Context context, List<SavantInfoBean.RecommEntity> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        if (data != null) {
            if (data.size() > 10) {
                return 10;
            } else {
                return data.size();
            }
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.savant_info_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SavantInfoBean.RecommEntity entity = data.get(position);

        holder.matchType.setText(entity.leagueName);
        holder.matchMain.setText(entity.home);
        holder.matchSecond.setText(entity.away);

        String createDate = entity.createDate;

        if (createDate != null) {
            holder.publishTime.setText(createDate.substring(5, 16));//2016-07-29 11:46:05
        }

        holder.matchData.setText(entity.openTime.substring(5, 10));

        holder.matchDataHour.setText(entity.openTime.substring(11, 16));
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
        switch (entity.isBuy) {
            case 0:
                if (entity.amount == -1) {
                    holder.matchPrice.setVisibility(View.GONE);
                } else if (entity.amount == 0) {
                    holder.matchPrice.setVisibility(View.VISIBLE);
                    holder.matchPrice.setText("免费");
                } else {
                    holder.matchPrice.setVisibility(View.VISIBLE);
                    String price = entity.amount + "钻查看";
                    holder.matchPrice.setText(price);
                }
                break;
            case 1:
                holder.matchPrice.setVisibility(View.VISIBLE);
                holder.matchPrice.setText("已购买");
                break;
            case 2:
                holder.matchPrice.setVisibility(View.VISIBLE);
                holder.matchPrice.setText("我的推荐");
                break;

            default:
                break;
        }

        holder.matchType.setText(entity.payRateName);
        String text = entity.suppCount + "";
        holder.goodNum.setText(text);

        String text1 = entity.hateCount + "";
        holder.badNum.setText(text1);
        return convertView;
    }

    class ViewHolder {
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
        TextView     badNum;
        @Bind(R.id.savant_info_good_item_num_ly)
        LinearLayout goodLy;
        @Bind(R.id.savant_info_item_goodnum)
        TextView     goodNum;
        @Bind(R.id.savant_info_lv_item_refer_result)
        ImageView    result;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
