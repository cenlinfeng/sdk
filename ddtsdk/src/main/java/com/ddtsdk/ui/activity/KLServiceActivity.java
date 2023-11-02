package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.utils.ImageUtils;

/**
 * Created by CZG on 2020/5/23.
 * 服务
 */
public class KLServiceActivity extends BaseActivity {

    private ImageView kl_back_iv;

    private TextView kl_wx_code_tv;
    private TextView kl_qq_tv;
    private ImageView kl_wx_code_iv;

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, KLServiceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        kl_back_iv = findViewById(resourceId("kl_back_iv","id"));
        kl_wx_code_tv = findViewById(resourceId("kl_wx_code_tv","id"));
        kl_qq_tv = findViewById(resourceId("kl_qq_tv","id"));
        kl_wx_code_iv = findViewById(resourceId("kl_wx_code_iv","id"));

        kl_wx_code_tv.setText("公众号:\n"+AppConstants.wx_name);
        kl_qq_tv.setText("QQ客服:\n"+AppConstants.customer_qq);
        if (TextUtils.isEmpty(AppConstants.customer_qq)){
            kl_qq_tv.setVisibility(View.GONE);
        }
        ImageUtils.loadImage(kl_wx_code_iv,AppConstants.wx_qrcode);

        kl_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

    }

    @Override
    protected String layoutName() {
        return Constants.service;
    }
}
