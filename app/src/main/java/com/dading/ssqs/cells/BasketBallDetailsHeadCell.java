package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
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

public  class BasketBallDetailsHeadCell extends LinearLayout {

    private TextView tvTitle;
    private TextView sectionView;
    private TextView timeView;
    private GuessFilterCell.OnRefreshListener refreshListener;

    private int second;//传递过来的时间
    private int currSecond;//用于计算的
    private boolean isStart = false;

    //主队
    private TextView homeTeamTitle;
    private TextView homeTeamSection1;
    private TextView homeTeamSection2;
    private TextView homeTeamSection3;
    private TextView homeTeamSection4;
    private TextView homeTeamAddTimeView;
    private TextView homeTeamFirstHalfView;
    private TextView homeTeamSecondHalfView;
    private TextView homeTeamTotalView;
    //客队
    private TextView visitingTeamTitle;
    private TextView visitingTeamSection1;
    private TextView visitingTeamSection2;
    private TextView visitingTeamSection3;
    private TextView visitingTeamSection4;
    private TextView visitingTeamAddTimeView;
    private TextView visitingTeamFirstHalfView;
    private TextView visitingTeamSecondHalfView;
    private TextView visitingTeamTotalView;
    private TextView refreshTextView;

    public BasketBallDetailsHeadCell(Context context) {
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
        backgroundLayout.setBackgroundResource(R.mipmap.ic_basket_bg);
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
        titleLayout.addView(leftTitleLayout, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 6f));

        sectionView = new TextView(context);
        sectionView.setTextSize(11);
        sectionView.setTextColor(0xFFFFF000);
        leftTitleLayout.addView(sectionView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        timeView = new TextView(context);
        timeView.setTextSize(11);
        timeView.setTextColor(Color.WHITE);
        leftTitleLayout.addView(timeView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 0, 0));

        TextView section1 = new TextView(context);
        section1.setTextColor(Color.WHITE);
        section1.setTextSize(11);
        section1.setText("1");
        section1.setGravity(Gravity.CENTER);
        titleLayout.addView(section1, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        TextView section2 = new TextView(context);
        section2.setTextColor(Color.WHITE);
        section2.setTextSize(11);
        section2.setText("2");
        section2.setGravity(Gravity.CENTER);
        titleLayout.addView(section2, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        TextView section3 = new TextView(context);
        section3.setTextColor(Color.WHITE);
        section3.setTextSize(11);
        section3.setText("3");
        section3.setGravity(Gravity.CENTER);
        titleLayout.addView(section3, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        TextView section4 = new TextView(context);
        section4.setTextColor(Color.WHITE);
        section4.setTextSize(11);
        section4.setText("4");
        section4.setGravity(Gravity.CENTER);
        titleLayout.addView(section4, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        TextView addTimeView = new TextView(context);
        addTimeView.setTextColor(Color.WHITE);
        addTimeView.setTextSize(11);
        addTimeView.setText(LocaleController.getString(R.string.add_time));
        addTimeView.setGravity(Gravity.CENTER);
        titleLayout.addView(addTimeView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 2f));

        TextView firstHalfView = new TextView(context);
        firstHalfView.setTextColor(Color.WHITE);
        firstHalfView.setTextSize(11);
        firstHalfView.setText(LocaleController.getString(R.string.first_half));
        firstHalfView.setGravity(Gravity.CENTER);
        titleLayout.addView(firstHalfView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 3f));

        TextView secondHalfView = new TextView(context);
        secondHalfView.setTextColor(Color.WHITE);
        secondHalfView.setTextSize(11);
        secondHalfView.setText(LocaleController.getString(R.string.second_half));
        secondHalfView.setGravity(Gravity.CENTER);
        titleLayout.addView(secondHalfView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 3f));

        TextView totalView = new TextView(context);
        totalView.setTextColor(Color.WHITE);
        totalView.setTextSize(11);
        totalView.setText(LocaleController.getString(R.string.basket_total));
        totalView.setGravity(Gravity.CENTER);
        titleLayout.addView(totalView, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 3f));

        LinearLayout homeTeamLayout = new LinearLayout(context);
        homeTeamLayout.setOrientation(LinearLayout.HORIZONTAL);
        homeTeamLayout.setBackgroundColor(0x7FBE957C);
        homeTeamLayout.setGravity(Gravity.CENTER_VERTICAL);
        contentLayout.addView(homeTeamLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 22, 7, 0, 7, 0));

        homeTeamTitle = new TextView(context);
        homeTeamTitle.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        homeTeamTitle.setTextSize(12);
        homeTeamTitle.setTextColor(Color.WHITE);
        homeTeamTitle.setSingleLine();
        homeTeamTitle.setEllipsize(TextUtils.TruncateAt.END);
        homeTeamTitle.setGravity(Gravity.CENTER_VERTICAL);
        homeTeamLayout.addView(homeTeamTitle, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 6f));

        homeTeamSection1 = new TextView(context);
        homeTeamSection1.setTextSize(12);
        homeTeamSection1.setTextColor(Color.WHITE);
        homeTeamSection1.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamSection1, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamSection2 = new TextView(context);
        homeTeamSection2.setTextSize(12);
        homeTeamSection2.setTextColor(0xFFFFF000);
        homeTeamSection2.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamSection2, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamSection3 = new TextView(context);
        homeTeamSection3.setTextSize(12);
        homeTeamSection3.setTextColor(Color.WHITE);
        homeTeamSection3.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamSection3, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamSection4 = new TextView(context);
        homeTeamSection4.setTextSize(12);
        homeTeamSection4.setTextColor(0xFFFFF000);
        homeTeamSection4.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamSection4, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamAddTimeView = new TextView(context);
        homeTeamAddTimeView.setTextSize(12);
        homeTeamAddTimeView.setTextColor(Color.WHITE);
        homeTeamAddTimeView.setGravity(Gravity.CENTER);
        homeTeamLayout.addView(homeTeamAddTimeView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        homeTeamFirstHalfView = new TextView(context);
        homeTeamFirstHalfView.setTextSize(12);
        homeTeamFirstHalfView.setTextColor(0xFFFFF000);
        homeTeamFirstHalfView.setGravity(Gravity.CENTER);
        homeTeamFirstHalfView.setBackgroundColor(0x7FFFFFFF);
        homeTeamLayout.addView(homeTeamFirstHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        homeTeamSecondHalfView = new TextView(context);
        homeTeamSecondHalfView.setTextSize(12);
        homeTeamSecondHalfView.setTextColor(Color.WHITE);
        homeTeamSecondHalfView.setGravity(Gravity.CENTER);
        homeTeamSecondHalfView.setBackgroundColor(0x7FFFFFFF);
        homeTeamLayout.addView(homeTeamSecondHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        homeTeamTotalView = new TextView(context);
        homeTeamTotalView.setTextSize(12);
        homeTeamTotalView.setTextColor(0xFFFFF000);
        homeTeamTotalView.setGravity(Gravity.CENTER);
        homeTeamTotalView.setBackgroundColor(0x7FFFFFFF);
        homeTeamLayout.addView(homeTeamTotalView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        LinearLayout visitingTeamLayout = new LinearLayout(context);
        visitingTeamLayout.setOrientation(LinearLayout.HORIZONTAL);
        visitingTeamLayout.setBackgroundColor(0x7FBE957C);
        visitingTeamLayout.setGravity(Gravity.CENTER_VERTICAL);
        contentLayout.addView(visitingTeamLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 22, 7, 0, 7, 0));

        visitingTeamTitle = new TextView(context);
        visitingTeamTitle.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        visitingTeamTitle.setTextSize(12);
        visitingTeamTitle.setTextColor(Color.WHITE);
        visitingTeamTitle.setSingleLine();
        visitingTeamTitle.setEllipsize(TextUtils.TruncateAt.END);
        visitingTeamTitle.setGravity(Gravity.CENTER_VERTICAL);
        visitingTeamLayout.addView(visitingTeamTitle, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 6f));

        visitingTeamSection1 = new TextView(context);
        visitingTeamSection1.setTextSize(12);
        visitingTeamSection1.setTextColor(Color.WHITE);
        visitingTeamSection1.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamSection1, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamSection2 = new TextView(context);
        visitingTeamSection2.setTextSize(12);
        visitingTeamSection2.setTextColor(0xFFFFF000);
        visitingTeamSection2.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamSection2, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamSection3 = new TextView(context);
        visitingTeamSection3.setTextSize(12);
        visitingTeamSection3.setTextColor(Color.WHITE);
        visitingTeamSection3.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamSection3, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamSection4 = new TextView(context);
        visitingTeamSection4.setTextSize(12);
        visitingTeamSection4.setTextColor(0xFFFFF000);
        visitingTeamSection4.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamSection4, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamAddTimeView = new TextView(context);
        visitingTeamAddTimeView.setTextSize(12);
        visitingTeamAddTimeView.setTextColor(Color.WHITE);
        visitingTeamAddTimeView.setGravity(Gravity.CENTER);
        visitingTeamLayout.addView(visitingTeamAddTimeView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        visitingTeamFirstHalfView = new TextView(context);
        visitingTeamFirstHalfView.setTextSize(12);
        visitingTeamFirstHalfView.setTextColor(0xFFFFF000);
        visitingTeamFirstHalfView.setGravity(Gravity.CENTER);
        visitingTeamFirstHalfView.setBackgroundColor(0x7FFFFFFF);
        visitingTeamLayout.addView(visitingTeamFirstHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        visitingTeamSecondHalfView = new TextView(context);
        visitingTeamSecondHalfView.setTextSize(12);
        visitingTeamSecondHalfView.setTextColor(Color.WHITE);
        visitingTeamSecondHalfView.setGravity(Gravity.CENTER);
        visitingTeamSecondHalfView.setBackgroundColor(0x7FFFFFFF);
        visitingTeamLayout.addView(visitingTeamSecondHalfView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        visitingTeamTotalView = new TextView(context);
        visitingTeamTotalView.setTextSize(12);
        visitingTeamTotalView.setTextColor(0xFFFFF000);
        visitingTeamTotalView.setGravity(Gravity.CENTER);
        visitingTeamTotalView.setBackgroundColor(0x7FFFFFFF);
        visitingTeamLayout.addView(visitingTeamTotalView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));
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
    }

    public void setTime(String time) {
        timeView.setText(time);
    }

    public void setHomeTeamInfo(String title, String section1, String section2, String section3, String section4, String addTime, String firstHalf, String secondHalf, String total) {
        homeTeamTitle.setText(title);
        homeTeamSection1.setText(section1);
        homeTeamSection2.setText(section2);
        homeTeamSection3.setText(section3);
        homeTeamSection4.setText(section4);
        homeTeamAddTimeView.setText(addTime);
        homeTeamFirstHalfView.setText(firstHalf);
        homeTeamSecondHalfView.setText(secondHalf);
        homeTeamTotalView.setText(total);
    }

    public void setvisitingTeamInfo(String title, String section1, String section2, String section3, String section4, String addTime, String firstHalf, String secondHalf, String total) {
        visitingTeamTitle.setText(title);
        visitingTeamSection1.setText(section1);
        visitingTeamSection2.setText(section2);
        visitingTeamSection3.setText(section3);
        visitingTeamSection4.setText(section4);
        visitingTeamAddTimeView.setText(addTime);
        visitingTeamFirstHalfView.setText(firstHalf);
        visitingTeamSecondHalfView.setText(secondHalf);
        visitingTeamTotalView.setText(total);
    }

}