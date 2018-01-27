package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.DateUtils;

import java.util.Random;

/**
 * Created by mazhuang on 2018/1/26.
 */

public class ScoreFootCell extends RelativeLayout {

    private LinearLayout imageLayout;
    private ImageView followImageView;
    private TextView matchNameTextView;

    private TextView scoreTopTextView;//(0-0)
    private TextView scoreTextView;//2-1

    private TextView homeYellowTextView;//主队黄牌
    private TextView homeRedTextView;//主队红牌
    private TextView homeNumberTextView;//[]里面的值
    private TextView homeNameTextView;//name

    private TextView typeTextView;//中场 时间
    private ImageView pointImageView;
    private TextView awayYellowTextView;
    private TextView awayRedTextView;
    private TextView awayNumberTextView;
    private TextView awayNameTextView;

    private TextView tvTime;

    private AlphaAnimation mAlphaAnim;

    public ScoreFootCell(Context context) {
        super(context);

        setBackgroundResource(R.drawable.ic_score_bg);
        setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFEDEDED);
        addView(lineView, new RelativeLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1));

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 80));

        imageLayout = new LinearLayout(context);
        imageLayout.setGravity(Gravity.CENTER);
        container.addView(imageLayout, LayoutHelper.createLinear(50, LayoutHelper.MATCH_PARENT));

        followImageView = new ImageView(context);
        followImageView.setImageResource(R.mipmap.guanzhu);
        followImageView.setScaleType(ImageView.ScaleType.CENTER);
        imageLayout.addView(followImageView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        leftLayout.setGravity(Gravity.RIGHT);
        leftLayout.setPadding(0, 0, AndroidUtilities.INSTANCE.dp(5), 0);
        container.addView(leftLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 5f));

        matchNameTextView = new TextView(context);
        matchNameTextView.setTextSize(12);
        matchNameTextView.setSingleLine();
        matchNameTextView.setTextColor(AndroidUtilities.INSTANCE.getRandomColor());
        leftLayout.addView(matchNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 8));

        LinearLayout homeInfoLayout = new LinearLayout(context);
        homeInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
        homeInfoLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftLayout.addView(homeInfoLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        homeYellowTextView = new TextView(context);
        homeYellowTextView.setPadding(AndroidUtilities.INSTANCE.dp(2), 0, AndroidUtilities.INSTANCE.dp(2), 0);
        homeYellowTextView.setBackgroundResource(R.drawable.bg_score_yellow);
        homeYellowTextView.setTextColor(Color.WHITE);
        homeYellowTextView.setTextSize(10);
        homeInfoLayout.addView(homeYellowTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        homeRedTextView = new TextView(context);
        homeRedTextView.setPadding(AndroidUtilities.INSTANCE.dp(2), 0, AndroidUtilities.INSTANCE.dp(2), 0);
        homeRedTextView.setBackgroundResource(R.drawable.bg_score_red);
        homeRedTextView.setTextColor(Color.WHITE);
        homeRedTextView.setTextSize(10);
        homeInfoLayout.addView(homeRedTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 0, 0));

        homeNumberTextView = new TextView(context);
        homeNumberTextView.setTextSize(10);
        homeNumberTextView.setTextColor(0xFF737373);
        homeInfoLayout.addView(homeNumberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 0, 0));

        homeNameTextView = new TextView(context);
        homeNameTextView.setTextSize(15);
        homeNameTextView.setTextColor(0xFF323232);
        homeNameTextView.setSingleLine();
        homeNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        homeInfoLayout.addView(homeNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 0, 0));

        tvTime = new TextView(context);
        tvTime.setTextSize(12);
        tvTime.setTextColor(0xFF8C8C8C);
        leftLayout.addView(tvTime, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 5, 0, 0));

        LinearLayout numberLayout = new LinearLayout(context);
        numberLayout.setOrientation(LinearLayout.VERTICAL);
        numberLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(numberLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

        scoreTopTextView = new TextView(context);
        scoreTopTextView.setTextSize(12);
        scoreTopTextView.setTextColor(0xFF8C8C8C);
        numberLayout.addView(scoreTopTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 8));

        scoreTextView = new TextView(context);
        scoreTextView.setTextSize(16);
        scoreTextView.setTextColor(0xFF309F0E);
        scoreTextView.setTypeface(Typeface.DEFAULT_BOLD);
        numberLayout.addView(scoreTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setPadding(AndroidUtilities.INSTANCE.dp(5), 0, 0, 0);
        rightLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(rightLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 7f));

        RelativeLayout rightTopLayout = new RelativeLayout(context);
        rightLayout.addView(rightTopLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 8, 15, 8));

        LinearLayout typeLayout = new LinearLayout(context);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setGravity(Gravity.TOP);
        rightTopLayout.addView(typeLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

        typeTextView = new TextView(context);
        typeTextView.setTextSize(12);
        typeTextView.setTextColor(0xFFEE630F);
        typeLayout.addView(typeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        pointImageView = new ImageView(context);
        pointImageView.setImageResource(R.mipmap.ic_score_point);
        typeLayout.addView(pointImageView, LayoutHelper.createLinear(2, 4, 5, 0, 0, 0));

        mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnim.setDuration(500);
        mAlphaAnim.setRepeatCount(Animation.INFINITE);

        LinearLayout rightImageLayout = new LinearLayout(context);
        rightImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        rightTopLayout.addView(rightImageLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT));

        ImageView imageView1 = new ImageView(context);
        imageView1.setImageResource(R.mipmap.findball_rect);
        rightImageLayout.addView(imageView1, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        ImageView imageView2 = new ImageView(context);
        imageView2.setImageResource(R.mipmap.green_rect);
        rightImageLayout.addView(imageView2, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 8, 0, 0, 0));

        LinearLayout awayInfoLayout = new LinearLayout(context);
        awayInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
        awayInfoLayout.setGravity(Gravity.CENTER_VERTICAL);
        rightLayout.addView(awayInfoLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        awayNameTextView = new TextView(context);
        awayNameTextView.setTextSize(15);
        awayNameTextView.setTextColor(0xFF323232);
        awayNameTextView.setSingleLine();
        awayNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        awayNameTextView.setMaxWidth(AndroidUtilities.INSTANCE.dp(100));
        awayInfoLayout.addView(awayNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        awayNumberTextView = new TextView(context);
        awayNumberTextView.setTextSize(10);
        awayNumberTextView.setTextColor(0xFF737373);
        awayInfoLayout.addView(awayNumberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 5, 0, 5, 0));

        awayYellowTextView = new TextView(context);
        awayYellowTextView.setPadding(AndroidUtilities.INSTANCE.dp(2), 0, AndroidUtilities.INSTANCE.dp(2), 0);
        awayYellowTextView.setBackgroundResource(R.drawable.bg_score_yellow);
        awayYellowTextView.setTextColor(Color.WHITE);
        awayYellowTextView.setTextSize(10);
        awayInfoLayout.addView(awayYellowTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 5, 0));

        awayRedTextView = new TextView(context);
        awayRedTextView.setPadding(AndroidUtilities.INSTANCE.dp(2), 0, AndroidUtilities.INSTANCE.dp(2), 0);
        awayRedTextView.setBackgroundResource(R.drawable.bg_score_red);
        awayRedTextView.setTextColor(Color.WHITE);
        awayRedTextView.setTextSize(10);
        awayInfoLayout.addView(awayRedTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public void setMatchName(String matchName) {
        matchNameTextView.setText(matchName);
    }

    public void setScoreTopText(String hScore, String aScore) {
        scoreTopTextView.setText("(" + hScore + "-" + aScore + ")");
    }

    public void setScoreText(String score) {
        scoreTextView.setText(score);
    }

    public void setHomeName(String name) {
        homeNameTextView.setText(name);
    }

    public void setHomeNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            homeNumberTextView.setText("[" + number + "]");
        } else {
            homeNumberTextView.setText("[0]");
        }
    }

    public void setHomeYellow(int number) {
        if (number > 0) {
            homeYellowTextView.setVisibility(View.VISIBLE);
            homeYellowTextView.setText(number);
        } else {
            homeYellowTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setHomeRed(int number) {
        if (number > 0) {
            homeRedTextView.setVisibility(View.VISIBLE);
            homeRedTextView.setText(number);
        } else {
            homeRedTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setTime(String time, boolean isMonth) {
        if (isMonth) {
            time = DateUtils.changeFormater(time, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm");
        } else {
            time = DateUtils.changeFormater(time, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        }
        tvTime.setText(time);
    }

    public void setType(int isOver, String protime) {
        switch (isOver) {
            case 0:
                if (!TextUtils.isEmpty(protime)) {//如果protime为空不处理
                    if (!"半场".equals(protime)) {
                        typeTextView.setText(protime);

                        if (pointImageView.getVisibility() == View.INVISIBLE) {
                            pointImageView.setVisibility(View.VISIBLE);
                        }

                        pointImageView.setAnimation(mAlphaAnim);
                        mAlphaAnim.start();
                    } else {
                        typeTextView.setText(protime);

                        mAlphaAnim.cancel();
                        pointImageView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    typeTextView.setText("赛前");

                    mAlphaAnim.cancel();
                    pointImageView.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                typeTextView.setText("完场");

                mAlphaAnim.cancel();
                pointImageView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                typeTextView.setText("中断");

                mAlphaAnim.cancel();
                pointImageView.setVisibility(View.INVISIBLE);
                break;
            case 3:
                typeTextView.setText("半场");
                
                mAlphaAnim.cancel();
                pointImageView.setVisibility(View.INVISIBLE);

                if (!TextUtils.isEmpty(protime)) {//如果protime为空不处理
                    if (!"半场".equals(protime)) {
                        typeTextView.setText(protime);

                        if (pointImageView.getVisibility() == View.INVISIBLE) {
                            pointImageView.setVisibility(View.VISIBLE);
                        }

                        pointImageView.setAnimation(mAlphaAnim);
                        mAlphaAnim.start();
                    }
                }
                break;
        }
    }

    public void setAwayName(String name) {
        awayNameTextView.setText(name);
    }

    public void setAwayNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            awayNumberTextView.setText("[" + number + "]");
        } else {
            awayNumberTextView.setText("[0]");
        }
    }

    public void setAwayYellow(int number) {
        if (number > 0) {
            awayYellowTextView.setVisibility(View.VISIBLE);
            awayYellowTextView.setText(number);
        } else {
            awayYellowTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setAwayRed(int number) {
        if (number > 0) {
            awayRedTextView.setVisibility(View.VISIBLE);
            awayRedTextView.setText(number);
        } else {
            awayRedTextView.setVisibility(View.INVISIBLE);
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
