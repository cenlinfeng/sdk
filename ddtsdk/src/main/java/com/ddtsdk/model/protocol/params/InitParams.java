package com.ddtsdk.model.protocol.params;

import com.ddtsdk.model.data.DeviceInfo;

/**
 * Created by CZG on 2020/4/13.
 */
public class InitParams {

    private String screen;
    private String serial;
    private String systeminfo;
    private String sysversion;
    private String model;
    private String imsi;
    private String mac;
    private String idfa;
    private String idfv;
    private String iscrack;
    private String ua;//urlencode(deviceInfo.getUserua()));//***暂时不要传递，不然/CenterToPay接口会请求失败
    private String mobile;
    private String packname;

    public InitParams(DeviceInfo deviceInfo) {
        this.screen = deviceInfo.getDeviceScreen();
        this.serial = deviceInfo.getSerialId();
        this.systeminfo = deviceInfo.getSystemInfo();
        this.sysversion = deviceInfo.getSystemVer();
        this.model = deviceInfo.getModel();
        this.imsi = deviceInfo.getImsi();
        this.mac = deviceInfo.getMac();
        this.mobile = deviceInfo.getNativePhoneNumber();
        this.idfa = "";
        this.idfv = "";
        this.iscrack = (deviceInfo.getIsbreak() ? "0" : "1");
        this.ua = "";
        this.packname = deviceInfo.getPackname();
    }
}
