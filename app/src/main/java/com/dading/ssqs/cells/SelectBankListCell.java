package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class SelectBankListCell extends LinearLayout {

    private ImageView iconView;
    private TextView tvTitle;

    public SelectBankListCell(Context context) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(LayoutHelper.createLinear(198, 50));
        setBackgroundResource(R.drawable.bg_select_bank_broder);

        iconView = new ImageView(context);
        addView(iconView, LayoutHelper.createLinear(35, 35, Gravity.CENTER_VERTICAL, 5, 0, 5, 0));

        tvTitle = new TextView(context);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(0xFF000000);
        addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    public void setIconResource(String url) {
        SSQSApplication.glide.load(url).into(iconView);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
