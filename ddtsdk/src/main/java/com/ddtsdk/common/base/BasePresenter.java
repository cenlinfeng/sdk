package com.ddtsdk.common.base;

import java.lang.ref.WeakReference;

/**
 * Created by CZG on 2020/4/17.
 */
public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected WeakReference<T> mView;

    @Override
    public synchronized final void attachView(T view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public synchronized final void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }
}
