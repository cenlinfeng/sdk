package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.model.protocol.bean.PokerInitBean;
import com.ddtsdk.model.protocol.bean.PokerPlayBean;


public class PokerContract {
    public interface View extends BaseContract.BaseView {
        void setPlayConfig(PokerInitBean initData);
        void setPlayResult(PokerPlayBean playResult);
        void showError(BaseBean baseBean);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void initPlay(Context context);
        void toPlay(Context context,String jackpot,String type);
    }

}
