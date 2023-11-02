package com.ddtsdk.listener;

/**
 * Created by CZG on 2020/4/24.
 */
public abstract class IDdtListener<T> {
    public abstract void onSuccess(T data);

    public void onLoadH5Url(String dologin_h5, String h5LoginLink){

    }

    /*public void onFail(String msg){

    }*/
}
