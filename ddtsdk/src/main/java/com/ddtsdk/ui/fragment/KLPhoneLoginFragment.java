package com.ddtsdk.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseMvpFragment;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.activity.KLFirstLoginActivity;
import com.ddtsdk.ui.activity.KLLoginActivity;
import com.ddtsdk.ui.activity.KLUserInfoActivity;
import com.ddtsdk.ui.activity.KLVisitorRegisterActivity;
import com.ddtsdk.ui.activity.RealNameActivity;
import com.ddtsdk.ui.contract.PhoneLoginContract;
import com.ddtsdk.ui.presenter.PhoneLoginPresenter;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.utils.Utils;

import org.json.JSONArray;

/**
 * Created by CZG on 2020/4/20.
 * 登录页
 */
public class KLPhoneLoginFragment extends BaseMvpFragment<PhoneLoginContract.View, PhoneLoginPresenter> implements PhoneLoginContract.View {
    private EditText kl_phone_et;
    private EditText kl_code_et;
    private TextView kl_code;
    private Button kl_login;
    private CheckBox kl_accept_cb;
    private TextView kl_agreement_tv;

    private String mPhone;
    private String mCode;

    private CountDownTimer mCountDownTimer;

    private LoginMessage mLoginMessage;

    private String float_menu;

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, KLPhoneLoginFragment.class);
        activity.startActivity(intent);
    }

    @Override
    protected String layoutName() {
        return Constants.login_phone;
    }

    @Override
    protected void initView() {
        kl_login = findViewById(resourceId("kl_login", "id"));
        kl_phone_et = findViewById(resourceId("kl_phone_et", "id"));
        kl_code_et = findViewById(resourceId("kl_code_et", "id"));
        kl_code = findViewById(resourceId("kl_code", "id"));
        kl_accept_cb = findViewById(resourceId("kl_accept_cb", "id"));
        kl_agreement_tv = findViewById(resourceId("kl_agreement_tv", "id"));
        kl_accept_cb.setChecked(false);
        initCountDownTimer();
    }

    private void initCountDownTimer() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = "发送" + millisUntilFinished / 1000 + "s";
                kl_code.setText(text);
                kl_code.setClickable(false);
            }

            public void onFinish() {
                String text = "重新发送";
                kl_code.setText(text);
                kl_code.setClickable(true);
            }
        };
    }

    @Override
    protected void initListener() {
        kl_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = kl_phone_et.getText().toString().trim();
                if (TextUtils.isEmpty(mPhone)) {
                    Toast.makeText(getActivity(), "请输入手机号",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.getCode(getActivity(), mPhone);
                PayPointReport.getInstance().pushPoint(17);
            }
        });

        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录
                mPhone = kl_phone_et.getText().toString();
                mCode = kl_code_et.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    showToastMsg("手机号不能为空！");
                    return;

                }
                if (TextUtils.isEmpty(mCode)) {
                    showToastMsg("验证码不为空！");
                    return;
                }
                if (!kl_accept_cb.isChecked()) {
                    showToastMsg("请勾选用户隐私协议方可继续");
                    return;
                }
                LogUtil.d("onClick: " + kl_login.isEnabled());
                loginButtonSettting(false);  //点击后,禁用按钮;当收到登录回调后,再恢复按钮可点击
                mPresenter.login(getActivity(), mPhone, mCode);
                PayPointReport.getInstance().pushPoint(18);
            }
        });

        kl_agreement_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("url", AppConstants.agree);
                intent.setClass(getActivity(), KLUserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
//		AppConstants.ddt_ver_id = Utils.getAgent(getActivity());
    }

    @Override
    protected PhoneLoginPresenter createPresenter() {
        return new PhoneLoginPresenter();
    }

    /**
     * 登录按钮的防抖
     */
    private void loginButtonSettting(Boolean enable) {
        kl_login.setEnabled(enable);
        if (enable) {
            kl_login.setText("登录");
        } else {
            kl_login.setText("正在登录...");
        }
    }

    private void toRealNameView() {
        /**
         * 登录成功
         * AppConstants.isautonym == 0 && AppConfig.forceautonym != 0 表示未实名并且需要进行实名的用户
         * 弹出实名认证界面框
         */
        if (AppConstants.isautonym == 0 && AppConstants.forceautonym != 0) {
            RealNameActivity.startThisActivity(getActivity());
        } else {
            BaseKLSDK.getInstance().wrapLoginInfo();
            OnLineTimeRequest.get().onlineTime();
        }
    }

    @Override
    public void loginSuccess(final LoginMessage loginMessage) {
        mLoginMessage = loginMessage;
        float_menu = loginMessage.getFloat_menu_new();
        String platformUrl = loginMessage.getFloat_url();

        loginButtonSettting(true);
        if (!TextUtils.isEmpty(float_menu)) {
            FloatUtlis.getInstance().hideFloatItems(float_menu);
        }
        AppConfig.isShow = true;
        showLoadingView();    //显示 进入游戏  提示条
        if (getActivity() instanceof KLFirstLoginActivity) {
            ((KLFirstLoginActivity) getActivity()).loginSuccess(loginMessage);
        } else if (getActivity() instanceof KLLoginActivity) {
            ((KLLoginActivity) getActivity()).loginSuccess(loginMessage);
        }
    }

    @Override
    public void showLoginFail() {
        loginButtonSettting(true);
    }

    @Override
    public void getCodeSuccess() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showShort(getActivity(), msg);
    }

    //登录完成显示悬浮进度界面
    private void showLoadingView() {
        LogUtil.i("显示悬浮进度界面-phone");
        FloatUtlis.getInstance().showLoadingView();
    }

    //显示实名验证界面
    private void showVerifyView(String url) {
//		LogUtil.e("showVerifyView 实名验证界面");
        // 实名验证
        url = Base64.decode(url);
        if (TextUtils.isEmpty(url)) return;  //为空直接跳出该方法
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.setClass(getActivity(), KLUserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroyView();
    }
}