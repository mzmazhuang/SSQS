package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
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
 * Created by mazhuang on 2017/11/30.
 */

public class GuessFilterCell extends RelativeLayout {

    private TextView tvTotalPage;
    private TextView tvCurrPage;
    private LinearLayout leftLayout;
    private TextView selectTextView;
    private LinearLayout selectLayout;
    private LinearLayout refreshLayout;

    private LinearLayout timeFilterLayout;
    private TextView timeTextView;
    private TextView refreshTextView;

    private int second;//传递过来的时间
    private int currSecond;//用于计算的
    private boolean isStart = false;

    public GuessFilterCell(Context context) {
        super(context);

        setBackgroundColor(0xFF007DB0);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        leftLayout = new LinearLayout(context);
        leftLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(leftLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        tvTotalPage = new TextView(context);
        tvTotalPage.setTextSize(13);
        tvTotalPage.setTextColor(Color.WHITE);
        leftLayout.addView(tvTotalPage, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout currPageLayout = new LinearLayout(context);
        currPageLayout.setGravity(Gravity.CENTER_VERTICAL);
        currPageLayout.setBackgroundColor(Color.WHITE);
        currPageLayout.setOrientation(LinearLayout.HORIZONTAL);
        leftLayout.addView(currPageLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 0, 0));

        tvCurrPage = new TextView(context);
        tvCurrPage.setTextColor(0xFF222222);
        tvCurrPage.setTextSize(13);
        tvCurrPage.setPadding(AndroidUtilities.dp(10), AndroidUtilities.dp(1), AndroidUtilities.dp(5), AndroidUtilities.dp(1));
        currPageLayout.addView(tvCurrPage, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        ImageView triangleView = new ImageView(context);
        triangleView.setImageResource(R.mipmap.ic_orange_triangle);
        currPageLayout.addView(triangleView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 5, 0));

        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams rightLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0);
        rightLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightLP.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(rightLayout, rightLP);

        LinearLayout collectionLayout = new LinearLayout(context);
        collectionLayout.setBackgroundResource(R.drawable.bg_guess_filter);
        rightLayout.addView(collectionLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

//        ImageView collectionImage = new ImageView(context);
//        collectionImage.setImageResource(R.mipmap.ic_star_white);
//        collectionLayout.addView(collectionImage, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 5, 5, 5));

        timeFilterLayout = new LinearLayout(context);
        timeFilterLayout.setOrientation(LinearLayout.HORIZONTAL);
        timeFilterLayout.setBackgroundResource(R.drawable.bg_guess_filter);
        timeFilterLayout.setGravity(Gravity.CENTER_VERTICAL);
        rightLayout.addView(timeFilterLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 27, 6, 0, 0, 0));

        timeTextView = new TextView(context);
        timeTextView.setPadding(AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3));
        timeTextView.setTextSize(12);
        timeTextView.setTextColor(Color.WHITE);
        timeTextView.setText(LocaleController.getString(R.string.guess_time));
        timeFilterLayout.addView(timeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        ImageView triangleYellowView = new ImageView(context);
        triangleYellowView.setImageResource(R.mipmap.ic_orange_yellow_triangle);
        timeFilterLayout.addView(triangleYellowView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 5, 0));

        selectLayout = new LinearLayout(context);
        selectLayout.setGravity(Gravity.CENTER_VERTICAL);
        selectLayout.setBackgroundResource(R.drawable.bg_guess_filter);
        rightLayout.addView(selectLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 27, 6, 0, 0, 0));

        selectTextView = new TextView(context);
        selectTextView.setPadding(AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3));
        selectTextView.setTextSize(12);
        selectTextView.setTextColor(Color.WHITE);
        selectTextView.setText(LocaleController.getString(R.string.select_all));
        selectLayout.addView(selectTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        refreshLayout = new LinearLayout(context);
        refreshLayout.setGravity(Gravity.CENTER);
        refreshLayout.setBackgroundResource(R.drawable.bg_guess_filter);
        refreshLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                destoryRunnable(true);
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
            }
        });
        rightLayout.addView(refreshLayout, LayoutHelper.createLinear(50, 27, 6, 0, 0, 0));

        refreshTextView = new TextView(context);
        refreshTextView.setGravity(Gravity.CENTER_VERTICAL);
        refreshTextView.setPadding(AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3), AndroidUtilities.dp(3));
        refreshTextView.setTextSize(12);
        refreshTextView.setTextColor(0xFFFFF000);
        refreshTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        refreshTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_refresh_white, 0, 0, 0);
        refreshLayout.addView(refreshTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public void setSecondRefresh(int second) {
        this.second = second;
        currSecond = second;
        refreshTextView.setText(second + "");
    }

    public void beginRunnable(boolean isReset) {
        isStart = true;
        destoryRunnable(isReset);
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

    public void setPageClickListener(final OnClickListener listener) {
        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    public void hideSelectMatch() {
        selectLayout.setVisibility(View.GONE);
    }

    public void setSelectText(String text) {
        selectTextView.setText(text);
    }

    public void setSelectClickListener(final OnClickListener listener) {
        selectLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    public void setFilterClickListener(final OnClickListener listener) {
        timeFilterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public void setTimeText(String text) {
        timeTextView.setText(text);
    }

    public void setTotalPage(int page) {
        tvTotalPage.setText(LocaleController.getString(R.string.total_page, page));
    }

    public void setCurrPage(int page) {
        tvCurrPage.setText(page + "");
    }

    private OnRefreshListener refreshListener;

    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
