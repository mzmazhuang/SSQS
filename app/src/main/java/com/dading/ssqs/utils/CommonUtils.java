package com.dading.ssqs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/*
 *  @项目名：  GooglePlay 
 *  @包名：    org.itheima15.googleplay.utils
 *  @文件名:   CommonUtils
 *  @创建者:   Administrator
 *  @创建时间:  2015/11/28 10:50
 *  @描述：    TODO
 */
public class CommonUtils {

    /**
     * 判断包是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName)
    {
        PackageManager manager = context.getPackageManager();
        try
        {
            manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    /**
     * 安装应用程序
     *
     * @param context
     * @param apkFile
     */
    public static void installApp(Context context, File apkFile)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开应用程序
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName)
    {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }
}
