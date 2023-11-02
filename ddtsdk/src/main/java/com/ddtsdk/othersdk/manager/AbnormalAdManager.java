package com.ddtsdk.othersdk.manager;

import android.app.Activity;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.othersdk.gdtsdk.GdtAdUtils;

public class AbnormalAdManager {
    public static void abAdInit(){
        if (!AppConstants.init_type.equals("decrement") && AdManager.getInstance().abnormalAdReport()){
            AppConstants.adopen = true;
            ((Activity)KLSDK.getInstance().getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adInit();
                }
            });
        }
    }

    public static void adInit(){
        switch (AppConfig.adType){
            case 0:
                AdManager.getInstance().init(AppConfig.mApplication);
                AdManager.getInstance().activityCreate(KLSDK.getInstance().getContext());
                break;
            case 1:
                AppConfig.mApplication.setInit(true);
                GdtAdUtils.getInstance().cApp(AppConfig.mApplication);
                break;
            default:
                break;
        }
    }
}
