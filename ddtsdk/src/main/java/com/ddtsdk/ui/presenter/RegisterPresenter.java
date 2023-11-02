package com.ddtsdk.ui.presenter;

import android.content.Context;
import android.os.Handler;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.model.protocol.params.PhoneRegisterParams;
import com.ddtsdk.model.protocol.params.RegisterParams;
import com.ddtsdk.model.protocol.params.SmsParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.ui.contract.RegisterContract;
import com.ddtsdk.utils.LogUtil;

/**
 * Created by CZG on 2020/4/21.
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter<RegisterContract.View> {

    /**
     * 注册
     *
     * @param username   账号 或手机号
     * @param password   密码
     * @param verifycode 验证码
     * /@param visitor    1 是访客
     */
    @Override
    public void register(final Context context, String username, final String password, final String verifycode, final int reg_type) {
        AppConstants.userName = username;
        AppConstants.regType = "userReg";
        AdManager.getInstance().logRegisterReport(context, "1", "success");

        RegisterParams registerParams = new RegisterParams(username, password, verifycode, "",RegisterParams.TYPE_VISITOR);

        //reg_is_adv、reg_adv_url、reg_adv_img、reg_source
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_REGISTER, registerParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                AdLogReport(context, loginMessage);
                AppConstants.reg_is_type = 1;
                AppConstants.reg_is_adv = loginMessage.getAdv_info().getIs_adv();
                AppConstants.reg_source = loginMessage.getAdv_info().getSource();
                AppConstants.reg_adv_url = loginMessage.getAdv_info().getAdv_url();
                AppConstants.reg_adv_img = loginMessage.getAdv_info().getAdv_img();

            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.isResult()) {
                    mView.get().registerSuccess(baseBean.getMsg());
                } else {
                    AdManager.getInstance().logRegisterReport(context, "2", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
//                endLog(context, "error", "", "", "", "");
                AdManager.getInstance().logRegisterReport(context, "2", "error");
            }
        });
    }

    @Override
    public void phoneRegister(final Context context, String mobile, String password, String verifycode) {
        AppConstants.userName = mobile;
        AppConstants.regType = "phoneReg";
        AdManager.getInstance().logRegisterReport(context, "1", "success");
        PhoneRegisterParams registerParams = new PhoneRegisterParams(mobile, password, verifycode);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_PHONE_REGISTER, registerParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                AppConstants.reg_is_type = 1;
                AppConstants.reg_is_adv = loginMessage.getAdv_info().getIs_adv();
                AppConstants.reg_source = loginMessage.getAdv_info().getSource();
                AppConstants.reg_adv_url = loginMessage.getAdv_info().getAdv_url();
                AppConstants.reg_adv_img = loginMessage.getAdv_info().getAdv_img();

                AdLogReport(context, loginMessage);
            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.isResult()) {
                    mView.get().registerSuccess(baseBean.getMsg());
                } else {
                    AdManager.getInstance().logRegisterReport(context, "2", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                AdManager.getInstance().logRegisterReport(context, "2", "error");
            }
        });
    }

    private void AdLogReport(final Context context, LoginMessage loginMessage){
        /**
         * 广告注册上报接口
         */
        AppConstants.uid = loginMessage.getUid();
        AppConstants.control_status = loginMessage.getControl_status();
        AppConstants.control_type = loginMessage.getControl_type();
        AppConstants.threshold_value = loginMessage.getThreshold_value();
        AdManager.getInstance().register(context);
    }

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     */
    @Override
    public void getCode(Context context, String mobile) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_SMS, new SmsParams(mobile, SmsParams.REGISTER), Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
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
