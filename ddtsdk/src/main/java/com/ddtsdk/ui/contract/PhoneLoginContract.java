package com.ddtsdk.ui.contract;

import android.app.Activity;
import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.protocol.bean.LoginMessage;

/**
 * Created by CZG on 2020/4/17.
 */
public class PhoneLoginContract {
    public interface View extends BaseContract.BaseView {
        void loginSuccess(LoginMessage loginMessage);
        void showLoginFail();
        void getCodeSuccess();
        void showMsg(String msg);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void login(Context context, String userName, String passWord);
        void getCode(Context context, String mobile);
    }
}
