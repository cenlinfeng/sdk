package com.ddtsdk.utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/4/28.
 */
public class MyHandlerUtil {
    private static MyHandlerUtil instance;
    private static List<IHandler> task = new ArrayList<IHandler>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            for (int i = 0; i < task.size(); i++) {
                task.get(i).handleMessage(msg);
                task.remove(i);
                i--;
            }
        };
    };

    public static synchronized MyHandlerUtil getInstance(IHandler iHandler) {
        if(instance == null){
            instance = new MyHandlerUtil();
        }
        if (!task.contains(iHandler)) {
            task.add(iHandler);
        }
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    public interface IHandler {
        void handleMessage(Message msg);
    }
}
