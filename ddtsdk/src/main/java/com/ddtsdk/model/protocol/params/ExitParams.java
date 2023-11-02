package com.ddtsdk.model.protocol.params;


import com.ddtsdk.constants.AppConstants;

public class ExitParams {
    private String version; //1红包版本 2平台版本
    public ExitParams(){
        if (AppConstants.platformUrl.contains("/sdk_pf/")){
            version = "2";  //
        }
        else if (AppConstants.platformUrl.contains("/sdk_rp/")){
            version = "1";
        }

//        if (AppConstants.platformUrl.contains("/1.1.0/")){
//            version = "1";  //
//        }
//        else if (AppConstants.platformUrl.contains("/1.0.0/")){
//            version = "2";
//        }
    }
}
