package com.dading.ssqs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2017/6/6.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static int lastClassGuid = 0;
    public int classGuid = 0;

    protected BaseActivity() {
        classGuid = lastClassGuid++;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (setLayoutId() != 0) {
            setContentView(setLayoutId());
        } else {
            setContentView(getContentView());
        }

        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected abstract int setLayoutId();

    protected View getContentView() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        reDisPlay();
    }

    public void reDisPlay() {
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        onPauPlay();
    }

    public void onPauPlay() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        setUnDe();
    }

    protected void setUnDe() {

    }

}
