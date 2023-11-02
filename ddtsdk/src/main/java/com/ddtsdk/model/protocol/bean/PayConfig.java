package com.ddtsdk.model.protocol.bean;

import com.ddtsdk.model.data.NotReceive;
import com.ddtsdk.model.data.PayData;
import com.ddtsdk.model.data.VoucherData;

import java.util.List;

public class PayConfig {

	private String msg;
	private float glod;

	private float money;
	private float rate;
	private List<PayData> paylist;
	private List<VoucherData> voucherlist; // 优惠卷数据
	private List<NotReceive> notReceive; //未领取的代金卷

	private int isautonym;   // 2019/11/25  新增实名参数  1是已实名  0是未实名
	private int isnonage;  //2019/11/25  新增实名参数  1是未成年人

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public float getGlod() {
		return glod;
	}

	public void setGlod(float glod) {
		this.glod = glod;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public List<PayData> getPaylist() {
		return paylist;
	}

	public void setPaylist(List<PayData> paylist) {
		this.paylist = paylist;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getIsautonym() {
		return isautonym;
	}

	public void setIsautonym(int isautonym) {
		this.isautonym = isautonym;
	}

	public int getIsnonage() {
		return isnonage;
	}

	public void setIsnonage(int isnonage) {
		this.isnonage = isnonage;
	}

	public List<VoucherData> getVoucherList() {
		return voucherlist;
	}

	public void setVoucherList(List<VoucherData> voucherlist) {
		this.voucherlist = voucherlist;
	}

	public List<NotReceive> getNotReceive() {
		return notReceive;
	}

	public void setNotReceive(List<NotReceive> notReceive) {
		this.notReceive = notReceive;
	}
}
