package com.dading.ssqs.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.NoticegBean;
import com.dading.ssqs.bean.SevenPopBean;
import com.dading.ssqs.bean.SignResultBean;
import com.dading.ssqs.bean.VersionBean;
import com.dading.ssqs.fragment.MainContentFragement;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {
    /**
     * Umeng APPkey:57a7ee5167e58e7fab002e07
     */
    private static final String FL_MAIN_CONTENT = "main_content";
    private static final String TAG = "MainActivity";

    TextView mPopNoticeTitle;
    TextView mPopNoticeContent;
    LinearLayout mPopNoticeLy;
    ImageView mPopNoticeClose;

    private MainContentFragement mFragment;
    private long mPreTime;
    private AlertDialog.Builder mBuilder;
    private DownloadCompleteReceiver mDownloadCompleteReceiver;
    private View mView;
    private View mViewMain;
    private PopupWindow mPopupWindow;
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("mazhuang", "System create" + System.currentTimeMillis());
    }

    @Override
    protected void initData() {
        mBuilder = new AlertDialog.Builder(this);
        getVerSon();
        mViewMain = View.inflate(this, R.layout.activity_main, null);

        mView = View.inflate(this, R.layout.pop_notice, null);
        mPopNoticeTitle = (TextView) mView.findViewById(R.id.pop_notice_title);
        mPopNoticeContent = (TextView) mView.findViewById(R.id.pop_notice_content);
        mPopNoticeLy = (LinearLayout) mView.findViewById(R.id.pop_notice_ly);
        mPopNoticeClose = (ImageView) mView.findViewById(R.id.pop_notice_close);

        mPopupWindow = PopUtil.popuMake(mView);

        mPopNoticeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initFragment();

        Logger.d(TAG, "点击的url---" + UIUtils.getSputils().getBoolean(Constent.IS_CLICK, false) + "----:" + UIUtils.getSputils().getString(Constent.IS_CLICK_URL, "https://www.sogou.com/"));
        /**
         * 打开外部浏览器
         */
        if (UIUtils.getSputils().getBoolean(Constent.IS_CLICK, false)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(UIUtils.getSputils().getString(Constent.IS_CLICK_URL, getIntent().getStringExtra(Constent.SPLASH_URL)));
            intent.setData(content_url);
            startActivity(intent);
        }

        getUserIsSign();
    }

    private SignBroadcastReceiver mRecevice;

    private class SignBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUserIsSign();
        }
    }

    @Override
    protected void initListener() {
        mRecevice = new SignBroadcastReceiver();
        UIUtils.ReRecevice(mRecevice, Constent.MAIN_SIGN);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.activity_main;
    }


    private void getVerSon() {
        /**
         * 请求后台命令判断是否更新
         */
        ishowVersionRefresh();
    }

    public void getNotice() {
        SSQSApplication.apiClient(classGuid).getNotices(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    NoticegBean bean = (NoticegBean) result.getData();

                    if (bean != null && !TextUtils.isEmpty(bean.getTitle()) && !TextUtils.isEmpty(bean.getContent())) {
                        mPopNoticeTitle.setText(bean.getTitle());
                        mPopNoticeContent.setText(bean.getContent());
                        UIUtils.getMainThreadHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mPopupWindow.showAtLocation(mViewMain, Gravity.CENTER, 0, 0);
                            }
                        });
                    }
                } else {
                    ToastUtils.midToast(MainActivity.this, result.getMessage(), 0);
                }
            }
        });
    }

    private void ishowVersionRefresh() {
        SSQSApplication.apiClient(classGuid).getVersionIsUpdate(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    VersionBean bean = (VersionBean) result.getData();

                    if (bean != null) {
                        showUpdataDialog(bean);
                    }
                } else {
                    Logger.e(TAG, result.getMessage() + "版本更新失败信息");
                }
            }
        });
    }

    // 弹出对话框提示更新
    public void showUpdataDialog(VersionBean bean) {
        mBuilder.setTitle("更新提醒");
        mBuilder.setCancelable(false);// 点击外部不允许取消
        mBuilder.setMessage(bean.content);

        mBuilder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
                //注册广播
                mDownloadCompleteReceiver = new DownloadCompleteReceiver();
            }
        });

        mBuilder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        if (bean.isShow == 1) {
            dialog.show();
        } else {
            getNotice();
        }
    }

    /**
     * 下载APK
     **/
    private void downloadApk() {
        String apkUrl = "http://ucdl.25pp.com/fs08/2017/01/20/4/110_6e8f30dfdd40bda7345c1f2b2c23e1e3.apk";
        Uri uri = Uri.parse(apkUrl);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(false);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkUrl));
        request.setMimeType(mimeString);
        //在通知栏中显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载ing...");
        request.setVisibleInDownloadsUi(true);
        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir("/Download", "downloadssqs.apk");
        // 将下载请求放入队列
        downloadManager.enqueue(request);
    }

    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**下载完成后安装APK**/
            installApk();
        }
    }


    private void installApk() {
        UIUtils.getSputils().putString(Constent.IS_FRISE, "0");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果不加，最后安装完成，点打开，无法打开新版本应用。
        String filePath = Environment.getExternalStorageDirectory() + "/Download/downloadssqs.apk";
        i.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        startActivity(i);
        Process.killProcess(Process.myPid()); //如果不加，最后不会提示完成、打开。
    }

    @Override
    protected void setUnDe() {
        if (mDownloadCompleteReceiver != null)
            UIUtils.UnReRecevice(mDownloadCompleteReceiver);
    }

    /**
     * 开始添加事务
     */
    private void initFragment() {
        //添加事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        mFragment = new MainContentFragement();
        //添加的那个空间内,要添加的事务,事务标记用于被寻找
        beginTransaction.add(R.id.fl_main_content, mFragment, FL_MAIN_CONTENT);
        beginTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragment.isBack()) {
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2s
                ToastUtils.midToast(UIUtils.getContext(), "再按一次,退出皇冠现金网", 0);
                mPreTime = System.currentTimeMillis();
                return;
            } else {
                super.onBackPressed();
            }
        }
    }

    private void getUserIsSign() {
        SSQSApplication.apiClient(0).getTaskDay(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    SevenPopBean bean = (SevenPopBean) result.getData();
                    if (bean != null && bean.isSign == 0) {
                        popData(bean);
                    }
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isShowPop) {
            isShowPop = false;
        }
    }

    //签到弹窗
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
    private boolean isShowPop = false;

    private void popData(final SevenPopBean popBean) {
        final View view = View.inflate(mContext, R.layout.activity_seven_day, null);
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

        mSignButton.setClickable(true);

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
                        mSevenDayAdd1.setTextColor(mContext.getResources().getColor(R.color.orange));
                        break;
                    case 2:
                        mSevenDayRedBet2.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd2.setText(text);
                        mSevenDayAdd2.setTextColor(mContext.getResources().getColor(R.color.orange));
                        break;
                    case 3:
                        mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet3.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd3.setText(text);
                        mSevenDayAdd3.setTextColor(mContext.getResources().getColor(R.color.orange));
                        break;
                    case 4:
                        mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet4.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd4.setText(text);
                        mSevenDayAdd4.setTextColor(mContext.getResources().getColor(R.color.orange));
                        break;
                    case 5:
                        mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet5.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd5.setText(text);
                        mSevenDayAdd5.setTextColor(mContext.getResources().getColor(R.color.orange));
                        break;
                    case 6:
                        mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet6.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd6.setText(text);
                        mSevenDayAdd6.setTextColor(mContext.getResources().getColor(R.color.orange));
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
        switch (dayCount) {
            case 0:
                mSevenDayRedBet1.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv1.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd1.setTextColor(mContext.getResources().getColor(R.color.orange));
                mS = data.tasks.get(0).banlance + "";
                break;
            case 1:
                mSevenDayRedBet2.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd2.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(1).banlance + "";
                break;
            case 2:
                mSevenDayRedBet3.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd3.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(2).banlance + "";
                break;
            case 3:
                mSevenDayRedBet4.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd4.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(3).banlance + "";
                break;
            case 4:
                mSevenDayRedBet5.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd5.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(4).banlance + "";
                break;
            case 5:
                mSevenDayRedBet6.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd6.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(5).banlance + "";
                break;
            case 6:
                mSevenDayRedBet7.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv7.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd7.setTextColor(mContext.getResources().getColor(R.color.orange));
                mSevenDayRedBet61.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(6).banlance + "";
                break;
            default:
                break;
        }

        UIUtils.getSputils().putString(Constent.SIGN_GLOD, mS);

        mPop = PopUtil.popuMake(view);

        //防止因为 activity is running异常
        try {
            mPop.showAtLocation(view, Gravity.CENTER, 0, 0);
        } catch (Exception ex) {
            isShowPop = true;
        }

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
                                int banlance = popBean.tasks.get((bean.dayCount - 1)).banlance;

                                UIUtils.getSputils().putBoolean(Constent.SIGN_SUC, true);

                                UIUtils.getSputils().putInt("banlance", banlance);

                                UIUtils.SendReRecevice(Constent.TASK_TAG);

                                UIUtils.SendReRecevice(Constent.REFRESH_MONY);

                                String s = "+" + banlance + "元";
                                mSignSucNum.setText(s);
                                mSignSucLy.setVisibility(View.VISIBLE);
                                mSevenDaySignLy.setVisibility(View.GONE);
                                SSQSApplication.getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPop.dismiss();
                                    }
                                }, 2000);
                                mSignButton.setImageResource(R.mipmap.s_parcel);
                            }
                        } else {
                            if (!AndroidUtilities.checkIsLogin(result.getErrno(), mContext)) {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
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
}
