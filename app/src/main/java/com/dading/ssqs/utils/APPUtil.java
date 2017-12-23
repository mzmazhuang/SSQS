package com.dading.ssqs.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.dading.ssqs.bean.AppInfo;
import com.dading.ssqs.bean.SpaceInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class APPUtil {
	

	public static SpaceInfo getMemory(){
		File file = Environment.getDataDirectory();
		
		long totalSpace = file.getTotalSpace();
		long freeSpace = file.getFreeSpace();
		long userSpace = totalSpace - freeSpace;
		
		 return new SpaceInfo(totalSpace, freeSpace, userSpace);
	}
	
	public static SpaceInfo getStorage(){
		
		File file = Environment.getExternalStorageDirectory();
		long totalSpace = file.getTotalSpace();
		long freeSpace = file.getFreeSpace();
		long userSpace = totalSpace - freeSpace;
		return new SpaceInfo(totalSpace, freeSpace, userSpace);
		
	}
	
	public static List<AppInfo> getAllApps(Context context){
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		
		//获取所有的应用程序(用户+系统)
		PackageManager pm = context.getPackageManager();
		
		List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		
		/**
		 * 图标
		 * 应用程序名
		 * 安装位置
		 * APK大小
		 *用户的APK:data/app
		 * 	系统的在data-system
		 */
		for (ApplicationInfo info : appInfos) {
			
			CharSequence apkLable = info.loadLabel(pm);//获取程序名称
			
			Drawable apkLcon = info.loadIcon(pm);//获取程序图标
			
			int uid = info.uid;
			
			String packageName = info.packageName;
			
			String sourceDir = info.sourceDir;
			
			//APK资源安装路径 datadir是普通放置位置
			File file = new File(info.sourceDir);
			
			
			long appSize = file.length();//获取程序大小
			//判断是用户程序还是系统程序  应用程序的flags可能包含多个  所以我们必须&上安装在sd卡的标记为  看是否还是等于sd卡标记或者系统标记
			boolean isSdcard = (info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
			boolean isSystem = (info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
			
			
			
			 AppInfo appInfo = new AppInfo(apkLable, apkLcon, appSize, isSdcard, isSystem ,packageName ,sourceDir ,uid);
			 
			 appInfoList.add(appInfo);
		}
		return appInfoList;
	}

	/**
	 * 检测是否已经在运行
	 * @param mContext
	 * @param activityClassName
     * @return
     */
	public static boolean isActivityRunning(Context mContext,String activityClassName){
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
		if(info != null && info.size() > 0){
			ComponentName component = info.get(0).topActivity;
			if(activityClassName.equals(component.getClassName())){
				return true;
			}
		}
		return false;
	}
}
