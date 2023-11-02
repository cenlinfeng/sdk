package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;

/**
 * Created by CZG on 2020/4/21.
 */
public class RealNameContract {
    public interface View extends BaseContract.BaseView {
        void certificateSuccess();
        void certificateFail();
    }
    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void certificate(Context context, String uName, String idCard);
        boolean checkIdValidation(String text);
    }
}
