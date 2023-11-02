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
import com.ddtsdk.ui.fragment.KLVisitorLoginFragment;
import com.ddtsdk.ui.fragment.KLWYQuickLoginFragment;
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
public class KLFirstQuickLoginActivity extends BaseActivity implements OnLimitClickListener {
	private RadioButton login_wy_rb;
	private RadioButton login_quick_rb;
	private View indicator1;
	private View indicator2;
	private TextView kl_login_account;
	private TextView kl_login_phone;
	private RelativeLayout kl_more_game_rl;
	private TextView kl_more_game;
	private ViewPager viewPager;
	private ImageView mLogo;
	private LoadingDialog mLoadingDialog;
	private KLWYQuickLoginFragment mWYQuickLoginFragment;
	private KLVisitorLoginFragment mVisitorFragment;
	private RelativeLayout kl_main_view;

	List<Fragment> mFragmentList = new ArrayList<>();

	private DdtFragmentPagerAdapter mAdapter;

	public static void startThisActivity(Activity activity) {
		Intent intent = new Intent(activity, KLFirstQuickLoginActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected String layoutName() {
		return Constants.first_login_quick;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		initListener();
	}

	protected void initView() {
		kl_main_view = findViewById(resourceId("kl_main_view","id"));
		login_wy_rb = findViewById(resourceId("login_wy_rb","id"));
		login_quick_rb = findViewById(resourceId("login_quick_rb","id"));
		indicator1 = findViewById(resourceId("indicator1","id"));
		indicator2 = findViewById(resourceId("indicator2","id"));
		kl_login_phone = findViewById(resourceId("kl_login_phone","id"));
		kl_login_account = findViewById(resourceId("kl_login_account","id"));
		kl_more_game_rl = findViewById(resourceId("kl_more_game_rl","id"));
		kl_more_game = findViewById(resourceId("kl_more_game","id"));

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

		initViewPager();

		ImageUtils.loadImage(mLogo,AppConstants.logo_img);

		kl_login_account.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_register_account", AppConstants.colorPrimary),null,null);
		kl_login_phone.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_register_phone",AppConstants.colorPrimary),null,null);
		kl_more_game.setCompoundDrawablesWithIntrinsicBounds(null,ImageUtils.tintDrawable(this,"kl_icon_more_game",AppConstants.colorPrimary),null,null);
	}

	private void initViewPager() {
		mWYQuickLoginFragment = new KLWYQuickLoginFragment();
		mVisitorFragment = KLVisitorLoginFragment.KLVisitorLoginFragment(kl_main_view);

		mFragmentList.add(mWYQuickLoginFragment);
		mFragmentList.add(mVisitorFragment);
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
		kl_login_account.setOnClickListener(new OnLimitClickHelper(this));
		kl_login_phone.setOnClickListener(new OnLimitClickHelper(this));

		login_wy_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
		login_quick_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					buttonView.setTextColor(Color.parseColor("#333333"));
					buttonView.setTextSize(14);
					indicator2.setVisibility(View.VISIBLE);
					viewPager.setCurrentItem(1);
					PayPointReport.getInstance().pushPoint(16);
					if (mVisitorFragment != null){
						mVisitorFragment.register();
					}
				} else {
					buttonView.setTextColor(Color.parseColor("#666666"));
					buttonView.setTextSize(12);
					indicator2.setVisibility(View.INVISIBLE);
				}
			}
		});
		login_wy_rb.setChecked(true);
	}

	public void loginSuccess(final LoginMessage loginMessage){
		AppConfig.isShow = true;
		if (!TextUtils.isEmpty(loginMessage.getFloat_url())){
			Log.e("KL_loginMessage",loginMessage+"=======");
			FloatUtlis.getInstance().setFloatItems(loginMessage.getFloat_url());
		} else if (!TextUtils.isEmpty(loginMessage.getFloat_menu_new())) {
			FloatUtlis.getInstance().hideFloatItems(loginMessage.getFloat_menu_new());
			Log.e("H5", "elseelseelseelseelseelseelseelse");
		}
		BaseKLSDK.getInstance().onLoadH5Url();
		Log.e("H5", "loginSuccess----------------onLoadH5Url");
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

	protected void initData() {
//		AppConstants.ddt_ver_id = Utils.getAgent(KLFirstQuickLoginActivity.this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == AppConfig.resourceId(this, "kl_login_phone", "id")) {
			KLFirstLoginActivity.startThisActivity(this,0);
			finish();
		} else if (v.getId() == AppConfig.resourceId(this, "kl_login_account", "id")){
			KLFirstLoginActivity.startThisActivity(this,1);
			finish();
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