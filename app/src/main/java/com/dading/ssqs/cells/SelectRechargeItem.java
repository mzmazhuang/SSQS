package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/11/24.
 */

public class SelectRechargeItem extends LinearLayout {

    private TextView textView;
    private boolean check;

    public SelectRechargeItem(Context context) {
        super(context);

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(12);
        addView(textView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30, 3, 3, 3, 3));
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setCheck(boolean check) {
        this.check = check;
        if (check) {
            textView.setBackgroundResource(R.drawable.bg_select_recharge_press);
            textView.setTextColor(0xFFFFFFFF);
        } else {
            textView.setBackgroundResource(R.drawable.bg_select_recharge);
            textView.setTextColor(0xFFFF7426);
        }
    }

    public boolean getCheck() {
        return check;
    }

    public void setOnClickListener(final OnClickListener listener) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
