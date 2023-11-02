package com.ddtsdk.model.data;

import java.util.List;


public class VoucherData {


    /**
     * {
     * "id":"232",
     * "coupon_id":"11",
     * "create_time":"1648794334",
     * "expired_time":"1648893600",
     * "coupon_name":"\u65b0\u7528\u6237\u798f\u522912\u51cf11.98",
     * "condition":"1",
     * "condition_amount":"12.00",
     * "discount_amount":"11.98",
     * "type":"1",
     * "state":1,
     * "total":1
     * }
     */
    private String id;
    private String create_time;//领取时间
    private String expired_time;//结束时间
    private String coupon_name; //优惠卷名称
    private String condition; //优惠卷类型 1 通用卷 2满减卷 4 折扣卷
    private String condition_amount; //金额满多少使用
    private String discount_amount; //减免金额
    private int state; //1表示可用 0表示不可用
    private String type; //代金卷类型 1为现金卷 2为折扣价
    private int total; //相同类型的代金卷个数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(String expired_time) {
        this.expired_time = expired_time;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition_amount() {
        return condition_amount;
    }

    public void setCondition_amount(String condition_amount) {
        this.condition_amount = condition_amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


}
