package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by mazhuang on 2017/12/19.
 */

public class TimeCell extends LinearLayout {

    private Calendar calendar = Calendar.getInstance();

    private TextView todayText;
    private TextView time1;
    private TextView time2;
    private TextView time3;
    private TextView time4;
    private TextView time6;

    public TimeCell(Context context) {
        super(context);

        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);

        todayText = new TextView(context);
        todayText.setTextSize(12);
        todayText.setText(LocaleController.getString(R.string.scroll_title15));
        todayText.setTextColor(0xFFFF0000);
        todayText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(1);
            }
        });
        addView(todayText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        time1 = new TextView(context);
        time1.setTextSize(12);
        time1.setGravity(Gravity.CENTER);
        time1.setTextColor(0xFF222222);
        time1.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日");
        time1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(2);
            }
        });
        addView(time1, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        time2 = new TextView(context);
        time2.setTextSize(12);
        time2.setGravity(Gravity.CENTER);
        time2.setTextColor(0xFF222222);
        time2.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日");
        time2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(3);
            }
        });
        addView(time2, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        time3 = new TextView(context);
        time3.setTextSize(12);
        time3.setGravity(Gravity.CENTER);
        time3.setTextColor(0xFF222222);
        time3.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日");
        time3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(4);
            }
        });
        addView(time3, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        time4 = new TextView(context);
        time4.setTextSize(12);
        time4.setGravity(Gravity.CENTER);
        time4.setTextColor(0xFF222222);
        time4.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日");
        time4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(5);
            }
        });
        addView(time4, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        time6 = new TextView(context);
        time6.setTextSize(12);
        time6.setGravity(Gravity.CENTER);
        time6.setTextColor(0xFF222222);
        time6.setText(LocaleController.getString(R.string.all_date));
        time6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                check(7);
            }
        });
        addView(time6, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));
    }

    private void check(int type) {
        todayText.setTextColor(0xFF222222);
        time1.setTextColor(0xFF222222);
        time2.setTextColor(0xFF222222);
        time3.setTextColor(0xFF222222);
        time4.setTextColor(0xFF222222);
        time6.setTextColor(0xFF222222);

        String time = "";

        if (type == 1) {
            todayText.setTextColor(0xFFFF0000);
            time = DateUtils.getCurTime("yyyyMMdd");
        } else if (type == 2) {
            time1.setTextColor(0xFFFF0000);
            time = DateUtils.getCurTimeAddND(1, "yyyyMMdd");
        } else if (type == 3) {
            time2.setTextColor(0xFFFF0000);
            time = DateUtils.getCurTimeAddND(2, "yyyyMMdd");
        } else if (type == 4) {
            time3.setTextColor(0xFFFF0000);
            time = DateUtils.getCurTimeAddND(3, "yyyyMMdd");
        } else if (type == 5) {
            time4.setTextColor(0xFFFF0000);
            time = DateUtils.getCurTimeAddND(4, "yyyyMMdd");
        } else if (type == 7) {
            time6.setTextColor(0xFFFF0000);
            time = "19700101";
        }

        if (listener != null) {
            listener.onChange(time);
        }
    }

    private OnChangTimeListener listener;

    public void setListener(OnChangTimeListener listener) {
        this.listener = listener;
    }

    public interface OnChangTimeListener {
        void onChange(String time);
    }
}
