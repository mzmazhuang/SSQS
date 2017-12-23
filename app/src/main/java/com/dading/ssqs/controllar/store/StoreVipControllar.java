package com.dading.ssqs.controllar.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.AwardExchangeElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.StoreBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/10 14:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StoreVipControllar {
    private static final String TAG = "StoreVipControllar";
    private final Context context;
    public View mRootView;
    @Bind(R.id.vip_privilege)
    ImageView mVipPrivilege;
    @Bind(R.id.sm_vip_vip1)
    ImageView mSmVipVip1;
    @Bind(R.id.store_buy_vip1)
    TextView mStoreBuyVip1;
    @Bind(R.id.sm_vip_vip2)
    ImageView mSmVipVip2;
    @Bind(R.id.store_buy_vip2)
    TextView mStoreBuyVip2;
    @Bind(R.id.sm_vip_vip3)
    ImageView mSmVipVip3;
    @Bind(R.id.store_buy_vip3)
    TextView mStoreBuyVip3;
    @Bind(R.id.sm_vip_vip4)
    ImageView mSmVipVip4;
    @Bind(R.id.store_buy_vip4)
    TextView mStoreBuyVip4;
    @Bind(R.id.vip_name1)
    TextView mVipName1;
    @Bind(R.id.store_buy_vip_leve1_text)
    TextView mStoreBuyVipLeve1Text;
    @Bind(R.id.vip_name2)
    TextView mVipName2;
    @Bind(R.id.store_buy_vip_leve2_text)
    TextView mStoreBuyVipLeve2Text;
    @Bind(R.id.vip_name3)
    TextView mVipName3;
    @Bind(R.id.store_buy_vip_leve3_text)
    TextView mStoreBuyVipLeve3Text;
    @Bind(R.id.vip_name4)
    TextView mVipName4;
    @Bind(R.id.store_buy_vip_leve4_text)
    TextView mStoreBuyVipLeve4Text;
    private View mView;
    private ArrayList<TextView> mListName;
    private ArrayList<ImageView> mListIm;
    private ArrayList<TextView> mListText;
    private ArrayList<TextView> mListPrice;
    private String mPrice;
    private List<StoreBean> mData;
    private ArrayList<ImageView> mListLy;
    private int mId;
    private String mVip;
    private View mPopView;
    private View mPopClose;
    private PopupWindow mPop;
    private RelativeLayout mPopLy;

    public StoreVipControllar(Context context) {
        this.context = context;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private View initView(Context context) {
        mPopView = View.inflate(context, R.layout.vip_privilege, null);
        mPopClose = ButterKnife.findById(mPopView, R.id.vip_privilege_close);
        mPopLy = ButterKnife.findById(mPopView, R.id.vip_privilege_pop_ly);
        mPop = PopUtil.popuMake(mPopView);
        mView = View.inflate(context, R.layout.store_vip, null);
        ButterKnife.bind(this, mView);
        mListName = new ArrayList<>();
        mListName.add(mVipName1);
        mListName.add(mVipName2);
        mListName.add(mVipName3);
        mListName.add(mVipName4);

        mListIm = new ArrayList<>();
        mListIm.add(mSmVipVip1);
        mListIm.add(mSmVipVip2);
        mListIm.add(mSmVipVip3);
        mListIm.add(mSmVipVip4);

        mListText = new ArrayList<>();
        mListText.add(mStoreBuyVipLeve1Text);
        mListText.add(mStoreBuyVipLeve2Text);
        mListText.add(mStoreBuyVipLeve3Text);
        mListText.add(mStoreBuyVipLeve4Text);

        mListPrice = new ArrayList<>();
        mListPrice.add(mStoreBuyVip1);
        mListPrice.add(mStoreBuyVip2);
        mListPrice.add(mStoreBuyVip3);
        mListPrice.add(mStoreBuyVip4);

        mListLy = new ArrayList<>();
        mListLy.add(mSmVipVip1);
        mListLy.add(mSmVipVip2);
        mListLy.add(mSmVipVip3);
        mListLy.add(mSmVipVip4);

        mSmVipVip1.setVisibility(View.GONE);
        mSmVipVip2.setVisibility(View.GONE);
        mSmVipVip3.setVisibility(View.GONE);
        mSmVipVip4.setVisibility(View.GONE);

        return mView;
    }

    private void initData() {
        /**
         1.	奖品列表
         a)	请求地址：
         /v1.0/award/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         1-道具2-转盘3-奖品4-钻石5-Vip
         */

        SSQSApplication.apiClient(0).getAwardType(5, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<StoreBean> items = (List<StoreBean>) result.getData();

                    if (items != null) {
                        mData = items;
                        processData(mData);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "商店vip失败信息");
                }
            }
        });
    }

    private void processData(List<StoreBean> data) {
        for (int i = 0; i < data.size(); i++) {
            StoreBean entity = data.get(i);
            if (entity != null) {
                SSQSApplication.glide.load(entity.itemImageUrl).error(R.mipmap.fail).centerCrop().into(mListIm.get(i));

                mListName.get(i).setText(entity.name);
                mListText.get(i).setText(entity.remark);
                String s = entity.cost + "钻";
                mListPrice.get(i).setText(s);
                mListLy.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    private void initListener() {
        mPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mPopLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mVipPrivilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
            }
        });
    }

    @OnClick({R.id.sm_vip_vip1_ly, R.id.sm_vip_vip2_ly, R.id.sm_vip_vip3_ly, R.id.sm_vip_vip4_ly})
    public void OnClik(View v) {
        if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((StoreActivity) context).finish();
            return;
        }
        Logger.d(TAG, "返回数据是------------------------------:" + UIUtils.getSputils().getInt(Constent.IS_VIP, 0));
        if (UIUtils.getSputils().getInt(Constent.IS_VIP, 0) != 0) {
            ToastUtils.midToast(context, "您已经是vip会员请勿重复购买!", 0);
            return;
        }
        switch (v.getId()) {
            case R.id.sm_vip_vip1_ly:
                if (mData != null && mData.size() > 0) {
                    mPrice = mData.get(0).cost;
                    mId = mData.get(0).id;
                    mVip = mData.get(0).name;
                }
                Logger.d(TAG, "购买Vip");
                break;
            case R.id.sm_vip_vip2_ly:
                if (mData != null && mData.size() > 1) {
                    mPrice = mData.get(1).cost;
                    mId = mData.get(1).id;
                    mVip = mData.get(1).name;
                }
                break;
            case R.id.sm_vip_vip3_ly:
                if (mData != null && mData.size() > 2) {
                    mPrice = mData.get(2).cost;
                    mId = mData.get(2).id;
                    mVip = mData.get(2).name;
                }
                break;
            case R.id.sm_vip_vip4_ly:
                if (mData != null && mData.size() > 3) {
                    mPrice = mData.get(3).cost;
                    mId = mData.get(3).id;
                    mVip = mData.get(3).name;
                }
                break;

            default:
                break;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("购买" + mVip);
        builder.setMessage("使用钻石兑换:" + mPrice);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     /v1.0/awardExchange/vip
                     b)	请求方式:
                     Post
                     c)	请求参数说明：
                     id: 兑奖vip ID
                     */
                    AwardExchangeElement element = new AwardExchangeElement();
                    element.setId(String.valueOf(mId));

                    SSQSApplication.apiClient(0).awardExchangeVip(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                String diamonsStr = UIUtils.getSputils().getString(Constent.DIAMONDS, "");
                                int diamons = Integer.parseInt(diamonsStr.replaceAll(",", ""));
                                UIUtils.getSputils().putString(Constent.DIAMONDS, (diamons - Integer.parseInt(mPrice.replaceAll(",", ""))) + "");

                                UIUtils.SendReRecevice(Constent.SERIES);

                                UIUtils.SendReRecevice(Constent.IS_VIP);
                                ToastUtils.midToast(context, "恭喜您购买了" + mVip + "会员!", 0);
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    ToastUtils.midToast(context, "兑换失败!" + result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ((StoreActivity) context).finish();
                }
                dialog.dismiss();
                //发送购买请求
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
