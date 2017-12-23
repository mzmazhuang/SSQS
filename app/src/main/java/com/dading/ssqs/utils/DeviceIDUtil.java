package com.dading.ssqs.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by mazhuang on 2017/11/20.
 */

public class DeviceIDUtil {
    private static String sIMEI = null;

    public static String getIMEI(Context context) {
        if (sIMEI != null) {
            return sIMEI;
        }

        String imei = UIUtils.getSputils().getString("IMEI", "");
        if (!TextUtils.isEmpty(imei)) {
            sIMEI = imei;
            return sIMEI;
        }
        try {
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                sIMEI = imei;
            }
        } catch (Exception ignored) {

        }

        if (TextUtils.isEmpty(imei)) {
            try {
                sIMEI = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception ignored) {

            }
        }

        if (TextUtils.isEmpty(sIMEI)) {
            UUID uuid = UUID.randomUUID();
            sIMEI = uuid.toString().replaceAll("-", "");
        }

        UIUtils.getSputils().putString("IMEI", sIMEI);
        return sIMEI;

    }
}
