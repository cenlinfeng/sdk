package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/15.
 */
public class RegisterParams {

    public static final int TYPE_PHONE = 1;
    public static final int TYPE_ACCOUNT = 2;
    public static final int TYPE_VISITOR = 0;

    private String uname;       //用户名
    private String password;    //密码
    private String verifycode;  //验证码
    private String visitor;     //是否游客
    private int reg_type;    //类型

    public RegisterParams(String uname, String password, String verifycode, String visitor,int reg_type) {
        this.uname = uname;
        this.password = password;
        this.verifycode = verifycode;
        this.visitor = visitor;
        this.reg_type = reg_type;
    }
}
