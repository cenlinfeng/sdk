package com.ddtsdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bytedance.hume.readapk.HumeSDK;
import com.kwai.monitor.payload.TurboHelper;
import com.tencent.vasdolly.helper.ChannelReaderUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    /**
     * ddt给CP的配置文件ddt.properties，下面是对应的参数
     * {
     * version=2.0.2	// SDK版本号
     * agent=test		// 渠道号，用于区分这个SDK包是哪个渠道的。
     * // 如果随便传，后台会自动生成。
     * // 如果需要融合第三方渠道或广告渠道，这个标识可以结合使用。
     * // 必须以后台录入的渠道列表内的渠道号为准，否则会丢失市场数据。
     * <p>
     * // 如需对接头条（其他第三方以此类推）
     * othersdk_aid=150148			// 在头条平台分配的aid
     * othersdk_gamename=ddt_test	// 在头条平台分配的gamename
     * <p>
     * // 如需开启启动页（闪屏图）getIsH5Game
     * mainclass=com.ddt.sdkdemo.MainActivity		// app启动的activity类名所在路径
     * }
     */
    private static final String PRPPERTIES_FILE = "ddt.properties";

    // UTF-8 encoding
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String LOGTAG = "Utils";

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }

        return false;
    }

    public static String NetWork_Type(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        String type = info.getTypeName();
        return type;

    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;

        isMobileDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return isMobileDataEnable;
    }

    /**
     * 判断wifi 是否可用
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

    /**
     * Get UTF8 bytes from a string
     *
     * @param string String
     * @return UTF8 byte array, or null if failed to get UTF8 byte array
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null)
            return new byte[0];

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * If system doesn't support UTF-8, use another way
             */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }

    /**
     * Get string in UTF-8 encoding
     *
     * @param b byte array
     * @return string in utf-8 encoding, or empty if the byte array is not
     * encoded with UTF-8
     */
    public static String getUTF8String(byte[] b) {
        if (b == null)
            return "";
        return getUTF8String(b, 0, b.length);
    }

    /**
     * Get string in UTF-8 encoding
     */
    public static String getUTF8String(byte[] b, int start, int length) {
        if (b == null) {
            return "";
        } else {
            try {
                return new String(b, start, length, ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    /**
     * 金额String类型转成整数类型
     *
     * @param str
     * @return
     */
    public static String price2String(String str) {
        return str.substring(0, str.length() - 3);
    }

	/*public static void getSeferencegame(Context context) {

		Seference seference = new Seference(context);
		if (TextUtils.isEmpty(AppConstants.Sessid)) {
			AppConstants.Sessid = seference.getPreferenceData("ddt_game", "sessid");

		}
		if (TextUtils.isEmpty(AppConstants.Token)) {
			AppConstants.Token = seference.getPreferenceData("ddt_game", "token");
		}
		//LogUtil.d("获取，"+seference.getPreferenceData("ddt_game", "token"));
	}

	public static void saveSeferencegameuser(Context context,
			LoginMessage gameuser) {
		Seference seference = new Seference(context);
		seference.savePreferenceData("ddt_gameuser", "userurl",
				gameuser.getUserurl());
		seference.savePreferenceData("ddt_gameuser", "orderurl",
				gameuser.getOrderurl());
		seference.savePreferenceData("ddt_gameuser", "liboaurl",
				gameuser.getLibaourl());
		seference.savePreferenceData("ddt_gameuser", "serviceurl",
				gameuser.getService());
		/*seference.savePreferenceData("ddt_gameuser", "tuijianurl",
				gameuser.getTuijianurl());
		LogUtil.d("保存，"+gameuser.getTuijianurl());
	}*/

    public static String getDDT_Agent(Context context) {
        String ver_ddt = "";
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open("ddt_channel.properties"));
            ver_ddt = properties.getProperty("ddt_agent");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ver_ddt;
    }


    public static String getAgent(Context context) {
        String ddt_ver_id = "";
        if (!getDDT_Agent(context).equals("")) {
            Log.e("133545", getDDT_Agent(context));
            return getDDT_Agent(context);
        }
        String verName = getPretoutiaoAgent(context);

        // 头条分包
        if (verName.contains("ttsdk_")) {
            ddt_ver_id = HumeSDK.getChannel(context);
            if (!TextUtils.isEmpty(ddt_ver_id)) {
                return getAgent(context, true) + "_" + ddt_ver_id;
            }
        }
        //快手分包
        if (verName.contains("kssdk_")) {
            ddt_ver_id = TurboHelper.getChannel(context);
            if (!TextUtils.isEmpty(ddt_ver_id)) {
                return getAgent(context, true) + "_" + ddt_ver_id;
            }
        }

        //gdt分包
        if (verName.contains("gdtsdk_")) {
            ddt_ver_id = ChannelReaderUtil.getChannel(context);
            if (!TextUtils.isEmpty(ddt_ver_id)) {
                return getAgent(context, true) + "_" + ddt_ver_id;
            }
        }
        //我们的渠道分包
        if (!TextUtils.isEmpty(getChannel(context))) {
            return getChannel(context);
        }

        //如果渠道号是空的情况下再去找配置文件
        if (!TextUtils.isEmpty(ddt_ver_id)) {
            return ddt_ver_id;
        }
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            ddt_ver_id = properties.getProperty("agent");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ddt_ver_id;
    }

    //获取渠道号
    public static String getAgent(Context context, boolean isMy) {
        //节省时间，false情况下直接返回空字符
        if (!isMy) return "";
        String ddt_ver_id = "";
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            ddt_ver_id = properties.getProperty("agent");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ddt_ver_id;
    }

    public static String getPretoutiaoAgent(Context context) {
        String ver_pre = null;
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            ver_pre = properties.getProperty("agent");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ver_pre;
    }


    public static String getWYBusinessId(Context context) {
        String businessId = null;
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            businessId = properties.getProperty("WYBusinessId");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessId;
    }

    /**
     * 游戏启动页
     */
    public static String getMainclass(Context context) {
        Properties properties = new Properties();
        String mainclass = "";
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            mainclass = properties.getProperty("mainclass");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mainclass;
    }

    public static String getChannel(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        LogUtil.d("utils--getChannel---sourceDir:" + sourceDir);
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.indexOf("DDTchannel") > 0) {
                    ret = entryName;
                    LogUtil.d("==>" + ret);
                    break;
                }
            }
        } catch (IOException e) {
            LogUtil.e("utils--getChannel---e:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return "";
        }
    }

    public static String getVersion(Context context) {
        Properties properties = new Properties();
        String Version = "0.0";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            Version = properties.getProperty("version");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Version = "0.0";
        }
        return Version;
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        LogUtil.e("getVersionCode, package=" + mContext.getPackageName());
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.e("getVersionCode, error=" + e.getMessage());
        }
        LogUtil.d("getVersionCode, versionCode=" + versionCode);
        return versionCode;
    }

    /**
     * 应用名称中文
     *
     * @param context
     * @return 中文GameName
     */
    public static String getGameName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getOtherSdkAid(Context context) {
        Properties properties = new Properties();
        int othersdk_aid = 0;

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            LogUtil.i("头条id" + properties.getProperty("othersdk_aid"));
//			String aid_str = Base64.decode(properties.getProperty("othersdk_aid"));
            String aid_str = properties.getProperty("othersdk_aid");
            othersdk_aid = Integer.parseInt(aid_str);
        } catch (Exception e) {
            LogUtil.e("getOtherSdkAid error." + e);
            e.printStackTrace();
        }
        LogUtil.d("othersdk_aid == >" + othersdk_aid);
        return othersdk_aid;
    }

    public static String getToutiaoGameName(Context context) {
        Properties properties = new Properties();
        String othersdk_gamename = "unknow";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            othersdk_gamename = properties.getProperty("othersdk_gamename");
        } catch (Exception e) {
            LogUtil.e("getToutiaoGameName error." + e.getMessage());
            e.printStackTrace();
        }
        LogUtil.d("othersdk_gamename == >" + othersdk_gamename);
        return othersdk_gamename;
    }

    public static String getPackageName(Context context) {
        String name = "";
        try {
            name = context.getPackageName();
            LogUtil.d("getPackageName:" + name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }


    public static String getGDTUserActionSetID(Context context) {
        Properties properties = new Properties();
        String userActionSetId = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            userActionSetId = Base64.decode(properties.getProperty("GDTUserActionSetID"));
        } catch (Exception e) {
            LogUtil.e("getGDTUserActionSetID error." + e.getMessage());
            e.printStackTrace();
        }
        return userActionSetId;
    }

    public static String getGDTAppSecretKey(Context context) {
        Properties properties = new Properties();
        String appSecretKey = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            appSecretKey = Base64.decode(properties.getProperty("GDTAppSecretKey"));
        } catch (Exception e) {
            LogUtil.e("getGDTAppSecretKey error." + e.getMessage());
            e.printStackTrace();
        }
        return appSecretKey;
    }

    public static String getUCAppId(Context context) {
        Properties properties = new Properties();
        String userActionSetId = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            userActionSetId = Base64.decode(properties.getProperty("UCAppId"));
        } catch (Exception e) {
            LogUtil.e("getUCAppId error." + e.getMessage());
            e.printStackTrace();
        }
        return userActionSetId;
    }

    public static String getUCAppName(Context context) {
        Properties properties = new Properties();
        String appSecretKey = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            appSecretKey = Base64.decode(properties.getProperty("UCAppName"));
        } catch (Exception e) {
            LogUtil.e("getUCAppName error." + e.getMessage());
            e.printStackTrace();
        }
        return appSecretKey;
    }

    public static String getKSAppId(Context context) {
        Properties properties = new Properties();
        String userActionSetId = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            userActionSetId = properties.getProperty("KSAppId");
        } catch (Exception e) {
            LogUtil.e("getKSAppId error." + e.getMessage());
            e.printStackTrace();
        }
        return userActionSetId;
    }

    public static String getKSAppName(Context context) {
        Properties properties = new Properties();
        String appSecretKey = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            appSecretKey = properties.getProperty("KSAppName");
        } catch (Exception e) {
            LogUtil.e("getKSAppName error." + e.getMessage());
            e.printStackTrace();
        }
        return appSecretKey;
    }

    public static String getAQYAppId(Context context) {
        Properties properties = new Properties();
        String appSecretKey = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            appSecretKey = Base64.decode(properties.getProperty("AQYAppId"));
        } catch (Exception e) {
            LogUtil.e("getAQYAppId error." + e.getMessage());
            e.printStackTrace();
        }
        return appSecretKey;
    }

    public static String getWxAppId(Context context) {
        Properties properties = new Properties();
        String wxAppid = "";

        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            wxAppid = Base64.decode(properties.getProperty("weChat"));
        } catch (Exception e) {
            LogUtil.e("getWxAppId error." + e.getMessage());
            e.printStackTrace();
        }
        return wxAppid;
    }

    public static String getIsH5Game(Context context) {
        Properties properties = new Properties();
        String isH5 = "";
        try {
            properties.load(context.getAssets().open(PRPPERTIES_FILE));
            isH5 = properties.getProperty("isH5Game");
        } catch (Exception e) {
            LogUtil.e("getIsH5Game error." + e.getMessage());
            e.printStackTrace();
        }
        return isH5;
    }


    public static boolean getSdkParams(Context mContext,String mName) {
        boolean UserAgreement = false;
        Properties properties = new Properties();
        try {
            properties.load(mContext.getAssets().open(PRPPERTIES_FILE));
            UserAgreement = Boolean.parseBoolean(properties.getProperty(mName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UserAgreement;
    }


    /**
     * 获取隐私协议
     *
     * @param mContext
     * @return
     */
    public static String getPrivacyAgreementUrl(Context mContext) {
        String PrivacyUrl = "";
        Properties properties = new Properties();
        try {
            properties.load(mContext.getAssets().open(PRPPERTIES_FILE));
            PrivacyUrl = properties.getProperty("KL_YSXY");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PrivacyUrl;
    }

    /**
     * 获取用户协议
     *
     * @param mContext
     * @return
     */
    public static String getUserAgreementUrl(Context mContext) {
        String PrivacyUrl = "";
        Properties properties = new Properties();
        try {
            properties.load(mContext.getAssets().open(PRPPERTIES_FILE));
            PrivacyUrl = properties.getProperty("KL_YHXY");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PrivacyUrl;
    }


    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    public static void showToast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * SP转px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param dp 获取到布局的信息是DP转成px
     * @return
     */
    public static int dp2px(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }


    public static int dp2px(Context context, float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }


    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 数学运输 减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String subtract(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.subtract(b2).toString();

    }

    /**
     * 数学运输 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String multiply(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).toString();

    }

    public static boolean isFileExistInAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] fileNames = assetManager.list("");
            if (fileNames == null) {
                return false;
            }
            for (String name : fileNames) {
                if (name.equals(fileName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String readFromAssets(Context context,String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = assetManager.open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


}
