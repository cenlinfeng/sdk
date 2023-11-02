package com.ddtsdk.othersdk.manager.bean;

import java.util.List;

public class AbnormalPayBean {
    private int control_status;  //0正常上报，1：增缩量上报
    private String control_type;   //类型：increment 增量，decrement 缩量
    private String threshold_value;  //增缩量的条数；缩量不用理，增量需要根据这个条数进行模拟生成数据上报
    private List<String> amount;

    public int getControl_status() {
        return control_status;
    }

    public void setControl_status(int control_status) {
        this.control_status = control_status;
    }

    public String getControl_type() {
        return control_type;
    }

    public void setControl_type(String control_type) {
        this.control_type = control_type;
    }

    public String getThreshold_value() {
        return threshold_value;
    }

    public void setThreshold_value(String threshold_value) {
        this.threshold_value = threshold_value;
    }

    public List<String> getAmount() {
        return amount;
    }

    public void setAmount(List<String> amount) {
        this.amount = amount;
    }
}
