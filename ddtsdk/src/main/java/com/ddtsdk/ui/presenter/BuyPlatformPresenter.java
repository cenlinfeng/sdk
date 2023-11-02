package com.ddtsdk.ui.presenter;

import android.content.Context;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.protocol.bean.BuyPlatformBean;
import com.ddtsdk.model.protocol.bean.PayMsg;
import com.ddtsdk.ui.contract.BuyPlatformContract;

import java.util.Map;

public class BuyPlatformPresenter extends BasePresenter<BuyPlatformContract.View>
        implements BuyPlatformContract.Presenter<BuyPlatformContract.View> {
    @Override
    public void getPlatformConfig(Context context) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_BUY_PLATFORM_INIT, null,
                BuyPlatformBean.class, new HttpRequestClient.ResultHandler<BuyPlatformBean>(context) {
                    @Override
                    public void onSuccess(BuyPlatformBean buyPlatformBean) {
                        mView.get().SetPlatformConfig(buyPlatformBean);
                    }
                });
    }

    @Override
    public void PlatformToPay(Context context, Map<String, String> prams) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_BUY_PLATFORM_PAY, prams,
                PayMsg.class, new HttpRequestClient.ResultHandler<PayMsg>(context) {
                    @Override
                    public void onSuccess(PayMsg payMsg) {
                        mView.get().platformToPaySuccess(payMsg);
                    }
                });
    }


}
