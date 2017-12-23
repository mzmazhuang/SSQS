package com.dading.ssqs.controllar.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.ShoppingAddressActivity;
import com.dading.ssqs.activity.ShoppingAddressShowActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.activity.TurnTablePrizeRecordActivity;
import com.dading.ssqs.adapter.MyGvAdpter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ShoppingAddBean;
import com.dading.ssqs.bean.StoreBean2;
import com.dading.ssqs.bean.TurnTablePrizeTextBean;
import com.dading.ssqs.bean.TurnTableResultBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.AutoVerticalScrollTextView;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class StoreTurnTableControllar {
    private static final String TAG = "StoreTurnTableControllar";

    private Context context;
    public View mRootView;
    private int[] ids = {0, 1, 2, 5, 8, 7, 6, 3};
    ChangeView changeView;
    private int id;
    private boolean isStart;
    private int startTime;
    private int stopTime;
    private MyGvAdpter mAdpter;
    private int mJ;
    private PopupWindow mPopupWindow;
    private View mView;
    private StoreBean2 mData;
    private int mI;
    private List<StoreBean2.AwardsEntity> mAwards;
    private int mIsEnd;
    private RelativeLayout mLy;
    private TextView mTv;
    private View mViewPop;
    private LinearLayout mPopLy;
    private ImageView mPopClose;
    private HashMap<Integer, Integer> mMapOrder;
    private long mStartTime;
    private AlertDialog mDialog;
    private View mContentView;
    private View mDialogResultView;
    private TextView mResutDialog;
    private AlertDialog.Builder mBuilderResult;
    private AlertDialog mDialogResult;
    private List<String> mDataText;
    private int number;
    public MyHander mHandler;

    @Bind(R.id.gv)
    GridView mGv;
    @Bind(R.id.store_turntable_prize_info)
    AutoVerticalScrollTextView mPrizeInfoText;
    @Bind(R.id.store_turntable_finish_data)
    TextView mStoreTurntableFinishData;


    class MyHander extends Handler {
        WeakReference<StoreActivity> mActivity;

        MyHander(StoreActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ChangeBG(ids[id]);
                    id++;
                    if (id >= 8) {
                        id = 0;
                    }
                    Logger.d(TAG, "开始抽奖！改变第" + id + "图片");
                    break;
                case 199:
                    ++number;
                    if (number < mDataText.size()) {
                        mPrizeInfoText.setText(mDataText.get(number));
                    } else {
                        number = 0;
                        mPrizeInfoText.setText(mDataText.get(number));
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public StoreTurnTableControllar(Context context) {
        this.context = context;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private View initView(Context context) {
        mView = View.inflate(context, R.layout.store_turn_table, null);
        ButterKnife.bind(this, mView);
        changeView = new ChangeView();
        mViewPop = View.inflate(context, R.layout.pop_view_protect, null);
        mLy = ButterKnife.findById(mViewPop, R.id.store_protocol);
        mTv = ButterKnife.findById(mViewPop, R.id.store_protocol_tv);
        mPopLy = ButterKnife.findById(mViewPop, R.id.store_protocol_ly);
        mPopClose = ButterKnife.findById(mViewPop, R.id.store_protocol_close);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 将layout转成view,因为dialog表示容器所以不能写true要写成空
        mContentView = View.inflate(context, R.layout.dialog_init, null);
        builder.setView(mContentView);
        mDialog = builder.create();

        mBuilderResult = new AlertDialog.Builder(context);
        // 将layout转成view,因为dialog表示容器所以不能写true要写成空
        mDialogResultView = View.inflate(context, R.layout.dialog_turn_table_result, null);
        mResutDialog = ButterKnife.findById(mDialogResultView, R.id.dialog_turn_table_text);
        mBuilderResult.setView(mDialogResultView);
        mDialogResult = mBuilderResult.create();
        return mView;
    }

    private void initData() {
        mHandler = new MyHander((StoreActivity) context);
        //1-2-3-4-5-6-7-0
        mMapOrder = new HashMap<>();
        mMapOrder.put(0, 1);
        mMapOrder.put(1, 2);
        mMapOrder.put(2, 3);
        mMapOrder.put(3, 4);
        mMapOrder.put(4, 5);
        mMapOrder.put(5, 6);
        mMapOrder.put(6, 7);
        mMapOrder.put(7, 0);
        /**
         1.奖品列表
         a)	请求地址：
         /v1.0/award/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         1-道具2-转盘3-奖品4-钻石5-Vip
         */

        SSQSApplication.apiClient(0).getAwardType2(2, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    StoreBean2 bean = (StoreBean2) result.getData();

                    if (bean != null) {
                        mData = bean;
                        processData(mData);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "商店转盘失败信息");
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
         2-转盘3-奖品
         */

        SSQSApplication.apiClient(0).getAwardMessageType(2, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    TurnTablePrizeTextBean bean = (TurnTablePrizeTextBean) result.getData();

                    if (bean != null && bean.data != null) {
                        processDataText(bean.data);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "转盘轮播失败信息");
                }
            }
        });
    }

    private void processDataText(List<String> data) {
        if (data != null && data.size() > 0) {
            mDataText = data;
            mPrizeInfoText.setText(mDataText.get(number));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ++number;
                    if (number < mDataText.size()) {
                        mPrizeInfoText.setText(mDataText.get(number));
                    } else {
                        number = 0;
                        mPrizeInfoText.setText(mDataText.get(number));
                    }
                    mHandler.postDelayed(this, 1500);
                }
            };
            mHandler.postDelayed(runnable, 1500);
        }
    }

    private void processData(StoreBean2 data) {
        Date date = DateUtils.formatDate(data.startTime);
        mStartTime = date.getTime();
        mTv.setText(mData.remark);
        String time = data.endTime;
        String endTime = time.substring(0, 4) + "年" + time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16);
        Logger.d(TAG, "结束时间是------------------------------:" + endTime);
        String s = "本期截止时间:" + endTime;
        mStoreTurntableFinishData.setText(s);

        mAwards = data.awards;
        ArrayList<StoreBean2.AwardsEntity> list = new ArrayList<>();
        list.add(mAwards.get(0));
        list.add(mAwards.get(1));
        list.add(mAwards.get(2));
        list.add(mAwards.get(7));
        list.add(mAwards.get(3));
        list.add(mAwards.get(6));
        list.add(mAwards.get(5));
        list.add(mAwards.get(4));
        mAdpter = new MyGvAdpter(context, list);
        mGv.setAdapter(mAdpter);
        mGv.setHorizontalSpacing(3);
        mGv.setVerticalSpacing(3);
    }

    private void initListener() {
        mDialogResultView.findViewById(R.id.dialog_turn_table_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGv.setAdapter(mAdpter);
                mDialogResult.dismiss();
            }
        });
        mDialogResultView.findViewById(R.id.dialog_turn_table_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TurnTablePrizeRecordActivity.class);
                context.startActivity(intent);
                mGv.setAdapter(mAdpter);
                mDialogResult.dismiss();
            }
        });
        // 找到cancel的控件id实行监听
        mContentView.findViewById(R.id.btn_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGv.setAdapter(mAdpter);
                        mDialog.dismiss();
                        mGv.setClickable(true);
                    }
                });

        // 找到confirm的控件id实行监听
        mContentView.findViewById(R.id.btn_confirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.SendReRecevice(Constent._SHOPING_GLOD);
                        mDialog.dismiss();
                        mGv.setClickable(true);
                    }
                });

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
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Logger.d(TAG, "开始时间------------------------------:" + mStartTime + "------------现在时间" + new Date().getTime());
                if (mData.isStart == 0) {
                    TmtUtils.midToast(context, "转盘抽奖活动尚未开始,敬请期待!", 0);
                    mGv.setClickable(true);
                    return;
                } else if (mData.isEnd == 1) {
                    TmtUtils.midToast(context, "转盘抽奖活动已经结束,敬请期待下次开启!", 0);
                    mGv.setClickable(true);
                    return;
                }
                if (mStartTime > new Date().getTime()) {
                    TmtUtils.midToast(context, "更新头像成功!", 0);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("活动提示");
                    builder.setMessage("活动尚未开始,敬请期待!");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mGv.setClickable(true);
                        }
                    });
                    builder.show();
                    return;
                }
                //1-2-3-4-5-6-7-0

                if (arg2 == 4) {
                    boolean clickable = mGv.isClickable();
                    mGv.setClickable(false);
                    if (!clickable) {
                        return;
                    }
                    if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((StoreActivity) context).finish();
                        return;
                    }
                    if (Integer.parseInt(UIUtils.getSputils().getString(Constent.GLODS, "0")) < 500) {
                        mDialog.show();
                        return;
                    }
                    /**
                     * 2.	转盘
                     a)	请求地址：
                     /v1.0/award/turn
                     b)	请求方式:
                     get
                     c)	请求参数说明：
                     auth_token：登陆后加入请求头
                     d)	返回格式
                     {"status":true,"code":0,"msg":"","data":7}
                     */

                    SSQSApplication.apiClient(0).awardTurn(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            mGv.setClickable(true);

                            if (result.isOk()) {
                                TurnTableResultBean bean = (TurnTableResultBean) result.getData();

                                if (bean != null) {
                                    mI = bean.num;
                                    mIsEnd = bean.isEnd;
                                    if (mIsEnd == 0) {
                                        id = 0;
                                        if (mI > 7) {
                                            TmtUtils.midToast(context, "转盘参数错误!请联系客服.", 0);
                                            return;
                                        }
                                        int i = mMapOrder.get(mI);
                                        if (i == 0) {
                                            i = 7;
                                            stopTime = i * 200 + 3200;
                                        } else {
                                            i = i - 1;
                                            stopTime = i * 200 + 3200;
                                        }
                                        Logger.d(TAG, "开始抽奖！stopTime" + i);
                                        new Thread(changeView).start();
                                        isStart = true;
                                    } else {
                                        TmtUtils.midToast(context, "活动已结束,欢迎下次参与!", 0);
                                        mGv.setClickable(true);
                                    }
                                } else {
                                    Logger.d(TAG, result.getMessage() + "抽奖结果失败信息");
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    class ChangeView implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            if (startTime >= stopTime) {
                mHandler.removeCallbacks(changeView);
                startTime = 0;
                stopTime = 0;
                //startAnim();
                ((StoreActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGv.setAdapter(mAdpter);
                    }
                });
                String s = "恭喜您,获得了" + mAwards.get(mI).name + "!";
                mResutDialog.setText(s);
                Logger.d(TAG, "恭喜中奖,您获得了" + mAwards.get(mI).name + "!");
                mDialogResult.show();
                String diamonsStr = UIUtils.getSputils().getString(Constent.GLODS, "");
                int glod = Integer.parseInt(diamonsStr.replaceAll(",", ""));
                ;
                UIUtils.getSputils().putString(Constent.GLODS, (glod - 500 + ""));
                UIUtils.SendReRecevice(Constent.SERIES);
                isStart = false;
                mGv.setClickable(true);
                return;
            }
            if (isStart) {
                mHandler.postDelayed(changeView, 100);
                startTime += 200;
            }

        }
    }

    private void ChangeBG(int id) {
        for (int i = 0; i < mGv.getChildCount(); i++) {
            if (i == id) {
                ((LinearLayout) mGv.getChildAt(id)).setBackgroundResource(R.mipmap.sm_lottery_frame_sel);
            } else if (i == 4) {
                continue;
            } else {
                ((LinearLayout) mGv.getChildAt(i)).setBackgroundResource(R.mipmap.sm_lottery_frame);
            }
        }
    }

    private void startAnim() {
        int[] oldP = new int[2];
        int[] newP = new int[2];
        mGv.getChildAt(4).getLocationOnScreen(newP);
        if (id == 0) {
            mJ = 7;
        } else {
            mJ = id - 1;
        }
        mGv.getChildAt(ids[mJ]).getLocationOnScreen(oldP);
        TranslateAnimation am = new TranslateAnimation(10, newP[0] - oldP[0],
                40, newP[1] - oldP[1]);
        // 动画开始到结束的执行时间(1000 = 1 秒)
        am.setDuration(2000);
        //因为执行完毕后有一次++所以必须减一
        mGv.getChildAt(ids[mJ]).startAnimation(am);
        for (int i = 0; i < mGv.getChildCount(); i++) {
            if (i == ids[id]) {
                continue;
            } else {
                mGv.getChildAt(i).setVisibility(View.GONE);
            }
        }
        TmtUtils.midToast(UIUtils.getContext(), "恭喜您中奖了", 0);
    }

    private PopupWindow popuMake(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow popupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                MATCH_PARENT, true);
        //设置PopupWindow的背景图片是透明的颜色图片
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        popupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }

    @OnClick({R.id.store_turntable_protocol, R.id.store_turntable_prize_record, R.id.store_turntable_shopping_addr,
            /*R.id.store_turn_table_lottery*/})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.store_turntable_protocol:
                //弹出pop
                mPopupWindow = popuMake(mViewPop);
                mPopupWindow.showAtLocation(mView, Gravity.CENTER, 120, 120);
                break;
            case R.id.store_turntable_prize_record:
                //如果没有登录
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(context, TurnTablePrizeRecordActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ((StoreActivity) context).finish();
                }
                break;
            case R.id.store_turntable_shopping_addr:
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ((StoreActivity) context).finish();
                    return;
                }
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
