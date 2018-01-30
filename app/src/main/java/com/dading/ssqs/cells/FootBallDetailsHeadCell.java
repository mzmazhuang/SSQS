package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2018/1/27.
 */

public class FootBallDetailsHeadCell extends LinearLayout {

    private TextView tvTitle;
    private TextView sectionView;
    private GuessFilterCell.OnRefreshListener refreshListener;

    private int second;//传递过来的时间
    private int currSecond;//用于计算的
    private boolean isStart = false;

    private ImageView pointImageView;
    private AlphaAnimation mAlphaAnim;

    //主队
    private TextView homeTeamTitle;
    private TextView homeTeamAddTimeView;
    private TextView homeTeamFirstHalfView;
    private TextView homeTeamSecondHalfView;
    private TextView homeTeamTotalView;
    //客队
    private TextView visitingTeamTitle;
    private TextView visitingTeamAddTimeView;
    private TextView visitingTeamFirstHalfView;
    private TextView visitingTeamSecondHalfView;
    private TextView visitingTeamTotalView;
    private TextView refreshTextView;

    public FootBallDetailsHeadCell(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setBackgroundColor(0xFF841F00);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        tvTitle = new TextView(context);
        tvTitle.setTextSize(13);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvTitle.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, 0, 0);
        topLayout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        refreshTextView = new TextView(context);
        refreshTextView.setGravity(Gravity.CENTER_VERTICAL);
        refreshTextView.setPadding(AndroidUtilities.INSTANCE.dp(3), AndroidUtilities.INSTANCE.dp(3), AndroidUtilities.INSTANCE.dp(3), AndroidUtilities.INSTANCE.dp(3));
        refreshTextView.setTextSize(12);
        refreshTextView.setTextColor(0xFFFFF000);
        refreshTextView.setBackgroundResource(R.drawable.bg_basketball_details_refresh);
        refreshTextView.setCompoundDrawablePadding(AndroidUtilities.INSTANCE.dp(5));
        refreshTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_refresh_white, 0, 0, 0);
        refreshTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                destoryRunnable(true);
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
            }
        });
        RelativeLayout.LayoutParams refreshLP = LayoutHelper.createRelative(40, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0);
        refreshLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        refreshLP.addRule(RelativeLayout.CENTER_VERTICAL);
        topLayout.addView(refreshTextView, refreshLP);

        LinearLayout backgroundLayout = new LinearLayout(context);
        backgroundLayout.setBackgroundResource(R.mipmap.ic_foot_bg);
        addView(backgroundLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 94));

        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setPadding(0, AndroidUtilities.INSTANCE.dp(15), 0, AndroidUtilities.INSTANCE.dp(15));
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        backgroundLayout.addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setBackgroundColor(0xFF330C00);
        titleLayout.setGravity(Gravity.CENTER_VERTICAL);
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 20, 7, 0, 7, 0));

        LinearLayout leftTitleLayout = new LinearLayout(context);
        leftTitleLayout.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        leftTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.addView(leftTitleLayout, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        sectionView = new TextView(context);
        sectionView.setTextSize(11);
        sectionView.setTextColor(0xFFFFF000);
        leftTitleLayout.addView(sectionView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        pointImageView = new ImageView(context);
        pointImageView.setImageResource(R.mipmap.ic_score_point);
        leftTitleLayout.addView(pointImageView, LayoutHelper.createLinear(1, 4, 2, 0, 0, 0));

        mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnim.setDuration(500);
        mAlphaAnim.setRepeatCount(Animation.INFINITE);

        TextView firstHalfView = new TextView(context);
        firstHalfView.setTextColor(Color.WHITE);
        firstHalfView.setTextSize(11);
        firstHalfView.setText(LocaleController.getString(R.string.first_half));
        firstHalfView.setGravity(Gravity.CENTER);
        titleLayout.addView(firstHalfView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        TextView secondHalfView = new TextView(context);
        secondHalfView.setTextColor(Color.WHITE);
        secondHalfView.setTextSize(11);
        secondHalfView.setText(LocaleController.getString(R.string.second_half));
        secondHalfView.setGravity(Gravity.CENTER);
        titleLayout.addView(secondHalfView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        TextView addTimeView = new TextView(context);
        addTimeView.setTextColor(Color.WHITE);
        addTimeView.setTextSize(11);
        addTimeView.setText(LocaleController.getString(R.string.add_time));
        addTimeView.setGravity(Gravity.CENTER);
        titleLayout.addView(addTimeView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        TextView totalView = new TextView(context);
        totalView.setTextColor(Color.WHITE);
        totalView.setTextSize(11);
        totalView.setText(LocaleController.getString(R.string.basket_total));
        totalView.setGravity(Gravity.CENTER);
        titleLayout.addView(totalView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        LinearLayout homeTeamLayout = new LinearLayout(context);
        homeTeamLayout.setOrientation(LinearLayout.HORIZONTAL);
        homeTeamLayout.setGravity(Gravity.CENTER_VERTICAL);
        homeTeamLayout.setBackgroundColor(0x7FA25E4A);
        contentLayout.addView(homeTeamLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 22, 7, 0, 7, 0));

        homeTeamTitle = new TextView(context);
        homeTeamTitle.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        homeTeamTitle.setTextSize(12);
        homeTeamTitle.setTextColor(Color.WHITE);
        homeTeamTitle.setSingleLine();
        homeTeamTitle.setEllipsize(TextUtils.TruncateAt.END);
        homeTeamTitle.setGravity(Gravity.CENTER_VERTICAL);
        homeTeamLayout.addView(homeTeamTitle, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamFirstHalfView = new TextView(context);
        homeTeamFirstHalfView.setTextSize(12);
        homeTeamFirstHalfView.setTextColor(0xFFFFF000);
        homeTeamFirstHalfView.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamFirstHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        homeTeamSecondHalfView = new TextView(context);
        homeTeamSecondHalfView.setTextSize(12);
        homeTeamSecondHalfView.setTextColor(Color.WHITE);
        homeTeamSecondHalfView.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamSecondHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        homeTeamAddTimeView = new TextView(context);
        homeTeamAddTimeView.setTextSize(12);
        homeTeamAddTimeView.setTextColor(Color.WHITE);
        homeTeamAddTimeView.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamAddTimeView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        homeTeamTotalView = new TextView(context);
        homeTeamTotalView.setTextSize(12);
        homeTeamTotalView.setTextColor(0xFFFFF000);
        homeTeamTotalView.setGravity(Gravity.CENTER);
        homeTeamTotalView.setBackgroundColor(0x7FA8877F);
        homeTeamLayout.addView(homeTeamTotalView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        LinearLayout visitingTeamLayout = new LinearLayout(context);
        visitingTeamLayout.setOrientation(LinearLayout.HORIZONTAL);
        visitingTeamLayout.setGravity(Gravity.CENTER_VERTICAL);
        visitingTeamLayout.setBackgroundColor(0x7FA25E4A);
        contentLayout.addView(visitingTeamLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 22, 7, 0, 7, 0));

        visitingTeamTitle = new TextView(context);
        visitingTeamTitle.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        visitingTeamTitle.setTextSize(12);
        visitingTeamTitle.setTextColor(Color.WHITE);
        visitingTeamTitle.setSingleLine();
        visitingTeamTitle.setEllipsize(TextUtils.TruncateAt.END);
        visitingTeamTitle.setGravity(Gravity.CENTER_VERTICAL);
        visitingTeamLayout.addView(visitingTeamTitle, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamFirstHalfView = new TextView(context);
        visitingTeamFirstHalfView.setTextSize(12);
        visitingTeamFirstHalfView.setTextColor(0xFFFFF000);
        visitingTeamFirstHalfView.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamFirstHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        visitingTeamSecondHalfView = new TextView(context);
        visitingTeamSecondHalfView.setTextSize(12);
        visitingTeamSecondHalfView.setTextColor(Color.WHITE);
        visitingTeamSecondHalfView.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamSecondHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        visitingTeamAddTimeView = new TextView(context);
        visitingTeamAddTimeView.setTextSize(12);
        visitingTeamAddTimeView.setTextColor(Color.WHITE);
        visitingTeamAddTimeView.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamAddTimeView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        visitingTeamTotalView = new TextView(context);
        visitingTeamTotalView.setTextSize(12);
        visitingTeamTotalView.setTextColor(0xFFFFF000);
        visitingTeamTotalView.setGravity(Gravity.CENTER);
        visitingTeamTotalView.setBackgroundColor(0x7FA8877F);
        visitingTeamLayout.addView(visitingTeamTotalView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));
    }

    public void setListener(GuessFilterCell.OnRefreshListener listener) {
        refreshListener = listener;
    }

    public void setRefreshCount(int count) {
        this.second = count;
        currSecond = second;

        refreshTextView.setText(count + "");
    }

    public void beginRunnable() {
        isStart = true;
        destoryRunnable(true);
        SSQSApplication.getHandler().postDelayed(runnable, 1000);
    }

    public boolean getStartStatus() {
        return isStart;
    }

    public void destoryRunnable(boolean isReset) {
        if (isReset) {
            currSecond = second;

            refreshTextView.setText(currSecond + "");
        }

        SSQSApplication.getHandler().removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currSecond > 1) {
                currSecond--;
                refreshTextView.setText(currSecond + "");
                SSQSApplication.getHandler().postDelayed(this, 1000);
            } else {
                refreshTextView.setText("0");

                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
                currSecond = second;
            }
        }
    };

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    public void setSection(String section) {
        sectionView.setText(section);

        pointImageView.setAnimation(mAlphaAnim);
        mAlphaAnim.start();
    }

    public void setHomeTeamInfo(String title, String addTime, String firstHalf, String secondHalf, String total) {
        homeTeamTitle.setText(title);
        homeTeamAddTimeView.setText(addTime);
        homeTeamFirstHalfView.setText(firstHalf);
        homeTeamSecondHalfView.setText(secondHalf);
        homeTeamTotalView.setText(total);
    }

    public void setvisitingTeamInfo(String title, String addTime, String firstHalf, String secondHalf, String total) {
        visitingTeamTitle.setText(title);
        visitingTeamAddTimeView.setText(addTime);
        visitingTeamFirstHalfView.setText(firstHalf);
        visitingTeamSecondHalfView.setText(secondHalf);
        visitingTeamTotalView.setText(total);
    }

}