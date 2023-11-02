package com.ddtsdk.platform;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.KLSDK;
import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.ui.activity.KLPlatformPayWebActivity;
import com.ddtsdk.ui.view.DdtWebView;
import com.ddtsdk.utils.CheckApkExistUtils;
import com.ddtsdk.utils.CreatBitmapUtil;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.utils.Utils;
import com.ddtsdk.utils.WeChatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by CZG on 2020/5/27.
 */
public class PlatformJsInterface {
	private  DdtWebView mWebView;

	PlatformJsInterface(DdtWebView webView) {
		mWebView = webView;
	}

	@JavascriptInterface
	public void toSwitchCount() {
		if (mWebView.getContext() instanceof Activity){
			((Activity)mWebView.getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					BaseKLSDK.getInstance().doSwitchAccount(true);
					((PlatformWebViewActivity)mWebView.getContext()).finish();
				}
			});
		}
	}
	/*
	 * orientation: 1: 横屏 0: 竖屏
	 * */
	@JavascriptInterface
	public void goBrowser(final String url, String type, int orientation) {
		Log.e("goBrowser", "goBrowser==url=" + url + " type=" + type + " orientation=" + orientation);
		if (TextUtils.equals(type,"inside")){
			if (KLAppManager.getInstance().getActivitySize(((Activity)mWebView.getContext()).getClass()) > 1){
				mWebView.post(new Runnable() {
					@Override
					public void run() {
						mWebView.tryLoadUrl(url);
					}
				});
			} else {
				PlatformWebViewActivity.startThisActivity(mWebView.getContext(),url,true,orientation, true);
			}
		}
		else if ("directlyOpen".equals(type)){
			PlatformWebViewActivity.startThisActivity(mWebView.getContext(),url,true,orientation, false);
		}
		else if (TextUtils.equals(type,"outside")){
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
			mWebView.getContext().startActivity(intent);
		}
	}

	/**
	 * 跳转支付页
	 */
	@JavascriptInterface
	public void JavascriptGoToPay(String url){
		if (!TextUtils.isEmpty(url)){
			Intent intent = new Intent();
			intent.putExtra("url", url);
			intent.setClass(mWebView.getContext(), KLPlatformPayWebActivity.class);
			mWebView.getContext().startActivity(intent);
		}
	}

	@JavascriptInterface
	public void closeBrowser(){
		if (mWebView.getContext() instanceof Activity){
			((Activity)mWebView.getContext()).finish();
		}
	}

    @JavascriptInterface
    public  void toSubmitInfo(String json){
		RoleData roleData = GsonUtils.getInstance().fromJson(json, RoleData.class);
		if (roleData != null){
			KLSDK.getInstance().setExtData(mWebView.getContext(),roleData);
		}
    }

	@JavascriptInterface
	public void goToAPP(String json){
		Log.e("goToAPP", "json=" + json);
		HashMap<String,String> map = GsonUtils.createObj(json);
		String url = map.get("url");
		String packageName = map.get("packageName");
		if (!TextUtils.isEmpty(packageName) && CheckApkExistUtils.checkApkExist(mWebView.getContext(),packageName)){
			Intent intent = mWebView.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
			mWebView.getContext().startActivity(intent);
		} else if (!TextUtils.isEmpty(url)){
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
			mWebView.getContext().startActivity(intent);
		}
	}

	@JavascriptInterface
	public void toBindWx(){
		WeChatUtils.getInstance().wechaBind(mWebView.getContext(), Utils.getWxAppId(mWebView.getContext()));
	}

	@JavascriptInterface
	public void savePhoto(String url){
		if (!TextUtils.isEmpty(url)){
			try {
				Log.e("savePhoto", "url=" + url);
				String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/ddtFile/";
				String fileName = url.substring(url.lastIndexOf("/")+1);
				File dirF = new File(dirPath);
				if (!dirF.exists()) dirF.mkdirs();
				File file = new File(dirPath + fileName);
				if (file.exists()){
					Toast.makeText(mWebView.getContext(), "图片保存在：/ddtFile/" + fileName, Toast.LENGTH_SHORT).show();
					return;
				}
				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				InputStream is = connection.getInputStream();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1){
					fileOutputStream.write(bytes, 0, len);
				}
				Toast.makeText(mWebView.getContext(), "图片保存在：/ddtFile/" + fileName, Toast.LENGTH_SHORT).show();
				is.close();
				fileOutputStream.close();
				CreatBitmapUtil.updatePic(mWebView.getContext(), file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
