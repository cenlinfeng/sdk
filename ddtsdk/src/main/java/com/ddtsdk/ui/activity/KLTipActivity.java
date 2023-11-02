package com.ddtsdk.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.contract.TipContract;
import com.ddtsdk.ui.presenter.TipPresenter;

/**
 * Created by CZG on 2020/5/23.
 */
public class KLTipActivity extends BaseMvpActivity<TipContract.View, TipPresenter> implements TipContract.View {

    public static final String TIP_TYPE = "tip_type";
    public static final String TIP_TEXT = "tip_text";
    public static final String IS_BAN_LOGIN = "is_ban_login";
    public static final String IS_RETURN = "is_return";

    public static final int TYPE_REAL_NAME = 0;
    public static final int TYPE_LOGIN_LIMIT = 1;
    public static final int TYPE_RECHARGE_LIMIT = 2;

    private TextView kl_tip_tv;
    private com.ddtsdk.ui.view.KLTextView kl_next_tv;
    private com.ddtsdk.ui.view.KLTextView kl_submit_tv;
    private TextView kl_title_tv;

    private int mType;
    private String mTipText;

    public static void startThisActivity(Context context, int type) {
        Intent intent = new Intent(context, KLTipActivity.class);
        intent.putExtra(TIP_TYPE, type);
        context.startActivity(intent);
    }

    public static void startThisActivity(Context context, String tipText,int isbanlogin,boolean isReturn) {
        Intent intent = new Intent(context, KLTipActivity.class);
        intent.putExtra(TIP_TYPE, TYPE_LOGIN_LIMIT);
        intent.putExtra(TIP_TEXT,tipText);
        intent.putExtra(IS_BAN_LOGIN,isbanlogin);
        intent.putExtra(IS_RETURN,isReturn);
        context.startActivity(intent);
    }

    public static void startThisActivity(Context context, String tipText) {
        Intent intent = new Intent(context, KLTipActivity.class);
        intent.putExtra(TIP_TYPE, TYPE_RECHARGE_LIMIT);
        intent.putExtra(TIP_TEXT,tipText);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        kl_tip_tv = findViewById(resourceId("kl_tip_tv", "id"));
        kl_next_tv = findViewById(resourceId("kl_next_tv", "id"));
        kl_submit_tv = findViewById(resourceId("kl_submit_tv", "id"));
        kl_title_tv = findViewById(resourceId("kl_title_tv", "id"));

        if (mType == TYPE_REAL_NAME) {
            kl_title_tv.setText("游客说明");
            kl_next_tv.setVisibility(View.VISIBLE);
            kl_submit_tv.setText("立即实名");
            kl_tip_tv.setText("根据国家规定，未实名用户在15天内只能试玩1小时，期间不能付费。");
        } else {
            kl_title_tv.setText("温馨提示");
            kl_next_tv.setVisibility(View.GONE);
            kl_submit_tv.setText("确认");
            if (!TextUtils.isEmpty(mTipText)){
                kl_tip_tv.setText(mTipText);
            }
        }

//        if (AppConstants.pack_model == 1){
//            kl_next_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
//        }
    }

    @Override
    protected void initData() {
        mType = getIntent().getIntExtra(TIP_TYPE, 0);
        mTipText = getIntent().getStringExtra(TIP_TEXT);
    }

    @Override
    protected void initListener() {
        kl_next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseKLSDK.getInstance().wrapLoginInfo();
                OnLineTimeRequest.get().onlineTime();
                finish();
            }
        });
        kl_submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == TYPE_REAL_NAME) {
                    RealNameActivity.startThisActivity(KLTipActivity.this);
                } else if (mType == TYPE_LOGIN_LIMIT){
                    int isbanlogin = getIntent().getIntExtra(IS_BAN_LOGIN,0);
                    boolean isReturn = getIntent().getBooleanExtra(IS_BAN_LOGIN,true);
                    if (isbanlogin == 1){
                        if (isReturn){
                            BaseKLSDK.getInstance().returnLogin(mTipText);
                        } else {
                            BaseKLSDK.getInstance().doSwitchAccount(false);
                        }
                    }
                }
                finish();
            }
        });
    }

    @Override
    protected TipPresenter createPresenter() {
        return new TipPresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.tip_common;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
