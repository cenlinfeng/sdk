package com.ddtsdk.othersdk.manager;

public class EventFlag {
    public static final int APPLICATIONCREATE = 0;  //Appcation Create
    public static final int REGISTER = 1;   //注册
    public static final int PAY = 2;  // 付费
    public static final int ACTIVITYCREATE = 3;  //activty onCreate
    public static final int ACTIVITYRESUME = 4;    //activty onStop
    public static final int ACTIVITYPAUSE = 5;   //activty onPause
    public static final int ACTIVITYSTOP = 6;    //activty onStop
    public static final int ACTIVITYDESTORY = 7; //activty onDestory
    public static final int USETUNIQUE = 8; //用户唯一标识
    public static final int EXIT = 9; //游戏退出
    public static final int ACTIVATION = 9; //激活

    public static final String USERTYPE = "userType"; //用户注册方式：手机，游客，账号等
    /**
     * 注册,付费日志上报服务端所需参数
     */
    public static final String MSG = "msg";   //上报前面标识， 1为广告上报前，2为广告上报结果
    public static final String STATUS = "status";   //广告上报的状态， success上报成功， error为上报失败
    public static final String STYPE = "stype";   //广告上报类型：register为注册, pay为付费
    public static final String USERNAME = "userName";   //用户名称
    public static final String VISITOR = "visitor";   //注册方式， 为1则为一键注册
    public static final String APPID = "appid";   //  appid
    public static final String CHANNEL = "channel";   //渠道号
    public static final String IMEI = "imei";   //imei
    public static final String OAID = "oaid";   //oaid
    public static final String ORDERID = "orderid";   //订单号
    public static final String AMOUNT = "amount";   //金额
    public static final String UID = "uid";   //用户id
    public static final String EXTRA = "extra";   //附加信息
    public static final String PAYCHAR = "paychar";   //附加信息
}
