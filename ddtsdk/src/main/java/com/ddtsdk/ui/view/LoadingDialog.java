package com.ddtsdk.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by CZG on 2020/4/1
 * 加载中动画dialog
 */

public class LoadingDialog {
    private Dialog mDialog;
    private Context mContext;

    public LoadingDialog(Context context) {
        this.mContext = context;
        initDialog(context);
    }

    private void initDialog(Context context) {
        mDialog = new Dialog(context, resourceId("loading_dialog","style"));
        View view = LayoutInflater.from(context).inflate(resourceId("kl_dialog_loading","layout"), null);
        mDialog.setContentView(view);

        // “返回键”可以取消进度框
        mDialog.setCancelable(false);
        // 点击进度框外不能取消
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        try {
            if (null != mDialog && !mDialog.isShowing()) {
                mDialog.show();
            }
        } catch (Exception e) {
        }
    }

    public void show(String msg) {

        show();
    }

    public void show(int resid) {
        show(mContext.getResources().getString(resid));
    }

    public void dismiss() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public int resourceId(String name, String type) {
        return mContext.getResources().getIdentifier(name, type, mContext.getPackageName());
    }
}
