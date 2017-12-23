package com.dading.ssqs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @创建时间 	 2016-4-8 上午11:27:51
 * @描述	     根据自己的需求,处理时间的传递
 *
 * @版本       $Rev: 39 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2016-04-08 14:25:01 +0800 (星期五, 08 四月 2016) $
 * @更新描述    TODO
 */
public class InnerVerpager extends ViewPager {
	public static final String	TAG	= InnerVerpager.class.getSimpleName();
	private float				mDownX;
	private float				mDownY;

	/**
	 if(水平滚动){
		if(position==0){//第一个点
			if(左滑动){
				希望自己处理事件
				requestDisallowInterceptTouchEvent(true);
			}else{
				希望父容器处理事件
				requestDisallowInterceptTouchEvent(false);
			}
		}else if(position == getCount-1){//最后一个点
			if(左滑动){
				希望父容器处理事件
				requestDisallowInterceptTouchEvent(false);
			}else{
				希望自己处理事件
				requestDisallowInterceptTouchEvent(true);
			}
			
		}else{//中间的点
			希望自己处理事件
			requestDisallowInterceptTouchEvent(true);
		}
	}else{
		希望父容器处理
	}

	 */

	public InnerVerpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerVerpager(Context context) {
		super(context);
	}

	/** 
	getParent(父容器).request(请求)Disallow(不允许)Intercept(拦截)TouchEvent(Touch事件)(true(同意))-->希望父容器不拦截-->自己处理
	getParent().requestDisallowInterceptTouchEvent(true/false)-->默认情况-->父容器优先处理
	 */
	/**
	 * 1.是否派发
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getRawX();
			float moveY = ev.getRawY();

			int diffX = (int) (moveX - mDownX + .5f);
			int diffY = (int) (moveY - mDownY + .5f);

			if (Math.abs(diffY) > Math.abs(diffX)) {//上下滑动
				//希望父容器处理
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {//左右滑动
				if (getCurrentItem() == 0) {
					if (diffX < 0) {//往左滑动
						//	希望自己处理事件
						getParent().requestDisallowInterceptTouchEvent(true);
						Log.i(TAG, "往左滑动-->希望自己处理事件");
					} else {//往右滑动
						//	希望父容器处理事件
						getParent().requestDisallowInterceptTouchEvent(false);
						Log.i(TAG, "往右滑动-->希望父容器处理事件");
					}
				} else if (getCurrentItem() == getAdapter().getCount() - 1) {
					if (diffX < 0) {//往左滑动
						Log.i(TAG, "往左滑动-->希望父容器处理事件");
						//希望父容器处理事件
						getParent().requestDisallowInterceptTouchEvent(false);
					} else {//往右滑动
						Log.i(TAG, "往右滑动-->希望自己处理事件");
						//希望自己处理事件
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				} else {//中间的点
					Log.i(TAG, "希望自己处理事件");
					//希望自己处理
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 2.是否拦截
	 */
	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		// TODO
		return super.onInterceptHoverEvent(event);
	}

	/**
	 * p3.是否处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO
		return super.onTouchEvent(ev);
	}

}
