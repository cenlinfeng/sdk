package com.ddtsdk.utils;

/**
 * 防快速点击
 */
public class FastClickUtils {
    private static long lastClickTime = 0;//上次点击的时间

    private static int spaceTime = 500;//时间间隔

    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间

        boolean isAllowClick;//是否允许点击

        isAllowClick = currentTime - lastClickTime <= spaceTime;

        lastClickTime = currentTime;

        return isAllowClick;

    }

}
