package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.listener.OnLimitClickHelper;
import com.ddtsdk.listener.OnLimitClickListener;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.network.OnLineTimeRequest;
import com.ddtsdk.ui.adapter.DdtFragmentPagerAdapter;
import com.ddtsdk.ui.fragment.KLAccountLoginFragment;
import com.ddtsdk.ui.fragment.KLPhoneLoginFragment;
import com.ddtsdk.ui.view.LoadingDialog;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.PayPointReport;
import com.ddtsdk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/4/20.
 * 登录页
 */
public class KLLoginActivity extends BaseActivity implements OnLimitClickListener {
	private RadioButton login_verification_rb;
	private RadioButton login_account_rb;
	private RadioButton login_quick_rb;
	private LinearLayout register_title;
	private LinearLayout register_content;
	private View indicator1;
	private View indicator2;
	private View indicator3;
	private RelativeLayout kl_more_game_rl;
	private TextView kl_more_game;
	private TextView kl_register_account;
	private TextView kl_register_phone;
	private ViewPager viewPager;
	private ImageView mLogo;
	private LoadingDialog mLoadingDialog;

	List<Fragment> mFragmentList = new ArrayList<>();

	private DdtFragmentPagerAdapter mAdapter;

	public static void startThisActivity(Activity activity) {
		Intent intent = new Intent(activity, KLLoginActivity.class);
		activity.startActivity(intent);
	}

	public static void startThisActivity(Activity activity,int index) {
		Intent intent = new Intent(activity, KLLoginActivity.class);
		intent.putExtra("index",index);
		activity.startActivity(intent);
	}

	public static void startQuickLoginActivity(Activity activity,String uname,String pwd) {
		Intent intent = new Intent(activity, KLLoginActivity.class);
		intent.putExtra("uname",uname);
		intent.putExtra("pwd",pwd);
		activity.startActivity(intent);
	}

	@Override
	protected String layoutName() {
		return Constants.login_main;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		initData();
		initView();
		initListener();
	}

	protected void initView() {
		login_verification_rb = findViewById(resourceId("login_verification_rb","id"));
		login_account_rb = findViewById(resourceId("login_account_rb","id"));
		login_quick_rb = findViewById(resourceId("login_quick_rb","id"));
		indicator1 = findViewById(resourceId("indicator1","id"));
		indicator2 = findViewById(resourceId("indicator2","id"));
		indicator3 = findViewById(resourceId("indicator3","id"));
		kl_register_account = findViewById(resourceId("kl_register_account","id"));
		kl_register_phone = findViewById(resourceId("kl_register_phone","id"));
		kl_more_game_rl = findViewById(resourceId("kl_more_game_rl","id"));
		kl_more_game = findViewById(resourceId("kl_more_game","id"));
		register_title = findViewById(resourceId("mks_register_title","id"));
		register_content = findViewById(resourceId("mks_register_content","id"));

		kl_more_game.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_more_game",AppConstants.colorPrimary),null,null);
		mLogo = findViewById(resourceId("logoimg","id"));
		viewPager = findViewById(resourceId("viewPager","id"));
		mLoadingDialog = new LoadingDialog(this);

		if (TextUtils.isEmpty(AppConstants.more_game)){
			kl_more_game_rl.setVisibility(View.GONE);
		} else {
			kl_more_game_rl.setVisibility(View.VISIBLE);
			kl_more_game.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(AppConstants.more_game));
					startActivity(intent);
					PayPointReport.getInstance().pushPoint(25);
				}
			});
		}
		//关闭注册
		if (AppConstants.banres){
			register_content.setVisibility(View.GONE);
			register_title.setVisibility(View.GONE);
			login_quick_rb.setVisibility(View.GONE);
		}

		if (AppConstants.login_config == 1){
			kl_register_phone.setText("手机号注册");
		} else {
			kl_register_phone.setText("手机注册");
		}

		initViewPager();

		ImageUtils.loadImage(mLogo,AppConstants.logo_img);

		kl_register_account.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_register_account", AppConstants.colorPrimary),null,null);
		kl_register_phone.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_register_phone",AppConstants.colorPrimary),null,null);
		kl_more_game.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_more_game",AppConstants.colorPrimary),null,null);
	}

	private void initViewPager() {
		KLAccountLoginFragment mAccountFragment = new KLAccountLoginFragment();
		KLPhoneLoginFragment mPhoneFragment = new KLPhoneLoginFragment();
		Fragment mEmptyFragment = new Fragment();

		mFragmentList.add(mPhoneFragment);
		mFragmentList.add(mAccountFragment);
		mFragmentList.add(mEmptyFragment);
		mAdapter = new DdtFragmentPagerAdapter(getFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				if (mFragmentList.get(position) instanceof KLAccountLoginFragment){
					if (getIntent().getExtras() != null) {
						String uname = getIntent().getStringExtra("uname");
						String pwd = getIntent().getStringExtra("pwd");
						KLAccountLoginFragment accountFragment = (KLAccountLoginFragment) mFragmentList.get(position);
						accountFragment.setQuickValue(uname, pwd);
					}
				}
				return mFragmentList.get(position);
			}

			@Override
			public int getCount() {
				return mFragmentList.size();
			}
		};
		viewPager.setAdapter(mAdapter);
	}

	protected void initListener() {
		kl_register_account.setOnClickListener(new OnLimitClickHelper(this));
		kl_register_phone.setOnClickListener(new OnLimitClickHelper(this));

		login_verification_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					buttonView.setTextColor(Color.parseColor("#333333"));
					buttonView.setTextSize(14);
					indicator1.setVisibility(View.VISIBLE);
					viewPager.setCurrentItem(0);
					PayPointReport.getInstance().pushPoint(20);
				} else {
					buttonView.setTextColor(Color.parseColor("#666666"));
					buttonView.setTextSize(12);
					indicator1.setVisibility(View.INVISIBLE);
				}
			}
		});
		login_account_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					buttonView.setTextColor(Color.parseColor("#333333"));
					buttonView.setTextSize(14);
					indicator2.setVisibility(View.VISIBLE);
					viewPager.setCurrentItem(1);
					PayPointReport.getInstance().pushPoint(16);
				} else {
					buttonView.setTextColor(Color.parseColor("#666666"));
					buttonView.setTextSize(12);
					indicator2.setVisibility(View.INVISIBLE);
				}
			}
		});
		login_quick_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					buttonView.setTextColor(Color.parseColor("#333333"));
					buttonView.setTextSize(14);
					indicator3.setVisibility(View.VISIBLE);
					KLVisitorRegisterActivity.startThisActivity(KLLoginActivity.this);
					Log.e("dddddd", "========");
					KLLoginActivity.this.finish();
				} else {
					buttonView.setTextColor(Color.parseColor("#666666"));
					buttonView.setTextSize(12);
					indicator3.setVisibility(View.INVISIBLE);
				}
			}
		});
		int index = getIntent().getIntExtra("index",0);
		if (index == 1){
			login_account_rb.setChecked(true);
		} else {
			login_verification_rb.setChecked(true);
		}
	}

	public void loginSuccess(final LoginMessage loginMessage){
		AppConfig.isShow = true;
		if (!TextUtils.isEmpty(loginMessage.getFloat_url())){
			FloatUtlis.getInstance().setFloatItems(loginMessage.getFloat_url());
		} else if (!TextUtils.isEmpty(loginMessage.getFloat_menu_new())) {
			FloatUtlis.getInstance().hideFloatItems(loginMessage.getFloat_menu_new());
		}
		BaseKLSDK.getInstance().onLoadH5Url();
		toRealNameView();
		finish();
	}

	private void toRealNameView() {
		/**
		 * 登录成功
		 * AppConstants.isautonym == 0 && AppConfig.forceautonym != 0 表示未实名并且需要进行实名的用户
		 * 弹出实名认证界面框
		 */
		if (AppConstants.isautonym == 0 && AppConstants.forceautonym != 0){
			RealNameActivity.startThisActivity(this);
		} else {
			BaseKLSDK.getInstance().wrapLoginInfo();
			OnLineTimeRequest.get().onlineTime();
		}
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == AppConfig.resourceId(this, "kl_register_account", "id")) {
			// 账号注册
			KLRegisterActivity.startThisActivity(this,KLRegisterActivity.TYPE_ACCOUNT);
		} else if (v.getId() == AppConfig.resourceId(this, "kl_register_phone", "id")) {
			// 手机号注册
			KLRegisterActivity.startThisActivity(this,KLRegisterActivity.TYPE_PHONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK){
			return;
		}
		Fragment fragment = mFragmentList.get(viewPager.getCurrentItem());
		if (fragment != null){
			fragment.onActivityResult(requestCode,resultCode,data);
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