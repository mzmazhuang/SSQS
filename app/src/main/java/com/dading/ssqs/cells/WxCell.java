package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class WxCell extends LinearLayout {

    private ImageView iconView;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private View view;

    public WxCell(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(infoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 60));

        iconView = new ImageView(context);
        infoLayout.addView(iconView, LayoutHelper.createLinear(40, 40, 10, 10, 10, 10));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        tvTitle = new TextView(context);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(0xFF000000);
        layout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 0, 1f));

        tvSubTitle = new TextView(context);
        tvSubTitle.setTextSize(12);
        tvSubTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvSubTitle.setTextColor(0xFF787878);
        layout.addView(tvSubTitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 0, 1f));

        view = new View(context);
        view.setBackgroundColor(0xFFF8F8F8);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 5));
    }

    public void isShowLine(boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setImageResource(String url) {
        SSQSApplication.glide.load(url).asBitmap().into(iconView);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setSubTitle(String subTitle) {
        tvSubTitle.setText(subTitle);
    }
}
