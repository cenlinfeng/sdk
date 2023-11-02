package com.ddtsdk.othersdk.aqysdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.othersdk.manager.AdInterface;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;

import com.ddtsdk.utils.Utils;
import com.iqiyi.qilin.trans.QiLinTrans;
import com.iqiyi.qilin.trans.TransParam;
import com.iqiyi.qilin.trans.TransType;

import org.json.JSONObject;

public class AqyAdUtils implements AdInterface {
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
                init(context);
                break;
            case EventFlag.USETUNIQUE:
                break;
            case EventFlag.ACTIVITYRESUME:
                onResume();
                break;
            case EventFlag.ACTIVITYDESTORY:
                onDestroy();
                break;
            case EventFlag.ACTIVITYCREATE:
                break;
        }
    }

    public void init(Context context){
        QiLinTrans.setDebug(TransParam.LogLevel.LOG_DEBUG, false, "");
        QiLinTrans.init(context, Utils.getAQYAppId(context), Utils.getAgent(context), AppConstants.Oaid);
        LogUtils.getInstance().superLog(5,"",null,"爱奇艺初始化，AQYAppId："
                +Utils.getAQYAppId(context)+" ,Oaid:"+ AppConstants.Oaid);
        AppConfig.adSdkInitSuccess = true;
//        if (AppConfig.sdkInitSuccess){
//            PayManager.getInstance().cacheOrderid();
//        }
    }

    public void register(Context context){
        QiLinTrans.uploadTrans(TransType.QL_REGISTER);
        AdManager.getInstance().logRegisterReport(context, "2", "success");
        LogUtils.getInstance().superLog(5,"",null,"爱奇艺注册");

    }

    public void pay(Context context, Bundle bundle){
        try {
            JSONObject transParams = new JSONObject();
            transParams.put(TransParam.MONEY, bundle.getString(EventFlag.AMOUNT));
            QiLinTrans.uploadTrans(TransType.QL_PURCHASE, transParams);
            AdManager.getInstance().logPayReport(context, bundle);
            Log.d("aqysdkUtils", "pay=" + bundle.getString(EventFlag.AMOUNT));
            LogUtils.getInstance().superLog(5,"",null,"爱奇艺pay="+ bundle.getString(EventFlag.AMOUNT));
        } catch (Exception ignored) {
        }
    }

//    public void event(){
//        try {
//            JSONObject transParams = new JSONObject();
//            transParams.put(TransParam.APPLY_TYPE, TransParam.ApplyType.CREDIT);
//            transParams.put(TransParam.MONEY, 100);
//            QiLinTrans.uploadTrans(TransType.QL_APPLY, transParams);
//        } catch (Exception ignored) {
//        }
//    }

    public void onResume(){
        QiLinTrans.onResume();
        LogUtils.getInstance().superLog(5,"",null,"爱奇艺onResume");
    }

    public void onDestroy(){
        QiLinTrans.onDestroy();
        LogUtils.getInstance().superLog(5,"",null,"爱奇艺onDestroy");
    }
}
