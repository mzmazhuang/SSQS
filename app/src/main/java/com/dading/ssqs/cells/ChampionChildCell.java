package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class ChampionChildCell extends LinearLayout {

    private Context mContext;
    private TextView leftTextView;
    private TextView rightTextView;

    private ChampionBean.ChampionItems.ChampionItem bean;
    private ChampionBean.ChampionItems items;
    private int position;
    private String title;

    private boolean check;

    public ChampionChildCell(Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 25));

        leftTextView = new TextView(mContext);
        leftTextView.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, 0, 0);
        leftTextView.setTextSize(12);
        leftTextView.setTextColor(0xFF222222);
        leftTextView.setSingleLine();
        leftTextView.setGravity(Gravity.CENTER_VERTICAL);
        leftTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(leftTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        rightTextView = new TextView(mContext);
        rightTextView.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(12), 0);
        rightTextView.setTextColor(0xFFFF0000);
        rightTextView.setTextSize(12);
        rightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        addView(rightTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
    }

    public void setData(ChampionBean.ChampionItems.ChampionItem bean, ChampionBean.ChampionItems items, int position, String title, List<ToDayFootBallChampionFragment.MergeBean> focusList) {
        this.bean = bean;
        this.items = items;
        this.position = position;
        this.title = title;

        leftTextView.setText(bean.getLeftStr());
        rightTextView.setText(bean.getRightStr());

        leftTextView.setBackgroundColor(Color.WHITE);
        leftTextView.setTextColor(0xFF222222);
        rightTextView.setBackgroundColor(Color.WHITE);
        rightTextView.setTextColor(0xFFFF0000);

        check = false;

        if (focusList != null) {
            for (int i = 0; i < focusList.size(); i++) {
                ToDayFootBallChampionFragment.MergeBean mergeBean = focusList.get(i);

                if (mergeBean.getItems().getLeagueId() == items.getLeagueId()) {
                    List<ChampionBean.ChampionItems.ChampionItem> beanItems = mergeBean.getBean();

                    for (int j = 0; j < beanItems.size(); j++) {
                        if (beanItems.get(j).getId() == bean.getId()) {
                            if (beanItems.get(j).getPosition() == position) {
                                check = true;

                                leftTextView.setBackgroundColor(0xFFFF9600);
                                leftTextView.setTextColor(Color.WHITE);
                                rightTextView.setBackgroundColor(0xFFFF9600);
                                rightTextView.setTextColor(Color.WHITE);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setListener(final OnClickListener listener) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftTextView.getText().toString().trim().length() >= 1 || rightTextView.getText().toString().trim().length() >= 1) {
                    if (listener != null) {
                        if (check) {
                            if (listener.onClick(bean, items, false, position, title)) {
                                check = false;

                                leftTextView.setBackgroundColor(Color.WHITE);
                                leftTextView.setTextColor(0xFF222222);
                                rightTextView.setBackgroundColor(Color.WHITE);
                                rightTextView.setTextColor(0xFFFF0000);
                            }
                        } else {
                            if (listener.onClick(bean, items, true, position, title)) {
                                check = true;

                                leftTextView.setBackgroundColor(0xFFFF9600);
                                leftTextView.setTextColor(Color.WHITE);
                                rightTextView.setBackgroundColor(0xFFFF9600);
                                rightTextView.setTextColor(Color.WHITE);
                            }
                        }
                    }
                }
            }
        });
    }

    public interface OnClickListener {
        boolean onClick(ChampionBean.ChampionItems.ChampionItem bean, ChampionBean.ChampionItems items, boolean isAdd, int position, String title);
    }
}
