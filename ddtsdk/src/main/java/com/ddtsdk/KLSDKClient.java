package com.ddtsdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.heartbeat.KLHeartbeatUtils;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.manager.PopManager;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.model.protocol.bean.RoleMsg;
import com.ddtsdk.mylibrary.BuildConfig;
import com.ddtsdk.othersdk.manager.AbnormalAdManager;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.ui.activity.KLFirstQuickLoginActivity;
import com.ddtsdk.ui.activity.KLLoginActivity;
import com.ddtsdk.ui.activity.KLPlatformPayActivity;
import com.ddtsdk.ui.activity.KLQuickLoginActivity;
import com.ddtsdk.ui.activity.KLSmallGameActivity;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.utils.CheckSimulator;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.utils.KLPermissionUtils;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.QuickLoginUtils;
import com.ddtsdk.utils.SecurityUtils;
import com.ddtsdk.utils.SharedPreferenceUtil;
import com.ddtsdk.utils.TimeReport;
import com.ddtsdk.utils.TimeUtil;

import com.ddtsdk.utils.Utils;
import com.ddtsdk.utils.oaid.AppIdsUpdater;
import com.ddtsdk.utils.oaid.KlOaidHelper;
import com.ddtsdk.view.Exitdialog;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.listener.ApiListenerInfo;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.listener.IKLExitListener;
import com.ddtsdk.listener.IKLUserListener;
import com.ddtsdk.listener.InitListener;
import com.ddtsdk.listener.UserApiListenerInfo;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.protocol.bean.ResCertificate;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.model.protocol.params.RoleInfoParams;
import com.ddtsdk.network.InitData;
import com.ddtsdk.network.JrttTimeRequest;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.activity.KLPaymentActivity;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.view.PopupDialogRole;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CZG on 2020/4/28.
 */
public class KLSDKClient extends KLSDK implements KLPermissionUtils.AllPermissionAgreeCallback {
    private static Context mContext = null;
    private Exitdialog exitdialog;

    private static IKLUserListener mIKLUserListener;
    private static ApiListenerInfo apiListenerInfo;
    private static IDdtListener<LoginMessageInfo> mILoginListener;
    private boolean allPermissionAgree;

    protected KLSDKClient() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void wrapLoginInfo() {
        LoginMessageInfo loginMessageInfo = new LoginMessageInfo();
        loginMessageInfo.setResult("success");
        loginMessageInfo.setMsg("登录成功");
        loginMessageInfo.setTime(AppConstants.time);
        loginMessageInfo.setDologin_h5(AppConstants.dologin_h5);
        loginMessageInfo.setUid(AppConstants.uid);
        loginMessageInfo.setGametoken(AppConstants.gametoken);
        loginMessageInfo.setSessid(AppConstants.Sessid);
        loginMessageInfo.setIs_test(AppConstants.is_test);

        AppConfig.isLogining = false;
        if (mILoginListener != null) {
            mILoginListener.onSuccess(loginMessageInfo);
        }
        TimeReport.getInstance(getContext()).report();
        try {
            PopManager.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //开启心跳上报
        KLHeartbeatUtils.getInstance().startHeartbeat(KLSDK.getInstance().getContext());
        popVolumeHint();
    }

    private void popVolumeHint() {
        //先判断服务器返回数据
        if (AppConstants.is_voucher) {
            //登录回调时候弹出代金卷提示框，每天提示一次
            if (SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.VOLUME_HINT, "").equals("")
                    || !SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.VOLUME_HINT, "").
                    equals(TimeUtil.getCurrentTime("yyyyMMdd"))) {
                SharedPreferenceUtil.getInstance(mContext).setString(SharedPreferenceUtil.VOLUME_HINT, TimeUtil.getCurrentTime("yyyyMMdd"));
                KLCommonAffirmDialog
                        .Builder(1, false)
                        .setContent("你有代金卷可使用!")
                        .setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                            @Override
                            public void onOkClick() {

                            }

                            @Override
                            public void onCancelClick() {

                            }
                        }).show(((Activity) mContext).getFragmentManager(), "");
            }
        }
    }

    private String addParam() {
        if (TextUtils.isEmpty(AppConstants.H5LoginLink)) return "";
        Map<String, String> params = new HashMap<>();
        params.put("ddt_platform", "dadatu");
        params.put("ddt_uid", AppConstants.uid);
        params.put("ddt_openid", "");
        params.put("ddt_appid", String.valueOf(AppConstants.appId));
        params.put("ddt_sid", "");
        params.put("ddt_time", AppConstants.time);
        params.put("ddt_extInfo", "");
        params.put("ddt_invited_role_id", "");
        params.put("ddt_callback_url", "");
        params.put("ddtType", "1");
        return assignSign(params, AppConstants.H5LoginLink);
    }

    private String assignSign(Map<String, String> params, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        StringBuffer sign = new StringBuffer();
        Object[] keyArr = params.keySet().toArray();
        Arrays.sort(keyArr);
        for (Object key : keyArr) {
            String value = params.get(key) == null ? "" : params.get(key);
            sign.append(value);
            sb.append("&").append(key.toString()).append("=").append(value);
        }
        sign.append(AppConstants.appKey);
        sb.append("&ddt_sign=").append(SecurityUtils.getMD5Str(sign.toString()));
        return sb.toString();
    }

    @Override
    public void onLoadH5Url() {
        Log.e("H5", "onLoadonLoadH5UrlH5Url");
        if (mILoginListener != null) {
            mILoginListener.onLoadH5Url(AppConstants.dologin_h5, addParam());
        }
    }

    @Override
    public void returnLogin(String tip) {
        if (mIKLUserListener != null) {
            mIKLUserListener.returnLogin(tip);
        }
    }

    @Override
    public void payCallback(String billo) {
        if (apiListenerInfo != null) {
            apiListenerInfo.onSuccess(billo);
            AppConfig.coupon_id = "";
        }
    }

    //首次启动时，增加隐私政策提示
    private void showPrivacyPolicyDialog(final Activity activity) {
        KLCommonAffirmDialog.Builder(0, true)
                .setTitle("用户协议及隐私政策保护指引")
                .setSubmitText("同意")
                .setCancelText("不同意")
                .setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                    @Override
                    public void onOkClick() {
                        SharedPreferenceUtil.getInstance(activity).setBoolean(SharedPreferenceUtil.PRIVACY_POLICY, false);
                        KLPermissionUtils.getInstance().requestPermission();
                    }

                    @Override
                    public void onCancelClick() {
                        activity.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).show(activity.getFragmentManager(), "");
    }

    /**
     * 初始化接口
     *
     * @param appid
     * @param appkey
     */
    private InitListener mInitListener;

    @Override
    public void initInterface(final Context context, int appid, String appkey,
                              final InitListener listener) {
        LogUtil.e("activity:" + AppConfig.getActivity());
        try {
            mContext = context;
            // 如果传递过来的为空字符串，就需要自己读配置文件
            final String ddt_ver_id = Utils.getAgent(context);
            AppConstants.appId = appid;
            AppConstants.appKey = appkey;
            AppConstants.ddt_ver_id = ddt_ver_id;
            AppConstants.appVer = Utils.getVersion(context);
            mInitListener = listener;

            KLPermissionUtils.getInstance().requestPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSDK(final Context context, String ddt_ver_id,
                         final InitListener listener) {
        new InitData(context, ddt_ver_id, true, new InitListener() {
            @Override
            public void Success(String msg) {
                Log.i("initInterface", "msg:" + msg);
                QuickLoginUtils.getInstance().init(KLSDK.getInstance().getContext());
                listener.Success(msg);
            }

            @Override
            public void fail(String msg) {
                listener.fail(msg);
                KLCommonAffirmDialog.Builder(1, false)
                        .setContent("初始化失败，请重新进入游戏！")
                        .setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                            @Override
                            public void onOkClick() {
                                System.exit(0);
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        }).show(((Activity) context).getFragmentManager(), "");
            }
        });
    }

    /**
     * 登录接口
     *
     * @param context
     * @param appid
     * @param appkey
     * @param listener
     */
    @Override
    public void login(Activity context, int appid, String appkey, IDdtListener<LoginMessageInfo> listener) {
        Log.e("H5", "login");
        if (TextUtils.isEmpty(AppConstants.Token)) return;
        Log.e("H5", "AppConfig.isLogining");
        try {
            if (AppConfig.isLogining) {
                Log.e("H5", "AppConfig.isLogining");
                return;
            }
            AppConfig.isLogining = true;
            AppConstants.appId = appid;
            AppConstants.appKey = appkey;
            AppConstants.appVer = Utils.getVersion(context);
            mILoginListener = listener;

            //登录共享 end
            if (!TextUtils.isEmpty(AppConstants.small_game_url) && AppConstants.isOpen_smallgame != 0) {
                Log.e("H5", "if");
                if (!AppConstants.is_update) {
                    KLSmallGameActivity.startKLSmallGameActivity(context);
                }
            } else {
                Log.e("H5", "goLogin");
                if (!AppConstants.is_update) {
                    goLogin(context);
                }

            }
        } catch (Exception e) {
            AppConfig.isLogining = false;
        }
    }


    @Override
    public void payment(Activity activity, PaymentInfo payInfo, ApiListenerInfo listener) {
        PayPointReport.getInstance().payPoint(1, "payment");
        try {
            if (TextUtils.isEmpty(payInfo.getSubject())) {
                LogUtil.e("CP请注意，商品名称subject不允许为空！");
            }
            LogUtils.getInstance().superLog(5, "", null, payInfo);
            AppConstants.appId = payInfo.getAppid();
            AppConstants.appKey = payInfo.getAppKey();
            // LogUtil.i("kl "+payInfo.getRoleid());
            // 判断游戏厂家点击登录按钮没有
            // 判断游戏方是否传渠道参数
            payInfo.setAgent("");
            if (payInfo.getAgent().isEmpty()) {
                // 如果传递过来的为空字符串，就需要自己读配置文件
//				final String ver_id = Utils.getAgent(activity);
//                AppConstants.ddt_ver_id = Utils.getAgent(activity);
                payInfo.setAgent(AppConstants.ddt_ver_id);
            }
            apiListenerInfo = listener;

            PayPointReport.getInstance().payPoint(1, GsonUtils.getInstance().toJson(payInfo));
            if (TextUtils.isEmpty(AppConstants.platform_pay)) {
                Log.e("PAYLOG", "非平台支付");
                KLPaymentActivity.startPaymentActivity(activity, payInfo);
            } else {
                Log.e("PAYLOG", "平台支付");
                KLPlatformPayActivity.startKLPlatformPayActivity(activity, payInfo, "");
            }
        } catch (Exception e) {
            PayPointReport.getInstance().payPoint(3, "Exception=" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exit(final Activity activity, final IKLExitListener exitlistener) {
        LogUtil.i("---exit--");
        exitdialog = new Exitdialog(activity, AppConfig.resourceId(activity,
                "kl_MyDialog", "style"), new Exitdialog.Exitdialoglistener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == AppConfig.resourceId(activity, "dialog_exit",
                        "id")) {
					/*WindowManager wm = (WindowManager) activity
							.getApplicationContext()
							.getSystemService(
									activity.getApplicationContext().WINDOW_SERVICE);*/

                    exitLogout(activity, exitlistener);
                    AdManager.getInstance().exit(activity);
                    exitdialog.dismiss();
                } else if (v.getId() == AppConfig.resourceId(activity,
                        "dialog_cancel", "id")) {

                    exitlistener.fail("fail");
                    exitdialog.dismiss();
                }
            }
        });
        exitdialog.show();
    }

    private void exitLogout(final Activity activity, final IKLExitListener exitlistener) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_LOGINOUT, new NoDataParams(), Object.class, new HttpRequestClient.ResultHandler<Object>(activity) {
            @Override
            public void onSuccess(Object o) {
                exitlistener.exitSuccess("exit");
                KLHeartbeatUtils.getInstance().stopHeartbeat(activity);
            }

            @Override
            public void onFailure(Throwable t) {
                exitlistener.exitSuccess("exit");
            }
        });
    }

    @Override
    public void switchAccount() {
        doSwitchAccount(true);
    }

    @Override
    public void doSwitchAccount(boolean isReturn) {
        LogUtil.i("触发切换账号");
        AppConfig.isLogining = false;
        AppConfig.isShow = false;
        AccountManager.logout();
        OnLineTimeRequest.get().cancle();
        JrttTimeRequest.get().cancle();
        FloatUtlis.getInstance().hideFloat();
        FloatUtlis.getInstance().destroyH5Float();
        TimeReport.getInstance(getContext()).cannelReport();
        AdManager.getInstance().setUserUniqueID(mContext, null); //设置用户标识码
        if (isReturn) {
            if (mIKLUserListener != null) {
                mIKLUserListener.onLogout("switchAccount");
            }
        }
    }

    @Override
    public void getCertificateData(Context context, final IDdtListener<ResCertificate> listener) {
        if (TextUtils.isEmpty(AppConstants.uid)) {
            LogUtil.e("请先登录");
            return;
        }

        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_GETCERTIFICATE, new NoDataParams(), ResCertificate.class, new HttpRequestClient.ResultHandler<ResCertificate>(context) {
            @Override
            public void onSuccess(ResCertificate resCertificate) {
                Log.e("SHIMING", "跑onsuccess了");
                listener.onSuccess(resCertificate);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("SHIMING", "跑onFailure了");
                LogUtil.e("getCertificateData error,statusCode is:" + t.getMessage());
                ResCertificate resCertificate = new ResCertificate();
                resCertificate.setMsg(t.getMessage());
                listener.onSuccess(resCertificate);
            }
        });
    }

    @Override
    public void setUserListener(final UserApiListenerInfo listener) {
        mIKLUserListener = new IKLUserListener() {

            @Override
            public void onLogout(Object obj) {
                KLHeartbeatUtils.getInstance().stopHeartbeat(KLSDK.getInstance().getContext());
                listener.onLogout(obj);
                Log.e("MKSsdk", "切换账号~~~");
            }

            @Override
            public void returnLogin(final Object result) {
                AppConfig.isShow = false;
                FloatUtlis.getInstance().hideFloat();
                listener.onLogout(result);
            }
        };
    }

    public void goLogin(Activity context) {
        if (AppConfig.simulatorCanLogin) {
            if (AccountManager.getInstance(context).hasUserHistory()) {
                //有历史登录的记录走这个地方
                KLQuickLoginActivity.startLoginActivity(context);
            } else {
                //用户第一次登录判断该渠道是否开启注册
                if (AppConstants.banres) {
                    KLLoginActivity.startThisActivity(context, 1);
                } else {
                    KLFirstQuickLoginActivity.startThisActivity(context);
                }

            }
        } else {
            CheckSimulator.showExitDialog(context);
        }
    }


    /**
     * 预留接口额外信息
     *
     * @param context 上下文
     *                /@param scene_Id
     *                场景 分别为进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)
     *                /@param roleId
     *                角色id
     *                /@param roleName
     *                角色名
     *                /@param roleLevel
     *                角色等级
     *                /@param zoneId
     *                当前登录的游戏区服id
     *                /@param zoneName
     *                当前游戏区服名称
     *                /@param balance
     *                游戏币余额
     *                /@param Vip
     *                当前用户vip等级
     *                /@param partyName
     *                当前用户所属帮派
     *                /@param roleCTime
     *                单位为秒，创建角色的时间
     *                /@param roleLevelMTime
     *                单位为秒，角色等级变化时间
     */
    @Override
    public void setExtData(final Context context, final RoleData roleData) {
        LogUtils.getInstance().superLog(5, "", null, roleData);
        try {
            if (TextUtils.isEmpty(AppConstants.uid)) {
                LogUtil.e("请先登录");
                return;
            }

            Log.e("qwertyuu", "额外信息" + "场景=" + roleData.getScene_Id() +
                    ", 角色id=" + roleData.getRoleid() +
                    ", 角色名=" + roleData.getRolename() +
                    ", 角色等级=" + roleData.getRolelevel() +
                    ", 服务器id=" + roleData.getServerid() +
                    ", 服务器名=" + roleData.getServername() +
                    ", 游戏币余额=" + roleData.getBalance() +
                    ", 帮派=" + roleData.getPartyname() +
                    ", 创建时间=" + roleData.getRolectime() +
                    ", 升级时间=" + roleData.getRolelevelmtime());

            if (TextUtils.isEmpty(roleData.getServername()) && TextUtils.isEmpty(roleData.getServerid())) {
                roleData.setServername(roleData.getZonename());
                roleData.setServerid(roleData.getZoneid());
            }
            roleData.setServername(TextUtils.isEmpty(roleData.getServername()) ? roleData.getServerid() : roleData.getServername());
            roleData.setRolename(TextUtils.isEmpty(roleData.getRolename()) ? roleData.getRoleid() : roleData.getRolename());
            Log.e("roleScene_Id", "Scene_Id: " + roleData.getScene_Id());
            RoleInfoParams roleInfoParams = new RoleInfoParams(roleData.getServerid(), roleData.getServername(), roleData.getRoleid(), roleData.getRolename(), roleData.getRolelevel(), roleData.getScene_Id(), AppConstants.isLandscape);
            HttpRequestClient.sendPostRequest(ApiConstants.ACTION_ROLEINFO, roleInfoParams, RoleMsg.class, new HttpRequestClient.ResultHandler<RoleMsg>(context) {
                @Override
                public void onSuccess(RoleMsg roleMsg) {
                    Log.e("roleMsg", "is_popup:" + roleMsg.is_popup + "      pic_url:" + roleMsg.pic_url);
                    if (!TextUtils.isEmpty(roleMsg.pic_url)) {
                        if (roleMsg.is_popup) {
                            new PopupDialogRole(context, roleMsg.pic_url).show();
                        }
                    }

                }

                @Override
                public void onResponse(BaseBean baseBean) {
                    if (baseBean.isResult()) {
                        Log.e("http_ACTION_ROLEINFO", "===========aaaaaa" + baseBean.getMsg());
                    } else {
                        LogUtil.e("setRoleinfo error:" + baseBean.getMsg());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtil.e("setRoleinfo error" + t.getMessage());
                }
            });
            JrttTimeRequest.get().jjrtTime((Activity) mContext, AppConstants.uid, roleData.getRolelevel(), roleData.getRoleid());
        } catch (Exception e) {
            LogUtil.e("ExtData error, " + e.getMessage());
        }
    }

    @Override
    public void applicationInit(Context context) {
//		AdUtils.getInstance().adUtilsInit(context);
        //广点通sdk初始化
//		GdtAdUtils.getInstance().gdtInit(context);
        Log.e("AAAAAAAA", "不在application调用");
        AppConfig.mApplication = new KLApplication();
        AdManager.getInstance().init((Application) context);


        int sdkVersion = context.getApplicationInfo().targetSdkVersion;
        LogUtil.i("targetSdkVer=" + sdkVersion);
    }

    @Override
    public void onStart(Activity activity) {
        LogUtil.i("onStart:" + activity.getLocalClassName());
        checkMainThread("onStart");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onStart=============");
    }

    @Override
    public void onCreate(Activity activity) {
        AppConfig.activity = activity;
        KLPermissionUtils.getInstance().setAllRequestPermissions(this);
        LogUtil.i("onCreate:" + activity.getLocalClassName());
        Log.e("onCreate:", "onCreate:" + activity.getLocalClassName());
        LogUtils.getInstance().superLog(5, "", null, "DDT_onCreate=============");
        checkMainThread("onCreate");
        AdManager.getInstance().activityCreate(activity);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AppConstants.isLandscape = 1;
            Log.e("islandscape======", "横竖屏情况：" + AppConstants.isLandscape);
        } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            AppConstants.isLandscape = 0;
            Log.e("islandscape======", "横竖屏情况：" + AppConstants.isLandscape);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getOaid();
            }
        });
    }

    @Override
    public void onStop(Activity activity) {
        LogUtil.i("onStop:" + activity.getLocalClassName());
        checkMainThread("onStop");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onStop=============");
        AdManager.getInstance().activityStop(activity);
    }


    @Override
    public void onDestroy(Activity activity) {
        LogUtil.i("onDestroy:" + activity.getLocalClassName());
        checkMainThread("onDestroy");
        KLHeartbeatUtils.getInstance().stopHeartbeat(activity);
        LogUtils.getInstance().superLog(5, "", null, "DDT_onDestroy=============");
        AdManager.getInstance().setUserUniqueID(activity, null);
        AdManager.getInstance().activityDestory(activity);

    }

    @Override
    public void onResume(final Activity activity) {
        LogUtil.i("onResume:" + activity.getLocalClassName());
        checkMainThread("onResume");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onResume=============");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (AppConfig.isShow) {
                    FloatUtlis.getInstance().showFloat();
                }
            }
        });
        AdManager.getInstance().activityResume(activity);
    }


    @Override
    public void onPause(Activity activity) {
        LogUtil.i("onPause:" + activity.getLocalClassName());
        checkMainThread("onPause");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onPause=============");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FloatUtlis.getInstance().hideFloat();
            }
        });
        AdManager.getInstance().activityPause(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        LogUtil.i("onRestart:" + activity.getLocalClassName());
        LogUtils.getInstance().superLog(5, "", null, "DDT_onRestart=============");
        checkMainThread("onRestart");
    }

    @Override
    public void onNewIntent(Intent intent) {
        LogUtil.i("onNewIntent");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onNewIntent=============");
        checkMainThread("onNewIntent");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        LogUtil.i("onActivityResult");
        LogUtils.getInstance().superLog(5, "", null, "DDT_onActivityResult=============");
        checkMainThread("onActivityResult");
    }

    // 判断是否在主线程
    private boolean checkMainThread(String tag) {
        Thread mainThread = Looper.getMainLooper().getThread();
        Thread cutThread = Thread.currentThread();
        boolean is = mainThread == cutThread;
        if (!is) {
            LogUtil.e("***CP请注意***当前方法" + tag + "调用不在主线程内，建议在主线程，不然会出现线程安全问题！！！");
        }
        return is;
    }

    // 是否打开日志，要在Application的super.onCreate()之前调用，才能输出所有信息
    @Override
    public void openLog(Context context, boolean isopen) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            boolean isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

            if (isDebug) {
                LogUtil.openLog(isopen);
                Log.e(LogUtil.TAG, "当前为DEBUG模式，日志状态：" + LogUtil.isOpen());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAgent(Context context) {
        return Utils.getAgent(context, true);
    }

    public void getOaid() {
        KlOaidHelper.getInstance().getOaid(AppConfig.mApplication, new AppIdsUpdater() {
            @Override
            public void onIdsAvalid(String oaid) {
                AppConstants.Oaid = oaid;
                LogUtil.i("oaid callback:" + oaid);
            }
        }, getClass().getClassLoader());
    }


    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void allPermissionAgree() {
        allPermissionAgree = true;
        LogUtil.e("allPermissionAgree");
        initSDK(mContext, AppConstants.ddt_ver_id, mInitListener);
    }
}
