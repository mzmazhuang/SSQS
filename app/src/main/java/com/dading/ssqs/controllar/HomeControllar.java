package com.dading.ssqs.controllar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.controllar.sns.FiveLeaguesControllar;
import com.dading.ssqs.controllar.sns.FootBallBabyControllar;
import com.dading.ssqs.controllar.sns.FootBallLotteryControllar;
import com.dading.ssqs.controllar.sns.HeadLineControllar;
import com.dading.ssqs.controllar.sns.HotSpotControllar;
import com.dading.ssqs.controllar.sns.PPAPControllar;
import com.dading.ssqs.controllar.sns.SnsRferControllar;
import com.dading.ssqs.controllar.sns.SnsVpAdapter;
import com.dading.ssqs.controllar.sns.WordCupControllar;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 16:43
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 */

public class HomeControllar extends BaseTabsContainer {
    private FootBallBabyControllar mFootBaby;
    private TabIndicator mSnsindicator;
    private ViewPager mSnsviewpage;
    private LinearLayout mMLoadAnimal;
    private Runnable mTask;


    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initData() {
        ArrayList<String> listTitle = new ArrayList<>();
        listTitle.add("头条");
        listTitle.add("热点");
        listTitle.add("推荐");
        listTitle.add("足彩");
        listTitle.add("中超");
        listTitle.add("足球宝贝");
        listTitle.add("五大联赛");
        listTitle.add("世界杯");
        ArrayList<View> listControllar = new ArrayList<>();
        HeadLineControllar headLine = new HeadLineControllar(mContent);
        listControllar.add(headLine.mRootView);
        HotSpotControllar hotSpot = new HotSpotControllar(mContent);
        listControllar.add(hotSpot.mRootView);
        SnsRferControllar snsRefer = new SnsRferControllar(mContent);
        listControllar.add(snsRefer.mRootView);
        FootBallLotteryControllar lottery = new FootBallLotteryControllar(mContent);
        listControllar.add(lottery.mRootView);
        PPAPControllar PPAP = new PPAPControllar(mContent);
        listControllar.add(PPAP.mRootView);
        mFootBaby = new FootBallBabyControllar(mContent);
        listControllar.add(mFootBaby.mRootView);
        FiveLeaguesControllar fiveLeagues = new FiveLeaguesControllar(mContent);
        listControllar.add(fiveLeagues.mRootView);
        WordCupControllar wordCup = new WordCupControllar(mContent);
        listControllar.add(wordCup.mRootView);


        mSnsviewpage.setAdapter(new SnsVpAdapter(mContent, listControllar, listTitle));
        mSnsindicator.setViewPager(mSnsviewpage);
        mTask = new Runnable() {
            @Override
            public void run() {
                mMLoadAnimal.setVisibility(View.GONE);
            }
        };
        UIUtils.postTaskDelay(mTask, 1500);
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.removeTask(mTask);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
       /* mContenHomeLy.setVisibility(View.VISIBLE);
        mReferrLy.setVisibility(View.GONE);
        mGuessBallLy.setVisibility(View.GONE);
        mScoreLy.setVisibility(View.GONE);
        mContenMyLy.setVisibility(View.GONE);*/
        setVisbilityViews(mListTitle, mContenHomeLy);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(context, R.layout.activity_my_sns, null);
        mSnsindicator = (TabIndicator) view.findViewById(R.id.sns_indicator);
        mSnsviewpage = (ViewPager) view.findViewById(R.id.sns_viewpage);
        mMLoadAnimal = (LinearLayout) view.findViewById(R.id.loading_animal);
        return view;
    }
}
