package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.utils.LogUtil;

public class MksCommonWebActivity extends Activity implements View.OnClickListener {
    private ImageView ivBlack;
    private ImageView ivClose;
    private TextView tvTitle;
    private WebView wvWebView;
    private Boolean isalipay = false;// 区别阿里协议
    private String url;
    private String title;


    public static MksCommonWebActivity instance(String url, String title) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        MksCommonWebActivity commonWeb = new MksCommonWebActivity();
        return commonWeb;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(AppConfig.resourceId(this, "mks_activity_common_web", "layout"));
        ivBlack = findViewById(AppConfig.resourceId(this, "mks_iv_black", "id"));
        ivClose = findViewById(AppConfig.resourceId(this, "mks_iv_close", "id"));
        tvTitle = findViewById(AppConfig.resourceId(this, "mks_tv_title", "id"));
        wvWebView = findViewById(AppConfig.resourceId(this, "mks_activity_web", "id"));

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        ivBlack.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        initWebView();
        syncCookie(url);

        wvWebView.loadUrl(url);
        //https://api.hnyoumeng.cn/privacy/xieyi1.html
        //https://api.hnyoumeng.cn/privacy/privacy1.html
    }


    private void initWebView() {
        WebSettings settings = wvWebView.getSettings();
        // 设置可以支持缩放
        settings.setSupportZoom(false);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        // 扩大比例的缩放
        settings.setUseWideViewPort(true);
        // 自适应屏幕
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        //重写web页面返回上一级按钮
        wvWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wvWebView.canGoBack()) {
                        wvWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        wvWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mpay://")) return true;

                if (url.startsWith("intent://") || url.startsWith("alipays://")) {
                    Intent intent;
                    try {
                        isalipay = true;
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        intent.setSelector(null);
                        startActivityIfNeeded(intent, -1);
                    } catch (Exception e) {
                        Toast.makeText(KLSDK.getInstance().getContext(), "请安装支付宝", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return true;
                }

                if (isalipay) {
                    view.loadUrl(url);
                }

                if (url.startsWith("weixin:")) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        view.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(KLSDK.getInstance().getContext(), "请安装微信", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return true;
                } else if (!url.startsWith("weixin://wap/pay") && !url.startsWith("http:") && !url.startsWith("https:")) {
                    Intent intenturl = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));

                    startActivity(intenturl);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });

        wvWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
            }

        });

    }

    /**
     * 同步cookie
     *
     * @param url
     */
    private void syncCookie(String url) {
        //判断api版本21以上，webview做了较大的改动，同步cookie的操作已经可以自动同步、但前提是我们必须开启第三方cookie的支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvWebView, true);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, "PHPSESSID=" + AppConstants.Sessid);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);//获取你设置的Cookie
        LogUtil.i("cookie:" + newCookie);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onClick(View view) {
        if (view == ivBlack) {
            finish();
        } else if (view == ivClose) {
            finish();
        }
    }
}
