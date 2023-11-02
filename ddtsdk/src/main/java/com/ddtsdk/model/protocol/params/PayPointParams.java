package com.ddtsdk.model.protocol.params;

public class PayPointParams {
    private String gtype = "1"; //游戏类型 1:安卓 2:IOS 3:H5 4:小程序 5:小游戏
    private String idfa = "";
    private int rtype;
    private String paramquery;
    private String ext;

    public PayPointParams(int rtype, String paramquery, String ext){
        this.rtype = rtype;
        this.paramquery = paramquery;
        this.ext = ext;
    }

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getParamquery() {
        return paramquery;
    }

    public void setParamquery(String paramquery) {
        this.paramquery = paramquery;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
