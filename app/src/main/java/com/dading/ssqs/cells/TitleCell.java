package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;


/**
 * Created by mazhuang on 2017/11/24.
 * 标题
 */

public class TitleCell extends RelativeLayout {

    private ImageView backView;
    private TextView tvTitleView;

    public TitleCell(Context context, String title) {
        super(context);

        setBackgroundColor(getResources().getColor(R.color.home_top_blue));

        backView = new ImageView(context);
        backView.setPadding(AndroidUtilities.dp(15), AndroidUtilities.dp(15), AndroidUtilities.dp(15), AndroidUtilities.dp(15));
        backView.setImageResource(R.mipmap.er_back_arrow);
        addView(backView, LayoutHelper.createRelative(48, 48, 5, 0, 0, 0));

        tvTitleView = new TextView(context);
        tvTitleView.setGravity(Gravity.CENTER);
        tvTitleView.setTextSize(20);
        tvTitleView.setText(title);
        tvTitleView.setTextColor(Color.WHITE);
        addView(tvTitleView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, RelativeLayout.CENTER_IN_PARENT));
    }

    public void setBackListener(final OnClickListener listener) {
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
