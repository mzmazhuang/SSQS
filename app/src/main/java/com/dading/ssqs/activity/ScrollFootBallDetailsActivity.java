package com.dading.ssqs.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Created by mazhuang on 2018/1/27.
 * 滚球足球的所有玩法
 */

public class ScrollFootBallDetailsActivity extends BaseActivity implements OnRefreshListener {

    private Context mContext;

    private SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        mContext = this;

        RelativeLayout container = new RelativeLayout(mContext);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setBackgroundColor(Color.WHITE);
        container.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        TitleCell titleCell = new TitleCell(this, getResources().getString(R.string.match_section));
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        contentLayout.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        View view = View.inflate(mContext, R.layout.fragment_scrollball, null);
        contentLayout.addView(view);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        return container;
    }

    @Override
    public void onRefresh() {

    }
}
