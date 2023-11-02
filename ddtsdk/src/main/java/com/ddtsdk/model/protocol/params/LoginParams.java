package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/15.
 */
public class LoginParams {

    private String uname;       //用户名
    private String password;    //密码

    public LoginParams(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }
}
