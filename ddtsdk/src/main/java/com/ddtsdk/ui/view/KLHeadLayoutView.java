package com.ddtsdk.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ddtsdk.constants.AppConstants;

public class KLHeadLayoutView extends RelativeLayout {

    public KLHeadLayoutView(Context context) {
        super(context);
        initView();
    }

    public KLHeadLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KLHeadLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        GradientDrawable drawable = (GradientDrawable) getBackground();
        drawable.setColor(Color.parseColor(AppConstants.colorPrimary));
    }
}
