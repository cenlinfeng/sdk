package com.ddtsdk.othersdk.manager.bean;

import java.util.List;

public class PayState {
    private boolean result;
    private String msg;
    private List<PayResult> data;

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

    public List<PayResult> getPayResults() {
        return data;
    }

    public void setPayResults(List<PayResult> payResults) {
        this.data = payResults;
    }
}
