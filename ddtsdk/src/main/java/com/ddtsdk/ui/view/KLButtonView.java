package com.ddtsdk.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.ddtsdk.constants.AppConstants;

@SuppressLint("AppCompatCustomView")
public class KLButtonView extends Button {

    public KLButtonView(Context context) {
        super(context);
        initView();
    }

    public KLButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KLButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        GradientDrawable drawable = (GradientDrawable) getBackground();
        drawable.setColor(Color.parseColor(AppConstants.colorPrimary));
    }
}
