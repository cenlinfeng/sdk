package com.ddtsdk.model.data;

import com.ddtsdk.model.protocol.bean.InitMsg;

import java.io.Serializable;

public class LoginMessageInfo implements Serializable {
	private String result;
	private String msg;
	private String gametoken;
	private String time;
	private String uid;
	private String sessid;
	private String dologin_h5;//h5_game专用
	private String mobile;  //手机号  2020/11/4
	private int is_test; //是否是测试包

	public int getIs_test() {
		return is_test;
	}

	public void setIs_test(int is_test) {
		this.is_test = is_test;
	}

	public String getDologin_h5() {
		return dologin_h5;
	}

	public void setDologin_h5(String dologin_h5) {
		this.dologin_h5 = dologin_h5;
	}

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getGametoken() {
		return gametoken;
	}
	public void setGametoken(String gametoken) {
		this.gametoken = gametoken;
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
	public String getSessid() {
		return sessid;
	}
	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


}
