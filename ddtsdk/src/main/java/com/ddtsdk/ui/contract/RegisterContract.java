package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;

/**
 * Created by CZG on 2020/4/21.
 */
public class RegisterContract {
    public interface View extends BaseContract.BaseView {
        void registerSuccess(String msg);
        void getCodeSuccess();
        void showMsg(String msg);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void register(Context context, String username, String password, String verifycode, int reg_type);
        void phoneRegister(Context context, String mobile, String password, String verifycode);
        void getCode(Context context, String mobile);
    }
}
