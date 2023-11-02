package com.ddtsdk.ui.presenter;

import android.content.Context;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.model.protocol.params.PhoneParams;
import com.ddtsdk.model.protocol.params.SmsParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.ui.contract.BindPhoneContract;

/**
 * Created by CZG on 2020/5/21.
 */
public class BindPhonePresenter extends BasePresenter<BindPhoneContract.View> implements BindPhoneContract.Presenter<BindPhoneContract.View>{


    @Override
    public void getCode(Context context, String mobile) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_SMS, new SmsParams(mobile, SmsParams.BINDMOBILE), Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object obj) {
                mView.get().getCodeSuccess();
            }

            @Override
            public void onResponse(BaseBean baseBean) {
                mView.get().showMsg(baseBean.getMsg());
            }

        });
    }

    @Override
    public void bindPhone(Context context, String mobile, String verification) {
        PhoneParams phoneParams = new PhoneParams(mobile,verification);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_BIND_PHONE, phoneParams, Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object o) {
                mView.get().bindPhoneSuccess();
            }
        });
    }
}
