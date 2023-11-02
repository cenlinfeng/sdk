package com.ddtsdk.constants;

import com.ddtsdk.mylibrary.BuildConfig;

/**
 * Created by CZG on 2020/3/31.
 */
public class Constants {

    //nas测试api   https://slb.ddt.lynaqi.com
    //测试服地址  api-test.ddt.ddtugame.com
    public static String baseHttp = "https://api.hnyoumeng.cn";

    static {
        switch (BuildConfig.BASE_HTTP) {
            case 0:
                baseHttp = "https://api.hnyoumeng.cn";
                break;
            case 1:
                baseHttp = "https://api.hnyoumeng.cn";
                break;
            case 2:
                break;
        }
    }

    //    public static final String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJEnr4EhRYNj/ts3WFnXwGkn/f" +
//            "AR/CdasC5eOR/RvWqgGGZRs1FgmUoY1fmF63M2Bp0J3jVn7ZqBNVcGdXq7GpxE7o" +
//            "PqeRNlYgeUrQgRov8/xzv/jMgMV261f69q6Fw0ywtjDrcrXz3kEU8XmS6el+c75y" +
//            "b7kkED+LbLpehJvG2wIDAQAB";
//    public static final String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMkSevgSFFg2P+2z" +
//            "dYWdfAaSf98BH8J1qwLl45H9G9aqAYZlGzUWCZShjV+YXrczYGnQneNWftmoE1Vw" +
//            "Z1ersanETug+p5E2ViB5StCBGi/z/HO/+MyAxXbrV/r2roXDTLC2MOtytfPeQRTx" +
//            "eZLp6X5zvnJvuSQQP4tsul6Em8bbAgMBAAECgYEAvSGZ82+mHVdiCKeOOtFokRIK" +
//            "H2yCQDrIUeCFj5OiNOgSqSS/U87X0iunvCypou87mCy35tg8V2WGUg551nhUkm04" +
//            "weFI3DQAHiC8do2M59/nzfiC0hhzYwb0jnlmsgaFJA3oaFs++qNFsaQiYtOrcUQ4" +
//            "ksJVhTwAhjvb6XcT9JECQQDsjg6uNI/pT6VUoRIhJK8IyY+5QNy1MG8MckFrsvXc" +
//            "ftwsJv7iU0LuImKcrPnqnZ1QkJEd+/6Odkq66+EBiMjpAkEA2Zm+D11GipCB6KPZ" +
//            "gD207Ng6IipQpm3Z5Lzpz6pQlCESKI3EMzuAZyzqi131aNW2I/98K2+yyZ5JaOMF" +
//            "OuR3IwJAA5+5gdm0SrK5qa4+BNv3An90ADaKKwxu5xXpAqlfMt2Oqe5/ASCdaeCE" +
//            "+jl8Kqf3fQB5+KeforcVNf/fFpLt4QJAfpXWn/+GIuOv/xMaW2UKVGHxZ6CwLDFp" +
//            "eYyAhAMzPwkCFD9sbNVnfB4AD7VIJ4VzoPtmU6p2Gp4PXIn8p+byewJBAOrKuBG5" +
//            "SI2zbPgevyl8L9BPfJJs7X5N0EtDRo415QPgswd2wkEEA0uuGi9ykS/T9OjohOED" +
//            "rc6CXAzizytIB7A=";
    public static final String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJEnr4EhRYNj/ts3WFnXwGkn/f" +
            "AR/CdasC5eOR/RvWqgGGZRs1FgmUoY1fmF63M2Bp0J3jVn7ZqBNVcGdXq7GpxE7o" +
            "PqeRNlYgeUrQgRov8/xzv/jMgMV261f69q6Fw0ywtjDrcrXz3kEU8XmS6el+c75y" +
            "b7kkED+LbLpehJvG2wIDAQAB";
    public static final String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMkSevgSFFg2P+2z" +
            "dYWdfAaSf98BH8J1qwLl45H9G9aqAYZlGzUWCZShjV+YXrczYGnQneNWftmoE1Vw" +
            "Z1ersanETug+p5E2ViB5StCBGi/z/HO/+MyAxXbrV/r2roXDTLC2MOtytfPeQRTx" +
            "eZLp6X5zvnJvuSQQP4tsul6Em8bbAgMBAAECgYEAvSGZ82+mHVdiCKeOOtFokRIK" +
            "H2yCQDrIUeCFj5OiNOgSqSS/U87X0iunvCypou87mCy35tg8V2WGUg551nhUkm04" +
            "weFI3DQAHiC8do2M59/nzfiC0hhzYwb0jnlmsgaFJA3oaFs++qNFsaQiYtOrcUQ4" +
            "ksJVhTwAhjvb6XcT9JECQQDsjg6uNI/pT6VUoRIhJK8IyY+5QNy1MG8MckFrsvXc" +
            "ftwsJv7iU0LuImKcrPnqnZ1QkJEd+/6Odkq66+EBiMjpAkEA2Zm+D11GipCB6KPZ" +
            "gD207Ng6IipQpm3Z5Lzpz6pQlCESKI3EMzuAZyzqi131aNW2I/98K2+yyZ5JaOMF" +
            "OuR3IwJAA5+5gdm0SrK5qa4+BNv3An90ADaKKwxu5xXpAqlfMt2Oqe5/ASCdaeCE" +
            "+jl8Kqf3fQB5+KeforcVNf/fFpLt4QJAfpXWn/+GIuOv/xMaW2UKVGHxZ6CwLDFp" +
            "eYyAhAMzPwkCFD9sbNVnfB4AD7VIJ4VzoPtmU6p2Gp4PXIn8p+byewJBAOrKuBG5" +
            "SI2zbPgevyl8L9BPfJJs7X5N0EtDRo415QPgswd2wkEEA0uuGi9ykS/T9OjohOED" +
            "rc6CXAzizytIB7A=";


    //layout资源文件名
    public static final String login_main = "kl_activity_login_main";//登录
    public static final String login_main_first = "kl_activity_login_first";//新用户登录
    public static final String register = "kl_activity_register";//注册
    public static final String visitor_register = "kl_activity_visitor";//游客注册
    public static final String visitor_register_hb = "kl_activity_visitor_hb";//游客注册  红包版
    public static final String forget_password = "kl_activity_reset_pwd";//忘记密码
    public static final String splash = "kl_activity_splash";//欢迎页
    public static final String real_name = "kl_activity_real_name";//实名认证
    public static final String permission = "kl_activity_per";//权限
    public static final String pay_web = "kl_activity_recharge_web_land";//支付web
    public static final String user_info = "kl_activity_user_info";//用户信息web
    public static final String platform = "kl_activity_platform";//平台
    public static final String pay = "kl_activity_pay";//支付
    public static final String pay2 = "kl_activity_pay2";//支付2
    public static final String bind_phone = "kl_activity_bind_phone";//绑定手机
    public static final String quick_login = "kl_activity_login_quick";//快速登录
    public static final String tip_common = "kl_activity_tip_common";//温馨提示
    public static final String service = "kl_activity_service";//服务
    public static final String getPlatform_pay = "kl_activity_platform_pay";//平台支付界面
    public static final String first_login_quick = "kl_activity_login_first_quick";//快速登录

    public static final String login_account = "kl_fragment_login_account";//账户登录View
    public static final String login_phone = "kl_fragment_login_phone";//验证码登录View
    public static final String register_account = "kl_view_register_account";//账户注册View
    public static final String register_phone = "kl_view_register_phone";//手机号注册View
    public static final String item_account_list = "kl_item_account_list";//用户列表item
    public static final String kl_platform_web_pay = "kl_activity_platform_web_pay";
    public static final String fragment_wy_quick_login = "kl_fragment_wy_quick_login";//网易快速登陆
    public static final String fragment_visitor = "kl_fragment_visitor";//游客

    public static final String kl_open_smallgame = "kl_activity_smallgame";
    public static final String kl_dialog_teghin_voucher = "kl_dialog_teghin_voucher"; //代金卷dialog
    public static final String kl_dialog_exclusive_volume = "kl_dialog_exclusive_volume";//专属幸运礼卷
    public static final String kl_dialog_activity_center = "kl_dialog_activity_center"; //福利中心
    public static final String kl_dialog_key_code = "kl_dialog_key_code"; //激活码兑换
    public static final String kl_poker_game = "kl_activity_poker";
    public static final String kl_buy_platfrom = "kl_activity_buy_platform";//购买平台币
    public static final String kl_permission_dialog = "kl_dialog_permission";
//    public static final String getPlatform_pay ="kl_activity_platform_pay";//平台支付界面

}
