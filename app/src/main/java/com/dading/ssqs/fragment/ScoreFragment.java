package com.dading.ssqs.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.components.SelectMatchDialog;
import com.dading.ssqs.fragment.guesstheball.ScoreDataController;
import com.dading.ssqs.fragment.score.ScoreBasketBallFragment;
import com.dading.ssqs.fragment.score.ScoreFootBallFragment;
import com.dading.ssqs.fragment.score.basketball.BImmediateFragment;
import com.dading.ssqs.fragment.score.basketball.BResultFragment;
import com.dading.ssqs.fragment.score.basketball.BScheduleFragment;
import com.dading.ssqs.fragment.score.football.FImmediateFragment;
import com.dading.ssqs.fragment.score.football.FResultFragment;
import com.dading.ssqs.fragment.score.football.FScheduleFragment;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

/**
 * Created by mazhuang on 2018/1/26.
 * 比分
 */

public class ScoreFragment extends Fragment implements NotificationController.NotificationControllerDelegate {

    private final String TAG = "ScoreFragment";

    private Context mContext;
    private boolean hasInit = false;

    private TextView footBallTextView;
    private TextView basketBallTextView;
    private ImageView filterView;

    private ScoreFootBallFragment scoreFootBallFragment;
    private ScoreBasketBallFragment scoreBasketBallFragment;

    private SelectMatchDialog selectMatchDialog;
    private LoadingDialog loadingDialog;

    private int currPgae = 1;

    private int footChildPage = 1;//  1==即时  2==赛果  3==赛程  4==关注
    private int basketChildPage = 1;//  1==即时  2==赛果  3==赛程  4==关注

    private String filterDate;//筛选的时间

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        NotificationController.getInstance().addObserver(this, NotificationController.scoreFootChildPage);
        NotificationController.getInstance().addObserver(this, NotificationController.scoreBasketChildPage);
        NotificationController.getInstance().addObserver(this, NotificationController.scoreFootBallFilter);
        NotificationController.getInstance().addObserver(this, NotificationController.scoreBasketBallFilter);
        return initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreFootChildPage);
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreBasketChildPage);
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreFootBallFilter);
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreBasketBallFilter);
    }

    public View initView() {
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout topLayout = new RelativeLayout(mContext);
        topLayout.setBackgroundColor(getResources().getColor(R.color.home_top_blue));
        container.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.addView(titleLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, RelativeLayout.CENTER_IN_PARENT));

        footBallTextView = new TextView(mContext);
        footBallTextView.setTextSize(18);
        footBallTextView.setTextColor(Color.WHITE);
        footBallTextView.setText(LocaleController.getString(R.string.football));
        footBallTextView.setGravity(Gravity.CENTER_VERTICAL);
        footBallTextView.setPadding(AndroidUtilities.INSTANCE.dp(10), 0, AndroidUtilities.INSTANCE.dp(10), 0);
        footBallTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor(1);
                changePage(1);
            }
        });
        titleLayout.addView(footBallTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        basketBallTextView = new TextView(mContext);
        basketBallTextView.setTextSize(18);
        basketBallTextView.setTextColor(0xFF65D2FF);
        basketBallTextView.setPadding(AndroidUtilities.INSTANCE.dp(10), 0, AndroidUtilities.INSTANCE.dp(10), 0);
        basketBallTextView.setText(LocaleController.getString(R.string.basketball));
        basketBallTextView.setGravity(Gravity.CENTER_VERTICAL);
        basketBallTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor(2);
                changePage(2);
            }
        });
        titleLayout.addView(basketBallTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        filterView = new ImageView(mContext);
        filterView.setScaleType(ImageView.ScaleType.CENTER);
        filterView.setImageResource(R.mipmap.ic_score_filter);
        filterView.setBackgroundDrawable(AndroidUtilities.INSTANCE.createBarSelectorDrawable());
        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterMatch();
            }
        });
        RelativeLayout.LayoutParams filerLP = LayoutHelper.createRelative(40, 40, 0, 0, 15, 0);
        filerLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        filerLP.addRule(RelativeLayout.CENTER_VERTICAL);
        topLayout.addView(filterView, filerLP);

        FrameLayout layout_parent = new FrameLayout(mContext);
        layout_parent.setId(R.id.score_parent);
        container.addView(layout_parent, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        return container;
    }

    //筛选比赛
    private void filterMatch() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }

        if (selectMatchDialog == null) {
            selectMatchDialog = new SelectMatchDialog(mContext);
            selectMatchDialog.setListener(new SelectMatchDialog.OnSubmitListener() {
                @Override
                public void onSubmit(List<String> list, boolean isAll) {
                    String leagueIDs = "";

                    for (int i = 0; i < list.size(); i++) {
                        leagueIDs += list.get(i) + ",";
                    }

                    if (!TextUtils.isEmpty(leagueIDs)) {
                        leagueIDs = leagueIDs.substring(0, leagueIDs.length() - 1);
                    }

                    //发通知
                    sendNotices(leagueIDs);
                }
            });
        }

        if (currPgae == 1) {//足球筛选
            if (footChildPage == 1) {//即时页面
                if (ScoreDataController.Companion.getInstance().getFootBallImmediateData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncFootBall(TAG, 2, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallImmediateData(), ScoreDataController.Companion.getInstance().getFootBallImmediateHotData(), "联赛选择");
                }
            } else if (footChildPage == 2) {//赛果
                if (ScoreDataController.Companion.getInstance().getFootBallResultData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncFootBall(TAG, 3, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallResultData(), ScoreDataController.Companion.getInstance().getFootBallResultHotData(), "联赛选择");
                }
            } else if (footChildPage == 3) {//赛程
                if (ScoreDataController.Companion.getInstance().getFootBallScheduleData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncFootBall(TAG, 4, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallScheduleData(), ScoreDataController.Companion.getInstance().getFootBallScheduleHotData(), "联赛选择");
                }
            }
        } else if (currPgae == 2) {//篮球筛选
            if (basketChildPage == 1) {//即时页面
                if (ScoreDataController.Companion.getInstance().getBasketBallImmediateData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncBasketBall(TAG, 2, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallImmediateData(), ScoreDataController.Companion.getInstance().getBasketBallImmediateHotData(), "联赛选择");
                }
            } else if (basketChildPage == 2) {//赛果
                if (ScoreDataController.Companion.getInstance().getBasketBallResultData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncBasketBall(TAG, 3, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallResultData(), ScoreDataController.Companion.getInstance().getBasketBallResultData(), "联赛选择");
                }
            } else if (basketChildPage == 3) {//赛程
                if (ScoreDataController.Companion.getInstance().getBasketBallScheduleData() == null) {
                    loadingDialog.show();
                    ScoreDataController.Companion.getInstance().syncBasketBall(TAG, 4, filterDate);
                } else {
                    selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallScheduleData(), ScoreDataController.Companion.getInstance().getBasketBallScheduleHotData(), "联赛选择");
                }
            }
        }
    }

    private void sendNotices(String leaguesId) {
        if (currPgae == 1) {//足球筛选
            if (footChildPage == 1) {//即时页面
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FImmediateFragment.TAG, leaguesId);
            } else if (footChildPage == 2) {
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FResultFragment.TAG, leaguesId);
            } else if (footChildPage == 3) {
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FScheduleFragment.TAG, leaguesId);
            }
        } else if (currPgae == 2) {//篮球筛选
            if (basketChildPage == 1) {//即时页面
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BImmediateFragment.TAG, leaguesId);
            } else if (basketChildPage == 2) {
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BResultFragment.TAG, leaguesId);
            } else if (basketChildPage == 3) {
                NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BScheduleFragment.TAG, leaguesId);
            }
        }
    }

    private void changeTextColor(int page) {
        clearTextColor();

        if (page == 1) {
            footBallTextView.setTextColor(Color.WHITE);
        } else if (page == 2) {
            basketBallTextView.setTextColor(Color.WHITE);
        }
    }

    private void changePage(int pageType) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (scoreFootBallFragment != null) {
            transaction.hide(scoreFootBallFragment);
        }

        if (scoreBasketBallFragment != null) {
            transaction.hide(scoreBasketBallFragment);
        }

        if (pageType == 1) {
            if (scoreFootBallFragment == null) {
                scoreFootBallFragment = new ScoreFootBallFragment();
                transaction.add(R.id.score_parent, scoreFootBallFragment);
            } else {
                transaction.show(scoreFootBallFragment);
            }

            currPgae = 1;
        } else if (pageType == 2) {
            if (scoreBasketBallFragment == null) {
                scoreBasketBallFragment = new ScoreBasketBallFragment();
                transaction.add(R.id.score_parent, scoreBasketBallFragment);
            } else {
                transaction.show(scoreBasketBallFragment);
            }

            currPgae = 2;
        }

        transaction.commit();
    }

    private void clearTextColor() {
        footBallTextView.setTextColor(0xFF65D2FF);
        basketBallTextView.setTextColor(0xFF65D2FF);
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

    private void init() {
        scoreFootBallFragment = new ScoreFootBallFragment();

        if (pageType > 0) {
            scoreFootBallFragment.setPageType(pageType);
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.score_parent, scoreFootBallFragment);
        transaction.commit();
    }

    private int pageType;

    public void setTitleType(int type, int pageType) {
        hasInit = true;

        this.pageType = pageType;

        changeTextColor(type);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (scoreFootBallFragment != null) {
            transaction.hide(scoreFootBallFragment);
        }

        if (scoreBasketBallFragment != null) {
            transaction.hide(scoreBasketBallFragment);
        }

        if (type == 1) {//足球
            if (scoreFootBallFragment == null) {
                init();
            } else {
                transaction.show(scoreFootBallFragment);

                if (pageType > 0) {
                    scoreFootBallFragment.setPageType(pageType);
                }
            }

            currPgae = 1;
        } else if (type == 2) {//篮球
            if (scoreBasketBallFragment == null) {
                scoreBasketBallFragment = new ScoreBasketBallFragment();

                if (pageType > 0) {
                    scoreBasketBallFragment.setPageType(pageType);
                }
                transaction.add(R.id.score_parent, scoreBasketBallFragment);
            } else {
                transaction.show(scoreBasketBallFragment);

                if (pageType > 0) {
                    scoreBasketBallFragment.setPageType(pageType);
                }
            }

            currPgae = 2;
        }

        transaction.commit();
    }

    private void handlerChildPage(int childPage, String date) {
        if (childPage == 4) {
            filterView.setVisibility(View.GONE);
        } else {
            filterView.setVisibility(View.VISIBLE);
        }

        if (childPage == 2 && (TextUtils.isEmpty(filterDate) || TextUtils.isEmpty(date))) {//赛果是从昨天开始
            filterDate = DateUtils.getCurTimeAddND(-1, "yyyyMMddHH:mm:ss");
        } else if (childPage == 3 && (TextUtils.isEmpty(filterDate) || TextUtils.isEmpty(date))) {//赛程是从明天开始
            filterDate = DateUtils.getCurTimeAddND(1, "yyyyMMddHH:mm:ss");
        } else if (!TextUtils.isEmpty(date)) {
            filterDate = date;
        } else {
            filterDate = "";
        }
    }

    @Override
    public void didReceivedNotification(int id, String... args) {
        if (id == NotificationController.scoreFootChildPage) {
            if (args != null && args.length >= 1) {
                footChildPage = Integer.valueOf(args[0]);

                String date = "";

                if (args.length == 2) {
                    date = args[1];
                }

                handlerChildPage(footChildPage, date);

            }
        } else if (id == NotificationController.scoreBasketChildPage) {
            if (args != null && args.length >= 1) {
                basketChildPage = Integer.valueOf(args[0]);

                String date = "";

                if (args.length == 2) {
                    date = args[1];
                }

                handlerChildPage(basketChildPage, date);

            }
        } else if (id == NotificationController.scoreFootBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    if ("2".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallImmediateData(), ScoreDataController.Companion.getInstance().getFootBallImmediateHotData(), "联赛选择");
                    } else if ("3".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallResultData(), ScoreDataController.Companion.getInstance().getFootBallResultHotData(), "联赛选择");
                    } else if ("4".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getFootBallScheduleData(), ScoreDataController.Companion.getInstance().getFootBallScheduleHotData(), "联赛选择");
                    }
                }
            }
        } else if (id == NotificationController.scoreBasketBallFilter) {
            if (args != null && args.length >= 1) {
                if (TAG.equals(args[0])) {
                    loadingDialog.dismiss();
                    if ("2".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallImmediateData(), ScoreDataController.Companion.getInstance().getBasketBallImmediateHotData(), "联赛选择");
                    } else if ("3".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallResultData(), ScoreDataController.Companion.getInstance().getBasketBallResultHotData(), "联赛选择");
                    } else if ("4".equals(args[1])) {
                        selectMatchDialog.show(ScoreDataController.Companion.getInstance().getBasketBallScheduleData(), ScoreDataController.Companion.getInstance().getBasketBallScheduleHotData(), "联赛选择");
                    }
                }
            }
        }
    }
}
