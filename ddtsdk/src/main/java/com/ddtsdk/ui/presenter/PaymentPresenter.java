package com.ddtsdk.ui.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.PayConfig;
import com.ddtsdk.model.protocol.bean.PayMsg;
import com.ddtsdk.model.protocol.params.GetPayParams;
import com.ddtsdk.model.protocol.params.PayParams;
import com.ddtsdk.network.ApiException;
import com.ddtsdk.network.HttpStatusCode;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.ui.contract.PaymentContract;
import com.ddtsdk.utils.Utils;

import java.util.HashMap;

/**
 * Created by CZG on 2020/4/21.
 */
public class PaymentPresenter extends BasePresenter<PaymentContract.View> implements PaymentContract.Presenter<PaymentContract.View> {

    @Override
    public void initRequest(Context context, String amount) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_GETPAY, new GetPayParams(amount), PayConfig.class, new HttpRequestClient.ResultHandler<PayConfig>(context) {
            @Override
            public void onSuccess(PayConfig payConfig) {
                mView.get().initSuccess(payConfig);
            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (!baseBean.isResult()) {
                    PayConfig payConfig = new PayConfig();
                    String result = baseBean.getData().toString();
                    String msg = baseBean.getMsg();
                    if (result != null) {
                        HashMap<String, String> map = GsonUtils.createObj(result);
                        if (map.size() > 0) {
                            if (!TextUtils.isEmpty(map.get("isnonage"))) {
                                Log.e("ISNONAGE", map.get("isnonage") + "=================");
                                payConfig.setIsnonage(Integer.parseInt(map.get("isnonage")));
//                                payConfig.setIsnonage((int)Float.parseFloat(map.get("isnonage")));
                                AppConstants.isnonage = payConfig.getIsnonage();
                            } else {
                                payConfig.setIsnonage(AppConstants.isnonage);
                            }
                            if (!TextUtils.isEmpty(map.get("isautonym"))) {
                                payConfig.setIsnonage(Integer.parseInt(map.get("isautonym")));
//                                payConfig.setIsautonym((int)Float.parseFloat(map.get("isnonage")));
                                AppConstants.isautonym = payConfig.getIsautonym();
                            } else {
                                payConfig.setIsautonym(AppConstants.isautonym);
                            }
                            if (!TextUtils.isEmpty(map.get("msg"))) {
                                payConfig.setMsg(map.get("msg"));
                                msg = map.get("msg");
                            }
                        }
                    }
                    mView.get().initFail(msg);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (!(t instanceof ApiException && ((ApiException) t).getStatus() == HttpStatusCode.RESPONSE_FAIL)) {
                    Log.e("PAYERROR", t.getMessage());
                    mView.get().showFailMsg("网络连接失败，请检查您的网络连接");
                }
            }
        });
    }


    @Override
    public void pay(Context context, String billNo, String amount,
                    final String paytarget, String paychar, String serverId,
                    String extraInfo, String subject, String isTest, String rolename,
                    String level, String roleid, String pw, String coupon_id,
                    String voucherGoods, String goods_id) {
        String packageNmae = Utils.getPackageName(context);
        //TODO:红包盒子
//        String packageNmae = "com.ddt.game.adbox";

        PayParams payParams = new PayParams(amount, paytarget, paychar, pw, billNo, extraInfo, subject, serverId, isTest, rolename, level, roleid, packageNmae, coupon_id, voucherGoods, goods_id);
        if (AppConfig.adType != -1) {
            AppConstants.mPayParam = payParams;
        }
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_CENTERTOPAY, payParams, PayMsg.class, new HttpRequestClient.ResultHandler<PayMsg>(context) {
            @Override
            public void onSuccess(PayMsg payMsg) {
                if (paytarget.equals("5")) {
                    mView.get().paySuccess(payMsg);
                }
            }

            @Override
            public void onResponse(BaseBean baseBean) {
                if (paytarget.equals("5")) {
                    if (!baseBean.isResult()) {
                        mView.get().payFail(baseBean.getMsg());
                    }
                } else {
                    if (baseBean.isResult()) {
                        mView.get().platformSuccess(baseBean.getMsg());
                    } else {
                        mView.get().payFail(baseBean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof ApiException) {
                    mView.get().payFail(t.getMessage());
                } else {
                    mView.get().payFail("网络连接失败，请检查您的网络连接");
                }
            }
        });
    }
}
