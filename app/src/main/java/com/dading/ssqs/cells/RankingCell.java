package com.dading.ssqs.cells;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2017/12/27.
 */

public class RankingCell extends RelativeLayout {

    private TextView titleTextView;
    private TextView subTitleTextView;

    private TextView rightTextView;

    public RankingCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));

        ImageView imageView = new ImageView(context);
        imageView.setId(R.id.ranking_image);
        imageView.setImageResource(R.mipmap.ic_ranking_medal);
        addView(imageView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        rightTextView = new TextView(context);
        rightTextView.setId(R.id.ranking_right);
        rightTextView.setTextSize(13);
        rightTextView.setTextColor(0xFFEC1313);
        rightTextView.setSingleLine();
        LayoutParams rightLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 15, 0);
        rightLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightLP.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(rightTextView, rightLP);

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams infoLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 10, 0, 10, 0);
        infoLP.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        infoLP.addRule(RelativeLayout.LEFT_OF, rightTextView.getId());
        infoLP.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(infoLayout, infoLP);

        titleTextView = new TextView(context);
        titleTextView.setTextSize(13);
        titleTextView.setTextColor(0xFF323232);
        titleTextView.setSingleLine();
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        infoLayout.addView(titleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        subTitleTextView = new TextView(context);
        subTitleTextView.setTextSize(12);
        subTitleTextView.setTextColor(0xFF9E9E9E);
        subTitleTextView.setSingleLine();
        infoLayout.addView(subTitleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view = new View(context);
        view.setBackgroundColor(0xFFEFEFF4);
        addView(view, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1, RelativeLayout.ALIGN_PARENT_BOTTOM));
    }

    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public void setSubTitle(String matchName) {
        if (!TextUtils.isEmpty(matchName)) {
            if (matchName.contains("|")) {
                String[] splitArray = matchName.split("\\|");
                if (splitArray != null && splitArray.length == 2) {
                    subTitleTextView.setText(splitArray[0] + "　VS　" + splitArray[1]);
                }
            } else {
                subTitleTextView.setText(matchName);
            }
        }
    }

    public void setRightText(String text) {
        rightTextView.setText("赢得" + text + "元");
    }
}
