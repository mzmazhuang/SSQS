package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.ScrollBallItemAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/1.
 */

public class ScrollBallItemCell extends LinearLayout {

    private TextView timeTextView;
    private LinearLayout titleTextLayout;
    private TextView titleTextView;
    private LinearLayout pointLayout;
    private ImageView pointView;
    private TextView protTimeView;
    private List<ScrollBallDefaultFragment.MergeBean> focusList;
    private View lineView;
    private Context mContext;
    private LinearLayout cellLayout;
    private ScrollBallItemAdapter.OnItemClickListener listener;
    private int beanId;
    private AlphaAnimation mAlphaAnim;
    private ImageView allImageView;

    private List<ScrollBallItemChildCell> cells = new ArrayList<>();

    public void setFocus(List<ScrollBallDefaultFragment.MergeBean> list) {
        focusList = list;
    }

    public void setListener(ScrollBallItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ScrollBallItemCell(Context context) {
        super(context);
        mContext = context;

        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        //时间和标题
        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        timeTextView = new TextView(context);
        timeTextView.setTextSize(10);
        timeTextView.setTextColor(0xFFBDBDBD);
        topLayout.addView(timeTextView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 17, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        allImageView = new ImageView(context);
        allImageView.setId(R.id.scroll_all_play);
        allImageView.setImageResource(R.mipmap.ic_all_play);
        allImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAllClick(beanId);
                }
            }
        });
        topLayout.addView(allImageView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, 0, 0, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT));

        titleTextLayout = new LinearLayout(context);
        titleTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams titleLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        titleLP.addRule(RelativeLayout.LEFT_OF, allImageView.getId());
        topLayout.addView(titleTextLayout, titleLP);

        titleTextView = new TextView(context);
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(0xFF626262);
        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextView.setSingleLine();
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextLayout.addView(titleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        pointLayout = new LinearLayout(context);
        pointLayout.setVisibility(View.GONE);
        pointLayout.setOrientation(LinearLayout.HORIZONTAL);
        pointLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        pointLayout.setGravity(Gravity.LEFT);
        titleTextLayout.addView(pointLayout, LayoutHelper.createLinear(25, LayoutHelper.WRAP_CONTENT, 2, 6, 0, 0));

        protTimeView = new TextView(context);
        protTimeView.setTextSize(10);
        protTimeView.setTextColor(0xFFFF9600);
        protTimeView.setSingleLine();
        pointLayout.addView(protTimeView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        pointView = new ImageView(context);
        pointView.setScaleType(ImageView.ScaleType.FIT_XY);
        pointView.setImageResource(R.mipmap.ic_guessball_scroll_point);
        pointLayout.addView(pointView, LayoutHelper.createLinear(3, 6));

        //一级标题 如   场次/胜平负/让球/大小/单双
        LinearLayout titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 42));

        LinearLayout title1Layout = new LinearLayout(context);
        title1Layout.setBackgroundColor(0xFF009BDB);
        title1Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title1Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        TextView title1 = new TextView(context);
        title1.setTextColor(Color.WHITE);
        title1.setTextSize(12);
        title1.setText(LocaleController.getString(R.string.scroll_title6));
        title1Layout.addView(title1, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view1 = new View(context);
        view1.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view1, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title2Layout = new LinearLayout(context);
        title2Layout.setBackgroundColor(0xFF009BDB);
        title2Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title2Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        TextView title2 = new TextView(context);
        title2.setTextColor(Color.WHITE);
        title2.setTextSize(12);
        title2.setText(LocaleController.getString(R.string.scroll_title7));
        title2Layout.addView(title2, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view2 = new View(context);
        view2.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view2, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title3Layout = new LinearLayout(context);
        title3Layout.setBackgroundColor(0xFF009BDB);
        title3Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title3Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        TextView title3 = new TextView(context);
        title3.setTextColor(Color.WHITE);
        title3.setTextSize(12);
        title3.setText(LocaleController.getString(R.string.scroll_title8));
        title3Layout.addView(title3, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view3 = new View(context);
        view3.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view3, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        LinearLayout title4Layout = new LinearLayout(context);
        title4Layout.setBackgroundColor(0xFF009BDB);
        title4Layout.setGravity(Gravity.CENTER);
        titleLayout.addView(title4Layout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        TextView title4 = new TextView(context);
        title4.setTextColor(Color.WHITE);
        title4.setTextSize(12);
        title4.setText(LocaleController.getString(R.string.scroll_title9));
        title4Layout.addView(title4, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View view4 = new View(context);
        view4.setBackgroundColor(0xFFE7E7E7);
        titleLayout.addView(view4, LayoutHelper.createLinear(1, LayoutHelper.MATCH_PARENT));

        TextView title5 = new TextView(context);
        title5.setTextColor(Color.WHITE);
        title5.setTextSize(12);
        title5.setGravity(Gravity.CENTER);
        title5.setBackgroundColor(0xFF009BDB);
        title5.setText(LocaleController.getString(R.string.scroll_title10));
        titleLayout.addView(title5, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 3f));

        //中间的内容   全场/半场  数字
        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 180));

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.addView(leftLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        RelativeLayout leftTopLayout = new RelativeLayout(context);
        leftLayout.addView(leftTopLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 91));

        TextView title6 = new TextView(context);
        title6.setTextSize(13);
        title6.setTextColor(0xFF222222);
        title6.setText(LocaleController.getString(R.string.scroll_title11));
        leftTopLayout.addView(title6, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        View view7 = new View(context);
        view7.setBackgroundColor(0xFFE7E7E7);
        leftTopLayout.addView(view7, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1, RelativeLayout.ALIGN_PARENT_BOTTOM));

        View view8 = new View(context);
        view8.setBackgroundColor(0xFFE7E7E7);
        leftTopLayout.addView(view8, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        RelativeLayout leftBottomLayout = new RelativeLayout(context);
        leftLayout.addView(leftBottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 89));

        TextView title7 = new TextView(context);
        title7.setTextSize(13);
        title7.setTextColor(0xFF222222);
        title7.setText(LocaleController.getString(R.string.scroll_title12));
        leftBottomLayout.addView(title7, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        View view9 = new View(context);
        view9.setBackgroundColor(0xFFE7E7E7);
        leftBottomLayout.addView(view9, LayoutHelper.createRelative(1, LayoutHelper.MATCH_PARENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        cellLayout = new LinearLayout(context);
        cellLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.addView(cellLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 11f));

        for (int i = 0; i < 6; i++) {

            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            cellLayout.addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
            for (int j = 0; j < 4; j++) {
                float weight = 3;

                if (j == 0) {
                    weight = 2;
                }
                ScrollBallItemChildCell cell = new ScrollBallItemChildCell(mContext);
                layout.addView(cell, LayoutHelper.createLinear(0, 30, weight));
                cells.add(cell);
            }
        }

        lineView = new View(context);
        lineView.setBackgroundColor(0xFFE4E4E4);
        addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 5));
    }

    private void setItemChild(List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> list, ScrollBallFootBallBean.ScrollBeanItems items) {
        if (list.size() == cells.size()) {
            for (int i = 0; i < list.size(); i++) {
                ScrollBallItemChildCell cell = cells.get(i);
                cell.setOnClickListener(listener);
                cell.setData(list.get(i), items, beanId, focusList, i);
            }
        }
    }

    private void setTime(String time) {
        timeTextView.setText(time);
    }

    private void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (title.contains("<font")) {
                titleTextView.setText(Html.fromHtml(title));
            } else {
                titleTextView.setText(title);
            }
        }
    }

    public void setBeanId(int id) {
        this.beanId = id;
    }

    public void setData(ScrollBallFootBallBean.ScrollBeanItems bean, int page) {
        setTime(bean.getTime());

        LinearLayout.LayoutParams layoutParams = (LayoutParams) titleTextView.getLayoutParams();

        if (page == 3) {//早盘页面
            titleTextLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(75f), 0);

            layoutParams.setMargins(AndroidUtilities.INSTANCE.dp(75f), 0, 0, 0);
        } else if (page == 2) {//今日
            titleTextLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(60f), 0);

            layoutParams.setMargins(AndroidUtilities.INSTANCE.dp(60f), 0, 0, 0);
        } else {//滚球
            titleTextLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(5f), 0);

            layoutParams.setMargins(AndroidUtilities.INSTANCE.dp(60f), 0, 0, 0);
        }

        titleTextView.setLayoutParams(layoutParams);

        if (page != 1) {
            setTitle(bean.getTitle() + "　VS　" + bean.getByTitle());

            allImageView.setVisibility(View.GONE);

            if (mAlphaAnim != null) {
                mAlphaAnim.cancel();
            }
        } else {
            allImageView.setVisibility(View.VISIBLE);

            setTitle("<font color=\"#626262\">" + bean.getTitle() + "</font>&nbsp;&nbsp;<font color=\"#1FA605\">" + bean.gethScore() + "-" + bean.getaScore() + "</font>&nbsp;&nbsp;<font color=\"#626262\">" + bean.getByTitle() + "</font>");

            if (!TextUtils.isEmpty(bean.getProtTime())) {
                pointLayout.setVisibility(View.VISIBLE);

                protTimeView.setText(bean.getProtTime());

                mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
                mAlphaAnim.setDuration(500);
                mAlphaAnim.setRepeatCount(Animation.INFINITE);
                pointView.setAnimation(mAlphaAnim);

                mAlphaAnim.start();
            }
        }
        setItemChild(bean.getTestItems(), bean);
    }
}
