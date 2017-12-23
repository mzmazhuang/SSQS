package com.dading.ssqs.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by mazhuang on 2017/12/1.
 * 自定义Scrollview,拦截滑动事件,解决与Recycler的滑动冲突
 */

public class RecyclerScrollview extends ScrollView {

    private int downY;
    private int mTouchSlop;

    private Context mContext;

    public RecyclerScrollview(Context context) {
        this(context, null);
    }

    public RecyclerScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
