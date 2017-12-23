package com.dading.ssqs.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/6 9:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GuessballGameActivity extends BaseActivity {

    @Bind(R.id.guess_game_list)
    ListView  mGuessGameList;

    @Bind(R.id.top_title)
    TextView  mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;

    private View        mView;
    private View        mPopView;
    private TextView    mToLook;
    private TextView    mClearOnce;
    private PopupWindow mPop;

    /**
     * @param v
     */
    @OnClick({R.id.top_back, R.id.top_icon})
    public void OnClik (View v) {
        switch (v.getId( )) {
            case R.id.top_back:
                finish( );
                break;
            case R.id.top_icon:
                mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                mTopIcon.setClickable(false);
                break;

            default:
                break;
        }
    }

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.guess_game));
    }

    @Override
    protected void initView ( ) {
        mView = View.inflate(this, R.layout.activity_guessball_game, null);

        mPopView = View.inflate(this, R.layout.delete_pop_view, null);
        mToLook = (TextView) mPopView.findViewById(R.id.clear_pop_look);
        mClearOnce = (TextView) mPopView.findViewById(R.id.clear_pop_authentication);

        mPop = PopUtil.popuMake(mPopView);
    }

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_guessball_game;
    }
}
