package com.dading.ssqs.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/7 15:21
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RankingSettingActivity extends BaseActivity {
    @Bind(R.id.ranking_setting_rich_switch)
    CheckBox mRankingSettingRichSwitch;
    @Bind(R.id.ranking_setting_three_ranking_switch)
    CheckBox mRankingSettingThreeRankingSwitch;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.ranking_setting));
    }

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_ranking_setting;
    }

    @Override
    protected void initListener ( ) {
        mRankingSettingRichSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //// TODO: 2016/10/7 参加财富排行
                } else {
                    //不参加
                }
            }
        });
        mRankingSettingThreeRankingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //// TODO: 2016/10/7 参胜率回报等三个排行
                } else {
                    //不参加
                }
            }
        });
    }

    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }
}
