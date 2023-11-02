package com.ddtsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.params.NoDataParams;

import java.time.Period;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户时长上报，每15分钟上报一次，只要登录成功就开启上报
 * created by 2020/10/28
 */
public class TimeReport {
    private static TimeReport timeReport;
//    private static Handler handler;
//    private static Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            sendReport();
//            timeR();
//        }
//    };
    private TimerTask timerTask;
    private static Timer timer = null;
    private final long timeTotal = 15*60;  //单位：秒 上报服务器时长周期时长
    private final int cacheTotal = 10;    //单位：秒  保存统计的周期时长
    private long currentCacheTime = 0;   //当前内存已经统计的总时长
    private int cacheTime = 0;  //单位秒，秒杀累加，记录达到多少秒存一次数据
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sp;

    public static TimeReport getInstance(Context context){
        if (timeReport == null) {
            timeReport = new TimeReport();
//            handler = new Handler();
            timer = new Timer();
            sp = context.getSharedPreferences("ddt_time_report", Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        return timeReport;
    }

    public void report(){
//        timeR();
        if (!TextUtils.isEmpty(AppConstants.uid)) currentCacheTime = sp.getLong(AppConstants.uid, 0);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                currentCacheTime++;
                if (currentCacheTime >= timeTotal+1){
                    currentCacheTime = 0;
                    HttpRequestClient.sendPostRequest(ApiConstants.ACTION_REPORT, new NoDataParams(), Object.class, new HttpRequestClient.ResultHandler<Object>(KLSDK.getInstance().getContext()) {
                        @Override
                        public void onSuccess(Object o) {
                            try{
                                editor.putLong(AppConstants.uid, currentCacheTime);
                                editor.apply();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) { }
                    });
                }
                cacheTime++;
                if (cacheTime >= cacheTotal+1 && !TextUtils.isEmpty(AppConstants.uid)){
                    editor.putLong(AppConstants.uid, currentCacheTime);
                    editor.apply();
                    cacheTime = 0;
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);  //首次执行1秒,以后周期1秒重复任务
    }

    public void cannelReport(){
//        handler.removeCallbacks(runnable);
        timerTask.cancel();
    }

//    private static void timeR(){
//        handler.postDelayed(runnable, 15*60*1000);
//    }

//    private static void sendReport(){
//        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_REPORT, new NoDataParams(), Object.class, new HttpRequestClient.ResultHandler<Object>(KLSDK.getInstance().getContext()) {
//            @Override
//            public void onSuccess(Object o) { }
//
//            @Override
//            public void onFailure(Throwable t) { }
//        });
//    }
    private TimeReport(){}

}
