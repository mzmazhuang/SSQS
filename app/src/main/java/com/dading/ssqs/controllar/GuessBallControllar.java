package com.dading.ssqs.controllar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.AccountDetailActivity;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.RankingSettingActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.adapter.GuessballPopAdapter;
import com.dading.ssqs.adapter.ScrollAdapterInner;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.guessball.GBGrounder;
import com.dading.ssqs.controllar.guessball.GBMatchBefore;
import com.dading.ssqs.controllar.guessball.GBSeries;
import com.dading.ssqs.controllar.guessball.JSGuessControllar;
import com.dading.ssqs.controllar.guessball.SGGuessControllar;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 16:55
 * 描述	      猜球
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${}
 */
public class GuessBallControllar extends BaseTabsContainer {
    private static final String TAG = "GuessBallControllar";
    private ViewPager mScrollViewpagerGuess;
    private TabIndicator mScrollIndicatorGuess;
    private ArrayList<String> mDataIndicatorGuess;
    private ArrayList<Fragment> mDataFragment;
    private GridView mPopGv;
    private View mView;
    private PopupWindow mPop;
    private View mPopView;
    private LinearLayout mPopVieLy;
    public MyGuessRecevice mRecevice;
    public GBMatchBefore mGbMatchBefore;
    public GBSeries mGbSeries;
    public GBGrounder mGbGrounder;
    private Button mMain;
    private View mPopFloatView;
    private LinearLayout mFLoatPlay;
    private LinearLayout mFLoatRecord;
    private PopupWindow mPopFloat;
    private Button mFLoatClose;
    private View mPopViewPlay;
    private LinearLayout mLyOut;
    private ImageView mIvClose;
    private PopupWindow mPopupWindowPlay;
    private RelativeLayout mFLoatLy;
    private JSGuessControllar mJsScoreControllar;
    private SGGuessControllar mSgScoreControllar;

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        setVisbilityViews(mListTitle, mGuessBallLy);
    }

    @Override
    public View initContentView(Context context) {
        mView = View.inflate(context, R.layout.guessballpager, null);
        mScrollViewpagerGuess = (ViewPager) mView.findViewById(R.id.scroll_viewpager_guessball);
        mScrollIndicatorGuess = (TabIndicator) mView.findViewById(R.id.scroll_indicator_guessball);

        mPopView = View.inflate(context, R.layout.guessball_pop, null);
        mPopVieLy = (LinearLayout) mPopView.findViewById(R.id.guessball_pop_view);
        mPopGv = (GridView) mPopView.findViewById(R.id.guessball_pop_gv);

        mPopViewPlay = View.inflate(mContent, R.layout.jc_play_help_popu, null);
        mLyOut = (LinearLayout) mPopViewPlay.findViewById(R.id.jc_play_help_pop_out);
        mIvClose = (ImageView) mPopViewPlay.findViewById(R.id.jc_play_help_pop_close);

        mPopupWindowPlay = PopUtil.popuMakeFalse(mPopViewPlay);

        mPopFloatView = View.inflate(context, R.layout.guessball_pop_float, null);
        mFLoatLy = (RelativeLayout) mPopFloatView.findViewById(R.id.float_guessball);
        mFLoatClose = (Button) mPopFloatView.findViewById(R.id.float_close);
        mFLoatPlay = (LinearLayout) mPopFloatView.findViewById(R.id.float_play_introduce);
        mFLoatRecord = (LinearLayout) mPopFloatView.findViewById(R.id.float_betting_record);


        mPopFloat = PopUtil.popuMakeFalseW(mPopFloatView);

        mMain = (Button) mView.findViewById(R.id.fab_main);
        mMain.bringToFront();

        return mView;
    }

    public void initData() {
        mRecevice = new MyGuessRecevice();

        UIUtils.ReRecevice(mRecevice, Constent.LOADING_ACTION);
        UIUtils.ReRecevice(mRecevice, Constent.HOME_BALL);

        setTitle();
        ArrayList<String> list = new ArrayList<>();
        list.add("投注记录");
        list.add("账户明细");
        list.add("金币任务");
        // list.add("发送广播");
        // list.add("刷新金币");
        list.add("金币充值");
        //list.add("排行设置");
        ArrayList<Integer> listIcon = new ArrayList<>();
        listIcon.add(R.mipmap.a_betting_record);
        listIcon.add(R.mipmap.a_accounting_identities);
        listIcon.add(R.mipmap.a_coin_quest);
        //listIcon.add(R.mipmap.a_refresh_gold);
        listIcon.add(R.mipmap.a_gold_recharge);
        listIcon.add(R.mipmap.a_ranking_set);
        //listIcon.add(R.mipmap.reds);
        mPopGv.setAdapter(new GuessballPopAdapter(mContent, list, listIcon));

        mPop = PopUtil.popuMake(mPopView);

        mDataIndicatorGuess = new ArrayList<>();
        mDataIndicatorGuess.add("滚球");
        mDataIndicatorGuess.add("今日");
        mDataIndicatorGuess.add("早盘");
        mDataIndicatorGuess.add("串关");
        mDataIndicatorGuess.add("赛果");

        mDataFragment = new ArrayList<>();

        mGbGrounder = new GBGrounder();
        mJsScoreControllar = new JSGuessControllar();
        mGbMatchBefore = new GBMatchBefore();
        mGbSeries = new GBSeries();
        mSgScoreControllar = new SGGuessControllar();

        mDataFragment.add(mGbGrounder);
        mDataFragment.add(mJsScoreControllar);
        mDataFragment.add(mGbMatchBefore);
        mDataFragment.add(mGbSeries);
        mDataFragment.add(mSgScoreControllar);


        mScrollViewpagerGuess.setAdapter(new ScrollAdapterInner(this.getFragmentManager(), mDataFragment, mDataIndicatorGuess));
        mScrollIndicatorGuess.setViewPager(mScrollViewpagerGuess);

        mGuessTitleRanking.setVisibility(View.GONE);
        mGuessTitleRg.setVisibility(View.VISIBLE);
    }

    private void setTitle() {
        LogUtil.util("GBSS", "TITLE返回数据是------:" + UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true));
        if (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true)) {
            mGuessTitleFootball.setChecked(true);
        } else {
            mGuessTitleBasketBall.setChecked(true);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mFLoatLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopFloat.isShowing())
                    mPopFloat.dismiss();
            }
        });
        mPopFloat.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mMain.setVisibility(View.VISIBLE);
            }
        });
        //help
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowPlay.dismiss();
            }
        });
        mLyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowPlay.dismiss();
            }
        });
        mFLoatRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                mPopFloat.dismiss();
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                    intent = new Intent(mContent, BettingRecordActivity.class);
                else
                    intent = new Intent(mContent, LoginActivity.class);
                mContent.startActivity(intent);
            }
        });
        mFLoatPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopFloat.dismiss();
                mPopupWindowPlay.showAtLocation(mView, Gravity.CENTER, 0, 0);
            }
        });
        mFLoatClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopFloat.dismiss();
                mMain.setVisibility(View.VISIBLE);
            }
        });
        mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopFloat.isShowing()) {
                    mPopFloat.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                    mMain.setVisibility(View.GONE);
                }
            }
        });
        mScrollIndicatorGuess.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PopUtil.closePop(mGbMatchBefore.mPopChioce);
                PopUtil.closePop(mGbSeries.mPopChioce);
                PopUtil.closePop(mGbSeries.mPopupWindow1);
            }

            @Override
            public void onPageSelected(int position) {
                if (mPopFloat.isShowing())
                    mPopFloat.dismiss();
                switch (position) {
                    case 0:
                        UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                        break;
                    case 1:
                        UIUtils.SendReRecevice(Constent.JS_RECEVICE);
                        break;
                    case 2:
                        UIUtils.SendReRecevice(Constent.SQ_RECEVICE);//赛前
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mContentGuessly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, StoreActivity.class);
                intent.putExtra(Constent.DIAMONDS, "2");
                mContent.startActivity(intent);
            }
        });

        mGuessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "显示猜球pop");
                mPop.showAsDropDown(mGuessBallLy, 0, 0);
            }
        });
        mPopVieLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mPopGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private Intent mIntent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);

                switch (position) {
                    case 0:
                        if (b)
                            mIntent = new Intent(mContent, BettingRecordActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 1:
                        if (b)
                            mIntent = new Intent(mContent, AccountDetailActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 2:
                        if (b) {
                            mIntent = new Intent(mContent, HomeFreeGlodActivity.class);
                        } else {
                            mIntent = new Intent(mContent, LoginActivity.class);
                        }
                        break;
                    case 3:
                        if (b)
                            mIntent = new Intent(mContent, NewRechargeActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 4:
                        if (b)
                            mIntent = new Intent(mContent, RankingSettingActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    default:
                        break;
                }
                if (mIntent != null)
                    mContent.startActivity(mIntent);
                mPop.dismiss();
            }
        });

    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
    }

    private class MyGuessRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constent.LOADING_ACTION:
                    initTitleBar();
                    break;
                case Constent.HOME_BALL:
                    setTitle();
                    break;
                default:
                    break;
            }
        }
    }
}
