package com.ddtsdk.network;

/**
 * 接口异常
 */
public class ApiException extends Exception {
    private int status;

    public ApiException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
