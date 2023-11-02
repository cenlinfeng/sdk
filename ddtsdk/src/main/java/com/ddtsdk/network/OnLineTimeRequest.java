package com.ddtsdk.network;

import android.text.TextUtils;

import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.ResOnLineTime;
import com.ddtsdk.model.protocol.params.OnlineTimeParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.ui.activity.KLTipActivity;
import com.ddtsdk.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

public class OnLineTimeRequest {
    private static OnLineTimeRequest instance;

    private Timer onLineTimer = new Timer();
    private TimerTask onLineTimerTask;

    public static OnLineTimeRequest get() {
        if (instance == null) {
            instance = new OnLineTimeRequest();
        }
        return instance;
    }

    public void onlineTime() {
//		LogUtil.e("onLineTime 开始时长统计了...minutetime=" + AppConfig.minutetime + ", isautonym=" + AppConstants.isautonym + ", isnonage=" + AppConstants.isnonage);
        /**
         * AppConfig.minutetime  为-1的话表示，不开启时长上报统计
         */
        if (AppConstants.minutetime == -1) return;
        if (onLineTimerTask != null) {
            onLineTimerTask.cancel();
        }

        /**
         * AppConfig.isautonym是否实名， AppConfig.isnonage是否是成年人
         *
         * !(AppConstants.isautonym == 1 && AppConstants.isnonage == 0)表示不是成年人和未实名的用户开启时长上报统计
         *
         * 时长统计的时间根据服务端初始化返回的时长字段进行延时（AppConfig.minutetime）
         */
        if (!(AppConstants.isautonym == 1 && AppConstants.isnonage == 0)) {
			onLineTimerTask = new TimerTask() {
				@Override
				public void run() {
					loadOnlineTime(true);
				}
			};
            int delay = 60 * 1000 * AppConstants.minutetime;
            onLineTimer.schedule(onLineTimerTask, delay,delay);
//			LogUtil.e("onLineTime 开启了时间倒计时..." + delay / 1000 + "秒");
        } else {
//			LogUtil.e("onLineTime 原来是成年人，不用时间倒计时...");
        }
    }

    public void loadOnlineTime(final boolean isReturn) {
		if (BaseKLSDK.getInstance().getContext() == null) return;
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_ONLINETIME, new OnlineTimeParams(isReturn?AppConstants.minutetime:0), ResOnLineTime.class,
                new HttpRequestClient.ResultHandler<ResOnLineTime>(BaseKLSDK.getInstance().getContext()) {
                    @Override
                    public void onSuccess(ResOnLineTime resOnLineTime) {
                        AppConstants.isautonym = resOnLineTime.getIsautonym();
                        AppConstants.isnonage = resOnLineTime.getIsnonage();
                        /*if (resOnLineTime.getIsbanlogin() == 1) {
                            if (isReturn){
                                sendData(AppConfig.ONLINE_TIME_LIMIT, "根据国家规定，未实名和未成年人用户实行游戏时长上限策略，您已达游戏时长上限!", handler);
                            } else {
                                BaseKLSDK.getInstance().doSwitchAccount(false);
                            }
                        }*/
                        if (!TextUtils.isEmpty(resOnLineTime.getMsg())) {
                            KLTipActivity.startThisActivity(BaseKLSDK.getInstance().getContext(), resOnLineTime.getMsg(),resOnLineTime.getIsbanlogin(),isReturn);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    public void cancle() {
        if (onLineTimerTask != null) {
            LogUtil.e("onLineTime 停止时长统计");
            onLineTimerTask.cancel();
        }
    }

}
