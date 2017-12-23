package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ListAdapter;

import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 14:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScoreMatchAdapterSG extends ScoreMatchAdapter implements ListAdapter {

    public ScoreMatchAdapterSG(Context content, List<ScoreBean> sgData, int i) {
        super(content, sgData, i);
    }

    public ScoreMatchAdapterSG(Context context, int i) {
        super(context, i);
    }

    @Override
    public Intent getIntentS(int id) {
        Intent intent = new Intent(content, MatchInfoActivity.class);
        intent.putExtra(Constent.MATCH_ID, id);
        intent.putExtra(Constent.INTENT_FROM, "SG");
        return intent;
    }

  /*  @Override
    public void setVisiblesOrData(int position) {
        holder.scoreSgMatchRedTwinkle.setVisibility(View.GONE);
        ScoreBean.DataEntity.ItemsEntity entity = mData.get(position);
        if (entity.isOver == 1){
        holder.scoreSgMatchResultTime.setText("完场");
        }else if (entity.isOver  == 2){
        holder.scoreSgMatchResultTime.setText("中断");
        }
    }*/

}
