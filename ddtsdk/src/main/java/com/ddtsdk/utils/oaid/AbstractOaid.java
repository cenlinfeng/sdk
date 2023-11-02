package com.ddtsdk.utils.oaid;

import android.content.Context;
import android.util.Log;


import com.ddtsdk.utils.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class AbstractOaid {

    protected Context context;
    protected AppIdsUpdater listener;
    protected ClassLoader classLoader;
    protected boolean hasWaitLongTime = true;
    protected final static long WAITTIME = 1000;

    public AbstractOaid(Context context, AppIdsUpdater listener, ClassLoader classLoader) {
        this.context = context;
        this.listener = listener;
        this.classLoader = classLoader;
        LogUtil.i("oaid version:" + getClass().getName());
    }

    public abstract void getOaid();

    protected void waitLongTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAITTIME);
                    if (hasWaitLongTime) {
                        LogUtil.d("has wait for oaid result for " + WAITTIME + " mill second");
                        listener.onIdsAvalid("");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected Object createIdentifierListener(String listenerCls) {
        Class clazz = null;
        try {
            clazz = Class.forName(listenerCls, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz == null) {
            return null;
        }
        return Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = "OnSupport";
                if (name.equals(method.getName())) {
                    for (Object arg : args) {
                        if (!"java.lang.Boolean".equals(arg.getClass().getName())) {
                            getOaidFromIdSupplier(arg);
                        }
                    }
                }
                return null;
            }
        });
    }

    protected void getOaidFromIdSupplier(Object object) {
        try {
            Method oaidMethod = object.getClass().getDeclaredMethod("getOAID");
            String oaid = oaidMethod.invoke(object).toString();
            LogUtil.d("oaid:" + oaid);
            hasWaitLongTime = false;
            listener.onIdsAvalid(object == null ? "" : oaid);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
