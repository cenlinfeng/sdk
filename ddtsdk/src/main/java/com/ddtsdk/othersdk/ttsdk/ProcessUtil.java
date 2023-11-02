package com.ddtsdk.othersdk.ttsdk;

import android.app.ActivityManager;
import android.content.Context;


/**
 * Created by chen on 2018/1/25.
 */

public class ProcessUtil {

//    public static String getProcessName(Context context){
//        String sCurProcessName = null;
//        try{
//            int pid = android.os.Process.myPid();
//
//            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
//                if (appProcess.pid == pid) {
//                    if (Logger.debug()) {
//                        Logger.d("Process", "processName = " + appProcess.processName);
//                    }
//                    sCurProcessName = appProcess.processName;
//                }
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return sCurProcessName;
//    }
}
