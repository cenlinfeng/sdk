package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.model.protocol.bean.PayConfig;
import com.ddtsdk.model.protocol.bean.PayMsg;

/**
 * Created by CZG on 2020/4/21.
 */
public class PaymentContract {
    public interface View extends BaseContract.BaseView {
        void initSuccess(PayConfig payConfig);
        void initFail(String msg);
        void showFailMsg(String msg);
        void paySuccess(PayMsg payMsg);
        void platformSuccess(String msg);
        void payFail(String msg);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void initRequest(Context context, String amount);
        void pay(Context context, String billNo, String amount,
                 String paytarget, String paychar, String serverId,
                 String extraInfo, String subject, String isTest, String rolename,
                 String level, String roleid, String pw,String coupon_id,String voucherGoods,String goods_id);
    }
}
