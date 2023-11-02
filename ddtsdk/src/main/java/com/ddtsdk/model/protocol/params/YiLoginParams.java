package com.ddtsdk.model.protocol.params;

/**
 * Created on 2020/11/2.
 */
public class YiLoginParams {
    private String tokenInfo;
    private String carrier;
    private String packname;
    private String itype = "1";
    private String accessToken;

    public YiLoginParams(String tokenInfo, String carrier, String packname) {
        this.tokenInfo = tokenInfo;
        this.carrier = carrier;
        this.packname = packname;
    }

    public YiLoginParams(String tokenInfo, String carrier, String packname, String accessToken) {
        this.tokenInfo = tokenInfo;
        this.carrier = carrier;
        this.packname = packname;
        this.accessToken = accessToken;
    }
}
