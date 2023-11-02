package com.ddtsdk.othersdk.manager;

import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.othersdk.aqysdk.AqyAdUtils;
import com.ddtsdk.othersdk.ttsdk.AdUtils;
import com.ddtsdk.othersdk.gdtsdk.GdtAdUtils;
import com.ddtsdk.othersdk.kssdk.KsAdUtils;
import com.ddtsdk.othersdk.ucsdk.UCSDKUtils;
import com.ddtsdk.utils.Utils;

public class AdFactory {
    private static AdInterface adInterface = null;

    public static AdInterface createAd(String channel) {
        if (channel.contains("ttsdk_")) {
            if (AppConstants.adopen) {
                adInterface = new AdUtils();
            }
            AppConfig.adType = 0;
        } else if (channel.contains("gdtsdk_")) {
//          广点通渠道目前是服务端上报，如改客户端，将此对象打开i5
            if (AppConstants.adopen) {
                adInterface = GdtAdUtils.getInstance();
            }
            AppConfig.adType = 1;
        } else if (channel.contains("ucsdk_")) {
            adInterface = new UCSDKUtils();
            AppConfig.adType = 2;
        } else if (channel.contains("kssdk_")) {
            adInterface = new KsAdUtils();
            AppConfig.adType = 3;
        } else if (channel.contains("aqysdk_")) {
            adInterface = new AqyAdUtils();
            AppConfig.adType = 4;
        }
        return adInterface;
    }



}
