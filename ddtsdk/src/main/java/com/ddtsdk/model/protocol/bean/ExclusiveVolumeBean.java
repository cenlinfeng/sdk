package com.ddtsdk.model.protocol.bean;

import java.util.List;

public class ExclusiveVolumeBean {

    private List<voucherList> voucherList;

    public List<ExclusiveVolumeBean.voucherList> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<ExclusiveVolumeBean.voucherList> voucherList) {
        this.voucherList = voucherList;
    }

    public class voucherList {
        private String id;  //商品id
        private String coupon_id; //代金卷id
        private String coupon_num; //代金卷数量
        private String inventory;//库存
        private String price;//销售价
        private String create_time; //创建时间
        private String start_time; //开始时间
        private String end_time;//结束时间
        private String type; //来源标识,1.渠道,2.用户
        private String valid_time;//商品有效期
        private String coupon_name; // 商品名称
        private String cost; //成本
        private String status;//状态
        private String sales; //销售量
        private String rule; //规则

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getCoupon_num() {
            return coupon_num;
        }

        public void setCoupon_num(String coupon_num) {
            this.coupon_num = coupon_num;
        }

        public String getInventory() {
            return inventory;
        }

        public void setInventory(String inventory) {
            this.inventory = inventory;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValid_time() {
            return valid_time;
        }

        public void setValid_time(String valid_time) {
            this.valid_time = valid_time;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        @Override
        public String toString() {
            return "voucherList{" +
                    "id='" + id + '\'' +
                    ", coupon_id='" + coupon_id + '\'' +
                    ", coupon_num='" + coupon_num + '\'' +
                    ", inventory='" + inventory + '\'' +
                    ", price='" + price + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", type='" + type + '\'' +
                    ", valid_time='" + valid_time + '\'' +
                    ", coupon_name='" + coupon_name + '\'' +
                    ", cost='" + cost + '\'' +
                    '}';
        }
    }
}
