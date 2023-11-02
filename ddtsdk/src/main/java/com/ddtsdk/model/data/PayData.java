package com.ddtsdk.model.data;

public class PayData {

    private String payname;
    private String paychar;

    private float overdrawn;
    private String paytype;
    private String payicon;

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public String getPaychar() {
        return paychar;
    }

    public void setPaychar(String paychar) {
        this.paychar = paychar;
    }

    public float getOverdrawn() {
        return overdrawn;
    }

    public void setOverdrawn(float overdrawn) {
        this.overdrawn = overdrawn;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPayicon() {
        return payicon;
    }

    public void setPayicon(String payicon) {
        this.payicon = payicon;
    }
}