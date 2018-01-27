package com.dading.ssqs.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchCollectActivity;
import com.dading.ssqs.activity.MoreSettingActivity;
import com.dading.ssqs.activity.MyMessageActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 创建者 张传榴
 * 创建时间 2016-4-7 下午4:25:56
 * 描述 将controllar抽取出来一个基类
 * 描述 我们需要给它一个共有的数据那么就在构造方法添加
 * 版本 $Rev$
 * 更新者 $Author$
 * 更新时间 $Data$
 * 更新描述 TODO
 */
public abstract class BaseTabsContainer extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "BaseTabsContainer";
    // 设置成公共的方便子类进行调用赋值
    public View mRootView;
    public Context mContent;                // 保存一个上下文t mContent;                // 保存一个上下文

    @Bind(R.id.content_icon_Store)
    ImageView mStore;

    @Bind(R.id.content_title_home_ly)
    public RelativeLayout mContenHomeLy;
    @Bind(R.id.content_referr_Store)
    public ImageView mReferrStore;
    @Bind(R.id.content_referr_referr)
    public RadioButton mReferrReferr;
    @Bind(R.id.content_referr_circle)
    public RadioButton mReferrcircle;
    @Bind(R.id.content_referr_radio_group)
    public RadioGroup mReferrReferrRg;
    @Bind(R.id.content_referr_write)
    public ImageView mReferrWrite;
    @Bind(R.id.content_referr_help)
    public ImageView mReferrHelpIv;
    @Bind(R.id.content_title_referr_ly)
    public RelativeLayout mReferrLy;
    @Bind(R.id.content_title_guessball_photo_back)
    public ImageView mGuessBallPhotoBack;
    @Bind(R.id.content_title_guessball_beticon)
    public ImageView mContentGuessIcon;
    @Bind(R.id.content_title_guessball_glod_number)
    public TextView mGuessBallGlodNumber;
    @Bind(R.id.content_title_guessball_glod_add)
    public ImageView mGuessIconAdd;
    @Bind(R.id.content_title_guessball_bet_ly)
    public LinearLayout mContentGuessly;
    //@Bind(R.id.content_title_guessball_title_f)
    public RadioButton mGuessTitleFootball;
    //@Bind(content_title_guessball_title_b)
    public RadioButton mGuessTitleBasketBall;
    @Bind(R.id.content_title_guessball_title_rg)
    public RadioGroup mGuessTitleRg;
    @Bind(R.id.content_title_guessball_rank)
    public TextView mGuessTitleRanking;
    @Bind(R.id.content_title_guessball_icon)
    public ImageButton mGuessIcon;
    @Bind(R.id.content_title_guessball_ly)
    public RelativeLayout mGuessBallLy;
    //@Bind(R.id.content_title_score_title_f)
    public RadioButton mScoreTitleFootball;
    //@Bind(content_title_score_title_b)
    public RadioButton mScoreTitleBasketBall;
    @Bind(R.id.content_title_score_title_rg)
    public RadioGroup mScoreTitleRg;
    @Bind(R.id.content_title_score_choice)
    public ImageView mScoreChoice;
    @Bind(R.id.content_title_score_ly)
    public RelativeLayout mScoreLy;
    @Bind(R.id.content_my_message_num)
    public TextView mMyStoreMessNum;
    @Bind(R.id.content_my_message)
    public RelativeLayout mMyStoreMess;
    @Bind(R.id.content_title_my_ly)
    public RelativeLayout mContenMyLy;
    @Bind(R.id.content_title_ly)
    public RelativeLayout mContenLy;
    @Bind(R.id.content_main_middle)
    FrameLayout mViewPagerController;
    private View mView;
    private Intent mIntent;
    public ArrayList<RelativeLayout> mListTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContent = this.getActivity();
        mRootView = initView(mContent);
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        setUnDe();
    }

    /**
     * 标题显示
     *
     * @param list
     * @param ly
     */
    public void setVisbilityViews(ArrayList<RelativeLayout> list, RelativeLayout ly) {
        for (RelativeLayout v : list) {
            if (v == ly) {
                Logger.INSTANCE.d(TAG, "是标题显示---" + v.equals(ly) + "--------:" + (v == ly));
                v.setVisibility(View.VISIBLE);
            } else {
                Logger.INSTANCE.d(TAG, "不是标题显示---" + v.equals(ly) + "--------:" + (v == ly));
                v.setVisibility(View.GONE);
            }
        }
    }


    protected void setUnDe() {
    }

    /**
     * 我们不知道子类需要什么杨的控件所以我们要让子类自己去写
     * 我们必须返回一个view给调用者所以必须让他重写
     * 2016-4-7 下午4:31:Context
     */
    public View initView(Context content) {
        mView = View.inflate(content, R.layout.content_title, null);
        mGuessTitleFootball = (RadioButton) mView.findViewById(R.id.content_title_guessball_title_f);
        mScoreTitleFootball = (RadioButton) mView.findViewById(R.id.content_title_score_title_f);
        mGuessTitleBasketBall = (RadioButton) mView.findViewById(R.id.content_title_guessball_title_b);
        mScoreTitleBasketBall = (RadioButton) mView.findViewById(R.id.content_title_score_title_b);
        ButterKnife.bind(this, mView);

        mReferrReferr.setClickable(false);
        mMyStoreMessNum.setVisibility(View.GONE);

        //针对mTvTitle,mIbMenu-->都是属于标题栏-->交给子类,基类不知道如何确定,但是子类必须要初始化标题栏
        // 针对title和menu基类不知道如何实现所以我们要给子类去做
        //添加view
        mViewPagerController.addView(initContentView(mContent));
        mScoreTitleFootball.setChecked(true);
        //UIUtils.getSputils( ).putBoolean(Constent.IS_FOOTBALL, true);此处放开会导致首页跳转篮球标题无法切换
        initTitleBar();
        return mView;
    }

    public void initTitleBar() {
        mListTitle = new ArrayList<>();
        mListTitle.add(mContenHomeLy);
        mListTitle.add(mReferrLy);
        mListTitle.add(mGuessBallLy);
        mListTitle.add(mScoreLy);
        mListTitle.add(mContenMyLy);
    }

    protected void initListener() {
        mGuessTitleRg.setOnCheckedChangeListener(this);
        mScoreTitleRg.setOnCheckedChangeListener(this);
    }

    /**
     * 设置标题, 判断是否显示menu
     * 2016-4-7 下午5:53:18
     */

    /**
     * f返回ContentContainer容器中具体应该填充什么视图
     * 交给子类去实现
     * 2016-4-7 下午5:48:25
     */

    public abstract View initContentView(Context context);

    /**
     * 我们不知道子类什么时候加载所以必须让子类自己去做
     * 子类不加载数据那么返回的就是一个空控件所以子类必须进行重写
     * 我们不知道子类什么时候加载哪些数据
     * 2016-4-7 下午4:34:58
     */
    public abstract void initData();

    /**
     * 切换到子类中的第postion个页面
     * 2016-4-9 下午8:01:53
     */
    @OnClick({R.id.content_my_setting, R.id.content_my_message, R.id.content_title_guessball_photo_back
            , R.id.content_icon_Store, R.id.content_referr_Store, R.id.content_title_guessball_beticon
            , R.id.content_title_guessball_glod_add, R.id.content_title_guessball_glod_number})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_my_setting:
                Intent intentSetting = new Intent(mContent, MoreSettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.content_my_message:
                Intent intentMess = null;
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                    intentMess = new Intent(mContent, MyMessageActivity.class);
                else
                    intentMess = new Intent(mContent, LoginActivity.class);

                startActivity(intentMess);

                break;
            case R.id.content_title_guessball_photo_back:
                UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
                break;
            case R.id.content_icon_Store:
            case R.id.content_referr_Store:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    mIntent = new Intent(mContent, NewRechargeActivity.class);
                } else {
                    mIntent = new Intent(mContent, LoginActivity.class);
                }
                startActivity(mIntent);
                break;

            case R.id.content_title_guessball_beticon:
            case R.id.content_title_guessball_glod_add:
            case R.id.content_title_guessball_glod_number://如果未登陆跳转到登陆界面,如果已经登陆跳转到商城
                //if ()
                Intent intentGlodBuy = new Intent(mContent, StoreActivity.class);
                intentGlodBuy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentGlodBuy.putExtra(Constent.DIAMONDS, "2");
                startActivity(intentGlodBuy);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.content_title_guessball_title_f:
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
                UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL);
                Logger.INSTANCE.d("GBSS", "發送足球------------------------------:");
                break;
            case R.id.content_title_guessball_title_b:
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);
                UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL);
                Logger.INSTANCE.d("GBSS", "發送籃球------------------------------:");
                break;
            case R.id.content_title_score_title_f:
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
                UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);

                break;
            case R.id.content_title_score_title_b:
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);
                UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                break;
            default:
                break;
        }
    }
}
