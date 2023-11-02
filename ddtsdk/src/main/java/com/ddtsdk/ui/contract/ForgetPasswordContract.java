package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;

/**
 * Created by CZG on 2020/4/21.
 */
public class ForgetPasswordContract {
    public interface View extends BaseContract.BaseView {
        void showCodeMsg(String msg);
        void resetSuccess(String msg);
    }
    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCode(Context context, String mobile);
        void resetPwd(Context context, String mobile, String verifyCode, String pwd);
    }
}
