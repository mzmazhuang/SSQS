package com.dading.ssqs.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.dading.ssqs.R;
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

import butterknife.Bind;
import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 10:05
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SnsActivity extends BaseActivity {
    @Bind(R.id.sns_indicator)
    TabIndicator snsindicator;
    @Bind(R.id.sns_viewpage)
    ViewPager snsviewpage;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadAnimal;
    private FootBallBabyControllar mFootBaby;

    @Override
    protected void initData() {
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
        HeadLineControllar headLine = new HeadLineControllar(this);
        listControllar.add(headLine.mRootView);
        HotSpotControllar hotSpot = new HotSpotControllar(this);
        listControllar.add(hotSpot.mRootView);
        SnsRferControllar snsRefer = new SnsRferControllar(this);
        listControllar.add(snsRefer.mRootView);
        FootBallLotteryControllar lottery = new FootBallLotteryControllar(this);
        listControllar.add(lottery.mRootView);
        PPAPControllar PPAP = new PPAPControllar(this);
        listControllar.add(PPAP.mRootView);
        mFootBaby = new FootBallBabyControllar(this);
        listControllar.add(mFootBaby.mRootView);
        FiveLeaguesControllar fiveLeagues = new FiveLeaguesControllar(this);
        listControllar.add(fiveLeagues.mRootView);
        WordCupControllar wordCup = new WordCupControllar(this);
        listControllar.add(wordCup.mRootView);


        snsviewpage.setAdapter(new SnsVpAdapter(this, listControllar, listTitle));
        snsindicator.setViewPager(snsviewpage);
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                mLoadAnimal.setVisibility(View.GONE);
            }
        }, 1500);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_sns;
    }
}
