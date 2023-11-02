package com.ddtsdk.utils.oaid;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import com.ddtsdk.utils.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class KlOaidHelper {
    public static String TAG =  KlOaidHelper.class.getName();
    private final static KlOaidHelper instance = new KlOaidHelper();
    private final Map<String, String> oaidVersions;
    private final static String JLIBRARY_CLASS = "com.bun.miitmdid.core.JLibrary";

    public KlOaidHelper() {
        oaidVersions = new HashMap<>();
        oaidVersions.put(Oaid1_0_13.LISTENER_CLASS,  KlOaidHelper.class.getPackage().getName() + ".Oaid1_0_13");
        oaidVersions.put(Oaid1_0_23_25.LISTENER_CLASS,  KlOaidHelper.class.getPackage().getName() + ".Oaid1_0_23_25");
    }

    public static KlOaidHelper getInstance() {
        return instance;
    }

    public void init(Context context) {
        try {
            Class<?> aClass = Class.forName(JLIBRARY_CLASS);
            Method initEntryMethod = aClass.getDeclaredMethod("InitEntry", Context.class);
            initEntryMethod.invoke(null, context);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LogUtil.d( "JLibrary不存在");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void getOaid(Context context, AppIdsUpdater callback, ClassLoader classLoader) {
        if (Build.VERSION.SDK_INT < 28) {
             LogUtil.d( "oaid not support because low system version:" + Build.VERSION.SDK_INT);
            callback.onIdsAvalid("");
            return;
        }
        for (Map.Entry<String, String> entry : oaidVersions.entrySet()) {
            if (isSupport(entry.getKey())) {
                try {
                    Class aClass = Class.forName(entry.getValue());
                    AbstractOaid oaidHandler = (AbstractOaid) aClass.getDeclaredConstructor(Context.class, AppIdsUpdater.class, ClassLoader.class)
                            .newInstance(context, callback, classLoader);
                    oaidHandler.getOaid();
                } catch (Exception e) {
                     LogUtil.d( "oaid not support because something wrong when load oaid sdk");
                    callback.onIdsAvalid("");
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isSupport(String callbackName) {
        try {
            Class.forName(callbackName);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
