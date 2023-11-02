package com.ddtsdk;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.log.LogUtils;

import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.utils.LogUtil;

public class KLApplication extends Application {

    private boolean canInit = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        AppConfig.mApplication = this;
        LogUtils.getInstance().superLog(5, "", null, "klApplication=================");
        // TODO Auto-generated method stub
        super.onCreate();
        //广告SDK初始化
        AdManager.getInstance().init(this);
        int sdkVersion = this.getApplicationInfo().targetSdkVersion;
        LogUtil.i("targetSdkVer=" + sdkVersion);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // TODO Auto-generated method stub
        super.attachBaseContext(base);
    }


    public void setInit(boolean canInit) {
        this.canInit = canInit;
    }

}
