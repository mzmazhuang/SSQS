package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

import java.math.BigDecimal;

/**
 * Created by mazhuang on 2017/12/2.
 */

public class ScrollBallCommitMenuCell extends LinearLayout {

    private TextView ranksTextView1;
    private TextView ranksTextView2;
    private TextView ranksTextView3;
    private TextView oddsTextView;

    private ImageView delImage;

    private EditText moneyTextView;
    private TextView cashTextView;

    private View lineView;
    private TextView vsView;

    private int position;

    private ScrollBallCommitMenuAdapter.OnNumberListener listener;

    private RelativeLayout moneyLayout;

    public ScrollBallCommitMenuCell(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        RelativeLayout layout = new RelativeLayout(context);
        addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout container = new LinearLayout(context);
        container.setBackgroundColor(0xFFE9F9FF);
        container.setOrientation(LinearLayout.VERTICAL);
        layout.addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        delImage = new ImageView(context);
        delImage.setScaleType(ImageView.ScaleType.CENTER);
        delImage.setImageResource(R.mipmap.ic_scroll_menu_del);
        RelativeLayout.LayoutParams delLP = LayoutHelper.createRelative(30, 30, 0, 0, 12, 0);
        delLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        delLP.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(delImage, delLP);

        RelativeLayout infoLayout = new RelativeLayout(context);
        infoLayout.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(45), 0);
        container.addView(infoLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 35));

        View view = new View(context);
        view.setId(R.id.scroll_commit_menu_view);
        view.setBackgroundColor(0xFF009BDB);
        infoLayout.addView(view, LayoutHelper.createRelative(1, 15, RelativeLayout.CENTER_IN_PARENT));

        RelativeLayout leftLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams leftLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        leftLP.addRule(RelativeLayout.LEFT_OF, view.getId());
        infoLayout.addView(leftLayout, leftLP);

        vsView = new TextView(context);
        vsView.setId(R.id.scroll_commit_menu_vs);
        vsView.setTextSize(10);
        vsView.setTextColor(0xFFFF0000);
        leftLayout.addView(vsView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        ranksTextView1 = new TextView(context);
        ranksTextView1.setSingleLine();
        ranksTextView1.setTextSize(12);
        ranksTextView1.setTextColor(0xFF626262);
        ranksTextView1.setGravity(Gravity.LEFT);
        ranksTextView1.setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams ranks1LP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 10, 0);
        ranks1LP.addRule(RelativeLayout.CENTER_VERTICAL);
        ranks1LP.addRule(RelativeLayout.LEFT_OF, vsView.getId());
        leftLayout.addView(ranksTextView1, ranks1LP);

        ranksTextView2 = new TextView(context);
        ranksTextView2.setSingleLine();
        ranksTextView2.setPadding(0, 0, AndroidUtilities.dp(15), 0);
        ranksTextView2.setTextSize(12);
        ranksTextView2.setTextColor(0xFF626262);
        ranksTextView2.setGravity(Gravity.RIGHT);
        ranksTextView2.setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams ranks2LP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10, 0, 0, 0);
        ranks2LP.addRule(RelativeLayout.CENTER_VERTICAL);
        ranks2LP.addRule(RelativeLayout.RIGHT_OF, vsView.getId());
        leftLayout.addView(ranksTextView2, ranks2LP);

        RelativeLayout rightLayout = new RelativeLayout(context);
        rightLayout.setPadding(AndroidUtilities.dp(15), 0, 0, 0);
        RelativeLayout.LayoutParams rightLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        rightLP.addRule(RelativeLayout.RIGHT_OF, view.getId());
        infoLayout.addView(rightLayout, rightLP);

        TextView linkView = new TextView(context);
        linkView.setId(R.id.scroll_commit_menu_link);
        linkView.setText("@");
        linkView.setTextSize(10);
        linkView.setTextColor(0xFF222222);
        rightLayout.addView(linkView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        ranksTextView3 = new TextView(context);
        ranksTextView3.setSingleLine();
        ranksTextView3.setTextSize(12);
        ranksTextView3.setTextColor(0xFFE82324);
        ranksTextView3.setGravity(Gravity.LEFT);
        ranksTextView3.setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams ranks3LP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        ranks3LP.addRule(RelativeLayout.CENTER_VERTICAL);
        ranks3LP.addRule(RelativeLayout.LEFT_OF, linkView.getId());
        rightLayout.addView(ranksTextView3, ranks3LP);

        oddsTextView = new TextView(context);
        oddsTextView.setTextSize(12);
        oddsTextView.setTextColor(0xFFE82324);
        oddsTextView.setGravity(Gravity.RIGHT);
        RelativeLayout.LayoutParams oddsLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        oddsLP.addRule(RelativeLayout.CENTER_VERTICAL);
        oddsLP.addRule(RelativeLayout.RIGHT_OF, linkView.getId());
        rightLayout.addView(oddsTextView, oddsLP);

        moneyLayout = new RelativeLayout(context);
        moneyLayout.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);
        container.addView(moneyLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        View moneyView = new View(context);
        moneyView.setId(R.id.scroll_commit_menu_money_view);
        moneyView.setBackgroundColor(0xFF009BDB);
        moneyLayout.addView(moneyView, LayoutHelper.createRelative(1, 15, RelativeLayout.CENTER_IN_PARENT));

        LinearLayout moneyLeftLayout = new LinearLayout(context);
        moneyLeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        moneyLeftLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams moneyLeftLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 30, 0, 0, 15, 0);
        moneyLeftLP.addRule(RelativeLayout.LEFT_OF, moneyView.getId());
        moneyLeftLP.addRule(RelativeLayout.CENTER_VERTICAL);
        moneyLayout.addView(moneyLeftLayout, moneyLeftLP);

        TextView tvMoneyTipView = new TextView(context);
        tvMoneyTipView.setTextColor(0xFF222222);
        tvMoneyTipView.setTextSize(12);
        tvMoneyTipView.setText("下注金额:");
        moneyLeftLayout.addView(tvMoneyTipView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        moneyTextView = new EditText(context);
        AndroidUtilities.disableShowInput(moneyTextView);
        moneyTextView.setPadding(AndroidUtilities.dp(2), 0, AndroidUtilities.dp(2), 0);
        moneyTextView.setTextSize(12);
        moneyTextView.setSingleLine();
        moneyTextView.setTextColor(0xFF222222);
        moneyTextView.setGravity(Gravity.CENTER_VERTICAL);
        moneyTextView.setBackgroundResource(R.drawable.bg_guess_ball_menu_money);
        moneyTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (listener != null) {
                        listener.onReady(position);
                    }
                }
                return false;
            }
        });
        moneyTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                moneyTextView.setSelection(moneyTextView.getText().length());
            }
        });
        moneyLeftLayout.addView(moneyTextView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 25, 5, 0, 0, 0));

        LinearLayout moneyRightLayout = new LinearLayout(context);
        moneyRightLayout.setGravity(Gravity.CENTER_VERTICAL);
        moneyRightLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams moneyRightLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 30, 15, 0, 0, 0);
        moneyRightLP.addRule(RelativeLayout.RIGHT_OF, moneyView.getId());
        moneyRightLP.addRule(RelativeLayout.CENTER_VERTICAL);
        moneyLayout.addView(moneyRightLayout, moneyRightLP);

        TextView estimateTextView = new TextView(context);
        estimateTextView.setText("预计奖金:");
        estimateTextView.setTextColor(0xFF222222);
        estimateTextView.setTextSize(12);
        moneyRightLayout.addView(estimateTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        cashTextView = new TextView(context);
        cashTextView.setTextSize(12);
        cashTextView.setText("0元");
        cashTextView.setTextColor(0xFFFF0000);
        cashTextView.setSingleLine();
        moneyRightLayout.addView(cashTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 3, 0, 0, 0));

        lineView = new View(context);
        lineView.setBackgroundColor(0xFFF5F4F9);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setType(int type) {
        if (type == 2) {
            moneyLayout.setVisibility(View.GONE);
        }
    }

    public void setListener(ScrollBallCommitMenuAdapter.OnNumberListener listener) {
        this.listener = listener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMoney(String money, boolean isFocus, String odds) {
        moneyTextView.setText(money);

        if (!TextUtils.isEmpty(money)) {
            if (isFocus) {
                moneyTextView.requestFocus();
            }
            resetMoney(money, odds);
        } else {
            cashTextView.setText("0元");
        }
    }

    private void resetMoney(String money, String odds) {
        try {
            int cash = Integer.valueOf(money);

            double odd = Double.valueOf(odds);

            double add = cash * odd;

            BigDecimal bigDecimal = new BigDecimal(add);

            String str = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元";

            cashTextView.setText(str);

        } catch (Exception ex) {
            cashTextView.setText("0元");
        }
    }

    public void setRans1Title(String title) {
        ranksTextView1.setText(title);
    }

    public void setRans2Title(String title) {
        ranksTextView2.setText(title);

        if (TextUtils.isEmpty(title)) {
            vsView.setVisibility(View.GONE);
        } else {
            vsView.setText("vs");
        }
    }

    public void setRans3Title(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (title.contains("<font")) {
                ranksTextView3.setText(Html.fromHtml(title));
            } else {
                ranksTextView3.setText(title);
            }
        }
    }

    public void setOdds(String odds) {
        oddsTextView.setText(odds);
    }

    public void setDeleteOnClickListener(final OnClickListener listener) {
        delImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
