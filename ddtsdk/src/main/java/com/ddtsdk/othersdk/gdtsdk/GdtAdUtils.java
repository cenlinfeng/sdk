package com.ddtsdk.othersdk.gdtsdk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLApplication;
import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogTag;

import com.ddtsdk.log.LogUtils;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.othersdk.manager.AdInterface;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.bean.BaseState;
import com.ddtsdk.utils.PermissionUtil;
import com.ddtsdk.utils.Utils;
import com.qq.gdt.action.ActionType;
import com.qq.gdt.action.GDTAction;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by cai on 2019/10/10.
 *
 * 暂未启用
 */
public class GdtAdUtils extends Instrumentation implements AdInterface {
    private static GdtAdUtils gdtAdUtils;

    public static GdtAdUtils getInstance(){
        if (gdtAdUtils == null){
            gdtAdUtils = new GdtAdUtils();
        }
        return gdtAdUtils;
    }

    private boolean permissionAllow = false;  //检查是否开启READ_PHONE_STATE权限
    @Override
    public void adReport(int event, Context context, Bundle bundle) {
        switch (event){
            case EventFlag.REGISTER:
                registerForGdt(context, bundle);
                break;
            case EventFlag.PAY:
                purchaseForGdt(context, bundle);
                break;
            case EventFlag.APPLICATIONCREATE:
                gdtInit(context);
                break;
            case EventFlag.USETUNIQUE:
                setUniqueId(bundle);
                break;
            case EventFlag.ACTIVITYCREATE:
//                onResumeForGdt(context);
                break;
            case EventFlag.ACTIVITYDESTORY:
                break;
            case EventFlag.ACTIVATION:
//                activation(context);
                break;
        }
    }

    public void cApp(Application app){
        callApplicationOnCreate(app);
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        super.callApplicationOnCreate(app);
    }

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//        gdtInit((Application)KLSDK.getInstance().getContext().getApplicationContext());
//    }

    /**
     * 广点通初始化，必须在application中进行调用
     * @param context
     */
    public void gdtInit(final Context context) {
        Log.e("广点通广告", "初始化===");
            // 第一个参数是Context上下文，第二个参数是您在DMP上获得的行为数据源ID，第三个参数是您在DMP上获得AppSecretKey
//        ((Activity)KLSDK.getInstance().getContext()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                GDTAction.init(context, Utils.getGDTUserActionSetID(context), Utils.getGDTAppSecretKey(context), Utils.getAgent(context));
                LogUtils.getInstance().superLog(5,LogTag.ADTAG,null,"广点通广告初始化=====,gdtActionSetID="
                        +Utils.getGDTUserActionSetID(context)+" ,gdtSecretKey="+Utils.getGDTAppSecretKey(context));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activation(context);
                    }
                }, 200);
//            }
//        });
    }

    /**
     * 上报启动行为  在Activity中的onCreate上报，每次启动应用只上报一次
     *
     * @param context
     */
    public void onResumeForGdt(Context context) {
        LogUtils.getInstance().superLog(5, LogTag.ADTAG, null, "onResumeForGdt");
//        if (!(TextUtils.isEmpty(AppConstants.init_type) || AppConstants.init_type.equals("decrement"))){
            LogUtils.getInstance().superLog(5, LogTag.ADTAG, null, "decrement");
            GDTAction.logAction(ActionType.START_APP);
//        }
    }

    /**
     * 上报激活  只上报一次
     *
     * @param context
     */
    public void activation(Context context) {
        LogUtils.getInstance().superLog(5, LogTag.ADTAG, null, "activation");
        // 传入的actionType参数必须是ActionType.START_APP
//        permissionAllow = PermissionUtil.hasPermission(context, Manifest.permission.READ_PHONE_STATE);
//        if (permissionAllow) {
        GDTAction.logAction(ActionType.START_APP);
//        }
    }

    public void setUniqueId(final Bundle bundle){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                GDTAction.setUserUniqueId(bundle.getString(EventFlag.UID));
                LogUtils.getInstance().superLog(5,LogTag.ADTAG,null,"广点通setUniqueId上报======="+bundle.getString(EventFlag.UID));
            }
        }, 300);
    }

    /**
     * 上报充值行为
     */
    public void purchaseForGdt(Context context, Bundle bundle) {
        LogUtils.getInstance().superLog(5, LogTag.ADTAG, null, "purchaseForGdt");
        JSONObject actionParam = new JSONObject();
        try {
            actionParam.put("value", (Integer.parseInt(bundle.getString(EventFlag.AMOUNT)) * 100) + "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("purchaseForGdt", "msg=" + e.getMessage());
        }
        GDTAction.logAction(ActionType.PURCHASE, actionParam);
        AdManager.getInstance().logPayReport(context, bundle);
        LogUtils.getInstance().superLog(5,"",null,"广点通支付上报======="+actionParam);
    }

    /**
     * 上报注册行为，添加OUTER_ACTION_ID字段，去重处理，字段的value传入imei
     */
    public void registerForGdt(final Context context, Bundle bundle) {
        LogUtils.getInstance().superLog(5, LogTag.ADTAG, null, "registerForGdt");
        GDTAction.logAction(ActionType.REGISTER);
        AdManager.getInstance().logRegisterReport(context, "2", "success");
        LogUtils.getInstance().superLog(5,LogTag.ADTAG,null,"广点通注册上报");
    }
}
