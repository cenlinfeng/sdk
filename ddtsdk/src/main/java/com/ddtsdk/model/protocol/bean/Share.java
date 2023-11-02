package com.ddtsdk.model.protocol.bean;


public class Share {
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIs_wake_up() {
        return is_wake_up;
    }

    public void setIs_wake_up(String is_wake_up) {
        this.is_wake_up = is_wake_up;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String appid;  //游戏appid
    private String name;  //游戏名称
    private String logo;  //游戏logo
    private String is_wake_up;  //是否新窗口打开
    private String url;  //游戏地址
}
