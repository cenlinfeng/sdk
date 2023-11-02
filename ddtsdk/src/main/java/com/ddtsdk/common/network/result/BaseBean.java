package com.ddtsdk.common.network.result;

import java.io.Serializable;

/**
 * 返回数据基类
 * 服务器端返回的统一数据格式
 * {
 *   "id":"1543202645289876",
 *   "state":{"code":0,"msg":"操作成功"},
 *   "data":{} // 数据
 *   "etag":"f9e0c242d912297c8189b365748aaa43"
 * }
 *
 */

public class BaseBean<T> implements Serializable {
    private String msg;
    private boolean result;
    private T data;

    public String getMsg() {
        return msg;
    }

    public boolean isResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
