package com.ddtsdk.log;

import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.log.interfaces.LogPrinterType;

public class PrinterType implements LogPrinterType {

    @Override
    public void superLog(int priority, String tag, Throwable throwable, Object... args) {
        printLog(tag, priority, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), throwable);
    }

    @Override
    public void log(int priority, String tag, Throwable throwable, Object... args) {
        printLog(tag, priority, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), throwable);
    }

    @Override
    public void v(String tag, Object... args) {
        printLog(tag, LOG_VERBOSE, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), null);
    }

    @Override
    public void d(String tag, Object... args) {
        printLog(tag, LOG_DEBUG, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), null);
    }

    @Override
    public void i(String tag, Object... args) {
        printLog(tag, LOG_INFO, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), null);
    }

    @Override
    public void w(String tag, Object... args) {

        printLog(tag, LOG_WARN, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), null);
    }

    @Override
    public void e(String tag, Throwable throwable, Object... args) {
        printLog(tag, LOG_ERROR, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), args), throwable);
    }

    @Override
    public void json(String tag, String json) {
        printLog(tag, LOG_DEBUG, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), json), null);
    }

    @Override
    public void xml(String tag, String xml) {
        printLog(tag, LOG_DEBUG, PrinterFormat.msgFormat(LogUtils.getInstance().getMethodMsg(), xml), null);
    }

    private void printLog(String tag, int priority, String Msg, Throwable throwable){
        String tagName = TextUtils.isEmpty(tag)? LogUtils.TAG : tag;
        switch (priority){
            case LOG_VERBOSE:
                Log.v(tagName, Msg);
                break;
            case LOG_DEBUG:
                Log.d(tagName, Msg);
                break;
            case LOG_INFO:
                Log.i(tagName, Msg);
                break;
            case LOG_WARN:
                Log.w(tagName, Msg);
                break;
            case LOG_ERROR:
                Log.e(tagName, Msg, throwable);
                break;
        }
    }
}
