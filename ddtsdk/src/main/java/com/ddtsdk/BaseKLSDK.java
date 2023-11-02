package com.ddtsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

/**
 * Created by CZG on 2020/6/10.
 */
public abstract class BaseKLSDK {
    private static BaseKLSDK sInstance;

    public static BaseKLSDK getInstance(){
        if(sInstance==null){
            sInstance = new KLSDKClient();
        }
        return sInstance;
    }

    public abstract Context getContext();
    public abstract void  wrapLoginInfo();
    public abstract void returnLogin(String tip);
    public abstract void payCallback(String billo);
    public abstract void onLoadH5Url();
    public abstract void doSwitchAccount(boolean isReturn);//切换账号可选不上报注销信息
    public abstract void goLogin(Activity activity);


}
