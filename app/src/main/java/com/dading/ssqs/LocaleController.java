package com.dading.ssqs;

import com.dading.ssqs.utils.Logger;

import java.util.Locale;

/**
 * Created by mazhuang on 2017/11/20.
 */

public class LocaleController {

    private Locale systemDefaultLocale;
    private String localLanguage;

    private static volatile LocaleController Instance = null;

    public static LocaleController getInstance() {
        LocaleController localInstance = Instance;
        if (localInstance == null) {
            synchronized (LocaleController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new LocaleController();
                }
            }
        }
        return localInstance;
    }

    private LocaleController() {
        init();
    }

    private void init() {
        systemDefaultLocale = Locale.getDefault();

        String languageCode;

        if (systemDefaultLocale == null) {
            languageCode = "en";
        } else {
            languageCode = systemDefaultLocale.getLanguage();
        }

        String systemLanguage = languageCode.split("[-]")[0].toLowerCase();

        localLanguage = systemLanguage;
    }

    public static String getLocalLanguage() {
        return getInstance().localLanguage;
    }

    public String getCountry() {
        return systemDefaultLocale.getCountry();
    }

    public static String getString(int res) {
        return getInstance().getStringInternal(res);
    }

    public static String getString(int res, Object... args) {
        return getInstance().getStringInternal(res, args);
    }

    private String getStringInternal(int res, Object... args) {
        String value = null;
        try {
            if (SSQSApplication.getContext() != null) {
                value = SSQSApplication.getContext().getString(res);
            }
        } catch (Exception e) {
            String TAG = "LocaleController";
            Logger.INSTANCE.e(TAG, e);
        }
        if (value == null) {
            value = "LOC_ERR:" + res;
        }
        try {
            return String.format(value, args);
        } catch (Exception e) {
            String TAG = "LocaleController";
            Logger.INSTANCE.e(TAG, e);
        }
        return value;
    }
}
