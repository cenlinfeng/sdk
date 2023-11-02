package com.ddt.h5game.network;

public interface UrlCallBack {
    //url:地址链接    type：加载方式：  0为旧加载模式， 1为新加载方式
    void getUrl(String url, int type);
}
