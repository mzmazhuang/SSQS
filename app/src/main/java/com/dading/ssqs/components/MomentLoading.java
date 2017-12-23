package com.dading.ssqs.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dading.ssqs.R;

/**
 * Created by asus on 2017/12/6.
 * 加载
 */

@SuppressLint("AppCompatCustomView")
public class MomentLoading extends ImageView {

    private final Animation animation;
    private boolean isRun;

    public MomentLoading(Context context) {
        super(context);
        animation = AnimationUtils.loadAnimation(context, R.anim.loading);
        animation.setDuration(800);

        setVisibility(View.GONE);
    }

    public void show() {
        if (!isRun) {
            isRun = true;
            setVisibility(View.VISIBLE);
            setImageResource(R.mipmap.ic_video_loading);
            startAnimation(animation);
        }
    }

    public void dismiss() {
        if (isRun) {
            isRun = false;
            setVisibility(View.GONE);
            clearAnimation();
            setImageResource(R.drawable.transparent);
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
