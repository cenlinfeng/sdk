package com.ddtsdk.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.ui.view.DdtWebView;
import com.ddtsdk.utils.MainActivityUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.Utils;
import com.ddtsdk.view.RecommendDialog;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by CZG on 2020/5/27.
 * 平台H5页面浏览activity
 */
public class PlatformWebViewActivity extends Activity {
    private static final String KEY_URL = "url";
    private static final String IS_FULL_SCREEN = "is_full_screen";
    private static final String ORIENTATION = "orientation";
    private static final String NO_FLOAT = "no_float";
    private static final String APPENDURL = "appendurl";

    private DdtWebView mWebView;
    private ProgressBar mProgressBar;
    private String mUrl;
    private boolean isFullScreen;
    private static int orientation = -1;
    private boolean isNoFloat = false;

    // 文件选择
    int RESULT_CODE = 0;
    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadMessageArray;
    private RelativeLayout kl_platform_view;

    private static Context mContext =null;
    //

    public static void startThisActivity(Context context, String url) {
        Intent intent = new Intent(context, PlatformWebViewActivity.class);
        intent.putExtra(KEY_URL, url);
//  intent.putExtra(IS_FULL_SCREEN, true);
        //横屏游戏要开启这个，默认是0是竖屏
//        intent.putExtra(ORIENTATION,1);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext = context;
        context.startActivity(intent);


    }

    public static void startThisActivityWithNoFloat(Context context, String url,boolean isFullScreen,int orientation) {
        Intent intent = new Intent(context, PlatformWebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(IS_FULL_SCREEN, isFullScreen);
        intent.putExtra(ORIENTATION,orientation);
        intent.putExtra(NO_FLOAT,true);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext = context;
        context.startActivity(intent);

    }

    public static void startThisActivity(Context context, String url,boolean isFullScreen,int orientation, boolean appendurl) {
        Intent intent = new Intent(context, PlatformWebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(IS_FULL_SCREEN, isFullScreen);
        intent.putExtra(ORIENTATION,orientation);
        intent.putExtra(APPENDURL,appendurl);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext = context;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        KLAppManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
//        setTheme(R.style.kl_plat_transparent);
        setTheme(getResources().getIdentifier("kl_plat_transparent", "style", getPackageName()));
        mUrl = getIntent().getStringExtra(KEY_URL);
        isFullScreen = getIntent().getBooleanExtra(IS_FULL_SCREEN,false);
        orientation = getIntent().getIntExtra(ORIENTATION,-1);
        isNoFloat = getIntent().getBooleanExtra(NO_FLOAT,false);
        boolean appendurl = getIntent().getBooleanExtra(APPENDURL,true);
        initView();
        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }
        if (appendurl){
            mWebView.tryLoadUrl(mUrl);
        }
        else {
            mWebView.loadUrl(mUrl);
        }

        initWebViewSetting();
        initJsInterface();
    }

    private void initView() {
        LinearLayout mainView = new LinearLayout(this);
        mainView.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.setLayoutParams(lp);
        mainView.setWeightSum(1);
        kl_platform_view = new RelativeLayout(this);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (isFullScreen){
            lp2.weight = 1.0f;
        } else if (this.getResources().getConfiguration().orientation == 1) {
            lp2.weight = 1.0f;
        } else {
            lp2.weight = 1.0f;
        }
        try {
            switch (orientation){
                case 0:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case 1:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        kl_platform_view.setLayoutParams(lp2);
        mainView.addView(kl_platform_view);
        mWebView = new DdtWebView(this);
        mWebView.setLayoutParams(lp);
        kl_platform_view.addView(mWebView);
        mProgressBar = new ProgressBar(this);
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(lp3);
        kl_platform_view.addView(mProgressBar);
        setContentView(mainView);
        mProgressBar.setVisibility(View.GONE);

        if (!isFullScreen) {
            ViewGroup.LayoutParams lp4 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout rightView = (LinearLayout) LayoutInflater.from(this).inflate(resourceId("kl_activtiy_platform_rightview", "layout"), null);
            rightView.setLayoutParams(lp4);
            setListener(rightView);
            mainView.addView(rightView);
        }
//        mainView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int right = kl_platform_view.getRight();
//                int x = (int) event.getX();
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    if (x > right){
//                        finish();
//                    }
//                }
//                return false;
//            }
//        });
    }

    public void setListener(View view){
        view.findViewById(resourceId("return_ll", "id")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatformWebViewActivity.this.finish();
                PayPointReport.getInstance().pushPoint(28);
            }
        });

        LinearLayout layout = view.findViewById(resourceId("refresh_ll", "id"));
        if (TextUtils.equals(Utils.getIsH5Game(this),"1")){
            layout.setVisibility(View.VISIBLE);
            view.findViewById(resourceId("view_space", "id")).setVisibility(View.VISIBLE);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivityUtil.getMethod(KLSDK.getInstance().getContext(), "reFreshGame");
                    PlatformWebViewActivity.this.finish();
                    PayPointReport.getInstance().pushPoint(29);
                }
            });
        }
        view.findViewById(resourceId("exit_ll", "id")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(AppConstants.platformUrl)) {
                    PlatformWebViewActivity.this.finish();
                    KLAppManager.getInstance().exitApp();
                }
                else {
                    new RecommendDialog(PlatformWebViewActivity.this, resourceId(
                            "kl_MyDialog", "style"));
                }
                PayPointReport.getInstance().pushPoint(30);

            }
        });

        /*view.findViewById(resourceId("share_ll", "id")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(PlatformWebViewActivity.this).create();
                PayPointReport.getInstance().pushPoint(27);
            }
        });*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isFullScreen){
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) kl_platform_view.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.weight = 0.8f;
        } else {
            lp.weight = 1.0f;
        }
    }

    public boolean isScreenOrientationPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void initJsInterface() {
        mWebView.addJavascriptInterface(new PlatformJsInterface(mWebView), "android");
    }

    private void initWebViewSetting() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessageArray = filePathCallback;
                pickFile();
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();// 接受https所有网站的证书
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    // 打开系统文件浏览
    private void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isFullScreen){
                KLCommonAffirmDialog.Builder(0,false).setContent("请问您是否退出当前页面?").setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                    @Override
                    public void onOkClick() {
                        finish();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }).show(getFragmentManager(),"");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 监听文件选择
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadMessageArray) {
                return;
            }
            if (null != mUploadMessage && null == mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

            if (null == mUploadMessage && null != mUploadMessageArray) {

                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessageArray.onReceiveValue(new Uri[]{result});
                mUploadMessageArray = null;
            }

        }
    }

    @Override
    protected void onDestroy() {
        try {
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.removeAllViews();
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.clearHistory();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        KLAppManager.getInstance().finishActivity(this);
    }
    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }

}
