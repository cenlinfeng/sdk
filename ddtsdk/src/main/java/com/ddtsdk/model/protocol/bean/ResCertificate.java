package com.ddtsdk.model.protocol.bean;

/**
 *careted by 2019/11/25
 * 实名认证服务端返回参数
 */
public class ResCertificate {
    private boolean result;  //实名结果： true成功，  false失败
    private String msg;  //提示信息
    private int isnonage;   //是否已经未成年人，【1-未成年人，0-成年人】
    private int isautonym;   //是否已经实名，【1-已实名，0-未实名】
    private int type;   //年龄阶段，【0是没有实名，1是8至15周岁，2是16至18周岁，3是已成年】
    private int isbanlogin;  //  是否强制下线，【0-否，1-是】
    private int downtime;  //  倒计时时间，【单位是秒】

    public int getDowntime() {
        return downtime;
    }

    public void setDowntime(int downtime) {
        this.downtime = downtime;
    }

    public int getIsbanlogin() {
        return isbanlogin;
    }

    public void setIsbanlogin(int isbanlogin) {
        this.isbanlogin = isbanlogin;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsnonage() {
        return isnonage;
    }

    public void setIsnonage(int isnonage) {
        this.isnonage = isnonage;
    }

    public int getIsautonym() {
        return isautonym;
    }

    public void setIsautonym(int isautonym) {
        this.isautonym = isautonym;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
