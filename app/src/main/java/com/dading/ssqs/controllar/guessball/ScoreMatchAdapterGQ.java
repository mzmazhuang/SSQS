package com.dading.ssqs.controllar.guessball;

import android.content.Context;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.adapter.ScoreMatchAdapter;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 14:23
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScoreMatchAdapterGQ extends ScoreMatchAdapter {

    private static final String TAG = "ScoreMatchAdapterGQ";
    private AlphaAnimation                   mAlphaAnim;

    public ScoreMatchAdapterGQ(Context content, List<ScoreBean> sgData, int type) {
        super(content, sgData, type);
        mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnim.setDuration(500);
        mAlphaAnim.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public Intent getIntentS(int id) {
        Intent intent = new Intent(content, MatchInfoActivity.class);
        intent.putExtra(Constent.MATCH_ID, id);
        intent.putExtra(Constent.INTENT_FROM, "GQ");
        return intent;
    }
}
