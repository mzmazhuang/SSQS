package com.dading.ssqs.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.fragment.guesstheball.EarlyFragment;
import com.dading.ssqs.fragment.guesstheball.ScrollBallFragment;
import com.dading.ssqs.fragment.guesstheball.ToDayMatchFragment;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class GuessTheBallFragment extends Fragment {

    private Context mContext;
    private TextView scrollBallTextView;
    private TextView toDayTextView;
    private TextView earlyTextView;

    private ScrollBallFragment scrollBallFragment;
    private ToDayMatchFragment toDayMatchFragment;
    private EarlyFragment earlyFragment;

    private FragmentManager fragmentManager;

    private boolean hasInit = false;

    private GuessBallType currType = GuessBallType.SCROLLBALL;//当前的页面

    private int type = -1;

    private enum GuessBallType {
        SCROLLBALL,//滚球
        TODAY,//今日赛事
        Early//早盘
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        return initView();
    }

    //构建页面布局
    private View initView() {
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.home_top_blue));
        titleLayout.setGravity(Gravity.CENTER);
        container.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        LinearLayout titleTextLayout = new LinearLayout(mContext);
        titleTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.addView(titleTextLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        scrollBallTextView = new TextView(mContext);
        scrollBallTextView.setGravity(Gravity.CENTER_VERTICAL);
        scrollBallTextView.setTextColor(Color.WHITE);
        scrollBallTextView.setTextSize(18);
        scrollBallTextView.setText(LocaleController.getString(R.string.scroll_ball));
        scrollBallTextView.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);
        scrollBallTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageTextColor(GuessBallType.SCROLLBALL, false);
            }
        });
        titleTextLayout.addView(scrollBallTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        toDayTextView = new TextView(mContext);
        toDayTextView.setGravity(Gravity.CENTER_VERTICAL);
        toDayTextView.setTextColor(0xFF00435E);
        toDayTextView.setTextSize(18);
        toDayTextView.setText(LocaleController.getString(R.string.today_match));
        toDayTextView.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);
        toDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageTextColor(GuessBallType.TODAY, false);
            }
        });
        titleTextLayout.addView(toDayTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        earlyTextView = new TextView(mContext);
        earlyTextView.setGravity(Gravity.CENTER_VERTICAL);
        earlyTextView.setTextColor(0xFF00435E);
        earlyTextView.setTextSize(18);
        earlyTextView.setText(LocaleController.getString(R.string.early));
        earlyTextView.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);
        earlyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePageTextColor(GuessBallType.Early, false);
            }
        });
        titleTextLayout.addView(earlyTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        LinearLayout parentLayout = new LinearLayout(mContext);//用来替换fragment的布局
        parentLayout.setId(R.id.guess_parent);
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        return container;
    }

    private void init() {
        scrollBallFragment = new ScrollBallFragment();

        fragmentManager = getChildFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.guess_parent, scrollBallFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true;
                init();
            }
        }
    }

    //改变title文字颜色
    private void changePageTextColor(GuessBallType type, boolean checkPage) {
        if (this.currType != type) {
            this.currType = type;

            clearTextColor();

            if (type == GuessBallType.SCROLLBALL) {
                scrollBallTextView.setTextColor(Color.WHITE);
            } else if (type == GuessBallType.TODAY) {
                toDayTextView.setTextColor(Color.WHITE);
            } else if (type == GuessBallType.Early) {
                earlyTextView.setTextColor(Color.WHITE);
            }

            changePage(type);
        }

        if (checkPage) {
            changePage(type);
        }
    }

    public void setType(int type) {
        this.type = type;

        changePageTextColor(GuessBallType.SCROLLBALL, true);
    }

    public void fragmentResume() {
        if (currType == GuessBallType.SCROLLBALL) {
            if (scrollBallFragment != null) {
                scrollBallFragment.fragmentResume();
            }
        } else if (currType == GuessBallType.TODAY) {
            if (toDayMatchFragment != null) {
                toDayMatchFragment.fragmentResume();
            }
        } else if (currType == GuessBallType.Early) {
            if (earlyFragment != null) {
                earlyFragment.fragmentResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentResume();
    }

    public void fragmentPause() {
        if (currType == GuessBallType.SCROLLBALL) {
            if (scrollBallFragment != null) {
                scrollBallFragment.fragmentPause();
            }
        } else if (currType == GuessBallType.TODAY) {
            if (toDayMatchFragment != null) {
                toDayMatchFragment.fragmentPause();
            }
        } else if (currType == GuessBallType.Early) {
            if (earlyFragment != null) {
                earlyFragment.fragmentPause();
            }
        }
    }

    //fragment 试图切换
    private void changePage(GuessBallType type) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (scrollBallFragment != null) {
            fragmentTransaction.hide(scrollBallFragment);
            scrollBallFragment.fragmentPause();
        }
        if (toDayMatchFragment != null) {
            fragmentTransaction.hide(toDayMatchFragment);
            toDayMatchFragment.fragmentPause();
        }
        if (earlyFragment != null) {
            fragmentTransaction.hide(earlyFragment);
            earlyFragment.fragmentPause();
        }

        if (type == GuessBallType.SCROLLBALL) {
            if (scrollBallFragment == null) {
                scrollBallFragment = new ScrollBallFragment();
                fragmentTransaction.add(R.id.guess_parent, scrollBallFragment);
            } else {
                scrollBallFragment.fragmentResume();
                fragmentTransaction.show(scrollBallFragment);
            }
            if (this.type > 0) {
                if (this.type == 1) {
                    scrollBallFragment.selectFootBall();
                } else if (this.type == 2) {
                    scrollBallFragment.selectBasketBall();
                }
            }
        } else if (type == GuessBallType.TODAY) {
            if (toDayMatchFragment == null) {
                toDayMatchFragment = new ToDayMatchFragment();
                fragmentTransaction.add(R.id.guess_parent, toDayMatchFragment);
            } else {
                toDayMatchFragment.fragmentResume();
                fragmentTransaction.show(toDayMatchFragment);
            }
        } else if (type == GuessBallType.Early) {
            if (earlyFragment == null) {
                earlyFragment = new EarlyFragment();
                fragmentTransaction.add(R.id.guess_parent, earlyFragment);
            } else {
                earlyFragment.fragmentResume();
                fragmentTransaction.show(earlyFragment);
            }
        }
        fragmentTransaction.commit();
    }

    //清除标题字体颜色
    private void clearTextColor() {
        scrollBallTextView.setTextColor(0xFF00435E);
        toDayTextView.setTextColor(0xFF00435E);
        earlyTextView.setTextColor(0xFF00435E);
    }
}
