package com.ddtsdk.listener;

/**
 * Created by CZG on 2020/4/28.
 */
public interface IKLUserListener {

    void onLogout(Object obj);

    void returnLogin(Object result);
}
