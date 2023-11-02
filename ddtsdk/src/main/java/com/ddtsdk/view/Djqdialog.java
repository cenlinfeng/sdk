package com.ddtsdk.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.ScreenUtils;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class Djqdialog extends Dialog{
	private Context mContext;
	private View mView;
	private WebView webView;
	private static Djqdialog dialog;

	public static Djqdialog getInstance(){
		return dialog;
	}

	public Djqdialog(Context context) {
		super(context, AppConfig.resourceId(context,
				"kl_PopupDialog", "style"));
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "kl_dialog_popup", "layout"),
				null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mView);
		dialog = this;
		webView = (WebView) mView.findViewById(AppConfig.resourceId(mContext,"popup_webview", "id"));
		webView.setBackgroundColor(Color.TRANSPARENT);
		initWebView();
		ViewGroup.LayoutParams wvl= webView.getLayoutParams();
		wvl.width = ScreenUtils.getScreenWidth(getContext());
		wvl.height = ScreenUtils.getScreenHeight(getContext());
		webView.loadUrl(getUrl());
	}

	private String getUrl(){
		return "https://h5frontend.ddtugame.com/platform/sdk_pf/1.0.0/popup_window/index.html#/?" +
				"appid=" + AppConstants.appId +
				"&position=1" +
				"&uid=" + AppConstants.uid;
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
		webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new JsInterface(mContext), "android");
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 修复一些机型webview无法点击****/
		webView.requestFocus(View.FOCUS_DOWN);
		// ************************//
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
				sslErrorHandler.proceed();// 接受https所有网站的证书
			}
			@SuppressLint("NewApi") @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
				super.onPageStarted(webView, s, bitmap);
			}

			@Override
			public void onPageFinished(WebView webView, String s) {
				super.onPageFinished(webView, s);
			}
		}
		);

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
//				if (newProgress == 100) {
//					Message msg = new Message();
//					msg.what = FLAG_WEB_SUCCESS;
//					handler.sendMessage(msg);
//				}
			}
		});
	}

}
