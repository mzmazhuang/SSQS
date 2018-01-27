package com.dading.ssqs.cells;

/**
 * Created by mazhuang on 2018/1/27.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.adapter.newAdapter.BasketBallDetailsItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;

public class BasketBallItemCell extends LinearLayout {

    private Context mContext;
    private TextView titleView;

    private int type;

    private RelativeLayout itemLayout;
    private TextView numberView;

    private BasketBallDetailsItemAdapter adapter;

    public BasketBallItemCell(Context context, final int type) {
        super(context);
        mContext = context;

        this.type = type;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout layout = new RelativeLayout(context);
        layout.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, AndroidUtilities.INSTANCE.dp(12), 0);
        layout.setOnClickListener(listener);
        addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 23));

        titleView = new TextView(context);
        titleView.setTextSize(13);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setCompoundDrawablePadding(AndroidUtilities.INSTANCE.dp(5));
        if (type == 1) {
            layout.setBackgroundColor(0xFFCF570E);
            titleView.setText(LocaleController.getString(R.string.my_handicap));
            titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right, 0, 0, 0);
        } else {
            layout.setBackgroundColor(0xFFE19716);
            titleView.setText(LocaleController.getString(R.string.main_handicap));
            titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right_yellow, 0, 0, 0);
        }

        layout.addView(titleView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

        LinearLayout numberLayout = new LinearLayout(context);
        numberLayout.setGravity(Gravity.CENTER_VERTICAL);
        numberLayout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(numberLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        if (type == 1) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.ic_star_yellow);
            numberLayout.addView(imageView, LayoutHelper.createLinear(18, 18, 0, 0, 5, 0));
        }

        numberView = new TextView(context);
        numberView.setTextSize(13);
        numberView.setTextColor(Color.WHITE);
        numberView.setText("0");
        numberView.setGravity(Gravity.CENTER);
        numberView.setBackgroundColor(0xFF841F00);
        numberLayout.addView(numberView, LayoutHelper.createLinear(23, LayoutHelper.MATCH_PARENT));

        itemLayout = new RelativeLayout(context);
        itemLayout.setVisibility(View.GONE);
        addView(itemLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemLayout.addView(recyclerView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        adapter = new BasketBallDetailsItemAdapter(context);
        recyclerView.setAdapter(adapter);

        View view = new View(context);
        view.setBackgroundColor(Color.WHITE);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));
    }

    public void refreshData() {
        adapter.refreshData();
    }

    public void setFocus(List<BasketBallDetailsActivity.BasketData.BasketItemData> focus) {
        adapter.setFocus(focus);
    }

    public void setListener(BasketBallDetailsItemCell.OnItemClickListener listener) {
        adapter.setItemClickListener(listener);
    }

    public List<BasketBallDetailsActivity.BasketData> getItem() {
        return adapter.getData();
    }

    public void setOnItemClickListener(BasketBallDetailsItemAdapter.OnItemClickListener listener) {
        adapter.setListener(listener);
    }

    public void setData(List<BasketBallDetailsActivity.BasketData> data) {
        if (type == 1) {
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setLike(true);
                }
            }
        }
        adapter.setData(data);
    }

    public void setNumber(int number) {
        numberView.setText(number + "");
    }

    public void resetArrow() {
        itemLayout.setVisibility(View.GONE);
        if (type == 1) {
            titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right, 0, 0, 0);
        } else {
            titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right_yellow, 0, 0, 0);
        }
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (itemLayout.getVisibility() == View.GONE) {
                itemLayout.setVisibility(View.VISIBLE);

                if (type == 1) {
                    titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull, 0, 0, 0);
                } else {
                    titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_yellow, 0, 0, 0);
                }
            } else {
                itemLayout.setVisibility(View.GONE);
                if (type == 1) {
                    titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right, 0, 0, 0);
                } else {
                    titleView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_basket_pull_right_yellow, 0, 0, 0);
                }
            }
        }
    };
}