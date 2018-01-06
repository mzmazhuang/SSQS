package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by asus on 2017/11/25.
 */

public class BankCell extends LinearLayout {
    private CardView cardView;

    private ImageView iconView;
    private TextView bankText;
    private TextView numberText;
    private TextView peopleText;
    private TextView bankSubText;

    public void setOnClickListener(final OnClickListener listener) {
        cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    public BankCell(Context context) {
        super(context);

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        cardView = new CardView(context);
        cardView.setRadius(AndroidUtilities.INSTANCE.dp(5));
        cardView.setCardBackgroundColor(0xFFF8F8F8);
        addView(cardView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10, 10, 10, 10));

        LinearLayout container = new LinearLayout(context);
        container.setBackgroundColor(Color.WHITE);
        container.setOrientation(LinearLayout.HORIZONTAL);
        cardView.addView(container, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        iconView = new ImageView(context);
        container.addView(iconView, LayoutHelper.createLinear(40, 40, Gravity.CENTER_VERTICAL, 10, 10, 10, 10));

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(infoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout bankLayout = new LinearLayout(context);
        bankLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.addView(bankLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView bankTextView = new TextView(context);
        bankTextView.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        bankTextView.setTextColor(0xFF333333);
        bankTextView.setText(LocaleController.getString(R.string.bank_name_all));
        bankLayout.addView(bankTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        bankText = new TextView(context);
        bankText.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        bankText.setTextColor(0xFFAEAEAE);
        bankLayout.addView(bankText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout numberLayout = new LinearLayout(context);
        numberLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.addView(numberLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView numberTextView = new TextView(context);
        numberTextView.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        numberTextView.setTextColor(0xFF333333);
        numberTextView.setText(LocaleController.getString(R.string.bank_num_all));
        numberLayout.addView(numberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        numberText = new TextView(context);
        numberText.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        numberText.setTextColor(0xFFAEAEAE);
        numberLayout.addView(numberText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout peopleLayout = new LinearLayout(context);
        peopleLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.addView(peopleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView peopleTextView = new TextView(context);
        peopleTextView.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        peopleTextView.setTextColor(0xFF333333);
        peopleTextView.setText(LocaleController.getString(R.string.owner_all));
        peopleLayout.addView(peopleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        peopleText = new TextView(context);
        peopleText.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        peopleText.setTextColor(0xFFAEAEAE);
        peopleLayout.addView(peopleText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout subTextLayout = new LinearLayout(context);
        subTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.addView(subTextLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView subTextView = new TextView(context);
        subTextView.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        subTextView.setTextColor(0xFF333333);
        subTextView.setText(LocaleController.getString(R.string.bank_open_place));
        subTextLayout.addView(subTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        bankSubText = new TextView(context);
        bankSubText.setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        bankSubText.setTextColor(0xFFAEAEAE);
        subTextLayout.addView(bankSubText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public void setIconResource(String url) {
        SSQSApplication.glide.load(url).into(iconView);
    }

    public void setBankName(String bankName) {
        bankText.setText(bankName);
    }

    public void setBankNumber(String number) {
        numberText.setText(number);
    }

    public void setBankPeople(String people) {
        peopleText.setText(people);
    }

    public void setBankSubText(String subText) {
        bankSubText.setText(subText);
    }
}
