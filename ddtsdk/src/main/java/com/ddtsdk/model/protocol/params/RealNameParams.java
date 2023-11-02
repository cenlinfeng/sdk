package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/15.
 */
public class RealNameParams {
    private String truename;       //真实姓名
    private String idcard;         //身份证

    public RealNameParams(String truename, String idcard) {
        this.truename = truename;
        this.idcard = idcard;
    }
}
