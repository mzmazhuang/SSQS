package com.dading.ssqs.controllar.store;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.DiamondExchangeElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.OrderBean;
import com.dading.ssqs.bean.StoreBean;
import com.dading.ssqs.bean.WXOrderBean;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dading.ssqs.bean.Constent.WX;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/10 14:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StoreDiamondsControllar implements View.OnClickListener {
    private static final String TAG = "StoreDiamondsControllar";
    private static final String APP_ID = "wxf03354b453566347";
    private final Context context;
    public View mRootView;
    @Bind(R.id.store_dimonds1)
    TextView mStoreDimonds1;
    @Bind(R.id.store_dimonds2)
    TextView mStoreDimonds2;
    @Bind(R.id.store_dimonds3)
    TextView mStoreDimonds3;
    @Bind(R.id.buy12)
    TextView mBuy1;
    @Bind(R.id.buy30)
    TextView mBuy2;
    @Bind(R.id.buy68)
    TextView mBuy3;
    @Bind(R.id.store_dimonds4)
    TextView mStoreDimonds4;
    @Bind(R.id.store_dimonds5)
    TextView mStoreDimonds5;
    @Bind(R.id.store_dimonds6)
    TextView mStoreDimonds6;
    @Bind(R.id.buy128)
    TextView mBuy4;
    @Bind(R.id.buy328)
    TextView mBuy5;
    @Bind(R.id.buy648)
    TextView mBuy6;
    @Bind(R.id.store_dimonds7)
    TextView mStoreDimonds7;
    @Bind(R.id.store_dimonds8)
    TextView mStoreDimonds8;
    @Bind(R.id.store_dimonds9)
    TextView mStoreDimonds9;
    @Bind(R.id.buy1000)
    TextView mBuy7;
    @Bind(R.id.buy3000)
    TextView mBuy8;
    @Bind(R.id.buy5000)
    TextView mBuy9;
    @Bind(R.id.store_dimonds_remark1)
    TextView mStoreDimondsRemark1;
    @Bind(R.id.store_dimonds_remark2)
    TextView mStoreDimondsRemark2;
    @Bind(R.id.store_dimonds_remark3)
    TextView mStoreDimondsRemark3;
    @Bind(R.id.store_dimonds_remark4)
    TextView mStoreDimondsRemark4;
    @Bind(R.id.store_dimonds_remark5)
    TextView mStoreDimondsRemark5;
    @Bind(R.id.store_dimonds_remark6)
    TextView mStoreDimondsRemark6;
    @Bind(R.id.store_dimonds_remark7)
    TextView mStoreDimondsRemark7;
    @Bind(R.id.store_dimonds_remark8)
    TextView mStoreDimondsRemark8;
    @Bind(R.id.store_dimonds_remark9)
    TextView mStoreDimondsRemark9;
    @Bind(R.id.buy1_ly)
    RelativeLayout mBuy1Ly;
    @Bind(R.id.buy2_ly)
    RelativeLayout mBuy2Ly;
    @Bind(R.id.buy3_ly)
    RelativeLayout mBuy3Ly;
    @Bind(R.id.buy4_ly)
    RelativeLayout mBuy4Ly;
    @Bind(R.id.buy5_ly)
    RelativeLayout mBuy5Ly;
    @Bind(R.id.buy6_ly)
    RelativeLayout mBuy6Ly;
    @Bind(R.id.buy7_ly)
    RelativeLayout mBuy7Ly;
    @Bind(R.id.buy8_ly)
    RelativeLayout mBuy8Ly;
    @Bind(R.id.buy9_ly)
    RelativeLayout mBuy9Ly;

    @Bind(R.id.ly1)
    LinearLayout mLy1;
    @Bind(R.id.ly11)
    LinearLayout mLy11;
    @Bind(R.id.ly2)
    LinearLayout mLy2;
    @Bind(R.id.ly22)
    LinearLayout mLy22;
    @Bind(R.id.ly3)
    LinearLayout mLy3;
    @Bind(R.id.ly33)
    LinearLayout mLy33;
    @Bind(R.id.diamons_grideview)
    GridView mGridView;
    @Bind(R.id.diamons_scoreview)
    ScrollView mscore;

    private View mView;

    private View mPopview;
    private RelativeLayout mPopBg;
    private ImageView mPayMethodClose;
    private TextView mPayMethodRmb;
    private TextView mPayMethodGlod;
    private RelativeLayout mPayMethodWxPay;
    private RelativeLayout mPayMethodAliyPay;
    private PopupWindow mPopu;
    private ArrayList<TextView> mDimons;
    private ArrayList<TextView> mRemark;
    private ArrayList<TextView> mPrice;
    private List<StoreBean> mData;
    private int mId;
    private IWXAPI mWxapi;
    private boolean mInstall;
    private ArrayList<RelativeLayout> LY;
    private ArrayList<LinearLayout> mList;
    private ArrayList<LinearLayout> mListPrie;

    public StoreDiamondsControllar(Context context) {
        this.context = context;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private View initView(Context context) {
        mView = View.inflate(context, R.layout.store_diamonds, null);
        ButterKnife.bind(this, mView);

        mPopview = View.inflate(context, R.layout.store_diamonds_pop, null);
        mPopBg = ButterKnife.findById(mPopview, R.id.pop_bg);
        mPayMethodClose = ButterKnife.findById(mPopview, R.id.pay_method_close);
        mPayMethodRmb = ButterKnife.findById(mPopview, R.id.pay_method_rmb);
        mPayMethodGlod = ButterKnife.findById(mPopview, R.id.pay_method_glod);
        mPayMethodWxPay = ButterKnife.findById(mPopview, R.id.pay_method_wx_pay);
        mPayMethodAliyPay = ButterKnife.findById(mPopview, R.id.pay_method_aliy_pay);
        return mView;
    }

    private void initData() {
        //mscore.setVisibility(View.GONE);
        mWxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mWxapi.registerApp(APP_ID);

        mList = new ArrayList<>();
        mList.add(mLy1);
        mList.add(mLy2);
        mList.add(mLy3);
        mListPrie = new ArrayList<>();
        mListPrie.add(mLy11);
        mListPrie.add(mLy22);
        mListPrie.add(mLy33);

        LY = new ArrayList<>();
        LY.add(mBuy1Ly);
        LY.add(mBuy2Ly);
        LY.add(mBuy3Ly);
        LY.add(mBuy4Ly);
        LY.add(mBuy5Ly);
        LY.add(mBuy6Ly);
        LY.add(mBuy7Ly);
        LY.add(mBuy8Ly);
        LY.add(mBuy9Ly);

        mDimons = new ArrayList<>();
        mDimons.add(mStoreDimonds1);
        mDimons.add(mStoreDimonds2);
        mDimons.add(mStoreDimonds3);
        mDimons.add(mStoreDimonds4);
        mDimons.add(mStoreDimonds5);
        mDimons.add(mStoreDimonds6);
        mDimons.add(mStoreDimonds7);
        mDimons.add(mStoreDimonds8);
        mDimons.add(mStoreDimonds9);

        mRemark = new ArrayList<>();
        mRemark.add(mStoreDimondsRemark1);
        mRemark.add(mStoreDimondsRemark2);
        mRemark.add(mStoreDimondsRemark3);
        mRemark.add(mStoreDimondsRemark4);
        mRemark.add(mStoreDimondsRemark5);
        mRemark.add(mStoreDimondsRemark6);
        mRemark.add(mStoreDimondsRemark7);
        mRemark.add(mStoreDimondsRemark8);
        mRemark.add(mStoreDimondsRemark9);

        mPrice = new ArrayList<>();
        mPrice.add(mBuy1);
        mPrice.add(mBuy2);
        mPrice.add(mBuy3);
        mPrice.add(mBuy4);
        mPrice.add(mBuy5);
        mPrice.add(mBuy6);
        mPrice.add(mBuy7);
        mPrice.add(mBuy8);
        mPrice.add(mBuy9);

        /**
         1.	奖品列表
         a)	请求地址：
         /v1.0/award/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         1-道具2-转盘3-奖品4-钻石5-Vip
         d)	返回格式
         1，4，5类型格式
         */

        SSQSApplication.apiClient(0).getAwardType(4, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<StoreBean> items = (List<StoreBean>) result.getData();

                    if (items != null) {
                        mData = items;
                        processData(mData);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "红人明星中獎失败信息");
                }
            }
        });
    }

    private void processData(List<StoreBean> bean) {
        // mGridView.setAdapter(new GVAdatapter(context,bean));
        for (int i = 0; i < bean.size(); i++) {
            LY.get(i).setVisibility(View.VISIBLE);
            mPrice.get(i).setVisibility(View.VISIBLE);
            if (i < bean.size()) {
                mDimons.get(i).setText(bean.get(i).name);
                String price = "¥" + bean.get(i).cost + "元";
                mPrice.get(i).setText(price);
                mRemark.get(i).setText(bean.get(i).remark);
                LY.get(i).setClickable(true);
                mPrice.get(i).setClickable(true);
            } else {
                LY.get(i).setClickable(false);
                mPrice.get(i).setClickable(false);
                mDimons.get(i).setText("");
                mPrice.get(i).setText("");
                mRemark.get(i).setText("");
                LY.get(i).setBackground(null);
                mPrice.get(i).setBackground(null);
            }
        }

        switch (bean.size()) {
            case 1:
            case 2:
            case 3:
                mList.get(0).setVisibility(View.VISIBLE);
                mListPrie.get(0).setVisibility(View.VISIBLE);
                mList.get(1).setVisibility(View.GONE);
                mListPrie.get(1).setVisibility(View.GONE);
                mList.get(1).setVisibility(View.GONE);
                mListPrie.get(1).setVisibility(View.GONE);
                break;
            case 4:
            case 5:
            case 6:
                mList.get(0).setVisibility(View.VISIBLE);
                mListPrie.get(0).setVisibility(View.VISIBLE);
                mList.get(1).setVisibility(View.VISIBLE);
                mListPrie.get(1).setVisibility(View.VISIBLE);
                mList.get(2).setVisibility(View.GONE);
                mListPrie.get(2).setVisibility(View.GONE);
                break;
            case 7:
            case 8:
            case 9:
                mList.get(0).setVisibility(View.VISIBLE);
                mListPrie.get(0).setVisibility(View.VISIBLE);
                mList.get(1).setVisibility(View.VISIBLE);
                mListPrie.get(1).setVisibility(View.VISIBLE);
                mList.get(2).setVisibility(View.VISIBLE);
                mListPrie.get(2).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void initListener() {
        mPayMethodClose.setOnClickListener(this);
        mPopBg.setOnClickListener(this);
        mPayMethodWxPay.setOnClickListener(this);
        mPayMethodAliyPay.setOnClickListener(this);
    }

    @OnClick({R.id.buy12, R.id.buy30, R.id.buy68, R.id.buy128, R.id.buy328, R.id.buy648, R.id.buy1000, R.id.buy3000,
            R.id.buy5000, R.id.buy1_ly, R.id.buy2_ly, R.id.buy3_ly, R.id.buy4_ly, R.id.buy5_ly, R.id.buy6_ly, R.id.buy7_ly,
            R.id.buy8_ly, R.id.buy9_ly})
    public void OnClik(View v) {
        String price = "0";
        mId = 0;
        switch (v.getId()) {
            case R.id.buy12:
            case R.id.buy1_ly:
                if (mData != null && mData.size() > 0) {
                    mId = 0;
                    price = mData.get(0).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy30:
            case R.id.buy2_ly:
                if (mData != null && mData.size() > 1) {
                    mId = 1;
                    price = mData.get(1).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy68:
            case R.id.buy3_ly:
                if (mData != null && mData.size() > 2) {
                    mId = 2;
                    price = mData.get(2).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy128:
            case R.id.buy4_ly:
                if (mData != null && mData.size() > 3) {
                    mId = 3;
                    price = mData.get(3).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy328:
            case R.id.buy5_ly:
                if (mData != null && mData.size() > 4) {
                    mId = 4;
                    price = mData.get(4).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy648:
            case R.id.buy6_ly:
                if (mData != null && mData.size() > 5) {
                    mId = 5;
                    price = mData.get(5).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy1000:
            case R.id.buy7_ly:
                if (mData != null && mData.size() > 6) {
                    mId = 6;
                    price = mData.get(6).cost;
                } else {
                    mBuy7.setVisibility(View.GONE);
                    return;
                }
                break;
            case R.id.buy3000:
            case R.id.buy8_ly:
                if (mData != null && mData.size() > 7) {
                    mId = 7;
                    price = mData.get(7).cost;
                } else {
                    return;
                }
                break;
            case R.id.buy5000:
            case R.id.buy9_ly:
                if (mData != null && mData.size() > 8) {
                    mId = 8;
                    price = mData.get(8).cost;
                } else {
                    return;
                }
                break;
            default:
                break;
        }
        mPopu = PopUtil.popuMake(mPopview);
        String s = "¥" + price + "元";
        mPayMethodRmb.setText(s);
        String s1 = price + "金币";
        mPayMethodGlod.setText(s1);
        int i = DensityUtil.dip2px(context, 50);
        int j = DensityUtil.dip2px(context, 200);

        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mPopu.showAtLocation(mView, Gravity.CENTER, j, i);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((StoreActivity) context).finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_method_close:
                mPopu.dismiss();
                break;
            case R.id.pop_bg:
                mPopu.dismiss();
                break;
            case R.id.pay_method_wx_pay:
                //调用微信
                mPayMethodWxPay.setClickable(false);
                sendWX();
                Logger.d(TAG, "调用微信");
                break;
            case R.id.pay_method_aliy_pay:
                //调用支付宝
                if (Build.MODEL.contains("OPPO")) {
                    mInstall = ((StoreActivity) context).CheckInstall(((StoreActivity) context).ALIPAY_PACKGE_NAME);
                    if (!mInstall) {
                        ToastUtils.midToast(context, "您还没有安装支付宝,请更换支付方式!", 0);
                        return;
                    }
                }
                Logger.d(TAG, "调用支付宝");
                getPay();
                mPayMethodAliyPay.setClickable(false);
                break;
            default:
                break;
        }
    }

    private void sendWX() {
        /**
         9.	钻石兑换
         a)	请求地址：
         /v1.0/awardExchange/zs
         b)	请求方式:
         post
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         id：奖品ID
         payType: 1:支付包2:微信3:苹果支付
         */
        DiamondExchangeElement element = new DiamondExchangeElement();
        element.setId(String.valueOf(mData.get(mId).id));
        element.setPayType("2");

        SSQSApplication.apiClient(0).diamondExchange2(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    WXOrderBean bean = (WXOrderBean) result.getData();

                    if (bean != null) {
                        sendWXOrder(bean);
                        mPayMethodWxPay.setClickable(true);
                        mPopu.dismiss();
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.midToast(context, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void sendWXOrder(WXOrderBean data) {
        PayReq req = new PayReq();
        req.appId = data.appid;
        req.partnerId = data.partnerid;
        req.prepayId = data.prepayid;
        req.nonceStr = data.noncestr;
        req.timeStamp = data.timestamp;
        req.packageValue = data.packages;
        req.sign = data.sign;
        req.extData = "app data";
        UIUtils.getSputils().putString(WX, data.orderID);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        mWxapi.sendReq(req);
    }

    private void getPay() {
        /**
         9.	钻石兑换
         a)	请求地址：
         /v1.0/awardExchange/zs
         b)	请求方式:
         post
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         id：奖品ID
         payType: 1:支付包2:微信3:苹果支付
         */
        DiamondExchangeElement element = new DiamondExchangeElement();
        element.setId(String.valueOf(mData.get(mId).id));
        element.setPayType("1");

        SSQSApplication.apiClient(0).diamondExchange(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    OrderBean bean = (OrderBean) result.getData();

                    if (bean != null) {
                        mPayMethodAliyPay.setClickable(true);
                        ((StoreActivity) context).Alipay(bean.data);
                        mPopu.dismiss();
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.midToast(context, result.getMessage(), 0);
                    }
                }
            }
        });
    }
}
