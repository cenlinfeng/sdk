package com.ddtsdk.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by CZG on 2020/4/17.
 */
public abstract class BaseMvpActivity<V extends BaseContract.BaseView, T extends BasePresenter<V>> extends BaseActivity implements BaseContract.BaseView{

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();
}
