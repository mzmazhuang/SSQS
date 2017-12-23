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
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.GuessBallTopAdapter;
import com.dading.ssqs.adapter.newAdapter.GuessBallTopSubAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GuessTopTitle;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.cells.GuessBallTopCell;
import com.dading.ssqs.components.ScrollBallCommitMenuView;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallResultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.TmtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mazhuang on 2017/11/30.
 * 滚球
 * <p>
 * 因有多个页面比较类似，但是考虑到后续如果不同的页面做了一些功能上的改动
 * 对开发不利，把每个功能都拆成一个页面，方便用于后续功能维护、开发
 */

public class ScrollBallFragment extends Fragment implements NotificationController.NotificationControllerDelegate {

    private Context mContext;

    private GuessBallTopCell topCell;
    private TextView tvTitle;
    private View maskView;
    private FragmentManager fragmentManager;
    private LinearLayout rightLayout;
    //fragments
    private ScrollBallDefaultFragment scrollball_parent;
    private ScrollBallBoDanFragment boDanFragment;
    private ScrollBallTotalFragment totalFragment;
    private ScrollBallHalfCourtFragment halfCourtFragment;
    private ScrollBallResultFragment resultFragment;
    private ScrollBallBasketBallDefaultFragment ballBasketBallDefaultFragment;

    private List<GuessTopTitle> footBallSubTitles = new ArrayList<>();
    private List<GuessTopTitle> basketBallSubTitles = new ArrayList<>();

    private int currTitlePosition = 1;//一级标题position 默认足球
    private int twoTitleFootPosition = 0;//二级标题足球的position  默认第一个选中
    private int twoTitleBasketPosition = -1;//二级标题篮球的position

    private int currPage = 1;//当前页面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        View view = initView();

        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask);
    }

    private View initView() {
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new ViewGroup.LayoutParams(LayoutHelper.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
                NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "child_close");
            }
        });
        maskView.setVisibility(View.GONE);
        maskView.setBackgroundColor(0xA5000000);
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100));

        topCell = new GuessBallTopCell(mContext);
        topCell.setTopListener(topClickListener);
        topCell.setSubTopListener(subTitleClickListener);
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout titleLayout = new RelativeLayout(mContext);
        titleLayout.setBackgroundColor(0xFF00425D);
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        tvTitle = new TextView(mContext);
        tvTitle.setTextSize(13);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText(LocaleController.getString(R.string.scroll_ball) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1));
        tvTitle.setGravity(Gravity.CENTER);
        titleLayout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, 30, 12, 0, 0, 0));

        rightLayout = new LinearLayout(mContext);
        rightLayout.setVisibility(View.GONE);
        rightLayout.setOrientation(LinearLayout.HORIZONTAL);
        rightLayout.setGravity(Gravity.CENTER_VERTICAL);
        titleLayout.addView(rightLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, 30, 0, 0, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT));

        TextView leftTextView = new TextView(mContext);
        leftTextView.setTextColor(0xFF73B9D6);
        leftTextView.setTextSize(12);
        leftTextView.setText("主要盘口");
        rightLayout.addView(leftTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        View lineView = new View(mContext);
        lineView.setBackgroundColor(Color.WHITE);
        rightLayout.addView(lineView, LayoutHelper.createLinear(1, 13, 5, 0, 5, 0));

        TextView rightTextView = new TextView(mContext);
        rightTextView.setTextColor(0xFF73B9D6);
        rightTextView.setTextSize(12);
        rightTextView.setText("赛节投注");
        rightLayout.addView(rightTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout parentLayout = new LinearLayout(mContext);//用来替换fragment的布局
        parentLayout.setId(R.id.scrollball_parent);
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        init();
        return container;
    }

    //一级title点击事件
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
        if (currPage == 1 && scrollball_parent != null) {
            scrollball_parent.filterResume();
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment.filterResume();
        } else if (currPage == 3 && totalFragment != null) {
            totalFragment.filterResume();
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment.filterResume();
        } else if (currPage == 5 && resultFragment != null) {
            resultFragment.filterResume();
        } else if (currPage == 6 && ballBasketBallDefaultFragment != null) {
            ballBasketBallDefaultFragment.filterResume();
        }
    }

    public void fragmentPause() {
        if (currPage == 1 && scrollball_parent != null) {
            scrollball_parent.filterPause();
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment.filterPause();
        } else if (currPage == 3 && totalFragment != null) {
            totalFragment.filterPause();
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment.filterPause();
        } else if (currPage == 5 && resultFragment != null) {
            resultFragment.filterPause();
        } else if (currPage == 6 && ballBasketBallDefaultFragment != null) {
            ballBasketBallDefaultFragment.filterPause();
        }
    }

    //二级title点击事件
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

            tvTitle.setText(LocaleController.getString(R.string.scroll_ball) + "-" + (currTitlePosition == 1 ? LocaleController.getString(R.string.football) : LocaleController.getString(R.string.basketball)) + ":" + str);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (scrollball_parent != null) {
                fragmentTransaction.hide(scrollball_parent);
                scrollball_parent.filterPause();
            }
            if (boDanFragment != null) {
                fragmentTransaction.hide(boDanFragment);
                boDanFragment.filterPause();
            }
            if (totalFragment != null) {
                fragmentTransaction.hide(totalFragment);
                totalFragment.filterPause();
            }
            if (halfCourtFragment != null) {
                fragmentTransaction.hide(halfCourtFragment);
                halfCourtFragment.filterPause();
            }
            if (resultFragment != null) {
                fragmentTransaction.hide(resultFragment);
                resultFragment.filterPause();
            }
            if (ballBasketBallDefaultFragment != null) {
                fragmentTransaction.hide(ballBasketBallDefaultFragment);
                ballBasketBallDefaultFragment.filterPause();
            }
            rightLayout.setVisibility(View.GONE);

            if (currTitlePosition == 1) {
                if (str.equals(LocaleController.getString(R.string.scroll_title1))) {
                    if (scrollball_parent == null) {
                        scrollball_parent = new ScrollBallDefaultFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, scrollball_parent);
                    } else {
                        scrollball_parent.filterResume();
                        fragmentTransaction.show(scrollball_parent);
                    }
                    currPage = 1;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title2))) {
                    if (boDanFragment == null) {
                        boDanFragment = new ScrollBallBoDanFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, boDanFragment);
                    } else {
                        boDanFragment.filterResume();
                        fragmentTransaction.show(boDanFragment);
                    }
                    currPage = 2;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title3))) {
                    if (totalFragment == null) {
                        totalFragment = new ScrollBallTotalFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, totalFragment);
                    } else {
                        totalFragment.filterResume();
                        fragmentTransaction.show(totalFragment);
                    }
                    currPage = 3;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title4))) {
                    if (halfCourtFragment == null) {
                        halfCourtFragment = new ScrollBallHalfCourtFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, halfCourtFragment);
                    } else {
                        halfCourtFragment.filterResume();
                        fragmentTransaction.show(halfCourtFragment);
                    }
                    currPage = 4;
                } else if (str.equals(LocaleController.getString(R.string.scroll_title5))) {
                    if (resultFragment == null) {
                        resultFragment = new ScrollBallResultFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, resultFragment);
                    } else {
                        resultFragment.filterResume();
                        fragmentTransaction.show(resultFragment);
                    }
                    currPage = 5;
                }
            } else {
                rightLayout.setVisibility(View.VISIBLE);
                //篮球
                if (str.equals(LocaleController.getString(R.string.scroll_title1))) {
                    if (ballBasketBallDefaultFragment == null) {
                        ballBasketBallDefaultFragment = new ScrollBallBasketBallDefaultFragment();
                        fragmentTransaction.add(R.id.scrollball_parent, ballBasketBallDefaultFragment);
                    } else {
                        ballBasketBallDefaultFragment.filterResume();
                        fragmentTransaction.show(ballBasketBallDefaultFragment);
                    }
                    currPage = 6;
                }
            }
            fragmentTransaction.commit();
        }
    };

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
        subTitle5.setName(LocaleController.getString(R.string.scroll_title5));

        footBallSubTitles.add(subTitle1);
        footBallSubTitles.add(subTitle2);
        footBallSubTitles.add(subTitle3);
        footBallSubTitles.add(subTitle4);
        footBallSubTitles.add(subTitle5);

        basketBallSubTitles.add(subTitle1);

        //一级标题
        List<GuessTopTitle> topList = new ArrayList<>();

        GuessTopTitle title1 = new GuessTopTitle();
        title1.setId(1);
        title1.setName(LocaleController.getString(R.string.football));
        title1.setCount(5);

        GuessTopTitle title2 = new GuessTopTitle();
        title2.setId(2);
        title2.setName(LocaleController.getString(R.string.basketball));
        title2.setCount(1);

        topList.add(title1);
        topList.add(title2);

        topCell.setTopTitleData(topList);
        topCell.setTopSubTitleData(footBallSubTitles);
    }

    private void init() {
        initTitles();

        scrollball_parent = new ScrollBallDefaultFragment();

        fragmentManager = getChildFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.scrollball_parent, scrollball_parent);
        fragmentTransaction.commit();
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.scroll_mask) {
            if (args != null && args.length >= 1) {
                if ("open".equals(args[0])) {
                    openMask();
                } else if ("close".equals(args[0])) {
                    hideMask();
                }
            }
        }
    }

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
}
