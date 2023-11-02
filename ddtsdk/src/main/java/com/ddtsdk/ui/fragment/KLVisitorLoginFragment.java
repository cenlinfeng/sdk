package com.ddtsdk.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseMvpFragment;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.ui.activity.KLBindPhoneActivity;
import com.ddtsdk.ui.activity.KLFirstQuickLoginActivity;
import com.ddtsdk.ui.activity.KLUserInfoActivity;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.ui.presenter.LoginPresenter;
import com.ddtsdk.utils.CreatBitmapUtil;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;

import org.json.JSONArray;

/**
 * Created by CZG on 2020/5/20.
 * 游客注册页
 */
public class KLVisitorLoginFragment extends BaseMvpFragment<LoginContract.View, LoginPresenter> implements LoginContract.View {

    public static final int VISITOR_REQUEST_CODE = 200;

    private TextView kl_account_tv;
    private TextView kl_password_tv;
    private com.ddtsdk.ui.view.KLCheckBoxView kl_accept_cb;
    private TextView kl_agreement_tv;
    private TextView kl_bind_phone;
    private TextView kl_login;
    private LinearLayout kl_main_view;
    private static View act_main_view;

    private JSONArray float_menu;

    private String mAccount;
    private String mPassword;

    private LoginMessage mLoginMessage;

    public static KLVisitorLoginFragment KLVisitorLoginFragment(View view){
        act_main_view = view;
        return new KLVisitorLoginFragment();
    }

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, KLVisitorLoginFragment.class);
        activity.startActivityForResult(intent,VISITOR_REQUEST_CODE);
    }

    @Override
    protected void initView() {
        kl_account_tv = findViewById(resourceId("kl_account_tv", "id"));
        kl_password_tv = findViewById(resourceId("kl_password_tv", "id"));
        kl_accept_cb = (com.ddtsdk.ui.view.KLCheckBoxView)findViewById(resourceId("kl_accept_cb", "id"));
        kl_agreement_tv = findViewById(resourceId("kl_agreement_tv", "id"));
        kl_bind_phone = findViewById(resourceId("kl_bind_phone", "id"));
        kl_login = findViewById(resourceId("kl_login", "id"));
        kl_main_view = findViewById(resourceId("kl_main_view", "id"));
        kl_accept_cb.setChecked(false);
        kl_agreement_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
//        GradientDrawable drawable =  (GradientDrawable)kl_login.getBackground();
//        drawable.setColor(getResources().getColor(resourceId("color_007cf1", "color")));
//        kl_login.setBackground(drawable);
    }

    @Override
    protected void initData() {


    }

    public void register(){
        // 一键注册
        if (mLoginMessage == null){
            mPresenter.register(getActivity(), "", "", "", "1");
        }
    }

    @Override
    protected void initListener() {
        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!kl_accept_cb.isChecked()) {
                    Toast.makeText(getActivity(), "请勾选用户隐私协议方可继续",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (TextUtils.isEmpty(mAccount)){
                    ToastUtils.showShort(KLVisitorRegisterActivity.this,"账号密码还未生成");
                    return;
                }*/
                loginSuccess(mLoginMessage);
            }
        });
        kl_bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLBindPhoneActivity.startThisActivity(getActivity());
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
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.fragment_visitor;
    }

    @Override
    public void loginSuccess(final LoginMessage loginMessage) {
        showLoadingView();    //显示 进入游戏  提示条
        ((KLFirstQuickLoginActivity)getActivity()).loginSuccess(mLoginMessage);
    }

    @Override
    public void registerSuccess(final LoginMessage loginMessage) {
        AccountManager.setLoginMessage(loginMessage);
        AccountManager.getInstance(getActivity()).setUserData(loginMessage);
        AccountManager.getInstance(getActivity()).addHistoryUserData(loginMessage.getUid(),loginMessage.getUname(), loginMessage.getPwd(),"");
        mAccount = loginMessage.getUname();
        mPassword = loginMessage.getPwd();
        kl_account_tv.setText("账号：" + mAccount);
        kl_password_tv.setText("密码：" + mPassword);
        mLoginMessage = loginMessage;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (act_main_view == null){
                    CreatBitmapUtil.getInstance(getActivity()).saveUserViewBitmap(kl_main_view);
                }
                else {
                    CreatBitmapUtil.getInstance(getActivity()).saveUserViewBitmap(act_main_view);
                }
            }
        }, 500);

    }

    //登录完成显示悬浮进度界面
    private void showLoadingView() {
        LogUtil.i("显示悬浮进度界面 - Visitor");
        FloatUtlis.getInstance().showLoadingView();
    }

}
