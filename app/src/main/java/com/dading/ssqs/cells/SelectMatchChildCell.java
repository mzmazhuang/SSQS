package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/12/13.
 */

public class SelectMatchChildCell extends LinearLayout {

    private TextView tvTitle;
    private TextView tvCount;
    private RelativeLayout layout;
    private boolean check;

    public SelectMatchChildCell(Context context) {
        super(context);

        setGravity(Gravity.CENTER);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));

        layout = new RelativeLayout(context);
        addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40, 10, 0, 0, 0));

        tvTitle = new TextView(context);
        tvTitle.setTextSize(14);
        layout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 5, 0, RelativeLayout.CENTER_VERTICAL));

        tvCount = new TextView(context);
        tvCount.setTextSize(14);
        RelativeLayout.LayoutParams countLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 5, 0);
        countLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        countLP.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(tvCount, countLP);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setCount(int count) {
        tvCount.setText(count + "场");
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
        if (check) {
            tvTitle.setTextColor(0xFFFF9600);
            tvCount.setTextColor(0xFFFF9600);
            layout.setBackgroundResource(R.drawable.bg_select_match_press);
        } else {
            tvTitle.setTextColor(0xFF999999);
            tvCount.setTextColor(0xFF999999);
            layout.setBackgroundResource(R.drawable.bg_select_match_gray);
        }
    }
}
