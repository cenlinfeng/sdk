package com.ddtsdk.model.protocol.bean;

public class ResOnLineTime {
    private int login_time;  //登录时长
    private int isautonym;  //是否已经实名，【1-已实名，0-未实名】
    private int isbanlogin;  //  是否强制下线，【0-否，1-是】
    private int isnonage;  //  是否已经未成年人，【1-未成年人，0-成年人】
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLogin_time() {
        return login_time;
    }

    public void setLogin_time(int login_time) {
        this.login_time = login_time;
    }

    public int getIsautonym() {
        return isautonym;
    }

    public void setIsautonym(int isautonym) {
        this.isautonym = isautonym;
    }

    public int getIsbanlogin() {
        return isbanlogin;
    }

    public void setIsbanlogin(int isbanlogin) {
        this.isbanlogin = isbanlogin;
    }

    public int getIsnonage() {
        return isnonage;
    }

    public void setIsnonage(int isnonage) {
        this.isnonage = isnonage;
    }
}
