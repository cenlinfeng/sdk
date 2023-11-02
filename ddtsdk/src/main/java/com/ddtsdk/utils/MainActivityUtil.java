package com.ddtsdk.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivityUtil {
    /**
     *
     * @param obj  调用类的对象
     * @param name 调用对象的方法名
     */
    public static void getMethod(Object obj, String name){
        try {
            Method method = obj.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            method.invoke(obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param obj  调用类的对象
     * @param name 调用对象的方法名
     */
    public static void getMethod(Object obj, String name, String param, Class<?>... parameterTypes){
        try {
            Method method = obj.getClass().getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            method.invoke(obj, param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
