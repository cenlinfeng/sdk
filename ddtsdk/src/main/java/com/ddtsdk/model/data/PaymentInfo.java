package com.ddtsdk.model.data;


import android.os.Parcel;
import android.os.Parcelable;

public class PaymentInfo implements Parcelable {
    private int appid;
    private String appKey;
    private String agent;        //渠道id
    private String serverid;    //区服id
    private String billno;        //订单id
    private String productId; //商品id
    private String amount;        //金额
    private String extrainfo;    //扩展信息
    private String subject;    //支付描述
    private String uid;            // uid辨别用户是否只接入充值，如果只接入充值uid传"",相反传对方平台的用户名
    private String istest;        //是否是test的参数


    private String rolename;//角色名
    private String rolelevel;//角色等级

    private String roleid;//角色id
    private String goods_id; //直充卷商品id

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUid() {
        return "";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIstest() {
        return istest;
    }

    public void setIstest(String istest) {
        this.istest = istest;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getRolelevel() {
        return rolelevel;
    }

    public void setRolelevel(String rolelevel) {
        this.rolelevel = rolelevel;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public static Creator<PaymentInfo> getCreator() {
        return CREATOR;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // TODO Auto-generated method stub
        parcel.writeInt(appid);
        parcel.writeString(appKey);
        parcel.writeString(agent);
        parcel.writeString(serverid);

        parcel.writeString(billno);
        parcel.writeString(productId);
        parcel.writeString(amount);
        parcel.writeString(extrainfo);
        parcel.writeString(subject);
        parcel.writeString(uid);
        parcel.writeString(istest);


        parcel.writeString(rolename);
        parcel.writeString(rolelevel);

        parcel.writeString(roleid);
        parcel.writeString(goods_id);

    }
    //序列化和反序列化一定接的要和上面的writeToParcel数据对其不然数据会被混淆
    public static final Creator<PaymentInfo> CREATOR = new Creator<PaymentInfo>() {

        @Override
        public PaymentInfo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            PaymentInfo payInfo = new PaymentInfo();
            payInfo.appid = source.readInt();
            payInfo.appKey = source.readString();
            payInfo.agent = source.readString();
            payInfo.serverid = source.readString();

            payInfo.billno = source.readString();
            payInfo.productId = source.readString();
            payInfo.amount = source.readString();
            payInfo.extrainfo = source.readString();
            payInfo.subject = source.readString();
            payInfo.uid = source.readString();
            payInfo.istest = source.readString();

            payInfo.rolename = source.readString();
            payInfo.rolelevel = source.readString();
            payInfo.roleid = source.readString();
            payInfo.goods_id = source.readString();


            return payInfo;
        }

        @Override
        public PaymentInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new PaymentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


}
