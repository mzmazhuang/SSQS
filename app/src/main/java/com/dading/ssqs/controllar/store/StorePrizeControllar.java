package com.dading.ssqs.controllar.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.PrizeInfoRecordActivity;
import com.dading.ssqs.activity.ShoppingAddressActivity;
import com.dading.ssqs.activity.ShoppingAddressShowActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.AwardExchangeElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ShoppingAddBean;
import com.dading.ssqs.bean.StoreBean2;
import com.dading.ssqs.bean.TurnTablePrizeTextBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.AutoVerticalScrollTextView;
import com.google.gson.Gson;


import java.lang.ref.WeakReference;
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
public class StorePrizeControllar implements View.OnClickListener {
    private static final String TAG = "StorePrizeControllar";
    @Bind(R.id.store_prize_iv1)
    ImageView mStorePrizeIv1;
    @Bind(R.id.store_prize_iv2)
    ImageView mStorePrizeIv2;
    @Bind(R.id.store_prize_iv3)
    ImageView mStorePrizeIv3;
    @Bind(R.id.store_prize_iv4)
    ImageView mStorePrizeIv4;
    @Bind(R.id.store_prize_iv5)
    ImageView mStorePrizeIv5;
    @Bind(R.id.store_prize_iv6)
    ImageView mStorePrizeIv6;
    @Bind(R.id.store_prize_iv7)
    ImageView mStorePrizeIv7;
    @Bind(R.id.store_prize_iv8)
    ImageView mStorePrizeIv8;
    @Bind(R.id.store_prize_iv9)
    ImageView mStorePrizeIv9;
    private Context context;
    public View mRootView;
    @Bind(R.id.store_prize_record_finish_data)
    TextView mStorePrizeRecordFinishData;
    @Bind(R.id.store_prize_1)
    TextView mStorePrize1;
    @Bind(R.id.store_prize_11)
    TextView mStorePrize11;
    @Bind(R.id.store_prize_2)
    TextView mStorePrize2;
    @Bind(R.id.store_prize_22)
    TextView mStorePrize22;
    @Bind(R.id.store_prize_3)
    TextView mStorePrize3;
    @Bind(R.id.store_prize_33)
    TextView mStorePrize33;
    @Bind(R.id.store_prize_price1)
    TextView mStorePrizePrice1;
    @Bind(R.id.store_prize_price2)
    TextView mStorePrizePrice2;
    @Bind(R.id.store_prize_price3)
    TextView mStorePrizePrice3;
    @Bind(R.id.store_prize_4)
    TextView mStorePrize4;
    @Bind(R.id.store_prize_44)
    TextView mStorePrize44;
    @Bind(R.id.store_prize_5)
    TextView mStorePrize5;
    @Bind(R.id.store_prize_55)
    TextView mStorePrize55;
    @Bind(R.id.store_prize_6)
    TextView mStorePrize6;
    @Bind(R.id.store_prize_66)
    TextView mStorePrize66;
    @Bind(R.id.store_prize_price4)
    TextView mStorePrizePrice4;
    @Bind(R.id.store_prize_price5)
    TextView mStorePrizePrice5;
    @Bind(R.id.store_prize_price6)
    TextView mStorePrizePrice6;
    @Bind(R.id.store_prize_7)
    TextView mStorePrize7;
    @Bind(R.id.store_prize_77)
    TextView mStorePrize77;
    @Bind(R.id.store_prize_8)
    TextView mStorePrize8;
    @Bind(R.id.store_prize_88)
    TextView mStorePrize88;
    @Bind(R.id.store_prize_9)
    TextView mStorePrize9;
    @Bind(R.id.store_prize_99)
    TextView mStorePrize99;
    @Bind(R.id.store_prize_price7)
    TextView mStorePrizePrice7;
    @Bind(R.id.store_prize_price8)
    TextView mStorePrizePrice8;
    @Bind(R.id.store_prize_price9)
    TextView mStorePrizePrice9;
/*    @Bind(R.id.store_prize_ten)
    TextView mStorePrizeTen;
    @Bind(R.id.store_prize_ten_2)
    TextView mStorePrizeTen2;
    @Bind(R.id.store_prize_eleven)
    TextView mStorePrizeEleven;
    @Bind(R.id.store_prize_eleven_2)
    TextView mStorePrizeEleven2;
    @Bind(R.id.store_prize_twelve)
    TextView mStorePrizeTwelve;
    @Bind(R.id.store_prize_twelve_2)
    TextView mStorePrizeTwelve2;
    @Bind(R.id.store_prize_price_ten)
    TextView mStorePrizePriceTen;
    @Bind(R.id.store_prize_price_eleven)
    TextView mStorePrizePriceEleven;
    @Bind(R.id.store_prize_price_twelve)
    TextView mStorePrizePriceTwelve;*/

    @Bind(R.id.store_prize_record_protocol)
    ImageView mStorePrizeProtocol;
    @Bind(R.id.store_prize_record_prize_record)
    ImageView mStorePrizeRecord;
    @Bind(R.id.store_prize_record_shopping_addr)
    ImageView mStorePrizeAddress;
    @Bind(R.id.store_prize_record_prize_info)
    AutoVerticalScrollTextView mStorePrizeText;

    private View mView;
    private ArrayList<TextView> mListName;
    private ArrayList<TextView> mListCount;
    private ArrayList<TextView> mListPrice;
    private String mPriceStr;
    private List<StoreBean2.AwardsEntity> mAwards;
    private String mName;
    private int mId;
    private StoreBean2 mData;
    private View mViewPop;
    private RelativeLayout mLy;
    private TextView mTv;
    private LinearLayout mPopLy;
    private ImageView mPopClose;
    private PopupWindow mPopupWindow;
    private int number;
    private List<String> mDataText;
    private boolean mIsRunning = true;
    private AlertDialog.Builder mBuilderResult;
    private View mDialogResultView;
    private TextView mResutDialog;
    private AlertDialog mDialogResult;
    public MyHandle mHandler;
    private ArrayList<ImageView> mListIv;

    public StorePrizeControllar(Context context) {
        this.context = context;
        mRootView = initView(context);
        initData();
        initListener();
    }

    class MyHandle extends Handler {
        WeakReference<StoreActivity> mActivity;

        MyHandle(StoreActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        public void handleMessage(Message msg) {
            ++number;
            if (number < mDataText.size()) {
                mStorePrizeText.setText(mDataText.get(number));
                Logger.d(TAG, "滚动返回数据是------------------------------:" + mDataText.get(number));
            } else {
                number = 0;
                mStorePrizeText.setText(mDataText.get(number));
                Logger.d(TAG, "滚动返回数据是------------------------------:" + mDataText.get(number));
            }
        }
    }


    private View initView(Context context) {
        mView = View.inflate(context, R.layout.store_prize, null);
        ButterKnife.bind(this, mView);

        mViewPop = View.inflate(context, R.layout.pop_view_protect, null);
        mLy = ButterKnife.findById(mViewPop, R.id.store_protocol);
        mTv = ButterKnife.findById(mViewPop, R.id.store_protocol_tv);
        mPopLy = ButterKnife.findById(mViewPop, R.id.store_protocol_ly);
        mPopClose = ButterKnife.findById(mViewPop, R.id.store_protocol_close);


        mBuilderResult = new AlertDialog.Builder(context);
        mDialogResultView = View.inflate(context, R.layout.dialog_turn_table_result, null);
        mResutDialog = ButterKnife.findById(mDialogResultView, R.id.dialog_turn_table_text);
        mBuilderResult.setView(mDialogResultView);
        mDialogResult = mBuilderResult.create();


        mListName = new ArrayList<>();
        mListName.add(mStorePrize1);
        mListName.add(mStorePrize2);
        mListName.add(mStorePrize3);
        mListName.add(mStorePrize4);
        mListName.add(mStorePrize5);
        mListName.add(mStorePrize6);
        mListName.add(mStorePrize7);
        mListName.add(mStorePrize8);
        mListName.add(mStorePrize9);
 /*       mListName.add(mStorePrizeTen);
        mListName.add(mStorePrizeEleven);
        mListName.add(mStorePrizeTwelve);*/

        mListIv = new ArrayList<>();
        mListIv.add(mStorePrizeIv1);
        mListIv.add(mStorePrizeIv2);
        mListIv.add(mStorePrizeIv3);
        mListIv.add(mStorePrizeIv4);
        mListIv.add(mStorePrizeIv5);
        mListIv.add(mStorePrizeIv6);
        mListIv.add(mStorePrizeIv7);
        mListIv.add(mStorePrizeIv8);
        mListIv.add(mStorePrizeIv9);

        mListCount = new ArrayList<>();
        mListCount.add(mStorePrize11);
        mListCount.add(mStorePrize22);
        mListCount.add(mStorePrize33);
        mListCount.add(mStorePrize44);
        mListCount.add(mStorePrize55);
        mListCount.add(mStorePrize66);
        mListCount.add(mStorePrize77);
        mListCount.add(mStorePrize88);
        mListCount.add(mStorePrize99);
      /*  mListCount.add(mStorePrizeTen2);
        mListCount.add(mStorePrizeEleven2);
        mListCount.add(mStorePrizeTwelve2);*/

        mListPrice = new ArrayList<>();
        mListPrice.add(mStorePrizePrice1);
        mListPrice.add(mStorePrizePrice2);
        mListPrice.add(mStorePrizePrice3);
        mListPrice.add(mStorePrizePrice4);
        mListPrice.add(mStorePrizePrice5);
        mListPrice.add(mStorePrizePrice6);
        mListPrice.add(mStorePrizePrice7);
        mListPrice.add(mStorePrizePrice8);
        mListPrice.add(mStorePrizePrice9);
     /*   mListPrice.add(mStorePrizePriceTen);
        mListPrice.add(mStorePrizePriceEleven);
        mListPrice.add(mStorePrizePriceTwelve);
*/
        return mView;
    }

    private void initData() {
        /**
         * 1.	奖品列表
         a)	请求地址：
         /v1.0/award/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         1-道具2-獎品3-奖品4-钻石5-Vip
         */

        SSQSApplication.apiClient(0).getAwardType2(3, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    StoreBean2 bean = (StoreBean2) result.getData();

                    if (bean != null) {
                        mData = bean;
                        mAwards = mData.awards;
                        processData(bean);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "商店奖品中獎失败信息");
                }
            }
        });

        /**
         * 7.	轮播信息
         a)	请求地址：
         /v1.0/award/msg/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         2-獎品3-奖品
         */

        SSQSApplication.apiClient(0).getAwardMessageType(3, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    TurnTablePrizeTextBean bean = (TurnTablePrizeTextBean) result.getData();

                    if (bean != null && bean.data != null) {
                        processDataText(bean.data);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "獎品轮播失败信息");
                }
            }
        });
    }

    private void processDataText(List<String> data) {
        mHandler = new MyHandle((StoreActivity) context);
        if (data != null && data.size() > 0) {
            mDataText = data;
            mStorePrizeText.setText(data.get(0));
            Logger.d(TAG, "滚动返回数据是------------------------------:" + data.get(0));
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ++number;
                    if (number < mDataText.size()) {
                        mStorePrizeText.setText(mDataText.get(number));
                        Logger.d(TAG, "滚动返回数据是------------------------------:" + mDataText.get(number));
                    } else {
                        number = 0;
                        mStorePrizeText.setText(mDataText.get(number));
                        Logger.d(TAG, "滚动返回数据是------------------------------:" + mDataText.get(number));
                    }
                    mHandler.postDelayed(this, 1500);
                }
            }, 1500);
        }
    }

    private void processData(StoreBean2 data) {
        mTv.setText(mData.remark);
        for (int i = 0; i < data.awards.size(); i++) {
            StoreBean2.AwardsEntity entity = data.awards.get(i);
            mListName.get(i).setText(entity.name);
            String count = "还剩" + entity.count + "个";
            mListCount.get(i).setText(count);
            String price = entity.cost + "金币";
            mListPrice.get(i).setText(price);

            ImageView iv = mListIv.get(i);
            SSQSApplication.glide.load(data.awards.get(i).itemImageUrl).error(R.mipmap.fail).centerCrop().into(iv);
        }
        String time = data.endTime;
        String endTime = time.substring(0, 4) + "年" + time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16);
        Logger.d(TAG, "结束时间是------------------------------:" + endTime);
        String s = "本期截止时间:" + endTime;
        mStorePrizeRecordFinishData.setText(s);
    }

    private void initListener() {
        mDialogResultView.findViewById(R.id.dialog_turn_table_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogResult.dismiss();
            }
        });
        mDialogResultView.findViewById(R.id.dialog_turn_table_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrizeInfoRecordActivity.class);
                context.startActivity(intent);
                mDialogResult.dismiss();
            }
        });
        mStorePrizeProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow = PopUtil.popuMake(mViewPop);
                mPopupWindow.showAtLocation(mView, Gravity.CENTER, 120, 120);
            }
        });
        mStorePrizeRecord.setOnClickListener(this);
        mStorePrizeAddress.setOnClickListener(this);
        mLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.store_prize_price1, R.id.store_prize_price2, R.id.store_prize_price3,
            R.id.store_prize_price4, R.id.store_prize_price5, R.id.store_prize_price6,
            R.id.store_prize_price7, R.id.store_prize_price8, R.id.store_prize_price9,
            /*R.id.store_prize_price_ten,R.id.store_prize_price_eleven,R.id.store_prize_price_twelve*/})
    public void OnClik(View v) {
        if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((StoreActivity) context).finish();
            return;
        }
        if (mData.isStart == 0) {
            ToastUtils.midToast(context, "礼品兑换活动尚未开始,敬请期待!", 0);
            return;
        } else if (mData.isEnd == 1) {
            ToastUtils.midToast(context, "礼品兑换活动已经结束,敬请期待下次开启!", 0);
            return;
        }
        switch (v.getId()) {
            case R.id.store_prize_price1:
                mPriceStr = mAwards.get(0).cost;
                mName = mAwards.get(0).name;
                mId = mAwards.get(0).id;
                break;
            case R.id.store_prize_price2:
                mPriceStr = mAwards.get(1).cost;
                mName = mAwards.get(1).name;
                mId = mAwards.get(1).id;
                break;
            case R.id.store_prize_price3:
                mPriceStr = mAwards.get(2).cost;
                mName = mAwards.get(2).name;
                mId = mAwards.get(2).id;
                break;
            case R.id.store_prize_price4:
                mPriceStr = mAwards.get(3).cost;
                mName = mAwards.get(3).name;
                mId = mAwards.get(3).id;
                break;
            case R.id.store_prize_price5:
                mPriceStr = mAwards.get(4).cost;
                mName = mAwards.get(4).name;
                mId = mAwards.get(4).id;
                break;
            case R.id.store_prize_price6:
                mPriceStr = mAwards.get(5).cost;
                mName = mAwards.get(5).name;
                mId = mAwards.get(5).id;
                break;
            case R.id.store_prize_price7:
                mPriceStr = mAwards.get(6).cost;
                mName = mAwards.get(6).name;
                mId = mAwards.get(6).id;
                break;
            case R.id.store_prize_price8:
                mPriceStr = mAwards.get(7).cost;
                mName = mAwards.get(7).name;
                mId = mAwards.get(7).id;
                break;
            case R.id.store_prize_price9:
                mPriceStr = mAwards.get(8).cost;
                mName = mAwards.get(8).name;
                mId = mAwards.get(8).id;
                break;
           /* case R.id.store_prize_price_ten:
                mPriceStr = mAwards.get(9).cost;
                mName = mAwards.get(9).name;
                mId = mAwards.get(9).id;
                break;
            case R.id.store_prize_price_eleven:
                mPriceStr = mAwards.get(10).cost;
                mName = mAwards.get(10).name;
                mId = mAwards.get(10).id;
                break;
            case R.id.store_prize_price_twelve:
                mPriceStr = mAwards.get(11).cost;
                mName = mAwards.get(11).name;
                mId = mAwards.get(11).id;
                break;*/
            default:
                break;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("兑换奖品");
        builder.setMessage("使用金币" + mPriceStr + "兑换:" + mName);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     * 3.	奖品兑换
                     a)	请求地址：
                     /v1.0/awardExchange
                     b)	请求方式:
                     Post
                     c)	请求参数说明：
                     id: 兑奖奖品ID
                     */
                    AwardExchangeElement element = new AwardExchangeElement();
                    element.setId(String.valueOf(mId));

                    SSQSApplication.apiClient(0).awardExchange(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                String diamonsStr = UIUtils.getSputils().getString(Constent.GLODS, "");
                                int glod = Integer.parseInt(diamonsStr.replaceAll(",", ""));
                                UIUtils.getSputils().putString(Constent.GLODS, (glod - Integer.parseInt(mPriceStr.replace(",", ""))) + "");

                                UIUtils.SendReRecevice(Constent.SERIES);
                                String s = "恭喜您获得了" + mName + "礼品!我们将在三天内为您送出..";
                                mResutDialog.setText(s);
                                mDialogResult.show();
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

    @Override
    public void onClick(View v) {
        if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((StoreActivity) context).finish();
            return;
        }
        switch (v.getId()) {
            case R.id.store_prize_record_prize_record:
                Intent intentRecord = new Intent(context, PrizeInfoRecordActivity.class);
                context.startActivity(intentRecord);
                break;
            case R.id.store_prize_record_shopping_addr:
                /**
                 * 13.	获取用户收货地址
                 a)	请求地址：
                 /v1.0/user/get/address
                 b)	请求方式:
                 get
                 c)	请求参数说明：
                 auth_token：登陆后加入请求头
                 */

                SSQSApplication.apiClient(0).getUserAddress(new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            ShoppingAddBean bean = (ShoppingAddBean) result.getData();

                            if (bean != null) {
                                Gson gson = new Gson();

                                Intent intent = new Intent(context, ShoppingAddressShowActivity.class);
                                intent.putExtra(Constent.ADD_MSG, gson.toJson(bean, ShoppingAddBean.class));
                                context.startActivity(intent);
                            }
                        } else {
                            Intent intentAdd = new Intent(context, ShoppingAddressActivity.class);
                            context.startActivity(intentAdd);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
