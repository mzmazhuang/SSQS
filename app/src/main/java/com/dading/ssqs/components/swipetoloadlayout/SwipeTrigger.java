package com.dading.ssqs.components.swipetoloadlayout;

/**
 * Created by mazhuang on 2015/8/17.
 */
interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}
