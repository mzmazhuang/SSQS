package com.dading.ssqs.cells;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.BasketBallPassAdapter;
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 * 篮球 综合过关
 */

public class BasketBallPassCell extends LinearLayout {

    private Context mContext;
    private ImageView arrowImageView;
    private TextView tvTitle;
    private LinearLayout topLayout;
    private LinearLayout itemLayout;
    private BasketBallPassAdapter adapter;

    private ScrollBallBasketBallBean bean;

    public BasketBallPassCell(Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        topLayout = new LinearLayout(context);
        topLayout.setPadding(AndroidUtilities.dp(12), AndroidUtilities.dp(5), 0, AndroidUtilities.dp(5));
        topLayout.setBackgroundColor(0xFFDEF5FF);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        arrowImageView = new ImageView(context);
        arrowImageView.setImageResource(R.mipmap.ic_jiantou_right);
        topLayout.addView(arrowImageView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 5, 0));

        tvTitle = new TextView(context);
        tvTitle.setTextColor(0xFF222222);
        tvTitle.setTextSize(12);
        topLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        itemLayout = new LinearLayout(context);
        itemLayout.setVisibility(View.GONE);
        addView(itemLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemLayout.addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        adapter = new BasketBallPassAdapter(context);
        recyclerView.setAdapter(adapter);

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFE7E7E7);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));
    }

    public void setData(ScrollBallBasketBallBean bean) {
        this.bean = bean;

        tvTitle.setText(bean.getTitle().getTitle());

        adapter.setBeanId(bean.getTitle().getId());
        adapter.setData(bean.getItems());
    }

    public void checkShow() {
        if (bean.getIsOpen() == 1) {
            openItemLayout();
        } else {
            closeItemLayout();
        }
    }

    public void setOnItemListener(BasketScrollBallItemAdapter.OnItemClickListener listener) {
        adapter.setListener(listener);
    }

    public void setFocus(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        adapter.setFocus(list);
    }

    public void setTopClickListener(final ScrollBallCell.OnReadyListener listener) {
        topLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //加载数据
                if (bean.getItems() == null || bean.getItems().size() == 0) {
                    if (listener != null) {
                        listener.onReady(bean == null ? "" : bean.getTitle().getTitle());
                    }
                } else {
                    if (itemLayout.getVisibility() == View.GONE) {
                        open();
                    } else {
                        closeItemLayout();
                    }
                }
            }
        });
    }

    private void closeItemLayout() {
        itemLayout.setVisibility(View.GONE);

        arrowImageView.setImageResource(R.mipmap.ic_jiantou_right);

        if (bean != null) {
            bean.setIsOpen(0);
        }
    }

    public void openItemLayout() {
        if (bean.getItems() != null && bean.getItems().size() > 0) {
            open();
        }
    }

    private void open() {
        itemLayout.setVisibility(View.VISIBLE);

        arrowImageView.setImageResource(R.mipmap.ic_jiantou_down);

        if (bean != null) {
            bean.setIsOpen(1);
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
