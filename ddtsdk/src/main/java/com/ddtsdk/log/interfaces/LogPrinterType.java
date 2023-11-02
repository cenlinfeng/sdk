package com.ddtsdk.log.interfaces;

public interface LogPrinterType {
    int LOG_VERBOSE = 1;
    int LOG_DEBUG = 2;
    int LOG_INFO = 3;
    int LOG_WARN = 4;
    int LOG_ERROR = 5;
    void superLog(int priority, String tag, Throwable throwable, Object... args);
    void log(int priority, String tag, Throwable throwable, Object... args);
    void v(String tag, Object... args);
    void d(String tag, Object... args);
    void i(String tag, Object... args);
    void w(String tag, Object... args);
    void e(String tag, Throwable throwable, Object... args);
    void json(String tag, String json);
    void xml(String tag, String xml);
}
