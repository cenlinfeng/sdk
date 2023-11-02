package com.ddtsdk.model.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ddtsdk.utils.CheckSimulator;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PermissionUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.UUID;

public class DeviceInfo {
    //	private PermissionHelper mPermissionHelper;
    protected static final String PREFS_FILE = "ddt_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";

    private String nativePhoneNumber;// 当前设置的电话号码
    private String serialId = "";// sim序列号
    private String imei = "";// imei 唯一的设备ID：GSM手机的 IMEI 和 CDMA手机的 MEID.
    private String systemId = "";// ANDROID_ID
    private String systemInfo = "";// 系统信息【格式：系统版本@手机型号】
    private String uuidString = null;// 设备唯一码
    private String systemVer; //手机版本号
    private String model;//手机型号
    private String deviceScreen;//手机分辨率
    private String appVersion;//应用版本号
    private String imsi = "";//手机sim卡的串号
    private String mac = "";
    private String userua = "";//玩家手机UA
    private Boolean isbreak = false;//是否root
    private Boolean oaid;
    private String issimulator;//是否模拟器
    private String packname;

    public DeviceInfo(Context context) {
        if (PermissionUtil.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            hasPermissionMethod(context);
        }
            commonMethod(context);

    }

    //当权限存在时调用。
    private void hasPermissionMethod(Context context) {
//		LogUtil.d("当权限存在时调用:hasPermissMethod");
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            nativePhoneNumber = telephonyManager.getLine1Number() != null ? telephonyManager.getLine1Number() : "";
            String tempImei = telephonyManager.getDeviceId();
            imei = (tempImei != null) && !tempImei.equals("unknown") ? telephonyManager.getDeviceId() : "";
//			imei = telephonyManager.getDeviceId() != null ? telephonyManager.getDeviceId() : "";
            serialId = telephonyManager.getSimSerialNumber() != null ? telephonyManager.getSimSerialNumber() : "";
            imsi = telephonyManager.getSubscriberId() != null ? telephonyManager.getSubscriberId() : "";
        } catch (Exception e) {
            LogUtil.e("权限还是没开，重启APP生效。");
        }
    }


    //不管有没有权限都会调用。
    private void commonMethod(Context context) {
//		LogUtil.d("不管有没有权限都会调用:commonMethod");
        setNativePhoneNumber();
        setSystemInfo();
        setSystemId(context);
        setUuidString(context);
        setDevScreen(context);
        setAppVer(context);
        setMac(context);
        setUserua(context);
        setIsbreak();
//        setIssimulator(context);
        setPackname(context);

        LogUtil.d("电话号码:" + nativePhoneNumber + "\n" +
                "sim序列号:" + serialId + "\n" +
                "imei:" + imei + "\n" +
                "ANDROID_ID:" + systemId + "\n" +
                "系统信息【格式：系统版本:" + systemInfo + "\n" +
                "设备唯一码:" + uuidString + "\n" +
                "手机版本号:" + systemVer + "\n" +
                "手机型号:" + model + "\n" +
                "手机分辨率:" + deviceScreen + "\n" +
                "应用版本号:" + appVersion + "\n" +
                "手机sim卡的串号:" + imsi + "\n" +
                "mac:" + mac + "\n");
    }

    public void setPackname(Context context) {
        this.packname = context.getPackageName();
    }

    private void setNativePhoneNumber() {
        if (TextUtils.isEmpty(nativePhoneNumber)) {
            nativePhoneNumber = "+0000";
        }
    }

    private void setSystemInfo() {
        systemVer = Build.VERSION.RELEASE;
        model = "";
        try {
            model = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        systemInfo = systemVer + "@" + model;
    }

    private void setSystemId(Context context) {
        systemId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    private void setUuidString(Context context) {
        if (uuidString == null) {
            final SharedPreferences prefs = context.getSharedPreferences(
                    PREFS_FILE, 0);
            uuidString = prefs.getString(PREFS_DEVICE_ID, null);
            if (uuidString == null) {
                // Use the Android ID unless it's broken, in which case fallback on deviceId,
                // unless it's not available, then fallback on a random number which we store to a prefs file
                try {
                    if (!"9774d56d682e549c".equals(systemId)) {
                        uuidString = UUID.nameUUIDFromBytes(
                                systemId.getBytes("utf8")).toString();
                    } else {
                        uuidString = imei != null ? UUID.nameUUIDFromBytes(
                                imei.getBytes("utf8")).toString() : UUID
                                .randomUUID().toString();
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            // Write the value out to the prefs file
            prefs.edit().putString(PREFS_DEVICE_ID, uuidString).commit();
        }
    }

    private void setDevScreen(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        if (context instanceof Activity) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        deviceScreen = W + "*" + H;
    }

    private void setAppVer(Context context) {
        try {
            appVersion = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode + "";//info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            appVersion = "";
        }
//		appVersion = Utils.getVersionCode(context)+"";
    }

    public String getIssimulator() {
        return issimulator;
    }

    public void setIssimulator(Context context) {
        this.issimulator = String.valueOf(CheckSimulator.getInstance().readSysProperty(context));
    }

    private void setMac(Context context) {
        //获取wifi服务
//		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		//判断wifi是否开启
//		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
//		}
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		int ipAddress = wifiInfo.getIpAddress();
        this.mac = getPhoneMac(context);
    }

    public String getImsi() {
        return imsi;
    }

    public String getMac() {
        return mac;
    }

    public String getNativePhoneNumber() {
        return nativePhoneNumber;
    }

    public String getSerialId() {
        return serialId;
    }

    public String getImei() {
        return imei;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getUuid() {
        return uuidString;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public String getSystemVer() {
        return systemVer;
    }

    public void setSystemVer(String systemVer) {
        this.systemVer = systemVer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceScreen() {
        return deviceScreen;
    }

    public void setDeviceScreen(String deviceScreen) {
        this.deviceScreen = deviceScreen;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserua() {
        return userua;
    }

    public void setUserua(Context context) {
        this.userua = getUA(context);
    }

    public Boolean getIsbreak() {
        return isbreak;
    }

    public void setIsbreak() {
        isbreak = isDeviceRooted();
    }

    private static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
    }

    private static String getPhoneMac(Context context) {
        String phoneMAC = "";
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
            if (networkInterface == null) {
                phoneMAC = getWifiMac(context);
                return phoneMAC;
            }
            byte[] addrByte = networkInterface.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (byte b : addrByte) {
                sb.append(String.format("%02X:", b));
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            phoneMAC = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            phoneMAC = getWifiMac(context);
        }
        return phoneMAC;
    }

    private static String getWifiMac(Context context) {
        String wifiMAC = "";
        WifiManager wifiM = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        try {
            WifiInfo wifiI = wifiM.getConnectionInfo();
            wifiMAC = wifiI.getMacAddress();
        } catch (NullPointerException e1) {
            e1.printStackTrace();
            wifiMAC = "02:00:00:00:00:00";
        }
        return wifiMAC;
    }

    /**
     * 获取ua信息
     *
     * @throws UnsupportedEncodingException
     */
    private static String getUA(Context context) {
        // 4.4版本之后，new WebView不能在主线程调用，否则会闪退。
        Log.e(LogUtil.TAG, "SKD_INT=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 19) {
            WebView webview = new WebView(context);
            webview.layout(0, 0, 0, 0);
            String str = webview.getSettings().getUserAgentString();
            Log.e(LogUtil.TAG, "ua0=" + str);
            return str;
        } else {
            String str = WebSettings.getDefaultUserAgent(context);
            Log.e(LogUtil.TAG, "ua1=" + str);
            return str;
        }
    }

    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = new String[]{"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        String[] var2 = locations;
        int var3 = locations.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String location = var2[var4];
            if ((new File(location + su)).exists()) {
                return true;
            }
        }

        return false;
    }

    public String getPackname() {
        return packname;
    }
}
