package com.ddtsdk.constants;

/**
 * Created by CZG on 2020/4/14.
 */
public class ApiConstants {
    // 初始化接口
    public static final String ACTION_INIT = "/Api/Common/SdkInit";
    // 更新接口
    public static final String ACTION_UPDATE = "/Api/Common/SdkUpdate";
    // 请求短信
    public static final String ACTION_SMS = "/Api/Common/RequestSMS";
    // 注册
    public static final String ACTION_REGISTER = "/Api/Common/Register";
    // 手机注册
    public static final String ACTION_PHONE_REGISTER = "/Api/Common/PhoneRegister";
    // 登录
    public static final String ACTION_LOGIN = "/Api/Common/Login";
    //判断设备是否已经存在,广点通SDK注册上报去重的
    public static final String ACTION_GDT_DATA = "/Api/Common/Judge_uniqueuser";
    //头条上报统计，每分钟上报一次
    public static final String ACTION_JRTTSTORAGETIME = "/Api/Common/jrttStorageTime";
    // 2019/11/25添加实名认证Api
    // 查询是否已实名认证
    public static final String ACTION_GETCERTIFICATE = "/Api/Member/getcertificate";
    // 实名认证
    public static final String ACTION_CERTIFICATEVERIFY = "/Api/Member/certificateverify";
    // 查询用户在线时长
    public static final String ACTION_ONLINETIME = "/Api/Member/onlinetime";
    //提交用户信息
    public static final String ACTION_ROLEINFO	= "/Api/Rolemember/Roleinfo";
    //提交用户应用列表
    public static final String ACTION_APPLIST = "/Api/Member/UpAppInfo";
    //支付配置
    public static final String ACTION_GETPAY = "/Api/Member/GetPay";
    //支付订单
    public static final String ACTION_CENTERTOPAY = "/Api/Member/CenterToPay";
    //广告日志上报
    public static final String ACTION_REPORTLOG = "/Api/Log/androidYybPayStatus";
    //登出接口
    public static final String ACTION_LOGINOUT	= "/Api/Member/Loginout";
    //找回密码
    public static final String ACTION_RESETPWD	= "/Api/Common/ResetPwd";
    //绑定手机
    public static final String ACTION_BIND_PHONE	= "/Api/member/bind_phone";
    //手机验证码登录
    public static final String ACTION_LOGIN_PHONE	= "/Api/Common/phoneLogin";
    //付费结果查询
    public static final String ACTION_QUERY_PAY	= "/Api/member/demandorder";
    //微信code上传
    public static final String ACTION_WECHAT_CODE	= "/Api/Member/bindingUsersnew";
    //退出游戏按钮接口
    public static final String ACTION_EXIT	= "/Sdk/Reference/Quite";
    //用户时长上报
    public static final String ACTION_REPORT	= "/api/Member/gaintime";
    //付费增缩量上报
    public static final String ACTION_PAY_REPORT	= "/Api/common/checkVerIsControl";
    //网易一键登陆
    public static final String ACTION_WYYILOGIN	= "/Api/common/getWyPhone";
    //分享接口
    public static final String ACTION_ShARE	= "/Sdk/Popup/share";
    //付费打点上报
    public static final String ACTION_PAY_POINT	= "/Api/Common/sdkTrack";
    //弹窗盒子跳转
    public static final String ACTION_SDK_POP	= "/Api/Common/SdkPop";
    //获取直充卷
    public static final String ACTION_EXCLUSIVE_VOLUME = "/Api/Member/getVoucherGoods";
    //心跳上报接口
    public static final String Action_HEARTBEAT  = "/Api/heartbeat/upSession";
    //激活码
    public static final String ACTION_CODE_KEY = "/Api/VoucherActivate/activateCode";
    //德州界面参数初始化
    public static final String ACTION_POKER_INIT = "/Api/Poker/init";
    //德州开始游戏
    public static final String ACTION_POKER_PLAY = "/Api/Poker/play";
    //购买平台币界面配置数据
    public static final String ACTION_BUY_PLATFORM_INIT = "/Api/Money/GetMoneyConfig";
    //平台币充值
    public static final String ACTION_BUY_PLATFORM_PAY = "/Api/Money/CenterToPay";
    //隐藏悬浮框配置
    public static final String ACTION_HIDE_FLOAT_MENU = "/Api/Float/menu";
}
