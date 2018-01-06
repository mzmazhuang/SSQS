package com.dading.ssqs.cells;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/4.
 */

public class ChampionItemCell extends LinearLayout {

    private Context mContext;
    private TextView titleView;

    private List<ChampionChildCell> cells = new ArrayList<>();
    private List<LinearLayout> layouts = new ArrayList<>();

    public ChampionItemCell(Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        titleView = new TextView(context);
        titleView.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, AndroidUtilities.INSTANCE.dp(12), 0);
        titleView.setTextSize(12);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setBackgroundColor(0xFFFFFADC);
        titleView.setTextColor(0xFF74490D);
        addView(titleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));

        for (int i = 0; i < 10; i++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30));
            layouts.add(layout);

            ChampionChildCell leftCell = new ChampionChildCell(context);
            layout.addView(leftCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

            View view = new View(context);
            view.setBackgroundColor(0xFFE7E7E7);
            layout.addView(view, new LinearLayout.LayoutParams(1, LayoutHelper.MATCH_PARENT));

            ChampionChildCell rightCell = new ChampionChildCell(context);
            layout.addView(rightCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

            View lineView = new View(context);
            lineView.setBackgroundColor(0xFFE7E7E7);
            addView(lineView, new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1));

            cells.add(leftCell);
            cells.add(rightCell);
        }
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setData(ChampionBean.ChampionItems bean, ChampionChildCell.OnClickListener listener, String title, List<ToDayFootBallChampionFragment.MergeBean> focusList) {
        List<ChampionBean.ChampionItems.ChampionItem> list = bean.getItems();

        if (list != null && list.size() >= 1) {
            int row;
            if (list.size() == 0) {
                row = 0;
            } else if (list.size() <= 2) {
                row = 1;
            } else {
                if (list.size() / 2 == 0) {
                    row = list.size() / 2;
                } else {
                    row = list.size() / 2 + 1;
                }
            }

            for (int i = row; i < layouts.size(); i++) {
                layouts.get(i).setVisibility(View.GONE);
            }

            for (int i = 0; i < list.size(); i++) {
                cells.get(i).setData(list.get(i), bean, i, title, focusList);
                cells.get(i).setListener(listener);
            }
        }
    }
}
