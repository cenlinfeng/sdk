package com.ddtsdk.model.data;

/**
 * Created by CZG on 2020/4/17.
 */
public class UserData extends BaseUserData{
    private String userurl;//用户信息地址
    private String orderurl;//订单地址
    private String libaourl;
    private String service;

    public String getUserurl() {
        return userurl;
    }

    public void setUserurl(String userurl) {
        this.userurl = userurl;
    }

    public String getOrderurl() {
        return orderurl;
    }

    public void setOrderurl(String orderurl) {
        this.orderurl = orderurl;
    }

    public String getLibaourl() {
        return libaourl;
    }

    public void setLibaourl(String libaourl) {
        this.libaourl = libaourl;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
