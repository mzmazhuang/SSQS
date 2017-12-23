package com.dading.ssqs.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dading.ssqs.R;

/*
 * 组合式控件:
 * 1.抽取公用的空间代码到一个布局里面
 * 2.定义一个类这个类继承有规定,布局标签是什么类型就必须继承什么类型如RelativeLayout
 * p3.让这个类显示这个控件 View.inflate(context, R.layout.item_setting_view, this);
 */
public class SoftSpaceStyle extends LinearLayout {

	private TextView tvTitle;
	private TextView tvUserSpace;
	private TextView tvFreSpace;
	private ProgressBar pbProgressbar;
	public SoftSpaceStyle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public SoftSpaceStyle(Context context, AttributeSet attrs) {
		super(context, attrs);
		//表示操作显示的是那个布局
		View view = View.inflate(context, R.layout.style_soft_space, this);
		pbProgressbar = (ProgressBar) view.findViewById(R.id.soft_pb_progresssbar);
	}
	//设置内存类标题
	public void setLable(String title){
		tvTitle.setText(title);
	}
	
	//设置进度条类进度值
	public void setProgress(long userSpace){
		pbProgressbar.setProgress((int) userSpace);
	}
	
	//设置进度条类二次值
	public void setProgresSecondsMax(long progressMax){
		pbProgressbar.setSecondaryProgress((int) progressMax);
	}
	//设置进度条类最大值
	public void setProgressMax(long progressMax){
		pbProgressbar.setMax((int) progressMax);
	}
	//设置已用内存
	public void setLeftTitle(String userSpace){
		tvUserSpace.setText(userSpace);
	}
	//设置可用内存
	public void setRightTitle(String freeSpace){
		tvFreSpace.setText(freeSpace);
	}
	
	public SoftSpaceStyle(Context context) {
		super(context);
	}
	
}
