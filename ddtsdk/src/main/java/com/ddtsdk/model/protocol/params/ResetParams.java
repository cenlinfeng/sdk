package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/16.
 */
public class ResetParams {
    private String mobile;
    private String verifycode;
    private String pwd;
    private String from;

    public ResetParams(String mobile, String verifycode, String pwd, String from) {
        this.mobile = mobile;
        this.verifycode = verifycode;
        this.pwd = pwd;
        this.from = from;
    }
}
