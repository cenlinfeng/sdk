package com.ddtsdk.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.AccountManager;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.model.protocol.params.LoginParams;
import com.ddtsdk.model.protocol.params.RegisterParams;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.utils.CreatBitmapUtil;
import com.ddtsdk.utils.LogUtil;

/**
 * Created by CZG on 2020/4/17.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View>{

    //登录接口
    //注意：跳去别的界面回来时，会在onResume()方法做判断，是否第一次登录。
    @Override
    public void login(final Context context, final BaseUserData userData) {
        LoginParams loginParams = new LoginParams(userData.getUname(),userData.getPwd());
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_LOGIN, loginParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                AccountManager.setLoginMessage(loginMessage);
                AccountManager.getInstance(context).setUserData(loginMessage);
                AccountManager.getInstance(context).addHistoryUserData(loginMessage.getUid(),
                        loginMessage.getUname(), userData.getPwd(),loginMessage.getMobile());
                AppConstants.is_voucher = loginMessage.isVoucher();
                mView.get().loginSuccess(loginMessage);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
//                mView.get().wrapLoginInfo(getLoginMessageInfo(new LoginMessage(),"fail", ""));
            }
        });
    }

    /**
     * 注册
     *
     * @param username
     *            账号 或手机号
     * @param visitor
     *            1 是访客
     */
    @Override
    public void register(final Context context, String username, final String password, final String verifycode, String visitor) {
        AppConstants.userName = username;
        AppConstants.regType = "fastReg";
        AdManager.getInstance().logRegisterReport(context, "1", "success");
        RegisterParams registerParams = new RegisterParams(username, password, verifycode, visitor,RegisterParams.TYPE_VISITOR);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_REGISTER, registerParams, LoginMessage.class, new HttpRequestClient.ResultHandler<LoginMessage>(context) {
            @Override
            public void onSuccess(LoginMessage loginMessage) {
                //一键注册，报送数据
                LogUtil.d("一键注册，报送数据:fastReg");
                //开启截图
//                savaUserInfoFromFastLogin(context,loginMessage.getUname(), loginMessage.getPwd());
                AppConstants.reg_is_type = 1;
                AppConstants.reg_is_adv = loginMessage.getAdv_info().getIs_adv();
                AppConstants.reg_source = loginMessage.getAdv_info().getSource();
                AppConstants.reg_adv_url = loginMessage.getAdv_info().getAdv_url();
                AppConstants.reg_adv_img = loginMessage.getAdv_info().getAdv_img();

                mView.get().registerSuccess(loginMessage);
                AppConstants.control_status = loginMessage.getControl_status();
                AppConstants.control_type = loginMessage.getControl_type();
                AppConstants.threshold_value = loginMessage.getThreshold_value();
                AdManager.getInstance().register(context);
            }

            @Override
            public void onFailure(Throwable t) {
                AdManager.getInstance().logRegisterReport(context, "2", "error");
//                mView.get().wrapLoginInfo(getLoginMessageInfo(new LoginMessage(),"fail", ""));
                super.onFailure(t);
            }
        });
    }

//    //一键注册保存账号密码为图片
//    private void savaUserInfoFromFastLogin(Context context,final String name, final String pwd){
////		LogUtil.e("showCaptureView 截图界面");
//        CreatBitmapUtil.getInstance(context).saveUserBitmap(name,pwd);
//    }

}
