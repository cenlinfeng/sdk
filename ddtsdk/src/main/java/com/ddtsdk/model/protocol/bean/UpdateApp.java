package com.ddtsdk.model.protocol.bean;

public class UpdateApp {
	/*
	 * "result": true, 表示请求成功 
	 * "msg": "请求成功",
	 * "versionurl":"http://bxxxx.com/sjkf/a734.apk",【包更新地址】
	 *  "newversion":true, 【是否有新版本，false的时候versionurl字段为空】 
	 *  "updatecontent":” http://politics.cn/update.php”, 【更新提示信息返回URL地址】 
	 *  "updatetype":, 【更新类别，1强制，2选则】
	 *  "announcementurl":"http://politics.cn/07662.html",【公告地址】
	 * "announcemenstatus":true 【true需要展示公告，false不需要展示公告】
	 */
	private String msg;
	private Boolean result;
	private String versionurl;
	private Boolean newversion;
	private String updatecontent;
	private String updatetype;
	private String announcementurl;
	private Boolean announcemenstatus;

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public String getVersionurl() {
		return versionurl;
	}
	public void setVersionurl(String versionurl) {
		this.versionurl = versionurl;
	}
	public Boolean getNewversion() {
		return newversion;
	}
	public void setNewversion(Boolean newversion) {
		this.newversion = newversion;
	}
	public String getUpdatecontent() {
		return updatecontent;
	}
	public void setUpdatecontent(String updatecontent) {
		this.updatecontent = updatecontent;
	}
	public String getAnnouncementurl() {
		return announcementurl;
	}
	public String getUpdatetype() {
		return updatetype;
	}
	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}
	public void setAnnouncementurl(String announcementurl) {
		this.announcementurl = announcementurl;
	}
	public Boolean getAnnouncemenstatus() {
		return announcemenstatus;
	}
	public void setAnnouncemenstatus(Boolean announcemenstatus) {
		this.announcemenstatus = announcemenstatus;
	}


//	@Override
//	public String toString() {
//		return "UpdateApp{" +
//				"msg='" + msg + '\'' +
//				", result=" + result +
//				", versionurl='" + versionurl + '\'' +
//				", newversion=" + newversion +
//				", updatecontent='" + updatecontent + '\'' +
//				", updatetype='" + updatetype + '\'' +
//				", announcementurl='" + announcementurl + '\'' +
//				", announcemenstatus=" + announcemenstatus +
//				'}';
//	}
}
