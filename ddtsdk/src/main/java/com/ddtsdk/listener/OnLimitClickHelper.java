package com.ddtsdk.listener;

import android.view.View;

import com.ddtsdk.utils.LogUtil;

import java.util.Calendar;

public class OnLimitClickHelper implements View.OnClickListener {

    public static final int LIMIT_TIME = 3000;
    private long lastClickTime = 0;
    private OnLimitClickListener onLimitClickListener;

    public OnLimitClickHelper(OnLimitClickListener onLimitClickListener){
        this.onLimitClickListener = onLimitClickListener;
    }

    @Override
    public void onClick(View view) {
        long curTime = Calendar.getInstance().getTimeInMillis();
        long duration = curTime - lastClickTime;
        if (duration > LIMIT_TIME) {
            lastClickTime = curTime;
            if(onLimitClickListener != null){
                onLimitClickListener.onClick(view);
            }
        }else {
            LogUtil.i("onClick: curTime="+duration+"<"+LIMIT_TIME);
        }
    }
}
