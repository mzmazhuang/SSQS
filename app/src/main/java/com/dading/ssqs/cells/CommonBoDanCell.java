package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/2.
 */

public class CommonBoDanCell extends LinearLayout {

    private TextView timeTextView;
    private TextView titleTextView;

    private Context mContext;
    private LinearLayout titleTextLayout;

    private View view;

    private String[] oneData = new String[]{"球队", "1:0", "2:0", "2:1", "3:0", "3:1", "3:2", "4:0", "4:1", "4:2", "4:3"};
    private String[] fourData = new String[]{"", "0:0", "1:1", "2:2", "3:3", "4:4", "其他比分"};

    private List<BoDanChildCell> twoTextView = new ArrayList<>();
    private List<BoDanChildCell> threeTextView = new ArrayList<>();
    private List<BoDanChildCell> fiveTextView = new ArrayList<>();

    private LinearLayout pointLayout;
    private ImageView pointView;
    private TextView protTimeView;
    private AlphaAnimation mAlphaAnim;

    public CommonBoDanCell(Context context) {
        super(context);
        mContext = context;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        timeTextView = new TextView(context);
        timeTextView.setTextSize(10);
        timeTextView.setTextColor(0xFFBDBDBD);
        topLayout.addView(timeTextView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 17, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        titleTextLayout = new LinearLayout(context);
        titleTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        topLayout.addView(titleTextLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        titleTextView = new TextView(context);
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(0xFF626262);
        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextView.setSingleLine();
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setGravity(Gravity.CENTER_VERTICAL);
        titleTextLayout.addView(titleTextView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        pointLayout = new LinearLayout(context);
        pointLayout.setVisibility(View.GONE);
        pointLayout.setOrientation(LinearLayout.HORIZONTAL);
        pointLayout.setGravity(Gravity.LEFT);
        titleTextLayout.addView(pointLayout, LayoutHelper.createLinear(25, LayoutHelper.WRAP_CONTENT, 2, 6, 0, 0));

        protTimeView = new TextView(context);
        protTimeView.setTextSize(10);
        protTimeView.setTextColor(0xFFFF9600);
        protTimeView.setSingleLine();
        pointLayout.addView(protTimeView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        pointView = new ImageView(context);
        pointView.setScaleType(ImageView.ScaleType.FIT_XY);
        pointView.setImageResource(R.mipmap.ic_guessball_scroll_point);
        pointLayout.addView(pointView, LayoutHelper.createLinear(3, 6));

        LinearLayout tableLayout = new LinearLayout(context);
        tableLayout.setOrientation(LinearLayout.VERTICAL);
        addView(tableLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout oneRowLayout = new LinearLayout(mContext);
        oneRowLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(oneRowLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 42));

        for (int i = 0; i < oneData.length; i++) {
            RelativeLayout layout = new RelativeLayout(mContext);

            float weight;
            if (i == 0) {
                weight = 5;
            } else {
                weight = 2;
            }
            oneRowLayout.addView(layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));

            TextView textView = new TextView(mContext);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(0xFF009BDB);
            textView.setTextColor(Color.WHITE);
            textView.setText(oneData[i]);
            layout.addView(textView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

            if (i != oneData.length - 1) {
                View view = new View(mContext);
                view.setBackgroundColor(0xFFE7E7E7);
                layout.addView(view, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));
            }
        }

        LinearLayout twoRowLayout = new LinearLayout(mContext);
        twoRowLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(twoRowLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        for (int i = 0; i < 11; i++) {
            BoDanChildCell childCell = new BoDanChildCell(mContext);
            float weight;

            if (i == 0) {
                weight = 5;
                childCell.setTextColor(0xFF222222);
                childCell.setTextBackgroundColor(Color.WHITE);
            } else {
                weight = 2;
            }
            twoRowLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));
            twoTextView.add(childCell);

            if (i != 10) {
                childCell.isShowLine(true);
            }
        }

        //横线
        View lineView1 = new View(mContext);
        lineView1.setBackgroundColor(0xFFE7E7E7);
        tableLayout.addView(lineView1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        LinearLayout threeRowLayout = new LinearLayout(mContext);
        threeRowLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(threeRowLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        for (int i = 0; i < 11; i++) {
            BoDanChildCell childCell = new BoDanChildCell(mContext);

            float weight;

            if (i == 0) {
                weight = 5;
                childCell.setTextColor(0xFF222222);
                childCell.setTextBackgroundColor(Color.WHITE);
            } else {
                weight = 2;
            }
            threeRowLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));
            threeTextView.add(childCell);

            if (i != 10) {
                childCell.isShowLine(true);
            }
        }

        LinearLayout fourRowLayout = new LinearLayout(mContext);
        fourRowLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(fourRowLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 42));

        for (int i = 0; i < fourData.length; i++) {
            RelativeLayout layout = new RelativeLayout(mContext);

            float weight;
            if (i == 0) {
                weight = 5;
            } else if (i == fourData.length - 1) {
                weight = 10;
            } else {
                weight = 2;
            }
            fourRowLayout.addView(layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));

            TextView textView = new TextView(mContext);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(0xFF009BDB);
            textView.setTextColor(Color.WHITE);
            textView.setText(fourData[i]);
            layout.addView(textView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

            if (i != fourData.length - 1) {
                View view = new View(mContext);
                view.setBackgroundColor(0xFFE7E7E7);
                layout.addView(view, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));
            }
        }

        LinearLayout fiveRowLayout = new LinearLayout(mContext);
        fiveRowLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(fiveRowLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        for (int i = 0; i < 7; i++) {
            BoDanChildCell childCell = new BoDanChildCell(mContext);

            float weight;
            if (i == 0) {
                weight = 5;
            } else if (i == fourData.length - 1) {
                weight = 10;
            } else {
                weight = 2;
            }
            fiveRowLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));
            fiveTextView.add(childCell);

            if (i != 6) {
                childCell.isShowLine(true);
            }
        }

        view = new View(context);
        view.setBackgroundColor(0xFFE4E4E4);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setTime(String time) {
        timeTextView.setText(time);
    }

    public void setTitle(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems items, int pageType) {

        LinearLayout.LayoutParams layoutParams = (LayoutParams) titleTextView.getLayoutParams();

        if (pageType == 3) {
            titleTextLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(75), 0);

            layoutParams.setMargins(AndroidUtilities.INSTANCE.dp(75f), 0, 0, 0);
        } else {
            titleTextLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(60f), 0);

            layoutParams.setMargins(AndroidUtilities.INSTANCE.dp(60f), 0, 0, 0);
        }

        titleTextView.setLayoutParams(layoutParams);

        if (pageType != 1) {
            titleTextView.setText(items.getTitle() + "　VS　" + items.getByTitle());

            if (mAlphaAnim != null) {
                mAlphaAnim.cancel();
            }
        } else {
            titleTextView.setText(Html.fromHtml("<font color=\"#626262\">" + items.getTitle() + "</font>&nbsp;&nbsp;<font color=\"#1FA605\">" + items.gethScore() + "-" + items.getaScore() + "</font>&nbsp;&nbsp;<font color=\"#626262\">" + items.getByTitle() + "</font>"));

            if (!TextUtils.isEmpty(items.getProtTime())) {
                pointLayout.setVisibility(View.VISIBLE);

                protTimeView.setText(items.getProtTime());

                mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
                mAlphaAnim.setDuration(500);
                mAlphaAnim.setRepeatCount(Animation.INFINITE);
                pointView.setAnimation(mAlphaAnim);

                mAlphaAnim.start();
            }
        }
    }

    //创建波胆样式的表格
    public void setTableRowAndColumn(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems bean, BoDanChildCell.OnItemClickListener listener, List<ScrollBallBoDanFragment.MergeBean> focusList) {
        List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> list = bean.getItemList();

        for (int i = 0; i < list.size(); i++) {
            if (i < twoTextView.size()) {
                twoTextView.get(i).setData(bean, list.get(i), i, focusList);
                twoTextView.get(i).setListener(listener);
            } else if (i < (twoTextView.size() + threeTextView.size())) {
                threeTextView.get((i - twoTextView.size())).setData(bean, list.get(i), i, focusList);
                threeTextView.get((i - twoTextView.size())).setListener(listener);
            } else {
                fiveTextView.get((i - twoTextView.size() - threeTextView.size())).setData(bean, list.get(i), i, focusList);
                fiveTextView.get((i - twoTextView.size() - threeTextView.size())).setListener(listener);
            }
        }
    }
}
