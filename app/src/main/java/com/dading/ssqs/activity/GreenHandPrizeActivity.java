package com.dading.ssqs.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 16:41
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GreenHandPrizeActivity extends BaseActivity {

    @Bind(R.id.green_hand_web)
    WebView   mGreenHandWeb;
    @Bind(R.id.top_title)
    TextView  mTopTitle;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_greenhand_prize;
    }

    @Override
    protected void initData ( ) {
      mTopTitle.setText(getString(R.string.greenhand_prize));
    }

    @OnClick({R.id.top_back})
              public void OnClik(View v){
                  finish();
              }
}
