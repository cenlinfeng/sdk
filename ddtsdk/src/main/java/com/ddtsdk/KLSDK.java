package com.ddtsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.ddtsdk.listener.ApiListenerInfo;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.listener.IKLExitListener;
import com.ddtsdk.listener.InitListener;
import com.ddtsdk.listener.UserApiListenerInfo;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.model.protocol.bean.ResCertificate;

/**
 * Created by CZG on 2020/4/28.
 */
public abstract class KLSDK extends BaseKLSDK{
    private static KLSDK sInstance;

    public static KLSDK getInstance(){
        if(sInstance==null){
            sInstance = new KLSDKClient();
        }
        return sInstance;
    }

    public abstract void applicationInit(Context context);
    public abstract void initInterface(Context context, int appid, String appkey, InitListener listener);
    public abstract void login(Activity context, int appid, String appkey, IDdtListener<LoginMessageInfo> listener);
    public abstract void payment(Activity activity, PaymentInfo payInfo, ApiListenerInfo listener);
    public abstract void exit(Activity activity, IKLExitListener exitlistener);
    public abstract void switchAccount();
    public abstract void getCertificateData(Context context, IDdtListener<ResCertificate> listener);
    public abstract void setUserListener(UserApiListenerInfo listener);
    public abstract void setExtData(Context context, RoleData roleData);
    public abstract void openLog(Context context, boolean isopen);
    public abstract String getAgent(Context context);


    public abstract void onStart(Activity activity);
    public abstract void onCreate(Activity activity);
    public abstract void onStop(Activity activity);
    public abstract void onDestroy(Activity activity);
    public abstract void onResume(Activity activity);
    public abstract void onPause(Activity activity);
    public abstract void onRestart(Activity activity);
    public abstract void onNewIntent(Intent intent);
    public abstract void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
    public abstract void onKeyDown(int keyCode, KeyEvent event);
    public abstract void onConfigurationChanged(Configuration newConfig);
    public abstract void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

}
