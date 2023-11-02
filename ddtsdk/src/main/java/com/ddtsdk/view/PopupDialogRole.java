package com.ddtsdk.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.PopMsg;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.utils.CheckApkExistUtils;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.ScreenUtils;

public class PopupDialogRole extends Dialog {

    private Context mContext;
    private View mView;
    private ImageView im_dialog_popup_close_role;
    private ImageView im_dialog_popup_pic_role;
    private String picUrl_role = "";

//	public PopupPicDialog(Context context, int type) {
//		super(context, AppConfig.resourceId(context,
//				"kl_PopupDialog", "style"));
//		// TODO Auto-generated constructor stub
//		init(context, type);
//	}

    public PopupDialogRole(Context context, String url) {
        super(context,AppConfig.resourceId(context,"kl_PopupDialog","style"));
        // TODO Auto-generated constructor stub
        this.picUrl_role = url;
        this.mContext = context;
        this.mView = LayoutInflater.from(context).inflate(
                AppConfig.resourceId(context, "kl_dialog_popup_pic_role", "layout"),
                null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(mView);
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        FrameLayout frameLayout = (FrameLayout) mView.findViewById(AppConfig.resourceId(mContext, "fl_dialog_popup_role", "id"));
//        frameLayout.setBackgroundColor(Color.TRANSPARENT);
        im_dialog_popup_pic_role = (ImageView) mView.findViewById(AppConfig.resourceId(mContext, "im_dialog_popup_pic_role", "id"));
        im_dialog_popup_close_role = (ImageView) mView.findViewById(AppConfig.resourceId(mContext, "im_dialog_popup_close_role", "id"));
        ViewGroup.LayoutParams wvl = frameLayout.getLayoutParams();
        wvl.width = ScreenUtils.getScreenWidth(getContext());
        wvl.height = ScreenUtils.getScreenHeight(getContext());
        ImageUtils.loadImage(im_dialog_popup_pic_role, picUrl_role);
        setListener();
    }

    private void setListener() {
        im_dialog_popup_close_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        im_dialog_popup_pic_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        PlatformWebViewActivity.startThisActivity(getContext(),AppConstants.platformUrl);
                }
            }
        );
    }

}
