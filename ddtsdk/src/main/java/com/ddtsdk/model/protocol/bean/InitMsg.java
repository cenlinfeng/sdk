package com.ddtsdk.model.protocol.bean;

public class InitMsg {
    public String token;
    public String sessid;
    public String qq;
    public String phone;
    public String fpwd;
    public String strategy_site;
    private boolean bannlogging; //true则给模拟器玩家进入游戏，否则禁止使用模拟器登录游戏
    private String agree;
    private String logo_img;
    private String float_img;
    private int minutetime;
    private int openillustrate;    //0  不开启提示  ,1开启提示
    private String wx_qrcode;
    private String wx_name;
    private String customer_qq;
    private String platform_cash;  //代金券支付拼接链接   2020/9/3
    private String H5LoginLink;
    private int pack_model;  //1红包 , 2平台
    private String control_type;  //类型：increment 增量，decrement 缩量
    private int login_config; // 0:默认手机注册 1:网易一键登录注册
    private String more_game; //更多游戏
    private AdvInfo adv_info;
    private String mini_game_url; //小游戏加载地址
    private int show_time;   //小游戏显示时间
    private int is_test; //0非测试，1测试
    private boolean banreg;

    public int getIs_test() {
        return is_test;
    }

    public void setIs_test(int is_test) {
        this.is_test = is_test;
    }

    public Boolean getBanpackage() {
        return banpackage;
    }

    public void setBanpackage(Boolean banpackage) {
        this.banpackage = banpackage;
    }

    private Boolean banpackage;   //封禁抓包


    public String getMini_game_url() {
        return mini_game_url;
    }

    public void setMini_game_url(String mini_game_url) {
        this.mini_game_url = mini_game_url;
    }

    public int getShow_time() {
        return show_time;
    }

    public void setShow_time(int show_time) {
        this.show_time = show_time;
    }


    public String getMore_game() {
        return more_game;
    }

    public void setMore_game(String more_game) {
        this.more_game = more_game;
    }

    public int getLogin_config() {
        return login_config;
    }

    private int sdk_track;  //sdk_track 为 1，则开启埋点上报，否则 0 不上报

    public int getOpenillustrate() {
        return openillustrate;
    }

    public void setOpenillustrate(int openillustrate) {
        this.openillustrate = openillustrate;
    }

    public String getCustomer_qq() {
        return customer_qq;
    }

    public void setCustomer_qq(String customer_qq) {
        this.customer_qq = customer_qq;
    }

    public String getWx_qrcode() {
        return wx_qrcode;
    }

    public void setWx_qrcode(String wx_qrcode) {
        this.wx_qrcode = wx_qrcode;
    }

    public String getWx_name() {
        return wx_name;
    }

    public void setWx_name(String wx_name) {
        this.wx_name = wx_name;
    }

    public String getAgree() {
        return agree;
    }

    public String getLogo_img() {
        return logo_img;
    }

    public String getFloat_img() {
        return float_img;
    }

    public int getMinutetime() {
        return minutetime;
    }

    public String getStrategy_site() {
        return strategy_site;
    }

    public void setStrategy_site(String strategy_site) {
        this.strategy_site = strategy_site;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessid() {
        return sessid;
    }

    public void setSessid(String sessid) {
        this.sessid = sessid;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFpwd() {
        return fpwd;
    }

    public void setFpwd(String fpwd) {
        this.fpwd = fpwd;
    }

    public Boolean getBannlogging() {
        return bannlogging;
    }

    public void setBannlogging(Boolean bannlogging) {
        this.bannlogging = bannlogging;
    }

    public String getPlatform_cash() {
        return platform_cash;
    }

    public void setPlatform_cash(String platform_cash) {
        this.platform_cash = platform_cash;
    }

    public String getH5LoginLink() {
        return H5LoginLink;
    }

    public void setH5LoginLink(String h5LoginLink) {
        H5LoginLink = h5LoginLink;
    }

    public int getPack_model() {
        return pack_model;
    }

    public void setPack_model(int pack_model) {
        this.pack_model = pack_model;
    }

    public String getControl_type() {
        return control_type;
    }

    public void setControl_type(String control_type) {
        this.control_type = control_type;
    }

    public int getSdk_track() {
        return sdk_track;
    }

    public void setSdk_track(int sdk_track) {
        this.sdk_track = sdk_track;
    }

    public AdvInfo getAdv_info() {
        return adv_info;
    }

    public void setAdv_info(AdvInfo adv_info) {
        this.adv_info = adv_info;
    }

//	@Override
//	public String toString() {
//		return 	"---InitMsg  toString---result:"+result +",msg:"+ msg +",token:"+ token
//				+",sessid:" + sessid +",qq:"+ qq +",phone:"+ phone
//				+",fpwd:"+ fpwd +",strategy_site:"+ strategy_site + "---" ;
//	}

    public boolean isBanreg() {
        return banreg;
    }


    public class AdvInfo {
        private int is_adv; // 是否开启弹窗，0:否 1:是
        private String adv_url; // 弹窗跳转链接
        private String adv_img; // 弹窗图片地址

        public int getIs_adv() {
            return is_adv;
        }

        public void setIs_adv(int is_adv) {
            this.is_adv = is_adv;
        }

        public String getAdv_url() {
            return adv_url;
        }

        public void setAdv_url(String adv_url) {
            this.adv_url = adv_url;
        }

        public String getAdv_img() {
            return adv_img;
        }

        public void setAdv_img(String adv_img) {
            this.adv_img = adv_img;
        }
    }
}
