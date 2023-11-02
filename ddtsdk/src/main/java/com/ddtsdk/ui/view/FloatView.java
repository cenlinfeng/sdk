package com.ddtsdk.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.ui.activity.KLPlatFormBuyActivity;
import com.ddtsdk.ui.activity.KLUserInfoActivity;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.ScreenUtils;
import com.ddtsdk.view.BallControler;

import java.util.Timer;
import java.util.TimerTask;


public class FloatView extends FrameLayout implements OnTouchListener {

    private final int HANDLER_TYPE_HIDE_LOGO = 100;// 隐藏LOGO

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;
    private Context mContext;

    private ImageView mIvFloatLogo;
    private View float_empty_view;
    private LinearLayout mLlFloatMenu;
    private LinearLayout mTvAccount;
    private LinearLayout mTvKefu;
    private LinearLayout mTvGonglue;
    private LinearLayout mTvHide;
    private LinearLayout mTvGift;
    private FrameLayout rootFloatView;

    private View line1;
    private View line2;
    private View line3;

    private View right_view;
    private View left_view;
    private float float_logo_width = 0;

    private FrameLayout mFlFloatLogo;

    private boolean mIsRight;// logo是否在右边
    private boolean mCanHide;// 是否允许隐藏
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;
    private boolean mShowLoader = true;
    private boolean rotateRight = false;
    private boolean canTouch = true;
    private boolean showPlatformView = true;
    private int ztl_height;

    private Timer mTimer;
    private TimerTask mTimerTask;
    final Handler mTimerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_TYPE_HIDE_LOGO:
                    // 比如隐藏悬浮框
                    if (mCanHide) {
                        mCanHide = false;
                        float_empty_view.setVisibility(GONE);
                        canTouch = false;
                        showPlatformView = false;
//						if (mIsRight) {
//							// 靠边隐藏图片
////							mIvFloatLogo.setImageResource(AppConfig.resourceId(mContext, "kl_halftuoyuanround", "mipmap"));
//							setAnimation(false);
//						}
//						else{
////							mIvFloatLogo.setImageResource(AppConfig.resourceId(mContext, "kl_halftuoyuan", "mipmap"));
//							setAnimation(false);
//						}
//						mWmParams.alpha = 0.7f;
                        mWindowManager.updateViewLayout(FloatView.this, mWmParams);
                        mLlFloatMenu.setVisibility(View.GONE);
                        refreshFloatMenu(mIsRight, false);
                        setAnimation(false);
                    }
                    break;
            }
            return false;
        }
    });


    public FloatView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        mWindowManager = ((Activity) mContext).getWindowManager();
        ztl_height = ScreenUtils.getStatusHeight(mContext);
        //mWindowManager = (WindowManager) mContext
        //		.getSystemService(Context.WINDOW_SERVICE);
        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        this.mWmParams = new WindowManager.LayoutParams();
        // 设置window type
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			mWmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//		} else {
//			mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//		}
        mWmParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        // 设置图片格式，效果为背景透明
        mWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置?
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mWmParams.x = 0;
        mWmParams.y = mScreenHeight / 2;
        mWmParams.alpha = 1f;

        // 设置悬浮窗口长宽数据
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
        try {
            addView(createView(mContext));
            mWindowManager.addView(this, mWmParams);
        } catch (Exception e) {

        }
        mTimer = new Timer();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:// 横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:// 竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    /**
     * 创建Float view
     *
     * @param context
     * @return
     */

    private View createView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 从布局文件获取浮动窗口视图
        rootFloatView = (FrameLayout) inflater.inflate(
                AppConfig.resourceId(context, "kl_float_view", "layout"), null);
        mFlFloatLogo = (FrameLayout) rootFloatView.findViewById(AppConfig
                .resourceId(context, "float_view", "id"));

        left_view = (View) rootFloatView.findViewById(AppConfig
                .resourceId(context, "left_view", "id"));

        right_view = (View) rootFloatView.findViewById(AppConfig
                .resourceId(context, "right_view", "id"));


        // 浮点图标
        mIvFloatLogo = (ImageView) rootFloatView.findViewById(AppConfig
                .resourceId(context, "float_view_icon_imageView", "id"));
        float_empty_view = rootFloatView.findViewById(AppConfig.resourceId(context, "float_empty_view", "id"));
        loadFloat();

        mLlFloatMenu = (LinearLayout) rootFloatView.findViewById(AppConfig
                .resourceId(context, "ll_menu", "id"));

        //个人中心按钮
        mTvAccount = (LinearLayout) rootFloatView.findViewById(AppConfig
                .resourceId(context, "tv_account", "id"));
        mTvAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LogUtil.i(AppConstants.userUrl);
                turnToIntent(AppConstants.userUrl);
                mLlFloatMenu.setVisibility(View.GONE);
            }
        });

        //客服按钮
        mTvKefu = (LinearLayout) rootFloatView.findViewById(AppConfig
                .resourceId(context, "tv_kefu", "id"));
        mTvKefu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                turnToIntent(AppConstants.serviceUrl);
                mLlFloatMenu.setVisibility(View.GONE);
            }
        });

        //代金卷
        mTvGonglue = (LinearLayout) rootFloatView.findViewById(AppConfig
                .resourceId(context, "mks_ll_platform_pay", "id"));
        mTvGonglue.setVisibility(View.GONE);
        mTvGonglue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, KLPlatFormBuyActivity.class);
                mContext.startActivity(intent);
                mLlFloatMenu.setVisibility(View.GONE);
            }
        });

        //礼包中心
        mTvGift = rootFloatView.findViewById(AppConfig.resourceId(context, "ll_gift", "id"));
        mTvGift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLViewControl.getInstance().showActivityCenter((Activity) mContext);
                mLlFloatMenu.setVisibility(View.GONE);
            }
        });

        //退出按钮
        mTvHide = (LinearLayout) rootFloatView.findViewById(AppConfig.resourceId(
                context, "tv_hide", "id"));
        mTvHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				KLSDK.userlistenerinfo.onLogout("logout");//登出
                mLlFloatMenu.setVisibility(View.GONE);
                hide();
            }
        });

        line1 = (View) rootFloatView.findViewById(AppConfig.resourceId(
                context, "split1", "id"));
        line2 = (View) rootFloatView.findViewById(AppConfig.resourceId(
                context, "split2", "id"));
        line3 = (View) rootFloatView.findViewById(AppConfig.resourceId(
                context, "split3", "id"));

        rootFloatView.setOnTouchListener(this);
        rootFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPlatformView) {
                    if (TextUtils.isEmpty(AppConstants.platformUrl)) return;
                    PlatformWebViewActivity.startThisActivity(getContext(), AppConstants.platformUrl);
                    showPlatformView = false;
                    return;
                }
                showPlatformView = true;
                canTouch = true;
                setAnimation(true);
//				loadFloat();
                mWindowManager.updateViewLayout(FloatView.this, mWmParams);
                timerForHide();
                if (!TextUtils.isEmpty(AppConstants.platformUrl)) {
                    if (float_empty_view.getVisibility() == View.VISIBLE) {
                        float_empty_view.setVisibility(View.GONE);
                    } else {
                        float_empty_view.setVisibility(View.VISIBLE);
                    }
                    return;
                }

                if (!mDraging) {
                    if (mLlFloatMenu.getVisibility() == View.VISIBLE) {
                        mLlFloatMenu.setVisibility(View.GONE);
                    } else {
                        mLlFloatMenu.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        rootFloatView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        rootFloatView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float_logo_width = mIvFloatLogo.getWidth();
                Log.e("edgdgg", "float_logo_width=" + float_logo_width);
            }
        });
        return rootFloatView;
    }

    /**
     * 隐藏 某些菜单功能
     */
    public void hideItems() {
        showAllItems();
        if (!BallControler.canShow("userfloat")) {
            mTvAccount.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("servicefloat")) {
            line1.setVisibility(View.GONE);
            mTvKefu.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("moneyrechargefloat")) {
            line2.setVisibility(View.GONE);
            mTvGonglue.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("activityfloat")) {
            mTvGift.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("hidefloat")) {
            line3.setVisibility(View.GONE);
            mTvHide.setVisibility(View.GONE);
        }
    }

    private void showAllItems() {
        mTvAccount.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        mTvKefu.setVisibility(View.VISIBLE);
        line2.setVisibility(View.GONE);
        mTvGonglue.setVisibility(View.GONE);
        line3.setVisibility(View.VISIBLE);
        mTvHide.setVisibility(View.VISIBLE);
        mTvGift.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        removeTimerTask();
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
////			ImageUtils.loadGit(mIvFloatLogo,AppConstants.float_img,AppConfig.resourceId(mContext, "platform_float", "drawable"));
//			loadFloat();
//			mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!canTouch) {
                    return true;
                }
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();

                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    mDraging = true;
                    // 更新浮动窗口位置参数
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    if (mWmParams.y < ScreenUtils.dp2px(KLSDK.getInstance().getContext(), 34)) {
                        mWmParams.y = ScreenUtils.dp2px(KLSDK.getInstance().getContext(), 34);
                    }
                    mWindowManager.updateViewLayout(this, mWmParams);
                    mLlFloatMenu.setVisibility(View.GONE);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mWmParams.x >= mScreenWidth / 2) {
                    mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mWmParams.x = 0;
                    mIsRight = false;
                }
                refreshFloatMenu(mIsRight, true);
                mWindowManager.updateViewLayout(this, mWmParams);
                // 初始化
                mTouchStartX = mTouchStartY = 0;
                timerForHide();
                break;
        }
        return false;
    }

    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        LogUtil.i("隐藏悬浮窗 hide");
        setVisibility(View.GONE);
        Message message = mTimerHandler.obtainMessage();
        message.what = HANDLER_TYPE_HIDE_LOGO;
        mTimerHandler.sendMessage(message);
        removeTimerTask();
    }

    /**
     * 显示悬浮窗
     */
    public void show() {
        try {
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
                if (mShowLoader) {
//				ImageUtils.loadImage(mIvFloatLogo,AppConstants.float_img, AppConfig.resourceId(mContext, "kl_float", "mipmap"));
//				ImageUtils.loadGit(mIvFloatLogo, AppConstants.float_img, AppConfig.resourceId(getContext(), "platform_float", "drawable"));
//                    loadFloat();
                    mWmParams.alpha = 1f;
                    mWindowManager.updateViewLayout(this, mWmParams);

                    timerForHide();

                    mShowLoader = false;
                }
            } else {
                timerForHide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新float view menu
     *
     * @param right
     */
    private void refreshFloatMenu(boolean right, boolean show) {

        int padding = dp2px(30);

        if (right) {
            //设置悬浮球图标的位置
            setGravityForBall(Gravity.RIGHT);
            //在右边时,左0,右25dp:
            setPaddingInPartitionView(0, padding);
//			setAnimation(false);
        } else {
            //设置悬浮球图标的位置
            setGravityForBall(Gravity.LEFT);
            //在左边时,左25dp,右0:
            setPaddingInPartitionView(padding, 0);
//			setAnimation(false);
        }
    }

    /**
     * dp 转 px
     */
    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources()
                        .getDisplayMetrics());
    }

    /**
     * 设置悬浮球图标的位置
     *
     * @param gravity 位置
     *                <p>
     *                传入示例:Gravity.RIGHT
     **/
    private void setGravityForBall(int gravity) {
        LayoutParams params = (LayoutParams) mIvFloatLogo
                .getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        params.gravity = gravity;
        mIvFloatLogo.setLayoutParams(params);
        LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo
                .getLayoutParams();
        paramsFlFloat.gravity = gravity;
        mFlFloatLogo.setLayoutParams(paramsFlFloat);
    }

    /**
     * 设置悬浮球图标的位置
     *
     * @param left_padding   左边的间距
     * @param rightn_padding 右边的间距
     *                       <p>
     *                       传入的参数需要传入px单位, 若为dp,需要先转换.
     **/
    private void setPaddingInPartitionView(int left_padding, int rightn_padding) {
        LinearLayout.LayoutParams plv = (LinearLayout.LayoutParams) left_view
                .getLayoutParams();
        plv.leftMargin = left_padding;
        left_view.setLayoutParams(plv);

        LinearLayout.LayoutParams prv = (LinearLayout.LayoutParams) right_view
                .getLayoutParams();
        prv.leftMargin = rightn_padding;
        right_view.setLayoutParams(prv);
    }


    /**
     * 定时隐藏float view
     */
    private void timerForHide() {
        mCanHide = true;

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

        if (mCanHide) {
            mTimer.schedule(mTimerTask, 5000, 1000);
        }
    }

    /**
     * 是否Float view
     */
    public void destroy() {
        hide();
        removeFloatView();
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

    private void turnToIntent(String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(mContext, "此功能暂未开通", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.setClass(mContext, KLUserInfoActivity.class);
        mContext.startActivity(intent);
    }

    public void setAnimation(boolean show) {
        TranslateAnimation translateAnimation = null;
        RotateAnimation rotateAnimation = null;
        if (show) {
            if (mIsRight) {
                rotateAnimation = new RotateAnimation(270f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                translateAnimation = new TranslateAnimation(mIvFloatLogo.getLeft(), mIvFloatLogo.getLeft(), 0, 0);
            } else {
                rotateAnimation = new RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                translateAnimation = new TranslateAnimation(mIvFloatLogo.getLeft(), mIvFloatLogo.getLeft(), 0, 0);
            }
        } else {
            if (mIsRight) {
                rotateAnimation = new RotateAnimation(0f, 295f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                translateAnimation = new TranslateAnimation(mIvFloatLogo.getLeft(), mIvFloatLogo.getLeft() + float_logo_width / 2, 0, 0);
            } else {
                rotateAnimation = new RotateAnimation(0f, 65f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                translateAnimation = new TranslateAnimation(mIvFloatLogo.getLeft(), mIvFloatLogo.getLeft() - float_logo_width / 2, 0, 0);
            }
//			rotateRight = mIsRight;
        }

        AnimationSet animationSet = new AnimationSet(true);
//		rotateAnimation.setDuration(500);
//		rotateAnimation.setFillAfter(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);
        animationSet.setDuration(300);
        rootFloatView.startAnimation(animationSet);
    }

    public void loadFloat() {
        if (TextUtils.isEmpty(AppConstants.float_img)) {
//            ImageUtils.loadGit(mIvFloatLogo, AppConfig.resourceId(getContext(), "kl_float_gif", "mipmap"));
            rootFloatView.setVisibility(View.GONE);
            LogUtil.i("loadFloat：不显示浮窗");
        } else {
            if (AppConstants.float_img.endsWith(".gif")) {
//				ImageUtils.loadImage(mIvFloatLogo, AppConstants.float_img);
                ImageUtils.loadGit(mIvFloatLogo, AppConstants.float_img);
            } else {
                ImageUtils.loadImage(mIvFloatLogo, AppConstants.float_img);
            }
        }
    }
}


