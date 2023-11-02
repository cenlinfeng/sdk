package com.ddtsdk.othersdk.manager.log;

import android.content.Context;
import android.os.Bundle;

import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.params.BaseParam;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.LogUtil;

import com.ddtsdk.othersdk.manager.bean.BaseState;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 广告日志上报接口
 */
public class LogReport {

    private static LogReport logr = null;
    DeviceInfo deviceInfo = null;

    /***
     *
     * @param context
     * @param msg
     * @param status
     *  map   data字段包含的日志数据 内容格式key1=value1&key2=value2 的base64字符串
     */
    public void logRegisterReport(Context context, String msg, String status) {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo(context);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userName", AppConstants.userName);
        map.put("visitor", "fastReg".equals(AppConstants.regType) ? "0" : "1");
        map.put("appid", AppConstants.appId);
        map.put("channel", AppConstants.ddt_ver_id);
        map.put("imei", deviceInfo.getImei());
        map.put("oaid", AppConstants.Oaid);
        map.put("time", AppConstants.time);
        String data = mapToStr(map);

        BaseParam baseParam = new BaseParam();
        baseParam.setVer(AppConstants.ddt_ver_id);
        baseParam.setStatus(status);
        baseParam.setAppid(AppConstants.appId);
        baseParam.setData(data);
        baseParam.setMsg(msg);
        baseParam.setStype("register");
        baseParam.setType("android");
        reportLogToServer(context, baseParam);
    }

    /***
     *
     * @param context
     * @param bundle  请求后台所需参数的集合
     *  map   data字段包含的日志数据 内容格式key1=value1&key2=value2 的base64字符串
     */
    public void logPayReport(Context context, Bundle bundle) {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo(context);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userName", AppConstants.userName);
        map.put("orderid", bundle.getString(EventFlag.ORDERID));
        map.put("appid", AppConstants.appId);
        map.put("channel", AppConstants.ddt_ver_id);
        map.put("imei", deviceInfo.getImei());
        map.put("oaid", AppConstants.Oaid);
        map.put("amount", bundle.getString(EventFlag.AMOUNT));
        map.put("extra", bundle.getString(EventFlag.EXTRA));

        String data = mapToStr(map);

        BaseParam baseParam = new BaseParam();
        baseParam.setVer(AppConstants.ddt_ver_id);
        baseParam.setStatus(bundle.getString(EventFlag.STATUS));
        baseParam.setAppid(AppConstants.appId);
        baseParam.setData(data);
        baseParam.setMsg(bundle.getString(EventFlag.MSG));
        baseParam.setStype("pay");
        baseParam.setType("android");
        reportLogToServer(context, baseParam);
    }

    /**
     * @param map HashMap集合，  将map集合进行排序，然后转成base64字符串
     * @return
     */
    public String mapToStr(HashMap<String, Object> map) {

        if (map == null) return "";
        Object[] keylist = map.keySet().toArray();
        Arrays.sort(keylist);
        StringBuilder stringBuilder = new StringBuilder();
        String sep = "&";
        for (int i = 0; i < keylist.length; i++) {
            if (i == keylist.length - 1) sep = "";
            stringBuilder.append(keylist[i] + "=" + map.get(keylist[i]) + sep);
        }
        return Base64.encode(stringBuilder.toString().getBytes());
    }

    /**
     * 将整理好的参数发送到服务器
     *
     * @param context
     * @param params  上报服务端所需参数
     */
    public void reportLogToServer(Context context, BaseParam params) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_REPORTLOG, params, Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object basestate) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public static LogReport getInstance() {
        if (logr == null) {
            logr = new LogReport();
        }
        return logr;
    }

    private LogReport() {
    }
}
