package com.dading.ssqs.fragment.guesstheball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.GuessBallTopAdapter;
import com.dading.ssqs.adapter.newAdapter.GuessBallTopSubAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GuessTopTitle;
import com.dading.ssqs.cells.GuessBallTopCell;
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallChampionFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallPassFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallPassFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayHalfCourtFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayResultFragment;
import com.dading.ssqs.fragment.guesstheball.today.ToDayTotalFragment;
import com.dading.ssqs.fragment.guesstheball.today.TodayBoDanFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/30.
 * 今日赛事
 * <p>
 * 因有多个页面比较类似，但是考虑到后续如果不同的页面做了一些功能上的改动
 * 对开发不利，把每个功能都拆成一个页面，方便用于后续功能维护、开发
 */

public class ToDayMatchFragment extends Fragment implements NotificationController.NotificationControllerDelegate {

    private Context mContext;

    private GuessBallTopCell topCell;
    private TextView tvTitle;

    private List<GuessTopTitle> footBallSubTitles = new ArrayList<>();
    private List<GuessTopTitle> basketBallSubTitles = new ArrayList<>();

    private int currTitlePosition = 1;//一级标题position 默认足球
    private int twoTitleFootPosition = 0;//二级标题足球的position  默认第一个选中
    private int twoTitleBasketPosition = -1;//二级标题篮球的position

    private Calendar calendar = Calendar.getInstance();

    private FragmentManager fragmentManager;

    private ToDayDefaultFragment defaultFragment;
    private TodayBoDanFragment boDanFragment;
    private ToDayTotalFragment toDayTotalFragment;
    private ToDayHalfCourtFragment halfCourtFragment;
    private ToDayResultFragment resultFragment;

    private ToDayBasketBallDefaultFragment basketBallDefaultFragment;
    private ToDayFootBallChampionFragment toDayChampionFragment;
    private ToDayBasketBallChampionFragment toDayBasketBallChampionFragment;
    private ToDayFootBallPassFragment basketBallPassFragment;
    private ToDayBasketBallPassFragment footBallPassFragment;

    private View maskView;

    private int currPage = 1;//当前页面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.today_mask);

        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationController.getInstance().removeObserver(this, NotificationController.today_mask);
    }

    private View initView() {
        calendar.setTimeInMillis(System.currentTimeMillis());

        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout maskLayout = new RelativeLayout(mContext);
        container.addView(maskLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        maskLayout.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        maskView = new View(mContext);
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMask();
                NotificationController.getInstance().postNotification(NotificationController.today_mask, "child_close");
            }
        });
        maskView.setVisibility(View.GONE);
        maskView.setBackgroundColor(0xA5000000);
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100));

        topCell = new GuessBallTopCell(mContext);
        topCell.setTopListener(topClickListener);
        topCell.setSubTopListener(subTitleClickListener);
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setBackgroundColor(0xFF00425D);
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        tvTitle = new TextView(mContext);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setTextSize(13);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText(LocaleController.getString(R.string.scroll_title15) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1));
        tvTitle.setGravity(Gravity.CENTER);
        titleLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 30, 12, 0, 0, 0));

        LinearLayout parentLayout = new LinearLayout(mContext);//用来替换fragment的布局
        parentLayout.setId(R.id.today_parent);
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        init();
        return container;
    }

    private void initTitles() {
        GuessTopTitle subTitle1 = new GuessTopTitle();
        subTitle1.setId(3);
        subTitle1.setName(LocaleController.getString(R.string.scroll_title1));

        GuessTopTitle subTitle2 = new GuessTopTitle();
        subTitle2.setId(4);
        subTitle2.setName(LocaleController.getString(R.string.scroll_title2));

        GuessTopTitle subTitle3 = new GuessTopTitle();
        subTitle3.setId(5);
        subTitle3.setName(LocaleController.getString(R.string.scroll_title3));

        GuessTopTitle subTitle4 = new GuessTopTitle();
        subTitle4.setId(6);
        subTitle4.setName(LocaleController.getString(R.string.scroll_title4));

        GuessTopTitle subTitle5 = new GuessTopTitle();
        subTitle5.setId(7);
        subTitle5.setName(LocaleController.getString(R.string.scroll_title13));

        GuessTopTitle subTitle6 = new GuessTopTitle();
        subTitle6.setId(8);
        subTitle6.setName(LocaleController.getString(R.string.scroll_title14));

        GuessTopTitle subTitle7 = new GuessTopTitle();
        subTitle7.setId(9);
        subTitle7.setName(LocaleController.getString(R.string.scroll_title5));

        footBallSubTitles.add(subTitle1);
        footBallSubTitles.add(subTitle2);
        footBallSubTitles.add(subTitle3);
        footBallSubTitles.add(subTitle4);
        footBallSubTitles.add(subTitle5);
        footBallSubTitles.add(subTitle6);
        footBallSubTitles.add(subTitle7);

        basketBallSubTitles.add(subTitle1);
        basketBallSubTitles.add(subTitle5);
        basketBallSubTitles.add(subTitle6);

        //一级标题
        List<GuessTopTitle> topList = new ArrayList<>();

        GuessTopTitle title1 = new GuessTopTitle();
        title1.setId(1);
        title1.setName(LocaleController.getString(R.string.football));
        title1.setCount(7);

        GuessTopTitle title2 = new GuessTopTitle();
        title2.setId(2);
        title2.setName(LocaleController.getString(R.string.basketball));
        title2.setCount(3);

        topList.add(title1);
        topList.add(title2);

        topCell.setTopTitleData(topList);
        topCell.setTopSubTitleData(footBallSubTitles);
    }

    private void init() {
        initTitles();

        defaultFragment = new ToDayDefaultFragment();

        fragmentManager = getChildFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.today_parent, defaultFragment);
        fragmentTransaction.commit();
    }

    //一级标题点击事件
    private GuessBallTopAdapter.OnGuessTopClickListener topClickListener = new GuessBallTopAdapter.OnGuessTopClickListener() {
        @Override
        public void onClick(int id) {
            if (id == 1) {//足球
                currTitlePosition = 1;
                topCell.setTopSubTitleData(footBallSubTitles);
                topCell.setTopSubTitleSelect(twoTitleFootPosition);
            } else if (id == 2) {//篮球
                currTitlePosition = 2;
                topCell.setTopSubTitleData(basketBallSubTitles);
                topCell.setTopSubTitleSelect(twoTitleBasketPosition);
            }
        }
    };

    public void fragmentResume() {
        if (currPage == 1 && defaultFragment != null) {
            defaultFragment.filterResume();
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment.filterResume();
        } else if (currPage == 3 && toDayTotalFragment != null) {
            toDayTotalFragment.filterResume();
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment.filterResume();
        } else if (currPage == 5 && toDayChampionFragment != null) {
            toDayChampionFragment.filterResume();
        } else if (currPage == 6 && basketBallPassFragment != null) {
            basketBallPassFragment.filterResume();
        } else if (currPage == 7 && resultFragment != null) {
            resultFragment.filterResume();
        } else if (currPage == 8 && basketBallDefaultFragment != null) {
            basketBallDefaultFragment.filterResume();
        } else if (currPage == 9 && toDayBasketBallChampionFragment != null) {
            toDayBasketBallChampionFragment.filterResume();
        } else if (currPage == 10 && footBallPassFragment != null) {
            footBallPassFragment.filterResume();
        }
    }

    public void fragmentPause() {
        if (currPage == 1 && defaultFragment != null) {
            defaultFragment.filterPause();
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment.filterPause();
        } else if (currPage == 3 && toDayTotalFragment != null) {
            toDayTotalFragment.filterPause();
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment.filterPause();
        } else if (currPage == 5 && toDayChampionFragment != null) {
            toDayChampionFragment.filterPause();
        } else if (currPage == 6 && basketBallPassFragment != null) {
            basketBallPassFragment.filterPause();
        } else if (currPage == 7 && resultFragment != null) {
            resultFragment.filterPause();
        } else if (currPage == 8 && basketBallDefaultFragment != null) {
            basketBallDefaultFragment.filterPause();
        } else if (currPage == 9 && toDayBasketBallChampionFragment != null) {
            toDayBasketBallChampionFragment.filterPause();
        } else if (currPage == 10 && footBallPassFragment != null) {
            footBallPassFragment.filterPause();
        }
    }

    //二级标题点击事件
    private GuessBallTopSubAdapter.OnGuessSubTitleClickListener subTitleClickListener = new GuessBallTopSubAdapter.OnGuessSubTitleClickListener() {
        @Override
        public void onClick(int id, int position) {
            if (currTitlePosition == 1) {
                twoTitleFootPosition = position;
                twoTitleBasketPosition = -1;
            } else {
                twoTitleBasketPosition = position;
                twoTitleFootPosition = -1;
            }

            String str = "";
            for (int i = 0; i < footBallSubTitles.size(); i++) {
                if (footBallSubTitles.get(i).getId() == id) {
                    str = footBallSubTitles.get(i).getName();
                    break;
                }
            }

            tvTitle.setText("今日-" + (currTitlePosition == 1 ? "足球" : "篮球") + ":" + str);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (defaultFragment != null) {
                defaultFragment.filterPause();
                fragmentTransaction.hide(defaultFragment);
            }
            if (boDanFragment != null) {
                boDanFragment.filterPause();
                fragmentTransaction.hide(boDanFragment);
            }
            if (toDayTotalFragment != null) {
                toDayTotalFragment.filterPause();
                fragmentTransaction.hide(toDayTotalFragment);
            }
            if (halfCourtFragment != null) {
                halfCourtFragment.filterPause();
                fragmentTransaction.hide(halfCourtFragment);
            }
            if (toDayChampionFragment != null) {
                toDayChampionFragment.filterPause();
                fragmentTransaction.hide(toDayChampionFragment);
            }
            if (toDayBasketBallChampionFragment != null) {
                toDayBasketBallChampionFragment.filterPause();
                fragmentTransaction.hide(toDayBasketBallChampionFragment);
            }
            if (basketBallDefaultFragment != null) {
                basketBallDefaultFragment.filterPause();
                fragmentTransaction.hide(basketBallDefaultFragment);
            }
            if (resultFragment != null) {
                resultFragment.filterPause();
                fragmentTransaction.hide(resultFragment);
            }
            if (basketBallPassFragment != null) {
                basketBallPassFragment.filterPause();
                fragmentTransaction.hide(basketBallPassFragment);
            }
            if (footBallPassFragment != null) {
                footBallPassFragment.filterPause();
                fragmentTransaction.hide(footBallPassFragment);
            }

            if (currTitlePosition == 1) {//足球
                if (str.equals(LocaleController.getString(R.string.scroll_title1))) {
                    if (defaultFragment == null) {
                        defaultFragment = new ToDayDefaultFragment();
                        fragmentTransaction.add(R.id.today_parent, defaultFragment);
                    } else {
                        defaultFragment.filterResume();
                        fragmentTransaction.show(defaultFragment);
                    }
                    currPage = 1;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title2))) {
                    if (boDanFragment == null) {
                        boDanFragment = new TodayBoDanFragment();
                        fragmentTransaction.add(R.id.today_parent, boDanFragment);
                    } else {
                        boDanFragment.filterResume();
                        fragmentTransaction.show(boDanFragment);
                    }
                    currPage = 2;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title3))) {
                    if (toDayTotalFragment == null) {
                        toDayTotalFragment = new ToDayTotalFragment();
                        fragmentTransaction.add(R.id.today_parent, toDayTotalFragment);
                    } else {
                        toDayTotalFragment.filterResume();
                        fragmentTransaction.show(toDayTotalFragment);
                    }
                    currPage = 3;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title4))) {
                    if (halfCourtFragment == null) {
                        halfCourtFragment = new ToDayHalfCourtFragment();
                        fragmentTransaction.add(R.id.today_parent, halfCourtFragment);
                    } else {
                        halfCourtFragment.filterResume();
                        fragmentTransaction.show(halfCourtFragment);
                    }
                    currPage = 4;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title13))) {
                    if (toDayChampionFragment == null) {
                        toDayChampionFragment = new ToDayFootBallChampionFragment();
                        fragmentTransaction.add(R.id.today_parent, toDayChampionFragment);
                    } else {
                        toDayChampionFragment.filterResume();
                        fragmentTransaction.show(toDayChampionFragment);
                    }
                    currPage = 5;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title14))) {
                    if (basketBallPassFragment == null) {
                        basketBallPassFragment = new ToDayFootBallPassFragment();
                        fragmentTransaction.add(R.id.today_parent, basketBallPassFragment);
                    } else {
                        basketBallPassFragment.filterResume();
                        fragmentTransaction.show(basketBallPassFragment);
                    }
                    currPage = 6;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title5))) {
                    if (resultFragment == null) {
                        resultFragment = new ToDayResultFragment();
                        fragmentTransaction.add(R.id.today_parent, resultFragment);
                    } else {
                        resultFragment.filterResume();
                        fragmentTransaction.show(resultFragment);
                    }
                    currPage = 7;
                }
            } else {//篮球
                if (str.equals(LocaleController.getString(R.string.scroll_title1))) {
                    if (basketBallDefaultFragment == null) {
                        basketBallDefaultFragment = new ToDayBasketBallDefaultFragment();
                        fragmentTransaction.add(R.id.today_parent, basketBallDefaultFragment);
                    } else {
                        basketBallDefaultFragment.filterResume();
                        fragmentTransaction.show(basketBallDefaultFragment);
                    }
                    currPage = 8;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title13))) {
                    if (toDayBasketBallChampionFragment == null) {
                        toDayBasketBallChampionFragment = new ToDayBasketBallChampionFragment();
                        fragmentTransaction.add(R.id.today_parent, toDayBasketBallChampionFragment);
                    } else {
                        toDayBasketBallChampionFragment.filterResume();
                        fragmentTransaction.show(toDayBasketBallChampionFragment);
                    }
                    currPage = 9;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title14))) {
                    if (footBallPassFragment == null) {
                        footBallPassFragment = new ToDayBasketBallPassFragment();
                        fragmentTransaction.add(R.id.today_parent, footBallPassFragment);
                    } else {
                        footBallPassFragment.filterResume();
                        fragmentTransaction.show(footBallPassFragment);
                    }
                    currPage = 10;
                }
            }
            fragmentTransaction.commit();
        }
    };

    private void openMask() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(maskView, "alpha", 0.0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(200);
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                maskView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    private void hideMask() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(maskView, "alpha", 1.0f, 0.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(200);
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                maskView.setVisibility(View.GONE);
            }
        });
        set.start();
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.today_mask) {
            if (args != null && args.length >= 1) {
                if ("open".equals(args[0])) {
                    openMask();
                } else if ("close".equals(args[0])) {
                    hideMask();
                }
            }
        }
    }
}
