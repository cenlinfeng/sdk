package com.ddtsdk.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.ui.dialog.KLPermissionDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KLPermissionUtils implements KLPermissionDialog.PermissionAgreeCallback {
    private static KLPermissionUtils instance = new KLPermissionUtils();
    //    需要申请的权限
    private List<String> needRequestPermissions;
    //    权限描述
    private List<String> needRequestPermissionsDesc;
    //    权限弹窗标题
    private List<String> needRequestPermissionsTitle;
    //    用户拒绝后弹窗文案
    private List<String> needRequestPermissionsNotice;
    //    是否必要权限
    private List<Boolean> needRequestPermissionsNecessity;


    private AllPermissionAgreeCallback allPermissionAgreeCallback;

    private final static String permissionConfigFile = "permission.json";
    private final static String KEY_PERMISSION = "permissions";
    private final static String KEY_TITLE = "titles";
    private final static String KEY_DESC = "desc";
    private final static String KEY_NOTICE = "notice";
    private final static String KEY_VALUES = "values";

    public static KLPermissionUtils getInstance() {
        return instance;
    }

    private KLPermissionUtils() {
//        权限列表
        List<String> allRequestPermissions = new ArrayList<>();
//        标题
        List<String> allRequestPermissionsTitle = new ArrayList<>();
//        权限说明
        List<String> allRequestPermissionsDesc = new ArrayList<>();
//        用户拒绝一次后的引导说明
        List<String> allRequestPermissionsNotice = new ArrayList<>();
//        权限是否必须
        List<Boolean> allRequestPermissionsNecessity = new ArrayList<>();


        if (Utils.isFileExistInAssets(AppConfig.activity, permissionConfigFile)) {
            String permissionConfig = Utils.readFromAssets(AppConfig.activity, "permission.json");
            try {
                JSONObject jsonObject = new JSONObject(permissionConfig);
                JSONArray permissionArr = jsonObject.getJSONArray(KEY_PERMISSION);
                JSONArray titleArr = jsonObject.getJSONArray(KEY_TITLE);
                JSONArray descArr = jsonObject.getJSONArray(KEY_DESC);
                JSONArray noticeArr = jsonObject.getJSONArray(KEY_NOTICE);
                JSONArray valuesArr = jsonObject.getJSONArray(KEY_VALUES);

                Field values = JSONArray.class.getDeclaredField("values");
                values.setAccessible(true);

                allRequestPermissions = (List<String>) values.get(permissionArr);
                allRequestPermissionsTitle = (List<String>) values.get(titleArr);
                allRequestPermissionsDesc = (List<String>) values.get(descArr);
                allRequestPermissionsNotice = (List<String>) values.get(noticeArr);
                allRequestPermissionsNecessity = (List<Boolean>) values.get(valuesArr);

                LogUtil.i("read permission config from assets");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            allRequestPermissions.add("KL_log_permission");
            allRequestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            allRequestPermissions.add(Manifest.permission.READ_PHONE_STATE);

            allRequestPermissionsTitle.add("日志收集说明");
            allRequestPermissionsTitle.add("存储权限申请说明");
            allRequestPermissionsTitle.add("设备信息权限申请说明");

            allRequestPermissionsDesc.add("为保证游戏正常运行，数据风控，将对游戏过程中的日志信息进行收集");
            allRequestPermissionsDesc.add("用于访问您的存储空间，向您提供游戏资源缓存、下载游戏资源包的服务");
            allRequestPermissionsDesc.add("为方便您找回丢失的账号密码，保障账号安全，我们将会收集您的设备信息");

            allRequestPermissionsNotice.add("");
            allRequestPermissionsNotice.add("用于访问您的存储空间，向您提供游戏资源缓存、下载游戏资源包的服务，如果拒绝，部分功能可能无法使用");
            allRequestPermissionsNotice.add("为方便您找回丢失的账号密码，保障账号安全需收集设备信息，如您拒绝，则可能无法找回丢失的账号密码，存在账号风险");

            allRequestPermissionsNecessity.add(false);
            allRequestPermissionsNecessity.add(false);
            allRequestPermissionsNecessity.add(false);
        }


        needRequestPermissions = new ArrayList<>();
        needRequestPermissionsTitle = new ArrayList<>();
        needRequestPermissionsDesc = new ArrayList<>();
        needRequestPermissionsNotice = new ArrayList<>();
        needRequestPermissionsNecessity = new ArrayList<>();
        for (int i = 0; i < allRequestPermissions.size(); i++) {
            String permission = allRequestPermissions.get(i);
            if (permission.startsWith("KL_")) {
//                记录自定义提醒弹窗是否已经同意
                if (!KLSharedPreferencesUtils.getBool(AppConfig.activity, AppConstants.KL_PER_XML, permission)) {
//                    非必要自定义权限被拒绝后下次打开不弹窗
                    if (KLSharedPreferencesUtils.getBool(AppConfig.activity, AppConstants.KL_PER_XML, permission + "_noask")) {
                        continue;
                    }
                    needRequestPermissions.add(permission);
                    needRequestPermissionsTitle.add(allRequestPermissionsTitle.get(i));
                    needRequestPermissionsDesc.add(allRequestPermissionsDesc.get(i));
                    needRequestPermissionsNotice.add(allRequestPermissionsNotice.get(i));
                    needRequestPermissionsNecessity.add(allRequestPermissionsNecessity.get(i));
                }
            } else if (ActivityCompat.checkSelfPermission(AppConfig.activity, permission) != PackageManager.PERMISSION_GRANTED
                    && !KLSharedPreferencesUtils.getBool(AppConfig.activity, AppConstants.KL_PER_XML, permission)) {
                needRequestPermissions.add(permission);
                needRequestPermissionsTitle.add(allRequestPermissionsTitle.get(i));
                needRequestPermissionsDesc.add(allRequestPermissionsDesc.get(i));
                needRequestPermissionsNotice.add(allRequestPermissionsNotice.get(i));
                needRequestPermissionsNecessity.add(allRequestPermissionsNecessity.get(i));
            }
        }
    }


    public void requestPermission() {
        for (String permission : needRequestPermissions) {
            LogUtil.i("request permission:" + permission);
        }
        if (needRequestPermissions.size() > 0) {
            KLViewControl.getInstance().showPermissionView(AppConfig.activity, needRequestPermissionsTitle.get(0),
                    needRequestPermissionsDesc.get(0), needRequestPermissions.get(0), needRequestPermissionsNotice.get(0),
                    needRequestPermissionsNecessity.get(0));
        } else {
            allPermissionAgreeCallback.allPermissionAgree();
        }
    }

    public void setAllRequestPermissions(AllPermissionAgreeCallback callback) {
        this.allPermissionAgreeCallback = callback;
    }

    @Override
    public void userAgreePermission(String permission) {
        int index = needRequestPermissions.indexOf(permission) + 1;
        if (index < needRequestPermissions.size()) {
            KLViewControl.getInstance().showPermissionView(AppConfig.activity, needRequestPermissionsTitle.get(index),
                    needRequestPermissionsDesc.get(index), needRequestPermissions.get(index), needRequestPermissionsNotice.get(index),
                    needRequestPermissionsNecessity.get(index));
        } else {
            if (allPermissionAgreeCallback != null) {
                allPermissionAgreeCallback.allPermissionAgree();
            }
        }
    }

    public interface AllPermissionAgreeCallback {
        void allPermissionAgree();
    }
}
