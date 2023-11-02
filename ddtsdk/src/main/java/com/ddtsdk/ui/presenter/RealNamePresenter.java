package com.ddtsdk.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.ResCertificate;
import com.ddtsdk.model.protocol.params.RealNameParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.ui.activity.KLTipActivity;
import com.ddtsdk.ui.contract.RealNameContract;
import com.ddtsdk.view.ForceExitDialog;

/**
 * Created by CZG on 2020/4/21.
 */
public class RealNamePresenter extends BasePresenter<RealNameContract.View> implements RealNameContract.Presenter<RealNameContract.View> {

    @Override
    public void certificate(final Context context, String uName, String idCard) {
        RealNameParams realNameParams = new RealNameParams(uName, idCard);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_CERTIFICATEVERIFY, realNameParams, ResCertificate.class, new HttpRequestClient.ResultHandler<ResCertificate>(context) {
            @Override
            public void onSuccess(ResCertificate resCertificate) {
                AppConstants.isnonage = resCertificate.getIsnonage();
                mView.get().certificateSuccess();
                //这里判断未成年反沉迷直接退出
                if (resCertificate.getIsbanlogin() == 1) {
                    ForceExitDialog.showDialog(KLSDK.getInstance().getContext(), resCertificate.getMsg(),
                            resCertificate.getDowntime(), new ForceExitDialog.onTimesListener() {
                                @Override
                                public void OnSuccess() {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            });
                }else {
                        Log.e("14523UE237","GCGSUISISKKS====");
                        KLTipActivity.startThisActivity(context,"【健康系统】合理安排游戏时间，享受健康生活。建议您适当地休息，在畅享游戏的同时，也要注意劳逸结合哦~");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                mView.get().certificateFail();
            }
        });
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    @Override
    public boolean checkIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String regX = "[0-9]{17}X";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(regX) || text.matches(reg1) || text.matches(regex);
    }


}
