package com.dading.ssqs.fragment.score;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.NotificationController;
import com.dading.ssqs.R;
import com.dading.ssqs.adapter.ScrollAdapterInner;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.components.tabindicator.TabIndicator;
import com.dading.ssqs.fragment.score.football.FFollowFragment;
import com.dading.ssqs.fragment.score.football.FImmediateFragment;
import com.dading.ssqs.fragment.score.football.FResultFragment;
import com.dading.ssqs.fragment.score.football.FScheduleFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;

/**
 * Created by mazhuang on 2018/1/26.
 */

public class ScoreFootBallFragment extends Fragment {

    private Context mContext;

    private FImmediateFragment fImmediateFragment;
    private FResultFragment fResultFragment;
    private FScheduleFragment fScheduleFragment;
    private FFollowFragment fFollowFragment;
    private ViewPager viewPager;

    private int pageType;

    public void setPageType(int pageType) {
        this.pageType = pageType;

        if (viewPager != null) {
            viewPager.setCurrentItem((pageType - 1));

            NotificationController.getInstance().postNotification(NotificationController.scoreFootChildPage, pageType + "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        return initView();
    }

    private View initView() {
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);

        TabIndicator tabIndicator = new TabIndicator(mContext);
        tabIndicator.setLineColor(0xFF009BDB);
        tabIndicator.setLineStyle(TabIndicator.LINE_STYLE_WRAP);
        tabIndicator.setTabMode(TabIndicator.TAB_MODE_LINE);
        tabIndicator.setUnderLineColor(0xFFEDEDED);
        tabIndicator.setTabPadding(AndroidUtilities.INSTANCE.dp(26), 0, AndroidUtilities.INSTANCE.dp(26), 0);
        container.addView(tabIndicator, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        viewPager = new ViewPager(mContext);
        viewPager.setId(R.id.viewpager);
        container.addView(viewPager, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        ArrayList<String> titles = new ArrayList<>();
        titles.add(LocaleController.getString(R.string.pl_odds_js));
        titles.add(LocaleController.getString(R.string.scroll_title5));
        titles.add(LocaleController.getString(R.string.schedule));
        titles.add(LocaleController.getString(R.string.follow));

        fImmediateFragment = new FImmediateFragment();

        fResultFragment = new FResultFragment();

        fScheduleFragment = new FScheduleFragment();

        fFollowFragment = new FFollowFragment();

        if (pageType == 0 || pageType == 1) {
            fImmediateFragment.setBeginInit(true);
        } else if (pageType == 2) {
            fResultFragment.setBeginInit(true);
        } else if (pageType == 3) {
            fScheduleFragment.setBeginInit(true);
        } else if (pageType == 4) {
            fFollowFragment.setBeginInit(true);
        }

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(fImmediateFragment);
        fragmentList.add(fResultFragment);
        fragmentList.add(fScheduleFragment);
        fragmentList.add(fFollowFragment);

        viewPager.setAdapter(new ScrollAdapterInner(this.getChildFragmentManager(), fragmentList, titles));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fImmediateFragment.hasInit();
                } else if (position == 1) {
                    fResultFragment.hasInit();
                } else if (position == 2) {
                    fScheduleFragment.hasInit();
                } else if (position == 3) {
                    fFollowFragment.hasInit();
                }

                NotificationController.getInstance().postNotification(NotificationController.scoreFootChildPage, (position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabIndicator.setViewPager(viewPager);

        if (pageType > 1) {
            viewPager.setCurrentItem((pageType - 1));
        }

        String type;

        if (pageType == 0) {
            type = "1";
        } else {
            type = pageType + "";
        }

        NotificationController.getInstance().postNotification(NotificationController.scoreFootChildPage, type);

        return container;
    }
}
