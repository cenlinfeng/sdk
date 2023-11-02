package com.ddtsdk.othersdk.ucsdk;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.othersdk.manager.AdInterface;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.utils.Utils;
import com.gism.sdk.GismConfig;
import com.gism.sdk.GismEventBuilder;
import com.gism.sdk.GismSDK;


/**
 * Created by cai on 2020/4/13.
 */
public class UCSDKUtils implements AdInterface {

    public static UCSDKUtils getInstance() {
        return SingleObj.mInstance;
    }

    @Override
    public void adReport(int event, Context context, Bundle bundle) {
        switch (event) {
            case EventFlag.REGISTER:
                registerForUC(context, bundle.getString(EventFlag.USERTYPE));
                break;
            case EventFlag.PAY:
                purchaseForUC(context, bundle);
                break;
            case EventFlag.APPLICATIONCREATE:
                ucInit(context);
                break;
            case EventFlag.USETUNIQUE:
                break;
            case EventFlag.ACTIVITYCREATE:
                onCreateForUC();
                break;
            case EventFlag.ACTIVITYDESTORY:
                break;
            case EventFlag.EXIT:
                exit();
                break;
        }
    }

    /**
     * UC初始化，必须在application中进行调用
     *
     * @param context
     */
    public void ucInit(Context context) {
        // 第一个参数是Context上下文，第二个参数是您在DMP上获得的行为数据源ID，第三个参数是您在DMP上获得AppSecretKey
        GismSDK.init(GismConfig.newBuilder((Application) context)
                .appID(Utils.getUCAppId(context))
                .appName(Utils.getUCAppName(context))
                .appChannel(Utils.getAgent(context))
                .build());

        AppConfig.adSdkInitSuccess = true;
//        if (AppConfig.sdkInitSuccess){
//            PayManager.getInstance().cacheOrderid();
//        }
        Log.d("UC广告", "初始化===id=" + Utils.getUCAppId(context) + " Name=" + Utils.getUCAppName(context));
        LogUtils.getInstance().superLog(5,"",null,"UC广告初始化===id="+ Utils.getUCAppId(context)
                + " ,Name=" + Utils.getUCAppName(context));
//            GismSDK.debug();
    }

    /**
     * 启动游戏回调   必选。每次APP启动时必须回调，否则会漏报激活和
     * 启动，导致汇川平台数据偏少。
     */
    public void onCreateForUC() {
        Log.d("UC广告", "onCreate===");
        GismSDK.onLaunchApp();
        LogUtils.getInstance().superLog(5,"",null,"UC广告onCreate===");
    }


    /**
     * 上报充值行为
     * <p>
     * isPaySuccess 支付是否成功 boolean 必选，否则无法上报
     * payAmount 支付金额（单位：元，大于0） float 必选，否则无法上报
     */
    public void purchaseForUC(Context context, Bundle bundle) {
        GismSDK.onEvent(GismEventBuilder.onPayEvent().isPaySuccess(true)
                .payAmount(Float.parseFloat(bundle.getString(EventFlag.AMOUNT)))
                .build());
        AdManager.getInstance().logPayReport(context, bundle);
        Log.d("UC广告", "pay=" + bundle.getString(EventFlag.AMOUNT));
        LogUtils.getInstance().superLog(5,"",null,"UC广告pay=" + bundle.getString(EventFlag.AMOUNT));
    }

    /**
     * 上报注册行为
     * <p>
     * isRegisterSuccess 注册是否成功 boolean 必选，否则无法上报
     * registerType 注册方式，可自定义，如mobile、weixin String 必选，否则无法上报
     */
    public void registerForUC(Context context, String type_reg) {
        Log.d("UC广告", "注册===" + type_reg);
        GismSDK.onEvent(GismEventBuilder.onRegisterEvent().registerType(type_reg)
                .isRegisterSuccess(true)
                .build());
        AdManager.getInstance().logRegisterReport(context, "2", "success");
        LogUtils.getInstance().superLog(5,"",null,"UC广告注册===" + type_reg);
    }

    public void exit() {
        Log.d("UC广告", "退出===");
        GismSDK.onExitApp();
        LogUtils.getInstance().superLog(5,"",null,"UC广告退出===" );
    }

    private static class SingleObj {
        private final static UCSDKUtils mInstance = new UCSDKUtils();
    }
}
