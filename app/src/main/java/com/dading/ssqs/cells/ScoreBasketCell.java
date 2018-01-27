package com.dading.ssqs.cells;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.components.BasketBallNumberView;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.DateUtils;

/**
 * Created by mazhuang on 2018/1/27.
 */

public class ScoreBasketCell extends RelativeLayout {

    private LinearLayout imageLayout;
    private ImageView followImageView;

    private TextView matchNameTextView;

    private TextView homeNameTextView;
    private TextView awayNameTextView;

    private TextView tvTimeView;
    private TextView tvTypeView;
    private ImageView pointView;

    private AlphaAnimation mAlphaAnim;

    private BasketBallNumberView basketBallNumberView;

    public ScoreBasketCell(Context context) {
        super(context);

        setBackgroundResource(R.drawable.ic_score_bg);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFEDEDED);
        addView(lineView, new RelativeLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1));

        RelativeLayout container = new RelativeLayout(context);
        addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100));

        View centerLine = new View(context);
        centerLine.setId(R.id.score_basket_center);
        centerLine.setBackgroundColor(0xFFEDEDED);
        RelativeLayout.LayoutParams lineLP = new RelativeLayout.LayoutParams(1, LayoutHelper.MATCH_PARENT);
        lineLP.setMargins(0, AndroidUtilities.INSTANCE.dp(28), 0, AndroidUtilities.INSTANCE.dp(20));
        lineLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        container.addView(centerLine, lineLP);

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(10), 0);
        leftLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams leftLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        leftLP.addRule(RelativeLayout.LEFT_OF, centerLine.getId());
        container.addView(leftLayout, leftLP);

        imageLayout = new LinearLayout(context);
        imageLayout.setGravity(Gravity.CENTER);
        leftLayout.addView(imageLayout, LayoutHelper.createLinear(50, LayoutHelper.MATCH_PARENT));

        followImageView = new ImageView(context);
        followImageView.setImageResource(R.mipmap.guanzhu);
        followImageView.setScaleType(ImageView.ScaleType.CENTER);
        imageLayout.addView(followImageView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout nameLayout = new LinearLayout(context);
        nameLayout.setOrientation(LinearLayout.VERTICAL);
        leftLayout.addView(nameLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        matchNameTextView = new TextView(context);
        matchNameTextView.setTextSize(12);
        matchNameTextView.setSingleLine();
        matchNameTextView.setTextColor(AndroidUtilities.INSTANCE.getRandomColor());
        nameLayout.addView(matchNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0));

        LinearLayout nameInfoLayout = new LinearLayout(context);
        nameInfoLayout.setPadding(0, AndroidUtilities.INSTANCE.dp(10), 0, AndroidUtilities.INSTANCE.dp(20));
        nameInfoLayout.setOrientation(LinearLayout.VERTICAL);
        nameLayout.addView(nameInfoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        homeNameTextView = new TextView(context);
        homeNameTextView.setTextSize(15);
        homeNameTextView.setGravity(Gravity.CENTER_VERTICAL);
        homeNameTextView.setTextColor(0xFF323232);
        homeNameTextView.setSingleLine();
        homeNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        nameInfoLayout.addView(homeNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 0, 1f));

        awayNameTextView = new TextView(context);
        awayNameTextView.setGravity(Gravity.CENTER_VERTICAL);
        awayNameTextView.setTextSize(15);
        awayNameTextView.setTextColor(0xFF323232);
        awayNameTextView.setSingleLine();
        awayNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        nameInfoLayout.addView(awayNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 0, 1f));

        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setPadding(AndroidUtilities.INSTANCE.dp(10), 0, AndroidUtilities.INSTANCE.dp(15), 0);
        rightLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams rightLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        rightLP.addRule(RelativeLayout.RIGHT_OF, centerLine.getId());
        container.addView(rightLayout, rightLP);

        RelativeLayout rightTopLayout = new RelativeLayout(context);
        rightLayout.addView(rightTopLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0));

        tvTimeView = new TextView(context);
        tvTimeView.setTextSize(12);
        tvTimeView.setSingleLine();
        tvTimeView.setTextColor(0xFF8C8C8C);
        rightTopLayout.addView(tvTimeView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout typeLayout = new LinearLayout(context);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setGravity(Gravity.TOP);
        rightTopLayout.addView(typeLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        tvTypeView = new TextView(context);
        tvTypeView.setTextSize(12);
        tvTypeView.setTextColor(0xFFEE630F);
        tvTypeView.setText("第二节 06:46");
        typeLayout.addView(tvTypeView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        pointView = new ImageView(context);
        pointView.setImageResource(R.mipmap.ic_score_point);
        typeLayout.addView(pointView, LayoutHelper.createLinear(2, 4, 5, 0, 0, 0));

        mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnim.setDuration(500);
        mAlphaAnim.setRepeatCount(Animation.INFINITE);

        basketBallNumberView = new BasketBallNumberView(context);
        rightLayout.addView(basketBallNumberView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 0, 8, 0, 20));
    }

    public void setMatchName(String name) {
        matchNameTextView.setText(name);
    }

    public void setHomeName(String name) {
        homeNameTextView.setText(name);
    }

    public void setAwayName(String name) {
        awayNameTextView.setText(name);
    }

    public void setTime(String time) {
        time = DateUtils.changeFormater(time, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        tvTimeView.setText(time);
    }

    public void setMatchScore(String part1HScore, String part1AScore, String part2HScore, String part2AScore, String part3HScore, String part3AScore, String part4HScore, String part4AScore, String hOverTimeScore, String aOverTimeScore, String homeTotal, String awayTotal) {
        basketBallNumberView.setData(part1HScore, part1AScore, part2HScore, part2AScore, part3HScore, part3AScore, part4HScore, part4AScore, hOverTimeScore, aOverTimeScore, homeTotal, awayTotal);
    }

    public void setType(int isOver, String protime) {
        switch (isOver) {
            case 0:
                if (!TextUtils.isEmpty(protime)) {//如果protime为空不处理
                    if (!"半场".equals(protime)) {
                        tvTypeView.setText(protime);

                        if (pointView.getVisibility() == View.INVISIBLE) {
                            pointView.setVisibility(View.VISIBLE);
                        }

                        pointView.setAnimation(mAlphaAnim);
                        mAlphaAnim.start();
                    } else {
                        tvTypeView.setText(protime);

                        mAlphaAnim.cancel();
                        pointView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvTypeView.setText("赛前");

                    mAlphaAnim.cancel();
                    pointView.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                tvTypeView.setText("完场");

                mAlphaAnim.cancel();
                pointView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tvTypeView.setText("中断");

                mAlphaAnim.cancel();
                pointView.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tvTypeView.setText("半场");

                mAlphaAnim.cancel();
                pointView.setVisibility(View.INVISIBLE);

                if (!TextUtils.isEmpty(protime)) {//如果protime为空不处理
                    if (!"半场".equals(protime)) {
                        tvTypeView.setText(protime);

                        if (pointView.getVisibility() == View.INVISIBLE) {
                            pointView.setVisibility(View.VISIBLE);
                        }

                        pointView.setAnimation(mAlphaAnim);
                        mAlphaAnim.start();
                    }
                }
                break;
        }
    }

    public void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            followImageView.setImageResource(R.mipmap.guanzhu_highlight);
        } else {
            followImageView.setImageResource(R.mipmap.guanzhu);
        }
    }

    public void setFavoriteClickListener(final OnClickListener listener) {
        imageLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
