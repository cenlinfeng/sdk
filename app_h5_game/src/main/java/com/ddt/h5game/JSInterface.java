package com.ddt.h5game;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.KLSDK;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.view.DdtWebView;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.MainActivityUtil;
import com.ddtsdk.utils.ToastUtils;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

public class JSInterface{

    private static H5PayListener mH5PayListener;
    private WebView mWebView;

    JSInterface(WebView webView) {
        mWebView = webView;
    }

    @JavascriptInterface
    public void toPay(String json) {
        Log.e("jsonjson", "json=" + json);
        if(json==null){
            Log.e("mhsdk", "h5 to android Pay: null=");
            return;
        }
//        else {
//            Log.e("mhsdk", "toPay: "+json );
//            Log.e("mhsdk", "h5 to android Pay: json="+((String) json));
//        }
        mH5PayListener.onPay(json);
    }

    @JavascriptInterface
    public  void toSubmitInfo(String json){
        mH5PayListener.toSubmitInfo(json);
    }

    @JavascriptInterface
    public void h5Init(String json){
        mH5PayListener.h5Init(json);
    }

    @JavascriptInterface
    public void getInformation(String json){
        InformationBean informationBean = GsonUtils.getInstance().fromJson(json,InformationBean.class);
        if (TextUtils.equals(informationBean.getType(),"ddth5sdk")){
            DeviceInfo deviceInfo = new DeviceInfo(mWebView.getContext());
            informationBean.setAction(deviceInfo.getUuid());
            final String backJson = GsonUtils.getInstance().toJson(informationBean);
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.evaluateJavascript("window.DDTGameSDK.postMsg("+backJson+")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                }
            });
        }
    }

    @JavascriptInterface
    public void goBrowser(String url){
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        mWebView.getContext().startActivity(intent);
    }


    public interface H5PayListener{
        public void onPay(String json);
        public void toSubmitInfo(String json);
        public void h5Init(String json);
    }

    public static void setH5PayListener(H5PayListener listener){
        mH5PayListener = listener;
    }

}



