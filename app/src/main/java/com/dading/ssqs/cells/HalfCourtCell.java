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
import com.dading.ssqs.adapter.newAdapter.HalfCourtAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallHalfCourtBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;


/**
 * Created by mazhuang on 2017/12/4.
 * 场地Cell  半场/全场
 */

public class HalfCourtCell extends LinearLayout {

    private Context mContext;
    private ImageView arrowImageView;
    private TextView tvTitle;
    private LinearLayout topLayout;
    private LinearLayout itemLayout;
    private HalfCourtAdapter adapter;

    private ScrollBallFootBallHalfCourtBean bean;

    public void setFocus(List<ScrollBallHalfCourtFragment.MergeBean> focus) {
        adapter.setFocus(focus);
    }

    public void setListener(HalfCourtItemCell.OnItemClickListener listener) {
        adapter.setListener(listener);
    }

    public HalfCourtCell(Context context) {
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

        adapter = new HalfCourtAdapter(context);
        recyclerView.setAdapter(adapter);

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFE7E7E7);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));
    }

    public void setData(ScrollBallFootBallHalfCourtBean bean, boolean isScroll) {
        this.bean = bean;

        adapter.setScroll(isScroll);
        adapter.setList(bean.getItems());
    }

    public void checkShow() {
        if (bean.getIsOpen() == 1) {
            openItemLayout();
        } else {
            closeItemLayout();
        }
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

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void openItemLayout() {
        if (bean.getItems() != null && bean.getItems().size() > 0) {
            open();
        }
    }

    private void closeItemLayout() {
        itemLayout.setVisibility(View.GONE);

        arrowImageView.setImageResource(R.mipmap.ic_jiantou_right);

        if (bean != null) {
            bean.setIsOpen(0);
        }
    }

    private void open() {
        itemLayout.setVisibility(View.VISIBLE);

        arrowImageView.setImageResource(R.mipmap.ic_jiantou_down);

        if (bean != null) {
            bean.setIsOpen(1);
        }
    }
}
