package com.ddtsdk.network;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.params.JrttStorageTimeParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 同步今日头条时长统计，上报用户在线时长到后台
 */
public class JrttTimeRequest {
	private static Activity mActivity;
	private static JrttTimeRequest instance;

	private Timer jrttTimer = new Timer();
	private TimerTask jrttTimerTask;

	private static String jrtt_uid = "";		// 当前上报的用户ID
	private static String jrtt_roleLevel = "";	// 今日头条上报需要角色级别
	private static String jrtt_roleId = "";		// 今日头条上报需要角色ID
	private static boolean jrtt_doing = false;	// 今日头条时间事件状态

	public static JrttTimeRequest get() {
		if (instance == null) {
			instance = new JrttTimeRequest();
		}
		return instance;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@SuppressWarnings("AlibabaSwitchStatement")
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case AppConfig.JRTTLINE_TIME:
//					Message mssg = KLSDK.handler.obtainMessage();
//					mssg.obj = msg.obj;
//					mssg.what = AppConfig.RETURN_LOGIN;
//					KLSDK.handler.sendMessage(mssg);
					break;
				default:break;
			}
			return false;
		}
	});

	public void jjrtTime(final Activity activity, String uid, String roleLevel, String roleId){
		LogUtil.d("jrttTime, old_uid=" + jrtt_uid + ", new_uid=" + uid);
		if (AppConfig.adType != 0) {
			Log.d(LogUtil.TAG,"jrttTime 非jrtt包（agent=" + AppConstants.ddt_ver_id + "），无需启动时长上报");
			cancle();
			return;
		}
		if (jrtt_uid.equals(uid)) {
			jrtt_roleLevel = roleLevel;
			jrtt_roleId = roleId;
			Log.d(LogUtil.TAG,"jrttTime 是同一个用户（uid=" + uid + "），无需重复启动时长上报");
			return;
		}

		mActivity = activity;
		jrtt_uid = uid;
		jrtt_roleLevel = roleLevel;
		jrtt_roleId = roleId;

//		final String ddt_ver_id = Utils.getAgent(activity);
//		AppConstants.ddt_ver_id = ddt_ver_id;
		LogUtil.e("jrttTime 开始时长上报了...uid=" + jrtt_uid + ", roleLevel=" + jrtt_roleLevel + ", roleId=" + jrtt_roleId);
		if (jrttTimerTask != null) {
			jrttTimerTask.cancel();
		}
		jrttTimerTask = new TimerTask() {
			@Override
			public void run() {
				JrttStorageTimeParams jrttStorageTimeParams = new JrttStorageTimeParams(jrtt_roleLevel,jrtt_roleId);
//				HttpRequestClient.sendPostRequest(ApiConstants.ACTION_JRTTSTORAGETIME, jrttStorageTimeParams, Object.class, new HttpRequestClient.ResultHandler<Object>(mActivity) {
//					@Override
//					public void onSuccess(Object obj) {
//
//					}
//
//					@Override
//					public void onFailure(Throwable t) {
//
//					}
//				});
				LogUtil.d("jrttTime, level="  + jrtt_roleLevel);
			}
		};

		jrttTimer.schedule(jrttTimerTask, 0, 1000*60);
	}

	public void cancle() {
		jrtt_uid = "";
		jrtt_roleLevel = "";
		jrtt_roleId = "";
		if (jrttTimerTask != null){
			LogUtil.e("jrttTime 停止时长上报");
			jrttTimerTask.cancel();
		}
	}

	private void sendData(int num, Object data, Handler callback) {
		Message msg = callback.obtainMessage();
		msg.what = num;
		msg.obj = data;
		msg.sendToTarget();
	}
}
