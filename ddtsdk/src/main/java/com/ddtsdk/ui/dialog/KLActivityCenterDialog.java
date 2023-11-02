package com.ddtsdk.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.ui.activity.KLPokerActivity;
import com.ddtsdk.ui.view.KLCommonTitleBar;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.view.BallControler;

public class KLActivityCenterDialog extends BaseDialogFragment {

    private KLCommonTitleBar titleBar;
    private TextView mKlKeyCode;
    private TextView mKlVoucher;
    private TextView mKlPoker;

    @Override
    protected void initDialog(View view) {
        getDialog().setCancelable(false);
        titleBar = view.findViewById(AppConfig.resourceId(mContext, "kl_titlebar", "id"));
        mKlKeyCode = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_key_code", "id"));
        mKlVoucher = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_voucher", "id"));
        mKlPoker = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_lick_poker", "id"));


        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        titleBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatUtlis.getInstance().showFloat();
                dismiss();
            }
        });
        mKlKeyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLViewControl.getInstance().showKeyCode((Activity) mContext);
            }
        });

        mKlVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLViewControl.getInstance().showExclusiveVolume((Activity) mContext);
            }
        });

        mKlPoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KLPokerActivity.class);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        hideItems();
        FloatUtlis.getInstance().hideFloat();
    }


    private void hideItems() {
        showAllItems();
        if (!BallControler.canShow("activationfloat")) {
            mKlKeyCode.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("voucherfloat")) {
            mKlVoucher.setVisibility(View.GONE);
        }

        if (!BallControler.canShow("pokerfloat")) {
            mKlPoker.setVisibility(View.GONE);
        }
    }

    private void showAllItems() {
        mKlKeyCode.setVisibility(View.VISIBLE);
        mKlVoucher.setVisibility(View.VISIBLE);
        mKlPoker.setVisibility(View.GONE);
    }

    @Override
    protected String getLayoutId() {
        return Constants.kl_dialog_activity_center;
    }
}
