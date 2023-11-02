package com.ddtsdk.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.ui.KLViewControl;

public class KLCommonTitleBar extends RelativeLayout implements View.OnClickListener {
    private RelativeLayout rlRoot;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivClose;
    private ImageView ivTitle;
    private View vDivider;


    public KLCommonTitleBar(Context context) {
        this(context, null);
    }

    public KLCommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLCommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        String namespace = "http://schemas.android.com/apk/res-auto";
        String title = attrs.getAttributeValue(namespace, "title");
        boolean showTitle = attrs.getAttributeBooleanValue(namespace, "showTitle", true);
        boolean showBackView = attrs.getAttributeBooleanValue(namespace, "showBackView", true);
        boolean showCloseView = attrs.getAttributeBooleanValue(namespace, "showCloseView", true);
        boolean showImageView = attrs.getAttributeBooleanValue(namespace, "showImageView", false);

        LayoutInflater.from(context).inflate(AppConfig.resourceId(context, "kl_dialog_titlebar", "layout"), this);

        rlRoot = (RelativeLayout) findViewById(AppConfig.resourceId(context, "kl_rl_titlebar", "id"));
        tvTitle = (TextView) findViewById(AppConfig.resourceId(context, "kl_tv_titlebar_title", "id"));
        ivBack = (ImageView) findViewById(AppConfig.resourceId(context, "kl_iv_titlebar_back", "id"));
        ivClose = (ImageView) findViewById(AppConfig.resourceId(context, "kl_iv_titlebar_close", "id"));
        ivTitle = (ImageView) findViewById(AppConfig.resourceId(context, "kl_iv_titlebar_image", "id"));
        vDivider = findViewById(AppConfig.resourceId(context, "kl_v_divider", "id"));

        if (!TextUtils.isEmpty(title) && title.startsWith("@")) {
            int titleResourceId = Integer.parseInt(title.substring(1));
            tvTitle.setText(context.getResources().getString(titleResourceId));
        } else {
            tvTitle.setText(title);
        }


        //各个空间显示or隐藏
        tvTitle.setVisibility(showTitle ? VISIBLE : INVISIBLE);
        ivBack.setVisibility(showBackView ? VISIBLE : GONE);
        ivClose.setVisibility(showCloseView ? VISIBLE : GONE);
        ivTitle.setVisibility(showImageView ? VISIBLE : GONE);

        ivTitle.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    public void hideRightButton() {
        ivClose.setVisibility(INVISIBLE);
    }

    public void hideIVTITLE() {
        ivTitle.setVisibility(GONE);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
        tvTitle.setVisibility(VISIBLE);
    }

    public void setLeftButtonOnClickListener(OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    //    右上关闭按钮回调
    public void setRightButtonOnClickListener(OnClickListener listener) {
        ivClose.setOnClickListener(listener);
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    @Override
    public void onClick(View v) {
        KLViewControl.getInstance().back();
    }
}
