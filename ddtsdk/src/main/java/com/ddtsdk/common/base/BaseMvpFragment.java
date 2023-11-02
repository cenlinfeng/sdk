package com.ddtsdk.common.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by CZG on 2020/4/17.
 */
public abstract class BaseMvpFragment<V extends BaseContract.BaseView, T extends BasePresenter<V>> extends BaseFragment implements BaseContract.BaseView{

    protected T mPresenter;

    @Override
    protected void onViewInit(Bundle savedInstanceState, View view) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        initData();
        initView();
        initListener();
    }

    protected void initListener() {

    }

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();
}
