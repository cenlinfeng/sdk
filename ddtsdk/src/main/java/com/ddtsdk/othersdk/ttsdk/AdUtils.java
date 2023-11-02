package com.ddtsdk.othersdk.ttsdk;

/**
 * Created by a5706 on 2018/4/21.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bytedance.applog.AppLog;
import com.bytedance.applog.InitConfig;
import com.bytedance.applog.game.GameReportHelper;
import com.bytedance.applog.util.UriConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.othersdk.manager.AdInterface;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.log.LogReport;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;

import org.json.JSONObject;

/**
 * 第三方功能性sdk
 * <p>
 * 1、头条数据报送sdk接入。
 */

public class AdUtils implements AdInterface {

    @Override
    public void adReport(int event, Context context, Bundle bundle) {
        switch (event) {
            case EventFlag.REGISTER:
                setUserRegisterType(context, bundle.getString(EventFlag.USERTYPE));
                break;
            case EventFlag.PAY:
                LogUtil.e("头条 pay");
                setPurchase(context, bundle);
                break;
            case EventFlag.APPLICATIONCREATE:
                break;
            case EventFlag.USETUNIQUE:
                setUserUniqueID(bundle.getString(EventFlag.UID));
                break;
            case EventFlag.ACTIVITYCREATE:
                LogUtil.e("头条 init");
                adUtilsInit(context);
                break;
            case EventFlag.ACTIVITYRESUME:
                LogUtil.e("头条 resume");
                onResume(context);
                break;
            case EventFlag.ACTIVITYPAUSE:
                LogUtil.e("头条 pause");
                onPause(context);
                break;
            case EventFlag.ACTIVITYDESTORY:
                break;
        }
    }

    //默认初始化
    public void adUtilsInit(Context context) {
        Log.e("头条广告", "初始化===1");
        //param
        String channel = AppConstants.ddt_ver_id;
        int appId = Utils.getOtherSdkAid(context);
        String gameName = Utils.getToutiaoGameName(context);

        /* 初始化开始 */
        // appid和渠道，appid须保证与广告后台申请记录一致，渠道可自定义，如有多个马甲包建议设置渠道号唯一标识一个马甲包。
        final InitConfig initConfig = new InitConfig(appId + "", channel);

        /*
         域名默认国内: DEFAULT, 新加坡:SINGAPORE, 美东:AMERICA
         注意：国内外不同vendor服务注册的did不一样。由DEFAULT切换到SINGAPORE或者AMERICA，会发生变化，
         切回来也会发生变化。因此vendor的切换一定要慎重，随意切换导致用户新增和统计的问题，需要自行评估。
         */
        initConfig.setUriConfig(UriConstants.DEFAULT);

        // 是否在控制台输出日志，可用于观察用户行为日志上报情况，建议仅在调试时使用，release版本请设置为false ！
        initConfig.setAppName(gameName);
        initConfig.setAbEnable(true);
        initConfig.setAutoTrackEnabled(true); // 全埋点开关，true开启，false关闭

        // 游戏模式，YES会开始 playSession 上报，每隔一分钟上报心跳日志
        initConfig.setEnablePlay(true);
        initConfig.setAutoStart(true);
        AppLog.init(context, initConfig);
//        AppLog.setEncryptAndCompress(true);
        /* 初始化结束 */
        Log.e("头条广告", "初始化===" + "appId=" + appId + " appkey=" + gameName + " channel=" + channel);
        LogUtils.getInstance().superLog(5, "", null, "头条广告初始化===" + "appId=" + appId
                + " appkey=" + gameName + " channel=" + channel);
    }

    /**
     * 【必接！】
     * <p>
     * 注册/登录渠道、访问账号
     *
     * @param type_reg 注册方式
     */
    public void setUserRegisterType(Context context, String type_reg) {
        LogUtil.e("type_reg" + type_reg);
        GameReportHelper.onEventRegister(type_reg, true);
        AdManager.getInstance().logRegisterReport(context, "2", "success");
        Log.e("头条广告", "注册===" + type_reg);
        LogUtils.getInstance().superLog(5, "", null, "头条广告注册===" + type_reg);
    }

    /**
     * 设置appId,gameName,channel
     * <p>
     * 此方法 内部调用 三个get
     */
    public void setAppInfo(Context context) {
        int appId = Utils.getOtherSdkAid(context);
        String gameName = Utils.getToutiaoGameName(context);
        String channel = Utils.getAgent(context);
        if (TextUtils.isEmpty(channel)) {
            channel = "unknow";
        }
        setAppInfo(context, appId, gameName, channel);
    }

    /**
     * 设置appId,gameName,channel
     * <p>
     * 此方法 内部调用 两个get
     */
    public void setAppInfo(Context context, String channel) {
        int appId = Utils.getOtherSdkAid(context);
        String gameName = Utils.getToutiaoGameName(context);
        if (TextUtils.isEmpty(channel)) {
            channel = "unknow";
        }
        setAppInfo(context, appId, gameName, channel);
    }


    /**
     * 设置appId,gameName,channel
     */
    public void setAppInfo(@NonNull Context context, @NonNull int id, @NonNull String name, @NonNull String channel) {
        AppInfo.setAppInfo(context, id, name, channel);
    }

    /**
     * 上报自定义行为数据
     */
    public void onEventV3(JSONObject obj) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("group_id", 24432423);
//            jsonObject.put("activityName", "MainActivity");
//            jsonObject.put("from", "test");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        AppLogNewUtils.onEventV3("test", jsonObject);
    }

    /**
     * 由服务端上传数据
     * 【必接！--可由移动端or服务端接入】
     * 上报商业行为数据
     *
     * @param content_type    内容类型
     * @param content_name    商品/内容名
     * @param content_id      商品ID/内容ID
     * @param content_num     商品数量
     * @param payment_channel 支付渠道名 如支付宝、微信等
     * @param currency        真实货币类型
     * @param is_success      是否成功（必须上传）
     * @param currency_amount 本次支付的真实货币的金额（必须上传，单位：元）
     */
    public void setPurchase(Context context, Bundle bundle) {
        //内置事件“⽀付”，属性：商品类型，商品名称，商品ID，商品数量，⽀付渠道，币种，是否成功（必传），⾦额（必传）
        Log.i("TAG", "setPurchase: " + AppConstants.mPayParam.getPaychar());
        String content_type = AppConstants.mPayParam.getSubject();
        String content_name = AppConstants.mPayParam.getSubject();
        String content_id = AppConstants.mPayParam.getBillno();
        String payment_channel = TextUtils.equals(AppConstants.mPayParam.getPaychar(), "alipaywap")
                ? "支付宝" : "微信";
        String currency_amount = AppConstants.mPayParam.getAmount();
        boolean isSuccess = bundle.getString(EventFlag.STATUS) == "success" ? true : false;
        GameReportHelper.onEventPurchase(content_type, content_name, content_id, 1, payment_channel,
                "¥", isSuccess, Integer.parseInt(currency_amount));
    }


    /**
     * 设置用户唯一标识
     *
     * @param id 唯一标识码
     */
    public void setUserUniqueID(final String id) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AppLog.setUserUniqueID(id);
                LogUtils.getInstance().superLog(5, "", null, "头条广告  ", "唯一用户===" + id);
            }
        }, 1000);
    }

    /**
     * 时长统计
     */
    public void onResume(@NonNull Context context) {
        LogUtil.d("头条onResume --上报");
//        AppLog.onResume(context);
    }

    /**
     * 时长统计
     */
    public void onPause(@NonNull Context context) {
        LogUtil.d("onPause --上报");
//        AppLog.onPause(context);
    }

    /**
     * 头条时长统计，每分钟上报一次
     */
    public void storageTime(Context context, String level) {

    }
}
