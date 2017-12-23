package com.dading.ssqs.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.JCbean;

import java.util.List;

/**
 * Created by lenovo on 2017/9/22.
 */
public class HotMatchInfoAdapter extends BaseQuickAdapter<JCbean> {

    private final Context context;

    public HotMatchInfoAdapter (int hot_match_info_item, List<JCbean> data, Context context) {
        super(hot_match_info_item, data);
        this.context = context;
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder, JCbean d) {
        baseViewHolder.setText(R.id.match_info_item_title, d.payTypeName);
        //主队
        baseViewHolder.setText(R.id.item_son_left_name, d.home);
        baseViewHolder.setText(R.id.item_son_left_num, d.realRate1);
        //平局
        baseViewHolder.setText(R.id.item_son_mid_name, context.getString(R.string.draw_result));
        baseViewHolder.setText(R.id.item_son_mid_num, d.realRate2);
        //客队
        baseViewHolder.setText(R.id.item_son_right_name, d.away);
        baseViewHolder.setText(R.id.item_son_right_num, d.realRate3);

        switch (d.payTypeName) {
            case "全场赛果":
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.guess_ninty_result));
                break;
            case "当前让球":
                baseViewHolder.setVisible(R.id.item_son_left_mid, true)
                        .setVisible(R.id.item_son_right_mid, true);
                baseViewHolder.setVisible(R.id.item_son_mid, false);
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.ninty_lostball));
                setLost(baseViewHolder, d);
                break;
            case "半场让球":
                baseViewHolder.setVisible(R.id.item_son_left_mid, true)
                        .setVisible(R.id.item_son_right_mid, true);
                baseViewHolder.setVisible(R.id.item_son_mid, false);
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.guess_half_match_lost));
                setLost(baseViewHolder, d);
                break;
            case "全场大小":
                setSB(baseViewHolder, d);
                baseViewHolder.setVisible(R.id.item_son_mid, false);
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.ninty_all_balls));
                break;
            case "半场赛果":
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.guess_half_result));
                break;
            case "半场大小":
                setSB(baseViewHolder, d);
                baseViewHolder.setVisible(R.id.item_son_mid, false);
                baseViewHolder.setText(R.id.match_info_item_subscribe, context.getString(R.string.half_samll_big_score_num));
                break;
        }
    }

    private void setSB (BaseViewHolder baseViewHolder, JCbean d) {
        String highBs = "高于" + d.realRate2;
        String lowBs = "低于" + d.realRate2;
        baseViewHolder.setText(R.id.item_son_left_name, highBs);
        baseViewHolder.setText(R.id.item_son_right_name,lowBs);
        baseViewHolder.setText(R.id.item_son_left_num, d.realRate1);
        baseViewHolder.setText(R.id.item_son_right_num,d.realRate3);
    }

    private void setLost (BaseViewHolder baseViewHolder, JCbean d) {
        if (d.realRate2.contains("/")) {
            String[] split = d.realRate2.split("/");
            if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                if (Double.valueOf(split[0]) > 0) {
                    String s = "+" + split[0];
                    baseViewHolder.setText(R.id.item_son_left_mid, s);
                } else {
                    baseViewHolder.setText(R.id.item_son_left_mid, split[0]);
                }
                if (Double.valueOf(split[1]) > 0) {
                    String s = "+" + split[1];
                    baseViewHolder.setText(R.id.item_son_right_mid, s);
                } else {
                    baseViewHolder.setText(R.id.item_son_right_mid, split[1]);
                }
            } else {
                baseViewHolder.setText(R.id.item_son_left_mid, d.realRate2);
                baseViewHolder.setText(R.id.item_son_right_mid, d.realRate2);
            }
        } else {
            if (Double.valueOf(d.realRate2) > 0) {
                String s = "+" + d.realRate2;
                baseViewHolder.setText(R.id.item_son_left_mid, s);
                String s1 = "-" + d.realRate2;
                baseViewHolder.setText(R.id.item_son_right_mid, s1);
            } else {
                if (Double.valueOf(d.realRate2) == 0) {
                    String s = "" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.setText(R.id.item_son_left_mid, s);
                    String s1 = "" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.setText(R.id.item_son_right_mid, s1);
                } else {
                    baseViewHolder.setText(R.id.item_son_left_mid,d.realRate2);
                    String text = "+" + Math.abs(Double.valueOf(d.realRate2));
                    baseViewHolder.setText(R.id.item_son_right_mid,text);
                }
            }
        }
    }
}
