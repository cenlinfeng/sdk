package com.ddtsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.model.protocol.params.YiLoginParams;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.ui.activity.KLFirstLoginActivity;
import com.ddtsdk.ui.activity.KLLoginActivity;
import com.ddtsdk.ui.activity.KLRegisterActivity;
import com.ddtsdk.ui.activity.RealNameActivity;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.listener.ClickEventListener;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.netease.nis.quicklogin.utils.LoginUiHelper;

/**
 * Created by CZG on 2020/11/30
 */
public class QuickLoginUtils {

    private QuickLogin quickLogin;
    private static QuickLoginUtils instance;
    private QuickLoginListener mQuickLoginListener;

    public static QuickLoginUtils getInstance() {
        if (instance == null){
            instance = new QuickLoginUtils();
        }
        return instance;
    }

    public void init(Context context) {

        quickLogin = QuickLogin.getInstance(context, Utils.getWYBusinessId(context));
        quickLogin.setDebugMode(true);
    }

    public void prefetchNumber(final Activity context) {
        quickLogin.setUnifyUiConfig(getUiConfig(context));
        quickLogin.prefetchMobileNumber(new QuickLoginPreMobileListener() {
            @Override
            public void onGetMobileNumberSuccess(String YDToken, String mobileNumber) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(context,"预取号成功,可一键登录!");
                        quickLogin(context);
                    }
                });
            }

            @Override
            public void onGetMobileNumberError(final String YDToken, final String msg) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(context,"蜂窝网下点击快速注册，可快速获取手机号");
                        Log.e("onGetMobileNumberError", "YDToken=" + YDToken + "  msg=" + msg);
                        KLRegisterActivity.startThisActivity(context,KLRegisterActivity.TYPE_PHONE);
//                        context.finish();
                    }
                });
            }
        });
    }

    private void quickLogin(final Activity context) {
        quickLogin.onePass(new QuickLoginTokenListener() {
            @Override
            public void onGetTokenSuccess(String YDToken, String accessCode) {
                loginToServer(context, YDToken, accessCode);
                Log.d("quickLogin",YDToken+"+"+accessCode);
                PayPointReport.getInstance().pushPoint(11);
            }

            @Override
            public void onGetTokenError(String YDToken, String msg) {
                ToastUtils.showShort(context,"获取运营商授权码失败:"+msg);
                KLRegisterActivity.startThisActivity(context,KLRegisterActivity.TYPE_PHONE);
            }
        });
    }

    private void loginToServer(final Activity context, String token, String code){
        AppConstants.regType = "phoneReg";
        AdManager.getInstance().logRegisterReport(context, "1", "success");
        YiLoginParams yiLoginParams = new YiLoginParams(token, "", context.getPackageName(), code);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_WYYILOGIN, yiLoginParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                ToastUtils.showShort(KLSDK.getInstance().getContext(), "一键登录成功");

                quickLogin.quitActivity();
                AccountManager.setLoginMessage(loginMessage);
                AccountManager.getInstance(context).setUserData(loginMessage);
                AccountManager.getInstance(context).addHistoryUserData(loginMessage.getUid(),loginMessage.getUname(), loginMessage.getPwd(),loginMessage.getMobile());
                if ("1".equals(loginMessage.getRepregister())){
                    AdManager.getInstance().register(context);
                }
                else {
                    AdManager.getInstance().logRegisterReport(context, "2", "error");
                }
                if (mQuickLoginListener != null){
                    mQuickLoginListener.quickLoginSuccess(loginMessage);
                }
//                toRealNameView();

            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (!baseBean.isResult()) {
                    AdManager.getInstance().logRegisterReport(context, "2", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                AdManager.getInstance().logRegisterReport(context, "2", "error");
                super.onFailure(t);
            }
        });
    }

    public void setQuickLoginListener(QuickLoginListener quickLoginListener) {
        this.mQuickLoginListener = quickLoginListener;
    }

    private UnifyUiConfig getUiConfig(Activity context) {
        String logo_path = "ic_app";
        if (AppConstants.pack_model == 1){
            logo_path = "ic_app_hb";
        }
        TextView go_login_tv = new TextView(context);
        go_login_tv.setText("已有账号, 去登录");
        go_login_tv.setTextSize(10f);
        go_login_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
        go_login_tv.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,resourceId(context,"kl_icon_arrow_right","mipmap")),null);
        go_login_tv.setCompoundDrawablePadding(3);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.rightMargin = ScreenUtils.dp2px(context,25f);
        lp.bottomMargin = ScreenUtils.dp2px(context,20f);
//        lp.topMargin = ScreenUtils.dp2px(context,260f);
        go_login_tv.setLayoutParams(lp);

        int X_OFFSET = 0;
        int BOTTOM_OFFSET = 0;
        return new UnifyUiConfig.Builder()
                // 状态栏
                .setStatusBarDarkColor(true)
                // 设置导航栏
                .setNavigationTitle("账号注册")
                .setNavigationTitleColor(Color.WHITE)
                .setNavigationBackgroundColor(Color.parseColor(AppConstants.colorPrimary))
                .setNavigationIcon("kl_icon_back")
                .setNavigationBackIconWidth(15)
                .setNavigationBackIconHeight(15)
                .setNavigationHeight(40)
                .setHideNavigation(false)
                .setClickEventListener(new ClickEventListener() {
                    @Override
                    public void onClick(int viewType, int code) {
                        if (viewType == 3){
                            quickLogin.quitActivity();
                        }
                    }
                })

                // 设置logo
                .setLogoIconName(logo_path)
                .setLogoWidth(75)
                .setLogoHeight(75)
                .setLogoXOffset(X_OFFSET)
                .setLogoTopYOffset(15)
                .setHideLogo(false)
                //手机掩码
                .setMaskNumberColor(Color.parseColor("#707070"))
                .setMaskNumberSize(25)
                .setMaskNumberXOffset(X_OFFSET)
                .setMaskNumberTopYOffset(100)
                .setMaskNumberBottomYOffset(BOTTOM_OFFSET)

                .setSloganSize(0)   // 认证品牌
                .setSloganColor(Color.parseColor("#999999"))
                .setSloganXOffset(X_OFFSET)
                .setSloganTopYOffset(500)
                .setSloganBottomYOffset(BOTTOM_OFFSET)

                .setLoginBtnText("一键注册")    // 登录按钮
                .setLoginBtnTextColor(Color.WHITE)
//                .setLoginBtnBackgroundRes("klorange_btn_style")
                .setLoginBtnBackgroundDrawable(new ColorDrawable(Color.parseColor(AppConstants.colorPrimary)))
                .setLoginBtnWidth(260)
                .setLoginBtnHeight(38)
                .setLoginBtnTextSize(15)
                .setLoginBtnXOffset(X_OFFSET)
                .setLoginBtnTopYOffset(165)
                .setLoginBtnBottomYOffset(BOTTOM_OFFSET)

                .addCustomView(go_login_tv, "go_login_tv", UnifyUiConfig.POSITION_IN_BODY, new LoginUiHelper.CustomViewListener() {
                    @Override
                    public void onClick(Context context, View view) {
                        quickLogin.quitActivity();
                        KLFirstLoginActivity.startThisActivity((Activity) context);
                        KLAppManager.getInstance().finishAllActivity(KLLoginActivity.class);
                        PayPointReport.getInstance().pushPoint(12);
                    }
                })

                .setPrivacyTextStart("登陆即同意")   // 隐私栏
                .setProtocolText("《用户隐私政策》")
                .setProtocolLink(AppConstants.agree)
                .setPrivacyTextEnd("")
                .setPrivacyTextColor(Color.parseColor("#999999"))
                .setPrivacyProtocolColor(Color.parseColor("#1e709b"))
                .setHidePrivacyCheckBox(true)
                .setPrivacyXOffset(X_OFFSET)
                .setPrivacyState(true)
                .setPrivacySize(9)
                .setPrivacyTopYOffset(200)
                .setPrivacyBottomYOffset(BOTTOM_OFFSET)
                .setPrivacyTextGravityCenter(true) // 协议详情页导航栏
                .setDialogMode(true, (int) (ScreenUtils.px2dp(context,ScreenUtils.getScreenWidth(context))*0.8), 330, 0, 50, false)
                .build(context);
    }

    private void toRealNameView() {
        /**
         * 登录成功
         * AppConstants.isautonym == 0 && AppConfig.forceautonym != 0 表示未实名并且需要进行实名的用户
         * 弹出实名认证界面框
         */
        if (AppConstants.isautonym == 0 && AppConstants.forceautonym != 0){
            RealNameActivity.startThisActivity((Activity) KLSDK.getInstance().getContext());
        } else {
            BaseKLSDK.getInstance().wrapLoginInfo();
            OnLineTimeRequest.get().onlineTime();
        }
    }

    public int resourceId(Context context,String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public interface QuickLoginListener{
        void quickLoginSuccess(LoginMessage loginMessage);
    }
}
