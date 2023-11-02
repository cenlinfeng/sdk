package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.contract.BindPhoneContract;
import com.ddtsdk.ui.presenter.BindPhonePresenter;
import com.ddtsdk.utils.ImageUtils;

/**
 * Created by CZG on 2020/5/21.
 * 绑定手机
 */
public class KLBindPhoneActivity extends BaseMvpActivity<BindPhoneContract.View, BindPhonePresenter> implements BindPhoneContract.View {

    private ImageView kl_back_iv;
    private EditText kl_phone_et;
    private EditText kl_code_et;
    private com.ddtsdk.ui.view.KLTextView kl_code;
    private com.ddtsdk.ui.view.KLTextView kl_bind_phone;

    private CountDownTimer mCountDownTimer;

    public static void startThisActivity(Activity activity){
        Intent intent = new Intent(activity, KLBindPhoneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        kl_back_iv = findViewById(resourceId("kl_back_iv", "id"));
        kl_phone_et = findViewById(resourceId("kl_phone_et", "id"));
        kl_code_et = findViewById(resourceId("kl_code_et", "id"));
        kl_code = findViewById(resourceId("kl_code", "id"));
        kl_bind_phone = findViewById(resourceId("kl_bind_phone", "id"));

//        if (AppConstants.pack_model ==  1){
//            GradientDrawable drawable = (GradientDrawable)kl_code.getBackground();
//            drawable.setColor(Color.parseColor(AppConstants.colorPrimary));
//            kl_code.setBackground(drawable);
//        }
        initCountDownTime();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        kl_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        kl_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = kl_phone_et.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(KLBindPhoneActivity.this, "请输入手机号",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.getCode(KLBindPhoneActivity.this,phone);
            }
        });
        kl_bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = kl_phone_et.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(KLBindPhoneActivity.this, "请输入手机号",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String code = kl_code_et.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(KLBindPhoneActivity.this, "请输入验证码",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.bindPhone(KLBindPhoneActivity.this,phone,code);
            }
        });
    }

    public void initCountDownTime() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = "发送" + millisUntilFinished / 1000 + "s";
                kl_code.setText(text);
                kl_code.setClickable(false);
            }

            public void onFinish() {
                String text = "重新发送";
                kl_code.setText(text);
                kl_code.setClickable(true);
            }
        };
    }

    @Override
    protected BindPhonePresenter createPresenter() {
        return new BindPhonePresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.bind_phone;
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void getCodeSuccess() {
        if (mCountDownTimer != null){
            mCountDownTimer.start();
        }
    }

    @Override
    public void bindPhoneSuccess() {
        showToastMsg("手机绑定成功");
        finish();
    }

    @Override
    public void showMsg(String msg) {
        showToastMsg(msg);
    }
}
