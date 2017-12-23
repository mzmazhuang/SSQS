package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallHalfCourtBean;
import com.dading.ssqs.bean.ScrollBallFootBallTotalBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/18.
 */

public class HalfCourtItemCell extends RelativeLayout {

    private TextView textView;
    private View view;
    private int beanId;
    private int position;

    private boolean check = false;

    private ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems bean;
    private ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItem item;

    public HalfCourtItemCell(Context context) {
        super(context);

        textView = new TextView(context);
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        addView(textView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        view = new View(context);
        view.setBackgroundColor(0xFFE7E7E7);
        view.setVisibility(View.GONE);
        addView(view, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));
    }

    public void isShowLine(boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setData(ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems bean, ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItem item, int position, List<ScrollBallHalfCourtFragment.MergeBean> focusList) {
        this.beanId = bean.getId();
        this.position = position;
        this.bean = bean;
        this.item = item;

        textView.setText(item.getStr());

        check = false;

        if (item.getId() > 0) {
            textView.setBackgroundColor(0xFFFFFADC);
            textView.setTextColor(0xFFFF9600);
        }

        if (focusList != null) {
            for (int i = 0; i < focusList.size(); i++) {
                ScrollBallHalfCourtFragment.MergeBean mergeBean = focusList.get(i);

                if (mergeBean.getItems().getId() == bean.getId()) {
                    List<ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItem> beanItems = mergeBean.getBean();

                    for (int j = 0; j < beanItems.size(); j++) {
                        if (beanItems.get(j).getPosition() == position) {
                            check = true;

                            textView.setBackgroundColor(0xFFFF9600);
                            textView.setTextColor(Color.WHITE);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void setListener(final OnItemClickListener listener) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().trim().length() >= 1) {
                    if (item.getId() != 0) {
                        if (listener != null) {

                            if (check) {
                                if (listener.onClick(bean, item, beanId, false, position)) {
                                    check = false;

                                    textView.setBackgroundColor(0xFFFFFADC);
                                    textView.setTextColor(0xFFFF9600);
                                }
                            } else {
                                if (listener.onClick(bean, item, beanId, true, position)) {
                                    check = true;
                                    
                                    textView.setBackgroundColor(0xFFFF9600);
                                    textView.setTextColor(Color.WHITE);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public interface OnItemClickListener {
        boolean onClick(ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItems items, ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItem bean, int id, boolean isAdd, int position);
    }
}
