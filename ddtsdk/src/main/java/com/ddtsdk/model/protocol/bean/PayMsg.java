package com.ddtsdk.model.protocol.bean;

public class PayMsg {
    private String PayUrl;
    private String Billno;

    public String getPayUrl() {
        return PayUrl;
    }

    public void setPayUrl(String payUrl) {
        PayUrl = payUrl;
    }

    public String getBillno() {
        return Billno;
    }

    public void setBillno(String billno) {
        this.Billno = billno;
    }

}
