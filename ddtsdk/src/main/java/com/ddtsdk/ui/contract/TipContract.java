package com.ddtsdk.ui.contract;

import android.content.Context;

import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.model.protocol.bean.ResOnLineTime;

/**
 * Created by CZG on 2020/4/21.
 */
public class TipContract {
    public interface View extends BaseContract.BaseView {
    }
    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
    }
}
