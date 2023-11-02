package com.ddt.h5game;

import android.content.Context;
import android.util.Log;

import com.ddtsdk.KLApplication;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.utils.SharedPreferenceUtil;
import com.tencent.smtt.sdk.QbSdk;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a5706 on 2018/6/22.
 */

public class MyApp extends KLApplication {

    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initX5();
    }

    public void initX5() {
        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("X5bro", " onViewInitFinished is " + arg0);
                EventBus.getDefault().post(new InitX5Event(arg0));
                SharedPreferenceUtil.getInstance(getApplicationContext()).setInt(SharedPreferenceUtil.INITX5,arg0?1:0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        });
    }

    /**
     * 获得当前app运行的Application
     */
    public static synchronized MyApp getInstance() {
        return instance;
    }

}
