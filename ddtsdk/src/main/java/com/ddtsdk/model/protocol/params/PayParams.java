package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/16.
 */
public class PayParams {
    private String amount;
    private String paytarget;   //充值对象: 直接充到游戏传5，充到平台币传1 ,平台币兑换游戏币传2 , 赠宝兑换游戏币传3
    private String paychar;
    private String pwd;         //平台币支付密码，只在平台币兑换时候弹出提示框输入否则输入空串
    private String billno;
    private String extrainfo;
    private String subject;
    private String serverid;
    private String istest;
    private String rolename;
    private String rolelevel;
    private String roleid;
    private String packageName; //后端为package,java关键字无法设置变量名,BaseParams处理
    private String coupon_id; //优惠卷id
    private String voucherGoods; //充值渠道来源
    private String goods_id; //直充卷id

    public String getAmount() {
        return amount;
    }

    public String getPaytarget() {
        return paytarget;
    }

    public String getPaychar() {
        return paychar;
    }

    public String getPwd() {
        return pwd;
    }

    public String getBillno() {
        return billno;
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public String getSubject() {
        return subject;
    }

    public String getServerid() {
        return serverid;
    }

    public String getIstest() {
        return istest;
    }

    public String getRolename() {
        return rolename;
    }

    public String getRolelevel() {
        return rolelevel;
    }

    public String getRoleid() {
        return roleid;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getVoucherGoods() {
        return voucherGoods;
    }

    public String getGoods_id() {
        return goods_id;
    }


    public PayParams(String amount, String paytarget, String paychar,
                     String pwd, String billno, String extrainfo,
                     String subject, String serverid, String istest,
                     String rolename, String rolelevel, String roleid,
                     String packageName, String coupon_id, String voucherGoods,
                     String goods_id) {
        this.amount = amount;
        this.paytarget = paytarget;
        this.paychar = paychar;
        this.pwd = pwd;
        this.billno = billno;
        this.extrainfo = extrainfo;
        this.subject = subject;
        this.serverid = serverid;
        this.istest = istest;
        this.rolename = rolename;
        this.rolelevel = rolelevel;
        this.roleid = roleid;
        this.packageName = packageName;
        this.coupon_id =coupon_id;
        this.voucherGoods = voucherGoods;
        this.goods_id = goods_id;
    }
}
