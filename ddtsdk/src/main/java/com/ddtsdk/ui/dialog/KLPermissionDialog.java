package com.ddtsdk.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.utils.KLPermissionUtils;
import com.ddtsdk.utils.KLSharedPreferencesUtils;
import com.ddtsdk.utils.LogUtil;


/**
 * 单个权限请求说明弹窗
 */
public class KLPermissionDialog extends BaseDialogFragment implements View.OnClickListener {

    private View rootView;
    private TextView tvTitle;
    private TextView tvPermissionContent;
    private Button btnAgree;
    private Button btnDisagree;
    private PermissionAgreeCallback permissionAgreeCallback;

    private String permission;
    private int tag;
    private String permissionNotice;
    private Boolean permissionNecessity;

    public static final String PERMISSION = "permission";
    public static final String PERMISSION_TITLE = "permission_title";
    public static final String PERMISSION_CONTENT = "permission_content";
    public static final String PERMISSION_TAG = "permission_tag";
    public static final String PERMISSION_NOTICE = "permission_notice";
    public static final String PERMISSION_NECESSITY = "permission_necessity";

    @Override
    protected String getLayoutId() {
        return Constants.kl_permission_dialog;
    }

    @Override
    protected void initDialog(View view) {
        rootView = view;
        String title = getArguments().getString(PERMISSION_TITLE);
        String content = getArguments().getString(PERMISSION_CONTENT);
        permission = getArguments().getString(PERMISSION);
        tag = getArguments().getInt(PERMISSION_TAG);
        permissionNotice = getArguments().getString(PERMISSION_NOTICE);
        permissionNecessity = getArguments().getBoolean(PERMISSION_NECESSITY);

        tvTitle = (TextView) view.findViewById(AppConfig.resourceId(mContext,"kl_permission_title","id"));
        tvPermissionContent = (TextView) view.findViewById(AppConfig.resourceId(mContext,"kl_permission_content","id"));
        btnAgree = (Button) view.findViewById(AppConfig.resourceId(mContext,"kl_permission_agree","id"));
        btnDisagree = (Button) view.findViewById(AppConfig.resourceId(mContext,"kl_permission_disagree","id"));
        btnAgree.setOnClickListener(this);
        btnDisagree.setOnClickListener(this);

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        }

//        if (permission.startsWith("KL_")) {
//            btnAgree.setText("同意");
//        } else {
//            btnAgree.setText("去开启权限");
//        }
//        btnDisagree.setText(permissionNecessity ? "关闭" : "取消");
        btnDisagree.setText("拒绝");
        setCancelable(false);

        tvTitle.setText(title);
        tvPermissionContent.setText(content);

        permissionAgreeCallback = KLPermissionUtils.getInstance();


        if (KLSharedPreferencesUtils.getBool(mContext, AppConstants.KL_PER_XML, permission + "_noask")) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    rootView.setVisibility(View.INVISIBLE);
                    showAlertNotice();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        LogUtil.i("permissionNecessity:" + permissionNecessity);
        if (v == btnDisagree) {
//            dismiss();
            rootView.setVisibility(View.INVISIBLE);
            if (permissionNecessity) {
                if (permission.startsWith("KL_")) {
                    ((Activity) mContext).finish();
                } else {
                    showAlertNotice();
                }
            } else {
                if (permission.startsWith("KL_")) {
                    if(TextUtils.isEmpty(permissionNotice)) {
                        KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission, false);
//                        非必须权限拒绝后下次不再弹出
                        KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission + "_noask", true);
                        permissionAgreeCallback.userAgreePermission(permission);
                        return;
                    }
                }
                showAlertNotice();
            }
//            if (permissionNecessity) {
////                如果必要权限，用户点击绝拒按钮直接退出
//                ((Activity) context).finish();
//            } else {
////                非必要权限，直接跳过申请下一项
//                KLSharedPreferencesUtils.setParam(context, AppConstants.KL_PER_XML, permission, true);
//                permissionAgreeCallback.userAgreePermission(permission);
//            }
        } else {
            if (permission.startsWith("KL_")) {
//                用户已经同意，下次不再弹出
                KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission, true);
                KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission + "_noask", true);
                dismiss();
                permissionAgreeCallback.userAgreePermission(permission);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                如果用户选择了不再弹出申请弹窗
                if (KLSharedPreferencesUtils.getBool(mContext, AppConstants.KL_PER_XML, permission + "_noask")) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    this.startActivityForResult(intent, tag);
                } else {
                    requestPermissions(new String[]{permission}, tag);
                }

            } else {
                dismiss();
                permissionAgreeCallback.userAgreePermission(permission);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == tag) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                rootView.setVisibility(View.INVISIBLE);
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
                    KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission + "_noask", true);
                }
                showAlertNotice();
            } else {
                dismiss();
                KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission, true);
                permissionAgreeCallback.userAgreePermission(permission);
            }
        }
    }

    private void showAlertNotice() {
        new AlertDialog.Builder(mContext).setMessage(permissionNotice)
                .setNegativeButton("去开启权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, tag);
                    }
                }).setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (permissionNecessity) {
                    dismiss();
                    System.exit(0);
                } else {
                    dismiss();
                    KLSharedPreferencesUtils.setParam(mContext, AppConstants.KL_PER_XML, permission, true);
                    permissionAgreeCallback.userAgreePermission(permission);
                }
            }
        }).setCancelable(false).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("tag:" + tag + ",requestCode:" + resultCode + ",resultCode:" + resultCode);
        LogUtil.i("checkSelfPermission:" + ActivityCompat.checkSelfPermission(mContext, permission));
        if (requestCode == tag) {
            if (ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                dismiss();
                permissionAgreeCallback.userAgreePermission(permission);
            } else {
                showAlertNotice();
            }
        }
    }

    public interface PermissionAgreeCallback {
        void userAgreePermission(String permission);
    }
}
