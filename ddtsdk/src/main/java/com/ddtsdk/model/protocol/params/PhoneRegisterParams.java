package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/15.
 */
public class PhoneRegisterParams {

    private String mobile;      //手机号
    private String password;    //密码
    private String verifycode;  //验证码

    public PhoneRegisterParams(String mobile, String password, String verifycode) {
        this.mobile = mobile;
        this.password = password;
        this.verifycode = verifycode;
    }
}
