package com.ddtsdk.ui.presenter;

import android.content.Context;

import com.ddtsdk.common.base.BasePresenter;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.model.protocol.bean.PokerInitBean;
import com.ddtsdk.model.protocol.bean.PokerPlayBean;
import com.ddtsdk.model.protocol.params.PokerPlayParams;
import com.ddtsdk.ui.contract.PokerContract;

public class PokerPresenter extends BasePresenter<PokerContract.View> implements PokerContract.Presenter<PokerContract.View> {

    @Override
    public void initPlay(Context context) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_POKER_INIT, null, PokerInitBean.class,
                new HttpRequestClient.ResultHandler<PokerInitBean>(context) {
                    @Override
                    public void onSuccess(PokerInitBean pokerInitBean) {
                        mView.get().setPlayConfig(pokerInitBean);
                    }
                });
    }

    @Override
    public void toPlay(Context context, String jackpot, String type) {
        PokerPlayParams params = new PokerPlayParams(jackpot, type);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_POKER_PLAY, params, PokerPlayBean.class,
                new HttpRequestClient.ResultHandler<PokerPlayBean>(context) {
                    @Override
                    public void onSuccess(PokerPlayBean playResult) {
                       mView.get().setPlayResult(playResult);
                    }

                    @Override
                    public void onResponse(BaseBean baseBean) {
                        if (!baseBean.isResult())
                            mView.get().showError(baseBean);
                    }
                });
    }
}
