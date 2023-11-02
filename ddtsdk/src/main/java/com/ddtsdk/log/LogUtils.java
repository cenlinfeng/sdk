package com.ddtsdk.log;

import android.util.Log;

import com.ddtsdk.log.interfaces.LogPrinterType;

public class LogUtils {
    public static String TAG = "ddtsdk_debug_log";
    private boolean logOpen = false;
    public static int stackDepth = 3;  //调用栈日志的深度， 3代表当前类当前方法，递增为调用类的层级的方法位置，递减则为虚拟机（本地方法），线程等层级日志
    private static LogUtils logUtils = null;
    private static LogPrinterType logPrinterType = null;
    private String methodInfo = "";

    public static LogUtils getInstance() {
        if (logUtils == null) {
            logUtils = new LogUtils();
        }
        return logUtils;
    }

    public void openDebug(boolean debug) {
        this.logOpen = debug;
        if (debug) stackDepth = 5;
        logPrinterType = LogPrinterFactory.getInstance().createType(0);
    }

    /**
     * 此方法无视是否开启日志模式，都打印出来
     *
     * @param priority  日志等级
     * @param tag       日志tag
     * @param throwable 日志异常捕获
     * @param args      日志内容参数
     */
    public void superLog(int priority, String tag, Throwable throwable, Object... args) {
        if (logPrinterType == null) logPrinterType = LogPrinterFactory.getInstance().createType(0);
        methodInfo = getMethonInfo(4);
        logPrinterType.superLog(priority, tag, throwable, args);
    }

    public void log(int priority, String tag, Throwable throwable, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.log(priority, tag, throwable, args);
        }
    }

    public void v(String tag, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.v(tag, args);
        }
    }

    public void d(String tag, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.d(tag, args);
        }
    }

    public void i(String tag, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.i(tag, args);
        }
    }

    public void w(String tag, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.w(tag, args);
        }
    }

    public void e(String tag, Throwable throwable, Object... args) {
        if (logOpen) {
            methodInfo = getMethonInfo(stackDepth);
            logPrinterType.e(tag, throwable, args);
        }
    }

    /**
     * Formats the given json content and print it
     */
    public void json(String tag, String json) {
        methodInfo = getMethonInfo(stackDepth);
        logPrinterType.json(tag, json);
    }

    /**
     * Formats the given xml content and print it
     */
    public void xml(String tag, String xml) {
        methodInfo = getMethonInfo(stackDepth);
        logPrinterType.xml(tag, xml);
    }

    public String getMethodMsg() {
        return this.methodInfo;
    }

    private String getMethonInfo(int pos) {
        Log.e("pos=", pos + "");
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
//        for (StackTraceElement ste : elements) {
//            if (ste.isNativeMethod() || ste.getClassName().equals(Thread.class.getName())
//                    || ste.getClassName().equals(this.getClass().getName())) {
//                continue;
//            }
        builder.append(PrinterFormat.DOUBLE_SPACE)
                .append("Thread:").append(Thread.currentThread().getName()).append("\n")
                .append(PrinterFormat.DOUBLE_SPACE)
                .append("Method:").append(elements[pos].getMethodName()).append("\n")
                .append(PrinterFormat.DOUBLE_SPACE)
                .append("Class:").append(elements[pos].getClassName()).append("\n")
                .append(PrinterFormat.DOUBLE_SPACE)
                .append("Path:").append(elements[pos].getClassName()).append(".")
                .append(elements[pos].getMethodName())
                .append("(")
                .append(elements[pos].getFileName())
                .append(":").append(elements[pos].getLineNumber())
                .append(")").append("\n");
        return builder.toString();
    }
}
