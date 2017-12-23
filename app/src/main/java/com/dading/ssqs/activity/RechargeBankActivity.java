package com.dading.ssqs.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.CopyAdapter;
import com.dading.ssqs.adapter.TransferTypeAdapter;
import com.dading.ssqs.adapter.TypeRecycleAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ChargeUploadElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/6/7.
 */
public class RechargeBankActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RechargeBankActivity";
    @Bind(R.id.bank_name_tv)
    TextView mBankNameTv;
    @Bind(R.id.bank_address_tv)
    TextView mBankAddressTv;
    @Bind(R.id.card_number_tv)
    TextView mCardNumberTv;
    @Bind(R.id.card_owner)
    TextView mCardOwner;
    @Bind(R.id.transfer_accounts_time)
    TextView mTransferAccountsTime;
    @Bind(R.id.deposit_amount)
    EditText mDepositAmount;
    @Bind(R.id.transfer_accounts_name)
    EditText mTransferAccountsName;

    @Bind(R.id.recharge_bank_copy)
    RecyclerView mRecyclerViewCopy;

    @Bind(R.id.transfer_accounts_type_gv)
    GridView mTransferAccountsTypeGv;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    private WXDFBean.InfoBean mBean;
    private View mCanlendarView;
    private CalendarView calendarView;
    private TextView mClear;
    private TextView mClose;
    private TextView mToday;
    private PopupWindow mPop;
    private String mTemplate;
    private RecyclerView mRecyclerView;
    private TypeRecycleAdapter mAdapter;
    private PopupWindow mPopType;
    private Date mDate;
    private TransferTypeAdapter mAdapterType;
    private String mCurTime;
    private CopyAdapter mAdapter1;


    @Override
    protected void initView() {

        mCanlendarView = View.inflate(this, R.layout.pop_candlar, null);

        calendarView = (CalendarView) mCanlendarView.findViewById(R.id.betting_pop_calendar);
        mClear = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_clear);
        mClose = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_close);
        mToday = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_today);
        mPop = PopUtil.popuMakeWrap(mCanlendarView);

        View view = View.inflate(this, R.layout.recharge_type, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recharge_type_recycleview);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bank_recharge;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.bank_recharge));
        mDate = new Date();
        Intent intent = getIntent();
        mBean = (WXDFBean.InfoBean) intent.getSerializableExtra(Constent.RECHARGE_BANK);

        if (mBean != null) {
            String bank = "" + mBean.getName();
            mBankNameTv.setText(bank);

            String bankAddr = "" + mBean.getBankAddress();
            mBankAddressTv.setText(bankAddr);

            String cardNum = "" + mBean.getCardNumber();
            mCardNumberTv.setText(cardNum);

            String cardOwner = "" + mBean.getOwner();
            mCardOwner.setText(cardOwner);

            String text = mBean.getMoney() + "";
            mDepositAmount.setText(text);

            //设置光标
            Editable edt = mDepositAmount.getText();
            Selection.setSelection(edt, text.length());

            mAdapterType = new TransferTypeAdapter(this);
            mTransferAccountsTypeGv.setAdapter(mAdapterType);

        }

        mTemplate = "yyyy-MM-dd";
        mCurTime = DateUtils.getCurTime(mTemplate);
        mTransferAccountsTime.setText(mCurTime);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new TypeRecycleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mPopType = PopUtil.popuMake(mRecyclerView);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(getString(R.string.copy));
        }
        mRecyclerViewCopy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CopyAdapter mAdapter = new CopyAdapter(R.layout.copy, list);
        mRecyclerViewCopy.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                switch (i) {
                    case 0:
                        setCopy(mBean.getName());
                        ToastUtils.midToast(UIUtils.getContext(), "复制收款银行成功", 0);
                        break;
                    case 1:
                        setCopy(mBean.getOwner());
                        ToastUtils.midToast(UIUtils.getContext(), "复制收款人成功", 0);
                        break;
                    case 2:
                        setCopy(mBean.getCardNumber());
                        ToastUtils.midToast(UIUtils.getContext(), "复制收款卡号成功", 0);
                        break;
                    case 3:
                        setCopy(mBean.getBankAddress());
                        ToastUtils.midToast(UIUtils.getContext(), "复制收款支行成功", 0);
                        break;
                }
            }
        });

    }


    @OnClick({R.id.top_back, R.id.bank_upload, R.id.transfer_accounts_time, R.id.bank_up_step
    })
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.bank_up_step:
            case R.id.top_back:
                finish();
                break;
            case R.id.transfer_accounts_time:
                mPop.showAsDropDown(mTransferAccountsTime);
                break;
            case R.id.bank_upload:
                String time = mTransferAccountsTime.getText().toString();

                String acount = mDepositAmount.getText().toString();
                String name = mTransferAccountsName.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    ToastUtils.midToast(RechargeBankActivity.this, "请选择转账时间!", 0);
                    return;
                }
                if (TextUtils.isEmpty(acount)) {
                    ToastUtils.midToast(RechargeBankActivity.this, "请输入充值金额!", 0);
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.midToast(RechargeBankActivity.this, "请填写卡主姓名!", 0);
                    return;
                }
              /*  if (TextUtils.isEmpty(mAdapterType.getCheckTitle( ))) {
                    ToastUtils.midToast(RechargeBankActivity.this, "请选择转账渠道!", 0);
                    return;
                }*/
                /**
                 * 36.	充值上传
                 1)	请求地址：
                 /v1.0/charge/save
                 2)	请求方式:post
                 3)	请求参数说明：字段名	类型	长度	是否必填	备注
                 auth_token	string		是	token
                 amount	int		是	充值金额
                 id	int		是	充值类型ID
                 */
                final String value = mDepositAmount.getText().toString();

                ChargeUploadElement element = new ChargeUploadElement();
                element.setAmount(value);
                element.setId(mBean == null ? "" : String.valueOf(mBean.getId()));

                SSQSApplication.apiClient(classGuid).chargeUpload(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            Intent intent = new Intent(RechargeBankActivity.this, RechargeResultAcitvity.class);
                            intent.putExtra(Constent.ACCOUNT, value);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.midToast(RechargeBankActivity.this, result.getMessage(), 0);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void setCopy(String copyStr) {
        if (TextUtils.isEmpty(copyStr)) {
            ToastUtils.midToast(UIUtils.getContext(), "没有需要复制的内容", 0);
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
            ClipboardManager copy = (ClipboardManager) RechargeBankActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            copy.setText(copyStr);
        } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager copyq = (android.text.ClipboardManager) RechargeBankActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            copyq.setText(copyStr);
        }
    }

    @Override
    protected void initListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                String[] splitend = date.split("-");
                StringBuilder buffer = new StringBuilder();

                for (int i = 0; i < splitend.length; i++) {
                    String s = splitend[i];
                    if (s.length() >= 2) {
                        buffer.append(s);
                    } else {
                        s = "0" + s;
                        buffer.append(s);
                    }
                    if (i != 2)
                        buffer.append("-");
                }
                mTransferAccountsTime.setText(buffer);
                mPop.dismiss();
            }
        });
        mClear.setOnClickListener(this);
        mToday.setOnClickListener(this);
        mClose.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new TypeRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String type = mAdapter.getRechargeType(position);
                mPopType.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.betting_pop_calendar_clear:
                mTransferAccountsTime.setText("");
                calendarView.setDate(mDate.getTime());
                mPop.dismiss();
                break;
            case R.id.betting_pop_calendar_today:
                calendarView.setDate(mDate.getTime());
                mTransferAccountsTime.setText(mCurTime);
                mPop.dismiss();
                break;
            case R.id.betting_pop_calendar_close:
                mPop.dismiss();
                break;

            default:
                break;
        }
    }
}
