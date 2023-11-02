package com.ddtsdk.common.base;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.utils.DisplayUtil;
import com.ddtsdk.utils.ScreenUtils;
import com.ddtsdk.utils.ToastUtils;

/**
 * Created by CZG on 2020/4/17.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected View kl_main_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        KLAppManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        DisplayUtil.setDefaultDisplay(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        View view = LayoutInflater.from(this).inflate(resourceId(layoutName(),"layout"), null);
        setContentView(view);


        kl_main_view = view.findViewById(resourceId("kl_main_view","id"));
        setOrientationView(isScreenOrientationPortrait());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setOrientationView(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLAppManager.getInstance().finishActivity(this);
    }

    public void setOrientationView(boolean isPortrait){
        if (kl_main_view != null){
            int screenWidth = ScreenUtils.getScreenWidth(this);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) kl_main_view.getLayoutParams();
            if (isPortrait){
                lp.width = (int) (screenWidth * 0.8);
            } else {
                lp.width = (int) (screenWidth * 0.5);
            }
        }
    }

    public boolean isScreenOrientationPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    protected abstract String layoutName();

    /**
     * toast 提示信息
     *
     * @param msg
     */
    public void showToastMsg(String msg) {
        ToastUtils.showShort(this,msg);
    }

    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }
}
