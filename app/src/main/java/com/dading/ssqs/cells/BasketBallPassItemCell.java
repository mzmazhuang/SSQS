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
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 * 综合过关下item
 */

public class BasketBallPassItemCell extends LinearLayout {

    private TextView timeTextView;
    private TextView titleTextView;
    private LinearLayout tableLayout;

    private Context mContext;

    private String[] titles = new String[]{LocaleController.getString(R.string.scroll_title16), LocaleController.getString(R.string.scroll_title8), LocaleController.getString(R.string.scroll_title9), LocaleController.getString(R.string.scroll_title17)};

    private BasketScrollBallItemAdapter.OnItemClickListener listener;
    private List<ScrollBallBasketBallDefaultFragment.MergeBean> focusList;
    private List<BasketScrollBallItemChildCell> cells = new ArrayList<>();
    private int beanId;

    public BasketBallPassItemCell(Context context) {
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

        titleTextView = new TextView(context);
        titleTextView.setPadding(AndroidUtilities.dp(75), 0, AndroidUtilities.dp(12), 0);
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(0xFF626262);
        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextView.setSingleLine();
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setGravity(Gravity.CENTER);
        topLayout.addView(titleTextView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        tableLayout = new LinearLayout(context);
        tableLayout.setOrientation(LinearLayout.VERTICAL);
        addView(tableLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout titlesLayout = new LinearLayout(mContext);
        titlesLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(titlesLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        for (int i = 0; i < titles.length; i++) {
            TextView titleView = new TextView(mContext);
            titleView.setTextSize(12);
            titleView.setTextColor(Color.WHITE);
            titleView.setText(titles[i]);
            titleView.setGravity(Gravity.CENTER);
            titleView.setBackgroundColor(0xFF009BDB);

            float weight;
            if (i == titles.length - 1) {
                weight = 2;
            } else {
                weight = 1;
            }

            titlesLayout.addView(titleView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, weight));

            if (i != titles.length - 1) {
                View view = new View(mContext);
                view.setBackgroundColor(0xFFE7E7E7);
                titlesLayout.addView(view, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));
            }
        }

        for (int i = 0; i < 2; i++) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setBackgroundColor(0xFFfffadc);
            tableLayout.addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            for (int j = 0; j < 5; j++) {
                BasketScrollBallItemChildCell cell = new BasketScrollBallItemChildCell(mContext);
                layout.addView(cell, LayoutHelper.createLinear(0, 30, 1f));
                cells.add(cell);
            }
        }

        View view = new View(context);
        view.setBackgroundColor(0xFFE7E7E7);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setTime(String time) {
        timeTextView.setText(time);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public void setBeanId(int id) {
        this.beanId = id;
    }

    public void setListener(BasketScrollBallItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(ScrollBallBasketBallBean.ScrollBaksetBallItems bean) {
        if (bean.getTestItems() != null && bean.getTestItems().size() == cells.size()) {
            for (int i = 0; i < cells.size(); i++) {
                BasketScrollBallItemChildCell cell = cells.get(i);
                cell.setOnClickListener(listener);
                cell.setData(bean.getTestItems().get(i), bean, beanId, focusList, i);
            }
        }
    }
}
