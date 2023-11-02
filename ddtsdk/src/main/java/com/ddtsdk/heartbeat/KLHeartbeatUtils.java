package com.ddtsdk.heartbeat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ddtsdk.utils.LogUtil;

public class KLHeartbeatUtils {

    private static KLHeartbeatUtils instance = new KLHeartbeatUtils();
    private Intent intent;
    private ServiceConnection serviceConnection;

    private boolean isBind;


    private KLHeartbeatUtils() {
        //开启服务
        this.serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i("onServiceConnected");
                KLHeartbeatService._KLHeartBeatBinder heartBeatBinder = (KLHeartbeatService._KLHeartBeatBinder) service;
                heartBeatBinder.startHeartBeat();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.i("onServiceDisconnected");
            }
        };
    }

    public static KLHeartbeatUtils getInstance() {
        return instance;
    }

    public void startHeartbeat(Context context) {
        LogUtil.i("开始心跳上报");
        if (intent == null) {
            intent = new Intent(context, KLHeartbeatService.class);
        }
        isBind = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopHeartbeat(Context context) {
        LogUtil.i("停止心跳上报");
        if (isBind) {
            context.unbindService(serviceConnection);
            isBind = false;
        }
    }

}
