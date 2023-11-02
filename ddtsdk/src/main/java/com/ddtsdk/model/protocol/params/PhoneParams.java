package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/6/6.
 */
public class PhoneParams {
    private String mobile;    //手机号
    private String code;    //验证码

    public PhoneParams(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }
}
