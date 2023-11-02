package com.ddtsdk.ui.presenter;

import android.content.Context;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.model.protocol.params.PhoneParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.model.protocol.params.SmsParams;
import com.ddtsdk.ui.contract.PhoneLoginContract;

/**
 * Created by CZG on 2020/4/17.
 */
public class PhoneLoginPresenter extends BasePresenter<PhoneLoginContract.View> implements PhoneLoginContract.Presenter<PhoneLoginContract.View>{

    //登录接口
    //注意：跳去别的界面回来时，会在onResume()方法做判断，是否第一次登录。
    @Override
    public void login(final Context context, final String phone, final String code) {
        PhoneParams loginParams = new PhoneParams(phone,code);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_LOGIN_PHONE, loginParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                AccountManager.setLoginMessage(loginMessage);
                AccountManager.getInstance(context).setUserData(loginMessage);
                AccountManager.getInstance(context).addHistoryUserData(loginMessage.getUid(),loginMessage.getUname(), loginMessage.getPwd(),phone);
                mView.get().loginSuccess(loginMessage);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                mView.get().showLoginFail();
//                mView.get().wrapLoginInfo(getLoginMessageInfo(new LoginMessage(),"fail", ""));
            }
        });
    }


    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     */
    @Override
    public void getCode(Context context, String mobile) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_SMS, new SmsParams(mobile, SmsParams.PHONELOGIN), Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
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

}
