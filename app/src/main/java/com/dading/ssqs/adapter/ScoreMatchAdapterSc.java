package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;

import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 16:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScoreMatchAdapterSc extends ScoreMatchAdapter {


    public ScoreMatchAdapterSc(Context content, List<ScoreBean> sgData, int i) {
        super(content, sgData, i);
    }

    public ScoreMatchAdapterSc(Context content, int i) {
        super(content, i);
    }

    @Override
    public Intent getIntentS(int id) {
        Intent intent = new Intent(content, MatchInfoActivity.class);
        intent.putExtra(Constent.MATCH_ID, id);
        intent.putExtra(Constent.INTENT_FROM, "SC");
        return intent;
    }

   /* @Override
    public void setVisiblesOrData(int position) {
        holder.scoreSgMatchRedTwinkleIvLy.setVisibility(View.VISIBLE);
        holder.scoreSgMatchResultTime.setVisibility(View.GONE);
        holder.scoreSgMatchHalfScoreMain.setTextColor(Color.WHITE);
        holder.scoreSgMatchHalfScoreSecond.setTextColor(Color.WHITE);
        holder.middle.setTextColor(Color.WHITE);
        holder.left.setTextColor(Color.WHITE);
        holder.right.setTextColor(Color.WHITE);
    }*/
}
