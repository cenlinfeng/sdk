package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.ui.presenter.LoginPresenter;
import com.ddtsdk.utils.CreatBitmapUtil;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.ToastUtils;

import org.json.JSONArray;

/**
 * Created by CZG on 2020/5/20.
 * 游客注册页
 */
public class KLVisitorRegisterActivity extends BaseMvpActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    public static final int VISITOR_REQUEST_CODE = 200;

    private TextView kl_account_tv;
    private TextView kl_password_tv;
    private CheckBox kl_accept_cb;
    private TextView kl_agreement_tv;
    private TextView kl_bind_phone;
    private TextView kl_login;
    private LinearLayout kl_main_view;

    private String float_menu;

    private String mAccount;
    private String mPassword;

    private LoginMessage mLoginMessage;

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, KLVisitorRegisterActivity.class);
        activity.startActivityForResult(intent,VISITOR_REQUEST_CODE);
    }

    @Override
    protected void initView() {
        kl_account_tv = findViewById(resourceId("kl_account_tv", "id"));
        kl_password_tv = findViewById(resourceId("kl_password_tv", "id"));
        kl_accept_cb = findViewById(resourceId("kl_accept_cb", "id"));
        kl_agreement_tv = findViewById(resourceId("kl_agreement_tv", "id"));
        kl_bind_phone = findViewById(resourceId("kl_bind_phone", "id"));
        kl_login = findViewById(resourceId("kl_login", "id"));
        kl_main_view = findViewById(resourceId("kl_main_view", "id"));
        kl_accept_cb.setChecked(false);
//        GradientDrawable drawable =  (GradientDrawable)kl_login.getBackground();
//        drawable.setColor(getResources().getColor(resourceId("color_007cf1", "color")));
//        kl_login.setBackground(drawable);
    }

    @Override
    protected void initData() {
        // 一键注册
        mPresenter.register(this, "", "", "", "1");
    }

    @Override
    protected void initListener() {
        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!kl_accept_cb.isChecked()) {
                    Toast.makeText(KLVisitorRegisterActivity.this, "请勾选用户隐私协议方可继续",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                loginSuccess(mLoginMessage);
                toRealNameView();
                finish();
            }
        });
        kl_bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLBindPhoneActivity.startThisActivity(KLVisitorRegisterActivity.this);
            }
        });
        kl_agreement_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("url", AppConstants.agree);
                intent.setClass(KLVisitorRegisterActivity.this, KLUserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected String layoutName() {
        if (AppConstants.pack_model == 1) return Constants.visitor_register_hb;
        return Constants.visitor_register;
    }

    @Override
    public void loginSuccess(final LoginMessage loginMessage) {
        if (loginMessage == null){
            return;
        }
        float_menu = loginMessage.getFloat_menu_new();
        String platformUrl = loginMessage.getFloat_url();

        AppConfig.isShow = true;
//		KLSDK.saveUserToSd(KLFirstLoginActivity.this);
        showLoadingView();    //显示 进入游戏  提示条
        if (!TextUtils.isEmpty(platformUrl)){
            FloatUtlis.getInstance().setFloatItems(platformUrl);
        } else if (!TextUtils.isEmpty(float_menu)) {
            FloatUtlis.getInstance().hideFloatItems(float_menu);
        }
        BaseKLSDK.getInstance().onLoadH5Url();
    }

    @Override
    public void registerSuccess(final LoginMessage loginMessage) {
        AccountManager.setLoginMessage(loginMessage);
        AccountManager.getInstance(this).setUserData(loginMessage);
        AccountManager.getInstance(this).addHistoryUserData(loginMessage.getUid(),loginMessage.getUname(), loginMessage.getPwd(),"");
        mAccount = loginMessage.getUname();
        mPassword = loginMessage.getPwd();
        kl_account_tv.setText("账号：" + mAccount);
        kl_password_tv.setText("密码：" + mPassword);
        mLoginMessage = loginMessage;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CreatBitmapUtil.getInstance(KLVisitorRegisterActivity.this).saveUserViewBitmap(kl_main_view);
            }
        }, 500);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toRealNameView() {
        /**
         * 登录成功
         * AppConstants.isautonym == 0 && AppConfig.forceautonym != 0 表示未实名并且需要进行实名的用户
         * 弹出实名认证界面框
         */
        if (AppConstants.isautonym == 0 && AppConstants.forceautonym != 0) {
            RealNameActivity.startThisActivity(this);
        } else {
            BaseKLSDK.getInstance().wrapLoginInfo();
            OnLineTimeRequest.get().onlineTime();
        }
    }

    //登录完成显示悬浮进度界面
    private void showLoadingView() {
        LogUtil.i("显示悬浮进度界面 - Visitor");
        FloatUtlis.getInstance().showLoadingView();
    }

}
