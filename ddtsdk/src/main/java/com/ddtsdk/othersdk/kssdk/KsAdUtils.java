package com.ddtsdk.othersdk.kssdk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;

import com.ddtsdk.othersdk.manager.AdInterface;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;
import com.kwai.monitor.log.OAIDProxy;
import com.kwai.monitor.log.TurboAgent;
import com.kwai.monitor.log.TurboConfig;

public class KsAdUtils implements AdInterface {
    @Override
    public void adReport(int event, Context context, Bundle bundle) {
        switch (event){
            case EventFlag.REGISTER:
                register(context);
                break;
            case EventFlag.PAY:
                pay(context, bundle);
                break;
            case EventFlag.APPLICATIONCREATE:
                break;
            case EventFlag.USETUNIQUE:
                break;
            case EventFlag.ACTIVITYRESUME:
                onResume(context);
                break;
            case EventFlag.ACTIVITYCREATE:
                init(context);
                break;
            case EventFlag.ACTIVITYPAUSE:
                onPause(context);
                break;
        }
    }

    public void init(Context context){
        Log.e("init","sadasdsadsa");
        TurboAgent.init(TurboConfig.TurboConfigBuilder.create(context)
                .setAppId(Utils.getKSAppId(context))
                .setAppName(Utils.getKSAppName(context))
                .setAppChannel(Utils.getAgent(context))
                .setEnableDebug(false)
                .setOAIDProxy(new OAIDProxy() {
                    @Override
                    public String getOAID() {
                        LogUtil.i("oaid" + AppConstants.Oaid);
                        return AppConstants.Oaid;
                    }
                })
                .build());
        AppConfig.adSdkInitSuccess = true;
        Log.d("kssdkUtils", "初始化init=====,AppId:"+Utils.getKSAppId(context)+"  ,AppName:"+Utils.getKSAppName(context));
        LogUtils.getInstance().superLog(5,"",null,"初始化init=====,AppId:"+
                Utils.getKSAppId(context)+"  ,AppName:"+Utils.getKSAppName(context));

    }

//    public void appActive(){
//        TurboAgent.onAppActive();
//    }

    public void onResume(Context context){
        Log.d("kssdkUtils", "onResume=====");
        TurboAgent.onPageResume((Activity) context);
        LogUtils.getInstance().superLog(5,"",null,"kssdkUtils onResume=====");
    }

    public void onPause(Context context){
        Log.d("kssdkUtils", "onPause=====");
        TurboAgent.onPagePause((Activity) context);
        LogUtils.getInstance().superLog(5,"",null,"kssdkUtils onPause=====");
    }

    public void register(Context context){
        Log.d("kssdkUtils", "register==package=" + context.getPackageName());
        TurboAgent.onRegister();
        AdManager.getInstance().logRegisterReport(context, "2", "success");
        LogUtils.getInstance().superLog(5,"",null,"kssdkUtils register==package="+context.getPackageName());
    }

    public void pay(Context context, Bundle bundle){
        TurboAgent.onPay(Double.parseDouble(bundle.getString(EventFlag.AMOUNT)));
        AdManager.getInstance().logPayReport(context, bundle);
        Log.d("kssdkUtils", "pay=" + bundle.getString(EventFlag.AMOUNT));
        LogUtils.getInstance().superLog(5,"",null,"kssdkUtils pay="+bundle.getString(EventFlag.AMOUNT));
    }
}
