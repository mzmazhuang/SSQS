package com.dading.ssqs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtils {
    private Editor mEdit;
    private SharedPreferences mSp;

    public SpUtils(Context context) {
        mSp = context.getSharedPreferences("SSQS", Context.MODE_PRIVATE);
        mEdit = mSp.edit();
    }

    public void putBoolean(String str, boolean value) {
        mEdit.putBoolean(str, value);
        mEdit.commit();
    }

    public void putInt(String key, int value) {
        mEdit.putInt(key, value);
        mEdit.commit();
    }

    public void putString(String key, String value) {
        mEdit.putString(key, value);
        mEdit.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    public String getString(String key, String Value) {
        return mSp.getString(key, Value);
    }

}
