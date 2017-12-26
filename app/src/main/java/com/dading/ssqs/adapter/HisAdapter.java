package com.dading.ssqs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.BettingTBean;
import com.dading.ssqs.utils.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/1/9 11:54
 * 描述	      历史记录
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HisAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HisAdapter";
    private final Context context;
    private final ArrayList<BettingTBean.PayDetailsEntity> data;
    private View mView;
    private View mViewHowSerise;
    private View mViewNoSerise;

    public HisAdapter(Context context, ArrayList<BettingTBean.PayDetailsEntity> data) {
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
        ViewHolder hoder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.no_his_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        hoder.mBettingNoHisLy.removeAllViews();
        BettingTBean.PayDetailsEntity d = data.get(position);
        int cCount = d.cCount;
        for (BettingTBean.PayDetailsEntity.InfoEntity info : d.info) {
            hoder.mBettingLvNoHisNumCg.setText(info.orderID);
            hoder.mBettingLvNoHisTimeCg.setText(info.createDate);

            mView = View.inflate(context, R.layout.betting_children_match_single, null);
            TextView mBettingLvChildOrderTeamCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_order_team_cg1);
            TextView mBettingLvChildDoubleTeamCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_double_team_cg1);
            TextView mBettingLvChildOddsCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_odds_cg1);
            TextView mBettingLvChildOrderTypeCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_order_type_cg1);
            TextView mBettingLvChildWaitOpenCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_wait_open_cg1);

            switch (info.matchType) {
                // 1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
                case 0:
                    mBettingLvChildOrderTypeCg1.setText("冠军");
                    break;
                case 1:
                    mBettingLvChildOrderTypeCg1.setText("全场赛果");
                    break;
                case 2:
                    mBettingLvChildOrderTypeCg1.setText("当前让球");
                    break;
                case 3:
                    mBettingLvChildOrderTypeCg1.setText("全场大小");
                    break;
                case 4:
                    mBettingLvChildOrderTypeCg1.setText("半场赛果");
                    break;
                case 5:
                    mBettingLvChildOrderTypeCg1.setText("半场让球");
                    break;
                case 6:
                    mBettingLvChildOrderTypeCg1.setText("半场大小");
                    break;
                case 7:
                    mBettingLvChildOrderTypeCg1.setText("进球数区间");
                    break;
                case 8:
                    mBettingLvChildOrderTypeCg1.setText("全场比分");
                    break;
                case 9:
                    mBettingLvChildOrderTypeCg1.setText("全场比分");
                    break;
                case 10:
                    mBettingLvChildOrderTypeCg1.setText("全场比分");
                    break;
                case 11:
                    mBettingLvChildOrderTypeCg1.setText("全场比分");
                    break;
                case 12:
                    mBettingLvChildOrderTypeCg1.setText("半全场赛果");
                    break;
                case 13:
                    mBettingLvChildOrderTypeCg1.setText("全场单双");
                    break;
                case 14:
                    mBettingLvChildOrderTypeCg1.setText("半场单双");
                    break;
                case 15:
                    mBettingLvChildOrderTypeCg1.setText("滚球");
                    break;
                default:
                    break;
            }

            mBettingLvChildOrderTeamCg1.setText(info.name);
            String odds = "回报:" + info.payRate + "倍";
            mBettingLvChildOddsCg1.setText(odds);
            //1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半
            switch (info.status) {
                case 0:
                    mBettingLvChildWaitOpenCg1.setText("未开奖");
                    mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.blue_t1));
                    String text0 = info.home;
                    if (!TextUtils.isEmpty(info.away)) {
                        text0 += " vs " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text0);
                    Logger.d(TAG, "結果是--" + position + "---1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text0);
                    break;
                case 1:
                    mBettingLvChildWaitOpenCg1.setText("未完结");
                    mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.blue_t1));
                    String text = info.home;
                    if (!TextUtils.isEmpty(info.away)) {
                        text += " vs " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text);
                    Logger.d(TAG, "結果是--" + position + "---1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text);
                    break;
                case 2:
                    mBettingLvChildWaitOpenCg1.setText("赢");
                    mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.red_dark));
                    String text1;
                    if (info.matchType == 0) {
                        text1 = info.home;
                    } else {
                        text1 = info.home + " " + info.homeScore + ":" + info.awayScore + " " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text1);
                    Logger.d(TAG, "結果是--" + position + "---1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text1);
                    break;
                case 3:
                    mBettingLvChildWaitOpenCg1.setText("输");
                    mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.green_a));
                    String text2;
                    if (info.matchType == 0) {
                        text2 = info.home;
                    } else {
                        text2 = info.home + " " + info.homeScore + ":" + info.awayScore + " " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text2);
                    Logger.d(TAG, "結果是---" + position + "--1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text2);
                    break;
                case 4:
                    mBettingLvChildWaitOpenCg1.setText("平");
                    String text3;
                    if (info.matchType == 0) {
                        text3 = info.home;
                    } else {
                        text3 = info.home + " " + info.homeScore + ":" + info.awayScore + " " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text3);
                    Logger.d(TAG, "結果是--" + position + "---1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text3);
                    break;
                case 5:
                    String text4;
                    if (info.matchType == 0) {
                        text4 = info.home;
                    } else {
                        text4 = info.home + " " + info.homeScore + ":" + info.awayScore + " " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text4);
                    mBettingLvChildWaitOpenCg1.setText("输一半");
                    Logger.d(TAG, "結果是---" + position + "--1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text4);
                    break;
                case 6:
                    String text5;
                    if (info.matchType == 0) {
                        text5 = info.home;
                    } else {
                        text5 = info.home + " " + info.homeScore + ":" + info.awayScore + " " + info.away;
                    }
                    mBettingLvChildDoubleTeamCg1.setText(text5);
                    mBettingLvChildWaitOpenCg1.setText("赢一半");
                    Logger.d(TAG, "結果是---" + position + "--1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半---:" + info.status + "--" + text5);
                    break;
                default:
                    break;
            }
            hoder.mBettingNoHisLy.addView(mView);
        }
        if (cCount != 1) {
            mViewHowSerise = View.inflate(context, R.layout.serise_how, null);
            TextView mBettingPlOddsCg = (TextView) mViewHowSerise.findViewById(R.id.betting_pl_odds_cg);
            TextView mBettingLvChildAcountCg = (TextView) mViewHowSerise.findViewById(R.id.betting_lv_child_acount_cg);
            TextView mBettingLvChildExpectedReturnCg = (TextView) mViewHowSerise.findViewById(R.id.betting_lv_child_expected_return_cg);

            String text1 = "下注金币:" + d.info.get(0).amount;
            mBettingLvChildAcountCg.setText(text1);
            String returnGold = "预计返还:" + d.info.get(0).profit;
            mBettingLvChildExpectedReturnCg.setText(returnGold);
            String serise = cCount + "串1";
            mBettingPlOddsCg.setText(serise);
            hoder.mBettingNoHisLy.addView(mViewHowSerise);
        } else {
            mViewNoSerise = View.inflate(context, R.layout.no_serise_ly, null);
            TextView mBettingLvChildacount = (TextView) mViewNoSerise.findViewById(R.id.betting_lv_child_acount_1);
            TextView mBettingLvChildReturn = (TextView) mViewNoSerise.findViewById(R.id.betting_lv_child_expected_return_1);

            String text1 = "下注金币:" + d.info.get(0).amount;
            mBettingLvChildacount.setText(text1);

            String returnGold = "预计返还:" + d.info.get(0).profit;
            mBettingLvChildReturn.setText(returnGold);

            hoder.mBettingNoHisLy.addView(mViewNoSerise);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.betting_no_his_ly)
        LinearLayout mBettingNoHisLy;
        @Bind(R.id.betting_lv_no_his_num_cg)
        TextView mBettingLvNoHisNumCg;
        @Bind(R.id.betting_lv_no_his_time_cg)
        TextView mBettingLvNoHisTimeCg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
