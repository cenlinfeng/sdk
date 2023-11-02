package com.ddtsdk.common.base;

import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.utils.DataUtils;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.utils.RSAEncrypt;
import com.ddtsdk.utils.SecurityUtils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CZG on 2020/4/13.
 */
public class BaseParams<T> {

    private HashMap<String,String> params = new HashMap<>();

    private T data;

    public BaseParams(DeviceInfo deviceInfo, T data) {
        this.data = data;

        params.put("appid", String.valueOf(AppConstants.appId));
        params.put("ver", AppConstants.ddt_ver_id);
        params.put("device", AppConstants.DEVICE);
        params.put("udid", deviceInfo.getUuid());
        params.put("imei", deviceInfo.getImei());
        params.put("requestid", String.valueOf(System.currentTimeMillis()));
        params.put("sdkversion", AppConstants.appVer);
        params.put("pkversion", deviceInfo.getAppVersion());
        params.put("oaid", AppConstants.Oaid);
        params.put("sessid", AppConstants.Sessid);
        params.put("token", AppConstants.Token);
        params.put("sdkflag", AppConstants.sdkflag);  //  sdk标志位， 通过这个标志位，服务器可以知道当前sdk是否是最新SDK，便于新旧版本的兼容
        params.put("uid", AppConstants.uid);
        params.put("issimulator","false");
//        params.put("issimulator",deviceInfo.getIssimulator());
    }

    public HashMap<String, String> getParams(){
        if (data != null){
            HashMap<String, String> dataMap = GsonUtils.createObj(GsonUtils.getInstance().toJson(data));
            for (HashMap.Entry<String, String> entry : dataMap.entrySet()) {
                //临时处理package关键字问题,后续需要更换字段名
                if (TextUtils.equals(entry.getKey(),"packageName")){
                    params.put("package",entry.getValue());
                } else {
                    params.put(entry.getKey(),entry.getValue());
                }
            }
        }
        params.put("sign", assignSign());
        /*HashMap<String,String> encodeMap = new HashMap<>();
        String json = GsonUtils.getInstance().hashMapToJson(params);
        encodeMap.put("data", DataUtils.base64Encode(DataUtils.base64Encode(json.getBytes()).getBytes()));*/
        /**
         * 字符串进行加密
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("data", DataUtils.base64Encode(RSAEncrypt.encryptByPublicKey(GsonUtils.getInstance().hashMapToJson(params), Constants.public_key).getBytes()));
        return map;
    }

    //加密
    private String assignSign() {
        StringBuffer sign = new StringBuffer();
        Object[] keyArr = params.keySet().toArray();
        Arrays.sort(keyArr);
        for (Object key : keyArr) {
            String value = params.get(key) == null ? "" : params.get(key);
            sign.append(value);
        }
        sign.append(AppConstants.appKey);
        Log.e("assignSign=", sign.toString());
        return SecurityUtils.getMD5Str(sign.toString());
    }

}
