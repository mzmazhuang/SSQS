package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/12/12.
 */

public class DialogCell extends RelativeLayout {

    private TextView tvTitle;

    public DialogCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        tvTitle = new TextView(context);
        tvTitle.setTextSize(13);
        tvTitle.setGravity(Gravity.CENTER);
        addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        View view = new View(context);
        view.setBackgroundColor(0xFFEDEDED);
        LayoutParams params = new LayoutParams(LayoutHelper.MATCH_PARENT, 1);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(view, params);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setCheck(boolean check) {
        if (check) {
            setBackgroundColor(0xFF37C4FF);
            tvTitle.setTextColor(0xFFFFFFFF);
        } else {
            setBackgroundColor(0xFFFFFFFF);
            tvTitle.setTextColor(0xFF323232);
        }
    }
}
