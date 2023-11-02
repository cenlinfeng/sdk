package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.protocol.bean.LoginMessage;

/**
 * Created by CZG on 2020/4/17.
 */
public class LoginContract {
    public interface View extends BaseContract.BaseView {
        void loginSuccess(LoginMessage loginMessage);
        void registerSuccess(LoginMessage loginMessage);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void login(Context context, BaseUserData userData);
        void register(Context context, String username, String password, String verifycode, String visitor);
    }
}
