package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.protocol.bean.ResCertificate;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.SecurityUtils;

import com.ddtsdk.utils.Utils;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CZG on 2020/4/21.
 * 用户信息
 */
public class KLPlatformPayActivity extends Activity {
    private WebView mWebview;
    private ProgressBar mProgressBar;
    private PaymentInfo payInfo;
    private StringBuilder builder;
    private Boolean isalipay = false;// 区别阿里协议
    private String url = "";
    private final int timeOut = 10 * 1000;
    private Handler mHandler;
    private String payUrl = "";
    private DeviceInfo deviceInfo;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!KLPlatformPayActivity.this.isDestroyed()) {
                PayPointReport.getInstance().payPoint(3, "timeout");
                Toast.makeText(KLPlatformPayActivity.this, "网络请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                KLPlatformPayActivity.this.finish();
            }
        }
    };

    public static void startKLPlatformPayActivity(final Activity activity, final PaymentInfo payInfo, String payurl) {
        if (AppConstants.isautonym == 1) {
            Intent intent = new Intent(activity, KLPlatformPayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("kl_pay_info", payInfo);
            bundle.putString("url", payurl);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else {
            AppConstants.pay_realname = 1;
            hasCertificate(activity, payInfo, payurl);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        setContentView(AppConfig.resourceId(this, Constants.getPlatform_pay, "layout"));
        builder = new StringBuilder();
        mHandler = new Handler();
        deviceInfo = new DeviceInfo(this);
        //初始化后加载url
        intView();
    }


    private void intView() {
        // TODO Auto-generated method stub
        mWebview = findViewById(AppConfig.resourceId(this,
                "kl_platform_webview", "id"));
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        mProgressBar = findViewById(AppConfig.resourceId(this,
                "progressBar", "id"));
        mProgressBar.setVisibility(View.GONE);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        payInfo = intent.getParcelableExtra("kl_pay_info");
        if (!TextUtils.isEmpty(url)) {
            loadWebUrl(url);
            return;
        }
        initData();
        adLogReport();
        //webview加载url
        if (Utils.isWifiProxy(this)) {
            Log.e("PROXY", "====================3");

            if (AppConstants.banpackage) {
                KLCommonAffirmDialog.Builder(1, false)
                        .setContent("网络异常，请重新进入游戏！")
                        .setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                            @Override
                            public void onOkClick() {
                                System.exit(0);
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        }).show((this).getFragmentManager(), "");
                return;
            } else {
                loadWebUrl(toPayParam());
            }
        } else {
            loadWebUrl(toPayParam());
        }
        mHandler.postDelayed(mRunnable, timeOut);
    }

    public void adLogReport() {
        Bundle bundle = new Bundle();
        bundle.putString(EventFlag.ORDERID, AppConstants.cp_orderId);
        bundle.putString(EventFlag.AMOUNT, AppConstants.cp_amount);
        bundle.putString(EventFlag.MSG, "1");
        bundle.putString(EventFlag.STATUS, "success");
        AdManager.getInstance().logPayReport(KLSDK.getInstance().getContext(), bundle);
    }

    private void initData() {
        if (getIntent() != null) {
            AppConstants.cp_orderId = payInfo.getBillno();
            AppConstants.cp_amount = payInfo.getAmount();
        }
    }

    private String toPayParam() {

        Map<String, String> params = new HashMap<>();
        params.put("appid", String.valueOf(AppConstants.appId));
        params.put("uid", String.valueOf(AppConstants.uid));
        params.put("device", "1");
        params.put("sdkversion", AppConstants.appVer);
        params.put("pkversion", deviceInfo.getAppVersion());
        params.put("ver", AppConstants.ddt_ver_id);
        params.put("amount", payInfo.getAmount());
        params.put("sessid", AppConstants.Sessid);
        params.put("billno", payInfo.getBillno());
        params.put("extrainfo", payInfo.getExtrainfo());
        params.put("subject", payInfo.getSubject());
        params.put("serverid", payInfo.getServerid());
        params.put("istest", "0");
        params.put("rolename", payInfo.getRolename());
        params.put("rolelevel", payInfo.getRolelevel());
        params.put("roleid", payInfo.getRoleid());
        params.put("paytarget", "5");
        params.put("imei", deviceInfo.getImei());
        params.put("oaid", AppConstants.Oaid);
        params.put("package", getPackageName());
        params.put("token", AppConstants.Token);

        return signParam(params);

    }

    private String signParam(Map<String, String> params) {
        StringBuilder tempSB = new StringBuilder();
        tempSB.append(AppConstants.platform_pay);
        tempSB.append("?");
        StringBuffer boatrace = new StringBuffer();
        Object[] keyArr = params.keySet().toArray();
        Arrays.sort(keyArr);
        for (Object key : keyArr) {
            String value = params.get(key) == null ? "" : params.get(key);
            boatrace.append(value);
            boatrace.append("&");
            tempSB.append(key.toString()).append("=").append(value).append("&");
        }
        boatrace.append(AppConstants.appKey);
        tempSB.append("productName=").append(payInfo.getSubject());
        tempSB.append("&udid=").append(deviceInfo.getUuid());
        Log.e("1111122222", "boatrace:" + boatrace.toString());
        Log.e("1111122222", "boatrace_md5:" + SecurityUtils.getMD5Str(boatrace.toString()));
        tempSB.append("&boatrace=").append(SecurityUtils.getMD5Str(boatrace.toString()));
        AppConstants.paramquery = tempSB.toString();
        LogUtil.e(AppConstants.paramquery);
        return tempSB.toString();
    }

    private void loadWebUrl(String url) {
        if (TextUtils.isEmpty(url)) finish();
        Log.d("加载的地址:", url);
        payUrl = url;
        webviewSetting();
        webviewFixQ();
        webviewSetCli();
        mWebview.loadUrl(url);
    }


    private void webviewSetting() {
        mWebview.getSettings().setSupportZoom(true);
        mWebview.getSettings().setSaveFormData(false);
        mWebview.getSettings().setSavePassword(false);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setDisplayZoomControls(false);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.addJavascriptInterface(new JsInterface(this), "android");
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private void webviewFixQ() {
        // 修复一些机型webview无法点击****/
        mWebview.requestFocus(View.FOCUS_DOWN);
        mWebview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void webviewSetCli() {

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("urlurl", "url=" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
                sslErrorHandler.proceed();// 接受https所有网站的证书
                Log.e("PayPointReport11", "请求失败=onReceivedError888=");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mRunnable);
                PayPointReport.getInstance().payPoint(2, "loading success");
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                mProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mRunnable);
//				mWebview.setBackgroundColor(Color.WHITE);
                Log.e("PayPointReport11", "请求失败=onReceivedError=" + i + "==>" + s + "==>" + s1);
                PayPointReport.getInstance().payPoint(3, i + "==>" + s + "==>" + s1);
                Toast.makeText(KLPlatformPayActivity.this, "网络请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                KLPlatformPayActivity.this.finish();
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                Log.e("PayPointReport11", "请求失败=onReceivedHttpError=" + webResourceResponse.getStatusCode());
                PayPointReport.getInstance().payPoint(3, "Status=" + webResourceResponse.getStatusCode() + " url=" + webView.getUrl());

            }
        });
        mWebview.setWebChromeClient(new WebChromeClient());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public static void hasCertificate(final Context context, final PaymentInfo payInfo, final String payurl) {
        KLSDK.getInstance().getCertificateData(context, new IDdtListener<ResCertificate>() {
            @Override
            public void onSuccess(ResCertificate data) {
                if (data != null) {
                    if (data.getIsautonym() == 1) {
                        AppConstants.isautonym = 1;
                        Intent intent = new Intent(context, KLPlatformPayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("kl_pay_info", payInfo);
                        bundle.putString("url", payurl);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        RealNameActivity.startThisActivity((Activity) context, 3);
                        KLTipActivity.startThisActivity(context, "【防沉迷系统】根据系统规定，您还不可以充值哦！");
                    }
                }
            }
        });
    }

}
