package com.ddtsdk.model.protocol.params;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;

/**
 * Created by CZG on 2022/5/26.
 * 专属卷构造器
 */
public class ExclusiveVolumeParams {
    private String ver;  //渠道号
    private String uid;  //用户id

    public ExclusiveVolumeParams() {
        this.ver = AppConstants.ddt_ver_id;
        this.uid = AppConstants.uid;
    }
}
