package com.ddtsdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;

public class KlSplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate: 闪屏页" );
        String MAINCLASS = Utils.getMainclass(this);
        LogUtil.i( "KlSplashActivity onCreate: MAINCLASS="+MAINCLASS );
        if(TextUtils.isEmpty(MAINCLASS)) {
            LogUtil.e("KlSplashActivity onCreate: MAINCLASS="+MAINCLASS+",参数错误!" );
            finish();
        }
        try {
            final Class<?> mainClass = Class.forName(MAINCLASS);
            LogUtil.e("mainClass = "+mainClass.getSimpleName());
            new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Intent intent = new Intent(KlSplashActivity.this,mainClass);
                    startActivity(intent);
                    finish();
                    return false;
                }
            }).sendEmptyMessageDelayed(0,3000);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String layoutName() {
        return Constants.splash;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
