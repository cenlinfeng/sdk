package com.ddt.h5game;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class H5Utils {

    /**
     * @param obj  调用类的对象
     * @param url h5传输的url
     */
    public static void getMethodFromUrl(Object obj, String url, Class<?>... parameterTypes){
        Log.e("urlr", url);
        try {
            String methodName = url.substring("ddt://".length(), url.indexOf("?"));
            String params = url.substring(url.indexOf("?") + 1);
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
