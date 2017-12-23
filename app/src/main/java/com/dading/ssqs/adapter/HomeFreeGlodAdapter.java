package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SevenPopBean;
import com.dading.ssqs.bean.SignResultBean;
import com.dading.ssqs.bean.TaskBean;
import com.dading.ssqs.interfaces.MyItemClickListenerPostion;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/21 16:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeFreeGlodAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HomeFreeGlodAdapter";
    private final Context context;
    private final List<TaskBean.TasksEntity> data;
    private final View view;
    private final SevenPopBean popBean;
    private int mStatus;
    private PopupWindow mPop;
    private ImageView mSevenDayRedBet1;
    private ImageView mSevenDayRedBet11;
    private ImageView mSevenDayRedBet2;
    private ImageView mSevenDayRedBet21;
    private ImageView mSevenDayRedBet3;
    private ImageView mSevenDayRedBet31;
    private ImageView mSevenDayRedBet4;
    private ImageView mSevenDayRedBet41;
    private ImageView mSevenDayRedBet5;
    private ImageView mSevenDayRedBet51;
    private ImageView mSevenDayRedBet6;
    private ImageView mSevenDayRedBet61;
    private ImageView mSevenDayRedBet7;
    private ImageView mSevenDayGdIv7;
    private ImageView mSevenDayGdIv6;
    private ImageView mSevenDayGdIv5;
    private ImageView mSevenDayGdIv4;
    private ImageView mSevenDayGdIv3;
    private ImageView mSevenDayGdIv2;
    private ImageView mSevenDayGdIv1;
    private TextView mSevenDayAdd7;
    private TextView mSevenDayAdd6;
    private TextView mSevenDayAdd5;
    private TextView mSevenDayAdd4;
    private TextView mSevenDayAdd3;
    private TextView mSevenDayAdd2;
    private TextView mSevenDayAdd1;
    private ImageView mSignButton;
    private ImageView mSigncLose;
    private RelativeLayout mSignLyOut;
    private String mS;
    private RelativeLayout mSignSucLy;
    private TextView mSignSucNum;
    private ImageView mSignSucClose;
    private RelativeLayout mSevenDaySignLy;

    public HomeFreeGlodAdapter(Context context, List<TaskBean.TasksEntity> tasks, View view, SevenPopBean popBean) {
        this.context = context;
        this.data = tasks;
        this.popBean = popBean;
        this.view = view;
        popData();
    }

    /*public class JsInteration {
        @JavascriptInterface
        public void signPop(String message) {
            mPop.dismiss();
            LogUtil.util(TAG, "得到签到返回数据是------------------------------" + message);
        }
    }*/

    private void popData() {
        //// TODO: 2016/11/2 网页版
        /*mView2 = View.inflate(context, R.layout.qiandao, null);
        mSevenDayWeb = ButterKnife.findById(mView2, R.id.seven_day2_web);
        mSevenDayClose = ButterKnife.findById(mView2, R.id.seven_day2_pop_close);
        mSevenDayPopLy = ButterKnife.findById(mView2, R.id.senven_day2_pop_ly);

        //String url = "http://www.it1992.com/#/sign/" +    UIUtils.getSputils().getString(Constent.TOKEN, null);

        WebSettings webSettings = mSevenDayWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUserAgentString("mac os");
        webSettings.setDefaultTextEncodingName("utf-8");
        *//**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         *//*

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);//适应手机缩放
        webSettings.setLoadWithOverviewMode(true);

        mSevenDayWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mSevenDayWeb.setHorizontalScrollbarOverlay(true);
        mSevenDayWeb.setHorizontalScrollBarEnabled(true);
        mSevenDayWeb.requestFocus();
        //String url = "http://112.74.130.167/sign/";
        String url = "http://www.ddzlink.com/sign/";
        mSevenDayWeb.loadUrl(url);
        LogUtil.util(TAG, "签到url------------------------------:" + url);
        //网页版
        mPop = PopUtil.popuMake(mView2);*/
        //// TODO: 2016/11/2 原声版
        View view = View.inflate(context, R.layout.activity_seven_day, null);
        mSevenDaySignLy = ButterKnife.findById(view, R.id.seven_day_sign_ly);
        mSevenDayRedBet1 = ButterKnife.findById(view, R.id.seven_day_red_bet1);
        mSevenDayRedBet11 = ButterKnife.findById(view, R.id.seven_day_red_bet11);
        mSevenDayRedBet2 = ButterKnife.findById(view, R.id.seven_day_red_bet2);
        mSevenDayRedBet21 = ButterKnife.findById(view, R.id.seven_day_red_bet21);
        mSevenDayRedBet3 = ButterKnife.findById(view, R.id.seven_day_red_bet3);
        mSevenDayRedBet31 = ButterKnife.findById(view, R.id.seven_day_red_bet31);
        mSevenDayRedBet4 = ButterKnife.findById(view, R.id.seven_day_red_bet4);
        mSevenDayRedBet41 = ButterKnife.findById(view, R.id.seven_day_red_bet41);
        mSevenDayRedBet5 = ButterKnife.findById(view, R.id.seven_day_red_bet5);
        mSevenDayRedBet51 = ButterKnife.findById(view, R.id.seven_day_red_bet51);
        mSevenDayRedBet6 = ButterKnife.findById(view, R.id.seven_day_red_bet6);
        mSevenDayRedBet61 = ButterKnife.findById(view, R.id.seven_day_red_bet61);
        mSevenDayRedBet7 = ButterKnife.findById(view, R.id.seven_day_red_bet7);

        mSevenDayGdIv1 = ButterKnife.findById(view, R.id.seven_day_gd_iv1);
        mSevenDayGdIv2 = ButterKnife.findById(view, R.id.seven_day_gd_iv2);
        mSevenDayGdIv3 = ButterKnife.findById(view, R.id.seven_day_gd_iv3);
        mSevenDayGdIv4 = ButterKnife.findById(view, R.id.seven_day_gd_iv4);
        mSevenDayGdIv5 = ButterKnife.findById(view, R.id.seven_day_gd_iv5);
        mSevenDayGdIv6 = ButterKnife.findById(view, R.id.seven_day_gd_iv6);
        mSevenDayGdIv7 = ButterKnife.findById(view, R.id.seven_day_gd_iv7);


        mSevenDayAdd1 = ButterKnife.findById(view, R.id.seven_day_add1);
        mSevenDayAdd2 = ButterKnife.findById(view, R.id.seven_day_add2);
        mSevenDayAdd3 = ButterKnife.findById(view, R.id.seven_day_add3);
        mSevenDayAdd4 = ButterKnife.findById(view, R.id.seven_day_add4);
        mSevenDayAdd5 = ButterKnife.findById(view, R.id.seven_day_add5);
        mSevenDayAdd6 = ButterKnife.findById(view, R.id.seven_day_add6);
        mSevenDayAdd7 = ButterKnife.findById(view, R.id.seven_day_add7);

        mSignButton = ButterKnife.findById(view, R.id.seven_sign_button);
        mSigncLose = ButterKnife.findById(view, R.id.seven_day_close);
        mSignLyOut = ButterKnife.findById(view, R.id.seven_day_ly_out);

        mSignSucLy = ButterKnife.findById(view, R.id.seven_sign_suc_ly);
        mSignSucNum = ButterKnife.findById(view, R.id.get_glod_num);
        mSignSucClose = ButterKnife.findById(view, R.id.seven_day_suc_close);

        mSevenDaySignLy.setVisibility(View.VISIBLE);
        mSignSucLy.setVisibility(View.GONE);

        if (mStatus == 0) {
            mSignButton.setClickable(true);
        } else if (mStatus == 1) {
            mSignButton.setClickable(false);
            mSignButton.setImageResource(R.mipmap.s_parcel);
        }
        /**
         * banlanceCount	int	连续签到球币总和
         dayCount	int	连续签到天数
         banlance	int	奖励球币数
         status	int	状态0：未完成1：完成
         date	int	第几天
         */

        SevenPopBean data = popBean;
        final int dayCount = data.dayCount;
        List<SevenPopBean.TasksEntity> tasks = data.tasks;
        for (int i = 1; i <= tasks.size(); i++) {
            int status = tasks.get(i - 1).status;
            String text = "+" + tasks.get(i - 1).banlance;
            if (status == 1) {
                switch (i) {
                    case 1:
                        mSevenDayRedBet1.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv1.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd1.setText(text);
                        mSevenDayAdd1.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 2:
                        mSevenDayRedBet2.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd2.setText(text);
                        mSevenDayAdd2.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 3:
                        mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet3.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd3.setText(text);
                        mSevenDayAdd3.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 4:
                        mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet4.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd4.setText(text);
                        mSevenDayAdd4.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 5:
                        mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet5.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd5.setText(text);
                        mSevenDayAdd5.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 6:
                        mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet6.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd6.setText(text);
                        mSevenDayAdd6.setTextColor(context.getResources().getColor(R.color.orange));
                        break;
                    case 7:
                        //7不会出现只会在点击后出现
                        mSevenDayRedBet7.setImageResource(R.mipmap.s_have_checked_in_sel);
                        break;
                    default:
                        break;
                }
            } else {
                switch (i) {
                    case 1:
                        mSevenDayAdd1.setText(text);
                        break;
                    case 2:
                        mSevenDayAdd2.setText(text);
                        break;
                    case 3:
                        mSevenDayAdd3.setText(text);
                        break;
                    case 4:
                        mSevenDayAdd4.setText(text);
                        break;
                    case 5:
                        mSevenDayAdd5.setText(text);
                        break;
                    case 6:
                        mSevenDayAdd6.setText(text);
                        break;
                    case 7:
                        mSevenDayAdd7.setText(text);
                        break;
                    default:
                        break;
                }
            }
        }
        if (mStatus == 0) {
            LogUtil.util(TAG, "改变今天原点------------------------------:");
            switch (dayCount) {
                case 0:
                    mSevenDayRedBet1.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv1.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd1.setTextColor(context.getResources().getColor(R.color.orange));
                    mS = data.tasks.get(0).banlance + "";
                    break;
                case 1:
                    mSevenDayRedBet2.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd2.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(1).banlance + "";
                    break;
                case 2:
                    mSevenDayRedBet3.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd3.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(2).banlance + "";
                    break;
                case 3:
                    mSevenDayRedBet4.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd4.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(3).banlance + "";
                    break;
                case 4:
                    mSevenDayRedBet5.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd5.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(4).banlance + "";
                    break;
                case 5:
                    mSevenDayRedBet6.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd6.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(5).banlance + "";
                    break;
                case 6:
                    mSevenDayRedBet7.setImageResource(R.mipmap.s_unclaimed);
                    mSevenDayGdIv7.setImageResource(R.mipmap.s_gold_sel);
                    mSevenDayAdd7.setTextColor(context.getResources().getColor(R.color.orange));
                    mSevenDayRedBet61.setImageResource(R.mipmap.s_vertical_bar_sel);
                    mS = data.tasks.get(6).banlance + "";
                    break;
                default:
                    break;
            }
        }

        UIUtils.getSputils().putString(Constent.SIGN_GLOD, mS);

        //// TODO: 2016/11/2 原声版
        mPop = PopUtil.popuMake(view);

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSQSApplication.apiClient(0).userSign(new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            SignResultBean bean = (SignResultBean) result.getData();

                            if (bean != null) {
                                switch (bean.dayCount) {
                                    case 1:
                                        mSevenDayRedBet1.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 2:
                                        mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet2.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 3:
                                        mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet3.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 4:
                                        mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet4.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 5:
                                        mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet5.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 6:
                                        mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet6.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 7:
                                        mSevenDayRedBet61.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet7.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    default:
                                        break;
                                }
                                int banlance = popBean.tasks.get(bean.dayCount).banlance;

                                UIUtils.getSputils().putBoolean(Constent.SIGN_SUC, true);

                                UIUtils.getSputils().putInt("banlance", banlance);

                                UIUtils.SendReRecevice(Constent.TASK_TAG);

                                UIUtils.SendReRecevice(Constent.REFRESH_MONY);

                                String s = "+" + banlance + "金币";
                                mSignSucNum.setText(s);
                                mSignSucLy.setVisibility(View.VISIBLE);
                                mSevenDaySignLy.setVisibility(View.GONE);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        ((HomeFreeGlodActivity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPop.dismiss();
                                            }
                                        });
                                    }
                                }, 2000);
                                mSignButton.setImageResource(R.mipmap.s_parcel);
                            }
                        } else {
                            if (!AndroidUtilities.checkIsLogin(result.getErrno(), context)) {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
            }
        });
        mSigncLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mSignLyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });

        mSignSucClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_free_glod_lv_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TaskBean.TasksEntity entity = data.get(position);

        if (entity.status == 0) {
            holder.mNewsTasteTitleType.setImageResource(R.mipmap.a_everyday);
        } else {
            holder.mNewsTasteTitleType.setImageResource(R.mipmap.a_single);
        }

        if (entity.status == 0) {
            /**
             * 领取和签到不同
             */
            if (position == 1) {
                holder.mNewsTasteState.setImageResource(R.mipmap.a_to_receive);
            } else {
                holder.mNewsTasteState.setImageResource(R.mipmap.a_not_checked);
            }

            holder.mNewsTasteDimaonsIv.setImageResource(R.mipmap.a_diamond_little_sel);
            holder.mNewsTasteGlodIv.setImageResource(R.mipmap.a_bet_coin_sel);
            holder.mNewsTasteDiamonsTv.setTextColor(context.getResources().getColor(R.color.orange));
            holder.mNewsTasteGlodTv.setTextColor(context.getResources().getColor(R.color.orange));
            holder.mNewsTasteState.setImageResource(R.mipmap.a_unfinished);
        } else {
            holder.mNewsTasteState.setImageResource(R.mipmap.a_have_checked);
            holder.mNewsTasteDimaonsIv.setImageResource(R.mipmap.a_diamond_little);
            holder.mNewsTasteGlodIv.setImageResource(R.mipmap.a_bet_coin);
            holder.mNewsTasteDiamonsTv.setTextColor(context.getResources().getColor(R.color.gray8));
            holder.mNewsTasteGlodTv.setTextColor(context.getResources().getColor(R.color.gray8));
            holder.mNewsTasteState.setImageResource(R.mipmap.a_completed);
        }


        if (entity.timesType == 0) {
            holder.mNewsTasteIcon.setImageResource(R.mipmap.everyday);
        } else if (entity.timesType == 1) {
            holder.mNewsTasteIcon.setImageResource(R.mipmap.single);
        }

        holder.mNewsTasteTitle.setText(entity.name);

        String glod;

        if (entity.id == 2) {
            if (popBean.dayCount == 0) {
                glod = popBean.tasks.get(popBean.dayCount).banlance + "";
            } else {
                glod = popBean.tasks.get((popBean.dayCount - 1)).banlance + "";
            }
        } else {
            glod = entity.banlance + "";
        }

        holder.mNewsTasteGlodTv.setText(glod);
        holder.mNewsTasteDiamonsTv.setText("0");

        holder.mNewsTasteState.setOnClickListener(new MyItemClickListenerPostion(position) {
            @Override
            public void onClick(View v) {
                boolean b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                switch (p) {
                    case 0:
                        if (b && popBean != null) {
                            if (entity.status == 0) {
                                UIUtils.SendReRecevice(Constent.TASK_TAG2);
                            }
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                    case 1:
                        if (b && popBean != null) {
                            mPop.showAtLocation(view, Gravity.CENTER, 0, 0);
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        //验证手机
                        break;
                    case 4:
                        //关注圈子
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.news_taste_icon)
        ImageView mNewsTasteIcon;
        @Bind(R.id.news_taste_title)
        TextView mNewsTasteTitle;
        @Bind(R.id.news_taste_title_type)
        ImageView mNewsTasteTitleType;
        @Bind(R.id.news_taste_glod_iv)
        ImageView mNewsTasteGlodIv;
        @Bind(R.id.news_taste_glod__tv)
        TextView mNewsTasteGlodTv;
        @Bind(R.id.news_taste_dimaons_iv)
        ImageView mNewsTasteDimaonsIv;
        @Bind(R.id.news_taste_diamons_tv)
        TextView mNewsTasteDiamonsTv;
        @Bind(R.id.news_taste_state)
        ImageView mNewsTasteState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

