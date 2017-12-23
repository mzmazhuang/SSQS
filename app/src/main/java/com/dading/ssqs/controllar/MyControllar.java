package com.dading.ssqs.controllar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dading.ssqs.R;
import com.dading.ssqs.activity.AccountDetailActivity;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.ChangePhotoActivity;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.NewBindBankCardActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.ProxyCenterActivity;
import com.dading.ssqs.activity.RechargeDetailActivity;
import com.dading.ssqs.activity.RecomCodePrizeActivity;
import com.dading.ssqs.activity.RecomWardsActivity;
import com.dading.ssqs.activity.SuggestionActivity;
import com.dading.ssqs.activity.WithDrawActivity;
import com.dading.ssqs.activity.WithDrawDentailActivity;
import com.dading.ssqs.adapter.MyAdapter;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 17:06
 * 描述	      我的
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyControllar extends BaseTabsContainer {

    private static final String TAG = "MyControllar";
    private ListView mMyLv;
    private LinearLayout mNoLoading;
    private LinearLayout mLoading;
    private TextView mLoadingNickname;
    private ImageView mMyPhoto;
    private String mSt;
    private LoadingBean mBean;
    public MyBroadcastReciver mReceiver;
    private TextView mRmbNum;
    private LinearLayout mWithDrawBtn;
    private LinearLayout mRechargeBtn;

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        setVisbilityViews(mListTitle, mContenMyLy);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContent, R.layout.mypager, null);
        mMyLv = (ListView) view.findViewById(R.id.my_lv);

        View headview = View.inflate(context, R.layout.mypager_header, null);

        mNoLoading = (LinearLayout) headview.findViewById(R.id.my_gv_myinfo_noloading);

        mMyPhoto = (ImageView) headview.findViewById(R.id.my_photo);
        mLoading = (LinearLayout) headview.findViewById(R.id.my_gv_myinfo_loading);
        mLoadingNickname = (TextView) headview.findViewById(R.id.my_nickname);

        mRmbNum = (TextView) headview.findViewById(R.id.my_rmb_num);
        mRechargeBtn = (LinearLayout) headview.findViewById(R.id.my_recharge_btn);
        mWithDrawBtn = (LinearLayout) headview.findViewById(R.id.my_with_draw_btn);

        mMyLv.addHeaderView(headview);
        return view;
    }

    @Override
    public void initData() {
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mNoLoading.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            setDataView();
        } else {
            mNoLoading.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
        }

        mReceiver = new MyBroadcastReciver();
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_ACTION);
        UIUtils.ReRecevice(mReceiver, Constent.SERIES);
        UIUtils.ReRecevice(mReceiver, Constent.IS_VIP);
        UIUtils.ReRecevice(mReceiver, Constent.REFRESH_MONY);

        ArrayList<Integer> listIcon = new ArrayList<>();
        listIcon.add(R.mipmap.daili);
        listIcon.add(R.mipmap.account1);
        listIcon.add(R.mipmap.recharge_r);
        listIcon.add(R.mipmap.zhanghu);
        listIcon.add(R.mipmap.record1);
        listIcon.add(R.mipmap.free_gold);
        listIcon.add(R.mipmap.invitation);
        listIcon.add(R.mipmap.invitation_gold);
        listIcon.add(R.mipmap.opinion);

        ArrayList<String> listTitle = new ArrayList<>();
        listTitle.add(mContent.getString(R.string.proxy_center));
        listTitle.add(mContent.getString(R.string.with_draw_detail));
        listTitle.add(mContent.getString(R.string.recharge_detail));
        listTitle.add(mContent.getString(R.string.account_detail));
        listTitle.add(mContent.getString(R.string.betting_record));
        listTitle.add(mContent.getString(R.string.free_get_glod));
        listTitle.add(mContent.getString(R.string.recom_wards));
        listTitle.add(mContent.getString(R.string.recom_code_prize));
        listTitle.add(mContent.getString(R.string.suggestion));

        mMyLv.setAdapter(new MyAdapter(mContent, listIcon, listTitle));
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mReceiver);
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case Constent.LOADING_ACTION:
                    //通过传递广播的数据来判断是否登录成功
                    boolean b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                    LogUtil.util(TAG, "进入my的广播------------------------------:");
                    if (b) {
                        setInfo();
                    } else {
                        mNoLoading.setVisibility(View.VISIBLE);
                        mLoading.setVisibility(View.GONE);
                    }
                    break;
                case Constent.REFRESH_MONY:
                    int banlance = UIUtils.getSputils().getInt("banlance", 0);
                    if (banlance > 0) {
                        String num = (Integer.valueOf(UIUtils.getSputils().getString(Constent.GLODS, "")) + banlance) + "";

                        UIUtils.getSputils().putString(Constent.GLODS, num);
                        
                        mRmbNum.setText(num);
                    }
                    break;
            }
        }
    }

    private void setDataView() {
        setInfo();
    }

    private void setInfo() {
        mNoLoading.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mSt = UIUtils.getSputils().getString(Constent.LOADING_STATE_SP, "nobean");
        LogUtil.util(TAG, "我的SP返回数据是------------------------------:" + mSt);

        if (!"nobean".equals(mSt)) {
            mBean = JSON.parseObject(mSt, LoadingBean.class);
            if (mBean != null) {
                UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, mBean.isBindCard == 1);

                UIUtils.getSputils().putBoolean(Constent.USER_TYPE, mBean.userType == 3);
                UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, mBean.userType);

                if (TextUtils.isEmpty(mBean.avatar)) {
                    switch (mBean.sex) {
                        case 1:
                            mMyPhoto.setImageResource(R.mipmap.touxiang_nan);
                            break;
                        case 2:
                            mMyPhoto.setImageResource(R.mipmap.touxiang_nv);
                            break;
                        case 3:
                            mMyPhoto.setImageResource(R.mipmap.touxiang_baomi);
                            break;

                        default:
                            break;
                    }
                } else {
                    Glide.with(UIUtils.getContext()).load(mBean.avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(mMyPhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContent.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mMyPhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                mLoadingNickname.setText(mBean.username);

                String text = mBean.diamond + "";
                UIUtils.getSputils().putString(Constent.DIAMONDS, text);

                String num = mBean.banlance + "";
                UIUtils.getSputils().putString(Constent.GLODS, num);
                mRmbNum.setText(num);
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mMyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private Intent mIntent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean b = UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false);

                switch (position) {
                    //因为加了一个头所以是1
                    case 1:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                            mIntent = new Intent(mContent, ProxyCenterActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;

                    case 2:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            if (b) {
                                TmtUtils.midToast(mContent, "试玩账号不能进行充值，提现，查看提现、充值、账户明细!", 0);
                                return;
                            }
                            mIntent = new Intent(mContent, WithDrawDentailActivity.class);
                        } else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 3:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            if (b) {
                                TmtUtils.midToast(mContent, "试玩账号不能进行充值，提现，查看提现、充值、账户明细!", 0);
                                return;
                            }
                            mIntent = new Intent(mContent, RechargeDetailActivity.class);
                        } else {
                            mIntent = new Intent(mContent, LoginActivity.class);
                        }
                        break;
                    case 4:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            if (b) {
                                TmtUtils.midToast(mContent, "试玩账号不能进行充值，提现，查看提现、充值、账户明细!", 0);
                                return;
                            }
                            mIntent = new Intent(mContent, AccountDetailActivity.class);
                        } else {
                            mIntent = new Intent(mContent, LoginActivity.class);
                        }
                        break;
                    case 5:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                            mIntent = new Intent(mContent, BettingRecordActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 6:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                            mIntent = new Intent(mContent, HomeFreeGlodActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 7:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                            mIntent = new Intent(mContent, RecomWardsActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 8:
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                            mIntent = new Intent(mContent, RecomCodePrizeActivity.class);
                        else
                            mIntent = new Intent(mContent, LoginActivity.class);
                        break;
                    case 9:
                        mIntent = new Intent(mContent, SuggestionActivity.class);
                        break;
                    default:
                        break;
                }
                if (mIntent != null)
                    mContent.startActivity(mIntent);
            }
        });
        mMyPhoto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mRmbNum.setText(UIUtils.getSputils().getString(Constent.DIAMONDS, ""));
                }
            }
        });
        mLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, ChangePhotoActivity.class);
                intent.putExtra(Constent.MY_INFO, mSt);
                startActivity(intent);
            }
        });
        mMyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, ChangePhotoActivity.class);
                intent.putExtra(Constent.MY_INFO, mSt);
                startActivity(intent);
            }
        });

        mNoLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, LoginActivity.class);
                intent.setAction(Constent.LOADING_ACTION_MY);
                startActivity(intent);
            }
        });

        mRechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false)) {
                        TmtUtils.midToast(mContent, "试玩账号不能进行充值，提现，和查看提现明细、账户明细!", 0);
                        return;
                    }
                    Intent intent = new Intent(mContent, NewRechargeActivity.class);
                    startActivity(intent);
                } else {
                    Intent mIntent = new Intent(mContent, LoginActivity.class);
                    startActivity(mIntent);
                }
            }
        });
        mWithDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false)) {
                        TmtUtils.midToast(mContent, "试玩账号不能进行充值，提现，和查看提现明细、账户明细!", 0);
                        return;
                    }
                    Intent intent;
                    if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                        intent = new Intent(mContent, WithDrawActivity.class);
                    } else {
                        intent = new Intent(mContent, NewBindBankCardActivity.class);
                    }
                    startActivity(intent);
                } else {
                    Intent mIntent = new Intent(mContent, LoginActivity.class);
                    startActivity(mIntent);
                }
            }
        });
    }
}
