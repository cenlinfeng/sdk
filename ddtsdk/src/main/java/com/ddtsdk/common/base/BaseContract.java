package com.ddtsdk.common.base;

/**
 * Created by CZG on 2020/4/17.
 */
public class BaseContract {
    public interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    public interface BaseView {

    }
}
