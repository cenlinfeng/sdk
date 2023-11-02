package com.ddt.h5game;

public class H5PayBean {


    /**
     * result : true
     * msg : 1
     * data : {"userid":"16","serverid":"210001","orderid":"M201901291910296800","itemid":"1","subject":"����1000Ԫ��","appid":"100187","amount":10,"extrainfo":"eyJjcF9leHRpbmZvIjoiMzU1OGUwNWI5Y2E5NDQwNzhmMmZkOWY3ZDFhOTlmNWQiLCJjcF9vcmRlcmlkIjoiTTIwMTkwMTI5MTkxMDI5NjgwMCIsIml0ZW1faWQiOiIxIiwicGxhdGZvcm1fdWlkIjoiMTYifQ==","roleid":null,"rolename":"��Э","time":1548760229,"sign":"cc50a88aa91ce866f4dcf6caf7326539"}
     */

    private boolean result;
    private String msg;
    private DataBean data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userid : 16
         * serverid : 210001
         * orderid : M201901291910296800
         * itemid : 1
         * subject : ����1000Ԫ��
         * appid : 100187
         * amount : 10
         * extrainfo : eyJjcF9leHRpbmZvIjoiMzU1OGUwNWI5Y2E5NDQwNzhmMmZkOWY3ZDFhOTlmNWQiLCJjcF9vcmRlcmlkIjoiTTIwMTkwMTI5MTkxMDI5NjgwMCIsIml0ZW1faWQiOiIxIiwicGxhdGZvcm1fdWlkIjoiMTYifQ==
         * roleid : null
         * rolename : ��Э
         * time : 1548760229
         * sign : cc50a88aa91ce866f4dcf6caf7326539
         */

        private String userid;
        private String serverid;
        private String orderid;
        private String itemid;
        private String subject;
        private String appid;
        private String amount;
        private String extrainfo;
//        private String roleid;
        private String rolename;
        private String time;
        private String sign;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getServerid() {
            return serverid;
        }

        public void setServerid(String serverid) {
            this.serverid = serverid;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
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

//        public String getRoleid() {
//            return roleid;
//        }
//
//        public void setRoleid(String roleid) {
//            this.roleid = roleid;
//        }

        public String getRolename() {
            return rolename;
        }

        public void setRolename(String rolename) {
            this.rolename = rolename;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "userid='" + userid + '\'' +
                    ", serverid='" + serverid + '\'' +
                    ", orderid='" + orderid + '\'' +
                    ", itemid='" + itemid + '\'' +
                    ", subject='" + subject + '\'' +
                    ", appid='" + appid + '\'' +
                    ", amount=" + amount +
                    ", extrainfo='" + extrainfo + '\'' +
                    ", rolename='" + rolename + '\'' +
                    ", time=" + time +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }
}
