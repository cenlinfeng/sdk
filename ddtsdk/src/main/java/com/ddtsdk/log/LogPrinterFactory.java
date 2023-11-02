package com.ddtsdk.log;

import com.ddtsdk.log.interfaces.LogPrinterType;

public class LogPrinterFactory {
    static LogPrinterFactory logFactory = null;

    private LogPrinterFactory(){}

    public static LogPrinterFactory getInstance(){
        if (logFactory == null){
            logFactory = new LogPrinterFactory();
        }
        return logFactory;
    }

    /**
     *
     * @param type   0为默认打印类型
     * @return
     */
    public LogPrinterType createType(int type){
        LogPrinterType logPrinterType = null;
        switch (type){
            case 1:
                break;
            default:
                logPrinterType = new PrinterType();
        }
        return logPrinterType;
    }
}
