package com.dading.ssqs.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by mazhuang on 2017/12/27.
 * 自动滚动的recyclerView
 */

public class AutoRecyclerView extends RecyclerView {

    private static final long TIME_AUTO_POLL = 30;
    AutoPollTask autoPollTask;
    private boolean running; //标示是否正在自动轮询
    private boolean canRun;//标示是否可以自动轮询,可在不需要的是否置false

    public AutoRecyclerView(Context context) {
        super(context);
        autoPollTask = new AutoPollTask(this);
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running)
            stop();
        canRun = true;
        running = true;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        running = false;
        removeCallbacks(autoPollTask);
    }

    private boolean isClick = true;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null && isClick) {
                    listener.onClick();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (AndroidUtilities.INSTANCE.dp(e.getY()) >= 0 && AndroidUtilities.INSTANCE.dp(e.getY()) <= getHeight()) {
                    isClick = true;
                } else {
                    isClick = false;
                }
                return true;
        }
        return super.onTouchEvent(e);
    }

    private OnAutoRecyclerClickListener listener;

    public void setListener(OnAutoRecyclerClickListener listener) {
        this.listener = listener;
    }

    public interface OnAutoRecyclerClickListener {
        void onClick();
    }

    static class AutoPollTask implements Runnable {

        private final WeakReference<AutoRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoRecyclerView reference) {
            this.mReference = new WeakReference<>(reference);
        }

        @Override
        public void run() {
            AutoRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                recyclerView.scrollBy(2, 2);
                recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
            }
        }
    }
}
