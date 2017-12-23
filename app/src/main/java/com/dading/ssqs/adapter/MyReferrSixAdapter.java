package com.dading.ssqs.adapter;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/26 10:15
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

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
import com.dading.ssqs.activity.SavantLvItemActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MyReferBean;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 推荐比赛适配器---referControllar
 */
public class MyReferrSixAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyReferrLvAdapter";
    private final Context                                  mContent;
    private final List<MyReferBean> data;

    public MyReferrSixAdapter(Context context, List<MyReferBean> items) {

        this.mContent = context;
        this.data = items;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 3;
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
            convertView = View.inflate(mContent, R.layout.my_refer_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyReferBean entity = data.get(position);

        holder.mMyReferGuessType.setText(entity.payRateName);

        holder.mMyReferMatchType.setText(entity.leagueName);
        holder.mMyReferMatchMain.setText(entity.home);
        holder.mMyReferMatchSecond.setText(entity.away);
        holder.mMyReferMatchData.setText(entity.openTime.substring(5, 10));
        holder.mMyReferGuessPrice.setText("我的推荐");
        String date = entity.createDate;
        String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
        if (TextUtils.isEmpty(diffCurTime))
            diffCurTime = "0";
        String text2 = diffCurTime + "前发布";
        holder.mMyReferUploadTime.setText(text2);
        String text = entity.suppCount + "";
        holder.mMyReferGoodnum.setText(text);
        String text1 = entity.hateCount + "";
        holder.mMyReferBadnum.setText(text1);
        //1---未完结 2---赢 3--输 4--平  5--输一半6--赢一半
        switch (entity.status) {
            case 1:
                holder.mMyReferResult.setVisibility(View.GONE);
                break;
            case 2:
                holder.mMyReferResult.setVisibility(View.VISIBLE);
                holder.mMyReferResult.setImageResource(R.mipmap.ying);
                break;
            case 3:
                holder.mMyReferResult.setVisibility(View.VISIBLE);
                holder.mMyReferResult.setImageResource(R.mipmap.shu);
                break;
            case 4:
                holder.mMyReferResult.setVisibility(View.VISIBLE);
                holder.mMyReferResult.setImageResource(R.mipmap.pingju);
                break;
            case 5:
                holder.mMyReferResult.setVisibility(View.VISIBLE);
                holder.mMyReferResult.setImageResource(R.mipmap.shuban);
                break;
            case 6:
                holder.mMyReferResult.setVisibility(View.VISIBLE);
                holder.mMyReferResult.setImageResource(R.mipmap.yingban);
                break;

            default:
                break;
        }
        //// TODO: 2016/11/14 待展示
        holder.mMyReferItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转进推荐内容也
                Intent intentRefer = new Intent(mContent, SavantLvItemActivity.class);
                intentRefer.putExtra(Constent.MATCH_ID, String.valueOf(entity.id));
                mContent.startActivity(intentRefer);
            }
        });
        holder.mMyReferGuessPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果余额不足提示如果满足就跳进推荐
                Intent intentRefer = new Intent(mContent, SavantLvItemActivity.class);
                intentRefer.putExtra(Constent.MATCH_ID, String.valueOf(entity.id));
                mContent.startActivity(intentRefer);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.my_refer_match_type)
        TextView mMyReferMatchType;
        @Bind(R.id.my_refer_match_main)
        TextView mMyReferMatchMain;
        @Bind(R.id.my_refer_match_second)
        TextView mMyReferMatchSecond;
        @Bind(R.id.my_refer_match_data)
        TextView mMyReferMatchData;
        @Bind(R.id.my_refer_guess_type)
        TextView mMyReferGuessType;
        @Bind(R.id.my_refer_guess_price)
        TextView mMyReferGuessPrice;
        @Bind(R.id.my_refer_upload_time)
        TextView mMyReferUploadTime;
        @Bind(R.id.my_refer_badnum)
        TextView mMyReferBadnum;
        @Bind(R.id.my_refer_goodnum)
        TextView mMyReferGoodnum;

        @Bind(R.id.my_referr_item)
        LinearLayout mMyReferItem;
        @Bind(R.id.my_refer_result)
        ImageView    mMyReferResult;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
