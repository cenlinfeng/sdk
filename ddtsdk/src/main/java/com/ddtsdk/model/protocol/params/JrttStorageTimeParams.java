package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/16.
 */
public class JrttStorageTimeParams {
    private String level;
    private String roleid;
    private int min;
    private String os;

    public JrttStorageTimeParams(String level, String roleid) {
        this.level = level;
        this.roleid = roleid;
        this.min = 1;
        this.os = "android";
    }
}
