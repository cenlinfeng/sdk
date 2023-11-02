package com.ddtsdk.model.protocol.bean;

import com.ddtsdk.model.data.PayData;

import java.util.List;

public class BuyPlatformBean {
    private String money;
    private List<MoneyConfig> moneyconfig;
    private String tip;
    private List<PayData> paylist;

    public String getMoney() {
        return money;
    }

    public List<MoneyConfig> getMoneyconfig() {
        return moneyconfig;
    }

    public String getTip() {
        return tip;
    }

    public static class MoneyConfig {
        private String id;
        private String money;
        private String pay_money;
        private String money_name;
        private String pay_money_name;
        private String give_money_name;
        public String getId() {
            return id;
        }
        public String getMoney() {
            return money;
        }
        public String getPay_money() {
            return pay_money;
        }
        public String getMoney_name() {
            return money_name;
        }
        public String getPay_money_name() {
            return pay_money_name;
        }
        public String getGive_money_name() {
            return give_money_name;
        }
    }
}
