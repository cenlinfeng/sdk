package com.ddtsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *  构造一个本地存储账号密码的类用来实现自动登录
 */

public class KLSharedPreferencesUtils {

    /**
     *设置一个文件用来存储一组对象信息
     *Context.MODE_PRIVATE:表示只能被当前应用写入。
     * @param context
     * @param fileName
     * @param key
     * @param object
     */
    public static void setParam(Context context, String fileName, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE); //获取到SharedPreferences实例
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    /**
     * 获取存储的登录信息
     * Context.MODE_PRIVATE 只能被当前应用获取到
     * @param context
     * @param fileName  一个XML文件的名称，不需要我们自己加入>XML
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String fileName, String key, Object defaultObject) {
//        NofLogUtils.i( "sharepreference, filename:" + fileName + ",key:" + key);
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static String getString(Context context, String fileName, String key) {
        return (String) getParam(context, fileName, key, "");
    }

    public static Integer getInt(Context context, String fileName, String key) {
        return (Integer) getParam(context, fileName, key, 0);
    }

    public static Boolean getBool(Context context, String fileName, String key) {
        return (Boolean) getParam(context, fileName, key, false);
    }

    public static Float getFloat(Context context, String fileName, String key) {
        return (Float) getParam(context, fileName, key, 0f);
    }

    public static Long getLong(Context context, String fileName, String key) {
        return (Long) getParam(context, fileName, key, 0);
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
