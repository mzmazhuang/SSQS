package com.dading.ssqs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.SplashAdater;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.splash.SplashControllar;
import com.dading.ssqs.controllar.splash.SplashControllar1;
import com.dading.ssqs.controllar.splash.SplashControllar2;
import com.dading.ssqs.controllar.splash.SplashControllar3;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ThreadPoolUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;

public class SplashAcitivty extends BaseActivity {

    private static final String TAG = "SplashActivity";
    @Bind(R.id.splash_viewpager)
    ViewPager mSplashViewpager;

    private ArrayList<Fragment> mList;
    private Calendar mCalendar;
    private String mCurrentName;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void initData() {

        UIUtils.getSputils().putString(Constent.SUBTYPE, "0");
        UIUtils.getSputils().putString(Constent.LEAGUEIDS, "0");
        mCalendar = Calendar.getInstance();
        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                // 获取网络时间，这里获取中科院的时间
                try {
                    URL url = new URL("http://www.ntsc.ac.cn");
                    URLConnection uc = url.openConnection();
                    final long networkTime = uc.getDate();
                    Date date = new Date(networkTime);
                    mCalendar.setTime(date);//校验时间
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        SplashControllar1 splashControllar1 = new SplashControllar1();
        SplashControllar2 splashControllar2 = new SplashControllar2();
        SplashControllar3 splashControllar3 = new SplashControllar3();

        mList = new ArrayList<>();
        mCurrentName = AndroidUtilities.getVersionName(this);
        UIUtils.getSputils().putBoolean(Constent.CHIOCE, false);

        String s = UIUtils.getSputils().getString(Constent.IS_FRISE, "0");
        Logger.d(TAG, "是否是第一次登陆---0是-1不是---------------------------:" + s);
        if (s.equals("0")) {
            mList.add(splashControllar1);
            mList.add(splashControllar2);
            mList.add(splashControllar3);
            SplashControllar splashControllar = new SplashControllar();
            mList.add(splashControllar);
        } else {
            SplashControllar splashControllar = new SplashControllar();
            mList.add(splashControllar);
        }

        mSplashViewpager.setAdapter(new SplashAdater(this.getSupportFragmentManager(), mList));
        mSplashViewpager.setOffscreenPageLimit(3);
    }
}
