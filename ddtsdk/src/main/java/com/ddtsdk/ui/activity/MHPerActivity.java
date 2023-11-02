package com.ddtsdk.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.utils.PermissionHelper;
import com.ddtsdk.utils.PermissionInterface;
import com.ddtsdk.utils.SharedPreferenceUtil;
import com.ddtsdk.utils.Utils;

public class MHPerActivity extends BaseActivity implements PermissionInterface {

    //SharedPreference 的key在这里写
    public static String USER_CACHE = "user_cache";
    public static String HISTORY_USER_CACHE = "history_user_cache";
    public static String PRIVACY_POLICY = "privacy_policy";
    public static String INITX5 = "initx5";

    private PermissionHelper mPermissionHelper;
    protected ActivityInfo info = null;
    private ImageView kl_splash_iv;
    private int delay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kl_splash_iv = findViewById(resourceId("kl_splash_iv", "id"));
        try {
            info = this.getPackageManager().getActivityInfo(this.getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Utils.getIsH5Game(this).equals("1")) {
            kl_splash_iv.setVisibility(View.VISIBLE);
            delay = 2000;
        }
        if (SharedPreferenceUtil.getInstance(this).getBoolean(SharedPreferenceUtil.PRIVACY_POLICY,
                Utils.getSdkParams(this, "KL_SHOW_USERAGREEMENT_FIRST_RUN"))) {
            showPrivacyPolicyDialog((Activity) this);
        } else {
            startMainActivity();
        }

//        if (Build.VERSION.SDK_INT > 28){
//            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            this.getWindow().setAttributes(lp);
//        }
    }


    //    //首次启动时，增加隐私政策提示
    private void showPrivacyPolicyDialog(final Activity activity) {
        KLCommonAffirmDialog.Builder(0, true)
                .setTitle("用户协议及隐私政策保护指引")
                .setSubmitText("同意")
                .setCancelText("不同意")
                .setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                    @Override
                    public void onOkClick() {
                        startMainActivity();
//                        initPermission();
                        SharedPreferenceUtil.getInstance(activity).setBoolean(SharedPreferenceUtil.PRIVACY_POLICY, false);
                    }

                    @Override
                    public void onCancelClick() {
                        activity.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).show(activity.getFragmentManager(), "");
    }

    @Override
    protected String layoutName() {
        return Constants.permission;
    }

    public void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (info != null) {
                    String msg = info.metaData.getString("mainAct");
                    try {
                        final Class<?> mainClass = Class.forName(msg);
                        Intent intent = new Intent(MHPerActivity.this, mainClass);
                        startActivity(intent);
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, delay);
    }

    private void initPermission() {
        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(MHPerActivity.this, this);
        mPermissionHelper.requestPermissions(this);
    }

    @Override
    public int getPermissionsRequestCode() {
        return 10230;
    }

    @Override
    public String[] getPermissions() {
        String thirdPer = info.metaData.getString("thirdPer");
        if (thirdPer != null) {
            if (!"".equals(thirdPer)) {
                return new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        thirdPer
                };
            }
        }
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    public void requestPermissionsSuccess(Context context) {
        startMainActivity();
    }

    @Override
    public void requestPermissionsFail(Context context) {
        startMainActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10230) {
            startMainActivity();
        }
    }
}

