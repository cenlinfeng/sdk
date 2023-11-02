package com.ddtsdk.heartbeat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.model.net.KLApi;
import com.ddtsdk.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class KLHeartbeatService extends Service {
    _KLHeartBeatBinder binder = new _KLHeartBeatBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("heartbeat service onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i("heartbeat service onBind");
        return binder;
    }

    @Override
    public void onDestroy() {
        LogUtil.i("heartbeat service onDestroy");
        binder.stopHeartBeat();
        super.onDestroy();
    }

    class _KLHeartBeatBinder extends Binder {
        TimerTask timerTask;
        Timer timer;

        long startTime;
        long lastTime;
        String token;

        /**
         * 1分钟上报一次
         */
        final static int TIME_INTERVAL = 300 * 1000;

        /**
         * 本次上报与上次上报间隔超过15分钟刷新token
         */
        final static int TIME_REFRESH_TOKEN = 15;

        void startHeartBeat() {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    startTime = startTime == 0 ? getCurrentTime() : startTime;
                    token = TextUtils.isEmpty(token) ? getToken() : token;

                    if (lastTime != 0 && shouldRefreshToken(lastTime, getCurrentTime())) {
                        LogUtil.i("refresh token");
                        token = getToken();
                    }
                    lastTime = getCurrentTime();

//                    LogUtil.i("startTime:" + startTime);
//                    LogUtil.i("lastTime:" + lastTime);
//                    LogUtil.i("token:" + token);
                    KLApi.getInstance().heartbeatUpload(KLSDK.getInstance().getContext());
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, TIME_INTERVAL);
        }

        void stopHeartBeat() {
            startTime = 0;
            lastTime = 0;
            token = null;

            timer.cancel();
            timerTask.cancel();
            timer = null;
            timerTask = null;
        }


        private String getToken() {
            return UUID.randomUUID().toString();
        }

        private long getCurrentTime() {
            return System.currentTimeMillis() / 1000;
        }

        private boolean shouldRefreshToken(long lastUploadTime, long currentUploadTime) {
            long diff = currentUploadTime - lastUploadTime;
            float minutes = diff / (1000 * 60 * 1.f);
            return minutes > TIME_REFRESH_TOKEN;
        }

    }
}



