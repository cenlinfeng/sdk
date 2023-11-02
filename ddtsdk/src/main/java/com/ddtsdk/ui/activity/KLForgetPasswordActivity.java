package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.contract.ForgetPasswordContract;
import com.ddtsdk.ui.presenter.ForgetPasswordPresenter;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.Utils;

/**
 * Created by CZG on 2020/4/21.
 * 忘记密码
 */
public class KLForgetPasswordActivity extends BaseMvpActivity<ForgetPasswordContract.View, ForgetPasswordPresenter> implements ForgetPasswordContract.View {

	public static final String PASSWORD_TYPE = "password_type";
	public static final int TYPE_CHANGE = 0;
	public static final int TYPE_FORGET = 1;

	private ImageView kl_back_iv;//退
	private ImageView kl_pwd_see;//显示
	private ImageView kl_pwd_see_again;//显示
	private EditText kl_phone_et;//输入手机号
	private EditText kl_code_et;//输入验证码
	private EditText kl_edit_pwd;//输入新密码
	private EditText kl_pwd_again;//重新输入密码
	private com.ddtsdk.ui.view.KLTextView kl_code_tv;//获取验证码
	private com.ddtsdk.ui.view.KLTextView kl_submit_tv;//提交
	private TextView kl_title_tv;//标题
	private TextView kl_service_tv;//忘记密码

	private boolean canSee = false;
	private boolean canSeeAgain = false;
	private int mType;

	private String phone;
	private String code;
	private String newPwd;
	private String newPwdAgain;

	private CountDownTimer mCountDownTimer;

	public static void startThisActivity(Activity activity,int type) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(PASSWORD_TYPE,type);
		intent.setClass(activity, KLForgetPasswordActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected String layoutName() {
		return Constants.forget_password;
	}

	@Override
	protected void initView(){
		kl_back_iv = findViewById(resourceId("kl_back_iv","id"));
		kl_phone_et = findViewById(resourceId("kl_phone_et","id"));
		kl_code_et = findViewById(resourceId("kl_code_et","id"));
		kl_edit_pwd = findViewById(resourceId("kl_edit_pwd","id"));
		kl_pwd_again = findViewById(resourceId("kl_pwd_again","id"));
		kl_code_tv = findViewById(resourceId("kl_code_tv","id"));
		kl_submit_tv = findViewById(resourceId("kl_submit_tv","id"));
		kl_pwd_see = findViewById(resourceId("kl_pwd_see","id"));
		kl_pwd_see_again = findViewById(resourceId("kl_pwd_see_again","id"));
		kl_title_tv = findViewById(resourceId("kl_title_tv","id"));
		kl_service_tv = findViewById(resourceId("kl_service_tv","id"));

		initCountDownTime();
		if (mType == TYPE_CHANGE){
			kl_title_tv.setText("修改密码");
			kl_service_tv.setVisibility(View.GONE);
		} else if (mType == TYPE_FORGET){
			kl_title_tv.setText("找回密码");
			kl_service_tv.setVisibility(View.VISIBLE);
		}

		if (AppConstants.pack_model == 1){
			kl_service_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
		}
	}

	@Override
	protected void initData() {
//		AppConstants.ddt_ver_id = Utils.getAgent(KLForgetPasswordActivity.this);
		if (getIntent() != null){
			mType = getIntent().getIntExtra(PASSWORD_TYPE,0);
		}
	}

	@Override
	protected void initListener() {
		kl_back_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		kl_code_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phone = kl_phone_et.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(KLForgetPasswordActivity.this, "请输入手机号",
							Toast.LENGTH_LONG).show();
					return;
				}
				mPresenter.getCode(KLForgetPasswordActivity.this,phone);
				PayPointReport.getInstance().pushPoint(24);
			}
		});
		kl_submit_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phone = kl_phone_et.getText().toString().trim();
				code = kl_code_et.getText().toString().trim();
				newPwd = kl_edit_pwd.getText().toString().trim();
				newPwdAgain = kl_pwd_again.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(KLForgetPasswordActivity.this, "请输入手机号",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(code)) {
					Toast.makeText(KLForgetPasswordActivity.this, "请输入验证码",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(newPwd)) {
					Toast.makeText(KLForgetPasswordActivity.this, "请输入密码",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!TextUtils.equals(newPwd,newPwdAgain)) {
					Toast.makeText(KLForgetPasswordActivity.this, "密码不一致，请重新输入",
							Toast.LENGTH_LONG).show();
					return;
				}
				mPresenter.resetPwd(KLForgetPasswordActivity.this,phone,code,newPwd);
				PayPointReport.getInstance().pushPoint(25);
			}
		});
		kl_pwd_see.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canSee) {
					kl_edit_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
					kl_pwd_see.setImageResource(resourceId("kl_icon_nosee","mipmap"));
					canSee = false;
				} else {
					kl_edit_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					kl_pwd_see.setImageResource(resourceId("kl_icon_see","mipmap"));
					canSee = true;
				}
			}
		});
		kl_pwd_see_again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canSeeAgain) {
					kl_pwd_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
					kl_pwd_see_again.setImageResource(resourceId("kl_icon_nosee","mipmap"));
					canSeeAgain = false;
				} else {
					kl_pwd_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					kl_pwd_see_again.setImageResource(resourceId("kl_icon_see","mipmap"));
					canSeeAgain = true;
				}
			}
		});
        kl_service_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLServiceActivity.startThisActivity(KLForgetPasswordActivity.this);
				PayPointReport.getInstance().pushPoint(26);
            }
        });
	}

	@Override
	protected ForgetPasswordPresenter createPresenter() {
		return new ForgetPasswordPresenter();
	}


	@Override
	protected void onDestroy() {
		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
			mCountDownTimer = null;
		}
		super.onDestroy();
	}

	public void initCountDownTime() {
		mCountDownTimer = new CountDownTimer(60000, 1000) {
			public void onTick(long millisUntilFinished) {
				String text = "发送" + millisUntilFinished / 1000 + "s";
				kl_code_tv.setText(text);
				kl_code_tv.setClickable(false);
			}

			public void onFinish() {
				String text = "重新发送";
				kl_code_tv.setText(text);
				kl_code_tv.setClickable(true);
			}
		};
	}

	@Override
	public void showCodeMsg(String msg) {
		showToastMsg(msg);
		if (mCountDownTimer != null){
			mCountDownTimer.start();
		}
	}

	@Override
	public void resetSuccess(String msg) {
		showToastMsg(msg);
		AppConfig.isShow =true;
		finish();
	}
}
