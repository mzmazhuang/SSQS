package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class BasketScrollBallItemCell extends LinearLayout {

    private TextView timeTextView;
    private TextView titleTextView;

    private TextView title1;
    private TextView title2;
    private TextView title3;
    private TextView title4;
    private TextView title5;

    private LinearLayout infoLayout;
    private TextView title6;
    private TextView title7;

    private View lineView;

    private List<ScrollBallBasketBallDefaultFragment.MergeBean> focusList;

    private List<BasketScrollBallItemChildCell> cells = new ArrayList<>();

    private BasketScrollBallItemAdapter.OnItemClickListener listener;

    private int beanId;

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public void setListener(BasketScrollBallItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public BasketScrollBallItemCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        //时间和标题
        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        timeTextView = new TextView(context);
        timeTextView.setTextSize(10);
        timeTextView.setTextColor(0xFFBDBDBD);
        topLayout.addView(timeTextView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 17, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        titleTextView = new TextView(context);
        titleTextView.setPadding(AndroidUtilities.dp(75), 0, AndroidUtilities.dp(75), 0);
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(0xFF626262);
        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextView.setSingleLine();
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setGravity(Gravity.CENTER);
        topLayout.addView(titleTextView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        //一级标题 如   场次/胜平负/让球/大小/单双
        LinearLayout titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 42));

        LinearLayout title1Layout = new LinearLayout(context);
        title1Layout.setBackgroundColor(0xFF009BDB);
        title1Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title1Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        title1 = new TextView(context);
        title1.setTextColor(Color.WHITE);
        title1.setTextSize(12);
        title1.setText(LocaleController.getString(R.string.scroll_title18));
        title1Layout.addView(title1, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view1 = new View(context);
        view1.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view1, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title2Layout = new LinearLayout(context);
        title2Layout.setBackgroundColor(0xFF009BDB);
        title2Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title2Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        title2 = new TextView(context);
        title2.setTextColor(Color.WHITE);
        title2.setTextSize(12);
        title2.setText(LocaleController.getString(R.string.scroll_title16));
        title2Layout.addView(title2, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view2 = new View(context);
        view2.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view2, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title3Layout = new LinearLayout(context);
        title3Layout.setBackgroundColor(0xFF009BDB);
        title3Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title3Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        title3 = new TextView(context);
        title3.setTextColor(Color.WHITE);
        title3.setTextSize(12);
        title3.setText(LocaleController.getString(R.string.scroll_title8));
        title3Layout.addView(title3, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view3 = new View(context);
        view3.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view3, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title4Layout = new LinearLayout(context);
        title4Layout.setBackgroundColor(0xFF009BDB);
        title4Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title4Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        title4 = new TextView(context);
        title4.setTextColor(Color.WHITE);
        title4.setTextSize(12);
        title4.setText(LocaleController.getString(R.string.scroll_title9));
        title4Layout.addView(title4, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view4 = new View(context);
        view4.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view4, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        title5 = new TextView(context);
        title5.setTextColor(Color.WHITE);
        title5.setTextSize(12);
        title5.setGravity(Gravity.CENTER);
        title5.setBackgroundColor(0xFF009BDB);
        title5.setText("球队得分\n(大/小)");
        titleLayout.addView(title5, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        //中间的内容   全场/半场  数字
        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 120));

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.addView(leftLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        RelativeLayout leftTopLayout = new RelativeLayout(context);
        leftLayout.addView(leftTopLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 61));

        infoLayout = new LinearLayout(context);
        infoLayout.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), 0);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        leftTopLayout.addView(infoLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        title6 = new TextView(context);
        title6.setPadding(0, AndroidUtilities.dp(4), 0, AndroidUtilities.dp(4));
        title6.setTextSize(12);
        title6.setTextColor(0xFFFFD200);
        title6.setBackgroundResource(R.drawable.bg_basket_scroll_title);
        title6.setGravity(Gravity.CENTER);
        infoLayout.addView(title6, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        title7 = new TextView(context);
        title7.setPadding(0, AndroidUtilities.dp(4), 0, AndroidUtilities.dp(4));
        title7.setTextSize(13);
        title7.setGravity(Gravity.CENTER);
        title7.setBackgroundResource(R.drawable.bg_basket_scroll_number);
        infoLayout.addView(title7, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        View view7 = new View(context);
        view7.setBackgroundColor(0xFFE7E7E7);
        leftTopLayout.addView(view7, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1, RelativeLayout.ALIGN_PARENT_BOTTOM));

        View view8 = new View(context);
        view8.setBackgroundColor(0xFFE7E7E7);
        leftTopLayout.addView(view8, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        RelativeLayout leftBottomLayout = new RelativeLayout(context);
        leftLayout.addView(leftBottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 59));

        View view9 = new View(context);
        view9.setBackgroundColor(0xFFE7E7E7);
        leftBottomLayout.addView(view9, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        LinearLayout cellLayout = new LinearLayout(context);
        cellLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.addView(cellLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 4f));

        for (int i = 0; i < 4; i++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            cellLayout.addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
            for (int j = 0; j < 4; j++) {
                BasketScrollBallItemChildCell cell = new BasketScrollBallItemChildCell(context);
                layout.addView(cell, LayoutHelper.createLinear(0, 30, 1f));
                cells.add(cell);
            }
        }

        lineView = new View(context);
        lineView.setBackgroundColor(0xFFE4E4E4);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setInfoClickListener(final OnClickListener listener) {
        infoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    private void setTime(String time) {
        timeTextView.setText(time);
    }

    private void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setBeanId(int id) {
        this.beanId = id;
    }

    private void setItemChild(List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> list, ScrollBallBasketBallBean.ScrollBaksetBallItems items) {
        if (list.size() == cells.size()) {
            for (int i = 0; i < list.size(); i++) {
                BasketScrollBallItemChildCell cell = cells.get(i);
                cell.setOnClickListener(listener);
                cell.setData(list.get(i), items, beanId, focusList, i);
            }
        }
    }

    public void setData(ScrollBallBasketBallBean.ScrollBaksetBallItems bean) {
        setItemChild(bean.getTestItems(), bean);

        setTime(bean.getTime());
        setTitle(bean.getTitle() + "　VS　" + bean.getByTitle());

        title6.setText(bean.getScore().getCurrSchedule());

        String homeColor = "";
        String aColor = "";

        if (!TextUtils.isEmpty(bean.getScore().getLeftScore()) && !TextUtils.isEmpty(bean.getScore().getRightScore())) {
            if (Integer.valueOf(bean.getScore().getLeftScore()) >= Integer.valueOf(bean.getScore().getRightScore())) {
                homeColor = "#E91414";
                aColor = "#80510D";
            } else {
                homeColor = "#80510D";
                aColor = "#E91414";
            }
        }

        title7.setText(Html.fromHtml("<font color=\"" + homeColor + "\">" + bean.getScore().getLeftScore() + "</font><font color=\"#80510D\">-</font><font color=\"" + aColor + "\">" + bean.getScore().getRightScore() + "</font>"));
    }
}
