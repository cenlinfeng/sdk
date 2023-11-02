package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.PayPointReport;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class KLSmallGameActivity extends Activity {
    private WebView mWebview;
    private ProgressBar progressBar_smallgame;

    private String url = "";
    private  String startTime;
    private TimerTask timerTask;
    private static Timer timer = null;
    private SimpleDateFormat d;
    private  String nowTime;
    private final int RETURN_LOGIN = 8;
    private final int WEBVIEW_SHOW = 7;
    private static Activity context;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RETURN_LOGIN:
                    AppConstants.isOpen_smallgame =0;
                    KLSDK.getInstance().goLogin(context);
                    break;

                case WEBVIEW_SHOW:
                    progressBar_smallgame.setVisibility(View.GONE);
                    mWebview.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };



    public static void startKLSmallGameActivity(final Activity activity){

        Intent intent = new Intent(activity, KLSmallGameActivity.class);
        context = activity;
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(AppConfig.resourceId(this, Constants.kl_open_smallgame, "layout"));
        intView();
        d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
        timer = new Timer();
        startTime=d.format(new Date());//按以上格式 将当前时间转换成字符串
        timerTask = new TimerTask() {
            @Override
            public void run() {
                nowTime =d.format(new Date());
                Log.e("HSDAVDFC",nowTime);
                try {
                    if(((d.parse(nowTime).getTime()-d.parse(startTime).getTime())/60000) >= AppConstants.isOpen_smallgame){
                        handler.sendEmptyMessage(RETURN_LOGIN);
                        finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);  //首次执行1秒,以后周期1秒重复任务
    }



    private void intView() {
        // TODO Auto-generated method stub
        mWebview = findViewById(AppConfig.resourceId(this,
                "webView_smallgame", "id"));
        progressBar_smallgame  = findViewById(AppConfig.resourceId(this,"progressBar_smallgame", "id"));
        progressBar_smallgame.setVisibility(View.GONE);

        Log.e("FSADHADF","FGASDFJJJJ1111111");
            loadWebUrl(AppConstants.small_game_url);
            Log.e("FSADHADF","FGASDFJJJJ");

        }


    private void loadWebUrl(String url){
        if (TextUtils.isEmpty(url)) finish();
        Log.d("加载的地址:", url);

        webViewSetting(mWebview);
        webviewFixQ();
        webviewSetCli();
        mWebview.loadUrl(url);
    }


        private void webViewSetting(WebView webView) {

            WebSettings webSettings = webView.getSettings();
            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(0);
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            }
            this.getWindow().setFormat(PixelFormat.TRANSLUCENT);
            // 缓存白屏
            String appCachePath = this.getApplicationContext().getCacheDir()
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

    private void webviewSetCli(){

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar_smallgame.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				Log.e("urlurl")
                Log.e("urlurl", "url="+ url);

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000*1);
                            handler.sendEmptyMessage(WEBVIEW_SHOW);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Toast.makeText(KLSmallGameActivity.this, "网络请求失败，请稍后重试！", Toast.LENGTH_SHORT).show();
//
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                //400以上错误

            }
        });
        mWebview.setWebChromeClient(new WebChromeClient());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("GHSZDFZHGDC","GSAAAAAAADHZZZZZZZZZFF");
        removeTimerTask();
    }

    private void removeTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

}


