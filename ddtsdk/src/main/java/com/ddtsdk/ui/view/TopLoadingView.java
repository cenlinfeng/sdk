
 
package com.ddtsdk.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * LoadingView,登录成功后的提示窗。
 *
 * created by : chenjm date:2018/04/10
 *
**/
public class TopLoadingView extends FrameLayout  {

	private final int HANDLER_TYPE_HIDE_LOGO = 110;// 隐藏LOGO


	private WindowManager.LayoutParams mWmParams;
	private WindowManager mWindowManager;
	private Context mContext;

//	private boolean mCanHide;// 是否允许隐藏
	private int mScreenWidth;
	private int mScreenHeight;

	private View loadingView;
	private TextView bt_username;
	private Button bt_logout;


//	public static boolean haslive = true;

	private Timer mTimer;
	private TimerTask mTimerTask;
	final Handler mTimerHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case HANDLER_TYPE_HIDE_LOGO:
					//隐藏悬浮框
					handler_hide_view();
					break;
				default:
					LogUtil.i("默认隐藏");
					handler_hide_view();
					break;
			}
			return true;
		}
	});

	private void handler_hide_view(){
        LogUtil.i("系统版本号为："+Build.VERSION.SDK_INT);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {   //7是api 24
			//在版本低于此的时候，做一些处理
			//做一些处理
            LogUtil.i("handler_hide_view-->低版本调用此行");
			setVisibility(View.GONE);  //8.0系统 如小米，写此行出现bug，不消失
		}
		LogUtil.i("handler_hide_view" + loadingView);
		setVisibility(View.GONE);
//		loadingView.setVisibility(View.GONE);
	}



	//构造函数
	public TopLoadingView(Context context) {
		super(context);
		init(context);
	}

	//界面初始化
	private void init(Context mContext) {
		LogUtil.i("mLoadingView初始化");
		this.mContext = mContext;
		mWindowManager = ((Activity)mContext).getWindowManager();

		// 更新浮动窗口位置参数
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		mWindowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		this.mWmParams = new WindowManager.LayoutParams();

		mWmParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
		// 设置图片格式，效果为背景透明
		mWmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为上方
		mWmParams.gravity = Gravity.TOP;

		mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
		mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		mWmParams.y = 100;

		// 设置悬浮窗口长宽数据
		mWmParams.width = LayoutParams.WRAP_CONTENT;
		mWmParams.height = LayoutParams.WRAP_CONTENT;
		try{
			addView(createView(mContext));
			mWindowManager.addView(this, mWmParams);
		}catch(Exception e){
			e.printStackTrace();
		}
		mTimer = new Timer();  //初始化计时器
	}


	/**
	 * 创建loadingview
	 * 
	 * @param context
	 * @return
	 */
	private View createView(final Context context) {
		LogUtil.d("mLoadingView创建   createView");
		LayoutInflater inflater = LayoutInflater.from(context);
		// 从布局文件获取浮动窗口视图
		loadingView = inflater.inflate(
				AppConfig.resourceId(context, "kl_loading_view", "layout"), null);
		bt_username = (TextView) loadingView.findViewById(
				AppConfig.resourceId(context, "bt_username", "id"));

		bt_logout = (Button) loadingView.findViewById(
				AppConfig.resourceId(context, "bt_logout", "id"));

		bt_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchUser();
			}
		});
		bt_logout.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
		bt_logout.getPaint().setAntiAlias(true);//抗锯齿

		loadingView.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec
				.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		loadingView.setVisibility(View.GONE);

		return loadingView;
	}



	private void removeTimerTask() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	private void removeLoadingView() {
		try {
			LogUtil.d("mLoadingView   removeLoadingView  LoadingView被销毁");
			mWindowManager.removeView(loadingView);
			loadingView=null;
//			haslive = false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	/**
	 * 隐藏悬浮窗
	 */
	public void hide() {
        LogUtil.i("隐藏悬浮窗");
		Message message = mTimerHandler.obtainMessage();
		message.what = HANDLER_TYPE_HIDE_LOGO;
		mTimerHandler.sendMessage(message);
		removeTimerTask();
	}


	/**
	 * 显示悬浮窗
	 */
	public void show() {
		try{
			if (getVisibility() != View.VISIBLE) {
				LogUtil.d("show() LoadingView不存在，显示它");
				setVisibility(View.VISIBLE);
			}else {
				LogUtil.d("show() LoadingView已存在");
			}
			loadingView.setVisibility(View.VISIBLE);
			timerForHide();
		}catch(Exception e){
			
		}
	}



	/**
	 * 定时隐藏float view
	 */
	private void timerForHide() {
		LogUtil.d("mLoadingView  timerForHide()方法");
//		mCanHide = true;

		// 结束任务
		if (mTimerTask != null) {
			try {
				mTimerTask.cancel();
				mTimerTask = null;
			} catch (Exception e) {
			}

		}
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				Message message = mTimerHandler.obtainMessage();
				message.what = HANDLER_TYPE_HIDE_LOGO;
				mTimerHandler.sendMessage(message);
			}
		};
//		if (mCanHide) {
			mTimer.schedule(mTimerTask, 3000);
//		}
	}


	/**
	 * 销毁view
	 */
	public void destroy() {
		hide();
		removeLoadingView();
		removeTimerTask();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		try {
			mTimerHandler.removeMessages(1);
		} catch (Exception e) {
		}
	}

	/**
	 * 切换账号
	 */
	public void switchUser() {
		BaseKLSDK.getInstance().doSwitchAccount(true);
		hide();
	}

	public void setUserName(){
		if (AccountManager.getInstance(getContext()).getHistoryUserList().size() > 0){
			BaseUserData userData = AccountManager.getInstance(getContext()).getHistoryUserList().get(0);
			if (userData.getLoginType() == BaseUserData.PHONE){
				bt_username.setText(userData.getPhone());
			} else {
				bt_username.setText(userData.getUname());
			}
		}
	}

}

