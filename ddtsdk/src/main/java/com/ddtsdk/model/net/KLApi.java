package com.ddtsdk.model.net;

import android.content.Context;
import android.util.Log;

import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.model.data.Msg;
import com.ddtsdk.model.protocol.bean.ExclusiveVolumeBean;
import com.ddtsdk.model.protocol.params.ExclusiveVolumeParams;
import com.ddtsdk.model.protocol.params.RoleInfoParams;
import com.ddtsdk.othersdk.manager.bean.BaseState;
import com.ddtsdk.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class KLApi {
    private static KLApi instance;

    private KLApi() {
    }

    public static KLApi getInstance() {
        if (instance == null) {
            instance = new KLApi();
        }
        return instance;
    }

    /**
     * http请求，获取到所有直充卷
     */
    public void requestExclusiveVolume(Context context) {
        ExclusiveVolumeParams params = new ExclusiveVolumeParams();
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_EXCLUSIVE_VOLUME, params, ExclusiveVolumeBean.class, new HttpRequestClient.ResultHandler<ExclusiveVolumeBean>(context) {

            @Override
            public void onSuccess(ExclusiveVolumeBean ev) {
                AppConfig.exclusiveConfig = ev;
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
            }
        });
    }

    /**
     * 心跳上报
     */
    public void heartbeatUpload(Context context) {
        HttpRequestClient.sendPostRequest(ApiConstants.Action_HEARTBEAT, null, Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object baseState) {
                    LogUtil.i("心跳上报成功" + baseState);
            }
        });
    }
}
