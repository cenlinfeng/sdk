package com.ddtsdk.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by CZG on 2020/4/21.
 * 用户信息
 */
public class KLUserInfoActivity extends BaseActivity implements OnClickListener {
	private WebView mWebview;
	private String mUrl;
	private ImageView mBack;
	private ProgressBar mProgressBar;
//	private ImageView mClose;
//	private RelativeLayout rl;
//	private int h5_height;
	private String close_Url = "koala://close";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mUrl = getIntent().getStringExtra("url");

		LogUtil.d("加载的地址:"+ mUrl);

		if(TextUtils.isEmpty(mUrl)) {
			finish();
		}

		//初始化后加载url
		intView();

	}

	@Override
	protected String layoutName() {
		return Constants.user_info;
	}


	private void intView() {
		// TODO Auto-generated method stub
		mWebview = (WebView) findViewById(AppConfig.resourceId(this,
				"kl_webview", "id"));
		mBack = (ImageView) findViewById(AppConfig.resourceId(this,
				"kl_userback", "id"));
		mProgressBar  = (ProgressBar) findViewById(AppConfig.resourceId(this,
				"progressBar", "id"));
		mProgressBar.setVisibility(View.GONE);
//		用户协议
		if (mUrl.contains("xieyi.html")) {
			LogUtil.d("包含特殊词，开启返回按钮");
			mBack.setVisibility(View.VISIBLE);
			mBack.setOnClickListener(this);
		} else {
			mBack.setVisibility(View.GONE);
		}

		//webview加载url
		loadWebUrl();
	}

	private void loadWebUrl(){
		webviewSetting();
		webviewFixQ();
		webviewSetCli();
		mWebview.loadUrl(mUrl);
	}


	private void webviewSetting() {
		mWebview.setVerticalScrollBarEnabled(false);
		mWebview.getSettings().setSupportZoom(false);
		mWebview.getSettings().setSaveFormData(false);
		mWebview.getSettings().setSavePassword(false);
		mWebview.getSettings().setBuiltInZoomControls(false);
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.getSettings().setUseWideViewPort(true);
		mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		mWebview.getSettings().setLoadWithOverviewMode(true);
		mWebview.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
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

	private void webviewSetCli(){

		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mProgressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("weixin://wap/pay")) {
					Intent intent = null;
					try {
						intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
						view.getContext().startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;

				} else if(url.startsWith(close_Url)){
					finish();
					return true;

				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}
			}

			@Override
			public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
				sslErrorHandler.proceed();// 接受https所有网站的证书
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}
		});
		mWebview.setWebChromeClient(new WebChromeClient());
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == AppConfig.resourceId(this, "kl_userback", "id")) {
			if (mWebview.canGoBack()) {
				mWebview.goBack();
			} else {
				finish();
				AppConfig.isShow =true;
			}
		}
	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
			mWebview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	//检查app是否存在
	public boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

}
