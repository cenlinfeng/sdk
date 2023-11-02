package com.ddtsdk.ui.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.mylibrary.BuildConfig;
import com.ddtsdk.utils.SecurityUtils;
import com.ddtsdk.utils.UrlParse;

import com.ddtsdk.utils.Utils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CZG on 2020/5/27.
 */
public class DdtWebView extends WebView {

    public DdtWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DdtWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DdtWebView(Context context) {
        super(context);
        init();
    }


    private void init(){
        initSettings();

    }

    private void initSettings() {
        // 初始化设置
        WebSettings mSettings = this.getSettings();
        mSettings.setJavaScriptEnabled(true);//开启javascript
        mSettings.setDomStorageEnabled(true);//开启DOM
        mSettings.setDefaultTextEncodingName("utf-8");//设置字符编码
        //设置web页面
        mSettings.setAllowFileAccess(true);//设置支持文件流
        mSettings.setSupportZoom(false);// 支持缩放
        mSettings.setBuiltInZoomControls(false);// 支持缩放
        mSettings.setUseWideViewPort(true);// 调整到适合webview大小
        mSettings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        mSettings.setBlockNetworkImage(false);
        mSettings.setAppCacheEnabled(false);//开启缓存机制
        // 设置4.2以后版本支持autoPlay，非用户手势促发
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mSettings.setUserAgentString(mSettings.getUserAgentString() + " ddtsdk/" + Utils.getVersionCode(KLSDK.getInstance().getContext()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
    }


    /**
     * 加载过程中加入自定义全局头部信息
     * @param url 加载地址
     */
    public void tryLoadUrl(String url) {

        if (url != null) {
            //根据下发数据判断是否需要添下用户信息
            DeviceInfo deviceInfo = new DeviceInfo(getContext());
            BaseUserData userData = null;
            if (AccountManager.getInstance(getContext()).getHistoryUserList().size() > 0){
                userData = AccountManager.getInstance(getContext()).getHistoryUserList().get(0);
            }
            Map<String, String> params = UrlParse.getUrlParams(url);
            params.put("udid", TextUtils.isEmpty(deviceInfo.getImei())? AppConstants.Oaid:deviceInfo.getImei());
            params.put("uid", userData.getUid());
            params.put("device", "android");
            params.put("source", "android");
            params.put("appid", String.valueOf(AppConstants.appId));
            params.put("channel", AppConstants.ddt_ver_id);
            params.put("time", String.valueOf(System.currentTimeMillis()/1000));
            params.put("token", AppConstants.gametoken);
            if (AppConstants.source==1){
                params.put("isCash", "true");
                AppConstants.source = 0;
            }
            params.put("package", getContext().getPackageName());
            params.put("sign", assignSign(params));
            int i = 0;
            url = UrlParse.getUrlHostAndPath(url);
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                if (i == 0) {
                    url += "?";
                } else {
                    url += "&";
                }
                url += entry.getKey() + "=" + entry.getValue();
                i++;
            }
            super.loadUrl(url);
        }
    }

    private String assignSign(Map<String, String> params) {
        StringBuffer sign = new StringBuffer();
        Object[] keyArr = params.keySet().toArray();
        Arrays.sort(keyArr);
        for (Object key : keyArr) {
            String value = params.get(key) == null ? "" : params.get(key);
            sign.append(value);
        }
        sign.append(AppConstants.appKey);
        return SecurityUtils.getMD5Str(sign.toString());
    }

}
