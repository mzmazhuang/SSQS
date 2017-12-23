package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/18.
 */

public class BoDanChildCell extends RelativeLayout {

    private TextView textView;
    private View view;
    private int beanId;
    private int position;

    private boolean check = false;

    private ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems bean;
    private ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem item;

    public BoDanChildCell(Context context) {
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

    public void setTextBackgroundColor(int color) {
        textView.setBackgroundColor(color);
    }

    public void setData(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems bean, ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem item, int position, List<ScrollBallBoDanFragment.MergeBean> focusList) {
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
                ScrollBallBoDanFragment.MergeBean mergeBean = focusList.get(i);

                if (mergeBean.getItems().getId() == bean.getId()) {
                    List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = mergeBean.getBean();

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
        boolean onClick(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems items, ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem bean, int id, boolean isAdd, int position);
    }
}
