package com.dading.ssqs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/13 10:17
 * 描述	      禁止上下滑动grideView,设置高度
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GrapeGridview extends GridView {

    public GrapeGridview(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public GrapeGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public GrapeGridview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    //通过重新dispatchTouchEvent方法来禁止滑动
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
       public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

                  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                              MeasureSpec.AT_MOST);
                   super.onMeasure(widthMeasureSpec, expandSpec);
              }

}
