package com.ddtsdk.othersdk.manager;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.othersdk.manager.log.LogReport;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;


public class AdManager {
    private static AdManager adManager;
    private static AdInterface adInterface = null;
    private String channel = "";

    private AdManager() {
    }

    public static AdManager getInstance() {
        if (adManager == null) {
            adManager = new AdManager();
        }
        return adManager;
    }

    /**
     * AdManager 初始化，放在Applicantion的onCreate方法中
     */
    public void init(Application app) {
        LogUtils.getInstance().superLog(5, "", null, "Applicantion的onCreate方法中");
        Log.e("gsadasd", channel);
        channel = Utils.getAgent(app);

        adInterface = AdFactory.createAd(channel);
        if (adInterface != null) {
            adInterface.adReport(EventFlag.APPLICATIONCREATE, app, null);
            AppConfig.adSdkInitSuccess = true;
        }
    }

    /**
     * //     * @param 激活
     */
    public void activation(Context context) {
        if (adInterface != null && !AppConstants.init_type.equals("decrement")) {
            adInterface.adReport(EventFlag.ACTIVATION, context, null);
        }
    }

    /**
     * //     * @param registerType 注册方式， 手机，游客，账号
     */
    public void register(Context context) {
        if (adInterface != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EventFlag.USERTYPE, AppConstants.regType);
//            adInterface.adReport(EventFlag.REGISTER, context, bundle);
            abnormalRegister(context, bundle);
        }
    }

    private void abnormalRegister(final Context context, final Bundle bundle) {
        if (!abnormalAdReport()) {
            adInterface.adReport(EventFlag.REGISTER, context, bundle);
        } else {
            if (AppConstants.control_status == 0) {
                adInterface.adReport(EventFlag.REGISTER, context, bundle);
            } else if (AppConstants.control_type.equals("increment")) {
                adInterface.adReport(EventFlag.REGISTER, context, bundle);
                bundle.putString(EventFlag.USERTYPE, "userReg");
                int addCount = Integer.parseInt(AppConstants.threshold_value);
                for (int i = 0; i < addCount; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000 * 10);
                                adInterface.adReport(EventFlag.REGISTER, context, bundle);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (AppConstants.control_type.equals("decrement")) {
                AdManager.getInstance().logRegisterReport(context, "2", "error");
            }
        }
    }

    public void pay(Context context, Bundle bundle) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.PAY, context, bundle);
        }
    }

    public void activityCreate(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.ACTIVITYCREATE, context, null);
        }
    }

    public void activityResume(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.ACTIVITYRESUME, context, null);
        }
    }

    public void activityPause(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.ACTIVITYPAUSE, context, null);
        }
    }

    public void activityStop(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.ACTIVITYSTOP, context, null);
        }
    }

    public void activityDestory(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.ACTIVITYDESTORY, context, null);
        }
    }

    public void setUserUniqueID(Context context, String id) {
        if (adInterface != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EventFlag.UID, id);
            adInterface.adReport(EventFlag.USETUNIQUE, context, bundle);
        }
    }

    public void exit(Context context) {
        if (adInterface != null) {
            adInterface.adReport(EventFlag.EXIT, context, null);
        }
    }

    /**
     * 不开启付费上报的渠道,需要手动设置
     *
     * @return
     */
    public boolean noOpenPayReport() {
        return (adInterface == null) || (AppConfig.adType == 1);
    }

    /**
     * 开启增缩量上报的渠道,需要手动设置
     * 广点通
     *
     * @return
     */
    public boolean abnormalAdReport() {
        return AppConfig.adType == 1 || AppConfig.adType == 0;
    }

    public void logRegisterReport(Context context, String msg, String status) {
        if (adInterface != null) {
            LogReport.getInstance().logRegisterReport(context, msg, status);
        }
    }

    public void logPayReport(Context context, Bundle bundle) {
        if (adInterface != null && !noOpenPayReport()) {
            LogReport.getInstance().logPayReport(context, bundle);
        }
    }
}
