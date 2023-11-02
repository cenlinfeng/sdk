package com.ddt.h5game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import com.ddtsdk.common.base.BaseFragment;
import com.ddtsdk.utils.SharedPreferenceUtil;

import com.gr.xxsy22d.sdb.nearme.gamecenter.R;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.MimeTypeMap;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import static com.ddt.h5game.Constant.MHTAG;

/**
 * Created by CZG55 on 2020/9/2.
 */
public class H5WebFragment extends BaseFragment {

    private onWebCallBack mOnWebCallBack;
    private int isInitX5;
    private WebView webView;
    private Object obj = null;
    private String flag = "";
    private String url = "";

    private boolean hasSet = false;

    //出现webview为null的情况，使用以下方式解决
    private boolean hasJsInterface = true;  //判断是否已经调用过js交互接口
    private boolean hasLoadUrl = true;      //判断是否已经调用过加载连接接口

    public void setJsInterface(Object obj, String flag){
        this.obj = obj;
        this.flag = flag;
        if (webView != null) {
            addJsInterface();
        }
        else hasJsInterface = false;
    }

    private void addJsInterface(){
        if (obj == null){
            if (TextUtils.isEmpty(flag))
                 webView.addJavascriptInterface(new JSInterface(webView), "android");
            else webView.addJavascriptInterface(new JSInterface(webView), flag);
        }
        else {
            //↓也就是在MainActivity显示
            webView.addJavascriptInterface(obj, flag);
        }
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState, View view) {
        isInitX5 = SharedPreferenceUtil.getInstance(getActivity()).getInt(SharedPreferenceUtil.INITX5, -1);
        if (isInitX5 != -1) {
            webView = findViewById(R.id.webView);
            setWebViewConfig(webView);
            if (!hasJsInterface) {
                addJsInterface();
            }
            if (!hasLoadUrl){
                webView.loadUrl(url);
            }
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(InitX5Event initX5Event) {
        if (isInitX5 == -1) {
            webView = findViewById(R.id.webView);
            setWebViewConfig(webView);
            if (!hasJsInterface) {
                addJsInterface();
            }
            if (!hasLoadUrl){
                webView.loadUrl(url);
            }
        }
    }

    public void loadUrl(String url) {
        this.url = url;
        if (!hasSet) {
            webView = findViewById(R.id.webView);
            setJsInterface(obj, flag);
            setWebViewConfig(webView);
        }
        if (webView != null){
            Log.e("H5", "将H5-url放入安卓webView显示");
            webView.loadUrl(url);
        }
        else hasLoadUrl = false;
    }

    @Override
    protected String layoutName() {
        return "fragment_web";
    }

    private void setWebViewConfig(WebView webView) {
        hasSet = true;
        webViewSetting(webView);
        setWebviewClient(webView);
        setWebClient(webView);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "NewApi"})
    private void webViewSetting(WebView webView) {

        WebSettings webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        // 缓存白屏
        String appCachePath = getActivity().getApplicationContext().getCacheDir()
                .getAbsolutePath() + "/webcache";
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabasePath(appCachePath);

        webSettings.setAllowFileAccessFromFileURLs(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //支持与js交互
        webSettings.setJavaScriptEnabled(true);
        //支持通过js打开新的窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置缓存模式  LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //开启缓存
        webSettings.setAppCacheEnabled(true);
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(0);
        }
    }

    //
    private void setWebviewClient(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                Log.d(MHTAG, "onReceivedSslError: ");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(MHTAG, "开始加载");
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                Log.d(MHTAG, "加载结束" + url);
                if (mOnWebCallBack != null) {
                    mOnWebCallBack.onPageFinished();
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Log.i(MHTAG, "重定向Url：" + url);
                if (mOnWebCallBack != null) {
                    mOnWebCallBack.onReceivedError();
                }
            }

            // 链接跳转都会走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(MHTAG, "重定向Url：" + url);
                if (url.contains("ddt://")){
                    H5Utils.getMethodFromUrl(H5WebFragment.this.getActivity(), url, String.class);
                    return true;
                }
                view.loadUrl(url);// 强制在当前 WebView 中加载 url
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
                //做广告拦截，ADFIlterTool 为广告拦截工具类
                Log.i(MHTAG + "h5", url);
                WebResourceResponse response = null;
                if (!ADFilterTool.hasAd(webView.getContext(), url)) {
                    return super.shouldInterceptRequest(webView, url);
                } else {
                    return new WebResourceResponse(null, null, null);
                }
            }

//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
//                //做广告拦截，ADFIlterTool 为广告拦截工具类
//                Log.i(MHTAG + "h5", url);
//                return super.shouldInterceptRequest(webView, url);
//                WebResourceResponse response = super.shouldInterceptRequest(webView, url);
//                String resourceName = BWResourceHelper.getResourceName(getActivity(),url);
//                if (resourceName != null) {
//                    try {
//                        response = new WebResourceResponse(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url)), "UTF-8", getActivity().getAssets().open(resourceName));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!ADFilterTool.hasAd(webView.getContext(), url)) {
//                    return response;
//                } else {
//                    return new WebResourceResponse(null, null, null);
//                }
//            }

        });
    }
    //
    private void setWebClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(MHTAG, "newProgress：" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d(MHTAG, "标题：" + title);
            }


        });
    }

    public WebView getWebView(){
        if (webView != null) return webView;
        else return findViewById(R.id.webView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setOnWebCallBack(onWebCallBack onWebCallBack) {
        this.mOnWebCallBack = onWebCallBack;
    }

    public interface onWebCallBack {
        void onPageFinished();

        void onReceivedError();
    }

    public class Builder{
        public void setJsInterface(Object o, String flag){

        }
    }
}
