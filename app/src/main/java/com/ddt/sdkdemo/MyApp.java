package com.ddt.sdkdemo;

import com.ddtsdk.KLApplication;
import com.ddtsdk.KLSDK;

/**
 * Created by a5706 on 2018/6/22.
 */

public class MyApp extends KLApplication {
    @Override
    public void onCreate() {
        KLSDK.getInstance().openLog(this, true);
        super.onCreate();
    }
}
