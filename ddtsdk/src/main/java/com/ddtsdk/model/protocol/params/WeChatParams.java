package com.ddtsdk.model.protocol.params;

public class WeChatParams {
    private String code;
    private String wxappid;

    public WeChatParams(String code, String wxappid){
        this.code = code;
        this.wxappid = wxappid;
    }
}
