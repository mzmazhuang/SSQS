package com.dading.ssqs.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	public CharSequence apkLable;
	public Drawable apkLcon;
	public long appSize;
	
	public boolean isSdcard;
	public boolean isSystem;
	public String packageName;
	public String sourceDir;
	public int uid;
	
	

	public AppInfo(CharSequence apkLable, Drawable apkLcon, long appSize,
			boolean isSdcard, boolean isSystem, String packageName,
			String sourceDir, int uid) {
		super();
		this.apkLable = apkLable;
		this.apkLcon = apkLcon;
		this.appSize = appSize;
		this.isSdcard = isSdcard;
		this.isSystem = isSystem;
		this.packageName = packageName;
		this.sourceDir = sourceDir;
		this.uid = uid;
	}



	@Override
	public String toString() {
		return "AppInfo [apkLable=" + apkLable + ", apkLcon=" + apkLcon
				+ ", appSize=" + appSize + ", isSdcard=" + isSdcard
				+ ", isSystem=" + isSystem + ", packageName=" + packageName
				+ ", fileDir=" + sourceDir + "]";
	}
	

}
