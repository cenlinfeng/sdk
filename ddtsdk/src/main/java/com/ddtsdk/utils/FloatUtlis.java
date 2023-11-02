package com.ddtsdk.utils;

import android.content.Context;

import com.ddtsdk.ui.view.FloatView;
import com.ddtsdk.ui.view.TopLoadingView;
import com.ddtsdk.view.BallControler;


public class FloatUtlis {

    private FloatView mH5FloatView;
    private FloatView mFloatView;
    private TopLoadingView mLoadingView;

    private static Context sContext;

    private static FloatUtlis sInstance;

    public static FloatUtlis getInstance() {
        if (sInstance == null) {
            sInstance = new FloatUtlis();
        }
        return sInstance;
    }

    //初始化view
    public void initView(Context context) {
        LogUtil.d("FloatUtlis initView");
        if (context == null) {
            LogUtil.e("FloatUtlis initView context为空");
            return;
        }
        sContext = context;
        if (mFloatView == null) {
            LogUtil.i("悬浮球初始化");
            mFloatView = new FloatView(context);
        }
        if (mLoadingView == null) {
            mLoadingView = new TopLoadingView(context);
        }
        mFloatView.hide();
        mLoadingView.hide();
    }

    public void setFloatView(Context context) {
        LogUtil.d("FloatUtlis setFloatView");
        if (context == null) {
            LogUtil.e("FloatUtlis setFloatView context为空");
            return;
        }
        mH5FloatView = new FloatView(context);
        mH5FloatView.show();
    }

    //显示悬浮球
    public void showFloat() {
        LogUtil.i("显示悬浮球");

        if (mH5FloatView != null) {
            mH5FloatView.show();
            return;
        }

        if (mFloatView != null) {
            mFloatView.show();
        }

    }

    //隐藏悬浮球
    public void hideFloatItems(String json) {
        BallControler.setCtr(json);

        if (mH5FloatView != null) {
            mH5FloatView.hideItems();
            return;
        }

        if (mFloatView != null) {
            mFloatView.hideItems();
        }

    }

    public void setFloatItems(String url) {

    }

    //显示LoadingView
    public void showLoadingView() {
        LogUtil.d("FloatUtlis showLoadingView   sContext:" + sContext + ", mLoadingView:" + mLoadingView);
        if (sContext != null && mLoadingView == null) {
            mLoadingView = null;
            LogUtil.d("FloatUtlis showLoadingView  mLoadingView=null  创建TopLoadingView");
            mLoadingView = new TopLoadingView(sContext);
            mLoadingView.hide();
        }
        if (mLoadingView != null) {
            mLoadingView.show();
            mLoadingView.setUserName();
        }
    }


    //隐藏悬浮球
    public void hideFloat() {
        LogUtil.i("隐藏悬浮球");

        if (mH5FloatView != null) {
            mH5FloatView.hide();
            return;
        }
        if (mFloatView != null) {
            mFloatView.hide();
            mLoadingView.hide();
        }
    }

    //销毁悬浮球
    public void destroyH5Float() {
        if (mH5FloatView != null) {
            mH5FloatView.destroy();
        }
        mH5FloatView = null;
    }


    //销毁悬浮球
    public void destroyFloat() {
        if (mFloatView != null) {
            mFloatView.destroy();
        }
        mFloatView = null;
    }

    //销毁loadingview
    public void destroyLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.destroy();
        }
        mLoadingView = null;
    }


}
