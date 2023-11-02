package com.ddtsdk.push;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 推送服务 使用轮询实现 每隔一段时间发起pull请求，
 * 返回数据后跟本地id匹配，如果是没有接收过的，加载详细数据，显示通知栏消息
 * 
 * @author
 * 
 */

public class PushService extends Service {
//	 private static  FloatView mFloatView;

	    @Override
	    public IBinder onBind(Intent intent) {
	        return new FloatViewServiceBinder();
	    }


	    @Override
	    public void onCreate() {
	        super.onCreate();
	     //  mFloatView = new FloatView(this);
	     //  mFloatView.hide();
	    }

	   /* public static void showFloat() {
	        if ( mFloatView != null ) {
	            mFloatView.show();
	        }
	    }
	    public static void hideFloat() {
	        if ( mFloatView != null ) {
	            mFloatView.hide();
	        }
	    }

	    public static void destroyFloat() {
	        if ( mFloatView != null ) {
	            mFloatView.destroy();
	        }
	        mFloatView = null;
	    }*/

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	       // destroyFloat();
	    }

	    public class FloatViewServiceBinder extends Binder {
	        public PushService getService() {
	            return PushService.this;
	        }
	    }
	
}
