package com.dading.ssqs.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/27 11:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WriteReferPrizeProtectActivity extends BaseActivity {
    @Bind(R.id.write_refer_prize_wb)
    WebView  writereferprizewb;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.write_refer_prize_pro));
        writereferprizewb.loadUrl("http://www.ddzlink.com/ssqs/tpl/rewardRule.html");
    }

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_write_refer_prize_protect;
    }

   @OnClick({R.id.top_back})
   public void OnClik(View v){
       finish();
   }

}
