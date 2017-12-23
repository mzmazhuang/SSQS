package com.dading.ssqs.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dading.ssqs.R;
import com.dading.ssqs.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * @创建者 张传榴
 * @创建时间 2016-4-7 上午10:09:41
 * @描述 TODO
 * @版本 $Rev$
 * @更新者 $Author$
 * @更新时间 $Data$
 * @更新描述 TODO
 */
public abstract class BaseFragnment extends Fragment {
    /**
     * 抽取BaseFragment之后好处
     * <p>
     * 1.提高代码复用性
     * 2.以后不用过多关注fragment声明周期只要关注我们自己定义的方法即可
     * p3.提高代码的复用性
     * 4.可以规定子类哪些必须实现哪些选择实现
     */
    public Context mContent;
    public View    mViewLoad;
    public View    mRootView;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        mContent = getActivity( );//获取每一个当前页面的context对象
        init( );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayout( ), container, false);
        ButterKnife.bind(this, mRootView);
        mViewLoad = View.inflate(getActivity( ), R.layout.templete_load_more, null);
        return mRootView;
    }

    /**
     * @创建时间 2016-4-7 上午10:18:20
     * @描述 fragment初始化应该显示的view
     * @描述 fragment子类必须有显示的view所以我们必须让他们复写
     */
    protected abstract int setLayout ( );

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData( );
        initListener( );
    }

    /**
     * @创建时间 2016-4-7 上午10:18:04
     * @描述 初始化fragment要调用的方法
     * @描述 子类是选择性实现
     */
    public void init ( ) {

    }


    /**
     * @创建时间 2016-4-7 上午10:20:50
     * @描述 为显示的view绑定数据
     */
    public void initData ( ) {
    }


    /**
     * @创建时间 2016-4-7 上午10:19:09
     * @描述 为fragment view控件添加监听
     */
    public void initListener ( ) {
    }

    @Override
    public void onDestroyView ( ) {
        super.onDestroyView( );
        UIUtils.getMainThreadHandler( ).removeCallbacksAndMessages(this);
        ButterKnife.unbind(this);
        setUnDe( );
    }

    protected  void setUnDe ( ){}
}
