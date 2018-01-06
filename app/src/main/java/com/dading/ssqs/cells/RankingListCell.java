package com.dading.ssqs.cells;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingListCell extends RelativeLayout {

    private TextView titleTextView;
    private TextView moneyTextView;
    private TextView typeTextView;

    public RankingListCell(Context context) {
        super(context);

        LinearLayout container = new LinearLayout(context);
        container.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, AndroidUtilities.INSTANCE.dp(12), 0);
        container.setOrientation(LinearLayout.HORIZONTAL);
        addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 40));

        titleTextView = new TextView(context);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextSize(12);
        titleTextView.setTextColor(0xFF323232);
        container.addView(titleTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        moneyTextView = new TextView(context);
        moneyTextView.setGravity(Gravity.CENTER);
        moneyTextView.setTextSize(12);
        moneyTextView.setTextColor(0xFFFC3C3C);
        container.addView(moneyTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        typeTextView = new TextView(context);
        typeTextView.setGravity(Gravity.CENTER);
        typeTextView.setTextSize(12);
        typeTextView.setTextColor(0xFF323232);
        typeTextView.setSingleLine();
        typeTextView.setEllipsize(TextUtils.TruncateAt.END);
        container.addView(typeTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        View view = new View(context);
        view.setBackgroundColor(0xFFEDEDED);
        addView(view, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1, RelativeLayout.ALIGN_PARENT_BOTTOM));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setMoney(String money) {
        moneyTextView.setText(money);
    }

    public void setType(String matchName) {
        if (!TextUtils.isEmpty(matchName)) {
            if (matchName.contains("|")) {
                String[] splitArray = matchName.split("\\|");
                if (splitArray != null && splitArray.length == 2) {
                    typeTextView.setText(Html.fromHtml("<font color=\"#323232\">" + splitArray[0] + "</font>&nbsp;&nbsp;&nbsp;<font color=\"#FF0000\">VS</font>&nbsp;&nbsp;&nbsp;<font color=\"#323232\">" + splitArray[1] + "</font>"));
                }
            } else {
                typeTextView.setText(matchName);
            }
        }
    }
}
