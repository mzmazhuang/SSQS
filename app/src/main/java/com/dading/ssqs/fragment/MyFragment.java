package com.dading.ssqs.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.activity.AccountDetailActivity;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.ChangePhotoActivity;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MoreSettingActivity;
import com.dading.ssqs.activity.MyMessageActivity;
import com.dading.ssqs.activity.NewBindBankCardActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.ProxyCenterActivity;
import com.dading.ssqs.activity.RechargeDetailActivity;
import com.dading.ssqs.activity.RecomCodePrizeActivity;
import com.dading.ssqs.activity.RecomWardsActivity;
import com.dading.ssqs.activity.SuggestionActivity;
import com.dading.ssqs.activity.WithDrawActivity;
import com.dading.ssqs.activity.WithDrawDentailActivity;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.components.AvatarView;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

/**
 * Created by mazhuang on 2018/1/4.
 */

public class MyFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private boolean hasInit = false;

    private AvatarView noLoadingAvatarView;
    private AvatarView avatarView;
    private ImageView messageView;

    private TextView nickNameTextView;
    private TextView numberTextView;
    private TextView signTextView;
    private TextView moneyTextView;

    private LinearLayout rechargeLayout;
    private LinearLayout withdrawalLayout;
    private LinearLayout receiveLayout;

    private LinearLayout infoLayout;
    private LinearLayout noLoadingAvatarLayout;

    private MeCell accountDetailsCell;
    private MeCell bettingRecordCell;
    private MeCell rechargeRecordCell;
    private MeCell withdrawalRecordCell;
    private MeCell agencyCenterCell;
    private MeCell inviteFriendCell;
    private MeCell inviteMoneyCell;
    private MeCell settingCell;
    private MeCell feedbackCell;

    private MyBroadcastReciver mReceiver;

    private String mSt;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UIUtils.UnReRecevice(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        mReceiver = new MyBroadcastReciver();
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_ACTION);
        UIUtils.ReRecevice(mReceiver, Constent.SERIES);
        UIUtils.ReRecevice(mReceiver, Constent.IS_VIP);
        UIUtils.ReRecevice(mReceiver, Constent.REFRESH_MONY);

        return initView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true;
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    updateInfo();
                }
            }
        }
    }

    private void updateInfo() {
        mSt = UIUtils.getSputils().getString(Constent.LOADING_STATE_SP, "nobean");

        if (!TextUtils.isEmpty(mSt) && !"nobean".equals(mSt)) {
            noLoadingAvatarLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);

            LoadingBean mBean = JSON.parseObject(mSt, LoadingBean.class);
            if (mBean != null) {
                UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, mBean.isBindCard == 1);

                UIUtils.getSputils().putBoolean(Constent.USER_TYPE, mBean.userType == 3);
                UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, mBean.userType);

                if (TextUtils.isEmpty(mBean.avatar)) {
                    switch (mBean.sex) {
                        case 1:
                            nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_male, 0);
                            break;
                        case 2:
                            nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_female, 0);
                            break;
                        case 3:
                            nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_other, 0);
                            break;

                        default:
                            break;
                    }
                } else {
                    avatarView.setImageResource(mBean.avatar);
                }

                nickNameTextView.setText(mBean.username);

                String text = mBean.diamond + "";
                UIUtils.getSputils().putString(Constent.DIAMONDS, text);

                String num = mBean.banlance + "";
                UIUtils.getSputils().putString(Constent.GLODS, num);
                moneyTextView.setText(num);

                numberTextView.setText("会员号:" + mBean.id);
                signTextView.setText(mBean.signature);
            }
        } else {
            noLoadingAvatarLayout.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
        }
    }

    private View initView() {
        LinearLayout container = new LinearLayout(mContext);
        container.setBackgroundColor(Color.WHITE);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        RelativeLayout topLayout = new RelativeLayout(mContext);
        topLayout.setBackgroundResource(R.mipmap.ic_my_top_background);
        contentLayout.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 220));

        messageView = new ImageView(mContext);
        messageView.setBackgroundDrawable(AndroidUtilities.createBarSelectorDrawable());
        messageView.setScaleType(ImageView.ScaleType.CENTER);
        messageView.setImageResource(R.mipmap.ic_my_message);
        messageView.setOnClickListener(this);
        topLayout.addView(messageView, LayoutHelper.createRelative(48, 48, 0, 10, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT));

        //已经登录的信息布局
        infoLayout = new LinearLayout(mContext);
        infoLayout.setVisibility(View.GONE);
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.setOnClickListener(this);
        topLayout.addView(infoLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 35, 50, 0));

        avatarView = new AvatarView(mContext);
        avatarView.setImageResource(R.mipmap.ic_default_avatar);
        infoLayout.addView(avatarView, LayoutHelper.createLinear(90, 90));

        LinearLayout infoContentLayout = new LinearLayout(mContext);
        infoContentLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.addView(infoContentLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15, 0, 0, 0));

        nickNameTextView = new TextView(mContext);
        nickNameTextView.setTextColor(Color.WHITE);
        nickNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        nickNameTextView.setTextSize(14);
        nickNameTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        nickNameTextView.setSingleLine();
        nickNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        infoContentLayout.addView(nickNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        numberTextView = new TextView(mContext);
        numberTextView.setTextColor(0xFFDCE1FF);
        numberTextView.setTextSize(12);
        numberTextView.setSingleLine();
        numberTextView.setEllipsize(TextUtils.TruncateAt.END);
        infoContentLayout.addView(numberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0));

        signTextView = new TextView(mContext);
        signTextView.setTextColor(0xFFDCE1FF);
        signTextView.setTextSize(12);
        signTextView.setSingleLine();
        signTextView.setEllipsize(TextUtils.TruncateAt.END);
        infoContentLayout.addView(signTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0));

        moneyTextView = new TextView(mContext);
        moneyTextView.setTextSize(14);
        moneyTextView.setTextColor(Color.WHITE);
        moneyTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        moneyTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_gold, 0, 0, 0);
        moneyTextView.setSingleLine();
        moneyTextView.setEllipsize(TextUtils.TruncateAt.END);
        infoContentLayout.addView(moneyTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0));

        //没有登录的布局
        noLoadingAvatarLayout = new LinearLayout(mContext);
        noLoadingAvatarLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        noLoadingAvatarLayout.setOrientation(LinearLayout.VERTICAL);
        noLoadingAvatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentLogin();
            }
        });
        topLayout.addView(noLoadingAvatarLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 35, 0, 0, RelativeLayout.CENTER_HORIZONTAL));

        noLoadingAvatarView = new AvatarView(mContext);
        noLoadingAvatarView.setImageResource(R.mipmap.ic_default_avatar);
        noLoadingAvatarLayout.addView(noLoadingAvatarView, LayoutHelper.createLinear(90, 90));

        TextView noLoadingTipTextView = new TextView(mContext);
        noLoadingTipTextView.setTextSize(14);
        noLoadingTipTextView.setTextColor(Color.WHITE);
        noLoadingTipTextView.setText(LocaleController.getString(R.string.no_loading_tip));
        noLoadingAvatarLayout.addView(noLoadingTipTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 10, 0, 0));

        LinearLayout operationLayout = new LinearLayout(mContext);
        operationLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.addView(operationLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        rechargeLayout = new LinearLayout(mContext);
        rechargeLayout.setGravity(Gravity.CENTER_VERTICAL);
        rechargeLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext));
        rechargeLayout.setOnClickListener(this);
        operationLayout.addView(rechargeLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        TextView rechargeTextView = new TextView(mContext);
        rechargeTextView.setTextSize(14);
        rechargeTextView.setTextColor(0xFF323232);
        rechargeTextView.setText(LocaleController.getString(R.string.recharge));
        rechargeTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        rechargeTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_recharge, 0, 0, 0);
        rechargeLayout.addView(rechargeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 0, 0, 0));

        withdrawalLayout = new LinearLayout(mContext);
        withdrawalLayout.setGravity(Gravity.CENTER);
        withdrawalLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext));
        withdrawalLayout.setOnClickListener(this);
        operationLayout.addView(withdrawalLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        TextView withdrawalTextView = new TextView(mContext);
        withdrawalTextView.setTextSize(14);
        withdrawalTextView.setTextColor(0xFF323232);
        withdrawalTextView.setText(LocaleController.getString(R.string.with_draw));
        withdrawalTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        withdrawalTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_withdraw, 0, 0, 0);
        withdrawalLayout.addView(withdrawalTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        receiveLayout = new LinearLayout(mContext);
        receiveLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        receiveLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext));
        receiveLayout.setOnClickListener(this);
        operationLayout.addView(receiveLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        TextView receiveTextView = new TextView(mContext);
        receiveTextView.setTextSize(14);
        receiveTextView.setTextColor(0xFF323232);
        receiveTextView.setText(LocaleController.getString(R.string.free_get_glod));
        receiveTextView.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        receiveTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_receive, 0, 0, 0);
        receiveLayout.addView(receiveTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0));

        View lineView = new View(mContext);
        lineView.setBackgroundColor(0xFFF5F4F9);
        contentLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8, 0, 15, 0, 0));

        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setVerticalScrollBarEnabled(false);
        contentLayout.addView(scrollView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout cellLayout = new LinearLayout(mContext);
        cellLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(cellLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        accountDetailsCell = new MeCell(mContext, R.mipmap.ic_my_account_details, LocaleController.getString(R.string.account_detail), true);
        accountDetailsCell.setOnClickListener(this);
        cellLayout.addView(accountDetailsCell);

        bettingRecordCell = new MeCell(mContext, R.mipmap.ic_my_betting_record, LocaleController.getString(R.string.betting_history), true);
        bettingRecordCell.setOnClickListener(this);
        cellLayout.addView(bettingRecordCell);

        rechargeRecordCell = new MeCell(mContext, R.mipmap.ic_recharge_record, LocaleController.getString(R.string.recharge_detail), true);
        rechargeRecordCell.setOnClickListener(this);
        cellLayout.addView(rechargeRecordCell);

        withdrawalRecordCell = new MeCell(mContext, R.mipmap.ic_witharaw_record, LocaleController.getString(R.string.witharaw_detail), false);
        withdrawalRecordCell.setOnClickListener(this);
        cellLayout.addView(withdrawalRecordCell);

        View lineView1 = new View(mContext);
        lineView1.setBackgroundColor(0xFFF5F4F9);
        cellLayout.addView(lineView1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));

        agencyCenterCell = new MeCell(mContext, R.mipmap.ic_agency_center, LocaleController.getString(R.string.proxy_center), true);
        agencyCenterCell.setOnClickListener(this);
        cellLayout.addView(agencyCenterCell);

        inviteFriendCell = new MeCell(mContext, R.mipmap.ic_invite_friend, LocaleController.getString(R.string.invite_friend), true);
        inviteFriendCell.setOnClickListener(this);
        cellLayout.addView(inviteFriendCell);

        inviteMoneyCell = new MeCell(mContext, R.mipmap.ic_invite_money, LocaleController.getString(R.string.invite_money), false);
        inviteMoneyCell.setOnClickListener(this);
        cellLayout.addView(inviteMoneyCell);

        View lineView2 = new View(mContext);
        lineView2.setBackgroundColor(0xFFF5F4F9);
        cellLayout.addView(lineView2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));

        settingCell = new MeCell(mContext, R.mipmap.ic_my_settings, LocaleController.getString(R.string.more_setting), true);
        settingCell.setOnClickListener(this);
        cellLayout.addView(settingCell);

        feedbackCell = new MeCell(mContext, R.mipmap.ic_my_feedback, LocaleController.getString(R.string.suggestion), false);
        feedbackCell.setOnClickListener(this);
        cellLayout.addView(feedbackCell);

        View lineView3 = new View(mContext);
        lineView3.setBackgroundColor(0xFFF5F4F9);
        cellLayout.addView(lineView3, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));

        return container;
    }

    private void intentLogin() {
        Intent mIntent = new Intent(mContext, LoginActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View view) {
        if (view == settingCell) {//更多设置
            Intent intentSetting = new Intent(mContext, MoreSettingActivity.class);
            startActivity(intentSetting);
        } else if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            intentLogin();
        } else {
            boolean isTourist = UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false);//是否是试玩

            Intent intent = null;
            if (view == messageView) {//消息
                intent = new Intent(mContext, MyMessageActivity.class);
            } else if (view == rechargeLayout) {//充值
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能进行充值!", 0);
                    return;
                }
                intent = new Intent(mContext, NewRechargeActivity.class);
            } else if (view == withdrawalLayout) {//提现
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能进行提现!", 0);
                    return;
                }
                if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                    intent = new Intent(mContext, WithDrawActivity.class);
                } else {
                    intent = new Intent(mContext, NewBindBankCardActivity.class);
                }
            } else if (view == receiveLayout) {//领币
                intent = new Intent(mContext, HomeFreeGlodActivity.class);
            } else if (view == accountDetailsCell) {//账户明细
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看账户明细!", 0);
                    return;
                }
                intent = new Intent(mContext, AccountDetailActivity.class);
            } else if (view == bettingRecordCell) {//投注记录
                intent = new Intent(mContext, BettingRecordActivity.class);
            } else if (view == rechargeRecordCell) {//充值记录
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看充值记录!", 0);
                    return;
                }
                intent = new Intent(mContext, RechargeDetailActivity.class);
            } else if (view == withdrawalRecordCell) {//提款记录
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看提现!", 0);
                    return;
                }
                intent = new Intent(mContext, WithDrawDentailActivity.class);
            } else if (view == agencyCenterCell) {//代理中心
                intent = new Intent(mContext, ProxyCenterActivity.class);
            } else if (view == inviteFriendCell) {//邀请好友
                intent = new Intent(mContext, RecomWardsActivity.class);
            } else if (view == inviteMoneyCell) {//邀请领奖
                intent = new Intent(mContext, RecomCodePrizeActivity.class);
            } else if (view == feedbackCell) {//意见反馈
                intent = new Intent(mContext, SuggestionActivity.class);
            } else if (view == infoLayout) {//修改信息
                intent = new Intent(mContext, ChangePhotoActivity.class);
                intent.putExtra(Constent.MY_INFO, mSt);
            }

            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case Constent.LOADING_ACTION:
                    //通过传递广播的数据来判断是否登录成功
                    boolean b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                    if (b) {
                        updateInfo();
                    } else {
                        noLoadingAvatarLayout.setVisibility(View.VISIBLE);
                        infoLayout.setVisibility(View.GONE);
                    }
                    break;
                case Constent.REFRESH_MONY:
                    int banlance = UIUtils.getSputils().getInt("banlance", 0);
                    String num;
                    if (banlance > 0) {
                        num = (Integer.valueOf(UIUtils.getSputils().getString(Constent.GLODS, "")) + banlance) + "";

                        UIUtils.getSputils().putString(Constent.GLODS, num);
                    } else {
                        num = UIUtils.getSputils().getString(Constent.GLODS, "");
                    }
                    moneyTextView.setText(num);
                    break;
            }
        }
    }

    class MeCell extends RelativeLayout {

        private ImageView iconView;
        private TextView tvTitleView;

        public MeCell(Context context, int resId, String title, boolean isLine) {
            super(context);

            setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(context));
            setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

            iconView = new ImageView(context);
            iconView.setScaleType(ImageView.ScaleType.CENTER);
            iconView.setImageResource(resId);
            addView(iconView, LayoutHelper.createRelative(20, LayoutHelper.WRAP_CONTENT, 12, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

            tvTitleView = new TextView(context);
            tvTitleView.setTextSize(14);
            tvTitleView.setTextColor(0xFF323232);
            tvTitleView.setText(title);
            addView(tvTitleView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 42, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

            ImageView arrowView = new ImageView(context);
            arrowView.setImageResource(R.mipmap.ic_my_arrow);
            RelativeLayout.LayoutParams arrowLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0);
            arrowLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            arrowLP.addRule(RelativeLayout.CENTER_VERTICAL);
            addView(arrowView, arrowLP);

            if (isLine) {
                View view = new View(context);
                view.setBackgroundColor(0xFFEDEDED);
                RelativeLayout.LayoutParams viewLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1);
                viewLP.setMargins(AndroidUtilities.dp(42), 0, 0, 0);
                viewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                addView(view, viewLP);
            }
        }
    }
}
