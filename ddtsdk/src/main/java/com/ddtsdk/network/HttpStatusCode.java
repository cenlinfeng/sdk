package com.ddtsdk.network;

/**
 */
public class HttpStatusCode {
    /**
     * 客户端自定义内部异常
     */
    public static final int NO_NET_WORK = 10086;// 无网络
    public static final int CONNECT_FAIL = 10087;// 连接请求失败（超时）
    public static final int RESPONSE_BODY_NULL = 10088;// 响应体为null
    public static final int RESPONSE_FAIL = -1;// result返回false

}
