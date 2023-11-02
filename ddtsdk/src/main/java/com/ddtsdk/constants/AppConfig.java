package com.ddtsdk.constants;

import android.app.Activity;
import android.content.Context;

import com.ddtsdk.KLApplication;
import com.ddtsdk.model.protocol.bean.ExclusiveVolumeBean;
import com.ddtsdk.model.protocol.bean.PayConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppConfig {

    public final static int FLAG_SUCCESS = 0;

    public final static int FLAG_REQUEST_ERROR = 2;
    public final static int WEB_PAY_SUCCESS = 10;
    public final static int FLAG_FAIL = 20;
    public final static int RETURN_LOGIN = 23; //返回登录界面
    public static final int ONLINE_TIME_LIMIT = 24;
    public static final int JRTTLINE_TIME = 25;
    public final static String GAME_TIME_ACTION = "com.game.time";
    public static KLApplication mApplication;

    public static int userType = 0;
    public static boolean isAppExit = false;
    public static boolean isApkCacheExit = false;
    public static boolean isFirst = true;
    public static boolean isDownload = true;
    public static String GAME_TIME = "GAME_TIME";
    public static boolean isShow = false;
    public static boolean isLogining = false; // 是否已经处于登录UI拉起状态，避免拉起两个登录UI

    public static boolean simulatorCanLogin = true;

    public static Boolean isgdtsdk = false;
    public static Boolean isttsdk = false;
    public static boolean sdkInitSuccess = false;  //SDK初始化是否成功
    public static boolean adSdkInitSuccess = false;  //广告SDK初始化是否成功
    public static int adType = -1;  //广告类型： 0：今日头条， 1：广点通  2：UC， 3：快手，具体看AdFactory类
    public static boolean ORIENTATION_Land = true;

    public static void clearCache() {
        isApkCacheExit = false;
    }

    public static String coupon_id = ""; //选中代金卷值

    public static Activity activity;

    public static PayConfig pyyConfig = null;

    public static ExclusiveVolumeBean exclusiveConfig = null;
//    public static PayData payData = null;
    public static String isExclusive = ""; //充值来源 ""为正常充值 , true为直充卷充值

    public static List<String> intersect(String[] a, String[] b) {
        List<String> list = new ArrayList<String>(Arrays.asList(b));
        list.retainAll(Arrays.asList(a));
        return list;
    }

    public static int resourceId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type,
                context.getPackageName());
    }

    /**
     * 计算时间
     */
    public static String GameTime(String begin, String end) {
        String time = "";
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            Date beginDate = fmt.parse(begin);
            Date endDate = fmt.parse(end);
            long between = (endDate.getTime() - beginDate.getTime()) / 1000;
            long minute = between / 60;
            time = minute + "";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return time;
    }

    public static Activity getActivity(){
        return activity;
    }
}
