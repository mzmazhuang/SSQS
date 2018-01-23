package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/12/22.
 */

public class ResultTimeLayout extends RelativeLayout {

    private TextView timeTextView;
    private int day = 1;

    private ImageView rightView;
    private ImageView leftView;

    private TimeListener listener;

    public void setListener(TimeListener listener) {
        this.listener = listener;
    }

    public interface TimeListener {
        void onChange(int day);

        void onSelectTime();
    }

    public ResultTimeLayout(Context context) {
        super(context);

        LinearLayout timeLayout = new LinearLayout(context);
        timeLayout.setGravity(Gravity.CENTER_VERTICAL);
        timeLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(timeLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        leftView = new ImageView(context);
        leftView.setImageResource(R.mipmap.ic_result_left);
        leftView.setScaleType(ImageView.ScaleType.CENTER);
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day < 7) {
                    day++;

                    if (listener != null) {
                        listener.onChange(day);
                    }
                    changeImageStyle(day);
                }
            }
        });
        timeLayout.addView(leftView, LayoutHelper.createLinear(25, 25));

        timeTextView = new TextView(context);
        timeTextView.setTextColor(0xFFFF0000);
        timeTextView.setTextSize(12);
        timeLayout.addView(timeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 8, 0, 8, 0));

        rightView = new ImageView(context);
        rightView.setScaleType(ImageView.ScaleType.CENTER);
        rightView.setImageResource(R.mipmap.ic_result_right_gray);
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day > 1) {
                    day--;
                    if (listener != null) {
                        listener.onChange(day);
                    }
                    changeImageStyle(day);
                }
            }
        });
        timeLayout.addView(rightView, LayoutHelper.createLinear(25, 25));

        ImageView timeView = new ImageView(context);
        timeView.setImageResource(R.mipmap.ic_result_time);
        timeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        timeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelectTime();
                }
            }
        });
        RelativeLayout.LayoutParams timeLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0);
        timeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        timeLP.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(timeView, timeLP);
    }

    public void setTime(String time) {
        timeTextView.setText(time);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void changeImageStyle(int day) {
        if (day == 1) {
            leftView.setImageResource(R.mipmap.ic_result_left);
            rightView.setImageResource(R.mipmap.ic_result_right_gray);
        } else if (day == 7) {
            leftView.setImageResource(R.mipmap.ic_result_left_gray);
            rightView.setImageResource(R.mipmap.ic_result_right);
        } else {
            leftView.setImageResource(R.mipmap.ic_result_left);
            rightView.setImageResource(R.mipmap.ic_result_right);
        }
    }
}
