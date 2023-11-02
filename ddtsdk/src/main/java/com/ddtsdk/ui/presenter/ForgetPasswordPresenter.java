package com.ddtsdk.ui.presenter;

import android.content.Context;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.model.protocol.params.ResetParams;
import com.ddtsdk.model.protocol.params.SmsParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.ui.contract.ForgetPasswordContract;

/**
 * Created by CZG on 2020/4/21.
 */
public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View> implements ForgetPasswordContract.Presenter<ForgetPasswordContract.View> {

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     */
    @Override
    public void getCode(Context context, String mobile) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_SMS, new SmsParams(mobile, SmsParams.RESETPASSWORD), Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object obj) {

            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.isResult()){
                    mView.get().showCodeMsg(baseBean.getMsg());
                }
            }
        });
    }

    @Override
    public void resetPwd(Context context, String mobile, String verifyCode, String pwd) {
        ResetParams resetParams = new ResetParams(mobile,verifyCode,pwd,"web");
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_RESETPWD, resetParams, Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object obj) {

            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.isResult()){
                    mView.get().resetSuccess(baseBean.getMsg());
                }
            }

        });
    }
}
