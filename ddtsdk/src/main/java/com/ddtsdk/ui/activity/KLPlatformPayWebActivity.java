package com.ddtsdk.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class KLPlatformPayWebActivity extends BaseActivity implements OnClickListener {
	private String url = "";
	private WebView webView;
	private Boolean isalipay = false;// 区别阿里协议
	private FrameLayout platform_pay_fl;
	private ImageView platform_back_im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
	//	oritation = bundle.getString("oritation");
		url = bundle.getString("url");
		initPage();
		initWebView();
		LogUtil.d("url="+url);
	}

	@Override
	protected String layoutName() {
		return Constants.kl_platform_web_pay;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		webView.setVerticalScrollBarEnabled(false);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 修复一些机型webview无法点击****/
		webView.requestFocus(View.FOCUS_DOWN);
		webView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
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
		// ************************//
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
				sslErrorHandler.proceed();// 接受https所有网站的证书
			}

			@SuppressLint("NewApi") @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.e("androidnew", url);
				if(url.startsWith("mpay://")){
					return true;
				}
				if (url.startsWith("intent://")||url.startsWith("alipays://")) {
					platform_pay_fl.setVisibility(View.VISIBLE);
					Intent intent;
					try {
						isalipay = true;
						intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
						intent.addCategory("android.intent.category.BROWSABLE");
						intent.setComponent(null);
						intent.setSelector(null);
						startActivityIfNeeded(intent, -1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
				if (isalipay) {
					if (url.contains(Constants.baseHttp)) platform_pay_fl.setVisibility(View.GONE);
					view.loadUrl(url);
					return true;
				}

			if (url.startsWith("weixin:")) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        view.getContext().startActivity(intent);
                    } catch (Exception e) {
                    	Toast.makeText(KLPlatformPayWebActivity.this, "请安装微信", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                   return true;
                } else if(!url.startsWith("weixin://wap/pay")&&!url.startsWith("http:")&&!url.startsWith("https:")){
                	   Intent intenturl = new Intent(Intent.ACTION_VIEW,
          						Uri.parse(url));

          				startActivity(intenturl);
          			  return true;
                }
                else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
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

		webhtml(url);
	}

	private void webhtml(String url) {
		try {
			webView.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPage() {
		webView = (WebView) findViewById(getResources().getIdentifier(
				"webview", "id", getPackageName()));
		platform_pay_fl = (FrameLayout) findViewById(getResources().getIdentifier(
				"platform_pay_fl", "id", getPackageName()));
		platform_back_im = (ImageView) findViewById(getResources().getIdentifier(
				"platform_back_im", "id", getPackageName()));
		webView.setBackgroundColor(Color.TRANSPARENT);
		platform_back_im.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				PayManager.getInstance().newestPay(AppConstants.cp_orderId, AppConstants.cp_amount);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == getResources().getIdentifier("web_backbtn", "id",
				getPackageName())) {
			this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PayManager.getInstance().newestPay(AppConstants.cp_orderId, AppConstants.cp_amount);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
