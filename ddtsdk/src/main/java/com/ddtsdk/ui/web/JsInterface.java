package com.ddtsdk.ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.activity.KLPlatformPayActivity;
import com.ddtsdk.ui.activity.KLPlatformPayWebActivity;
import com.ddtsdk.ui.activity.KLUserInfoActivity;
import com.ddtsdk.ui.activity.KLpayWebActivity;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.view.Popupdialog;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


public class JsInterface {
	private  Context mContext;
	private WebView webView;
	
	public JsInterface(Context context) {
		mContext = context;
	}

	public JsInterface(Context context, WebView webView) {
		mContext = context;
	}

	@JavascriptInterface
	public void JavaScriptSavepassword(String user, String pwd, String uid) {
		AccountManager.getInstance(mContext).addHistoryUserData(uid,user,pwd,"");
		//QYTXApi.saveUserToSd(mContext);
	}
	@JavascriptInterface
    public  void JavaScriptToJumpLogin(){
		/*if(AppConfig .isnopay){
		if(Noticedialog.mNoticeListener!=null){
			Noticedialog.mNoticeListener.onClick("close");
			return;
		}
		}
    	if(mContext!=null){
    		((Activity)mContext).finish();
    	}*/
    }
	@JavascriptInterface
    public  void JavaScriptback(){

		Log.e("eeeee", "JavaScriptback");
    	if(mContext!=null){
    		((Activity)mContext).finish();
    	}
    }

	@JavascriptInterface
	public void JavaScriptPayBack(){
		if (mContext instanceof KLPlatformPayWebActivity){
			PayManager.getInstance().newestPay(AppConstants.cp_orderId, AppConstants.cp_amount);
			((Activity) mContext).finish();
		}
		else {
			Intent intent = new Intent();
			intent.putExtra("result", "支付页面关闭，支付结果请查看订单记录!");
			if (mContext != null) {
				((Activity) mContext).setResult(AppConfig.WEB_PAY_SUCCESS, intent);
				((Activity) mContext).finish();
			}
		}
	}

	/**
	 * 下载链接
	 * @param url
	 */
	@JavascriptInterface
	public void JavascriptToDown(String url){
		
		Intent viewIntent = new Intent("android.intent.action.VIEW",
				Uri.parse(url));
		mContext.startActivity(viewIntent);
	}

	@JavascriptInterface
	public void JavaScriptToJumppassword() {
		Intent intent = new Intent();
		intent.putExtra("url", AppConstants.userUrl);
		intent.setClass(mContext,  KLUserInfoActivity.class);
		mContext.startActivity(intent);
	}
	/**
	 * 切换账号
	 */
	@JavascriptInterface
	public void JavascriptSwitchUser() {
		if (mContext!=null && mContext instanceof Activity){
			((Activity)mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					BaseKLSDK.getInstance().doSwitchAccount(true);
					((Activity)mContext).finish();
				}
			});
		}
	}
	/**
	 * 跳转浏览器
	 */
	@JavascriptInterface
	public void JavascriptGoToout(String url){
		Log.e("urlurl", "urwe="+ url);
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(url));
		mContext.startActivity(intent);
	}

	/**
	 * 跳转支付页
	 */
	@JavascriptInterface
	public void JavascriptGoToPay(String url){
		if (!TextUtils.isEmpty(url)){
			Intent intent = new Intent();
			intent.putExtra("url", url);
			intent.setClass(mContext, KLPlatformPayWebActivity.class);
			mContext.startActivity(intent);
		}
	}

	/*
	 * orientation: 1: 横屏 0: 竖屏
	 * */
	@JavascriptInterface
	public  void goBrowser(String url,String type) {
		if (TextUtils.equals(type,"inside")){
			KLPlatformPayActivity.startKLPlatformPayActivity((Activity)mContext, null, url);
		}else if (TextUtils.equals(type,"outside")){
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
			mContext.startActivity(intent);
		}
	}


	/**
	 * 关闭平台支付界面
	 */
	@JavascriptInterface
	public void toCloseView(){
		BaseKLSDK.getInstance().payCallback("");    //4.3.4版本开始，支付加上回调给cp
		if (!((Activity)mContext).isDestroyed()){
			if (mContext instanceof KLPlatformPayWebActivity){
//				BaseKLSDK.getInstance().payCallback(AppConstants.cp_orderId);
				Log.e("eeeee", "fffff");
			}
			((Activity)mContext).finish();
		}
	}

	/**
<<<<<<< HEAD
	 * 是否显示弹窗
	 */
	@JavascriptInterface
	public void toCloseView(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			final boolean status = jsonObject.getBoolean("status");
			((Activity)KLSDK.getInstance().getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
			if (status){
				Popupdialog.getInstance().dismiss();
			}
			else{
				Popupdialog.getInstance().show();
			}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
			Popupdialog.getInstance().dismiss();
		}
	}

	@JavascriptInterface
	public void toClosePopup(){
		if (Popupdialog.getInstance() != null){
			Popupdialog.getInstance().dismiss();
		}
	}

	@JavascriptInterface
	public void payPoint(String ext, int type){
		Log.e("payPointpayPoint", "type=" + type + "===ext=" + ext);
		PayPointReport.getInstance().payPoint(type, ext);
	}
}
