package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.model.protocol.params.RegisterParams;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.ui.adapter.CommonViewPagerAdapter;
import com.ddtsdk.ui.contract.RegisterContract;
import com.ddtsdk.ui.presenter.RegisterPresenter;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/4/20.
 * 注册页
 */
public class KLRegisterActivity extends BaseMvpActivity<RegisterContract.View, RegisterPresenter> implements RegisterContract.View, OnClickListener {
    public static final String REGISTER_TYPE = "type_register";
    public static final int TYPE_PHONE = 0;
    public static final int TYPE_ACCOUNT = 1;


    private TextView kl_title_tv;
    private ViewPager kl_viewPager;
    private CheckBox kl_accept_cb;
    private TextView kl_agreement_tv;
    private Button kl_register;
    private TextView kl_login_tv;
    private ImageView kl_back_iv;

    private EditText kl_edit_user;
    private EditText kl_edit_pwd;
    private EditText kl_pwd_again;
    private EditText kl_code_et;
    private Button kl_code;
    private ImageView kl_pwd_see;
    private ImageView kl_pwd_see_again;

    private View mRegisterPhone;
    private View mRegisterAccount;

    private String phone;
    private String code;
    private String user;
    private String password;
    private String password_again;

    private CommonViewPagerAdapter mPageAdapter;

    private CountDownTimer mCountDownTimer;

    private boolean canSee = false;
    private boolean canSeeAgain = false;
    private int mType;
    private boolean isChangePage;

    public static void startThisActivity(Activity activity, int type) {
        Intent intent = new Intent();
        intent.putExtra(REGISTER_TYPE, type);
        intent.setClass(activity, KLRegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected String layoutName() {
        return Constants.register;
    }

    @Override
    protected void initData() {
//        AppConstants.ddt_ver_id = Utils.getAgent(KLRegisterActivity.this);
        if (getIntent() != null) {
            mType = getIntent().getIntExtra(REGISTER_TYPE, 0);
        }
    }

    @Override
    protected void initView() {
        kl_title_tv = findViewById(resourceId("kl_title_tv", "id"));
        kl_viewPager = findViewById(resourceId("kl_viewPager", "id"));
        kl_accept_cb = findViewById(resourceId("kl_accept_cb", "id"));
        kl_agreement_tv = findViewById(resourceId("kl_agreement_tv", "id"));
        kl_register = findViewById(resourceId("kl_register", "id"));
        kl_login_tv = findViewById(resourceId("kl_login_tv", "id"));
        kl_back_iv = findViewById(resourceId("kl_back_iv", "id"));
        kl_accept_cb.setChecked(false);

        mRegisterAccount = LayoutInflater.from(this).inflate(this.resourceId(Constants.register_account, "layout"), null);
        mRegisterPhone = LayoutInflater.from(this).inflate(this.resourceId(Constants.register_phone, "layout"), null);
        List<View> views = new ArrayList<>();
        views.add(mRegisterPhone);
        views.add(mRegisterAccount);
        mPageAdapter = new CommonViewPagerAdapter(views);
        kl_viewPager.setAdapter(mPageAdapter);
        kl_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initRegisterView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mType == TYPE_PHONE) {
            kl_viewPager.setCurrentItem(0);
        } else if (mType == TYPE_ACCOUNT) {
            kl_viewPager.setCurrentItem(1);
        }
        initRegisterView(mType);

//        kl_register.setTextColor(Color.parseColor(AppConstants.colorPrimary));
        kl_login_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
        kl_agreement_tv.setTextColor(Color.parseColor(AppConstants.colorPrimary));
    }

    public void initRegisterView(int position) {
        if (position == 0) {
            kl_title_tv.setText("手机注册");
            initCountDownTime();
            kl_edit_user = mRegisterPhone.findViewById(resourceId("kl_edit_user", "id"));
            kl_edit_pwd = mRegisterPhone.findViewById(resourceId("kl_edit_pwd", "id"));
            kl_pwd_again = mRegisterPhone.findViewById(resourceId("kl_pwd_again", "id"));
            kl_code_et = mRegisterPhone.findViewById(resourceId("kl_code_et", "id"));
            kl_code = mRegisterPhone.findViewById(resourceId("kl_code", "id"));
            kl_pwd_see = mRegisterPhone.findViewById(resourceId("kl_pwd_see", "id"));
            kl_pwd_see_again = mRegisterPhone.findViewById(resourceId("kl_pwd_see_again", "id"));
            kl_code.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PayPointReport.getInstance().pushPoint(13);
                    phone = kl_edit_user.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(KLRegisterActivity.this, "请输入手机号",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    mPresenter.getCode(KLRegisterActivity.this, phone);
                }
            });
        } else {
            kl_title_tv.setText("账号注册");
            kl_edit_user = mRegisterAccount.findViewById(resourceId("kl_edit_user", "id"));
            kl_edit_pwd = mRegisterAccount.findViewById(resourceId("kl_edit_pwd", "id"));
            kl_pwd_again = mRegisterAccount.findViewById(resourceId("kl_pwd_again", "id"));
            kl_pwd_see = mRegisterAccount.findViewById(resourceId("kl_pwd_see", "id"));
            kl_pwd_see_again = mRegisterAccount.findViewById(resourceId("kl_pwd_see_again", "id"));
            TextView suggest_tv = mRegisterAccount.findViewById(resourceId("suggest_tv", "id"));
            SpannableStringBuilder spannable = new SpannableStringBuilder(suggest_tv.getText());
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(AppConstants.colorPrimary)), 4, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            suggest_tv.setMovementMethod(LinkMovementMethod.getInstance());
            spannable.setSpan(new TextClick(), 4, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            suggest_tv.setText(spannable);
        }
        kl_pwd_see.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSee) {
                    kl_edit_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    kl_pwd_see.setImageResource(resourceId("kl_icon_nosee", "mipmap"));
                    canSee = false;
                } else {
                    kl_edit_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    kl_pwd_see.setImageResource(resourceId("kl_icon_see", "mipmap"));
                    canSee = true;
                }
            }
        });
        kl_pwd_see_again.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSeeAgain) {
                    kl_pwd_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    kl_pwd_see_again.setImageResource(resourceId("kl_icon_nosee", "mipmap"));
                    canSeeAgain = false;
                } else {
                    kl_pwd_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    kl_pwd_see_again.setImageResource(resourceId("kl_icon_see", "mipmap"));
                    canSeeAgain = true;
                }
            }
        });
    }

    @Override
    protected void initListener() {
        kl_agreement_tv.setOnClickListener(this);
        kl_register.setOnClickListener(this);
        kl_login_tv.setOnClickListener(this);
        kl_back_iv.setOnClickListener(this);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == AppConfig.resourceId(this, "kl_login_tv", "id")) {
//            KLLoginActivity.startThisActivity(KLRegisterActivity.this);
//            finish();
            if (KLAppManager.getInstance().getActivity(KLLoginActivity.class) == null) {
                KLFirstLoginActivity.startThisActivity(KLRegisterActivity.this);
                KLAppManager.getInstance().finishAllActivity(KLLoginActivity.class);
            } else {
                finish();
            }
            PayPointReport.getInstance().pushPoint(15);
        } else if (v.getId() == AppConfig.resourceId(this, "kl_register",
                "id")) {
            if (mType == TYPE_PHONE) {
                // 手机注册
                phone = kl_edit_user.getText().toString().trim();
                code = kl_code_et.getText().toString().trim();
                password = kl_edit_pwd.getText().toString().trim();
                password_again = kl_pwd_again.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入手机号",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入验证码",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入密码",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.equals(password, password_again)) {
                    Toast.makeText(KLRegisterActivity.this, "密码不一致，请重新输入",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!kl_accept_cb.isChecked()) {
                    Toast.makeText(KLRegisterActivity.this, "请勾选用户隐私协议方可继续",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.phoneRegister(this, phone, password, code);
                PayPointReport.getInstance().pushPoint(14);
            } else if (mType == TYPE_ACCOUNT) {
                // 用户注册
                user = kl_edit_user.getText().toString().trim();
                password = kl_edit_pwd.getText().toString().trim();
                password_again = kl_pwd_again.getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入账号",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入密码",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password_again)) {
                    Toast.makeText(KLRegisterActivity.this, "请输入密码",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.equals(password, password_again)) {
                    Toast.makeText(KLRegisterActivity.this, "密码不一致，请重新输入",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!kl_accept_cb.isChecked()) {
                    Toast.makeText(KLRegisterActivity.this, "请勾选用户隐私协议方可继续",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.register(this, user, password, "", RegisterParams.TYPE_ACCOUNT);
            }

        } else if (v.getId() == AppConfig.resourceId(this, "kl_agreement_tv", "id")) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("url", AppConstants.agree);
            intent.setClass(this, KLUserInfoActivity.class);
            startActivity(intent);
        } else if (v.getId() == AppConfig.resourceId(this, "kl_back_iv", "id")) {
            if (isChangePage) {
                kl_viewPager.setCurrentItem(1);
                isChangePage = false;
            } else {
//                KLLoginActivity.startThisActivity(KLRegisterActivity.this);
                finish();
            }
        }
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

    //显示实名验证界面
    private void showVerifyView(String url) {
        // 实名验证
        url = Base64.decode(url);
        if (TextUtils.isEmpty(url)) return;  //为空直接跳出该方法
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.setClass(this, KLUserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void registerSuccess(String msg) {
        AppConfig.isShow = true;
        showToastMsg(msg);
        if (TextUtils.isEmpty(user)) {
            user = phone;
        }
        KLAppManager.getInstance().finishAllActivity(KLRegisterActivity.class);
        if (mType == TYPE_PHONE) {
            KLLoginActivity.startQuickLoginActivity(KLRegisterActivity.this, phone, password);
        } else {
            KLLoginActivity.startQuickLoginActivity(KLRegisterActivity.this, user, password);
        }
        KLRegisterActivity.this.finish();
    }

    @Override
    public void getCodeSuccess() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    @Override
    public void showMsg(String msg) {
        showToastMsg(msg);
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }

    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            isChangePage = true;
            kl_viewPager.setCurrentItem(0);
        }

        @Override
        public void updateDrawState(TextPaint ds) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
