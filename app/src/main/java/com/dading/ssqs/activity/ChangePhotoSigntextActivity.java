package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/23 14:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ChangePhotoSigntextActivity extends BaseActivity {
    @Bind(R.id.change_photo_signature_new)
    EditText mChangePhotoSignatureNew;
    @Bind(R.id.change_photo_signature_save)
    TextView mChangePhotoSignatureSave;

    @Bind(R.id.top_title)
    TextView  mTopTitle;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_change_photo_signature;
    }

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.edit_signature));
        Intent intent = getIntent( );
        String s = intent.getStringExtra(Constent.SIGN_TEXT);
        if (!TextUtils.isEmpty(s)) {
            mChangePhotoSignatureNew.setText(s);
        }
    }

    @Override
    protected void initListener ( ) {
        mChangePhotoSignatureNew.setOnFocusChangeListener(new View.OnFocusChangeListener( ) {
            @Override
            public void onFocusChange (View v, boolean hasFocus) {
                if (hasFocus) {
                    mChangePhotoSignatureSave.setClickable(true);
                    mChangePhotoSignatureSave.setBackgroundResource(R.mipmap.register_sel);
                }
            }
        });
    }

    @OnClick({R.id.top_back, R.id.change_photo_signature_save})
    public void OnClik (View v) {
        switch (v.getId( )) {
            case R.id.top_back:
                finish( );
                break;
            case R.id.change_photo_signature_save:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus( ).getWindowToken( ), 0);
                String s = mChangePhotoSignatureNew.getText( ).toString( );
                Intent intent = new Intent( );
                intent.putExtra(Constent.SIGN_TEXT, s);
                setResult(RESULT_OK, intent);
                finish( );
                break;

            default:
                break;
        }
    }
}
