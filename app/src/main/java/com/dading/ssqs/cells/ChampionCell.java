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
import com.dading.ssqs.adapter.newAdapter.ChampionAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.List;


/**
 * Created by mazhuang on 2017/12/4.\
 * 冠军cell
 */

public class ChampionCell extends LinearLayout {

    private Context mContext;
    private ImageView arrowImageView;
    private TextView tvTitle;
    private LinearLayout itemLayout;
    private ChampionAdapter adapter;

    public void setFocus(List<ToDayFootBallChampionFragment.MergeBean> list) {
        adapter.setFocus(list);
    }

    public ChampionCell(Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setPadding(AndroidUtilities.INSTANCE.dp(12), AndroidUtilities.INSTANCE.dp(5), 0, AndroidUtilities.INSTANCE.dp(5));
        topLayout.setBackgroundColor(0xFFDEF5FF);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.setOnClickListener(listener);
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

        adapter = new ChampionAdapter(context);
        recyclerView.setAdapter(adapter);

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFE7E7E7);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));
    }

    public void setListener(ChampionChildCell.OnClickListener listener) {
        adapter.setListener(listener);
    }

    public void setData(ChampionBean bean) {
        adapter.setTitle(bean.getCommonTitle().getTitle());
        adapter.setList(bean.getItems());
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (itemLayout.getVisibility() == View.GONE) {
                itemLayout.setVisibility(View.VISIBLE);

                arrowImageView.setImageResource(R.mipmap.ic_jiantou_down);
            } else {
                itemLayout.setVisibility(View.GONE);
                arrowImageView.setImageResource(R.mipmap.ic_jiantou_right);
            }
        }
    };
}
