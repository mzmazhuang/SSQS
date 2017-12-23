package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/23 14:18
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ChangePhotoNicknameActivity extends BaseActivity {
    @Bind(R.id.change_photo_nickname_new)
    EditText mChangePhotoNicknameNew;
    @Bind(R.id.change_photo_nickname_save)
    TextView mChangePhotoNicknameSave;

    @Bind(R.id.top_title)
    TextView  mTopTitle;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_change_photo_nickname;
    }

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.change_nickname));

        Intent intent = getIntent( );
        String nickName = intent.getStringExtra(Constent.NICK_NAME);
        mChangePhotoNicknameNew.setText(nickName);
        mChangePhotoNicknameNew.setSelection(nickName.length( ));

    }

    @Override
    protected void initListener ( ) {
        mChangePhotoNicknameNew.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged (Editable s) {
                if (s.length( ) > 0) {
                    mChangePhotoNicknameSave.setClickable(true);
                    mChangePhotoNicknameSave.setBackgroundResource(R.mipmap.register_sel);
                }
            }
        });

    }

    @OnClick({R.id.top_back, R.id.change_photo_nickname_save})
    public void OnClik (View v) {
        switch (v.getId( )) {
            case R.id.top_back:
                finish( );
                break;
            case R.id.change_photo_nickname_save:
                String newName = mChangePhotoNicknameNew.getText( ).toString( );
                if (newName.length( ) > 10) {
                    TmtUtils.midToast(ChangePhotoNicknameActivity.this, "修改名称不能大于10个字!", 0);
                    return;
                }
                Intent intent = new Intent( );
                intent.putExtra(Constent.NICK_NAME, newName);
                UIUtils.hideKeyBord(this);
                setResult(RESULT_OK, intent);
                finish( );
                break;

            default:
                break;
        }
    }
}
