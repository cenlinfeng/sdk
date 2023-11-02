package com.ddtsdk.othersdk.ttsdk;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author shenrh
 * @version 2018/3/28
 */
public class AppInfo {
    private static final String APPINFO = "appinfo";

    private static final String APPID = "appid";
    private static final String APPNAME = "appname";
    private static final String APPCHANNEL = "appchannel";

    public static void setAppInfo(Context context, int id, String name, String channel) {
        SharedPreferences sp = context.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(APPID, id);
        editor.putString(APPNAME, name);
        editor.putString(APPCHANNEL, channel);
        editor.commit();
    }

    public static int getAppId(Context context) {
        return context.getSharedPreferences(APPINFO, Context.MODE_PRIVATE).getInt(APPID, 10008);
    }

    public static String getAppName(Context context) {
        return context.getSharedPreferences(APPINFO, Context.MODE_PRIVATE).getString(APPNAME, "game_none");
    }

    public static String getAppChannel(Context context) {
        return context.getSharedPreferences(APPINFO, Context.MODE_PRIVATE).getString(APPCHANNEL, "kl_none");
    }
}
