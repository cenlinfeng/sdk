package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.model.protocol.bean.BuyPlatformBean;
import com.ddtsdk.model.protocol.bean.PayMsg;

import java.util.Map;

public class BuyPlatformContract {
    public interface View extends BaseContract.BaseView {
        void SetPlatformConfig(BuyPlatformBean initConfig);
        void platformToPaySuccess(PayMsg payMsg);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getPlatformConfig(Context context);
        void PlatformToPay(Context context, Map<String,String> prams);
    }
}
