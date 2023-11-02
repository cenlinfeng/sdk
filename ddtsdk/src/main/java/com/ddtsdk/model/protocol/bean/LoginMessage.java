package com.ddtsdk.model.protocol.bean;

import android.text.TextUtils;


import com.ddtsdk.mylibrary.BuildConfig;

import org.json.JSONArray;

public class LoginMessage {
	public static final String INCREMENT = "increment";
	public static final String DECREMENT = "decrement";

	private String gametoken;
	private String time;
	private String uid;
	private String userurl;//用户信息地址
	private String orderurl;//订单地址
    private String uname;
    private String pwd;
    private String libaourl;
    private String service;
	private String valid; //2.0新增 实名认证
	private String tuijian; //2.0新增 推荐链接
	private int control_status; //2.1新增  头条控量上报   0 正常上报  1 控量上报
	private String control_type;  //新增 头条控量上报   increment 增量      decrement  缩量
	private String threshold_value;  //  增量或者缩量阈值
	private int isautonym;   // 2019/11/25  新增实名参数  1是已实名  0是未实名
	private int forceautonym;  //2019/11/25  新增实名参数  0 不需要实名 1 强制实名 2 可以跳过实名
	private int isnonage;  //2019/11/25  新增实名参数  1是未成年人
	private int isolduser;   //判断当前渠道用户是否是第一次登录，  0代表新用户， 1代表老用户
	private String dologin_h5; //h5登录链接
	private String float_url;	//点击悬浮窗展示的平台链接（如果这项有值，则忽略掉float_menu）
	private String platform_cash;  //代金券支付拼接链接   2020/9/3
	private String mobile;  //手机号  2020/11/4
	private String repregister;  // 网易易盾注册是否是第一次注册
	private AdvInfo adv_info;
	private boolean voucher;
	private String float_menu_new;//2.1新增  控制悬浮球内部功能

	public String getFloat_menu_new() {
		return float_menu_new;
	}

	public boolean isVoucher() {
		return voucher;
	}


	public String getFloat_url() {
		return !TextUtils.isEmpty(float_url) && BuildConfig.PLATFORM ? float_url : "";
	}

	public void setFloat_menu_new(String float_menu_new) {
		this.float_menu_new = float_menu_new;
	}

	public String getDologin_h5() {
		return dologin_h5;
	}

	public String getValid() {
		return valid;
	}

	public String getLibaourl() {
		return libaourl;
	}

	public String getGametoken() {
		return gametoken;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUserurl() {
		return userurl;
	}

	public String getOrderurl() {
		return orderurl;
	}

	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public int getControl_status() {
		return control_status;
	}



	public String getControl_type() {
		return control_type;
	}



	public String getThreshold_value() {
		return threshold_value;
	}


	public int getIsautonym() {
		return isautonym;
	}

	public void setIsautonym(int isautonym) {
		this.isautonym = isautonym;
	}

	public int getForceautonym() {
		return forceautonym;
	}

	public void setForceautonym(int forceautonym) {
		this.forceautonym = forceautonym;
	}

	public int getIsnonage() {
		return isnonage;
	}


	@Override
	public String toString() {
		return  "LoginMsg回调-----"+
				"gametoken:"+gametoken +
				",time:"+time +
				",uid:"+uid +
				",userurl:"+userurl +
				",orderurl:"+orderurl +
				",uname:"+uname +
				",pwd:"+pwd +
				",libaourl:"+libaourl +
				",service:"+service +
				",Valid:"+valid +
				",tuijian:"+tuijian;
	}

	public int getIsolduser() {
		return isolduser;
	}


	public String getPlatform_cash() {
		return platform_cash;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRepregister() {
		return repregister;
	}

	public AdvInfo getAdv_info() {
		return adv_info;
	}

	public class AdvInfo{
		private int is_adv; // 是否开启弹窗，0:否 1:是
		private String adv_url; // 弹窗跳转链接
		private String adv_img; // 弹窗图片地址
		private int source = 0;  //1为代金券弹窗， 0为普通弹窗

		public int getIs_adv() {
			return is_adv;
		}


		public String getAdv_url() {
			return adv_url;
		}


		public String getAdv_img() {
			return adv_img;
		}

		public int getSource() {
			return source;
		}

		public void setSource(int source) {
			this.source = source;
		}
	}
}
