package com.ddtsdk.constants;

import com.ddtsdk.model.protocol.params.PayParams;
import com.ddtsdk.othersdk.manager.params.PayParam;

/**
 * Created by CZG on 2020/4/14.
 */
public class AppConstants {
    public final static String DEVICE = "1";
    public static String sdkflag = "2019.11.27";   //  sdk标志位， 通过这个标志位，服务器可以知道当前sdk是否是最新SDK，便于新旧版本的兼容
    public static String colorPrimary = "#ff9201";
    //  权限存放目录
    public final static String KL_PER_XML = "kl_per_xml";

    //App初始化信息
    public static int appId = 0;
    public static String appKey = "";
    public static String ddt_ver_id = "";
    public static String appVer = "";
    public static String Sessid = "";
    public static String Token = "";
    public static String FPWD = "";//找回密码
    public static String Strategy_site = ""; //2.0新增 游戏攻略网址
    public static String agree = "";//用户协议
    public static String logo_img = ""; //2.1新增 界面logo
    public static String float_img = ""; //2.1新增 悬浮logo
    public static int minutetime = 2;   //2019/11/25  新增实名参数  游戏在线时长上报（分钟为单位） 默认2分钟上报一次
    public static String qq = "";
    public static String phone = "";
    public static String wx_qrcode = "";
    public static String wx_name = "";
    public static String customer_qq = "";
    public static int openillustrate = 0;  //游客说明  0
    public static String H5LoginLink = "";  //202/9/14 新增h5登录链接，需本地拼接参数
    public static int pack_model = 2;  //1红包 , 2平台
    public static int login_config = 0; // 0:默认手机注册 1:网易一键登录注册
    public static String more_game = ""; //更多游戏
    public static String Oaid = ""; //用户系统版本大于10的唯一标识
    public static boolean banres = false; //true为开启该渠道用户禁止注册
    //弹窗
    public static int is_adv = 0;
    public static String adv_url = "";
    public static String adv_img = "";
    public static int source = 0;

    //payinfo
    public static String cp_orderId = "";  //游戏订单号
    public static String cp_amount = "";  //游戏订单号
    public static PayParams mPayParam = null;


    //注册
    public static int control_status = 0;  //0正常上报，1：增缩量上报
    public static String control_type = "";   //类型：increment 增量，decrement 缩量
    public static String threshold_value = "";  //增缩量的条数；缩量不用理，增量需要根据这个条数进行模拟生成数据上报

    //初始化
    public static String init_type = "decrement";   //类型：increment 增量，decrement 缩量
    public static boolean adopen = false;  //是否开启广告上报
    public static int sdk_track = 0;  //是开启埋点上报  sdk_track 为 1，则开启埋点上报，否则 0 不上报
    public static String paramquery = "";

    //用户相关信息
    public static String regType = "";  //用户注册方式   userReg账号注册，fastReg快速注册，phoneReg手机注册
    public static String uid = ""; // 用户uid
    public static String userName = "";// 用户名
    public static String gametoken = "";
    public static String time = "";
    //    public static String visitor=""; //  "1"表示为一键注册
    public static String orderUrl = "";//订单地址
    public static String serviceUrl = "";
    public static String userUrl = "";//用户信息地址
    public static String libaourl = "";
    public static String valid = "";//2.0新增 实名认证
    public static String dologin_h5 = "";//h5_game专用
    public static String platformUrl = "";//平台地址
    public static int isautonym = 0;   // 2019/11/25  新增实名参数  1是已实名  0是未实名
    public static int forceautonym = 0;   //2019/11/25  新增实名参数  0 不需要实名 1 强制实名 2 可以跳过实名
    public static int isnonage = 0;     //2019/11/25  新增实名参数  1是未成年人  0是成年人
    public static int isolduser = 1;  //判断当前渠道用户是否是第一次登录，  0代表新用户， 1代表老用户
    public static int canlogin = 1;//1是能拉起登陆，0是不能拉起登陆
    public static int YSDKcanlogin = 1;//1是能拉起登陆，0是不能拉起登陆

    public static String box_packgeNme = "com.ddt.platform.gamebox";

    public static String platform_pay = "";  //平台h5支付url，如果不为空加载平台支付界面，否则加载本地界面支付
    //http://h5frontend.ddtugame.com/platform/sdk/1.2.0/pay/index.html

    //注册弹窗
    public static int reg_is_adv = 0;
    public static String reg_adv_url = "";
    public static String reg_adv_img = "";
    public static int reg_source = 0;
    public static int reg_is_type = 0; //1那注册的，0拿登陆的

    //初始化弹窗
    public static int init_is_adv = 0;
    public static String init_adv_url = "";
    public static String init_adv_img = "";

    //游戏横竖屏
    public static int isLandscape = 0; //0:竖屏  1：横屏

    //小游戏
    public static int isOpen_smallgame = 0;  //0:不打开小游戏   其他值打开小游戏,非0代表小游戏展示时间
    public static String small_game_url = "";  //小游戏加载链接

    public static Boolean isInit = false; // true是初始化接口，false不是初始化接口
    public static Boolean banpackage = true; //true禁止抓包

    public static int pay_realname = 0;

    public static int is_test = 0; //是否是测试包 0为不是

    public static boolean is_update = false; //如果要新版本更新就不要拉起登录框

    public static boolean is_voucher = false; //如果用户有使用的代金卷则为true


}

