package com.ddtsdk.othersdk.manager.params;

public class BaseParam {
    private String ver;
    private String type;
    private String msg;
    private String status;
    private String data;
    private String stype;
    private int appid;

    public void setVer(String ver) {
        this.ver = ver;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }
}
