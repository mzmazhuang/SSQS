package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class GuessBallTitleCell extends LinearLayout {

    private TextView tvName;
    private View line;

    private boolean check;

    //因设计图上 上下两行title高度不一致 用isHeight变量来判断
    public GuessBallTitleCell(Context context, boolean isHeight) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        tvName = new TextView(context);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(13);
        tvName.setGravity(Gravity.CENTER);
        tvName.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);

        int height;
        if (isHeight) {
            height = 40;
        } else {
            height = 30;
        }

        addView(tvName, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, height));

        line = new View(context);
        line.setBackgroundColor(Color.WHITE);

        int lineHeight;
        if (isHeight) {
            lineHeight = 15;
        } else {
            lineHeight = 17;
        }

        addView(line, LayoutHelper.createLinear(1, lineHeight));
    }

    public void setCheck(boolean check) {
        this.check = check;

        if (check) {
            tvName.setTextColor(0xFFFFF000);
        } else {
            tvName.setTextColor(Color.WHITE);
        }
    }

    public boolean isCheck() {
        return check;
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void isShowLine(boolean show) {
        if (show) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }
}
