package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/20.
 */

public class BasketDetailsChildCell extends LinearLayout {

    private View lineView;
    private TextView title;
    private TextView rightView;
    private TextView numberView;

    private boolean check;

    private BasketBallDetailsActivity.BasketData.BasketItemData data;
    private BasketBallDetailsActivity.BasketData basketData;

    public BasketDetailsChildCell(Context context) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);

        title = new TextView(context);
        title.setTextSize(11);
        title.setGravity(Gravity.CENTER_VERTICAL);
        addView(title, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        rightView = new TextView(context);
        rightView.setTextSize(11);
        rightView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        addView(rightView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        numberView = new TextView(context);
        numberView.setTextSize(11);
        numberView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        addView(numberView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        lineView = new View(context);
        lineView.setBackgroundColor(0xFFE7E7E7);
        addView(lineView, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));
    }

    public void setLeftPadding(int padding) {
        title.setPadding(AndroidUtilities.INSTANCE.dp(padding), 0, 0, 0);
    }

    public void setRightPadding(int padding) {
        numberView.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(padding), 0);
    }

    public void changeParams() {
        LinearLayout.LayoutParams titleLP = (LayoutParams) title.getLayoutParams();
        titleLP.weight = 1;
        title.setLayoutParams(titleLP);

        LinearLayout.LayoutParams numberLP = (LayoutParams) numberView.getLayoutParams();
        numberLP.weight = 1;
        numberView.setLayoutParams(numberLP);

        rightView.setVisibility(View.GONE);
    }

    public void setListener(final BasketBallDetailsItemCell.OnItemClickListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (check) {
                        if (listener.onClick(data, basketData, false)) {
                            check = false;

                            setBackgroundColor(Color.WHITE);
                            title.setTextColor(0xFF222222);
                            numberView.setTextColor(0xFFFF9600);
                            rightView.setTextColor(0xFF222222);
                        }
                    } else {
                        if (listener.onClick(data, basketData, true)) {
                            check = true;

                            setBackgroundColor(0xFFFF9600);
                            title.setTextColor(Color.WHITE);
                            numberView.setTextColor(Color.WHITE);
                            rightView.setTextColor(Color.WHITE);
                        }
                    }
                }
            }
        });
    }

    public void setData(BasketBallDetailsActivity.BasketData.BasketItemData data, BasketBallDetailsActivity.BasketData basketData, List<BasketBallDetailsActivity.BasketData.BasketItemData> focus, int position) {
        this.data = data;
        this.basketData = basketData;

        title.setTextColor(0xFF222222);
        title.setText(data.getLeftStr());

        if (!TextUtils.isEmpty(data.getRightStr())) {
            boolean flag = true;

            if (position == 0 || position % 2 == 0) {//主队
                flag = false;
            }

            String value = getRate2Str(data.getRightStr(), flag);

            if (!TextUtils.isEmpty(value) && !"null".equals(value)) {
                rightView.setVisibility(View.VISIBLE);
                rightView.setTextColor(0xFF222222);

                rightView.setText(value);
            }

        } else {
            rightView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getNumber())) {
            numberView.setVisibility(View.VISIBLE);
            numberView.setTextColor(0xFFFF9600);
            numberView.setText(data.getNumber());
        } else {
            numberView.setVisibility(View.GONE);
        }

        check = false;

        if (focus != null) {
            for (int i = 0; i < focus.size(); i++) {
                BasketBallDetailsActivity.BasketData.BasketItemData bean = focus.get(i);
                if (bean.getId() == data.getId() && bean.getNumber() == data.getNumber()) {
                    check = true;

                    setBackgroundColor(0xFFFF9600);
                    title.setTextColor(Color.WHITE);
                    numberView.setTextColor(Color.WHITE);
                    rightView.setTextColor(Color.WHITE);
                    return;
                }
            }
        }
    }

    public void isShowLine(boolean show) {
        if (show) {
            lineView.setVisibility(View.VISIBLE);
        } else {
            lineView.setVisibility(View.GONE);
        }
    }

    private String getRate2Str(String str, boolean isBig) {
        String value = "";
        if (!TextUtils.isEmpty(str)) {

            String[] array = null;

            if (str.contains("/")) {
                array = str.split("/");
            }

            String rate2;

            if (array != null && array.length == 2) {
                rate2 = array[1];
            } else {
                rate2 = str;
            }

            try {
                if (isBig) {
                    if (rate2.equals("0")) {
                        value = "0";
                    } else if (Double.valueOf(rate2) > 0.0) {
                        if (array != null) {
                            value = array[0] + "/" + Math.abs(Double.valueOf(rate2)) + "";
                        } else {
                            value = Math.abs(Double.valueOf(rate2)) + "";
                        }
                    } else {
                        value = "null";
                    }
                } else {
                    if (rate2.equals("-0")) {
                        value = "0";
                    } else if (Double.valueOf(rate2) < 0.0) {
                        if (array != null) {
                            value = array[0] + "/" + Math.abs(Double.valueOf(rate2)) + "";
                        } else {
                            value = Math.abs(Double.valueOf(rate2)) + "";
                        }
                    } else {
                        value = "null";
                    }
                }
            } catch (Exception ex) {//不是double类型

            }
        } else {
            value = "null";
        }

        return value;
    }
}
