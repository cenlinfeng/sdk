package com.ddtsdk.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.PopMsg;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.web.JsInterface;
import com.ddtsdk.utils.CheckApkExistUtils;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.ScreenUtils;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class PopupPicDialog extends Dialog {
    private Context mContext;
    private View mView;
    private ImageView im_dialog_popup_close;
    private ImageView im_dialog_popup_pic;
    private PopMsg popMsg;
    private int type = 0;
    private String picUrl = "";

//	public PopupPicDialog(Context context, int type) {
//		super(context, AppConfig.resourceId(context,
//				"kl_PopupDialog", "style"));
//		// TODO Auto-generated constructor stub
//		init(context, type);
//	}

    public PopupPicDialog(Context context, String url, int type, PopMsg popMsg) {
        super(context, AppConfig.resourceId(context,
                "kl_PopupDialog", "style"));
        // TODO Auto-generated constructor stub
        this.picUrl = url;
        this.mContext = context;
        this.type = type;
        this.popMsg = popMsg;
        this.mView = LayoutInflater.from(context).inflate(
                AppConfig.resourceId(context, "kl_dialog_popup_pic", "layout"),
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
        FrameLayout frameLayout = (FrameLayout) mView.findViewById(AppConfig.resourceId(mContext, "fl_dialog_popup", "id"));
//        frameLayout.setBackgroundColor(Color.TRANSPARENT);
        im_dialog_popup_pic = (ImageView) mView.findViewById(AppConfig.resourceId(mContext, "im_dialog_popup_pic", "id"));
        im_dialog_popup_close = (ImageView) mView.findViewById(AppConfig.resourceId(mContext, "im_dialog_popup_close", "id"));
        ViewGroup.LayoutParams wvl = frameLayout.getLayoutParams();
        wvl.width = ScreenUtils.getScreenWidth(getContext());
        wvl.height = ScreenUtils.getScreenHeight(getContext());
        ImageUtils.loadImage(im_dialog_popup_pic, picUrl);
        setListener();
    }

    private void setListener() {
        im_dialog_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        im_dialog_popup_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        if (!TextUtils.isEmpty(AppConstants.adv_url)) {
                            Intent intenturl = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(AppConstants.adv_url));
                            mContext.startActivity(intenturl);
                        }
                        else dismiss();
                        break;
                    case 1:
                        if (popMsg == null) {
                            dismiss();
                            break;
                        }
                        if ("0".equals(popMsg.getMtype())) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(popMsg.getUrl()));
                            getContext().startActivity(intent);
                        } else if (popMsg.getPackdownurl() != null && popMsg.getPackdownurl().size() > 0) {
                            boolean hasApk = false;
                            for (int i = 0; i < popMsg.getPackdownurl().size(); i++) {
                                if (CheckApkExistUtils.checkApkExist(getContext(), popMsg.getPackname().get(i))) {
                                    hasApk = true;
                                    Intent it = getContext().getPackageManager().getLaunchIntentForPackage(popMsg.getPackname().get(i));
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mtype", popMsg.getMtype());
                                    if ("4".equals(popMsg.getMtype()))
                                        bundle.putString("mid", popMsg.getUrl());
                                    else bundle.putString("mid", popMsg.getMid());
                                    it.putExtra("box_attach", bundle);
                                    getContext().startActivity(it);
                                    break;
                                }
                            }
                            if (!hasApk){
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(popMsg.getPackdownurl().get(0)));
                                getContext().startActivity(intent);
                            }
                        }
                        else dismiss();
                        break;
                    case 2:
                        AppConstants.source = 1;
                        PlatformWebViewActivity.startThisActivity(getContext(),AppConstants.platformUrl);
                        break;
                }
            }
        });
    }

}
