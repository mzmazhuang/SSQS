package com.dading.ssqs.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.newAdapter.BankAdapter;
import com.dading.ssqs.adapter.newAdapter.FindTabAdapter;
import com.dading.ssqs.adapter.newAdapter.SelectRechargeMoneyAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.SelectRechargeMoneyView;
import com.dading.ssqs.fragment.recharge.BankFragment;
import com.dading.ssqs.fragment.recharge.OnLineFragment;
import com.dading.ssqs.fragment.recharge.WxFragment;
import com.dading.ssqs.fragment.recharge.ZfbFragment;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static com.dading.ssqs.bean.Constent.RECHARGE_INFO;

/**
 * Created by mazhuang on 2017/11/24.
 */

public class NewRechargeActivity extends BaseActivity {

    private EditText moneyView;
    private SelectRechargeMoneyView selectRechargeMoneyView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private WxFragment wxFragment;
    private ZfbFragment zfbFragment;
    private BankFragment bankFragment;
    private OnLineFragment onLineFragment;

    private Context mContext;

    private List<WXDFBean> items;

    private int mType = 0;

    @Override
    protected int setLayoutId() {
        return 0;
    }

    //代码化布局
    @Override
    protected View getContentView() {
        mContext = this;

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE);

        TitleCell titleCell = new TitleCell(this, getResources().getString(R.string.recharge));
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout inputLayout = new RelativeLayout(this);
        inputLayout.setFocusable(true);
        inputLayout.setFocusableInTouchMode(true);
        topLayout.addView(inputLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50, 10, 8, 10, 0));

        TextView chargeMoneyView = new TextView(this);
        chargeMoneyView.setId(R.id.chargeMoney);
        chargeMoneyView.setText(LocaleController.getString(R.string.recharge_num) + ":");
        chargeMoneyView.setTextColor(0xFF888999);
        chargeMoneyView.setTextSize(14);
        inputLayout.addView(chargeMoneyView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

        TextView tvMoney = new TextView(this);
        tvMoney.setTextSize(14);
        tvMoney.setText(LocaleController.getString(R.string.yuan));
        tvMoney.setTextColor(0xFFFC3232);
        RelativeLayout.LayoutParams moneyLayoutParams = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 10, 0);
        moneyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        moneyLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        inputLayout.addView(tvMoney, moneyLayoutParams);

        moneyView = new EditText(this);
        moneyView.setInputType(InputType.TYPE_CLASS_NUMBER);
        moneyView.setBackground(null);
        moneyView.setTextSize(14);
        moneyView.setGravity(Gravity.CENTER_VERTICAL);
        moneyView.setTextColor(0xFFFF7426);
        moneyView.setText("10");//默认值
        RelativeLayout.LayoutParams layoutParams = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 10, 0, 10, 0);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, chargeMoneyView.getId());
        layoutParams.addRule(RelativeLayout.LEFT_OF, tvMoney.getId());
        inputLayout.addView(moneyView, layoutParams);

        selectRechargeMoneyView = new SelectRechargeMoneyView(this);
        selectRechargeMoneyView.setListener(new SelectRechargeMoneyAdapter.OnTextClickListener() {
            @Override
            public void onClick(String text) {
                moneyView.setText(text);
            }
        });
        topLayout.addView(selectRechargeMoneyView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10, 0, 10, 0));

        TextView tipView = new TextView(this);
        tipView.setTextSize(14);
        tipView.setText(LocaleController.getString(R.string.please_selelct_recharge));
        tipView.setTextColor(0xFF000000);
        topLayout.addView(tipView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15, 10, 0, 0));

        tabLayout = new TabLayout(this);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        container.addView(tabLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30, 0, 10, 0, 0));

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mType = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        container.addView(viewPager, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        List<String> mListTitle = new ArrayList<>();
        mListTitle.add("微信支付");
        mListTitle.add("支付宝支付");
        mListTitle.add("银行转账");
        mListTitle.add("在线支付");

        wxFragment = new WxFragment(listener);
        zfbFragment = new ZfbFragment(listener);
        bankFragment = new BankFragment(listener);
        onLineFragment = new OnLineFragment();

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(wxFragment);
        listFragments.add(zfbFragment);
        listFragments.add(bankFragment);
        listFragments.add(onLineFragment);

        for (int i = 0; i < mListTitle.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mListTitle.get(i)));
        }

        FindTabAdapter mFindTabAdapter = new FindTabAdapter(getSupportFragmentManager(), listFragments, mListTitle);

        viewPager.setAdapter(mFindTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getData();
        return container;
    }

    private BankAdapter.OnRechargeClickListener listener = new BankAdapter.OnRechargeClickListener() {
        @Override
        public void onClick(WXDFBean.InfoBean bean) {
            int money = Integer.valueOf(moneyView.getText().toString());
            if (money <= 0) {
                TmtUtils.midToast(UIUtils.getContext(), "请输入具体金额", 0);
                return;
            }
            WXDFBean.InfoBean newBean = bean;

            if (newBean.getAddrType() == 1) {
                newBean.setMoney(money);
            } else {
                newBean.setMoney(money);
                newBean.setPayType(newBean.getId());
                newBean.setRemark(items.get(mType).getRemark());
            }

            int addrType = newBean.getAddrType();

            Intent intent;
            Bundle bundle;
            if (addrType == 1) {
                intent = new Intent(mContext, RechargeBankActivity.class);

                bundle = new Bundle();
                bundle.putSerializable(Constent.RECHARGE_BANK, bean);

                setStartIntent(bundle, intent);
            } else if (addrType == 2 || addrType == 4 || addrType == 6) {
                intent = new Intent(mContext, RechargeActivity.class);

                bundle = new Bundle();
                bundle.putSerializable(RECHARGE_INFO, newBean);

                setStartIntent(bundle, intent);
            } else if (addrType == 3) {
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(newBean.getBankAddress());
                intent.setData(content_url);
                startActivity(intent);
            }
        }
    };

    private void setStartIntent(Bundle bundle, Intent intent) {
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getData() {

        SSQSApplication.apiClient(classGuid).getChargeList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    items = (List<WXDFBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        if (items.get(0).getMoneys() != null) {
                            selectRechargeMoneyView.setList(items.get(0).getMoneys());
                        }

                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getName().equals("微信支付")) {
                                wxFragment.setList(items.get(i).getInfo());
                            } else if (items.get(i).getName().equals("支付宝支付")) {
                                zfbFragment.setList(items.get(i).getInfo());
                            } else if (items.get(i).getName().equals("银行转账")) {
                                bankFragment.setList(items.get(i).getInfo());
                            }
                        }
                    }
                } else {
                    TmtUtils.midToast(mContext, result.getMessage(), 0);
                }
            }
        });
    }
}
