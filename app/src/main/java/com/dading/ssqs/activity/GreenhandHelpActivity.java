package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/15 14:03
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GreenhandHelpActivity extends BaseActivity {
    @Bind(R.id.top_title)
    TextView  mTopTitle;
    @Bind(R.id.top_back)
    ImageView mTopBack;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.send_green_hand_help;
    }

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.greenhand_help));
    }

    @Override
    protected void initListener ( ) {
        super.initListener( );
        mTopBack.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.green_hand_img, R.id.green_hand_t1, R.id.green_hand_t2, R.id.green_hand_t3, R.id.green_hand_t4
            , R.id.green_hand_t5, R.id.green_hand_t7, R.id.green_hand_t8, R.id.green_hand_t9})
    public void OnClik (View v) {
        Intent intent = new Intent(this, GreenHandHelp2Activity.class);
        switch (v.getId( )) {
            case R.id.green_hand_img:
                intent.putExtra(Constent.GREEN_HAND, "0");
                break;
            case R.id.green_hand_t1:
                intent.putExtra(Constent.GREEN_HAND, "1");
                break;
            case R.id.green_hand_t2:
                intent.putExtra(Constent.GREEN_HAND, "2");
                break;
            case R.id.green_hand_t3:
                intent.putExtra(Constent.GREEN_HAND, "3");
                break;
            case R.id.green_hand_t4:
                intent.putExtra(Constent.GREEN_HAND, "4");
                break;
            case R.id.green_hand_t5:
                intent.putExtra(Constent.GREEN_HAND, "5");
                break;
            case R.id.green_hand_t7:
                intent.putExtra(Constent.GREEN_HAND, "7");
                break;
            case R.id.green_hand_t8:
                intent.putExtra(Constent.GREEN_HAND, "8");
                break;
            case R.id.green_hand_t9:
                intent.putExtra(Constent.GREEN_HAND, "9");
                break;
        }
        startActivity(intent);
    }
}
