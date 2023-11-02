package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;

/**
 * Created by CZG on 2020/5/21.
 */
public class BindPhoneContract {
    public interface View extends BaseContract.BaseView {
        void getCodeSuccess();
        void bindPhoneSuccess();
        void showMsg(String msg);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCode(Context context, String mobile);
        void bindPhone(Context context, String mobile,String verification);
    }
}
