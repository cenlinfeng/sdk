package com.ddtsdk.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.ui.activity.MksCommonWebActivity;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.utils.UserInfo;
import com.ddtsdk.utils.Utils;

/**
 * Created by czg on 2020/2/27.
 */

public class KLCommonAffirmDialog extends DialogFragment implements View.OnClickListener {
    private TextView kl_tip_tv;
    private TextView kl_cancel_tv;
    private TextView kl_submit_tv;
    private TextView kl_title_tv;
    private TextView mks_user_agreement_tv;
    private TextView mks_privacy_agreement_tv;
    private CheckBox mks_user_agreement_notice_cb;
    private LinearLayout mks_ll_user_agreement_notice;

    public IAffirmDialogClick mIAffirmDialogClick;
    private String mTitle;
    private String mContent;
    private String mSubmit;
    private String mCancel;
    private CharSequence mSequenceText;
    protected Context mContext;
    private int mType;
    private boolean isChickBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, AppConfig.resourceId(getActivity(),
                "AppTheme_Dialog_FullScreen_Translucent", "style"));
        mType = getArguments().getInt("type");
        isChickBox = getArguments().getBoolean("isChickBox");
    }

    /**
     * @param type 0：取消和确认；1:只有一个确定按钮；
     */
    public static KLCommonAffirmDialog Builder(int type,boolean isChickBox) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putBoolean("isChickBox",isChickBox);
        KLCommonAffirmDialog fragment = new KLCommonAffirmDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (mType == 1 && keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return inflater.inflate(AppConfig.resourceId(getActivity(),
                "kl_dialog_affirm_common", "layout"), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kl_tip_tv = view.findViewById(AppConfig.resourceId(getActivity(),"kl_tip_tv", "id"));
        kl_cancel_tv = view.findViewById(AppConfig.resourceId(getActivity(),"kl_cancel_tv", "id"));
        kl_submit_tv = view.findViewById(AppConfig.resourceId(getActivity(),"kl_submit_tv", "id"));
        kl_title_tv = view.findViewById(AppConfig.resourceId(getActivity(),"kl_title_tv", "id"));
        mks_user_agreement_tv = view.findViewById(AppConfig.resourceId(getActivity(), "mks_tv_user_agreement", "id"));
        mks_privacy_agreement_tv = view.findViewById(AppConfig.resourceId(getActivity(), "mks_tv_privacy_agreement", "id"));
        mks_user_agreement_notice_cb = view.findViewById(AppConfig.resourceId(getActivity(), "mks_cb_user_agreement_notice", "id"));
        mks_ll_user_agreement_notice = view.findViewById(AppConfig.resourceId(getActivity(), "mks_ll_user_agreement_notice", "id"));

        //按钮显示类型
        switch (mType) {
            case 0:
                kl_cancel_tv.setVisibility(View.VISIBLE);
                break;
            case 1:
                kl_cancel_tv.setVisibility(View.GONE);
                break;
        }

        if (isChickBox){
            mks_ll_user_agreement_notice.setVisibility(View.VISIBLE);
        }else {
            mks_user_agreement_notice_cb.setChecked(true);
        }



        kl_cancel_tv.setOnClickListener(this);
        kl_submit_tv.setOnClickListener(this);
        mks_user_agreement_tv.setOnClickListener(this);
        mks_privacy_agreement_tv.setOnClickListener(this);

        if (kl_tip_tv != null && !TextUtils.isEmpty(mContent)) {
            kl_tip_tv.setText(mContent);
        }
        if (kl_title_tv != null && !TextUtils.isEmpty(mTitle)) {
            kl_title_tv.setText(mTitle);
        }
        if (kl_submit_tv != null && !TextUtils.isEmpty(mSubmit)) {
            kl_submit_tv.setText(mSubmit);
        }
        if (kl_cancel_tv != null && !TextUtils.isEmpty(mCancel)) {
            kl_cancel_tv.setText(mCancel);
        }
    }

    public KLCommonAffirmDialog setTitle(String str) {
        mTitle = str;
        if (kl_title_tv != null) {
            kl_title_tv.setText(mTitle);
        }
        return this;
    }

    public KLCommonAffirmDialog setContent(String str) {
        mContent = str;
        if (kl_tip_tv != null) {
            kl_tip_tv.setText(mContent);
        }
        return this;
    }

    public KLCommonAffirmDialog setIAffirmDialogClick(IAffirmDialogClick iAffirmDialogClick) {
        mIAffirmDialogClick = iAffirmDialogClick;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIAffirmDialogClick = null;
    }

    @Override
    public void onClick(View v) {

        if (v == kl_cancel_tv){
            if (mIAffirmDialogClick != null)
                mIAffirmDialogClick.onCancelClick();
            dismiss();
        } else if (v == kl_submit_tv){
            if (mks_user_agreement_notice_cb.isChecked() && mIAffirmDialogClick != null) {
                mIAffirmDialogClick.onOkClick();
                dismiss();
            } else {
                ToastUtils.showShort(mContext, "请阅读并同意用户协议和隐私协议");
            }
        }else if (v == mks_user_agreement_tv) {
            String url = Utils.getUserAgreementUrl(mContext);
            showBaseWebView(mContext, "用户协议", url);
        } else if (v == mks_privacy_agreement_tv) {
            String url = Utils.getPrivacyAgreementUrl(mContext);
            showBaseWebView(mContext, "隐私协议", url);
        }
    }

    public interface IAffirmDialogClick {
        void onOkClick();

        void onCancelClick();
    }

    public KLCommonAffirmDialog setSubmitText(String str) {
        mSubmit = str;
        return this;
    }

    public KLCommonAffirmDialog setCancelText(String str) {
        mCancel = str;
        return this;
    }

    private void showBaseWebView(Context context, String title, String url) {
        LogUtil.i("showBaseWebView" + url);
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            ToastUtils.showShort(KLSDK.getInstance().getContext(), "正在建设...");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.setClass(context, MksCommonWebActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }
}
