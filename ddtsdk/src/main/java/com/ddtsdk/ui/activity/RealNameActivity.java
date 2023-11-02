package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.contract.RealNameContract;
import com.ddtsdk.ui.presenter.RealNamePresenter;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.Utils;

/**
 * Created by CZG on 2020/4/21
 * 实名认证
 */
public class RealNameActivity extends BaseMvpActivity<RealNameContract.View, RealNamePresenter> implements RealNameContract.View {

    public static final int REALNAME_REQUEST_CODE = 300;
    public static final String RETURN_DATA = "return_data";

    private EditText kl_real_name_et;
    private EditText kl_idcard_et;
    private com.ddtsdk.ui.view.KLTextView kl_authenticate_tv;
    private TextView kl_next_tv;
    private CountDownTimer mCountDownTimer;

    private int isNext;//判断是否开启下次认证按钮， 1 不开启，强制实名   2 开启，可以跳过实名

    /**
     * @param activity     唤起RealNameActivity的Activity对象
     * @param forceautonym 服务端返回的实名认证参数  0 不需要实名 1 强制实名 2 可以跳过实名 3 回到游戏  默认强制实名
     */
    public static void startThisActivity(Activity activity, int forceautonym) {
        Intent intent = new Intent(activity, RealNameActivity.class);
        intent.putExtra("forceautonym", forceautonym);
        activity.startActivityForResult(intent, REALNAME_REQUEST_CODE);
    }

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, RealNameActivity.class);
        intent.putExtra("forceautonym", AppConstants.forceautonym);
        activity.startActivity(intent);
    }

    @Override
    protected String layoutName() {
        return Constants.real_name;
    }

    @Override
    protected void initView() {
        kl_real_name_et = findViewById(resourceId("kl_real_name_et", "id"));
        kl_idcard_et = findViewById(resourceId("kl_idcard_et", "id"));

        kl_authenticate_tv = findViewById(resourceId("kl_authenticate_tv", "id"));
        kl_next_tv = findViewById(resourceId("kl_next_tv", "id"));
        if (isNext == 2) {
            kl_next_tv.setText("下次认证");
            kl_next_tv.setVisibility(View.VISIBLE);
        } else if (isNext == 3) {
            kl_next_tv.setText("回到游戏");
            kl_next_tv.setVisibility(View.VISIBLE);
        }
        if (AppConstants.pack_model == 1) {
            kl_next_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
        }
    }

    @Override
    protected void initListener() {
        kl_next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (AppConstants.pay_realname == 1) {
                    BaseKLSDK.getInstance().payCallback("");    //4.3.4版本开始，支付加上回调给cp
                    AppConstants.pay_realname = 0;
                }

                if (isNext == 2) {
                    if (AppConstants.openillustrate == 0) {
                        BaseKLSDK.getInstance().wrapLoginInfo();
                        OnLineTimeRequest.get().loadOnlineTime(false);
                    } else {
                        intent.putExtra(RETURN_DATA, "error");
                        KLTipActivity.startThisActivity(RealNameActivity.this, KLTipActivity.TYPE_REAL_NAME);
                    }
                } else if (isNext == 3) {
                    intent.putExtra(RETURN_DATA, "backgame");
                }
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        kl_authenticate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEditText()) {
                    return;
                }
                if (AppConstants.pay_realname == 1) {
                    BaseKLSDK.getInstance().payCallback("");    //4.3.4版本开始，支付加上回调给cp
                    AppConstants.pay_realname = 0;
                }
                kl_authenticate_tv.setClickable(false);
                authenticateBottomSetting(false);
                mPresenter.certificate(RealNameActivity.this, kl_real_name_et.getText().toString(), kl_idcard_et.getText().toString());
            }
        });

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        isNext = intent.getIntExtra("forceautonym", 1);
//        AppConstants.ddt_ver_id = Utils.getAgent(RealNameActivity.this);
    }

    @Override
    protected RealNamePresenter createPresenter() {
        return new RealNamePresenter();
    }

    public Boolean checkEditText() {
        if (TextUtils.isEmpty(kl_real_name_et.getText().toString())) {
            showToastMsg("请输入真实姓名！");
            return false;
        }

        boolean isRight = mPresenter.checkIdValidation(kl_idcard_et.getText().toString());
        if (!isRight) {
            showToastMsg("请输入正确的身份证号！");
        }
        return isRight;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void certificateSuccess() {
        showToastMsg("实名成功！");
        AppConstants.isautonym = 1;
        Intent intent = new Intent();
        intent.putExtra(RETURN_DATA, "success");
        if (isNext != 3) {
            BaseKLSDK.getInstance().wrapLoginInfo();
            OnLineTimeRequest.get().onlineTime();
        }
        authenticateBottomSetting(true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void certificateFail() {
        kl_authenticate_tv.setClickable(true);
        authenticateBottomSetting(true);
    }

    /**
     * 身份验证防抖重复提交
     */
    private void authenticateBottomSetting(boolean enable) {
        kl_authenticate_tv.setEnabled(enable);
        if (enable) {
            kl_authenticate_tv.setText("立即认证");
        } else {
            kl_authenticate_tv.setText("认证中。。。");
        }
    }
}
