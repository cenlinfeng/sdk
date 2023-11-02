package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ddtsdk.BaseKLSDK;

import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.adapter.AccountHitoryAdapter;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.ui.presenter.LoginPresenter;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.PayPointReport;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/5/21.
 * 快速登录
 */
public class KLQuickLoginActivity extends BaseMvpActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    private TextView kl_login;
    private TextView kl_login_other;
    private ListView kl_account_list;

    private List<BaseUserData> mUserDataList = new ArrayList<>();
    private List<BaseUserData> mHistoryDataList = new ArrayList<>();
    private AccountHitoryAdapter mAdapter;

    private BaseUserData mUserData;

    private LoginMessage mLoginMessage;

    //跳转渠道登录页
    public static void startLoginActivity(Activity activity) {
        Log.e("H5", "跳转渠道登录页");
        Intent intent = new Intent(activity, KLQuickLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        kl_login = findViewById(resourceId("kl_login", "id"));
        kl_login_other = findViewById(resourceId("kl_login_other", "id"));
        kl_account_list = findViewById(resourceId("kl_account_list", "id"));

        mAdapter = new AccountHitoryAdapter(this, mUserDataList);
        kl_account_list.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mHistoryDataList = AccountManager.getInstance(this).getHistoryUserList();
        mUserDataList.add(mHistoryDataList.get(0));
        mUserData = mHistoryDataList.get(0);
    }

    @Override
    protected void initListener() {
        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(KLQuickLoginActivity.this, mUserData);
                PayPointReport.getInstance().pushPoint(9);
            }
        });
        kl_login_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLLoginActivity.startThisActivity(KLQuickLoginActivity.this);
                finish();
                PayPointReport.getInstance().pushPoint(10);
            }
        });
        mAdapter.setIAccountOnClickListener(new AccountHitoryAdapter.IAccountOnClickListener() {
            @Override
            public void onItemClick(View view, int position, BaseUserData data) {
                for (int i = 0; i < mHistoryDataList.size(); i++) {
                    if (TextUtils.equals(data.getUid(), mHistoryDataList.get(i).getUid())) {
                        mHistoryDataList.remove(i);
                        mHistoryDataList.add(0, data);
                        break;
                    }
                }
                if (mUserDataList.size() == 1) {
                    mUserDataList.clear();
                    mUserDataList.addAll(mHistoryDataList);
                } else if (mUserDataList.size() > 1) {
                    mUserDataList.clear();
                    mUserDataList.add(mHistoryDataList.get(0));
                }
                mAdapter.notifyDataSetChanged();
                mUserData = data;
            }
        });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.quick_login;
    }

    @Override
    public void loginSuccess(final LoginMessage loginMessage) {
        mLoginMessage = loginMessage;

        String platformUrl = loginMessage.getFloat_url();

        AppConfig.isShow = true;
        showLoadingView();    //显示 进入游戏  提示条
        if (!TextUtils.isEmpty(platformUrl)) {
            FloatUtlis.getInstance().setFloatItems(platformUrl);
        } else if (!TextUtils.isEmpty(loginMessage.getFloat_menu_new())) {
            FloatUtlis.getInstance().hideFloatItems(loginMessage.getFloat_menu_new());
        }
//        if (!TextUtils.isEmpty(AppConstants.dologin_h5)){
//            BaseKLSDK.getInstance().onLoadH5Url();
//        }
        BaseKLSDK.getInstance().onLoadH5Url();
        toRealNameView();
        finish();
    }

    private void toRealNameView() {
        /**
         * 登录成功
         * AppConstants.isautonym == 0 && AppConfig.forceautonym != 0 表示未实名并且需要进行实名的用户
         * 弹出实名认证界面框
         */
        Log.e("H5", "登录成功,弹起实名认证框");
        if (AppConstants.isautonym == 0 && AppConstants.forceautonym != 0) {
            RealNameActivity.startThisActivity(this);
        } else {
            BaseKLSDK.getInstance().wrapLoginInfo();
            OnLineTimeRequest.get().onlineTime();
        }
    }

    @Override
    public void registerSuccess(LoginMessage loginMessage) {

    }

    //登录完成显示悬浮进度界面
    private void showLoadingView() {
        LogUtil.i("显示悬浮进度界面 - QuickLogin");
        FloatUtlis.getInstance().showLoadingView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
