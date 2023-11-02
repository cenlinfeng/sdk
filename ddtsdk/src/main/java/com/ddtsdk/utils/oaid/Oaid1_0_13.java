package com.ddtsdk.utils.oaid;

import android.content.Context;

import com.ddtsdk.utils.LogUtil;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 适用oaid 1.0.13版本
 */
public class Oaid1_0_13 extends AbstractOaid {

    public final static String LISTENER_CLASS = "com.bun.supplier.IIdentifierListener";
    public final static String MDID_HELPER = "com.bun.miitmdid.core.MdidSdkHelper";

    public Oaid1_0_13(Context context, AppIdsUpdater listener, ClassLoader classLoader) {
        super(context, listener, classLoader);
    }

    @Override
    public void getOaid() {
        long timeb = System.currentTimeMillis();
        int nres = DirectCall();
        long timee = System.currentTimeMillis();
        long offset = timee - timeb;
        LogUtil.i("get oaid time:" + offset);
        if (nres == 1008612) {//不支持的设备
            LogUtil.i("oaid not support devices");
        } else if (nres == 1008613) {//加载配置文件出错
            LogUtil.i("oaid load config error");
        } else if (nres == 1008611) {//不支持的设备厂商
            LogUtil.i("oaid not support producer");
        } else if (nres == 1008614) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
            LogUtil.i("oaid result return later");
            waitLongTime();
        } else if (nres == 1008615) {//反射调用出错
            LogUtil.i("oaid reflect error");
        }
        LogUtil.d("return value: " + nres);
    }

    private int DirectCall() {
        Class<?> listenerClazz = null;
        try {
            listenerClazz = Class.forName(LISTENER_CLASS, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (listenerClazz == null) {
            return 1008615;
        }
        LogUtil.d("oaid listener name: " + listenerClazz.getName());

        Class<?> mdidSdkHelperClass = null;
        try {
            mdidSdkHelperClass = Class.forName(MDID_HELPER, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (mdidSdkHelperClass == null) {
            return 1008615;
        }
        LogUtil.d("oaid helper name: " + mdidSdkHelperClass.getName());

        Object identifierListener = createIdentifierListener(LISTENER_CLASS);
        if (identifierListener == null) {
            return 1008615;
        }

        try {
            Method initSdkMethod = mdidSdkHelperClass.getDeclaredMethod("InitSdk",
                    Context.class, Boolean.TYPE, listenerClazz);
            return (int) initSdkMethod.invoke(null, context, true, identifierListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return 1008615;
    }

}
