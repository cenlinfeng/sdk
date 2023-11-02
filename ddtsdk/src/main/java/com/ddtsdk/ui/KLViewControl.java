package com.ddtsdk.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.model.net.KLApi;
import com.ddtsdk.ui.activity.KLExclusiveVolumeActivity;
import com.ddtsdk.ui.dialog.KLActivityCenterDialog;
import com.ddtsdk.ui.dialog.KLKeyCodeDialog;
import com.ddtsdk.ui.dialog.KLPermissionDialog;
import com.ddtsdk.ui.dialog.KLTeghinVoucherDialog;
import com.ddtsdk.utils.LogUtil;

import java.util.ArrayList;

/**
 * dialog的Ui控制器，尽量使用dialog来控制窗体的展开，避免在manifest中去注册activity
 */
public class KLViewControl {
    private static KLViewControl instance;
    private FragmentManager fragmentManager;
    private ArrayList<BaseDialogFragment> views = new ArrayList<>();

    private KLViewControl() {

    }

    public static KLViewControl getInstance() {
        if (instance == null) {
            instance = new KLViewControl();
        }
        return instance;
    }

    private void show(Activity activity, Class cla, Bundle bundle) {
        fragmentManager = activity.getFragmentManager();
        try {
            String tag = cla.getSimpleName();
            BaseDialogFragment dialog = (BaseDialogFragment) cla.newInstance();
            if (bundle != null) {
                dialog.setArguments(bundle);
            }
            dialog.show(fragmentManager, tag);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉起代金卷dialog
     *
     * @param activity
     */
    public void showTeghinVolume(Activity activity) {
        LogUtil.i("showTeghinVolume");
        show(activity, KLTeghinVoucherDialog.class, null);
    }

    /**
     * 拉起活动中心
     */
    public void showActivityCenter(Activity activity) {
        LogUtil.i("showActivityCenter");
        show(activity, KLActivityCenterDialog.class, null);
    }


    /**
     * 拉起活动中心
     */
    public void showKeyCode(Activity activity) {
        LogUtil.i("showKeyCode");
        show(activity, KLKeyCodeDialog.class, null);
    }

    /**
     * 拉起专属幸运礼卷的activity
     *
     * @param activity
     */
    public void showExclusiveVolume(Activity activity) {
        LogUtil.i("showExclusiveVolume");
        show(activity, KLExclusiveVolumeActivity.class, null);
    }


    /**
     * 单个权限请求说明
     *
     * @param title
     * @param desc
     * @param permission
     */
    public void showPermissionView(Activity activity,String title, String desc, String permission, String permissionNotice, Boolean necessity) {
        LogUtil.i("showPermissionView");
        Bundle bundle = new Bundle();
        bundle.putString(KLPermissionDialog.PERMISSION_TITLE, title);
        bundle.putString(KLPermissionDialog.PERMISSION_CONTENT, desc);
        bundle.putString(KLPermissionDialog.PERMISSION, permission);
        bundle.putString(KLPermissionDialog.PERMISSION_NOTICE, permissionNotice);
        bundle.putBoolean(KLPermissionDialog.PERMISSION_NECESSITY, necessity);
        show(activity,KLPermissionDialog.class, bundle);
    }


    public void addView(BaseDialogFragment view) {
        if (!views.contains(view) && view != null) {
            views.add(view);
        }
    }

    public void removeView(BaseDialogFragment view) {
        views.remove(view);
    }

    public void hide(Class cla) {
        String tag = cla.getSimpleName();
        fragmentManager = AppConfig.getActivity().getFragmentManager();
        BaseDialogFragment dialog = (BaseDialogFragment) fragmentManager.findFragmentByTag(tag);
        if (dialog != null) {
            dialog.dismissAllowingStateLoss();
        }
    }

    public void back() {
        if (views.size() > 0) {
            BaseDialogFragment dialogFragment = views.get(views.size() - 1);
            if (dialogFragment != null) {
                dialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void destroy() {
        for (BaseDialogFragment view : views) {
            if (view != null) {
                view.dismissAllowingStateLoss();
            }
        }
        instance = null;
    }
}
