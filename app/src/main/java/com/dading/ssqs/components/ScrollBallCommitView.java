package com.dading.ssqs.components;

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

/**
 * Created by mazhuang on 2017/12/2.
 * 猜球中提交列表
 */

public class ScrollBallCommitView extends LinearLayout {

    private TextView countView;
    private TextView submitView;
    private ImageView imageView;

    public ScrollBallCommitView(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        RelativeLayout bottomLayout = new RelativeLayout(context);
        bottomLayout.setBackgroundColor(0xCC000000);
        addView(bottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 45));

        imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.ic_trash);
        bottomLayout.addView(imageView, LayoutHelper.createRelative(35, 35, 12, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        countView = new TextView(context);
        countView.setTextColor(Color.WHITE);
        countView.setTextSize(14);
        bottomLayout.addView(countView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        submitView = new TextView(context);
        submitView.setTextSize(18);
        submitView.setText("提交");
        submitView.setTextColor(Color.WHITE);
        submitView.setGravity(Gravity.CENTER);
        submitView.setBackgroundResource(R.drawable.bg_guess_ball_submit);
        RelativeLayout.LayoutParams submitLP = LayoutHelper.createRelative(70, 30, 0, 0, 12, 0);
        submitLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        submitLP.addRule(RelativeLayout.CENTER_VERTICAL);
        bottomLayout.addView(submitView, submitLP);
    }

    public void setOnSubmitClickListener(final OnClickListener listener) {
        submitView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    public void setCount(int count) {
        countView.setText("已选" + count + "场");
    }

    public void setDeleteClickListener(final OnClickListener listener) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
