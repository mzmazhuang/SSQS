package com.dading.ssqs.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @创建者	 伍碧林
 * @创建时间 	 2016-4-6 上午11:14:14
 * @描述	     1.不可以滚动的ViewPager
 * @描述	     2.不能影响孩子的事件
 *
 * @版本       $Rev: 21 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2016-04-06 14:19:31 +0800 (星期三, 06 四月 2016) $
 * @更新描述    TODO
 */
public class NoScrollViewPager extends LazyViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	/**
	 * 1.是否派发
	 * return true-->事件终止
	 * return false-->事件终止
	 * 所以一般情况不过处理,应该返回super
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 2.是否拦截
	 * return true-->会走到自己类里面的onTouchEvent中
	 * return fasle-->事件会传递给下一层
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO
		/**
		  大的viewPager默认会拦截事件,拦截事件没有必要
		 1.拦截之后,其实压根就没有处理对应的事件
		 2.可能还会影响孩子事件的处理
		 */
		//		return super.onInterceptTouchEvent(ev);
		return false;//-->传递孩子-->如果孩子拦截了事件,但是在孩子的onTouchEvent中没有消费事件,还有机会来到当前类的onTouEvent
	}

	/**
	 * p3.是否处理
	 * return true-->自己消费了事件,事件就终止了
	 * return false-->事件传递给上一层
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO
		//		return super.onTouchEvent(ev);//在viewpager中的饿action_move分支下面会产生滚动效果
		//		return false;
		return true;
	}

}
