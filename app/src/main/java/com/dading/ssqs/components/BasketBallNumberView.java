package com.dading.ssqs.components;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2018/1/27.
 */

public class BasketBallNumberView extends LinearLayout {

    private TextView hFitstScoreView;//主队第一节比分
    private TextView aFitstScoreView;//客队第一节比分
    private TextView hTwoScoreView;//客队第二节比分
    private TextView aTwoScoreView;//客队第二节比分
    private TextView hThreeScoreView;//客队第三节比分
    private TextView aThreeScoreView;//客队第三节比分
    private TextView hFourScoreView;//客队第四节比分
    private TextView aFourScoreView;//客队第四节比分
    private TextView hAddScoreView;//客队加时赛比分
    private TextView aAddScoreView;//客队加时赛比分
    private TextView hTotalScoreView;//客队总比分
    private TextView aTotalScoreView;//客队总比分


    public BasketBallNumberView(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 0, 1f));

        hFitstScoreView = new TextView(context);
        hFitstScoreView.setTextSize(12);
        hFitstScoreView.setTextColor(0xFF323232);
        hFitstScoreView.setGravity(Gravity.CENTER_VERTICAL);
        topLayout.addView(hFitstScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        hTwoScoreView = new TextView(context);
        hTwoScoreView.setTextSize(12);
        hTwoScoreView.setTextColor(0xFF323232);
        hTwoScoreView.setGravity(Gravity.CENTER);
        topLayout.addView(hTwoScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        hThreeScoreView = new TextView(context);
        hThreeScoreView.setTextSize(12);
        hThreeScoreView.setTextColor(0xFF323232);
        hThreeScoreView.setGravity(Gravity.CENTER);
        topLayout.addView(hThreeScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        hFourScoreView = new TextView(context);
        hFourScoreView.setTextSize(12);
        hFourScoreView.setTextColor(0xFF323232);
        hFourScoreView.setGravity(Gravity.CENTER);
        topLayout.addView(hFourScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        hAddScoreView = new TextView(context);
        hAddScoreView.setTextSize(12);
        hAddScoreView.setTextColor(0xFF323232);
        hAddScoreView.setGravity(Gravity.CENTER);
        topLayout.addView(hAddScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        hTotalScoreView = new TextView(context);
        hTotalScoreView.setTextSize(12);
        hTotalScoreView.setTextColor(0xFF309F01);
        hTotalScoreView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        topLayout.addView(hTotalScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        LinearLayout bottomLayout = new LinearLayout(context);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(bottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 0, 1f));

        aFitstScoreView = new TextView(context);
        aFitstScoreView.setTextSize(12);
        aFitstScoreView.setTextColor(0xFF323232);
        aFitstScoreView.setGravity(Gravity.CENTER_VERTICAL);
        bottomLayout.addView(aFitstScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        aTwoScoreView = new TextView(context);
        aTwoScoreView.setTextSize(12);
        aTwoScoreView.setTextColor(0xFF323232);
        aTwoScoreView.setGravity(Gravity.CENTER);
        bottomLayout.addView(aTwoScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        aThreeScoreView = new TextView(context);
        aThreeScoreView.setTextSize(12);
        aThreeScoreView.setTextColor(0xFF323232);
        aThreeScoreView.setGravity(Gravity.CENTER);
        bottomLayout.addView(aThreeScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        aFourScoreView = new TextView(context);
        aFourScoreView.setTextSize(12);
        aFourScoreView.setTextColor(0xFF323232);
        aFourScoreView.setGravity(Gravity.CENTER);
        bottomLayout.addView(aFourScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        aAddScoreView = new TextView(context);
        aAddScoreView.setTextSize(12);
        aAddScoreView.setTextColor(0xFF323232);
        aAddScoreView.setGravity(Gravity.CENTER);
        bottomLayout.addView(aAddScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        aTotalScoreView = new TextView(context);
        aTotalScoreView.setTextSize(12);
        aTotalScoreView.setTextColor(0xFF309F01);
        aTotalScoreView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        bottomLayout.addView(aTotalScoreView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
    }

    public void setData(String part1HScore, String part1AScore, String part2HScore, String part2AScore, String part3HScore, String part3AScore, String part4HScore, String part4AScore, String hOverTimeScore, String aOverTimeScore, String homeTotal, String awayTotal) {
        if (!TextUtils.isEmpty(part1HScore)) {
            hFitstScoreView.setText(part1HScore);
        }

        if (!TextUtils.isEmpty(part2HScore)) {
            hTwoScoreView.setText(part2HScore);
        }

        if (!TextUtils.isEmpty(part3HScore)) {
            hThreeScoreView.setText(part3HScore);
        }

        if (!TextUtils.isEmpty(part4HScore)) {
            hFourScoreView.setText(part4HScore);
        }

        if (!TextUtils.isEmpty(hOverTimeScore)) {
            hAddScoreView.setText(hOverTimeScore);
        }

        if (!TextUtils.isEmpty(homeTotal)) {
            hTotalScoreView.setText(homeTotal);
        }

        //客队
        if (!TextUtils.isEmpty(part1AScore)) {
            aFitstScoreView.setText(part1AScore);
        }

        if (!TextUtils.isEmpty(part2AScore)) {
            aTwoScoreView.setText(part2AScore);
        }

        if (!TextUtils.isEmpty(part3AScore)) {
            aThreeScoreView.setText(part3AScore);
        }

        if (!TextUtils.isEmpty(part4AScore)) {
            aFourScoreView.setText(part4AScore);
        }

        if (!TextUtils.isEmpty(aOverTimeScore)) {
            aAddScoreView.setText(aOverTimeScore);
        }

        if (!TextUtils.isEmpty(awayTotal)) {
            aTotalScoreView.setText(awayTotal);
        }
    }
}
