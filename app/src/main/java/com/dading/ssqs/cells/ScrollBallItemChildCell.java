package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.ScrollBallItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class ScrollBallItemChildCell extends LinearLayout {

    private TextView tvLeftText;
    private TextView tvRightText;

    private boolean check;
    private int currStyle;

    private ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem bean;//点击的item
    private ScrollBallFootBallBean.ScrollBeanItems items;//比赛信息
    private int beanId;//联赛id
    private int position;//根据position来计算出当前点击的是主场还是客场

    public ScrollBallItemChildCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));


        tvLeftText = new TextView(context);
        tvLeftText.setTextSize(12);
        tvLeftText.setVisibility(View.GONE);
        tvLeftText.setGravity(Gravity.CENTER);
        addView(tvLeftText, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        tvRightText = new TextView(context);
        tvRightText.setTextColor(0xFFFF9600);
        tvRightText.setTextSize(12);
        tvRightText.setVisibility(View.GONE);
        tvRightText.setGravity(Gravity.CENTER);
        addView(tvRightText, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
    }

    public void setOnClickListener(final ScrollBallItemAdapter.OnItemClickListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvLeftText.getVisibility() != View.GONE || tvRightText.getVisibility() != View.GONE) {

                    if (listener != null) {
                        boolean isHome = bean.getSelected() != 2;

                        if (check) {

                            if (listener.onItemClick(beanId, bean, items, false, isHome, position)) {
                                check = false;

                                if (currStyle == 1) {
                                    setBackgroundResource(R.drawable.bg_guess_ball_border);
                                } else if (currStyle == 2) {
                                    setBackgroundResource(R.drawable.bg_guess_ball_border_orange);
                                }
                                tvLeftText.setTextColor(0xFF222222);
                                tvRightText.setTextColor(0xFFFF9600);
                            }
                        } else {

                            if (listener.onItemClick(beanId, bean, items, true, isHome, position)) {
                                check = true;

                                setBackgroundResource(R.drawable.bg_guess_ball_border_pressed);
                                tvRightText.setTextColor(Color.WHITE);
                                tvLeftText.setTextColor(Color.WHITE);
                            }
                        }
                    }
                }
            }
        });
    }

    public void setData(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem item, ScrollBallFootBallBean.ScrollBeanItems items, int id, List<ScrollBallDefaultFragment.MergeBean> focusList, int position) {
        this.bean = item;
        this.items = items;
        this.beanId = id;
        this.position = position;

        if (item.getBackground() == 0xFFFFFFFF) {
            setBackgroundResource(R.drawable.bg_guess_ball_border);
            currStyle = 1;
        } else {
            setBackgroundResource(R.drawable.bg_guess_ball_border_orange);
            currStyle = 2;
        }

        if (!TextUtils.isEmpty(item.getLeftStr())) {
            tvLeftText.setVisibility(View.VISIBLE);
            if (!item.getLeftStr().equals("null")) {
                if (item.getLeftStr().contains("font")) {
                    tvLeftText.setText(Html.fromHtml(item.getLeftStr()));
                } else {
                    tvLeftText.setTextColor(0xFF222222);
                    tvLeftText.setText(item.getLeftStr());
                }
            }
        }

        if (!TextUtils.isEmpty(item.getRightStr())) {
            tvRightText.setVisibility(View.VISIBLE);
            tvRightText.setTextColor(0xFFFF9600);
            tvRightText.setText(item.getRightStr());
        }

        check = false;

        if (focusList != null) {
            for (int i = 0; i < focusList.size(); i++) {
                ScrollBallDefaultFragment.MergeBean bean = focusList.get(i);

                if (bean.getItems().getId() == items.getId()) {
                    List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = bean.getBean();

                    for (int j = 0; j < beanItems.size(); j++) {
                        if (beanItems.get(j).getPosition() == position) {
                            check = true;

                            setBackgroundResource(R.drawable.bg_guess_ball_border_pressed);
                            tvRightText.setTextColor(Color.WHITE);
                            tvLeftText.setTextColor(Color.WHITE);

                            return;
                        }
                    }
                }
            }
        }
    }
}
