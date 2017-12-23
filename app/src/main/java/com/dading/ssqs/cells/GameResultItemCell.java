package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class GameResultItemCell extends LinearLayout {

    private TextView ranksName1;
    private TextView ranksName2;
    private TextView integralView1;
    private TextView integralView2;
    private TextView integralView3;
    private TextView integralView4;

    public GameResultItemCell(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        addView(container, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 92));

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(leftLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        TextView tip1View = new TextView(context);
        tip1View.setBackgroundColor(Color.WHITE);
        tip1View.setTextColor(0xFF222222);
        tip1View.setTextSize(12);
        tip1View.setText("比赛队伍");
        tip1View.setGravity(Gravity.CENTER);
        leftLayout.addView(tip1View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFE7E7E7);
        leftLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        TextView tip2View = new TextView(context);
        tip2View.setBackgroundColor(Color.WHITE);
        tip2View.setTextColor(0xFF222222);
        tip2View.setTextSize(12);
        tip2View.setText("半　　场");
        tip2View.setGravity(Gravity.CENTER);
        leftLayout.addView(tip2View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View line1View = new View(context);
        line1View.setBackgroundColor(0xFFE7E7E7);
        leftLayout.addView(line1View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        TextView tip3View = new TextView(context);
        tip3View.setBackgroundColor(Color.WHITE);
        tip3View.setTextColor(0xFF222222);
        tip3View.setTextSize(12);
        tip3View.setText("全　　场");
        tip3View.setGravity(Gravity.CENTER);
        leftLayout.addView(tip3View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View leftView = new View(context);
        leftView.setBackgroundColor(0xFFE7E7E7);
        container.addView(leftView, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout centerLayout = new LinearLayout(context);
        centerLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(centerLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 4f));

        ranksName1 = new TextView(context);
        ranksName1.setTextSize(12);
        ranksName1.setGravity(Gravity.CENTER);
        centerLayout.addView(ranksName1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View line2View = new View(context);
        line2View.setBackgroundColor(0xFFE7E7E7);
        centerLayout.addView(line2View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        integralView1 = new TextView(context);
        integralView1.setBackgroundColor(0xFFFFFADC);
        integralView1.setTextSize(12);
        integralView1.setGravity(Gravity.CENTER);
        centerLayout.addView(integralView1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View line3View = new View(context);
        line3View.setBackgroundColor(0xFFE7E7E7);
        centerLayout.addView(line3View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        integralView2 = new TextView(context);
        integralView2.setTextSize(12);
        integralView2.setGravity(Gravity.CENTER);
        centerLayout.addView(integralView2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View rightView = new View(context);
        rightView.setBackgroundColor(0xFFE7E7E7);
        container.addView(rightView, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(rightLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 4f));

        ranksName2 = new TextView(context);
        ranksName2.setTextSize(12);

        ranksName2.setGravity(Gravity.CENTER);
        rightLayout.addView(ranksName2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View line4View = new View(context);
        line4View.setBackgroundColor(0xFFE7E7E7);
        rightLayout.addView(line4View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        integralView3 = new TextView(context);
        integralView3.setBackgroundColor(0xFFFFFADC);
        integralView3.setTextSize(12);
        integralView3.setGravity(Gravity.CENTER);
        rightLayout.addView(integralView3, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View line5View = new View(context);
        line5View.setBackgroundColor(0xFFE7E7E7);
        rightLayout.addView(line5View, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        integralView4 = new TextView(context);
        integralView4.setTextSize(12);
        integralView4.setGravity(Gravity.CENTER);
        rightLayout.addView(integralView4, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        View view = new View(context);
        view.setBackgroundColor(0xFFE7E7E7);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setRansTitle(String title, String title1, boolean isHome) {
        ranksName1.setText(title);
        ranksName2.setText(title1);

        if (isHome) {
            ranksName1.setTextColor(0xFFFF0000);
            ranksName2.setTextColor(0xFF626262);
        } else {
            ranksName1.setTextColor(0xFF626262);
            ranksName2.setTextColor(0xFFFF0000);
        }
    }

    public void setIntegral(String integral, String integral2, String integral3, String integral4, boolean isHome) {
        integralView1.setText(integral);
        integralView2.setText(integral2);
        integralView3.setText(integral3);
        integralView4.setText(integral4);

        if (isHome) {
            integralView1.setTextColor(0xFFFF0000);
            integralView2.setTextColor(0xFFFF0000);
            integralView3.setTextSize(0xFF626262);
            integralView4.setTextSize(0xFF626262);
        } else {
            integralView1.setTextColor(0xFF626262);
            integralView2.setTextColor(0xFF626262);
            integralView3.setTextSize(0xFFFF0000);
            integralView4.setTextSize(0xFFFF0000);
        }
    }
}
