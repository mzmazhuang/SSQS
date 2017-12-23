package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.BettingTBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/26 16:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyBettingAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "MyBettingAdapter";
    private Context                       context;
    private List<BettingTBean> data;
    private View                          mView;
    private View                          mViewHowSerise;
    private View mViewNoSerise;

    public MyBettingAdapter(Context context, List<BettingTBean> tBean) {
        this.context = context;
        this.data = tBean;
    }

    @Override
    public int getGroupCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<BettingTBean.PayDetailsEntity> details = data.get(groupPosition).payDetails;
        Logger.d(TAG, "子节点条目是------------------------------:" + details.size());
        if (details != null) {
            return details.size();
        }
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.betting_group_item, null);
            holder.mBettingLvItmeGroup = (TextView) convertView.findViewById(R.id.betting_lv_itme_group_data);
            holder.mBettingLvItmeIv = (ImageView) convertView.findViewById(R.id.betting_lv_itme_group_iv);
            holder.mBettingLvItmeProfit = (TextView) convertView.findViewById(R.id.betting_lv_itme_group_profit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BettingTBean entity = data.get(groupPosition);
        if (entity != null) {
            holder.mBettingLvItmeProfit.setText(entity.wins);
            List<BettingTBean.PayDetailsEntity> payDetails = entity.payDetails;
            if (payDetails.size() != 0 && payDetails.get(0).info.size() != 0) {
                String openTime = payDetails.get(0).info.get(0).openTime;
                Logger.d(TAG, "开始时间返回数据是------------------------------:" + openTime);
                if (openTime != null) {
                    String date = openTime.substring(0, 11);
                    String week = DateUtils.getweekdayBystr(date);
                    String text = date + "   " + week;
                    holder.mBettingLvItmeGroup.setText(text);
                }

                if (isExpanded) {
                    holder.mBettingLvItmeIv.setImageResource(R.mipmap.shang_);
                } else {
                    holder.mBettingLvItmeIv.setImageResource(R.mipmap.xia);
                }
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.betting_children_item_cg, null);

            holder.mBettingLvChildOrderNumCg = (TextView) convertView.findViewById(R.id.betting_lv_child_order_num_cg);
            holder.mBettingLvChildOrderTimeCg = (TextView) convertView.findViewById(R.id.betting_lv_child_order_time_cg);
            holder.mBettingMatchLy = (LinearLayout) convertView.findViewById(R.id.betting_match_info_ly);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mBettingMatchLy.removeAllViews();
        BettingTBean.PayDetailsEntity detailsEntity = data.get(groupPosition).payDetails.get(childPosition);
        if (detailsEntity != null) {
            List<BettingTBean.PayDetailsEntity.InfoEntity> info = detailsEntity.info;
            if (info != null) {
                int cCount = detailsEntity.cCount;
                holder.mBettingLvChildOrderNumCg.setText(info.get(0).orderID);
                holder.mBettingLvChildOrderTimeCg.setText(data.get(groupPosition).payDetails.get(0).info.get(0).createDate);

                for (int i = 0; i < 8; i++) {
                    if (i < cCount) {
                        BettingTBean.PayDetailsEntity.InfoEntity infoEntity = info.get(i);
                        mView = View.inflate(context, R.layout.betting_children_match_single, null);
                        TextView mBettingLvChildOrderTeamCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_order_team_cg1);
                        TextView mBettingLvChildDoubleTeamCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_double_team_cg1);
                        TextView mBettingLvChildOddsCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_odds_cg1);
                        TextView mBettingLvChildOrderTypeCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_order_type_cg1);
                        TextView mBettingLvChildWaitOpenCg1 = (TextView) mView.findViewById(R.id.betting_lv_child_wait_open_cg1);

                        switch (infoEntity.matchType) {
                            /**
                             * 比赛类型 1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
                             * 7进球数区间,8全场比分主胜9全场比分平10全场比分主负11全场比分其他12半全场赛果
                             * 13全场单双14半场单双
                             */
                            // 1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
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
                        mBettingLvChildOrderTeamCg1.setText(infoEntity.name);
                        String odds = "回报:"+infoEntity.payRate + "倍";
                        mBettingLvChildOddsCg1.setText(odds);
                        //1-未完结 2-赢 3-输 4-平 5-输一半6-赢一半
                        switch (infoEntity.status) {
                            case 1:
                                mBettingLvChildWaitOpenCg1.setText("未完结");
                                mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.blue_t1));
                                String text = infoEntity.home + "  vs  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text);
                                break;
                            case 2:
                                mBettingLvChildWaitOpenCg1.setText("赢");
                                mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.red_dark));
                                String text1 = infoEntity.home + "  " + infoEntity.homeScore + ":" + infoEntity.awayScore + "  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text1);
                                break;
                            case 3:
                                mBettingLvChildWaitOpenCg1.setText("输");
                                mBettingLvChildWaitOpenCg1.setTextColor(context.getResources().getColor(R.color.green_a));
                                String text2 = infoEntity.home + "  " + infoEntity.homeScore + ":" + infoEntity.awayScore + "  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text2);
                                break;
                            case 4:
                                mBettingLvChildWaitOpenCg1.setText("平");
                                String text3 = infoEntity.home + "  " + infoEntity.homeScore + ":" + infoEntity.awayScore + "  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text3);
                                break;
                            case 5:
                                mBettingLvChildWaitOpenCg1.setText("输一半");
                                String text4 = infoEntity.home + "  " + infoEntity.homeScore + ":" + infoEntity.awayScore + "  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text4);
                                break;
                            case 6:
                                mBettingLvChildWaitOpenCg1.setText("赢一半");
                                String text5 = infoEntity.home + "  " + infoEntity.homeScore + ":" + infoEntity.awayScore + "  " + infoEntity.away;
                                mBettingLvChildDoubleTeamCg1.setText(text5);
                                break;
                            default:
                                break;
                        }
                        holder.mBettingMatchLy.addView(mView);
                    }
                }
                if (cCount!=1){
                    mViewHowSerise = View.inflate(context, R.layout.serise_how, null);
                    TextView mBettingPlOddsCg = (TextView) mViewHowSerise.findViewById(R.id.betting_pl_odds_cg);
                    TextView mBettingLvChildAcountCg = (TextView) mViewHowSerise.findViewById(R.id.betting_lv_child_acount_cg);
                    TextView mBettingLvChildExpectedReturnCg = (TextView) mViewHowSerise.findViewById(R.id.betting_lv_child_expected_return_cg);

                    String text1 = "下注金币:"+info.get(0).amount;
                    mBettingLvChildAcountCg.setText(text1);
                    String returnGold = "预计返还:" + info.get(0).profit;
                    mBettingLvChildExpectedReturnCg.setText(returnGold);
                    String serise = cCount + "串1";
                    mBettingPlOddsCg.setText(serise);
                    holder.mBettingMatchLy.addView(mViewHowSerise);
                }else {
                    mViewNoSerise = View.inflate(context, R.layout.no_serise_ly, null);
                    TextView mBettingLvChildacount = (TextView) mViewNoSerise.findViewById(R.id.betting_lv_child_acount_1);
                    TextView mBettingLvChildReturn = (TextView) mViewNoSerise.findViewById(R.id.betting_lv_child_expected_return_1);

                    String text1 = "下注金币:"+info.get(0).amount;
                    mBettingLvChildacount.setText(text1);

                    String returnGold = "预计返还:" + info.get(0).profit;
                    mBettingLvChildReturn.setText(returnGold);

                    holder.mBettingMatchLy.addView(mViewNoSerise);
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        public TextView       mBettingLvItmeGroup;
        public ImageView      mBettingLvItmeIv;
        public TextView       mBettingLvItmeProfit;
        public TextView       mBettingLvChildOrderTimeCg;
        public TextView       mBettingLvChildOrderNumCg;
        public LinearLayout   mBettingMatchLy;
    }
    /*--------------------------------------------------------------------*/
    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
