package com.ddtsdk.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseMvpFragment;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.activity.KLFirstLoginActivity;
import com.ddtsdk.ui.activity.KLForgetPasswordActivity;
import com.ddtsdk.ui.activity.KLLoginActivity;
import com.ddtsdk.ui.activity.KLUserInfoActivity;
import com.ddtsdk.ui.activity.RealNameActivity;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.ui.presenter.LoginPresenter;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.Utils;

import org.json.JSONArray;

/**
 * Created by CZG on 2020/4/20.
 * 登录页
 */
public class KLAccountLoginFragment extends BaseMvpFragment<LoginContract.View, LoginPresenter> implements LoginContract.View {
    private EditText kl_account_et;
    private EditText kl_password_et;
    private Button kl_login;
    private TextView kl_forgetpassword;
    private ImageView kl_pwd_see;
    private CheckBox kl_accept_cb;
    private TextView kl_agreement_tv;

    private String mUserName;
    private String mPassWord;
    private boolean canSee = false;

    @Override
    protected String layoutName() {
        return Constants.login_account;
    }

    @Override
    protected void initView() {
        kl_login = findViewById(resourceId("kl_login", "id"));
        kl_account_et = findViewById(resourceId("kl_account_et", "id"));
        kl_password_et = findViewById(resourceId("kl_password_et", "id"));
        kl_pwd_see = findViewById(resourceId("kl_pwd_see", "id"));
        kl_forgetpassword = findViewById(resourceId("kl_forgetpassword", "id"));
        kl_agreement_tv = findViewById(resourceId("kl_agreement_tv", "id"));
        kl_accept_cb = findViewById(resourceId("kl_accept_cb", "id"));
        kl_accept_cb.setChecked(false);

        if (!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPassWord)) {
            kl_account_et.setText(mUserName);
            kl_password_et.setText(mPassWord);
            BaseUserData userData = new BaseUserData();
            userData.setUname(mUserName);
            userData.setPwd(mPassWord);
            mPresenter.login(getActivity(), userData);
        }
    }


    @Override
    protected void initListener() {
        kl_pwd_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置密码可见
                if (canSee) {
                    kl_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    kl_pwd_see.setImageResource(resourceId("kl_icon_nosee", "mipmap"));
                    canSee = false;
                } else {
                    kl_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    kl_pwd_see.setImageResource(resourceId("kl_icon_see", "mipmap"));
                    canSee = true;
                }
            }
        });

        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录
                mUserName = kl_account_et.getText().toString();
                mPassWord = kl_password_et.getText().toString();
                if (TextUtils.isEmpty(mUserName)) {
                    showToastMsg("账号不能为空！");
                    return;

                }
                if (TextUtils.isEmpty(mPassWord)) {
                    showToastMsg("密码不为空！");
                    return;
                }

                if (!kl_accept_cb.isChecked()) {
                    showToastMsg("请勾选用户隐私协议方可继续");
                    return;
                }
                LogUtil.d("onClick: " + kl_login.isEnabled());
                BaseUserData userData = new BaseUserData();
                userData.setUname(mUserName);
                userData.setPwd(mPassWord);
                mPresenter.login(getActivity(), userData);
                PayPointReport.getInstance().pushPoint(22);
            }
        });

        kl_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLForgetPasswordActivity.startThisActivity(getActivity(), KLForgetPasswordActivity.TYPE_FORGET);
                PayPointReport.getInstance().pushPoint(21);
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
//        AppConstants.ddt_ver_id = Utils.getAgent(getActivity());
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    public void setQuickValue(String uname, String pwd) {
        mUserName = uname;
        mPassWord = pwd;
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
    public void registerSuccess(final LoginMessage loginMessage) {

    }

    @Override
    public void loginSuccess(final LoginMessage loginMessage) {
        showLoadingView();//显示 进入游戏  提示条
        if (getActivity() instanceof KLFirstLoginActivity) {
            ((KLFirstLoginActivity) getActivity()).loginSuccess(loginMessage);
        } else if (getActivity() instanceof KLLoginActivity) {
            ((KLLoginActivity) getActivity()).loginSuccess(loginMessage);
        }
    }

    //登录完成显示悬浮进度界面
    private void showLoadingView() {
        LogUtil.i("显示悬浮进度界面");
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

}