package com.dading.ssqs.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

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
public abstract class BaseScoreControllar extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "BaseScoreControllar";
    public  Context                                       mContent;
    public  View                                          mRootView;
    public  ImageButton                                   mScoreWeekLeft;
    public  ImageButton                                   mScoreWeekRight;
    public  TextView                                      mScoreWeekData;
    public  android.support.percent.PercentRelativeLayout mScoreWeekLayout;
    private FrameLayout                                   mScoreVpContent;
    public  View                                          mView;
    public  ImageView                                     mScoreWeekCalendar;
    public  View                                          mPopView;
    public  RadioGroup                                    mRg;
    public  RadioGroup                                    mRgCm;
    public  RelativeLayout                                mPopLy;
    public  ImageView                                     mPopClose;
    public  RadioButton                                   mRB1;
    public  RadioButton                                   mRB2;
    public  RadioButton                                   mRB3;
    public  RadioButton                                   mRB4;
    public  RadioButton                                   mRB5;
    public  RadioButton                                   mRB6;
    public  RadioButton                                   mRB7;
    public  ArrayList<RadioButton>                        mListRB;
    public int mCalendarPostion;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContent = this.getActivity();
        mRootView = initView();
        return mRootView;

    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroyView ( ) {
        super.onDestroyView( );
        setUnDe();
    }

    public void setUnDe ( ) {

    }

    /**
     * des 我们不知道子类需要什么杨的控件所以我们要让子类自己去写
     * des 我们必须返回一个view给调用者所以必须让他重写
     * date 2016-4-7 下午4:31:09
     */

    public View initView() {

        mView = View.inflate(mContent, R.layout.scorepager_calendar, null);

        mScoreWeekLayout = ButterKnife.findById(mView, R.id.score_week_ly);
        mScoreWeekLeft = ButterKnife.findById(mView, R.id.score_week_left);
        mScoreWeekRight = ButterKnife.findById(mView, R.id.score_week_right);
        mScoreWeekData = ButterKnife.findById(mView, R.id.score_week_data);
        mScoreWeekCalendar = ButterKnife.findById(mView, R.id.score_week_calendar);

        mScoreVpContent = ButterKnife.findById(mView, R.id.score_vp_content);

        mScoreWeekLayout.setVisibility(View.GONE);
        initScorecalendar();
        //针对mTvTitle,mIbMenu-->都是属于标题栏-->交给子类,基类不知道如何确定,但是子类必须要初始化标题栏
        // 针对title和menu基类不知道如何实现所以我们要给子类去做
        //添加view
        mScoreVpContent.addView(initContentView(mContent));

        mPopView = View.inflate(mContent, R.layout.popu_calendar, null);

        mRg = ButterKnife.findById(mPopView, R.id.pop_calendar_rg);
        mRB1 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb1);
        mRB2 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb2);
        mRB3 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb3);
        mRB4 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb4);
        mRB5 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb5);
        mRB6 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb6);
        mRB7 = ButterKnife.findById(mPopView, R.id.pop_calendar_rb7);
        mRgCm = ButterKnife.findById(mPopView, R.id.pop_calendar_rg_checkmark);
        mPopLy = ButterKnife.findById(mPopView, R.id.pop_calendar_pop_ly);
        mPopClose = ButterKnife.findById(mPopView, R.id.pop_calendar_close);
        mListRB = new ArrayList<>();
        mListRB.add(mRB1);
        mListRB.add(mRB2);
        mListRB.add(mRB3);
        mListRB.add(mRB4);
        mListRB.add(mRB5);
        mListRB.add(mRB6);
        mListRB.add(mRB7);
        return mView;
    }

    protected void initListener() {
        mRg.setOnCheckedChangeListener(this);
    }

    /**
     * des f返回ContentContainer容器中具体应该填充什么视图
     * des 交给子类去实现
     * date 2016-4-7 下午5:48:25
     */

    public abstract View initContentView(Context context);

    /**
     * des 我们不知道子类什么时候加载所以必须让子类自己去做
     * des 子类不加载数据那么返回的就是一个空控件所以子类必须进行重写
     * des 我们不知道子类什么时候加载哪些数据
     * date 2016-4-7 下午4:34:58
     */
    public abstract void initData();

    /**
     * des 切换到子类中的第postion个页面
     * date 2016-4-9 下午8:01:53
     */
    public void switchChildren(int position) {

    }

    /**
     * 设置标题, 判断是否显示menu
     * 2016-4-7 下午5:53:18
     */
    public abstract void initScorecalendar();

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.pop_calendar_rg:
                switch (checkedId) {
                            case R.id.pop_calendar_rb1:
                                mRgCm.check(R.id.pop_calendar_rb1_cm);
                                mCalendarPostion = 0;
                                break;
                            case R.id.pop_calendar_rb2:
                                mRgCm.check(R.id.pop_calendar_rb2_cm);
                                mCalendarPostion = 1;
                                break;
                            case R.id.pop_calendar_rb3:
                                mRgCm.check(R.id.pop_calendar_rb3_cm);
                                mCalendarPostion = 2;
                                break;
                            case R.id.pop_calendar_rb4:
                                mRgCm.check(R.id.pop_calendar_rb4_cm);
                                mCalendarPostion = 3;
                                break;
                            case R.id.pop_calendar_rb5:
                                mRgCm.check(R.id.pop_calendar_rb5_cm);
                                mCalendarPostion = 4;
                                break;
                            case R.id.pop_calendar_rb6:
                                mRgCm.check(R.id.pop_calendar_rb6_cm);
                                mCalendarPostion = 5;
                                break;
                            case R.id.pop_calendar_rb7:
                                mRgCm.check(R.id.pop_calendar_rb7_cm);
                                mCalendarPostion = 6;
                                break;
                            default:
                                break;
                        }
                break;
            default:
                break;
        }
        checkInitListner();
    }

    public void checkInitListner(){
    }
}
