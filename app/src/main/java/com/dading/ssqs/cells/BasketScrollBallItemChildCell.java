package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class BasketScrollBallItemChildCell extends LinearLayout {

    private TextView tvLeftText;
    private TextView tvRightText;

    private boolean check;

    private ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem bean;
    private ScrollBallBasketBallBean.ScrollBaksetBallItems items;
    private int beanId;//联赛id
    private int position;//根据position来计算出当前点击的是主场还是客场

    public BasketScrollBallItemChildCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        tvLeftText = new TextView(context);
        tvLeftText.setTextSize(12);
        tvLeftText.setVisibility(View.GONE);
        tvLeftText.setGravity(Gravity.CENTER);
        addView(tvLeftText, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        tvRightText = new TextView(context);
        tvRightText.setTextColor(0xFFFF9600);
        tvRightText.setTextSize(12);
        tvRightText.setVisibility(View.GONE);
        tvRightText.setGravity(Gravity.CENTER);
        addView(tvRightText, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
    }

    public void setOnClickListener(final BasketScrollBallItemAdapter.OnItemClickListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvLeftText.getVisibility() != View.GONE || tvRightText.getVisibility() != View.GONE) {

                    if (listener != null) {
                        boolean isHome = bean.getSelected() == 1;

                        if (check) {

                            if (listener.onItemClick(beanId, bean, items, false, isHome, position)) {
                                check = false;

                                setBackgroundResource(R.drawable.bg_guess_ball_border_orange);
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

    public void setData(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem item, ScrollBallBasketBallBean.ScrollBaksetBallItems items, int id, List<ScrollBallBasketBallDefaultFragment.MergeBean> focusList, int position) {
        this.bean = item;
        this.items = items;
        this.beanId = id;
        this.position = position;

        setBackgroundResource(R.drawable.bg_guess_ball_border_orange);

        if (!TextUtils.isEmpty(item.getLeftStr())) {
            tvLeftText.setVisibility(View.VISIBLE);
            if (!item.getLeftStr().equals("null")) {
                if (item.getLeftStr().contains("</font>")) {
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
                ScrollBallBasketBallDefaultFragment.MergeBean bean = focusList.get(i);

                if (bean.getItems().getId() == items.getId()) {
                    List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = bean.getBean();

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
