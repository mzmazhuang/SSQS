package com.dading.ssqs.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.UIUtils;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/6 18:23
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseGuessball extends Fragment {
    public Context mContent;
    public View    mRootView;
    //@Bind(R.id.guessball_mid_controllar)
    FrameLayout mGuessballMidControllar;
    //@Bind(R.id.go_to_match_before)
    TextView    mGoToMatchBefore;
    //@Bind(R.id.loading_animal)
    public  LinearLayout mLoadingAnimal;
    private View         mView;
    public  LinearLayout mEmpty;
    private ImageView mLoadingAnimIv;
    public AnimationDrawable mDrawable;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContent = this.getActivity( );
        mRootView = initView( );
        return mRootView;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData( );
        initListeners( );
    }

    @Override
    public void onDestroyView ( ) {
        super.onDestroyView( );
        setUnDe( );
    }

    public void setUnDe ( ) {
    }


    private View initView ( ) {
        mView = View.inflate(mContent, R.layout.guessball_wining_info, null);
        mGuessballMidControllar = (FrameLayout) mView.findViewById(R.id.guessball_mid_controllar);
        mGoToMatchBefore = (TextView) mView.findViewById(R.id.go_to_match_before);
        mLoadingAnimal = (LinearLayout) mView.findViewById(R.id.loading_anim);
        mLoadingAnimIv = (ImageView) mView.findViewById(R.id.loading_anim_iv);

        mEmpty = (LinearLayout) mView.findViewById(R.id.guess_ball_no_data);

        mGuessballMidControllar.addView(initMidContentView(mContent));
        return mView;
    }

    public void initData ( ) {
        /**
         * 暂时禁止的动画
         */
        mLoadingAnimIv.setImageResource(R.drawable.loading_anim);
        mDrawable = (AnimationDrawable)mLoadingAnimIv.getDrawable( );

    }

    private void initListeners ( ) {
        mGoToMatchBefore.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                UIUtils.SendReRecevice(Constent.GO_TO_BEFORE);
                UIUtils.SendReRecevice(Constent.LOADING_SCORE);
            }
        });
        initListener( );
    }

    protected abstract void initListener ( );

    protected abstract View initMidContentView (Context content);
}
