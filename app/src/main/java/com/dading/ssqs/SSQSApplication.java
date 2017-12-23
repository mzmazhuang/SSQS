package com.dading.ssqs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.utils.AppException;
import com.dading.ssqs.utils.Constants;
import com.dading.ssqs.utils.DeviceIDUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.SpUtils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/2 15:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class SSQSApplication extends MultiDexApplication {

    private static Context mContext;
    private static Thread mMainThread;
    private static int mMainTreadId;
    private static Looper mMainLooper;
    private static Handler mHandler;
    private static SpUtils mSpUtils;

    //mazhuang write
    private static CcApiClient mApiClient;
    public static volatile RequestManager glide;
    public static IWXAPI mWxApi;
    public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private String TAG = "SSQSApplication";

    public static Context getContext() {
        return mContext;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static SpUtils getSpUtils() {
        return mSpUtils;
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constants.APP_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String registrationID = JPushInterface.getRegistrationID(this);//极光推送的注册标识
        LogUtil.util(TAG, "返回数据是------------------------------:" + registrationID);

        LeakCanary.install(this);
        // 主线程
        mMainThread = Thread.currentThread();
        // 主线程Id
        mMainTreadId = android.os.Process.myTid();
        // tid thread   uid user  pid process
        // 主线程Looper对象
        mMainLooper = getMainLooper();
        // 定义一个handler
        mHandler = new Handler();
        mContext = getApplicationContext();

        AppException appException = AppException.getInstance();
        // appException.init(mContext);
        mSpUtils = new SpUtils(this);

        mApiClient = CcApiClient.instance(mContext);

        mApiClient.setUserDevice(getUA());

        glide = Glide.with(this);

        registToWX();
    }

    public static CcApiClient apiClient(int guid) {
        mApiClient.setRequestId(String.valueOf(guid));
        return mApiClient;
    }

    //获取用户手机信息
    public static String getUA() {
        String deviceModel = "unknown";
        String langCode = "en";
        String appVersion = "unknown";
        String systemVersion = "Android " + Build.VERSION.SDK_INT;
        String imei = "0000";

        try {
            imei = DeviceIDUtil.getIMEI(mContext);
            langCode = LocaleController.getLocalLanguage();
            deviceModel = Build.MANUFACTURER;
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            appVersion = String.valueOf(pInfo.versionCode);
            systemVersion = "Android " + Build.VERSION.SDK_INT;
        } catch (Exception e) {
            LogUtil.e("Application", e);
        }
        return "imei=" + imei + "&model=" + deviceModel + "&language=" + langCode + "&version=" + appVersion + "&os=" + systemVersion;
    }
}