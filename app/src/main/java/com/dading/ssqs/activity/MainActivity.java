package com.dading.ssqs.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.NoticegBean;
import com.dading.ssqs.bean.VersionBean;
import com.dading.ssqs.fragment.MainContentFragement;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.File;


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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
    }

    @Override
    protected int setLayoutId() {
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

                    if (bean != null) {
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
}
