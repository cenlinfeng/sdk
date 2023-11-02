package com.ddtsdk.utils;

import android.content.Context;

/**
 * Created by a5706 on 2018/4/27.
 */

public interface  PermissionInterface {
    /**
     * 可设置请求权限请求码
     */
    int getPermissionsRequestCode();

    /**
     * 设置需要请求的权限
     */
    String[] getPermissions();

    /**
     * 请求权限成功回调
     */
    void requestPermissionsSuccess(Context context);

    /**
     * 请求权限失败回调
     */
    void requestPermissionsFail(Context context);
}
