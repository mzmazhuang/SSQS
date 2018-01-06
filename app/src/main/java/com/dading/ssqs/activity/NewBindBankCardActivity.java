package com.dading.ssqs.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.SelectBankListAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ExtractInfoElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.BankBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.SelectBankListView;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/25.
 */

public class NewBindBankCardActivity extends BaseActivity {
    private Context mContext;

    private RelativeLayout parentLayout;
    private TitleCell titleCell;
    private TextView tvTip;

    private BindBankCell peopleCell;//持卡人
    private BindBankCell bankNameCell;//银行名称
    private BindBankCell bankNumberCell;//银行账号
    private BindBankCell bankSubCell;//银行支行
    private BindBankCell passwordCell;//交易密码

    private SelectBankListView selectBankListView;

    private AlertDialog mShow;
    private AlertDialog.Builder mBuilder;

    private int bankId;


    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        mContext = this;

        parentLayout = new RelativeLayout(mContext);

        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        parentLayout.addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        titleCell = new TitleCell(this, getResources().getString(R.string.bind_bank_card));
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        tvTip = new TextView(mContext);
        tvTip.setGravity(Gravity.CENTER_VERTICAL);
        tvTip.setText(LocaleController.getString(R.string.bind_bank_card_introduce));
        tvTip.setTextSize(10);
        tvTip.setTextColor(getResources().getColor(R.color.yellow_orange2));
        container.addView(tvTip, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30, 15, 0, 10, 0));

        peopleCell = new BindBankCell(mContext, LocaleController.getString(R.string.card_holder), LocaleController.getString(R.string.hint_input_name), true, false, "");
        peopleCell.setRightIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog("为了您的账户资金安全，只能绑定持卡人本人的银行卡。获取帮助，请联系在线客服。", "持卡人提示");
            }
        });
        container.addView(peopleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        bankNameCell = new BindBankCell(mContext, LocaleController.getString(R.string.bank_name), "", false, true, LocaleController.getString(R.string.hint_chose_bank));
        bankNameCell.setTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBankListView.setVisibility(View.VISIBLE);
            }
        });
        container.addView(bankNameCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        bankNumberCell = new BindBankCell(mContext, LocaleController.getString(R.string.bank_num), LocaleController.getString(R.string.hint_bank_number), false, false, "");
        container.addView(bankNumberCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        bankSubCell = new BindBankCell(mContext, LocaleController.getString(R.string.bank_sub), LocaleController.getString(R.string.hint_bank_sub), false, false, "");
        container.addView(bankSubCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        passwordCell = new BindBankCell(mContext, LocaleController.getString(R.string.business_pwd), LocaleController.getString(R.string.hint_business_pwd), true, false, "");
        passwordCell.setRightIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog("交易密码用于余额提现，可以在个人信息中修改。", "交易密码提示");
            }
        });
        container.addView(passwordCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView tvDone = new TextView(mContext);
        tvDone.setGravity(Gravity.CENTER);
        tvDone.setTextColor(Color.WHITE);
        tvDone.setBackgroundResource(R.drawable.bg_bind_bank);
        tvDone.setText(LocaleController.getString(R.string.confirm_upload));
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        container.addView(tvDone, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 34, 15, 20, 15, 0));

        bankNameCell.post(new Runnable() {
            @Override
            public void run() {
                int left = bankNameCell.getTvName().getWidth() + AndroidUtilities.INSTANCE.dp(25);
                int top = titleCell.getHeight() + tvTip.getHeight() + peopleCell.getHeight() + bankNameCell.getHeight();

                selectBankListView = new SelectBankListView(mContext, itemClickListener);
                selectBankListView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectBankListView.setVisibility(View.INVISIBLE);
                    }
                });
                selectBankListView.setVisibility(View.INVISIBLE);
                selectBankListView.setPadding(left, top, 0, 0);
                RelativeLayout.LayoutParams params = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
                parentLayout.addView(selectBankListView, params);
            }
        });

        getNetDataWork();
        return parentLayout;
    }

    private void next() {
        if (isEmpty()) {
            ExtractInfoElement element = new ExtractInfoElement();
            element.setBankID(bankId + "");
            element.setBankCard(bankNumberCell.getEditValue());
            element.setUserName(peopleCell.getEditValue());
            element.setPassword(passwordCell.getEditValue());
            element.setBranch(bankSubCell.getEditValue());

            SSQSApplication.apiClient(0).extractInfoUpload(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, true);

                        Intent intent = new Intent(mContext, WithDrawActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        ToastUtils.midToast(mContext, "上传提现信息失败!" + result.getMessage(), 0);
                        Logger.d("BindBank", result.getMessage() + "失败信息");
                    }
                }
            });
        }
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(peopleCell.getEditValue())) {
            ToastUtils.midToast(this, "请输入真实姓名!", 1000);
            return false;
        }
        if (bankId <= 0) {
            ToastUtils.midToast(this, "请选择银行!", 1000);
            return false;
        }

        if (TextUtils.isEmpty(bankNumberCell.getEditValue())) {
            ToastUtils.midToast(this, "请输入银行卡账号!", 1000);
            return false;
        } else if (bankNumberCell.getEditValue().length() > 19 || bankNumberCell.getEditValue().length() < 16) {
            ToastUtils.midToast(this, "请输入正确的银行卡号!", 1000);
            return false;
        }

        if (TextUtils.isEmpty(bankSubCell.getEditValue())) {
            ToastUtils.midToast(this, "请输入银行支行", 1000);//开户银行更改后可以使用
            return false;
        }

        if (TextUtils.isEmpty(passwordCell.getEditValue())) {
            ToastUtils.midToast(this, "请输入交易密码!", 1000);
            return false;
        }

        return true;
    }

    private SelectBankListAdapter.OnBankItemClickListener itemClickListener = new SelectBankListAdapter.OnBankItemClickListener() {
        @Override
        public void onItemClick(BankBean bean) {
            bankId = bean.id;
            selectBankListView.setVisibility(View.INVISIBLE);
            bankNameCell.setText(bean.name);
        }
    };

    private void getNetDataWork() {
        SSQSApplication.apiClient(classGuid).getBankList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<BankBean> items = (List<BankBean>) result.getData();

                    if (items != null) {
                        selectBankListView.setList(items);
                    }
                } else {
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initDialog(String msg, String title) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_show_message_has_title_hasline, null);
        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(inflate);
        inflate.setBackgroundResource(R.drawable.shape_four_radius);
        inflate.setBackground(new BitmapDrawable());
        TextView message_tv = (TextView) inflate.findViewById(R.id.message_tv);
        TextView title_tv = (TextView) inflate.findViewById(R.id.title_tv);
        message_tv.setText(msg);
        title_tv.setText(title);
        Button ok_btn = (Button) inflate.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShow.dismiss();
            }
        });

        mShow = mBuilder.show();
    }

    private class BindBankCell extends RelativeLayout {

        private TextView tvName;
        private EditText editText;
        private ImageView imageView;
        private TextView textView;

        public BindBankCell(Context context, String name, String editHint, boolean isShowImage, boolean isText, String text) {
            super(context);

            setFocusable(true);
            setFocusableInTouchMode(true);

            setPadding(AndroidUtilities.INSTANCE.dp(15), 0, AndroidUtilities.INSTANCE.dp(15), 0);

            tvName = new TextView(context);
            tvName.setId(R.id.bind_bank_name);
            tvName.setTextSize(14);
            tvName.setText(name);
            addView(tvName, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

            imageView = new ImageView(context);
            if (isShowImage) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            imageView.setId(R.id.bind_bank_image);
            imageView.setImageResource(R.mipmap.prompt);
            RelativeLayout.LayoutParams layoutParams = LayoutHelper.createRelative(20, 20, 0, 0, 10, 0);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addView(imageView, layoutParams);

            editText = new EditText(context);

            editText.setSingleLine();
            editText.setVisibility(View.INVISIBLE);
            editText.setBackground(null);
            editText.setHint(editHint);
            editText.setTextSize(14);
            editText.setHintTextColor(0xFF787878);
            RelativeLayout.LayoutParams editParams = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10, 0, 10, 0);
            editParams.addRule(RelativeLayout.LEFT_OF, imageView.getId());
            editParams.addRule(RelativeLayout.RIGHT_OF, tvName.getId());
            editParams.addRule(RelativeLayout.CENTER_VERTICAL);
            addView(editText, editParams);

            textView = new TextView(context);
            textView.setVisibility(View.INVISIBLE);
            textView.setTextSize(14);
            textView.setText(text);
            textView.setTextColor(0xFF787878);
            RelativeLayout.LayoutParams textParams = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 12, 0, 10, 0);
            textParams.addRule(RelativeLayout.LEFT_OF, imageView.getId());
            textParams.addRule(RelativeLayout.RIGHT_OF, tvName.getId());
            textParams.addRule(RelativeLayout.CENTER_VERTICAL);
            addView(textView, textParams);

            if (isText) {
                textView.setVisibility(View.VISIBLE);
            } else {
                editText.setVisibility(View.VISIBLE);
            }
        }

        public void setTextClickListener(final OnClickListener listener) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }

        public void setText(String text) {
            textView.setText(text);
            textView.setTextColor(0xFF000000);
        }

        public String getText() {
            return textView.getText().toString();
        }

        public void setRightIconClickListener(final OnClickListener listener) {
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }

        public String getEditValue() {
            return editText.getText().toString();
        }

        public boolean isEmpty() {
            return editText.getText().toString().length() >= 1;
        }

        public TextView getTvName() {
            return tvName;
        }
    }
}
