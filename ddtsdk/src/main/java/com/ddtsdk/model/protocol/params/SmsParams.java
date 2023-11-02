package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/15.
 */
public class SmsParams {
    public static final int REGISTER = 1;
    public static final int BINDMOBILE = 2;
    public static final int RESETPASSWORD = 3;
    public static final int PHONELOGIN = 4;

    private String mobile;  //手机号
    private int type;    //1手机注册，2绑定手机，3找回密码
    private String testsms;

    public SmsParams(String mobile, int type) {
        this.mobile = mobile;
        this.type = type;
        this.testsms = "test";
    }
}
